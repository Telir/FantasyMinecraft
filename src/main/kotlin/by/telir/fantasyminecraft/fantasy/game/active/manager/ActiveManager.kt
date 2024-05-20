package by.telir.fantasyminecraft.fantasy.game.active.manager

import by.telir.fantasyminecraft.FantasyMinecraft
import by.telir.fantasyminecraft.fantasy.game.attribute.modifier.AttributeModifier
import by.telir.fantasyminecraft.fantasy.game.attribute.type.AttributeType
import by.telir.fantasyminecraft.fantasy.game.item.GameItem
import by.telir.fantasyminecraft.fantasy.game.property.GameProperty
import by.telir.fantasyminecraft.fantasy.game.property.type.PropertyType
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scheduler.BukkitTask

class ActiveManager(private var gameItem: GameItem) {
    companion object {
        private val PLUGIN = FantasyMinecraft.instance
    }

    val changedProperties = mutableMapOf<PropertyType, GameProperty>()
    val changedModifiers = mutableMapOf<AttributeType, AttributeModifier>()


    lateinit var oldProperties: Map<PropertyType, GameProperty>
    lateinit var oldModifiers: Map<AttributeType, AttributeModifier>

    private lateinit var stopTask: BukkitTask

    fun start(duration: Double) {
        if (changedProperties.isNotEmpty()) {
            oldProperties = gameItem.properties.toMap()
            gameItem.properties.putAll(changedProperties)
        }

        if (changedModifiers.isNotEmpty()) {
            oldModifiers = gameItem.modifiers.toMap()
            gameItem.modifiers.putAll(changedModifiers)
        }

        createStopTask(duration)
    }

    fun stop() {
        if (changedProperties.isNotEmpty()) {
            gameItem.properties.keys.removeAll(changedProperties.keys)
            gameItem.properties.putAll(oldProperties)
        }

        if (changedModifiers.isNotEmpty()) {
            gameItem.modifiers.keys.removeAll(changedModifiers.keys)
            gameItem.modifiers.putAll(oldModifiers)
        }
    }

    private fun createStopTask(duration: Double) {
        stopTask = object : BukkitRunnable() {
            override fun run() {
                stop()
            }

        }.runTaskLater(PLUGIN, (duration * 20).toLong())

    }
}