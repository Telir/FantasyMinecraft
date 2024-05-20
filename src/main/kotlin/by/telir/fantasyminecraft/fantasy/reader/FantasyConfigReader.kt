package by.telir.fantasyminecraft.fantasy.reader

import by.telir.fantasyminecraft.fantasy.game.active.GameActive
import by.telir.fantasyminecraft.fantasy.game.active.subactive.HealthRestoreActive
import by.telir.fantasyminecraft.fantasy.game.active.subactive.ManaRestoringActive
import by.telir.fantasyminecraft.fantasy.game.active.subactive.ReturnDamageUpActive
import by.telir.fantasyminecraft.fantasy.game.active.type.ActiveType
import by.telir.fantasyminecraft.fantasy.game.attribute.GameAttribute
import by.telir.fantasyminecraft.fantasy.game.attribute.modifier.AttributeModifier
import by.telir.fantasyminecraft.fantasy.game.attribute.type.AttributeType
import by.telir.fantasyminecraft.fantasy.game.damage.type.DamageType
import by.telir.fantasyminecraft.fantasy.game.effect.subeffect.BleedingEffect
import by.telir.fantasyminecraft.fantasy.game.effect.subeffect.SlownessEffect
import by.telir.fantasyminecraft.fantasy.game.property.GameProperty
import by.telir.fantasyminecraft.fantasy.game.property.subproperty.*
import by.telir.fantasyminecraft.fantasy.game.property.type.PropertyType
import org.bukkit.configuration.ConfigurationSection

class FantasyConfigReader(private val section: ConfigurationSection) {
    fun checkForProperties(): MutableMap<PropertyType, GameProperty> {
        val properties = HashMap<PropertyType, GameProperty>()

        if (!section.contains("property")) return properties
        val section = section.getConfigurationSection("property")

        for (key in section.getKeys(false)) {
            when (key) {
                "crit" -> {
                    val gameProperty = ModifyOutDamageProperty(section.getDouble("crit.chance", 1.0))
                    gameProperty.isCrit = true
                    gameProperty.percent = section.getDouble("crit.value")
                    properties[gameProperty.type] = gameProperty
                }

                "evasion" -> {
                    val gameProperty = EvasionProperty(section.getDouble("evasion.chance", 1.0))
                    properties[gameProperty.type] = gameProperty
                }

                "blindness" -> {
                    val gameProperty = BlindnessProperty(section.getDouble("blindness.chance", 1.0))
                    properties[gameProperty.type] = gameProperty
                }

                "modifyOut" -> {
                    val gameProperty =
                        ModifyOutDamageProperty(section.getDouble("modifyOut.chance", 1.0))
                    gameProperty.amount = section.getDouble("modifyOut.amount")
                    gameProperty.percent = section.getDouble("modifyOut.value")
                    properties[gameProperty.type] = gameProperty
                }

                "modifyInc" -> {
                    val gameProperty =
                        ModifyIncDamageProperty(section.getDouble("modifyInc.chance", 1.0))
                    gameProperty.amount = section.getDouble("modifyInc.amount")
                    gameProperty.percent = section.getDouble("modifyInc.value")
                    properties[gameProperty.type] = gameProperty
                }

                "lifesteal" -> {
                    val gameProperty =
                        LifestealProperty(section.getDouble("lifesteal.chance", 1.0))
                    gameProperty.amount = section.getDouble("lifesteal.amount")
                    gameProperty.percent = section.getDouble("lifesteal.value")
                    gameProperty.amplifier = section.getDouble("lifesteal.amplifier")
                    gameProperty.damagePercent = section.getDouble("lifesteal.damage.value")
                    gameProperty.damageType = DamageType.valueOf(section.getString("lifesteal.damage.type").uppercase())
                    gameProperty.enemyHealthPercent = section.getDouble("lifesteal.enemyHealthValue")
                    gameProperty.enemyMaxHealthPercent = section.getDouble("lifesteal.enemyMaxHealthValue")
                    properties[gameProperty.type] = gameProperty
                }

                "trueStrike" -> {
                    val gameProperty =
                        TrueStrikeProperty(section.getDouble("trueStrike.property", 1.0))
                    gameProperty.damageAmount = section.getDouble("lifesteal.damage.amount")
                    gameProperty.damageType = DamageType.valueOf(section.getString("lifesteal.damage.type").uppercase())
                    properties[gameProperty.type] = gameProperty
                }

                "returnDamage" -> {
                    val gameProperty =
                        ReturnDamageProperty(section.getDouble("returnDamage.chance", 1.0))
                    gameProperty.amount = section.getDouble("returnDamage.amount")
                    gameProperty.percent = section.getDouble("returnDamage.value")
                    gameProperty.isReflect = section.getBoolean("returnDamage.isReflect")
                    properties[gameProperty.type] = gameProperty
                }

                "bleedingHit" -> {
                    val gameProperty =
                        HitEffectProperty(section.getDouble("bleedingHit.chance", 1.0))
                    val bleedingEffect = BleedingEffect(
                        section.getDouble("bleedingHit.duration"),
                        section.getDouble("bleedingHit.period")
                    ).apply {
                        amount = section.getDouble("bleedingHit.amount")
                        value = section.getDouble("bleedingHit.value")
                    }
                    gameProperty.effect = bleedingEffect

                    properties[gameProperty.type] = gameProperty
                }

                "slownessHit" -> {
                    val gameProperty =
                        HitEffectProperty(section.getDouble("slownessHit.chance", 1.0))
                    val slownessEffect = SlownessEffect(
                        section.getDouble("slownessHit.duration"),
                    ).apply {
                        amount = section.getDouble("slownessHit.amount")
                        value = section.getDouble("slownessHit.value")
                    }
                    gameProperty.effect = slownessEffect

                    properties[gameProperty.type] = gameProperty
                }
            }
        }
        return properties
    }

