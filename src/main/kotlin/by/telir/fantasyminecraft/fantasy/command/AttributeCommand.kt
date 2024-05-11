package by.telir.fantasyminecraft.fantasy.command

import by.telir.fantasyminecraft.fantasy.game.attribute.type.AttributeType
import by.telir.fantasyminecraft.fantasy.game.user.User
import by.telir.fantasyminecraft.fantasy.game.user.util.UserUtil
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabExecutor
import org.bukkit.entity.Player

class AttributeCommand : TabExecutor {
    override fun onCommand(
        sender: CommandSender?,
        command: Command?,
        label: String?,
        args: Array<out String>?
    ): Boolean {
        val player: Player = sender as? Player ?: return true
        if (args.isNullOrEmpty()) return false
        when (args.size) {
            1 -> {
                val attributeType: AttributeType = when (args[0].lowercase()) {
                    "damage" -> AttributeType.DAMAGE
                    "attackspeed" -> AttributeType.ATTACK_SPEED
                    "movespeed" -> AttributeType.MOVEMENT_SPEED
                    "defense" -> AttributeType.DEFENSE
                    "toughness" -> AttributeType.TOUGHNESS
                    "healthregen" -> AttributeType.HEALTH_REGEN
                    "manaregen" -> AttributeType.MANA_REGEN
                    "health" -> AttributeType.HEALTH
                    "mana" -> AttributeType.MANA
                    "magicresistance" -> AttributeType.MAGIC_RESISTANCE
                    "agility" -> AttributeType.AGILITY
                    "intelligence" -> AttributeType.INTELLIGENCE
                    "strength" -> AttributeType.STRENGTH
                    else -> return false
                }
                val user: User = UserUtil.find(player.uniqueId)!!
                player.sendMessage(user.attributes[attributeType]!!.finalValue.toString())
            }

            else -> return false
        }
        return true
    }

    override fun onTabComplete(
        sender: CommandSender?,
        command: Command?,
        alias: String?,
        args: Array<out String>?
    ): List<String> {
        if (args!!.size == 1) {
            val returningArgs = mutableListOf(
                "damage", "attackSpeed", "moveSpeed", "health", "mana", "healthRegen", "manaRegen",
                "defense", "toughness", "magicResistance", "agility", "intelligence", "strength"
            )
            if (args[0].isEmpty()) return returningArgs
            return returningArgs.filter { it.lowercase().startsWith(args[0].lowercase()) }
        }
        return ArrayList()
    }
}