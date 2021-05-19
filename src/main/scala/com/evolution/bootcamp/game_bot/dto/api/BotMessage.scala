package com.evolution.bootcamp.game_bot.dto.api

import io.circe.generic.JsonCodec

@JsonCodec
final case class BotMessage(message_id: Integer, chat: Chat, text: String)
