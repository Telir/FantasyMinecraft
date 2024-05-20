package by.telir.fantasyminecraft.fantasy.game.showhealth.manager

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.scoreboard.DisplaySlot
import org.bukkit.scoreboard.Objective
import kotlin.math.ceil

class ShowHealthManager {
    private val sc = Bukkit.getScoreboardManager().mainScoreboard!!
    private lateinit var belowOjb: Objective

    fun setupBelow() {
        belowOjb = sc.getObjective("healthbarbellow") ?: sc.registerNewObjective("healthbarbellow", "dummy")

        belowOjb.displaySlot = DisplaySlot.BELOW_NAME
        belowOjb.displayName = "§c❤"
    }

    fun updateHealthbarBelow(player: Player) {
        val score: Int = ceil(player.health).toInt()
        belowOjb.getScore(player.name).score = score
    }

    fun removeBelow() {
        belowOjb.unregister()
    }
}