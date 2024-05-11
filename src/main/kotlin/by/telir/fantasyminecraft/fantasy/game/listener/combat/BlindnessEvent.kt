package by.telir.fantasyminecraft.fantasy.game.listener.combat

import by.telir.fantasyminecraft.fantasy.game.listener.util.EventUtil
import by.telir.fantasyminecraft.fantasy.game.property.subproperty.BlindnessProperty
import by.telir.fantasyminecraft.fantasy.game.property.type.GamePropertyType
import by.telir.fantasyminecraft.fantasy.game.user.util.UserUtil
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent

class BlindnessEvent : Listener {
    @EventHandler
    fun onEntityDamageByEntity(e: EntityDamageByEntityEvent) {
        val defender = EventUtil.getDefender(e) ?: return
        EventUtil.getEntityDamager(e) ?: return

        var blindnessChance = 1.0

        val user = UserUtil.find(defender.uniqueId) ?: return
        val propertyType = GamePropertyType.BLINDNESS

        val gameItems = user.getGameItems()
        gameItems.forEach {
            if (propertyType in it.properties) {
                val blindnessProperty = it.properties[propertyType] as BlindnessProperty
                blindnessChance *= 1 - blindnessProperty.chance
            }
        }

        val blindnessProperty = user.properties[propertyType] as BlindnessProperty
        val playerChance = blindnessProperty.chance
        blindnessChance *= 1 - playerChance

        blindnessChance = 1 - blindnessChance
        if (blindnessChance == 0.0) return

        blindnessProperty.chance = blindnessChance
        if (blindnessProperty.testFor()) e.isCancelled = true

        blindnessProperty.chance = playerChance
    }
}