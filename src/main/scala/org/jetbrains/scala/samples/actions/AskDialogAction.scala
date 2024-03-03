// Copyright 2000-2020 JetBrains s.r.o. and other contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package org.jetbrains.scala.samples.actions

import com.intellij.notification.{Notification, NotificationType, Notifications}
import com.intellij.openapi.actionSystem.{ActionUpdateThread, AnAction, AnActionEvent, CommonDataKeys}
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.progress.{ProgressIndicator, ProgressManager, Task}
import com.intellij.openapi.ui.Messages
import org.jetbrains.scala.samples.openai.OpenAIClient.PerformActionResponse
import org.jetbrains.scala.samples.services.ApplicationHelloService

object AskDialogAction {

  private val logger = Logger.getInstance(classOf[AskDialogAction])
}

class AskDialogAction extends AnAction("Ask ChatGPT!") {

  /**
   * Gives the user feedback when the dynamic action menu is chosen.
   * Pops a simple message dialog.
   *
   * @param event Event received when the associated menu item is chosen.
   */
  override def actionPerformed(event: AnActionEvent): Unit = { // Using the event, create and show a dialog
    val project      = event.getProject
    val editor       = event.getData(CommonDataKeys.EDITOR)
    val document     = editor.getDocument
    val file         = event.getData(CommonDataKeys.PSI_FILE)
    val primaryCaret = editor.getCaretModel.getPrimaryCaret
    val start        = primaryCaret.getSelectionStart
    val end          = primaryCaret.getSelectionEnd

    val command = Messages.showInputDialog(
      project,
      "Please enter a command",
      "",
      Messages.getInformationIcon
    )
    if (command == null) return

    val response = ProgressManager.getInstance()
      .run(new Task.WithResult[PerformActionResponse, Exception](project, "Contacting ChatGPT...", true) {
        override def compute(indicator: ProgressIndicator): PerformActionResponse = {
          val apiClient     = ApplicationHelloService.instance.openAiClient
          val selectedText  = document.getText.substring(start, end)
          val fileExtension = file.getFileType.getDefaultExtension

          try apiClient.performAction(selectedText, fileExtension, command) catch {
            case e: Exception =>
              Notifications.Bus.notify(
                new Notification(
                  "ChatGPT",
                  "Error communicating OpenAI API",
                  "An error occurred while contacting OpenAI: " + e.getMessage,
                  NotificationType.ERROR
                )
              )

              throw e
          }
        }
      })

    WriteCommandAction.runWriteCommandAction(project, new Runnable() {
      override def run(): Unit = {
        document.replaceString(start, end, response.code)
      }
    })

    primaryCaret.removeSelection()
  }

  /**
   * Determines whether this menu item is available for the current context.
   * Requires a project to be open.
   *
   * @param event Event received when the associated group-id menu is chosen.
   */
  override def update(event: AnActionEvent): Unit = { // Set the availability based on whether a project is open
    val project = event.getProject
    val editor  = event.getData(CommonDataKeys.EDITOR)

    event.getPresentation.setEnabledAndVisible {
      project != null && editor != null && editor.getSelectionModel.hasSelection()
    }
  }

  override def getActionUpdateThread: ActionUpdateThread = ActionUpdateThread.EDT
}
