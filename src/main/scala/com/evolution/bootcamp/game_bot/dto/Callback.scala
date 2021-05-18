package com.evolution.bootcamp.game_bot.dto

import io.circe.generic.JsonCodec

@JsonCodec
final case class Callback(id: Long, message: BotMessage, data: String)
