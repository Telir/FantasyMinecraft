package by.telir.fantasyminecraft.fantasy.game.attribute.type

import by.telir.fantasyminecraft.fantasy.game.attribute.modifier.AttributeModifier.DotaModifier
import by.telir.fantasyminecraft.fantasy.game.attribute.modifier.AttributeModifier.DotaModifier.*
import by.telir.fantasyminecraft.fantasy.game.attribute.modifier.AttributeModifier.DotaModifier.AgilityModifier.Companion.AGILITY_CFG
import by.telir.fantasyminecraft.fantasy.game.attribute.modifier.AttributeModifier.DotaModifier.IntelligenceModifier.Companion.INTELLIGENCE_CFG
import by.telir.fantasyminecraft.fantasy.game.attribute.modifier.AttributeModifier.DotaModifier.StrengthModifier.Companion.STRENGTH_CFG

enum class DotaAttribute(val attributeType: AttributeType, val dotaModifier: DotaModifier) {
    STRENGTH(
        AttributeType.STRENGTH, StrengthModifier(
            "strength",
            STRENGTH_CFG.getDouble("health"),
            STRENGTH_CFG.getDouble("healthRegen")
        )
    ),
    AGILITY(
        AttributeType.AGILITY, AgilityModifier(
            "agility",
            AGILITY_CFG.getDouble("defense"),
            AGILITY_CFG.getDouble("attackSpeed"),
        )
    ),
    INTELLIGENCE(
        AttributeType.INTELLIGENCE, IntelligenceModifier(
            "intelligence",
            INTELLIGENCE_CFG.getDouble("mana"),
            INTELLIGENCE_CFG.getDouble("manaRegen"),
            INTELLIGENCE_CFG.getDouble("magicResistance")
        )
    ),
}