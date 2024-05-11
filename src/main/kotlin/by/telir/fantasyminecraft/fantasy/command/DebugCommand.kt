package by.telir.fantasyminecraft.fantasy.command

import by.telir.fantasyminecraft.FantasyMinecraft
import by.telir.fantasyminecraft.fantasy.game.user.util.UserUtil
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabExecutor
import org.bukkit.entity.Player

class DebugCommand : TabExecutor {
    companion object {
        private val PLUGIN = FantasyMinecraft.instance
    }

    override fun onCommand(
        sender: CommandSender?,
        command: Command,
        label: String?,
        args: Array<out String>?
    ): Boolean {
        val player = sender as? Player ?: return true
        val user = UserUtil.find(player.uniqueId)
        when (args?.get(0)) {
            null -> return false
            "users" -> {
                player.sendMessage(UserUtil.users().toString())
                return true
            }

            else -> return false
        }
    }

    override fun onTabComplete(
        sender: CommandSender?,
        command: Command,
        label: String?,
        args: Array<out String>?
    ): List<String> {
        if (args!!.size == 1) {
            return listOf("users")
        }
        return ArrayList()
    }
}