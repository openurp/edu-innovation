/*
 * OpenURP, Agile University Resource Planning Solution.
 *
 * Copyright © 2014, The OpenURP Software.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful.
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.openurp.edu.innovation.student.web.action

import java.io.ByteArrayInputStream
import java.time.Instant

import org.beangle.commons.activation.MimeTypes
import org.beangle.commons.lang.Strings
import org.beangle.data.dao.{ EntityDao, OqlBuilder }
import org.beangle.security.Securities
import org.beangle.webmvc.api.action.{ ActionSupport, EntitySupport }
import org.beangle.webmvc.api.view.{ Stream, View }
import org.beangle.webmvc.entity.helper.PopulateHelper
import org.openurp.edu.innovation.model.{ Attachment, Closure, Material, Project, StageType }

import javax.servlet.http.Part

class ClosureAction extends ActionSupport with EntitySupport[Project] {

  var entityDao: EntityDao = _

  def index(): View = {
    val user = Securities.user;
    val query = OqlBuilder.from(classOf[Project], "p")
    query.where("p.manager.std.user.code=:code", user)
    val projects = entityDao.search(query);
    put("projects", projects)
    put("closureStage", new StageType(StageType.Closure))
    forward()
  }

  def closureForm(): View = {
    val projectId = longId("project")
    val project = entityDao.get(classOf[Project], projectId)
    put("project", project)
    val closureQuery = OqlBuilder.from(classOf[Closure], "closure").where("closure.project=:project", project)
    put("closures", entityDao.search(closureQuery))
    put("closureStage", new StageType(StageType.Closure))
    forward()
  }

  def saveClosure(): View = {
    val projectId = longId("project")
    val project = entityDao.get(classOf[Project], projectId)
    if (isIntime(project)) {
      val closureStage = new StageType(StageType.Closure)
      val closure =
        getId("closure", classOf[Long]) match {
          case None     => new Closure(project)
          case Some(id) => entityDao.get(classOf[Closure], id)
        }
      PopulateHelper.populate(closure, classOf[Closure].getName, "closure")
      if (!closure.applyExemptionReply) {
        closure.exemptionReason = null
      }
      val parts = getAll("attachment", classOf[Part])
      if (parts.size > 0 && parts.head.getSize > 0) {
        val material =
          project.materials.find(_.stageType == closureStage) match {
            case None    => new Material(project, closureStage)
            case Some(m) => m
          }
        val attachment = material.attachment
        val part = getAll("attachment", classOf[Part]).head
        val fileName = part.getSubmittedFileName
        val now = Instant.now
        material.fileName = fileName
        material.updatedAt = now
        attachment.merge(Attachment(part.getSubmittedFileName, part.getInputStream))
        entityDao.saveOrUpdate(attachment)
      }
      entityDao.saveOrUpdate(project, closure)
      redirect("index", "保存成功")
    } else {
      redirect("index", "不在时间范围内")
    }
  }

  def attachment(): View = {
    val attachment = entityDao.get(classOf[Attachment], longId("attachment"))
    Stream(new ByteArrayInputStream(attachment.content), decideContentType(attachment.fileName),
      attachment.fileName)
  }

  private def decideContentType(fileName: String): String = {
    MimeTypes.getMimeType(Strings.substringAfterLast(fileName, "."), MimeTypes.ApplicationOctetStream).toString
  }

  private def isIntime(project: Project): Boolean = {
    val closureStage = new StageType(StageType.Closure)
    project.batch.getStage(closureStage) match {
      case None        => false
      case Some(stage) => stage.intime
    }
  }
}
