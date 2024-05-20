package by.telir.fantasyminecraft.fantasy.game.user

import by.telir.fantasyminecraft.fantasy.game.attribute.GameAttribute
import by.telir.fantasyminecraft.fantasy.game.attribute.modifier.AttributeModifier
import by.telir.fantasyminecraft.fantasy.game.attribute.modifier.AttributeModifier.DotaModifier.*
import by.telir.fantasyminecraft.fantasy.game.attribute.modifier.AttributeModifier.OperationType
import by.telir.fantasyminecraft.fantasy.game.attribute.type.AttributeType
import by.telir.fantasyminecraft.fantasy.game.attribute.type.DotaAttribute
import by.telir.fantasyminecraft.fantasy.game.attribute.type.DotaAttribute.*
import by.telir.fantasyminecraft.fantasy.game.attribute.util.AttributeUtil
import by.telir.fantasyminecraft.fantasy.game.effect.Effect
import by.telir.fantasyminecraft.fantasy.game.effect.type.EffectType
import by.telir.fantasyminecraft.fantasy.game.hero.GameHero
import by.telir.fantasyminecraft.fantasy.game.hero.type.HeroAttribute
import by.telir.fantasyminecraft.fantasy.game.item.GameItem
import by.telir.fantasyminecraft.fantasy.game.item.type.ItemType
import by.telir.fantasyminecraft.fantasy.game.item.util.GameItemUtil
import by.telir.fantasyminecraft.fantasy.game.property.GameProperty
import by.telir.fantasyminecraft.fantasy.game.property.subproperty.BlindnessProperty
import by.telir.fantasyminecraft.fantasy.game.property.subproperty.EvasionProperty
import by.telir.fantasyminecraft.fantasy.game.property.type.PropertyType
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import java.util.*
import kotlin.math.max
import kotlin.math.min
import kotlin.math.round

data class User(val uuid: UUID) {

    val player: Player
        get() = Bukkit.getPlayer(uuid)

    var health: Double
        get() = player.health
        set(value) {
            player.health = min(max(0.0, value), attributes[AttributeType.HEALTH]!!.finalValue)
        }


    var mana: Double = 0.0
        set(value) {
            min(max(0.0, value), attributes[AttributeType.MANA]!!.finalValue)
        }

    fun update() {
        createPlayerAttributes()
        createPlayerProperties()
        updateHero()
        updateItems()
        updateEffects()
        updateDotaAttributes()
        updateMinecraftAttributes()
    }

    private fun updateHero() {
        if (hero == null) {
            mainAttribute = null
            return
        }
        properties.putAll(hero!!.properties)
        attributes.putAll(hero!!.attributes)
        mainAttribute = hero!!.mainAttribute
    }

    private fun updateItems() {
        gameItems.clear()
        if (uncheckedGameItems.isEmpty()) return
        for (type in uncheckedGameItems.keys) {
            if (type.playerBased) {
                gameItems[type] = uncheckedGameItems[type]!!.toMutableList()
            } else {
                when (type) {
                    ItemType.WEAPON -> {
                        val itemInMainHand = this.player.inventory.itemInMainHand
                        if (!uncheckedGameItems[type].isNullOrEmpty()) {
                            uncheckedGameItems[type]?.forEach {
                                if (it.itemStack == itemInMainHand) gameItems[type] = mutableListOf(it)
                            }
                        }
                    }

                    ItemType.OFFHAND -> {
                        val itemInOffHand = this.player.inventory.itemInOffHand
                        if (!uncheckedGameItems[type].isNullOrEmpty()) {
                            uncheckedGameItems[type]?.forEach {
                                if (it.itemStack == itemInOffHand) gameItems[type] = mutableListOf(it)
                            }
                        }
                    }

                    else -> {}
                }
            }
        }

        for (itemType in gameItems.keys) {
            if (gameItems[itemType].isNullOrEmpty()) continue
            for (gameItem in gameItems[itemType]!!.iterator()) {
                for (attributeType in gameItem.modifiers.keys) {
                    if (itemType == ItemType.WEAPON) {
                        when (attributeType) {
                            AttributeType.DAMAGE -> {
                                val attributeModifier = gameItem.modifiers[attributeType]!!.copy()
                                attributeModifier.amount -= 1
                                attributes[attributeType]!!.addModifier(attributeModifier)
                            }

                            AttributeType.ATTACK_SPEED -> {
                                val attributeModifier = gameItem.modifiers[attributeType]!!.copy()
                                attributeModifier.amount -= 4
                                attributes[attributeType]!!.addModifier(attributeModifier)
                            }

                            else -> {}
                        }
                    } else attributes[attributeType]!!.addModifier(gameItem.modifiers[attributeType]!!)
                }
            }
        }
    }

