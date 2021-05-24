package com.evolution.bootcamp.game_bot

import cats.effect.concurrent.Ref
import cats.effect.{ExitCode, IO, IOApp}
import com.evolution.bootcamp.game_bot.api.Api.ChatID
import com.evolution.bootcamp.game_bot.api.TelegramBotProcess
import com.evolution.bootcamp.game_bot.api.impl.TelegramBotApi
import com.evolution.bootcamp.game_bot.dao.DbTransactor
import com.evolution.bootcamp.game_bot.dao.config.DbConfig
import com.evolution.bootcamp.game_bot.dao.impl.QuestionDaoImpl
import com.evolution.bootcamp.game_bot.game_logic.GameMoment
import org.http4s.client.blaze.BlazeClientBuilder

import scala.concurrent.ExecutionContext

object App extends IOApp {
  override def run(args: List[String]): IO[ExitCode] = {
    DbConfig.loadDefaultConfig[IO]
      .flatMap(_.fold(err => IO(println(err)), config => DbTransactor.transactor[IO](config).use { transactor =>
        BlazeClientBuilder[IO](ExecutionContext.global).resource
          .use { client =>
            for {
              ref <- Ref.of[IO, Map[ChatID, GameMoment]](Map.empty[ChatID, GameMoment])
              repo = Repository(ref)
              dbService = QuestionDaoImpl(transactor)
              api = TelegramBotApi(client,"1713154808:AAG9IheKlmzxluHFnWrybPQ946phEX_ikMM")
              offsetRef <- Ref.of[IO, Int](0)
              _ <- new TelegramBotProcess[IO](api, dbService,offsetRef,repo).streamUpdates
            } yield ()
          }
      })) as ExitCode.Success
  }
}
