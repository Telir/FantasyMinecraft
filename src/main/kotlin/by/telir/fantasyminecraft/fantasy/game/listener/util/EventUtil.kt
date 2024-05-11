package by.telir.fantasyminecraft.fantasy.game.listener.util

import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.entity.Projectile
import org.bukkit.event.entity.EntityDamageByEntityEvent

class EventUtil {
    companion object {
        fun getDamager(e: EntityDamageByEntityEvent): Player? {
            var damager: Player? = e.damager as? Player
            if (damager == null) {
                val projectile: Projectile? = e.damager as? Projectile
                if (projectile != null) damager = projectile.shooter as Player
            }
            return damager
        }

        fun getEntityDamager(e: EntityDamageByEntityEvent): LivingEntity? {
            var damager: LivingEntity? = e.damager as? LivingEntity
            if (damager == null) {
                val projectile: Projectile? = e.damager as? Projectile
                if (projectile != null) damager = projectile.shooter as? LivingEntity
            }
            return damager
        }

        fun getDefender(e: EntityDamageByEntityEvent): Player? {
            return e.entity as? Player
        }

        fun getEntityDefender(e: EntityDamageByEntityEvent): LivingEntity? {
            return e.entity as? LivingEntity
        }
    }
}