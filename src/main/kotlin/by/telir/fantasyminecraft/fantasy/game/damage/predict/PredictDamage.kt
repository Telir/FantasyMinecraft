package by.telir.fantasyminecraft.fantasy.game.damage.predict

import by.telir.fantasyminecraft.fantasy.game.attribute.type.AttributeType
import by.telir.fantasyminecraft.fantasy.game.attribute.type.MinecraftAttribute
import by.telir.fantasyminecraft.fantasy.game.attribute.util.AttributeUtil
import by.telir.fantasyminecraft.fantasy.game.user.util.UserUtil
import by.telir.fantasyminecraft.pluginutil.nms.armor.NMSArmor
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.LivingEntity
import org.bukkit.potion.PotionEffectType
import kotlin.math.max

class PredictDamage(private val entity: LivingEntity, private val value: Double) {
    val physical: Double
        get() {
            var defense = AttributeUtil.getValue(entity, MinecraftAttribute.GENERIC_ARMOR)
            var toughness = AttributeUtil.getValue(entity, MinecraftAttribute.GENERIC_ARMOR_TOUGHNESS)
            var protection = 0

            val armorContents = entity.equipment.armorContents
            for (armor in armorContents) {
                if (armor == null) continue
                defense += NMSArmor(armor).defense!!
                toughness += NMSArmor(armor).toughness!!
                protection += armor.getEnchantmentLevel(Enchantment.PROTECTION_ENVIRONMENTAL)
            }

            val resistanceEffect = entity.getPotionEffect(PotionEffectType.DAMAGE_RESISTANCE)

            var resistance = 0
            if (resistanceEffect != null) {
                resistance += resistanceEffect.amplifier + 1
            }

            val armorBlock = max(defense / 5, defense - 4 * value / (toughness + 8)) / 25
            val allProtectionPercent = 4.0 * protection / 100
            val resistancePercent = 20.0 * resistance / 100
            return (value) * (1 - armorBlock) * (1 - allProtectionPercent) * (1 - resistancePercent)
        }

    val magical: Double
        get() {
            val user = UserUtil.find(entity.uniqueId) ?: return value
            return max(0.0, value * (1 - user.attributes[AttributeType.MAGIC_RESISTANCE]!!.finalValue))
        }

    val pure: Double
        get() {
            return value
        }
}