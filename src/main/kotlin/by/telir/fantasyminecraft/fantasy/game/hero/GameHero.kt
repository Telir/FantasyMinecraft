package by.telir.fantasyminecraft.fantasy.game.hero

import by.telir.fantasyminecraft.fantasy.game.attribute.GameAttribute
import by.telir.fantasyminecraft.fantasy.game.attribute.modifier.AttributeModifier
import by.telir.fantasyminecraft.fantasy.game.attribute.type.AttributeType
import by.telir.fantasyminecraft.fantasy.game.attribute.type.DotaAttribute
import by.telir.fantasyminecraft.fantasy.game.hero.type.HeroAttribute
import by.telir.fantasyminecraft.fantasy.game.property.GameProperty
import by.telir.fantasyminecraft.fantasy.game.property.type.GamePropertyType

class GameHero(gameName: String, mainAttribute: HeroAttribute) {
    val attributes = mutableMapOf<AttributeType, GameAttribute>()
    val properties = mutableMapOf<GamePropertyType, GameProperty>()
}