package by.telir.fantasyminecraft.fantasy.game.listener.util

import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageByEntityEvent

object EventUtil {
    fun getDamager(e: EntityDamageByEntityEvent): Player? {
        return e.damager as? Player
    }

    fun getEntityDamager(e: EntityDamageByEntityEvent): LivingEntity? {
        return e.damager as? LivingEntity
    }

    fun getDefender(e: EntityDamageByEntityEvent): Player? {
        return e.entity as? Player
    }

    fun getEntityDefender(e: EntityDamageByEntityEvent): LivingEntity? {
        return e.entity as? LivingEntity
    }
}