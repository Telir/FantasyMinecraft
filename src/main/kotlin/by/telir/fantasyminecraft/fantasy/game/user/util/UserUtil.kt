package by.telir.fantasyminecraft.fantasy.game.user.util

import by.telir.fantasyminecraft.FantasyMinecraft
import by.telir.fantasyminecraft.fantasy.game.attribute.type.AttributeType
import by.telir.fantasyminecraft.fantasy.game.user.User
import by.telir.fantasyminecraft.javautil.math.MathUtil
import by.telir.fantasyminecraft.pluginutil.nms.player.NMSPlayer
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import java.util.*

object UserUtil {
    private val plugin = FantasyMinecraft.instance

    fun create(vararg uuid: UUID) {
        uuid.forEach { plugin.users[it] = User(it) }
    }

    fun contains(uuid: UUID): Boolean {
        return uuid in plugin.users
    }

    fun find(uuid: UUID): User? {
        return plugin.users[uuid]
    }

    fun users(): Set<User> {
        return plugin.users.values.toSet()
    }

    fun runManaRegen() {
        object : BukkitRunnable() {
            override fun run() {
                for (user in users()) {
                    Bukkit.getPlayer(user.uuid) ?: continue

                    val manaRegen: Double = user.attributes[AttributeType.MANA_REGEN]!!.finalValue / 10
                    user.mana += manaRegen
                    sendInfo(user)
                }
            }
        }.runTaskTimer(plugin, 0L, 2L)
    }

    private fun sendInfo(user: User) {
        val player: Player = user.player

        val mana = MathUtil.round(user.mana, 1)
        val maxMana = user.attributes[AttributeType.MANA]!!.finalValue.toInt()

        val health = player.health
        val maxHealth = user.attributes[AttributeType.HEALTH]!!.finalValue.toInt()

        val manaPercent = if (maxMana > 0) MathUtil.round(mana / maxMana * 100, 1) else 0.0
        val healthPercent = MathUtil.round(health / maxHealth * 100, 1)


        var actionBarText =
            "§c§l${health.toInt()}/$maxHealth ($healthPercent%)        §b§l$mana/$maxMana ($manaPercent%)"

        val nmsPlayer = NMSPlayer(player)
        val absorptionAmount = nmsPlayer.getAbsorption()

        if (absorptionAmount > 0) {
            val absorptionHealthPercent = MathUtil.round(health + absorptionAmount / maxHealth * 100, 1)
            actionBarText =
                "§6§l${(health + absorptionAmount).toInt()}§c§l/$maxHealth (§6§l$absorptionHealthPercent§c§l)" +
                        "        " +
                        "§b§l$mana/$maxMana ($manaPercent)"
        }

        nmsPlayer.sendActionBar(actionBarText)
    }

}