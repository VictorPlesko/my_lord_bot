package com.evolution.bootcamp.game_bot.game_logic

import com.evolution.bootcamp.game_bot.TelegramBotApi.{ChatID, MessageID}
import com.evolution.bootcamp.game_bot.dto.api.{BotMessage, BotUpdate, Callback, Chat}

sealed trait Command
object Command {
  final case class Start(chatID: ChatID) extends Command
  final case class Stop(chatID: ChatID, messageId: MessageID) extends Command
  final case class Continue(chatID: ChatID, messageId: MessageID, action: Boolean) extends Command
  final case class Rule(chatID: ChatID) extends Command
  final case class Help(chatID: ChatID) extends Command

  def parseIntoCommand(botUpdate: BotUpdate): Option[Command] = {
    botUpdate match {
      case BotUpdate(_, Some(BotMessage(_, Chat(id), command)), None) if command == start => Some(Start(id))
      case BotUpdate(_, Some(BotMessage(_, Chat(id), command)), None) if command == help => Some(Help(id))
      case BotUpdate(_, Some(BotMessage(messageID, Chat(chatID), command)), None) if command == stop => Some(Stop(chatID, messageID))
      case BotUpdate(_, Some(BotMessage(_, Chat(id), command)), None) if command == rule => Some(Rule(id))
      case BotUpdate(_, None, Some(Callback(_, BotMessage(messageID, Chat(chatID), _), data))) =>
        if (data == yesResponse) Some(Continue(chatID, messageID, action = true)) else Some(Continue(chatID, messageID, action = false))
      case _ => None
    }
  }

  val yesResponse = "yes"
  val noResponse = "no"
  val start = "/start"
  val help = "/help"
  val stop = "/stop"
  val rule = "/rule"
}
