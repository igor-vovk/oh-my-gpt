// Copyright 2000-2020 JetBrains s.r.o. and other contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.

package org.jetbrains.scala.samples.services

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.Service
import org.jetbrains.scala.samples.openai.OpenAIClient
import org.jetbrains.scala.samples.settings.AskGptSettings

@Service
final class ApplicationHelloService {
  val openAiClient = new OpenAIClient(AskGptSettings.getInstance.getState.apiKey)
}

object ApplicationHelloService {
  def instance: ApplicationHelloService =
    ApplicationManager.getApplication.getService(classOf[ApplicationHelloService])
}
