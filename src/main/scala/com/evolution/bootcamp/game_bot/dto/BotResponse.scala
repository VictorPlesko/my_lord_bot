package com.evolution.bootcamp.game_bot.dto

import io.circe.generic.JsonCodec

@JsonCodec
final case class BotResponse(ok: Boolean, result: List[BotUpdate])
