package com.evolution.bootcamp.game_bot.dto.api

import io.circe.generic.JsonCodec

@JsonCodec
final case class BotUpdate(update_id: Int, message: Option[BotMessage], callback_query: Option[Callback])
