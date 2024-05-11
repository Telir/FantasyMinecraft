package by.telir.fantasyminecraft.fantasy.game.listener.regen

import by.telir.fantasyminecraft.fantasy.game.attribute.type.AttributeType
import by.telir.fantasyminecraft.fantasy.game.user.util.UserUtil
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityRegainHealthEvent

class HealthRegenEvent : Listener {
    @EventHandler
    fun onEntityRegainHealth(e: EntityRegainHealthEvent) {
        if (e.regainReason != EntityRegainHealthEvent.RegainReason.SATIATED) return
        val user = UserUtil.find(e.entity.uniqueId) ?: return
        e.amount = user.attributes[AttributeType.HEALTH_REGEN]!!.finalValue
    }
}