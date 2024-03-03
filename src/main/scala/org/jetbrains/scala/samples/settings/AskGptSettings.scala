package org.jetbrains.scala.samples.settings

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.{PersistentStateComponent, Service, State, Storage}
import com.intellij.util.xmlb.XmlSerializerUtil

/**
 * [[https://plugins.jetbrains.com/docs/intellij/persisting-state-of-components.html#customizing-the-xml-format-of-persisted-values]]
 */
@Service
@State(
  name = "org.jetbrains.scala.samples.settings.MyState",
  storages = Array(new Storage("AskGptPluginState.xml"))
)
final class AskGptSettings extends PersistentStateComponent[MyState] {

  private val state = new MyState

  override def getState: MyState = state

  override def loadState(state: MyState): Unit = XmlSerializerUtil.copyBean(state, this.state)
}

object AskGptSettings {
  def getInstance: AskGptSettings = ApplicationManager.getApplication.getService(classOf[AskGptSettings])
}