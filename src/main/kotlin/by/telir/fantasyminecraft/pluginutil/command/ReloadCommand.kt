package by.telir.fantasyminecraft.pluginutil.command

import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

class ReloadCommand : CommandExecutor {
    override fun onCommand(
        sender: CommandSender?,
        command: Command,
        label: String?,
        args: Array<out String>?
    ): Boolean {
        Bukkit.broadcastMessage("Start reloading!")
        Bukkit.reload()
        Bukkit.broadcastMessage("Finish reloading!")
        return true
    }
}