package org.jetbrains.scala.samples.openai

import com.intellij.openapi.diagnostic.Logger
import com.theokanning.openai.completion.chat.{ChatCompletionRequest, ChatMessage}
import com.theokanning.openai.service.OpenAiService

import scala.jdk.CollectionConverters._

object OpenAIClient {
  private val Model = "gpt-3.5-turbo"

  private val RoleSystem = "system"

  private val RoleUser = "user"

  case class PerformActionResponse(code: String)
}

class OpenAIClient(token: String) {

  import OpenAIClient._

  private val logger = Logger.getInstance(classOf[OpenAIClient])

  private val service = new OpenAiService(token)

  def performAction(code: String, extension: String, actionPrompt: String): PerformActionResponse = {
    val request = ChatCompletionRequest.builder()
      .model(Model)
      .messages(
        List(
          new ChatMessage(
            RoleSystem,
            "You are an assistant helping an engineer to write code. " +
              "The engineer selects a code and then asks to perform an action. " +
              "Please perform the action and return the code. Return only the code. " +
              "Pay attention to the tabs and spaces." +
              "Wrap the code in ``` to format it properly."
          ),
          new ChatMessage(
            RoleUser,
            s"""
               |The code is:
               |```$extension
               |$code
               |```
               |The action prompt is:
               |$actionPrompt
               |""".stripMargin
          ),
        ).asJava
      )
      .build()

    logger.info(">> Sending request to ChatGPT!")
    logger.info(request.toString)

    val result = service.createChatCompletion(request)

    logger.info("<< Got response from ChatGPT!")
    logger.info(result.toString)

    val content = result.getChoices.get(0).getMessage.getContent

    PerformActionResponse(
      content.substring(content.indexOf("```") + 3, content.lastIndexOf("```"))
    )
  }

}
