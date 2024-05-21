package by.telir.fantasyminecraft.fantasy.game.listener.gameitem

import by.telir.fantasyminecraft.fantasy.game.active.state.ActiveResult
import by.telir.fantasyminecraft.fantasy.game.item.manager.GameItemManager
import by.telir.fantasyminecraft.fantasy.game.item.type.ItemType
import by.telir.fantasyminecraft.fantasy.game.item.util.GameItemUtil
import by.telir.fantasyminecraft.fantasy.game.user.util.UserUtil
import by.telir.fantasyminecraft.javautil.math.MathUtil
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent

class ActiveUseEvent : Listener {
    @EventHandler
    fun onPlayerInteract(e: PlayerInteractEvent) {
        val itemStack = e.player.inventory.itemInMainHand

        val gameName = GameItemUtil.getGameName(itemStack) ?: return

        val player = e.player
        val user = UserUtil.find(player.uniqueId)!!

        val gameItem = user.findGameItem(gameName, itemStack) ?: return

        for (activeType in gameItem.actives.keys) {
            val result = gameItem.actives[activeType]!!.use(user, gameItem)
            when (result) {
                ActiveResult.USED -> {
                    if (gameItem.itemType == ItemType.CONSUMABLE) GameItemManager().remove(user, itemStack, false)
                    player.sendMessage("Use!")
                }

                ActiveResult.NOT_ENOUGH_MANA -> player.sendMessage("Not enough mana!")
                ActiveResult.NOT_ENOUGH_HEALTH -> player.sendMessage("Not enough health!")
                ActiveResult.ON_COOLDOWN -> player.sendMessage(
                    "cooldown: ${MathUtil.round(user.getCooldownTime(gameItem), 1)}"
                )
            }
        }
    }
}