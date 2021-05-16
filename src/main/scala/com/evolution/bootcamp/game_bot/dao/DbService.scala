package com.evolution.bootcamp.game_bot.dao

import cats.effect.Bracket
import com.evolution.bootcamp.game_bot.dao.domain.Question
import doobie.Transactor
import doobie.implicits._

final case class DbService[F[_] : Bracket[*[_], Throwable]](private val xa: Transactor[F]) {
  def getRandomQuestion: F[Question] = {
    val sqlForRandomQuestion = sql"SELECT q.value, e.religion, e.army, e.people, e.coffers FROM question q INNER JOIN effect e ON q.effect = e.id ORDER BY rand() LIMIT 1;"
    sqlForRandomQuestion.query[Question].unique.transact(xa)
  }
}
