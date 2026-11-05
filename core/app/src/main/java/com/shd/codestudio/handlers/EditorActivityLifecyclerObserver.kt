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

package com.shd.codestudio.handlers

import android.content.Context
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.shd.codestudio.eventbus.events.Event
import com.shd.codestudio.eventbus.events.EventReceiver
import com.shd.codestudio.eventbus.events.editor.OnCreateEvent
import com.shd.codestudio.eventbus.events.editor.OnDestroyEvent
import com.shd.codestudio.eventbus.events.editor.OnPauseEvent
import com.shd.codestudio.eventbus.events.editor.OnResumeEvent
import com.shd.codestudio.eventbus.events.editor.OnStartEvent
import com.shd.codestudio.eventbus.events.editor.OnStopEvent
import com.shd.codestudio.projects.internal.ProjectManagerImpl
import com.shd.codestudio.projects.util.BootClasspathProvider
import com.shd.codestudio.utils.EditorActivityActions
import com.shd.codestudio.utils.EditorSidebarActions
import com.shd.codestudio.utils.Environment
import java.util.concurrent.CompletableFuture
import org.greenrobot.eventbus.EventBus

/**
 * Observes lifecycle events if [com.shd.codestudio.EditorActivityKt].
 *
 * @author Akash Yadav
 */
class EditorActivityLifecyclerObserver : DefaultLifecycleObserver {

  private val fileActionsHandler = FileTreeActionHandler()

  override fun onCreate(owner: LifecycleOwner) {
    EditorActivityActions.register(owner as Context)
    EditorSidebarActions.registerActions(owner as Context)
    dispatchEvent(OnCreateEvent())
  }

  override fun onStart(owner: LifecycleOwner) {
    CompletableFuture.runAsync(this::initBootclasspathProvider)
    register(fileActionsHandler, ProjectManagerImpl.getInstance())

    dispatchEvent(OnStartEvent())
  }

  override fun onResume(owner: LifecycleOwner) {
    EditorActivityActions.register(owner as Context)
    dispatchEvent(OnResumeEvent())
  }

  override fun onPause(owner: LifecycleOwner) {
    EditorActivityActions.clear()
    dispatchEvent(OnPauseEvent())
  }

  override fun onStop(owner: LifecycleOwner) {
    unregister(fileActionsHandler, ProjectManagerImpl.getInstance())
    dispatchEvent(OnStopEvent())
  }

  override fun onDestroy(owner: LifecycleOwner) {
    dispatchEvent(OnDestroyEvent())
  }

  private fun register(vararg receivers: EventReceiver) {
    receivers.forEach { it.register() }
  }

  private fun unregister(vararg receivers: EventReceiver) {
    receivers.forEach { it.unregister() }
  }

  private fun dispatchEvent(event: Event) {
    EventBus.getDefault().post(event)
  }

  private fun initBootclasspathProvider() {
    BootClasspathProvider.update(listOf(Environment.ANDROID_JAR.absolutePath))
  }
}
