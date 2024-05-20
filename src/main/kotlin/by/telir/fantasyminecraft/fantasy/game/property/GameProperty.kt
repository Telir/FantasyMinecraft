package by.telir.fantasyminecraft.fantasy.game.property

import by.telir.fantasyminecraft.fantasy.game.property.type.PropertyType
import by.telir.fantasyminecraft.fantasy.util.pseudorandom.PseudoRandomUtil

abstract class GameProperty(
    val type: PropertyType,
    var chance: Double,
) {
    private var hitsWithout: Int = 0

    fun testFor(): Boolean {
        if (chance == 0.0) return false
        val isProc = PseudoRandomUtil.isProc(chance, hitsWithout + 1)

        if (isProc) {
            set0Htw()
            return true
        }
        incrementHtw()
        return false
    }

    private fun incrementHtw() {
        hitsWithout++
    }

    private fun set0Htw() {
        hitsWithout = 0
    }
}