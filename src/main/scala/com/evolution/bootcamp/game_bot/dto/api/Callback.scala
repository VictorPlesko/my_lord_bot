package com.evolution.bootcamp.game_bot.dto.api

import io.circe.generic.JsonCodec

@JsonCodec
final case class Callback(id: Long, message: BotMessage, data: String)
