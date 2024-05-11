package by.telir.fantasyminecraft.fantasy.game.item.manager

import by.telir.fantasyminecraft.fantasy.util.item.dye.ColoredItemUtil
import by.telir.fantasyminecraft.fantasy.util.item.dye.ColoredItemUtil.ItemColor
import by.telir.fantasyminecraft.fantasy.util.item.nbt.NBTUtil
import net.minecraft.server.v1_12_R1.AttributeModifier
import net.minecraft.server.v1_12_R1.EnumItemSlot
import org.bukkit.Material
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import java.util.*

class ItemManager(private val itemStack: ItemStack) {
    var displayName: String
        get() = itemStack.itemMeta.displayName
        set(value) {
            val itemMeta: ItemMeta = itemStack.itemMeta
            itemMeta.displayName = value
            itemStack.itemMeta = itemMeta
        }
    var lore: List<String>
        get() = itemStack.itemMeta.lore
        set(value) {
            val itemMeta: ItemMeta = itemStack.itemMeta
            itemMeta.lore = value
            itemStack.itemMeta = itemMeta
        }

    var isGlow: Boolean = false
        set(value) {
            val itemMeta = itemStack.itemMeta
            if (value) {
                itemMeta.addEnchant(Enchantment.MENDING, 0, true)
                itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS)
            } else {
                itemMeta.removeEnchant(Enchantment.MENDING)
                itemMeta.removeItemFlags(ItemFlag.HIDE_ENCHANTS)
            }
            itemStack.itemMeta = itemMeta
            field = value
        }

    var isUntouchable: Boolean = false
        set(value) {
            if (value) {
                NBTUtil.putNBT(itemStack, "isUntouchable", Boolean::class.java, true)
            } else NBTUtil.removeNBT(itemStack, "isUntouchable")
            field = value
        }
        get() = NBTUtil.hasTag(itemStack, "isUntouchable")

    var isUnbreakable: Boolean = false
        set(value) {
            val itemMeta = itemStack.itemMeta
            if (value) {
                itemMeta.isUnbreakable = true
                itemMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE)
            } else {
                itemMeta.isUnbreakable = false
                itemMeta.removeItemFlags(ItemFlag.HIDE_UNBREAKABLE)
            }
            itemStack.itemMeta = itemMeta
            field = value
        }
        get() = itemStack.itemMeta.hasItemFlag(ItemFlag.HIDE_UNBREAKABLE)


    var color: ItemColor? = null
        set(value) {
            if (itemStack.type != Material.INK_SACK) return
            ColoredItemUtil.setColor(itemStack, value!!, false)
            field = value
        }

    fun createUUID() {
        NBTUtil.putNBT(itemStack, "uuid", UUID::class.java, UUID.randomUUID())
    }

    fun removeAttributeModifiers() {
        val nmsItem = CraftItemStack.asNMSCopy(itemStack)
        nmsItem.a("empty", AttributeModifier("empty", 0.0, 0), EnumItemSlot.MAINHAND)

        itemStack.setItemMeta(CraftItemStack.getItemMeta(nmsItem))

        val itemMeta = itemStack.itemMeta
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES)
        itemStack.setItemMeta(itemMeta)
    }
}