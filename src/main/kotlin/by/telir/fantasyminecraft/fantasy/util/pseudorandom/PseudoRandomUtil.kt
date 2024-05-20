package by.telir.fantasyminecraft.fantasy.util.pseudorandom

import java.util.*
import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.min

object PseudoRandomUtil {
    private fun calculatePfromC(c: Double): Double {
        var pProcOnN: Double
        var pProcByN = 0.0
        var sumNpProcOnN = 0.0

        val maxFails = ceil(1.0 / c).toInt()

        for (n in 1..maxFails) {
            pProcOnN = min(1.0, n * c) * (1.0 - pProcByN)
            pProcByN += pProcOnN
            sumNpProcOnN += n * pProcOnN
        }

        return 1.0 / sumNpProcOnN
    }

    private fun calculateCfromP(p: Double): Double {
        var cUpper = p
        var cLower = 0.0
        var cMid: Double
        var p2 = 1.0

        while (true) {
            cMid = (cUpper + cLower) / 2.0
            val p1 = calculatePfromC(cMid)

            if (abs(p1 - p2) <= 0) {
                break
            }

            if (p1 > p) {
                cUpper = cMid
            } else {
                cLower = cMid
            }

            p2 = p1
        }

        return cMid
    }

    fun isProc(p: Double, n: Int): Boolean {
        if (p >= 1) return true
        if (p <= 0) return false

        val c = calculateCfromP(p)
        val chance = n * c
        val random = Random().nextInt(1_000_000)

        return random < chance * 1_000_000
    }
}