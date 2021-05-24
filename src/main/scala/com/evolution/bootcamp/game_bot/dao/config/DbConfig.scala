package com.evolution.bootcamp.game_bot.dao.config

import cats.effect.Sync
import pureconfig.generic.auto._
import pureconfig.{ConfigReader, ConfigSource}

final case class DbConfig(dbDriverName: String, dbUrl: String, dbUser: String, dbPwd: String)

object DbConfig {
  def loadDefaultConfig[F[_] : Sync]: F[ConfigReader.Result[DbConfig]] = {
    Sync[F].delay(ConfigSource.default.load[DbConfig])
  }
}
