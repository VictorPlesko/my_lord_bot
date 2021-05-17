package com.evolution.bootcamp.game_bot

import cats.effect.{Sync, Timer}
import com.evolution.bootcamp.game_bot.TelegramBotApi.Token
import com.evolution.bootcamp.game_bot.dto.{BotResponse, BotUpdate}
import org.http4s.client.Client

import scala.concurrent.duration._

final case class TelegramBotProcess[F[_]: Sync : Timer](client: Client[F], token: Token) {
  val api: TelegramBotApi[F] = TelegramBotApi(client, token)

  var offset = 0

  def process(update: BotUpdate): Unit = {
    offset = update.update_id + 1
    println(update.message)
  }

  def streamUpdates: F[Unit] = {
    fs2.Stream
      .awakeDelay[F](1.second)
      .evalMap(_ => api.getUpdates(offset))
      .flatMap(response => fs2.Stream.emits(response.result))
      .map(process)
      .compile
      .drain
  }
}
