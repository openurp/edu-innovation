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
package org.openurp.edu.innovation.web

import org.beangle.cdi.bind.BindModule
import org.openurp.edu.innovation.web.action.BatchAction
import org.openurp.edu.innovation.web.action.ClosureAction
import org.openurp.edu.innovation.web.action.ExpertAction
import org.openurp.edu.innovation.web.action.ProjectAction
import org.openurp.edu.innovation.web.action.LessonAction

class DefaultModule extends BindModule {

  protected override def binding() {
    bind(classOf[ProjectAction], classOf[BatchAction], classOf[ClosureAction], classOf[ExpertAction],classOf[LessonAction])
  }
}
