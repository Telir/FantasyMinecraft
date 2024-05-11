package by.telir.fantasyminecraft.fantasy.command

import by.telir.fantasyminecraft.fantasy.game.effect.subeffect.BleedingEffect
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
        val player = sender as? Player ?: return true
        val user = UserUtil.find(player.uniqueId)!!
        val bleedingEffect = BleedingEffect(user, 10.0, 1.5)
        bleedingEffect.amount = 2.0
        bleedingEffect.start(user)

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