package com.evolution.bootcamp.game_bot.dao

import cats.effect.{Blocker, Resource, Sync}
import doobie.Transactor
import doobie.hikari.HikariTransactor
import doobie.util.ExecutionContexts

object DbTransactor {
//  def transactor[F[_] : Sync]: Resource[F, Transactor[F]] = {
//    for {
//      ce <- ExecutionContexts.fixedThreadPool(32)
//      be <- Blocker[F]
//      xa <- HikariTransactor.newHikariTransactor[F](
//        driverClassName =
//      )
//    }
//  }
}
