package by.telir.fantasyminecraft.fantasy.game.active.manager

import by.telir.fantasyminecraft.FantasyMinecraft
import by.telir.fantasyminecraft.fantasy.game.attribute.modifier.AttributeModifier
import by.telir.fantasyminecraft.fantasy.game.attribute.type.AttributeType
import by.telir.fantasyminecraft.fantasy.game.item.GameItem
import by.telir.fantasyminecraft.fantasy.game.property.GameProperty
import by.telir.fantasyminecraft.fantasy.game.property.type.GamePropertyType
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scheduler.BukkitTask

class ActiveManager(var gameItem: GameItem) {
    companion object {
        private val PLUGIN = FantasyMinecraft.instance
    }

    val changedProperties = mutableMapOf<GamePropertyType, GameProperty>()
    val changedModifiers = mutableMapOf<AttributeType, AttributeModifier>()

    var cooldown: Double
        get() = if (endCooldownTime < System.currentTimeMillis()) 0.0 else (endCooldownTime - System.currentTimeMillis()).toDouble() / 1000
        set(value) {
            if (value > 0.0) {
                stopTask.cancel()
                createStopTask(value)
            } else stop()
        }

    private var endCooldownTime: Long = 0L

    lateinit var oldProperties: Map<GamePropertyType, GameProperty>
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