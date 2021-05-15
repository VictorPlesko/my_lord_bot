package com.evolution.bootcamp.game_bot.dto

import io.circe.generic.JsonCodec

@JsonCodec
final case class BotUpdate(update_id: Int, message: BotMessage)
