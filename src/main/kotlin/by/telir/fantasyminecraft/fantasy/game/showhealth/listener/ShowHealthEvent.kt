package by.telir.fantasyminecraft.fantasy.game.showhealth.listener

import by.telir.fantasyminecraft.instance
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityRegainHealthEvent
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerRespawnEvent
import org.bukkit.scheduler.BukkitRunnable

class ShowHealthEvent : Listener {
    private val showHealthManager = instance.showHealthManager

    @EventHandler
    fun onPlayerJoin(e: PlayerJoinEvent) {
        showHealthManager.updateHealthbarBelow(e.player)
    }

    @EventHandler
    fun onEntityDamage(e: EntityDamageEvent) {
        val player = e.entity as? Player ?: return
        object : BukkitRunnable() {
            override fun run() {
                showHealthManager.updateHealthbarBelow(player)
            }
        }.runTaskLater(instance, 2)
    }

    @EventHandler
    fun onPlayerRespawn(e: PlayerRespawnEvent) {
        showHealthManager.updateHealthbarBelow(e.player)
    }

    @EventHandler
    fun onEntityDeath(e: PlayerDeathEvent) {
        showHealthManager.updateHealthbarBelow(e.entity)
    }

    @EventHandler
    fun onEntityRegainHealth(e: EntityRegainHealthEvent) {
        val player = e.entity as? Player ?: return
        showHealthManager.updateHealthbarBelow(player)
    }
}