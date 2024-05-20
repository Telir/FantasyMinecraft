package by.telir.fantasyminecraft.fantasy.game.listener.combat

import by.telir.fantasyminecraft.fantasy.game.effect.Effect
import by.telir.fantasyminecraft.fantasy.game.effect.type.EffectType
import by.telir.fantasyminecraft.fantasy.game.listener.help.AttackInfoEvent
import by.telir.fantasyminecraft.fantasy.game.listener.util.EventUtil
import by.telir.fantasyminecraft.fantasy.game.property.subproperty.HitEffectProperty
import by.telir.fantasyminecraft.fantasy.game.property.type.PropertyType
import by.telir.fantasyminecraft.fantasy.game.user.util.UserUtil
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent

class HitEffectEvent : Listener {
    @EventHandler
    fun onEntityDamageByEntity(e: EntityDamageByEntityEvent) {
        if (e.isCancelled) return

        val damager = EventUtil.getDamager(e) ?: return
        val defender = EventUtil.getEntityDefender(e) ?: return

        val user = UserUtil.find(damager.uniqueId) ?: return
        val propertyType = PropertyType.HIT_EFFECT

        val effectMap = mutableMapOf<EffectType, Effect>()

        val gameItems = user.getGameItems()
        gameItems.forEach {
            if (propertyType in it.properties) {
                val hitEffectProperty = it.properties[propertyType] as HitEffectProperty

                val oldChance = hitEffectProperty.chance
                if (hitEffectProperty.chance != 1.0) {
                    hitEffectProperty.chance *= AttackInfoEvent.instance.attackCooldown
                }
                if (hitEffectProperty.testFor()) {
                    effectMap[hitEffectProperty.effect.type] = hitEffectProperty.effect
                }
                hitEffectProperty.chance = oldChance
            }
        }

        if (propertyType in user.properties) {
            val hitEffectProperty = user.properties[propertyType] as HitEffectProperty

            val oldChance = hitEffectProperty.chance
            if (hitEffectProperty.chance != 1.0) {
                hitEffectProperty.chance *= AttackInfoEvent.instance.attackCooldown
            }
            if (hitEffectProperty.testFor()) {
                effectMap[hitEffectProperty.effect.type] = hitEffectProperty.effect
            }
            hitEffectProperty.chance = oldChance
        }

        effectMap.values.forEach { it.start(defender) }
    }
}