package by.telir.fantasyminecraft.fantasy.game.attribute.modifier

import by.telir.fantasyminecraft.pluginutil.config.ConfigUtil

data class AttributeModifier(
    val name: String,
    var amount: Double,
    var operation: OperationType,
    val isUnstackable: Boolean,
) {

    enum class OperationType {
        ADD,
        SCALAR
    }

    abstract class DotaModifier(val name: String) {
        companion object {
            val config = ConfigUtil.getConfig("dotaAttribute")
        }

        class StrengthModifier(name: String, val health: Double, val healthRegen: Double) : DotaModifier(name) {
            companion object {
                val strengthCfg = config.getConfigurationSection("strength")!!
            }
        }

        class AgilityModifier(name: String, val defense: Double, val attackSpeed: Double) : DotaModifier(name) {
            companion object {
                val agilityCfg = config.getConfigurationSection("agility")!!
            }
        }

        class IntelligenceModifier(
            name: String,
            val mana: Double,
            val manaRegen: Double,
            val magicResistance: Double
        ) : DotaModifier(name) {
            companion object {
                val intelligenceCfg = config.getConfigurationSection("intelligence")!!
            }
        }
    }
}