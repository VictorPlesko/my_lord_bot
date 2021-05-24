package com.evolution.bootcamp.game_bot.api

import cats.effect.{Clock, IO, Timer}
import cats.effect.concurrent.Ref
import com.evolution.bootcamp.game_bot.Repository
import com.evolution.bootcamp.game_bot.api.Api.{Button, ChatID, MessageID}
import com.evolution.bootcamp.game_bot.dao.Dao
import com.evolution.bootcamp.game_bot.dao.domain.{Effect, Question}
import com.evolution.bootcamp.game_bot.dto.api.{BotMessage, BotResponse, BotUpdate, Chat}
import com.evolution.bootcamp.game_bot.game_logic.GameMoment
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

import scala.concurrent.duration.FiniteDuration

class TelegramBotProcessTest extends AnyFunSuite with Matchers{
  class ApiStub extends Api[IO] {
    override def getUpdates(offset: Int): IO[BotResponse[List[BotUpdate]]] =
      IO.pure(BotResponse(true, List(
        BotUpdate(1,Some(BotMessage(1, Chat(1), "/help")), None),
        BotUpdate(2,Some(BotMessage(1, Chat(1), "/rule")), None),
      )))

    override def sendMessage(chatID: ChatID, text: String, button: Button): IO[BotResponse[BotMessage]] =
      IO.pure(BotResponse(true, BotMessage(1, Chat(1), "hey")))

    override def editMessageReplyMarkup(chatId: ChatID, messageId: MessageID, button: Button): IO[BotResponse[BotMessage]] =
      IO.pure(BotResponse(true, BotMessage(1, Chat(1), "good")))
  }

  class DaoStub extends Dao[IO] {
    override def getRandomQuestion: IO[Question] = IO.pure(Question("ques", Effect(10, -10, 10, -10)))
  }

  implicit val timer: Timer[IO] = new Timer[IO] {
    override def clock: Clock[IO] = ???

    override def sleep(duration: FiniteDuration): IO[Unit] = ???
  }

  test("Method handleCommand processed start command") {
    val botProcess = for {
      offsetRef <- Ref.of[IO, Int](0)
      repoRef <- Ref.of[IO, Map[ChatID, GameMoment]](Map.empty[ChatID, GameMoment])
      repo = Repository(repoRef)
    } yield TelegramBotProcess(new ApiStub, new DaoStub, offsetRef, repo)


  }
}
