package by.telir.fantasyminecraft.fantasy.game.damage.deal

import by.telir.fantasyminecraft.fantasy.game.damage.predict.PredictDamage
import by.telir.fantasyminecraft.fantasy.game.damage.type.DamageType
import org.bukkit.entity.LivingEntity

class DamageDealer(val entity: LivingEntity) {
    fun dealDamage(value: Double, type: DamageType): Double {
        val predictDamage = PredictDamage(entity, value)
        when (type) {
            DamageType.PHYSICAL -> entity.damage(predictDamage.physical)
            DamageType.MAGICAL -> entity.damage(predictDamage.magical)
            DamageType.PURE -> entity.damage(predictDamage.pure)
        }
        return entity.health
    }
}