package by.telir.fantasyminecraft.fantasy.game.item

import by.telir.fantasyminecraft.fantasy.game.active.GameActive
import by.telir.fantasyminecraft.fantasy.game.active.type.ActiveType
import by.telir.fantasyminecraft.fantasy.game.attribute.modifier.AttributeModifier
import by.telir.fantasyminecraft.fantasy.game.attribute.type.AttributeType
import by.telir.fantasyminecraft.fantasy.game.item.type.ItemType
import by.telir.fantasyminecraft.fantasy.game.property.GameProperty
import by.telir.fantasyminecraft.fantasy.game.property.type.PropertyType
import by.telir.fantasyminecraft.pluginutil.nms.nbt.NBTUtil
import org.bukkit.inventory.ItemStack

class GameItem(val itemStack: ItemStack, val itemType: ItemType) {
    var gameName: String = ""
        set(value) {
            field = value
            NBTUtil.putNBT(itemStack, "gameName", String::class.java, value)
        }
    var parentName: String = ""
    var amount: Int = 1

    val modifiers = mutableMapOf<AttributeType, AttributeModifier>()
    val properties = mutableMapOf<PropertyType, GameProperty>()
    val actives = mutableMapOf<ActiveType, GameActive>()
}