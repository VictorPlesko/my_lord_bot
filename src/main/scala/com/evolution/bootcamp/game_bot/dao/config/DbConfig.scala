package com.evolution.bootcamp.game_bot.dao.config

import cats.effect.{Resource, Sync}
import io.circe.generic.JsonCodec
import io.circe.parser.decode

import scala.io.Source

@JsonCodec
final case class DbConfig(dbDriverName: String, dbUrl: String, dbUser: String, dbPwd: String)

object DbConfig {
  def loadDefaultConfig[F[_] : Sync]: F[Either[io.circe.Error, DbConfig]] = {
    Resource
      .fromAutoCloseable(Sync[F].delay(Source.fromFile("src/main/resources/db/db_config.json")))
      .use { source =>
        Sync[F].delay(decode[DbConfig](source.mkString))
      }
  }
}
