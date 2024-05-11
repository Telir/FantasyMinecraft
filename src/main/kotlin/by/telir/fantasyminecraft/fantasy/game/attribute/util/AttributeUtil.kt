package by.telir.fantasyminecraft.fantasy.game.attribute.util

import by.telir.fantasyminecraft.fantasy.game.attribute.type.MinecraftAttribute
import org.bukkit.entity.LivingEntity

class AttributeUtil {
    companion object {
        fun setValue(entity: LivingEntity, minecraftAttribute: MinecraftAttribute, amount: Double) {
            entity.getAttribute(minecraftAttribute.bukkitAttribute).baseValue = amount
        }

        fun getValue(entity: LivingEntity, minecraftAttribute: MinecraftAttribute): Double {
            return entity.getAttribute(minecraftAttribute.bukkitAttribute).value
        }
    }
}