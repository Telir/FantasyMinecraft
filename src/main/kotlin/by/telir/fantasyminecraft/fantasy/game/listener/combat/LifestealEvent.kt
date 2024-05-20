package by.telir.fantasyminecraft.fantasy.game.listener.combat

import by.telir.fantasyminecraft.fantasy.game.attribute.type.MinecraftAttribute
import by.telir.fantasyminecraft.fantasy.game.attribute.util.AttributeUtil
import by.telir.fantasyminecraft.fantasy.game.damage.deal.DamageDealer
import by.telir.fantasyminecraft.fantasy.game.damage.type.DamageType
import by.telir.fantasyminecraft.fantasy.game.listener.util.EventUtil
import by.telir.fantasyminecraft.fantasy.game.property.subproperty.LifestealProperty
import by.telir.fantasyminecraft.fantasy.game.property.type.PropertyType
import by.telir.fantasyminecraft.fantasy.game.user.util.UserUtil
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import kotlin.math.max

class LifestealEvent : Listener {
    @EventHandler
    fun onEntityDamageByEntity(e: EntityDamageByEntityEvent) {
        if (e.isCancelled) return

        val damager = EventUtil.getDamager(e) ?: return
        val defender = EventUtil.getEntityDefender(e) ?: return

        var healAmount = 0.0
        var healAmplifier = 1.0
        var damageAmount = 0.0
        var damageType = DamageType.PHYSICAL

        val user = UserUtil.find(damager.uniqueId) ?: return
        val propertyType = PropertyType.LIFESTEAL

        val defenderMaxHealth = AttributeUtil.getValue(defender, MinecraftAttribute.GENERIC_MAX_HEALTH)
        val defenderHealth = defender.health

        val gameItems = user.getGameItems()
        gameItems.forEach {
            if (propertyType in it.properties) {
                val lifestealProperty = it.properties[propertyType] as LifestealProperty
                if (lifestealProperty.testFor()) {
                    healAmplifier += lifestealProperty.amplifier
                    val heal = lifestealProperty.amount + e.damage * lifestealProperty.percent +
                            defenderMaxHealth * lifestealProperty.enemyMaxHealthPercent +
                            defenderHealth * lifestealProperty.enemyHealthPercent
                    healAmount = max(heal, healAmount)
                    if (heal * lifestealProperty.damagePercent > damageAmount) {
                        damageAmount = heal * lifestealProperty.damagePercent
                        damageType = lifestealProperty.damageType
                    }
                }
            }
        }
        if (propertyType in user.properties) {
            val lifestealProperty = user.properties[propertyType] as LifestealProperty
            if (lifestealProperty.testFor()) {
                if (lifestealProperty.testFor()) {
                    healAmplifier += lifestealProperty.amplifier
                    val heal = lifestealProperty.amount + e.damage * lifestealProperty.percent +
                            defenderMaxHealth * lifestealProperty.enemyMaxHealthPercent +
                            defenderHealth * lifestealProperty.enemyHealthPercent
                    healAmount = max(heal, healAmount)
                    if (heal * lifestealProperty.damagePercent > damageAmount) {
                        damageAmount = heal * lifestealProperty.damagePercent
                        damageType = lifestealProperty.damageType
                    }
                }
            }
        }
        if (healAmount > 0.0) {
            damager.health += max(healAmount * healAmplifier, AttributeUtil.getValue(damager, MinecraftAttribute.GENERIC_MAX_HEALTH))
        }
        if (damageAmount > 0.0) {
            DamageDealer(defender).dealDamage(damageAmount, damageType)
        }
    }
}