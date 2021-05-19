package com.evolution.bootcamp.game_bot.utils

object Messages {
  val helpMessage: String =
    """
      | С помощью данного бота можно убить чуть-чуть времени.
      | Для получения правил игры воспользуетесь командой - /rule
      | Для начала игры воспользуетесь командой - /start
      | Для остановки игры воспользуетесь командой - /stop
      | Для получения помощи воспользуетесь командой - /help
      | Приятной игры ❤
      |""".stripMargin

  val ruleMessage: String =
    """
      | Правила игры:
      | Представьте, что вы граф некоторого королевства. К вам приходят подданные прося разные милости для себя или ваших земель. У вас есть 4 основных ресурса (религия, армия, народ и казна), на которые Вы влияете своими решениями. Суть игры состоит в том, чтобы держать данные ресурсы в равновесии (от 0 до 100 единиц). На старте вы получаете ровно половину от ваших ресурсов. Удачной игры!
      |""".stripMargin

  val startMessage: String = "Король Франциск III назначил Вас графом Южных земель. Не успев осознать, что вы граф, к вам приходит множество подданных по разным вопросам."

  val stopMessage: String = "Игра остановлена."
}