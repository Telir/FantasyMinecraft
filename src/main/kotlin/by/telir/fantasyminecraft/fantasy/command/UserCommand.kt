package by.telir.fantasyminecraft.fantasy.command

import by.telir.fantasyminecraft.fantasy.game.hero.manager.HeroManager
import by.telir.fantasyminecraft.fantasy.game.hero.type.HeroAttribute
import by.telir.fantasyminecraft.fantasy.game.hero.util.HeroUtil
import by.telir.fantasyminecraft.fantasy.game.item.manager.GameItemManager
import by.telir.fantasyminecraft.fantasy.game.item.type.ItemType
import by.telir.fantasyminecraft.fantasy.game.item.util.GameItemUtil
import by.telir.fantasyminecraft.fantasy.game.user.util.UserUtil
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabExecutor
import org.bukkit.entity.Player

class UserCommand : TabExecutor {
    override fun onCommand(
        sender: CommandSender?,
        command: Command,
        label: String?,
        args: Array<out String>?
    ): Boolean {
        val player = sender as? Player ?: return true
        val user = UserUtil.find(player.uniqueId)!!

        if (!args?.get(0).isNullOrEmpty()) {
            when (args!![0].lowercase()) {
                "create" -> {
                    var targetPlayer: Player = player
                    if (args.size == 2 && args[1].isNotEmpty()) {
                        if (Bukkit.getPlayer(args[1]) == null) {
                            player.sendMessage("§c'${args[1]}' not found")
                            return true
                        } else targetPlayer = Bukkit.getPlayer(args[1])
                    }
                    UserUtil.create(targetPlayer.uniqueId)
                    player.sendMessage("§aUser '${targetPlayer.name}' created!")
                }

                "getitem" -> {
                    if (args[1].isEmpty()) return false
                    if (args[2].isEmpty()) return false

                    val type: ItemType = when (args[1]) {
                        "artifact" -> ItemType.ARTIFACT
                        "weapon" -> ItemType.WEAPON
                        "consumable" -> ItemType.CONSUMABLE
                        else -> return false
                    }

                    GameItemManager().add(user, args[2], type, false)
                }

                "selecthero" -> {
                    if (args[1].isEmpty()) return false
                    if (args[2].isEmpty()) return false

                    val attribute: HeroAttribute = when (args[1]) {
                        "strength" -> HeroAttribute.STRENGTH
                        "agility" -> HeroAttribute.AGILITY
                        "intelligence" -> HeroAttribute.INTELLIGENCE
                        "universal" -> HeroAttribute.UNIVERSAL
                        else -> return false
                    }

                    HeroManager().select(user, args[2], attribute)

                }

            }
        }
        return true
    }

    override fun onTabComplete(
        sender: CommandSender?,
        command: Command,
        label: String?,
        args: Array<out String>?
    ): List<String> {
        when (args!!.size) {
            1 -> {
                val returningArgs = mutableListOf("create", "getitem", "selecthero")
                if (args[0].isEmpty()) return returningArgs
                return returningArgs.filter { it.lowercase().startsWith(args[0].lowercase()) }
            }

            2 -> {
                if (args[0] == "getitem") {
                    val returningArgs = mutableListOf("artifact", "weapon", "consumable")
                    if (args[1].isEmpty()) return returningArgs
                    return returningArgs.filter { it.lowercase().startsWith(args[1].lowercase()) }
                }
                if (args[0] == "selecthero") {
                    val returningArgs = mutableListOf("strength", "agility", "intelligence", "universal")
                    if (args[1].isEmpty()) return returningArgs
                    return returningArgs.filter { it.lowercase().startsWith(args[1].lowercase()) }
                }
            }

            3 -> {
                if (args[0] == "getitem") {
                    val type: ItemType = when (args[1]) {
                        "artifact" -> ItemType.ARTIFACT
                        "weapon" -> ItemType.WEAPON
                        "consumable" -> ItemType.CONSUMABLE
                        else -> return ArrayList()
                    }
                    val returningArgs = GameItemUtil.getNames(type)
                    if (args[2].isEmpty()) return returningArgs.toList()
                    return returningArgs.filter { it.lowercase().startsWith(args[2].lowercase()) }
                }

                if (args[0] == "selecthero") {
                    val attribute: HeroAttribute = when (args[1]) {
                        "strength" -> HeroAttribute.STRENGTH
                        "agility" -> HeroAttribute.AGILITY
                        "intelligence" -> HeroAttribute.INTELLIGENCE
                        "universal" -> HeroAttribute.UNIVERSAL
                        else -> return ArrayList()
                    }
                    val returningArgs = HeroUtil.getNames(attribute)
                    if (args[2].isEmpty()) return returningArgs.toList()
                    return returningArgs.filter { it.lowercase().startsWith(args[2].lowercase()) }
                }
            }

            else -> return mutableListOf()
        }
        return mutableListOf()
    }


}