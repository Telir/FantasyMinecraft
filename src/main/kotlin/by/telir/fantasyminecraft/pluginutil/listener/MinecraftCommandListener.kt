package by.telir.fantasyminecraft.pluginutil.listener

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerCommandPreprocessEvent
import org.bukkit.event.server.TabCompleteEvent

class MinecraftCommandListener : Listener {
    @EventHandler
    fun onPlayerCommand(e: PlayerCommandPreprocessEvent) {
        if (e.message.startsWith("/minecraft:")) {
            e.player.sendMessage("Unknown command. Type \"/help\" for help.")
            e.isCancelled = true
        }
    }

    @EventHandler
    fun onPlayerTabComplete(e: TabCompleteEvent) {
        val filter = e.completions.filter { it.startsWith("/minecraft:") }
        e.completions.removeAll(filter)
    }
}