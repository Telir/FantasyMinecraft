package by.telir.fantasyminecraft.fantasy.game.listener.active

import by.telir.fantasyminecraft.fantasy.game.active.state.ActiveState
import by.telir.fantasyminecraft.fantasy.game.item.type.ItemType
import by.telir.fantasyminecraft.fantasy.game.item.util.GameItemUtil
import by.telir.fantasyminecraft.fantasy.game.listener.help.InventoryDropInfoEvent
import by.telir.fantasyminecraft.fantasy.game.user.util.UserUtil
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.event.player.PlayerInteractEvent

class ActiveUseEvent : Listener {
    @EventHandler
    fun onPlayerDropItem(e: PlayerDropItemEvent) {
        if (InventoryDropInfoEvent.instance.isInventoryDrop) return

        val itemStack = e.itemDrop.itemStack

        val gameName = GameItemUtil.getGameName(itemStack) ?: return
        e.isCancelled = true

        val player = e.player
        val user = UserUtil.find(player.uniqueId)!!

        val gameItem = user.findGameItem(gameName, itemStack) ?: return
        for (activeType in gameItem.actives.keys) {
            val result = gameItem.actives[activeType]!!.use(user, gameItem)
            when (result) {
                ActiveState.USED -> {}
                ActiveState.NOT_ENOUGH_MANA -> player.sendMessage("Not enough mana!")
                ActiveState.NOT_ENOUGH_HEALTH -> player.sendMessage("Not enough health!")
                ActiveState.ON_COOLDOWN -> player.sendMessage(gameItem.actives[activeType]!!.currentCooldown.toString())
            }
        }
    }

    @EventHandler
    fun onPlayerInteract(e: PlayerInteractEvent) {
        val action = e.action
        val allow = setOf(Action.RIGHT_CLICK_AIR, Action.RIGHT_CLICK_BLOCK)
        if (action !in allow) return

        val player = e.player
        val user = UserUtil.find(player.uniqueId)!!

        val itemInOffHand = player.inventory.itemInOffHand
        val gameName = GameItemUtil.getGameName(itemInOffHand) ?: return

        val gameItem = user.findGameItem(gameName, itemInOffHand) ?: return
        for (activeType in gameItem.actives.keys) {
            val result = gameItem.actives[activeType]!!.use(user, gameItem)
            when (result) {
                ActiveState.USED -> {
                    if (gameItem.itemType == ItemType.CONSUMABLE) user.removeUncheckedItem(gameItem)
                    user.update()
                }
                ActiveState.NOT_ENOUGH_MANA -> player.sendMessage("Not enough mana!")
                ActiveState.NOT_ENOUGH_HEALTH -> player.sendMessage("Not enough health!")
                ActiveState.ON_COOLDOWN -> player.sendMessage(gameItem.actives[activeType]!!.currentCooldown.toString())
            }
        }
    }
}