package by.telir.fantasyminecraft.pluginutil.command

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class FlyCommand : CommandExecutor {
    override fun onCommand(
        sender: CommandSender?,
        command: Command,
        label: String?,
        args: Array<out String>?
    ): Boolean {
        val player = sender as Player

        player.allowFlight = !player.allowFlight
        return true
    }
}