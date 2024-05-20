package by.telir.fantasyminecraft.fantasy.game.listener.combat

import by.telir.fantasyminecraft.fantasy.game.listener.util.EventUtil
import by.telir.fantasyminecraft.fantasy.game.property.subproperty.EvasionProperty
import by.telir.fantasyminecraft.fantasy.game.property.type.PropertyType
import by.telir.fantasyminecraft.fantasy.game.user.util.UserUtil
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent

class EvasionEvent: Listener {
    @EventHandler
    fun onEntityDamageByEntity(e: EntityDamageByEntityEvent) {
        if (e.isCancelled) return

        val damager = EventUtil.getDamager(e) ?: return
        EventUtil.getEntityDefender(e) ?: return

        var evasionChance = 1.0

        val user = UserUtil.find(damager.uniqueId) ?: return
        val propertyType = PropertyType.EVASION

        val gameItems = user.getGameItems()
        gameItems.forEach {
            if (propertyType in it.properties) {
                val evasionProperty = it.properties[propertyType] as EvasionProperty
                evasionChance *= 1 - evasionProperty.chance
            }
        }

        val evasionProperty = user.properties[propertyType] as EvasionProperty
        val playerChance = evasionProperty.chance
        evasionChance *= 1 - playerChance

        evasionChance = 1 - evasionChance
        if (evasionChance == 0.0) return

        evasionProperty.chance = evasionChance
        if (evasionProperty.testFor()) e.isCancelled = true

        evasionProperty.chance = playerChance
    }
}