    private fun updateEffects() {
        getActiveEffects().forEach { effect ->
            effect.attributeChanges.keys.forEach {
                attributes[it]!!.addModifier(effect.attributeChanges[it]!!)
            }
        }
    }

    private fun updateDotaAttributes() {
        for (attributeType in attributes.keys) {
            for (dotaAttribute in DotaAttribute.entries) {
                if (attributeType != dotaAttribute.attributeType) continue

                val finalValue = attributes[attributeType]!!.finalValue

                val defaultDamageModifier = AttributeModifier(
                    "mainAttribute",
                    round(finalValue),
                    OperationType.ADD,
                    false
                )
                val universalDamageModifier = AttributeModifier(
                    "mainAttribute",
                    round(finalValue) * 0.7,
                    OperationType.ADD,
                    false
                )
                when (dotaAttribute) {
                    STRENGTH -> {
                        val strengthModifier = STRENGTH.dotaModifier as StrengthModifier

                        if (mainAttribute == HeroAttribute.STRENGTH) {
                            attributes[AttributeType.DAMAGE]!!.addModifier(
                                defaultDamageModifier
                            )
                        }
                        if (mainAttribute == HeroAttribute.UNIVERSAL) {
                            attributes[AttributeType.DAMAGE]!!.addModifier(
                                universalDamageModifier
                            )
                        }

                        attributes[AttributeType.HEALTH]!!.addModifier(
                            AttributeModifier(
                                strengthModifier.name,
                                strengthModifier.health * round(finalValue),
                                OperationType.ADD,
                                false
                            )
                        )

                        attributes[AttributeType.HEALTH_REGEN]!!.addModifier(
                            AttributeModifier(
                                strengthModifier.name,
                                strengthModifier.healthRegen * round(finalValue),
                                OperationType.ADD,
                                false
                            )
                        )
                    }

                    AGILITY -> {
                        val agilityModifier = AGILITY.dotaModifier as AgilityModifier

                        if (mainAttribute == HeroAttribute.AGILITY) {
                            attributes[AttributeType.DAMAGE]!!.addModifier(
                                defaultDamageModifier
                            )
                        }
                        if (mainAttribute == HeroAttribute.UNIVERSAL) {
                            attributes[AttributeType.DAMAGE]!!.addModifier(
                                universalDamageModifier
                            )
                        }

                        attributes[AttributeType.DEFENSE]!!.addModifier(
                            AttributeModifier(
                                agilityModifier.name,
                                agilityModifier.defense * round(finalValue),
                                OperationType.ADD,
                                false
                            )
                        )

                        attributes[AttributeType.ATTACK_SPEED]!!.addModifier(
                            AttributeModifier(
                                agilityModifier.name,
                                agilityModifier.attackSpeed * round(finalValue),
                                OperationType.ADD,
                                false
                            )
                        )

                    }

                    INTELLIGENCE -> {
                        val intelligenceModifier = INTELLIGENCE.dotaModifier as IntelligenceModifier

                        if (mainAttribute == HeroAttribute.INTELLIGENCE) {
                            attributes[AttributeType.DAMAGE]!!.addModifier(
                                defaultDamageModifier
                            )
                        }
                        if (mainAttribute == HeroAttribute.UNIVERSAL) {
                            attributes[AttributeType.DAMAGE]!!.addModifier(
                                universalDamageModifier
                            )
                        }

                        attributes[AttributeType.MANA]!!.addModifier(
                            AttributeModifier(
                                intelligenceModifier.name,
                                intelligenceModifier.mana * round(finalValue),
                                OperationType.ADD,
                                false
                            )
                        )

                        attributes[AttributeType.MANA_REGEN]!!.addModifier(
                            AttributeModifier(
                                intelligenceModifier.name,
                                intelligenceModifier.manaRegen * round(finalValue),
                                OperationType.ADD,
                                false
                            )
                        )

                        attributes[AttributeType.MAGIC_RESISTANCE]!!.addModifier(
                            AttributeModifier(
                                intelligenceModifier.name,
                                intelligenceModifier.magicResistance * round(finalValue),
                                OperationType.ADD,
                                false
                            )
                        )
                    }
                }
            }
        }

    }

