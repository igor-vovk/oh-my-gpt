package org.jetbrains.scala.samples.settings

import com.intellij.util.ui.FormBuilder

import javax.swing.{JPanel, JTextField}

class AskGptSettingsComponent {

  private val apiKeyText = new JTextField()

  val panel: JPanel = FormBuilder.createFormBuilder()
    .addLabeledComponent("OpenAI API Key", apiKeyText, 1, false)
    .addComponentFillVertically(new JPanel(), 0)
    .getPanel

  def getApiKey: String = apiKeyText.getText

  def setApiKey(apiKey: String): Unit = apiKeyText.setText(apiKey)

}
