package by.telir.fantasyminecraft.fantasy.game.listener.combat

import by.telir.fantasyminecraft.fantasy.game.listener.util.EventUtil
import by.telir.fantasyminecraft.fantasy.game.property.subproperty.ModifyOutDamageProperty
import by.telir.fantasyminecraft.fantasy.game.property.type.PropertyType
import by.telir.fantasyminecraft.fantasy.game.user.util.UserUtil
import org.bukkit.Sound
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import kotlin.math.max

class ModifyOutDamageEvent : Listener {
    @EventHandler
    fun onEntityDamageByEntity(e: EntityDamageByEntityEvent) {
        if (e.isCancelled) return

        val damager = EventUtil.getDamager(e) ?: return
        EventUtil.getEntityDefender(e) ?: return

        var critMultiply = 1.0
        var defaultMultiply = 1.0
        var defaultAmount = 0.0

        val user = UserUtil.find(damager.uniqueId) ?: return
        val propertyType = PropertyType.MODIFY_OUT_DAMAGE

        val gameItems = user.getGameItems()
        gameItems.forEach {
            if (propertyType in it.properties) {
                val modifyOutDamageProperty = it.properties[propertyType] as ModifyOutDamageProperty
                if (modifyOutDamageProperty.testFor()) {
                    if (modifyOutDamageProperty.isCrit) {
                        critMultiply = max(modifyOutDamageProperty.percent, critMultiply)
                    } else {
                        defaultMultiply *= modifyOutDamageProperty.percent
                        defaultAmount += modifyOutDamageProperty.amount
                    }
                }
            }
        }
        if (propertyType in user.properties) {
            val modifyOutDamageProperty = user.properties[propertyType] as ModifyOutDamageProperty
            if (modifyOutDamageProperty.testFor()) {
                if (modifyOutDamageProperty.isCrit) critMultiply = max(modifyOutDamageProperty.percent, critMultiply)
                else {
                    defaultMultiply *= modifyOutDamageProperty.percent
                    defaultAmount += modifyOutDamageProperty.amount
                }
            }
        }
        if (critMultiply > 1) {
            damager.playSound(damager.location, Sound.ENTITY_IRONGOLEM_HURT, 0.85F, 0.7F)
            damager.sendMessage("CRIT")
        }
        e.damage = (e.damage + defaultAmount) * critMultiply * defaultMultiply
    }
}