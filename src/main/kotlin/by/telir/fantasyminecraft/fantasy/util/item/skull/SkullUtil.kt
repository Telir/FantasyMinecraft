package by.telir.fantasyminecraft.fantasy.util.item.skull

import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta

object SkullUtil {
    fun getPlayerSkull(player: Player): ItemStack {
        val meta = Bukkit.getItemFactory().getItemMeta(Material.SKULL_ITEM) as SkullMeta
        meta.setOwningPlayer(player)

        val skull = ItemStack(Material.SKULL_ITEM, 1, 3.toShort())
        skull.setItemMeta(meta)
        return skull
    }
}