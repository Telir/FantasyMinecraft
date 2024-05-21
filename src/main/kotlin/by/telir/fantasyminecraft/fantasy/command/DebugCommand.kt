package by.telir.fantasyminecraft.fantasy.command

import by.telir.fantasyminecraft.fantasy.game.user.util.UserUtil
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.ConsoleCommandSender

class DebugCommand : CommandExecutor {
    override fun onCommand(
        sender: CommandSender?,
        command: Command,
        label: String?,
        args: Array<out String>?
    ): Boolean {
        if (sender !is ConsoleCommandSender) return true
        if (args!!.size == 3) {
            if (args[0] == "lostmana") {
                val lostValue: Double
                try {
                    lostValue = args[1].toDouble()
                } catch (e: NumberFormatException) {
                    return false
                }

                val targetPlayer = Bukkit.getPlayer(args[2])
                if (targetPlayer == null) {
                    sender.sendMessage("Player not found!")
                    return true
                }

                UserUtil.find(targetPlayer.uniqueId)!!.mana -= lostValue
                sender.sendMessage("${targetPlayer.name} lost $lostValue mana")
                return true

            }
        }
        return false
    }
}