    fun checkForModifiers(sourceName: String): MutableMap<AttributeType, AttributeModifier> {
        val modifiers = mutableMapOf<AttributeType, AttributeModifier>()

        if (!section.contains("attribute")) return modifiers
        val section = section.getConfigurationSection("attribute")

        for (key in section.getKeys(false)) {
            when (key) {
                "damage" -> {
                    modifiers[AttributeType.DAMAGE] = AttributeModifier(
                        sourceName,
                        section.getDouble("damage.amount"),
                        AttributeModifier.OperationType.ADD,
                        section.getBoolean("damage.isUnstackable")
                    )
                }

                "damagePercent" -> {
                    modifiers[AttributeType.DAMAGE] = AttributeModifier(
                        sourceName,
                        section.getDouble("damagePercent.amount"),
                        AttributeModifier.OperationType.SCALAR,
                        section.getBoolean("damagePercent.isUnstackable")
                    )
                }

                "speed" -> {
                    modifiers[AttributeType.MOVEMENT_SPEED] = AttributeModifier(
                        sourceName,
                        section.getDouble("speed.amount"),
                        AttributeModifier.OperationType.ADD,
                        section.getBoolean("speed.isUnstackable")
                    )
                }


                "speedPercent" -> {
                    modifiers[AttributeType.MOVEMENT_SPEED] = AttributeModifier(
                        sourceName,
                        section.getDouble("speedPercent.amount"),
                        AttributeModifier.OperationType.SCALAR,
                        section.getBoolean("speedPercent.isUnstackable")
                    )
                }

                "attackSpeed" -> {
                    modifiers[AttributeType.ATTACK_SPEED] = AttributeModifier(
                        sourceName,
                        section.getDouble("attackSpeed.amount"),
                        AttributeModifier.OperationType.ADD,
                        section.getBoolean("attackSpeed.isUnstackable")
                    )
                }

                "health" -> {
                    modifiers[AttributeType.HEALTH] = AttributeModifier(
                        sourceName,
                        section.getDouble("health.amount"),
                        AttributeModifier.OperationType.ADD,
                        section.getBoolean("health.isUnstackable")
                    )
                }

                "healthRegen" -> {
                    modifiers[AttributeType.HEALTH_REGEN] = AttributeModifier(
                        sourceName,
                        section.getDouble("healthRegen.amount"),
                        AttributeModifier.OperationType.ADD,
                        section.getBoolean("healthRegen.isUnstackable")
                    )
                }

                "healthRegenAmp" -> {
                    modifiers[AttributeType.HEALTH_REGEN] = AttributeModifier(
                        sourceName,
                        section.getDouble("healthRegenAmp.amount"),
                        AttributeModifier.OperationType.SCALAR,
                        section.getBoolean("healthRegenAmp.isUnstackable")
                    )
                }

                "mana" -> {
                    modifiers[AttributeType.MANA] = AttributeModifier(
                        sourceName,
                        section.getDouble("mana.amount"),
                        AttributeModifier.OperationType.ADD,
                        section.getBoolean("mana.isUnstackable")
                    )
                }

                "manaRegen" -> {
                    modifiers[AttributeType.MANA_REGEN] = AttributeModifier(
                        sourceName,
                        section.getDouble("manaRegen.amount"),
                        AttributeModifier.OperationType.ADD,
                        section.getBoolean("manaRegen.isUnstackable")
                    )
                }

                "manaRegenAmp" -> {
                    modifiers[AttributeType.MANA_REGEN] = AttributeModifier(
                        sourceName,
                        section.getDouble("manaRegenAmp.amount"),
                        AttributeModifier.OperationType.SCALAR,
                        section.getBoolean("manaRegenAmp.isUnstackable")
                    )
                }


                "strength" -> {
                    modifiers[AttributeType.STRENGTH] = AttributeModifier(
                        sourceName,
                        section.getDouble("strength.amount"),
                        AttributeModifier.OperationType.ADD,
                        section.getBoolean("strength.isUnstackable")
                    )
                }

                "agility" -> {
                    modifiers[AttributeType.AGILITY] = AttributeModifier(
                        sourceName,
                        section.getDouble("agility.amount"),
                        AttributeModifier.OperationType.ADD,
                        section.getBoolean("agility.isUnstackable")
                    )
                }

                "intelligence" -> {
                    modifiers[AttributeType.INTELLIGENCE] = AttributeModifier(
                        sourceName,
                        section.getDouble("intelligence.amount"),
                        AttributeModifier.OperationType.ADD,
                        section.getBoolean("intelligence.isUnstackable")
                    )
                }
            }
        }
        return modifiers
    }

