package by.telir.fantasyminecraft.fantasy.game.hero.manager

import by.telir.fantasyminecraft.fantasy.game.hero.GameHero
import by.telir.fantasyminecraft.fantasy.game.hero.type.HeroAttribute
import by.telir.fantasyminecraft.fantasy.game.user.User
import by.telir.fantasyminecraft.fantasy.reader.FantasyConfigReader
import by.telir.fantasyminecraft.pluginutil.config.ConfigUtil
import org.bukkit.configuration.file.FileConfiguration

class HeroManager {
    companion object {
        private val STRENGTH = ConfigUtil.getConfig("strengthHeroes")
        private val AGILITY = ConfigUtil.getConfig("agilityHeroes")
        private val INTELLIGENCE = ConfigUtil.getConfig("intelligenceHeroes")
        private val UNIVERSAL = ConfigUtil.getConfig("universalHeroes")
    }
    fun select(user: User, gameName: String, heroAttribute: HeroAttribute) {
        val hero = createHero(gameName, heroAttribute) ?: throw RuntimeException("Illegal gameName")
        user.selectHero(hero)
    }

    private fun createHero(gameName: String, heroAttribute: HeroAttribute): GameHero? {
        if (!inConfig(gameName, heroAttribute)) return null

        val heroSection = getConfig(heroAttribute).getConfigurationSection(gameName)

        val gameHero = GameHero(gameName, heroAttribute)

        gameHero.attributes.putAll(FantasyConfigReader(heroSection).checkForAttributes())
        gameHero.properties.putAll(FantasyConfigReader(heroSection).checkForProperties())

        return gameHero
    }

    private fun inConfig(gameName: String, heroAttribute: HeroAttribute): Boolean {
        return getConfig(heroAttribute).getKeys(false).contains(gameName)
    }

    fun getConfig(heroAttribute: HeroAttribute): FileConfiguration {
        return when (heroAttribute) {
            HeroAttribute.STRENGTH -> STRENGTH
            HeroAttribute.AGILITY -> AGILITY
            HeroAttribute.INTELLIGENCE -> INTELLIGENCE
            HeroAttribute.UNIVERSAL -> UNIVERSAL
        }
    }
}