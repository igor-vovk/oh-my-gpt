package org.jetbrains.scala.samples.settings

import com.intellij.openapi.options.Configurable
import org.jetbrains.annotations.Nls

import javax.swing.JComponent

final class AskGptSettingsConfigurable() extends Configurable {

  private var component: AskGptSettingsComponent = _

  @Nls(capitalization = Nls.Capitalization.Title)
  override def getDisplayName: String = "Ask GPT Plugin"

  override def createComponent(): JComponent = {
    component = new AskGptSettingsComponent()

    component.panel
  }

  override def isModified: Boolean = {
    val settings = AskGptSettings.getInstance.getState
    val apiKey   = settings.apiKey

    val currentApiKey = component.getApiKey

    apiKey != currentApiKey
  }

  override def apply(): Unit = {
    val settings = AskGptSettings.getInstance.getState

    settings.apiKey = component.getApiKey
  }

  override def reset(): Unit = {
    val settings = AskGptSettings.getInstance.getState

    component.setApiKey(settings.apiKey)
  }

  override def disposeUIResources(): Unit = {
    component = null
  }
}
