package by.telir.fantasyminecraft.fantasy.game.effect.type

import by.telir.fantasyminecraft.fantasy.game.effect.type.EffectType.PeriodEffectType.CHANGE_HEALTH

enum class EffectType(val actionType: EffectActionType, val periodType: PeriodEffectType?) {
    SPEED(EffectActionType.DEFAULT, null),
    SLOWNESS(EffectActionType.DEFAULT, null),
    ATTACK_BOOST(EffectActionType.DEFAULT, null),
    PERIOD_HEAL(EffectActionType.PERIOD, null),
    BLEEDING(EffectActionType.PERIOD, CHANGE_HEALTH);

    enum class EffectActionType {
        DEFAULT,
        PERIOD
    }

    enum class PeriodEffectType {
        CHANGE_MANA,
        CHANGE_HEALTH
    }
}