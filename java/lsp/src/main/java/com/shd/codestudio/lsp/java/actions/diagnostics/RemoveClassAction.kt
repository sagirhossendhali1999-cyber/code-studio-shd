/*
 *  This file is part of AndroidIDE.
 *
 *  AndroidIDE is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  AndroidIDE is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *   along with AndroidIDE.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.shd.codestudio.lsp.java.actions.diagnostics

import com.shd.codestudio.actions.ActionData
import com.shd.codestudio.actions.hasRequiredData
import com.shd.codestudio.actions.markInvisible
import com.shd.codestudio.actions.requireFile
import com.shd.codestudio.actions.requirePath
import com.shd.codestudio.lsp.java.JavaCompilerProvider
import com.shd.codestudio.lsp.java.actions.BaseJavaCodeAction
import com.shd.codestudio.lsp.java.models.DiagnosticCode
import com.shd.codestudio.lsp.java.rewrite.RemoveClass
import com.shd.codestudio.lsp.java.utils.CodeActionUtils.findPosition
import com.shd.codestudio.projects.IProjectManager
import com.shd.codestudio.resources.R
import org.slf4j.LoggerFactory

/** @author Akash Yadav */
class RemoveClassAction : BaseJavaCodeAction() {

  override val id: String = "ide.editor.lsp.java.diagnostics.removeClass"
  override var label: String = ""
  private val diagnosticCode = DiagnosticCode.UNUSED_CLASS.id

  override val titleTextRes: Int = R.string.action_remove_class

  companion object {

    private val log = LoggerFactory.getLogger(RemoveClassAction::class.java)
  }

  override fun prepare(data: ActionData) {
    super.prepare(data)

    if (!visible || !data.hasRequiredData(com.shd.codestudio.lsp.models.DiagnosticItem::class.java)) {
      markInvisible()
      return
    }

    val diagnostic = data[com.shd.codestudio.lsp.models.DiagnosticItem::class.java]!!
    if (diagnosticCode != diagnostic.code) {
      markInvisible()
      return
    }
  }

  override suspend fun execAction(data: ActionData): Any {
    val diagnostic = data[com.shd.codestudio.lsp.models.DiagnosticItem::class.java]!!
    val compiler =
        JavaCompilerProvider.get(
            IProjectManager.getInstance()
                .getWorkspace()
                ?.findModuleForFile(data.requireFile(), false) ?: return Any()
        )
    val file = data.requirePath()

    return compiler.compile(file).get {
      RemoveClass(file, findPosition(it, diagnostic.range.start))
    }
  }

  override fun postExec(data: ActionData, result: Any) {
    if (result !is RemoveClass) {
      log.warn("Unable to remove class")
      return
    }

    performCodeAction(data, result)
  }
}