    fun checkForAttributes(): MutableMap<AttributeType, GameAttribute> {
        val attributes = mutableMapOf<AttributeType, GameAttribute>()

        if (!section.contains("attribute")) return attributes
        val section = section.getConfigurationSection("attribute")

        for (key in section.getKeys(false)) {
            when (key) {
                "damage" -> {
                    val gameAttribute = GameAttribute(AttributeType.DAMAGE)
                    gameAttribute.baseValue = section.getDouble("damage")
                    attributes[AttributeType.DAMAGE] = gameAttribute
                }

                "attackSpeed" -> {
                    val gameAttribute = GameAttribute(AttributeType.ATTACK_SPEED)
                    gameAttribute.baseValue = section.getDouble("attackSpeed")
                    attributes[AttributeType.ATTACK_SPEED] = gameAttribute
                }

                "moveSpeed" -> {
                    val gameAttribute = GameAttribute(AttributeType.MOVEMENT_SPEED)
                    gameAttribute.baseValue = section.getDouble("moveSpeed")
                    attributes[AttributeType.MOVEMENT_SPEED] = gameAttribute
                }

                "defense" -> {
                    val gameAttribute = GameAttribute(AttributeType.DEFENSE)
                    gameAttribute.baseValue = section.getDouble("defense")
                    attributes[AttributeType.DEFENSE] = gameAttribute
                }

                "toughness" -> {
                    val gameAttribute = GameAttribute(AttributeType.TOUGHNESS)
                    gameAttribute.baseValue = section.getDouble("toughness")
                    attributes[AttributeType.TOUGHNESS] = gameAttribute
                }

                "health" -> {
                    val gameAttribute = GameAttribute(AttributeType.HEALTH)
                    gameAttribute.baseValue = section.getDouble("health")
                    attributes[AttributeType.HEALTH] = gameAttribute
                }

                "healthRegen" -> {
                    val gameAttribute = GameAttribute(AttributeType.HEALTH_REGEN)
                    gameAttribute.baseValue = section.getDouble("healthRegen")
                    attributes[AttributeType.HEALTH_REGEN] = gameAttribute
                }

                "mana" -> {
                    val gameAttribute = GameAttribute(AttributeType.MANA)
                    gameAttribute.baseValue = section.getDouble("mana")
                    attributes[AttributeType.MANA] = gameAttribute
                }

                "manaRegen" -> {
                    val gameAttribute = GameAttribute(AttributeType.MANA_REGEN)
                    gameAttribute.baseValue = section.getDouble("manaRegen")
                    attributes[AttributeType.MANA_REGEN] = gameAttribute
                }

                "magicResistance" -> {
                    val gameAttribute = GameAttribute(AttributeType.MAGIC_RESISTANCE)
                    gameAttribute.baseValue = section.getDouble("magicResistance")
                    attributes[AttributeType.MAGIC_RESISTANCE] = gameAttribute
                }

                "strength" -> {
                    val gameAttribute = GameAttribute(AttributeType.STRENGTH)
                    gameAttribute.baseValue = section.getDouble("strength")
                    attributes[AttributeType.STRENGTH] = gameAttribute
                }

                "agility" -> {
                    val gameAttribute = GameAttribute(AttributeType.AGILITY)
                    gameAttribute.baseValue = section.getDouble("agility")
                    attributes[AttributeType.AGILITY] = gameAttribute
                }

                "intelligence" -> {
                    val gameAttribute = GameAttribute(AttributeType.INTELLIGENCE)
                    gameAttribute.baseValue = section.getDouble("intelligence")
                    attributes[AttributeType.INTELLIGENCE] = gameAttribute
                }
            }
        }
        return attributes
    }

