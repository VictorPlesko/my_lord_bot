package com.evolution.bootcamp.game_bot.api.impl

import cats.effect.Sync
import com.evolution.bootcamp.game_bot.api.Api
import com.evolution.bootcamp.game_bot.api.Api.{Button, ChatID, MessageID, Token}
import com.evolution.bootcamp.game_bot.dto.api.{BotMessage, BotResponse, BotUpdate}
import io.circe.generic.JsonCodec
import io.circe.syntax._
import org.http4s.circe.CirceEntityDecoder._
import org.http4s.client._
import org.http4s.implicits._
import org.http4s.{QueryParamEncoder, QueryParameterValue, Uri}

@JsonCodec
final case class InlineKeyboardButton(text: String, callback_data: String)

@JsonCodec
final case class KeyboardButton(text: String)

final case class TelegramBotApi[F[_] : Sync](client: Client[F],
                                             token: Token) extends Api[F] {
  val botApiUri: Uri = uri"https://api.telegram.org" / s"bot$token"

  implicit val encoderForButton: QueryParamEncoder[Button] =
    (value: Button) => QueryParameterValue(s"""{"inline_keyboard":${value.asJson}}""")

  def getUpdates(offset: Int): F[BotResponse[List[BotUpdate]]] = {
    val uri = (botApiUri / "getUpdates")
      .withQueryParam("offset", offset)
    client.expect[BotResponse[List[BotUpdate]]](uri)
  }

  def sendMessage(chatID: ChatID, text: String, button: Button = Nil): F[BotResponse[BotMessage]] = {
    val uri = (botApiUri / "sendMessage")
      .withQueryParam("chat_id", chatID)
      .withQueryParam("text", text)
      .withQueryParam[Button, String]("reply_markup", button)
    client.expect[BotResponse[BotMessage]](uri)
  }

  def editMessageReplyMarkup(chatId: ChatID, messageId: MessageID, button: Button = Nil): F[BotResponse[BotMessage]] = {
    val uri = (botApiUri / "editMessageReplyMarkup")
      .withQueryParam("chat_id", chatId)
      .withQueryParam("message_id", messageId)
      .withQueryParam[Button, String]("reply_markup", button)
    client.expect[BotResponse[BotMessage]](uri)
  }
}
