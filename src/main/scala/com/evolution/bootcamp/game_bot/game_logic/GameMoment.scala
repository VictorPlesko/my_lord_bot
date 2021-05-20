package com.evolution.bootcamp.game_bot.game_logic

import com.evolution.bootcamp.game_bot.dao.domain.{Effect, Question}
import com.evolution.bootcamp.game_bot.game_logic.GameEvents._

sealed trait GameEvents {
  def effect: String
}
object GameEvents {
  case object LotOfReligion extends GameEvents {
    override def effect: String = "На ваших землях религии стало так много, что она решила захватить власть."
  }
  case object LittleReligion extends GameEvents {
    override def effect: String = "Вы нерелигиозный правитель и инквизиция решила вас сжечь."
  }
  case object LotOfArmy extends GameEvents {
    override def effect: String = "Армия захватила власть, а вас казнила на главной площади."
  }
  case object LittleArmy extends GameEvents {
    override def effect: String = "На ваших землях стало слишком мало армии и шайка разбойников захватила город."
  }
  case object LotOfPeople extends GameEvents {
    override def effect: String = "Вы решили выйти к народу, а он из-за огромной любви ринулся к вам и случайно задавил."
  }
  case object LittlePeople extends GameEvents {
    override def effect: String = "Народ так обозлился на вас, что решил захватить власть."
  }
  case object LotOfCoffer extends GameEvents {
    override def effect: String = "Вас задавило горой денег."
  }
  case object LittleCoffer extends GameEvents {
    override def effect: String = "Король решил казнить вас за неуплату налогов."
  }
}

sealed abstract case class GameMoment private(question: Option[Question], currentEffect: Effect) {
  def update(response: Boolean, newGameQuestion: Option[Question])(implicit gameSettings: GameSettings): Either[GameEvents, GameMoment] = {
    question.fold[Either[GameEvents, GameMoment]](Right(this)) { q =>
      if (response) {
        val newEffect = Effect(currentEffect.religion + q.effect.religion,
          currentEffect.army + q.effect.army,
          currentEffect.people + q.effect.people,
          currentEffect.coffers + q.effect.coffers)
        GameMoment.of(newGameQuestion, newEffect)
      } else {
        val newEffect = Effect(currentEffect.religion - q.effect.religion,
          currentEffect.army - q.effect.army,
          currentEffect.people - q.effect.people,
          currentEffect.coffers - q.effect.coffers)
        GameMoment.of(newGameQuestion, newEffect)
      }
    }
  }
}
object GameMoment {
  def of(question: Option[Question], currentEffect: Effect)(implicit gameSettings: GameSettings): Either[GameEvents, GameMoment] = {
    if (currentEffect.religion <= gameSettings.religionSettings.minReligion) Left(LittleReligion)
    else if (currentEffect.religion >= gameSettings.religionSettings.maxReligion) Left(LotOfReligion)
    else if (currentEffect.army <= gameSettings.armySettings.minArmy) Left(LittleArmy)
    else if (currentEffect.army >= gameSettings.armySettings.maxArmy) Left(LotOfArmy)
    else if (currentEffect.people <= gameSettings.peopleSettings.minPeople) Left(LittlePeople)
    else if (currentEffect.people >= gameSettings.peopleSettings.maxPeople) Left(LotOfPeople)
    else if (currentEffect.coffers <= gameSettings.cofferSettings.minCoffer) Left(LittleCoffer)
    else if (currentEffect.coffers >= gameSettings.cofferSettings.maxCoffer) Left(LotOfCoffer)
    else Right(new GameMoment(question, currentEffect) {})
  }

  def default(question: Question): GameMoment = new GameMoment(Some(question), Effect(50, 50, 50, 50)) {}
}
