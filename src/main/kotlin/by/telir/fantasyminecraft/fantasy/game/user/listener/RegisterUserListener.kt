package by.telir.fantasyminecraft.fantasy.game.user.listener

import by.telir.fantasyminecraft.fantasy.game.user.util.UserUtil
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

class RegisterUserListener : Listener {
    @EventHandler
    fun onPlayerJoin(e: PlayerJoinEvent) {
        val uuid = e.player.uniqueId
        if (!UserUtil.contains(uuid)) UserUtil.create(uuid)
    }
}