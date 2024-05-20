package by.telir.fantasyminecraft.fantasy.game.listener.help

import by.telir.fantasyminecraft.fantasy.game.attribute.type.MinecraftAttribute
import by.telir.fantasyminecraft.fantasy.game.attribute.util.AttributeUtil
import by.telir.fantasyminecraft.javautil.math.MathUtil
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent

class AttackInfoEvent: Listener {
    companion object {
        lateinit var instance: AttackInfoEvent
    }

    init {
        instance = this
    }

    var attackCooldown: Double = 0.0
//    var isCrit: Boolean = false

    @EventHandler
    fun onEntityDamageByEntity(e: EntityDamageByEntityEvent) {
        val damager = e.damager as? Player ?: return
        val maxDamage: Double =
            MathUtil.round(AttributeUtil.getValue(damager, MinecraftAttribute.GENERIC_ATTACK_DAMAGE), 2)
        val damage: Double = MathUtil.round(e.damage, 2)
        val damageIfCrit: Double = MathUtil.round(maxDamage * 1.5, 2)

        if (damage == damageIfCrit) {
//            isCrit = true
            attackCooldown = 1.0
            return
        }
        attackCooldown = MathUtil.round(damage / maxDamage, 2)
    }
}