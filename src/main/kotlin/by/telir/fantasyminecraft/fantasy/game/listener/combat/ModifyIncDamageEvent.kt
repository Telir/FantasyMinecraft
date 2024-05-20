package by.telir.fantasyminecraft.fantasy.game.listener.combat

import by.telir.fantasyminecraft.fantasy.game.listener.util.EventUtil
import by.telir.fantasyminecraft.fantasy.game.property.subproperty.ModifyIncDamageProperty
import by.telir.fantasyminecraft.fantasy.game.property.type.PropertyType
import by.telir.fantasyminecraft.fantasy.game.user.util.UserUtil
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent

class ModifyIncDamageEvent : Listener {
    @EventHandler
    fun onEntityDamageByEntity(e: EntityDamageByEntityEvent) {
        if (e.isCancelled) return

        val defender = EventUtil.getDefender(e) ?: return
        EventUtil.getEntityDamager(e) ?: return

        var defaultMultiply = 1.0
        var defaultAmount = 0.0

        val user = UserUtil.find(defender.uniqueId) ?: return
        val propertyType = PropertyType.MODIFY_INC_DAMAGE

        val gameItems = user.getGameItems()
        gameItems.forEach {
            if (propertyType in it.properties) {
                val modifyIncDamageProperty = it.properties[propertyType] as ModifyIncDamageProperty
                if (modifyIncDamageProperty.testFor()) {
                    defaultMultiply *= modifyIncDamageProperty.percent
                    defaultAmount += modifyIncDamageProperty.amount
                }
            }
        }

        if (propertyType in user.properties) {
            val modifyIncDamageProperty = user.properties[propertyType] as ModifyIncDamageProperty
            if (modifyIncDamageProperty.testFor()) {
                defaultMultiply *= modifyIncDamageProperty.percent
                defaultAmount += modifyIncDamageProperty.amount

            }
        }

        e.damage = (e.damage - defaultAmount) * defaultMultiply
    }
}