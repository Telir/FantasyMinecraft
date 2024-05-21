package by.telir.fantasyminecraft.fantasy.game.user

import by.telir.fantasyminecraft.fantasy.game.attribute.GameAttribute
import by.telir.fantasyminecraft.fantasy.game.attribute.modifier.AttributeModifier
import by.telir.fantasyminecraft.fantasy.game.attribute.modifier.AttributeModifier.DotaModifier.*
import by.telir.fantasyminecraft.fantasy.game.attribute.modifier.AttributeModifier.OperationType
import by.telir.fantasyminecraft.fantasy.game.attribute.type.AttributeType
import by.telir.fantasyminecraft.fantasy.game.attribute.type.AttributeType.*
import by.telir.fantasyminecraft.fantasy.game.attribute.type.DotaAttribute
import by.telir.fantasyminecraft.fantasy.game.attribute.type.DotaAttribute.AGILITY
import by.telir.fantasyminecraft.fantasy.game.attribute.type.DotaAttribute.INTELLIGENCE
import by.telir.fantasyminecraft.fantasy.game.attribute.type.DotaAttribute.STRENGTH
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
import java.lang.System.currentTimeMillis
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
            player.health = min(max(0.0, value), attributes[HEALTH]!!.finalValue)
        }

    var mana: Double = 0.0
        set(value) {
            field = min(max(0.0, value), attributes[MANA]!!.finalValue)
        }

    private var oldHealthPercent = 0.0
    private var oldManaPercent = 0.0

    fun update() {
        val maxHealth = attributes[HEALTH]!!.finalValue
        val maxMana = attributes[MANA]!!.finalValue

        oldHealthPercent = health / maxHealth
        oldManaPercent = if (maxMana == 0.0) 1.0 else mana / maxMana

        createPlayerAttributes()
        createPlayerProperties()
        updateHero()
        updateItems()
        updateEffects()
        updateDotaAttributes()
        updateMinecraftAttributes()

        health = attributes[HEALTH]!!.finalValue * oldHealthPercent
        mana = attributes[MANA]!!.finalValue * oldManaPercent
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
                            DAMAGE -> {
                                val attributeModifier = gameItem.modifiers[attributeType]!!.copy()
                                attributeModifier.amount -= 1
                                addAttributeModifier(attributeType, attributeModifier)
                            }

                            ATTACK_SPEED -> {
                                val attributeModifier = gameItem.modifiers[attributeType]!!.copy()
                                attributeModifier.amount -= 4
                                addAttributeModifier(attributeType, attributeModifier)
                            }

                            else -> {}
                        }
                    } else repeat(gameItem.amount) {
                        addAttributeModifier(
                            attributeType,
                            gameItem.modifiers[attributeType]!!
                        )
                    }
                }
            }
        }
    }

    private fun updateEffects() {
        getActiveEffects().forEach { effect ->
            effect.attributeChanges.keys.forEach { addAttributeModifier(it, effect.attributeChanges[it]!!) }
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
                            addAttributeModifier(DAMAGE, defaultDamageModifier)
                        }
                        if (mainAttribute == HeroAttribute.UNIVERSAL) {
                            addAttributeModifier(DAMAGE, universalDamageModifier)
                        }

                        addAttributeModifier(
                            HEALTH, AttributeModifier(
                                strengthModifier.name,
                                strengthModifier.health * round(finalValue),
                                OperationType.ADD,
                                false
                            )
                        )

                        addAttributeModifier(
                            HEALTH_REGEN, AttributeModifier(
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
                            addAttributeModifier(DAMAGE, defaultDamageModifier)
                        }
                        if (mainAttribute == HeroAttribute.UNIVERSAL) {
                            addAttributeModifier(DAMAGE, universalDamageModifier)
                        }

                        addAttributeModifier(
                            DEFENSE, AttributeModifier(
                                agilityModifier.name,
                                agilityModifier.defense * round(finalValue),
                                OperationType.ADD,
                                false
                            )
                        )

                        addAttributeModifier(
                            ATTACK_SPEED, AttributeModifier(
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
                            addAttributeModifier(DAMAGE, defaultDamageModifier)
                        }
                        if (mainAttribute == HeroAttribute.UNIVERSAL) {
                            addAttributeModifier(DAMAGE, universalDamageModifier)
                        }

                        addAttributeModifier(
                            MANA, AttributeModifier(
                                intelligenceModifier.name,
                                intelligenceModifier.mana * round(finalValue),
                                OperationType.ADD,
                                false
                            )
                        )

                        addAttributeModifier(
                            MANA_REGEN, AttributeModifier(
                                intelligenceModifier.name,
                                intelligenceModifier.manaRegen * round(finalValue),
                                OperationType.ADD,
                                false
                            )
                        )

                        addAttributeModifier(
                            MAGIC_RESISTANCE, AttributeModifier(
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

    fun addUncheckedItem(gameItem: GameItem) {
        val itemType = gameItem.itemType

        if (uncheckedGameItems[itemType].isNullOrEmpty()) uncheckedGameItems[itemType] = mutableListOf()

        if (itemType == ItemType.CONSUMABLE) {
            val targetItem: GameItem? = findGameItem(gameItem.gameName, gameItem.itemStack)

            if (targetItem != null) targetItem.amount += gameItem.amount
            else uncheckedGameItems[itemType]!!.add(gameItem)
        } else uncheckedGameItems[itemType]!!.add(gameItem)
    }

    fun removeUncheckedItem(gameItem: GameItem) {
        val itemType = gameItem.itemType

        if (uncheckedGameItems[itemType].isNullOrEmpty()) uncheckedGameItems[itemType] = mutableListOf()

        val targetItem: GameItem = findGameItem(gameItem.gameName, gameItem.itemStack) ?: return
        if (itemType == ItemType.CONSUMABLE) {
            targetItem.amount -= 1
            if (targetItem.amount <= 0) uncheckedGameItems[itemType]!!.remove(gameItem)
        } else uncheckedGameItems[itemType]!!.remove(gameItem)
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

    private fun addAttributeModifier(attributeType: AttributeType, modifier: AttributeModifier) {
        var percentBefore = 0.0
        if (attributeType == HEALTH) percentBefore = health / attributes[HEALTH]!!.finalValue
        if (attributeType == MANA) percentBefore =
            if (attributes[MANA]!!.finalValue > 0) mana / attributes[MANA]!!.finalValue else 0.0

        attributes[attributeType]!!.addModifier(modifier)

        if (attributeType == HEALTH) health =
            attributes[HEALTH]!!.finalValue * percentBefore
        if (attributeType == MANA) mana = attributes[MANA]!!.finalValue * percentBefore
    }

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

    private val activeCooldown = mutableMapOf<GameItem, Long>()

    fun addCooldown(gameItem: GameItem, cooldown: Double) {
        activeCooldown[gameItem] = currentTimeMillis() + (cooldown * 1000).toLong()
    }

    fun getCooldownTime(gameItem: GameItem): Double {
        if (activeCooldown[gameItem] == null) return 0.0
        return max(0.0, ((activeCooldown[gameItem]!! - currentTimeMillis()).toDouble() / 1000))
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
        createPlayerAttributes()
        createPlayerProperties()
        update()
    }
}
