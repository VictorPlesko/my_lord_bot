package com.evolution.bootcamp.game_bot

import cats.data.EitherT
import cats.effect.concurrent.Ref
import cats.effect.{ExitCode, IO, IOApp}
import com.evolution.bootcamp.game_bot.TelegramBotApi.ChatID
import com.evolution.bootcamp.game_bot.dao.{DbService, DbTransactor}
import com.evolution.bootcamp.game_bot.dao.config.DbConfig
import com.evolution.bootcamp.game_bot.game_logic.GameMoment
import org.http4s.client.blaze.BlazeClientBuilder

import scala.concurrent.ExecutionContext

object App extends IOApp {
  override def run(args: List[String]): IO[ExitCode] = {
    EitherT.apply[IO, io.circe.Error, DbConfig](DbConfig.loadDefaultConfig[IO])
      .flatMap(config => EitherT.liftF[IO, io.circe.Error, Unit](DbTransactor.transactor[IO](config).use { transactor =>
        BlazeClientBuilder[IO](ExecutionContext.global).resource
          .use { client =>
            for {
              ref <- Ref.of[IO, Map[ChatID, GameMoment]](Map.empty[ChatID, GameMoment])
              repo = Repository(ref)
              dbService = DbService(transactor)
              _ <- TelegramBotProcess(client, "1713154808:AAG9IheKlmzxluHFnWrybPQ946phEX_ikMM", dbService,repo).streamUpdates
            } yield ()
          }
      })).value as ExitCode.Success
  }
}
