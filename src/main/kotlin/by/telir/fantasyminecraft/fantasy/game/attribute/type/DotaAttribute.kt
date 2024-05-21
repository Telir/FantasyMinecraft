package by.telir.fantasyminecraft.fantasy.game.attribute.type

import by.telir.fantasyminecraft.fantasy.game.attribute.modifier.AttributeModifier.DotaModifier
import by.telir.fantasyminecraft.fantasy.game.attribute.modifier.AttributeModifier.DotaModifier.*
import by.telir.fantasyminecraft.fantasy.game.attribute.modifier.AttributeModifier.DotaModifier.AgilityModifier.Companion.agilityCfg
import by.telir.fantasyminecraft.fantasy.game.attribute.modifier.AttributeModifier.DotaModifier.IntelligenceModifier.Companion.intelligenceCfg
import by.telir.fantasyminecraft.fantasy.game.attribute.modifier.AttributeModifier.DotaModifier.StrengthModifier.Companion.strengthCfg

enum class DotaAttribute(val attributeType: AttributeType, val dotaModifier: DotaModifier) {
    STRENGTH(
        AttributeType.STRENGTH, StrengthModifier(
            "strength",
            strengthCfg.getDouble("health"),
            strengthCfg.getDouble("healthRegen")
        )
    ),
    AGILITY(
        AttributeType.AGILITY, AgilityModifier(
            "agility",
            agilityCfg.getDouble("defense"),
            agilityCfg.getDouble("attackSpeed"),
        )
    ),
    INTELLIGENCE(
        AttributeType.INTELLIGENCE, IntelligenceModifier(
            "intelligence",
            intelligenceCfg.getDouble("mana"),
            intelligenceCfg.getDouble("manaRegen"),
            intelligenceCfg.getDouble("magicResistance")
        )
    ),
}