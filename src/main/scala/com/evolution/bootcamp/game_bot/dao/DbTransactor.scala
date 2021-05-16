package com.evolution.bootcamp.game_bot.dao

import cats.effect.{Async, Blocker, ContextShift, Resource}
import com.evolution.bootcamp.game_bot.dao.config.DbConfig
import doobie.Transactor
import doobie.hikari.HikariTransactor
import doobie.util.ExecutionContexts

object DbTransactor {
  def transactor[F[_] : Async : ContextShift](dbConfig: DbConfig): Resource[F, Transactor[F]] = {
    for {
      ce <- ExecutionContexts.fixedThreadPool(32)
      be <- Blocker[F]
      xa <- HikariTransactor.newHikariTransactor[F](
        driverClassName = dbConfig.dbDriverName,
        url = dbConfig.dbUrl,
        user = dbConfig.dbUser,
        pass = dbConfig.dbPwd,
        connectEC = ce,
        blocker = be
      )
    } yield xa
  }
}
