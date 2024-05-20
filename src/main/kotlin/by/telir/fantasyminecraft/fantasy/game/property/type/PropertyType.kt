package by.telir.fantasyminecraft.fantasy.game.property.type

import by.telir.fantasyminecraft.fantasy.game.property.GameProperty
import by.telir.fantasyminecraft.fantasy.game.property.subproperty.*
import kotlin.reflect.KClass

enum class PropertyType(val isForPlayer: Boolean, val type: KClass<out GameProperty>) {
    EVASION(true, EvasionProperty::class),
    BLINDNESS(true, BlindnessProperty::class),
    TRUE_STRIKE(false, TrueStrikeProperty::class),
    MODIFY_INC_DAMAGE(false, ModifyIncDamageProperty::class),
    MODIFY_OUT_DAMAGE(false, ModifyOutDamageProperty::class),
    LIFESTEAL(false, LifestealProperty::class),
    RETURN_DAMAGE(false, ReturnDamageProperty::class),
    HIT_EFFECT(false, HitEffectProperty::class);
}