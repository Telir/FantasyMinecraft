package by.telir.fantasyminecraft.fantasy.game.listener.restrictions

import by.telir.fantasyminecraft.FantasyMinecraft
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.FoodLevelChangeEvent
import org.bukkit.event.player.PlayerRespawnEvent
import org.bukkit.scheduler.BukkitRunnable

class AntiHungerEvent : Listener {
    @EventHandler
    fun onFoodLevelChange(e: FoodLevelChangeEvent) {
        val player = e.entity as? Player ?: return
        player.foodLevel = 20
        player.saturation = 0F
        e.isCancelled = true
    }

    @EventHandler
    fun onPlayerRespawn(e: PlayerRespawnEvent) {
        val player = e.player
        player.saturation = 0F

        object : BukkitRunnable() {
            override fun run() {
                player.saturation = 0F
            }
        }.runTaskLater(FantasyMinecraft.instance, 1)
    }
}