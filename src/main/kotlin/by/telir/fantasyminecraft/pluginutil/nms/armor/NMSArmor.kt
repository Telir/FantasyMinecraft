package by.telir.fantasyminecraft.pluginutil.nms.armor

import net.minecraft.server.v1_12_R1.ItemArmor
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack
import org.bukkit.inventory.ItemStack

class NMSArmor(armorItem: ItemStack) {
    private val nmsArmor = CraftItemStack.asNMSCopy(armorItem).item as? ItemArmor
    val defense = nmsArmor?.d
    val toughness = nmsArmor?.e
}