package com.evolution.bootcamp.game_bot

final case class ReligionSettings(minReligion: Int = 0, maxReligion: Int = 100)
final case class ArmySettings(minArmy: Int = 0, maxArmy: Int = 100)
final case class PeopleSettings(minPeople: Int = 0, maxPeople: Int = 100)
final case class CofferSettings(minCoffer: Int = 0, maxCoffer: Int = 100)


final case class GameSettings(religionSettings: ReligionSettings,
                              armySettings: ArmySettings,
                              peopleSettings: PeopleSettings,
                              cofferSettings: CofferSettings)
