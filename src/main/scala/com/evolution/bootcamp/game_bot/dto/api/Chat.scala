package com.evolution.bootcamp.game_bot.dto.api

import com.evolution.bootcamp.game_bot.api.Api.ChatID
import io.circe.generic.JsonCodec

@JsonCodec
final case class Chat(id: ChatID)
