package by.telir.fantasyminecraft.fantasy.command

import by.telir.fantasyminecraft.fantasy.game.effect.subeffect.BleedingEffect
import by.telir.fantasyminecraft.fantasy.game.effect.subeffect.SpeedEffect
import by.telir.fantasyminecraft.fantasy.game.user.util.UserUtil
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabExecutor
import org.bukkit.entity.Player

class EffectCommand : TabExecutor {
    override fun onCommand(
        sender: CommandSender?,
        command: Command,
        label: String?,
        args: Array<out String>?
    ): Boolean {

        return true
    }

    override fun onTabComplete(
        sender: CommandSender?,
        command: Command,
        label: String?,
        args: Array<out String>?
    ): List<String> {
        return ArrayList()
    }

}