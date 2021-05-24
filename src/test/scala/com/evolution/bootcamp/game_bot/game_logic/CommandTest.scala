package com.evolution.bootcamp.game_bot.game_logic

import com.evolution.bootcamp.game_bot.dto.api.{BotMessage, BotUpdate, Callback, Chat}
import com.evolution.bootcamp.game_bot.game_logic.Command.{Continue, Help, Rule, Start, Stop, stop}
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class CommandTest extends AnyFunSuite with Matchers {
  test("Command.parseIntoCommand works correctly") {
    val startUpdate = BotUpdate(1, Some(BotMessage(1, Chat(1), "/start")), None)
    val helpUpdate = BotUpdate(1, Some(BotMessage(1, Chat(1), "/help")), None)
    val stopUpdate = BotUpdate(1, Some(BotMessage(1, Chat(1), "/stop")), None)
    val ruleUpdate = BotUpdate(1, Some(BotMessage(1, Chat(1), "/rule")), None)
    val continueYesUpdate = BotUpdate(1, None, Some(Callback(1, BotMessage(1,Chat(1), ""), "yes")))
    val continueNoUpdate = BotUpdate(1, None, Some(Callback(1, BotMessage(1,Chat(1), ""), "no")))
    val errorUpdate = BotUpdate(1, Some(BotMessage(1, Chat(1), "hello")), None)

    Command.parseIntoCommand(startUpdate) should be (Some(Start(1)))
    Command.parseIntoCommand(helpUpdate) should be (Some(Help(1)))
    Command.parseIntoCommand(stopUpdate) should be (Some(Stop(1)))
    Command.parseIntoCommand(ruleUpdate) should be (Some(Rule(1)))
    Command.parseIntoCommand(continueYesUpdate) should be (Some(Continue(1,1,true)))
    Command.parseIntoCommand(continueNoUpdate) should be (Some(Continue(1,1,false)))
    Command.parseIntoCommand(errorUpdate) should be (None)
  }
}
