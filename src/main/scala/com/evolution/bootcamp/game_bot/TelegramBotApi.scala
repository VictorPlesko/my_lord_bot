package com.evolution.bootcamp.game_bot

import cats.effect.Sync
import com.evolution.bootcamp.game_bot.TelegramBotApi.Token
import com.evolution.bootcamp.game_bot.dto.BotResponse
import org.http4s.{EntityDecoder, Uri}
import org.http4s.circe.jsonOf
import org.http4s.client._
import org.http4s.implicits._
import org.http4s.syntax._

final case class TelegramBotApi[F[_] : Sync](client: Client[F], token: Token) {
  val botApiUri: Uri = uri"https://api.telegram.org" / s"bot$token"
  implicit val decoderBotResponse: EntityDecoder[F, BotResponse] = jsonOf[F,BotResponse]

  def getUpdates(offset: Int): F[BotResponse] = {
    val uri = (botApiUri / "getUpdates")
      .withQueryParam("offset", offset)
    client.expect[BotResponse](uri)
  }
}
object TelegramBotApi {
  type Token = String
}
