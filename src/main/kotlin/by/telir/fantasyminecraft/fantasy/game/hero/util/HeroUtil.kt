package by.telir.fantasyminecraft.fantasy.game.hero.util

import by.telir.fantasyminecraft.fantasy.game.hero.manager.HeroManager
import by.telir.fantasyminecraft.fantasy.game.hero.type.HeroAttribute

class HeroUtil {
    companion object {
        fun getNames(heroAttribute: HeroAttribute): MutableSet<String> {
            return HeroManager().getConfig(heroAttribute).getKeys(false)
        }
    }
}