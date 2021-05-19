package com.evolution.bootcamp.game_bot

import com.evolution.bootcamp.game_bot.GameEvents.{LittleArmy, LittleCoffer, LittlePeople, LittleReligion, LotOfArmy, LotOfCoffer, LotOfPeople, LotOfReligion}
import com.evolution.bootcamp.game_bot.dao.domain.{Effect, Question}

sealed trait GameEvents
object GameEvents {
  case object LotOfReligion extends GameEvents
  case object LittleReligion extends GameEvents
  case object LotOfArmy extends GameEvents
  case object LittleArmy extends GameEvents
  case object LotOfPeople extends GameEvents
  case object LittlePeople extends GameEvents
  case object LotOfCoffer extends GameEvents
  case object LittleCoffer extends GameEvents
}

sealed abstract case class GameMoment private(question: Option[Question], currentEffect: Effect) {
  def update(response: Boolean)(implicit gameSettings: GameSettings): Either[GameEvents, GameMoment] = {
    question.fold[Either[GameEvents, GameMoment]](Right(this)) { newQuestion =>
      if (response) {
        val newEffect = Effect(currentEffect.religion + newQuestion.effect.religion,
          currentEffect.army + newQuestion.effect.army,
          currentEffect.people + newQuestion.effect.people,
          currentEffect.coffers + newQuestion.effect.coffers)
        GameMoment.of(None, newEffect)
      } else {
        val newEffect = Effect(currentEffect.religion - newQuestion.effect.religion,
          currentEffect.army - newQuestion.effect.army,
          currentEffect.people - newQuestion.effect.people,
          currentEffect.coffers - newQuestion.effect.coffers)
        GameMoment.of(None, newEffect)
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
