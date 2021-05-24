package com.evolution.bootcamp.game_bot.dao

import com.evolution.bootcamp.game_bot.dao.domain.Question

trait Dao[F[_]] {
  def getRandomQuestion: F[Question]
}
