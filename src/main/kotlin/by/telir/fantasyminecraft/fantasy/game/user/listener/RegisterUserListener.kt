package by.telir.fantasyminecraft.fantasy.game.user.listener

import by.telir.fantasyminecraft.fantasy.game.user.util.UserUtil
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

class RegisterUserListener : Listener {
    @EventHandler
    fun onPlayerJoin(e: PlayerJoinEvent) {
        e.player.apply {
            UserUtil.create(uniqueId)
            healthScale = 20.0
        }
    }

    @EventHandler
    fun onPlayerQuit(e: PlayerQuitEvent) {
        e.player.apply {
            UserUtil.remove(uniqueId)
        }
    }
}