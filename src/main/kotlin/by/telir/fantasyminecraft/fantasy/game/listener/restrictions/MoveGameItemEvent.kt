package by.telir.fantasyminecraft.fantasy.game.listener.restrictions

import by.telir.fantasyminecraft.fantasy.game.item.type.ItemType
import by.telir.fantasyminecraft.fantasy.game.item.util.GameItemUtil
import by.telir.fantasyminecraft.fantasy.game.user.util.UserUtil
import by.telir.fantasyminecraft.instance
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryAction
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryDragEvent
import org.bukkit.event.player.PlayerItemHeldEvent
import org.bukkit.scheduler.BukkitRunnable

class MoveGameItemEvent : Listener {
    @EventHandler
    fun onPlayerItemHeld(e: PlayerItemHeldEvent) {
        var isUpdate = false

        val newItemStack = e.player.inventory.getItem(e.newSlot)
        if (newItemStack != null) {
            if (!GameItemUtil.hasGameName(newItemStack)) return

            val gameName = GameItemUtil.getGameName(newItemStack)!!
            val itemType = GameItemUtil.getItemType(gameName)

            if (itemType == ItemType.WEAPON) isUpdate = true
        }

        val oldItemStack = e.player.inventory.getItem(e.previousSlot)
        if (oldItemStack != null) {
            if (!GameItemUtil.hasGameName(oldItemStack)) return

            val gameName = GameItemUtil.getGameName(oldItemStack)!!
            val itemType = GameItemUtil.getItemType(gameName)

            if (itemType == ItemType.WEAPON) isUpdate = true
        }
        if (isUpdate) {
            object : BukkitRunnable() {
                override fun run() {
                    UserUtil.find(e.player.uniqueId)!!.update()
                }
            }.runTaskLater(instance, 1)
        }
    }

    @EventHandler
    fun onInventoryClick(e: InventoryClickEvent) {
        val available = mutableSetOf(
            InventoryAction.PLACE_ONE,
            InventoryAction.PLACE_SOME,
            InventoryAction.PLACE_ALL,
            InventoryAction.HOTBAR_SWAP,
            InventoryAction.SWAP_WITH_CURSOR
        )
        val action = e.action
        if (!available.contains(action)) return

        val player = e.whoClicked as Player
        val user = UserUtil.find(player.uniqueId)!!

        var isUpdate = false

        val cursorItem = e.cursor
        if (cursorItem != null) {
            if (!GameItemUtil.hasGameName(cursorItem)) return

            val gameName = GameItemUtil.getGameName(cursorItem)!!
            val itemType = GameItemUtil.getItemType(gameName)

            if (itemType == ItemType.WEAPON) isUpdate = true
        }

        val currentItem = e.currentItem
        if (currentItem != null) {
            if (!GameItemUtil.hasGameName(currentItem)) return

            val gameName = GameItemUtil.getGameName(currentItem)!!
            val itemType = GameItemUtil.getItemType(gameName)

            if (itemType == ItemType.WEAPON) isUpdate = true
        }

        object : BukkitRunnable() {
            override fun run() {
                val itemInMainHand = player.inventory.itemInMainHand
                if (!GameItemUtil.hasGameName(itemInMainHand)) return

                val gameName = GameItemUtil.getGameName(itemInMainHand)!!
                val itemType = GameItemUtil.getItemType(gameName)

                if (itemType == ItemType.WEAPON) isUpdate = true
                if (isUpdate) user.update()
            }
        }.runTaskLater(instance, 1)
    }

    @EventHandler
    fun onInventoryDrag(e: InventoryDragEvent) {
        val itemStack = e.oldCursor
        if (!GameItemUtil.hasGameName(itemStack)) return

        val gameName = GameItemUtil.getGameName(itemStack)!!
        val itemType = GameItemUtil.getItemType(gameName)

        if (itemType == ItemType.WEAPON) UserUtil.find((e.whoClicked.uniqueId))!!.update()
    }
}