    private fun updateMinecraftAttributes() {
        for (attributeType in AttributeType.entries) {
            if (attributes[attributeType]!!.attributeType.minecraftAttribute != null) {
                AttributeUtil.setValue(
                    this.player,
                    attributes[attributeType]?.attributeType?.minecraftAttribute!!,
                    attributes[attributeType]!!.finalValue
                )
            }
        }
    }


    private val gameItems = mutableMapOf<ItemType, MutableList<GameItem>>()
    private val uncheckedGameItems = mutableMapOf<ItemType, MutableList<GameItem>>()

    fun addUncheckedItem(gameItem: GameItem, type: ItemType) {
        if (uncheckedGameItems[type].isNullOrEmpty()) uncheckedGameItems[type] = mutableListOf()

        if (type == ItemType.CONSUMABLE) {
            val targetItem: GameItem? = findGameItem(gameItem.gameName, gameItem.itemStack)

            if (targetItem != null) targetItem.amount += gameItem.amount
            else uncheckedGameItems[type]!!.add(gameItem)
        } else uncheckedGameItems[type]!!.add(gameItem)
    }

    fun removeUncheckedItem(gameItem: GameItem) {
        val type = gameItem.itemType

        if (uncheckedGameItems[type].isNullOrEmpty()) uncheckedGameItems[type] = mutableListOf()

        val targetItem: GameItem = findGameItem(gameItem.gameName, gameItem.itemStack) ?: return
        if (type == ItemType.CONSUMABLE) {
            targetItem.amount -= gameItem.amount
            if (targetItem.amount <= 0) uncheckedGameItems[type]!!.remove(gameItem)
        } else uncheckedGameItems[type]!!.remove(gameItem)
    }

    fun findGameItem(gameName: String, itemStack: ItemStack): GameItem? {
        val type = GameItemUtil.getItemType(gameName)!!
        if (uncheckedGameItems[type].isNullOrEmpty()) uncheckedGameItems[type] = mutableListOf()

        var targetGameItem: GameItem? = null
        if (type == ItemType.CONSUMABLE) {
            uncheckedGameItems[type]!!.forEach { if (it.gameName == gameName) targetGameItem = it }
        } else {
            uncheckedGameItems[type]!!.forEach {
                if (it.gameName == gameName && it.itemStack == itemStack) targetGameItem = it
            }
        }
        return targetGameItem
    }

    fun getGameItems(): List<GameItem> {
        return gameItems.values.flatten()
    }

    val attributes = mutableMapOf<AttributeType, GameAttribute>()
    val properties = mutableMapOf<PropertyType, GameProperty>()

    val effects = mutableMapOf<EffectType, MutableSet<Effect>>()

    private fun getActiveEffects(): Set<Effect> {
        return effects.values.flatten().toSet()
    }

    fun addEffect(effect: Effect) {
        if (effects[effect.type] == null) effects[effect.type] = mutableSetOf(effect)
        else effects[effect.type]!!.add(effect)
        update()
    }

    fun removeEffect(effect: Effect) {
        effects[effect.type]!!.remove(effect)
        update()
    }

    private var hero: GameHero? = null
    private var mainAttribute: HeroAttribute? = null

    fun selectHero(gameHero: GameHero) {
        hero = gameHero
        update()
    }


    private fun createPlayerProperties() {
        properties.clear()
        properties[PropertyType.BLINDNESS] = BlindnessProperty(0.0)
        properties[PropertyType.EVASION] = EvasionProperty(0.0)
    }

    private fun createPlayerAttributes() {
        attributes.clear()
        AttributeType.entries.forEach { attributes[it] = GameAttribute(it) }
    }

    init {
        update()
    }
}
