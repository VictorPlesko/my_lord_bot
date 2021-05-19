package com.evolution.bootcamp.game_bot

import cats.effect.Sync
import com.evolution.bootcamp.game_bot.TelegramBotApi.{ChatID, Token}
import com.evolution.bootcamp.game_bot.dto.api.{BotMessage, BotResponse, BotUpdate}
import io.circe.generic.JsonCodec
import io.circe.syntax._
import org.http4s.circe.jsonOf
import org.http4s.client._
import org.http4s.implicits._
import org.http4s.{EntityDecoder, QueryParamEncoder, QueryParameterValue, Uri}

@JsonCodec
final case class InlineKeyboardButton(text: String, callback_data: String)

@JsonCodec
final case class KeyboardButton(text: String)

final case class TelegramBotApi[F[_] : Sync](client: Client[F], token: Token) {
  val botApiUri: Uri = uri"https://api.telegram.org" / s"bot$token"
  implicit val decoderBotResponseForUpdate: EntityDecoder[F, BotResponse[List[BotUpdate]]] = jsonOf[F, BotResponse[List[BotUpdate]]]
  implicit val decoderBotForMessage: EntityDecoder[F, BotResponse[BotMessage]] = jsonOf[F, BotResponse[BotMessage]]

  type Button = List[List[InlineKeyboardButton]]
  implicit val encoderForButton: QueryParamEncoder[Button] =
    (value: Button) => QueryParameterValue(s"""{"inline_keyboard":${value.asJson}}""")
  //  implicit val encoderForButton1: QueryParamEncoder[List[List[KeyboardButton]]] =
  //    (value: List[List[KeyboardButton]]) => QueryParameterValue(s"""{"keyboard":${value.asJson}}""")

  def getUpdates(offset: Int): F[BotResponse[List[BotUpdate]]] = {
    val uri = (botApiUri / "getUpdates")
      .withQueryParam("offset", offset)
    client.expect[BotResponse[List[BotUpdate]]](uri)
  }

  def sendMessage(chatID: ChatID, text: String): F[BotResponse[BotMessage]] = {
    val r: Button =
      List(
        List(InlineKeyboardButton("hi1!", "sps1"), InlineKeyboardButton("hi2!", "sps2")),
        List(InlineKeyboardButton("hi3!", "sps3"), InlineKeyboardButton("hi3!", "sps3")))
    //    val r: List[List[KeyboardButton]] =
    //      List(
    //        List(KeyboardButton("hi1!"), KeyboardButton("hi2!")),
    //        List(KeyboardButton("hi3!"), KeyboardButton("hi3!")))
    val uri = (botApiUri / "sendMessage")
      .withQueryParam("chat_id", chatID)
      .withQueryParam("text", text)
      .withQueryParam[Button, String]("reply_markup", r)
    client.expect[BotResponse[BotMessage]](uri)
  }
}
object TelegramBotApi {
  type Token = String
  type ChatID = Int
}
