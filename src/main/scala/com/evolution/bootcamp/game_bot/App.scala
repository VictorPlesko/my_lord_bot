package com.evolution.bootcamp.game_bot

import cats.effect.{ExitCode, IO, IOApp}
import org.http4s.client.blaze.BlazeClientBuilder

import scala.concurrent.ExecutionContext

object App extends IOApp {
  override def run(args: List[String]): IO[ExitCode] =
    BlazeClientBuilder[IO](ExecutionContext.global).resource
      .use {client =>
        TelegramBotApi(client, "1713154808:AAG9IheKlmzxluHFnWrybPQ946phEX_ikMM").getUpdates(0)
          .map(println(_)).as(ExitCode.Success)
      }
}
