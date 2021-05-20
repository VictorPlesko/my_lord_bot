package com.evolution.bootcamp.game_bot

import cats.data.OptionT
import cats.effect.{Sync, Timer}
import cats.implicits._
import com.evolution.bootcamp.game_bot.TelegramBotApi.{Button, ChatID, Token}
import com.evolution.bootcamp.game_bot.dao.DbService
import com.evolution.bootcamp.game_bot.dto.api.BotUpdate
import com.evolution.bootcamp.game_bot.game_logic.Command.{noResponse, yesResponse}
import com.evolution.bootcamp.game_bot.game_logic._
import com.evolution.bootcamp.game_bot.utils.Messages
import org.http4s.client.Client

import scala.concurrent.duration._

final case class TelegramBotProcess[F[_] : Sync : Timer](client: Client[F],
                                                         token: Token,
                                                         dbService: DbService[F],
                                                         gameRepo: Repository[F, ChatID, GameMoment]) {
  val api: TelegramBotApi[F] = TelegramBotApi(client, token)
  var offset = 0

  implicit val gameSettings: GameSettings = GameSettings(ReligionSettings(), ArmySettings(), PeopleSettings(), CofferSettings())

  val button: Button = List(List(InlineKeyboardButton("Да", yesResponse), InlineKeyboardButton("Нет", noResponse)))

  def process(update: BotUpdate): F[Unit] = {
    offset = update.update_id + 1
    val command = Command.parseIntoCommand(update)
    command.fold(Sync[F].unit)(handleCommand)
  }
  //    update.callback_query.fold(Sync[F].delay(println("None")))(p => Sync[F].delay(println(p))) *>
  //    update.message.fold(Sync[F].delay(BotResponse(false,BotMessage(1,Chat(1), "fdsf"))))(mes => api.sendMessage(mes.chat.id, "Hello!"))


  def streamUpdates: F[Unit] = {
    fs2.Stream
      .awakeDelay[F](1.second)
      .evalMap(_ => api.getUpdates(offset))
      .flatMap(response => fs2.Stream.emits(response.result))
      .evalMap(process)
      .compile
      .drain
  }

  private def handleCommand(command: Command): F[Unit] = {
    command match {
      case Command.Start(chatID) =>
        for {
          _ <- api.sendMessage(chatID, Messages.startMessage).void
          question <- dbService.getRandomQuestion
          gameMoment = GameMoment.default(question)
          _ <- gameRepo.put(chatID, gameMoment)
          _ <- api.sendMessage(chatID, Messages.effectInfoMessage(gameMoment.currentEffect) + "\n" + question.value, button).void
        } yield ()
      case Command.Continue(chatID, action) =>
        (for {
          oldGameMoment <- OptionT(gameRepo.get(chatID))
          question <- OptionT.liftF(dbService.getRandomQuestion)
          _ <-
            OptionT.liftF(oldGameMoment.update(action, Some(question)) match {
              case Left(gameEvents) =>
                gameRepo.delete(chatID) *>
                  api.sendMessage(chatID, gameEvents.effect).void
              case Right(gameMoment) =>
                api.sendMessage(chatID, Messages.effectInfoMessage(gameMoment.currentEffect) + "\n" + question.value, button) *>
                  gameRepo.update(chatID, gameMoment)
            })
        } yield ()).value.void
      case Command.Stop(chatID) =>
        for {
          _ <- gameRepo.delete(chatID)
          _ <- api.sendMessage(chatID, Messages.stopMessage)
        } yield ()
      case Command.Rule(chatID) => api.sendMessage(chatID, Messages.ruleMessage).void
      case Command.Help(chatID) => api.sendMessage(chatID, Messages.helpMessage).void
    }
  }
}
