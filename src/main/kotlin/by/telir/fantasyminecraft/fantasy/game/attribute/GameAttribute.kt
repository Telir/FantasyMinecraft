package by.telir.fantasyminecraft.fantasy.game.attribute

import by.telir.fantasyminecraft.fantasy.game.attribute.modifier.AttributeModifier
import by.telir.fantasyminecraft.fantasy.game.attribute.modifier.AttributeModifier.OperationType.ADD
import by.telir.fantasyminecraft.fantasy.game.attribute.modifier.AttributeModifier.OperationType.SCALAR
import by.telir.fantasyminecraft.fantasy.game.attribute.type.AttributeType
import kotlin.math.max

class GameAttribute(
    val attributeType: AttributeType,
) {
    var finalValue: Double = 0.0
    var baseValue: Double = attributeType.defaultValue

    private var validModifiers = mutableSetOf<AttributeModifier>()
    private var modifiers = mutableListOf<AttributeModifier>()

    fun addModifier(modifier: AttributeModifier) {
        modifiers.add(modifier)
        update()
    }

    private fun findModifier(name: String): AttributeModifier? {
        for (modifier in validModifiers) {
            if (modifier.name == name) return modifier
        }
        return null
    }

    private fun update() {
        updateModifiers()

        var add = 0.0
        var scalar = 1.0
        if (validModifiers.isNotEmpty()) {
            validModifiers.forEach {
                when (it.operation) {
                    ADD -> add += it.amount
                    SCALAR -> scalar += it.amount
                }
            }
        }

        finalValue = (baseValue + add) * scalar
    }

    private fun updateModifiers() {
        validModifiers.clear()
        if (modifiers.isEmpty()) {
            return
        }

        modifiers.forEach {
            val targetModifier: AttributeModifier? = findModifier(it.name)

            if (targetModifier == null) {
                validModifiers.add(it.copy())
            } else {
                if (targetModifier.isUnstackable || it.isUnstackable) {
                    targetModifier.amount = max(targetModifier.amount, it.amount)
                } else {
                    targetModifier.amount += it.amount
                }
            }
        }
    }

    override fun toString(): String {
        return "GameAttribute(gameAttributeType=$attributeType, finalValue=$finalValue, baseValue=$baseValue, validModifiers=$validModifiers, modifiers=$modifiers)"
    }

    init {
        update()
    }
}