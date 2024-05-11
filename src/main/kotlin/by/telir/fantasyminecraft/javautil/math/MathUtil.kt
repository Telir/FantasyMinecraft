package by.telir.fantasyminecraft.javautil.math

import java.math.BigDecimal
import java.math.RoundingMode

class MathUtil {
    companion object {
        fun round(number: Double, charNumber: Int): Double {
            return BigDecimal(number).setScale(charNumber, RoundingMode.HALF_UP).toDouble()
        }
    }
}