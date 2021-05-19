package com.evolution.bootcamp.game_bot

import cats.effect.Sync
import com.evolution.bootcamp.game_bot.TelegramBotApi.ChatID
import com.evolution.bootcamp.game_bot.dto.api.{BotMessage, BotUpdate, Chat}
import com.evolution.bootcamp.game_bot.utils.Messages

sealed trait Command
object Command {
  final case class Start(chatID: ChatID) extends Command
  final case class Stop(chatID: ChatID) extends Command
  final case class Rule(chatID: ChatID) extends Command
  final case class Help(chatID: ChatID) extends Command

  def parseIntoCommand(botUpdate: BotMessage): Option[Command] = {
    botUpdate match {
      case BotMessage(_, Chat(id), command) if command == start => Some(Start(id))
      case BotMessage(_, Chat(id), command) if command == help => Some(Help(id))
      case BotMessage(_, Chat(id), command) if command == stop => Some(Stop(id))
      case BotMessage(_, Chat(id), command) if command == rule => Some(Rule(id))
      case _ => None
    }
  }


  val start = "/start"
  val help = "/help"
  val stop = "/stop"
  val rule = "/rule"
}
