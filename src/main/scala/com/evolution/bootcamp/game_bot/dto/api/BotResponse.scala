package com.evolution.bootcamp.game_bot.dto.api

import io.circe.generic.JsonCodec

@JsonCodec
final case class BotResponse[T](ok: Boolean, result: T)
