package by.telir.fantasyminecraft.fantasy.game.item.util

import by.telir.fantasyminecraft.fantasy.game.item.manager.GameItemManager
import by.telir.fantasyminecraft.fantasy.game.item.type.ItemType
import by.telir.fantasyminecraft.pluginutil.nms.nbt.NBTUtil
import org.bukkit.inventory.ItemStack

object GameItemUtil {
    fun isGameItem(itemStack: ItemStack): Boolean {
        val nbt = NBTUtil.getNBT(itemStack, "gameName", String::class.java)
        return !nbt.isNullOrEmpty()
    }

    fun getGameName(itemStack: ItemStack): String? {
        val nbt = NBTUtil.getNBT(itemStack, "gameName", String::class.java)
        if (!nbt.isNullOrEmpty()) return nbt
        return null
    }

    fun hasGameName(itemStack: ItemStack): Boolean {
        return NBTUtil.hasTag(itemStack, "gameName")
    }

    fun getItemType(gameName: String): ItemType? {
        val gameItemManager = GameItemManager()
        for (entry in ItemType.entries) {
            if (gameItemManager.inConfig(gameName, entry)) return entry
        }
        return null
    }

    fun getNames(itemType: ItemType): MutableSet<String> {
        return GameItemManager().getConfig(itemType).getKeys(false)
    }
}