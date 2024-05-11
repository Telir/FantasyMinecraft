package by.telir.fantasyminecraft.fantasy.game.listener.combat

import by.telir.fantasyminecraft.fantasy.game.damage.deal.DamageDealer
import by.telir.fantasyminecraft.fantasy.game.damage.type.DamageType
import by.telir.fantasyminecraft.fantasy.game.listener.util.EventUtil
import by.telir.fantasyminecraft.fantasy.game.property.subproperty.ReturnDamageProperty
import by.telir.fantasyminecraft.fantasy.game.property.type.GamePropertyType
import by.telir.fantasyminecraft.fantasy.game.user.util.UserUtil
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import kotlin.math.max

class ReturnDamageEvent : Listener {
    @EventHandler
    fun onEntityDamageByEntity(e: EntityDamageByEntityEvent) {
        if (e.isCancelled) return

        val defender = EventUtil.getDefender(e) ?: return
        EventUtil.getEntityDamager(e) ?: return

        var returnDamage = 0.0
        var blockAmount = 0.0

        val user = UserUtil.find(defender.uniqueId) ?: return
        val propertyType = GamePropertyType.RETURN_DAMAGE

        val gameItems = user.getGameItems()
        gameItems.forEach {
            if (propertyType in it.properties) {
                val returnDamageProperty = it.properties[propertyType] as ReturnDamageProperty
                if (returnDamageProperty.testFor()) {
                    val returnAmount = returnDamageProperty.amount + returnDamageProperty.percent * e.damage
                    returnDamage = max(returnAmount, returnDamage)
                    if (returnDamageProperty.isReflect) blockAmount = max(blockAmount, returnDamage)
                }
            }
        }

        if (propertyType in user.properties) {
            val returnDamageProperty = user.properties[propertyType] as ReturnDamageProperty
            if (returnDamageProperty.testFor()) {
                val returnAmount = returnDamageProperty.amount + returnDamageProperty.percent * e.damage
                returnDamage = max(returnAmount, returnDamage)
                if (returnDamageProperty.isReflect) blockAmount = max(blockAmount, returnDamage)
            }
        }

        e.damage -= blockAmount
        DamageDealer(defender).dealDamage(returnDamage, DamageType.PHYSICAL)
    }
}