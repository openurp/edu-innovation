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
package org.openurp.edu.innovation.model

import org.beangle.data.orm.{IdGenerator, MappingModule}

class DefaultMapping extends MappingModule {

  def binding(): Unit = {
    defaultIdGenerator(IdGenerator.AutoIncrement)
    defaultCache("openurp.innovation", "read-write")

    bind[Member]

    bind[Project] declare { e =>
      e.members is depends("project")
      e.materials is depends("project")
      e.levels is depends("project")
      e.reviews is depends("project")
      e.instructors is ordered
    }

    bind[Intro] declare { e =>
      e.summary is length(500)
      e.innovation is length(300)
      e.product is length(300)
    }

    bind[ProjectCategory].cacheable()
    bind[ProjectLevel].cacheable()
    bind[ProjectState].cacheable()
    bind[LevelJounal]

    bind[Batch].declare { e =>
      e.stages is depends("batch")
    }.cacheable()

    bind[Stage].cacheable()

    bind[StageType].declare { e =>
      e.children is depends("parent")
    }.cacheable()

    bind[Section].declare { e =>
      e.children is depends("parent")
      e.name is length(100)
      e.remark is length(100)
    }

    bind[Template].declare { e =>
      e.sections is depends("template")
    }

    bind[Material].declare { e =>
      e.fileName is length(200)
    }

    bind[Attachment].declare { e =>
      e.content is lob
      e.fileName is length(200)
    }

    bind[Closure].declare { e =>
      e.exemptionReason is length(200)
    }

    bind[Expert].declare { e =>
      e.name is length(50)
      e.code is length(20)
      e.intro is length(300)
    }

    bind[ReviewGroup]

    bind[Review].declare { e =>
      e.comments is length(600)
    }
  }
}
