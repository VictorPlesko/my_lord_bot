package com.evolution.bootcamp.game_bot.api

import com.evolution.bootcamp.game_bot.api.Api.{Button, ChatID, MessageID}
import com.evolution.bootcamp.game_bot.api.impl.InlineKeyboardButton
import com.evolution.bootcamp.game_bot.dto.api.{BotMessage, BotResponse, BotUpdate}

trait Api[F[_]] {
  def getUpdates(offset: Int): F[BotResponse[List[BotUpdate]]]
  def sendMessage(chatID: ChatID, text: String, button: Button = Nil): F[BotResponse[BotMessage]]
  def editMessageReplyMarkup(chatId: ChatID, messageId: MessageID, button: Button = Nil): F[BotResponse[BotMessage]]
}
object Api {
  type Token = String
  type ChatID = Int
  type MessageID = Int
  type Button = List[List[InlineKeyboardButton]]
}
