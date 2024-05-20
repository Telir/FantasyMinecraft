package by.telir.fantasyminecraft.fantasy.game.hero

import by.telir.fantasyminecraft.fantasy.game.attribute.GameAttribute
import by.telir.fantasyminecraft.fantasy.game.attribute.type.AttributeType
import by.telir.fantasyminecraft.fantasy.game.hero.type.HeroAttribute
import by.telir.fantasyminecraft.fantasy.game.property.GameProperty
import by.telir.fantasyminecraft.fantasy.game.property.type.PropertyType

class GameHero(gameName: String, val mainAttribute: HeroAttribute) {
    val attributes = mutableMapOf<AttributeType, GameAttribute>()
    val properties = mutableMapOf<PropertyType, GameProperty>()
}