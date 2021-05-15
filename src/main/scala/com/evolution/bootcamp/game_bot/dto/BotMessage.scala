package com.evolution.bootcamp.game_bot.dto

import io.circe.generic.JsonCodec

@JsonCodec
final case class BotMessage(message_id: Integer, chat: Chat, text: String)