    fun checkForActives(): MutableMap<ActiveType, GameActive> {
        val actives = mutableMapOf<ActiveType, GameActive>()

        if (!section.contains("active")) return actives
        val section = section.getConfigurationSection("active")

        for (key in section.getKeys(false)) {
            when (key) {
                "healthRestore" -> {
                    val active = HealthRestoreActive(section.getDouble("healthRestore.cooldown"))
                    active.amount = section.getDouble("healthRestore.amount")
                    active.percent = section.getDouble("healthRestore.percent")
                    active.manaCost = section.getDouble("healthRestore.manaCost")
                    actives[ActiveType.HEALTH_RESTORE] = active
                }

                "manaRestore" -> {
                    val active = ManaRestoringActive(section.getDouble("manaRestore.cooldown"))
                    active.amount = section.getDouble("manaRestore.amount")
                    active.percent = section.getDouble("manaRestore.percent")
                    active.healthCost = section.getDouble("manaRestore.healthCost")
                    actives[ActiveType.MANA_RESTORE] = active
                }

                "returnDamageUp" -> {
                    val active = ReturnDamageUpActive(section.getDouble("returnDamageUp.cooldown"))
                    val property = ReturnDamageProperty(section.getDouble("returnDamageUp.chance"))

                    property.amount = section.getDouble("returnDamageUp.amount")
                    property.percent = section.getDouble("returnDamageUp.percent")
                    property.isReflect = section.getBoolean("returnDamageUp.isReflect")

                    active.newProperty = property
                    actives[ActiveType.RETURN_DAMAGE_UP] = active
                }
            }
        }
        return actives
    }
}