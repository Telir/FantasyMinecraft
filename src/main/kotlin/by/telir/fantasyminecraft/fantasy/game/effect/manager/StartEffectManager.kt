package by.telir.fantasyminecraft.fantasy.game.effect.manager

import by.telir.fantasyminecraft.fantasy.game.effect.Effect
import by.telir.fantasyminecraft.fantasy.game.user.util.UserUtil
import org.bukkit.entity.LivingEntity

class StartEffectManager(private val livingEntity: LivingEntity, private val effect: Effect) {
    fun startEffect() {
        UserUtil.find(livingEntity.uniqueId)?.addEffect(effect)
    }

    fun stopEffect() {
        UserUtil.find(livingEntity.uniqueId)?.removeEffect(effect)
    }
}