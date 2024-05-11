package by.telir.fantasyminecraft.fantasy.game.listener.combat

import by.telir.fantasyminecraft.fantasy.game.damage.deal.DamageDealer
import by.telir.fantasyminecraft.fantasy.game.damage.type.DamageType
import by.telir.fantasyminecraft.fantasy.game.listener.util.EventUtil
import by.telir.fantasyminecraft.fantasy.game.property.subproperty.TrueStrikeProperty
import by.telir.fantasyminecraft.fantasy.game.property.type.GamePropertyType
import by.telir.fantasyminecraft.fantasy.game.user.util.UserUtil
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent

class TrueStrikeEvent: Listener {
    @EventHandler
    fun onEntityDamageByEntity(e: EntityDamageByEntityEvent) {
        val damager = EventUtil.getDamager(e) ?: return
        val defender = EventUtil.getEntityDefender(e) ?: return

        var damageType = DamageType.PHYSICAL
        var damageAmount = 0.0
        var isTrueStrike = false

        val user = UserUtil.find(damager.uniqueId) ?: return
        val propertyType = GamePropertyType.TRUE_STRIKE

        val gameItems = user.getGameItems()
        gameItems.forEach {
            if (propertyType in it.properties) {
                val trueStrikeProperty = it.properties[propertyType] as TrueStrikeProperty
                if (trueStrikeProperty.testFor()) {
                    if (trueStrikeProperty.damageAmount + e.damage * trueStrikeProperty.damagePercent > damageAmount) {
                        damageAmount = trueStrikeProperty.damageAmount + e.damage * trueStrikeProperty.damagePercent
                        damageType = trueStrikeProperty.damageType
                    }
                }
            }
        }
        if (propertyType in user.properties) {
            val trueStrikeProperty = user.properties[propertyType] as TrueStrikeProperty
            if (trueStrikeProperty.testFor()) {
                if (trueStrikeProperty.damageAmount + e.damage * trueStrikeProperty.damagePercent > damageAmount) {
                    damageAmount = trueStrikeProperty.damageAmount + e.damage * trueStrikeProperty.damagePercent
                    damageType = trueStrikeProperty.damageType
                }
            }
        }
        if (isTrueStrike) {
            e.isCancelled = false
            DamageDealer(defender).dealDamage(damageAmount, damageType)
        }

    }
}