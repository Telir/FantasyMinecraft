package by.telir.fantasyminecraft.fantasy.game.item.manager

import by.telir.fantasyminecraft.fantasy.game.item.GameItem
import by.telir.fantasyminecraft.fantasy.game.item.type.ItemType
import by.telir.fantasyminecraft.fantasy.game.item.util.GameItemUtil
import by.telir.fantasyminecraft.fantasy.game.user.User
import by.telir.fantasyminecraft.fantasy.reader.FantasyConfigReader
import by.telir.fantasyminecraft.fantasy.util.item.dye.ColoredItemUtil
import by.telir.fantasyminecraft.pluginutil.config.ConfigUtil
import org.bukkit.Material
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.inventory.ItemStack

class GameItemManager {
    private companion object {
        private val ARTIFACTS = ConfigUtil.getConfig("artifacts")
        private val WEAPONS = ConfigUtil.getConfig("weapons")
        private val CONSUMABLES = ConfigUtil.getConfig("consumables")
        private val OFFHAND = ConfigUtil.getConfig("offhand")
    }

    fun add(user: User, gameName: String, type: ItemType, isPickup: Boolean) {
        val gameItem = create(gameName, type) ?: throw RuntimeException("Illegal gameName")

        user.addUncheckedItem(gameItem, type)

        if (!isPickup) user.player.inventory.addItem(gameItem.itemStack)
        user.update()
    }

    fun remove(user: User, itemStack: ItemStack, isDrop: Boolean) {
        val gameName = GameItemUtil.getGameName(itemStack) ?: return

        val findGameItem = user.findGameItem(gameName, itemStack)
        if (findGameItem != null) user.removeUncheckedItem(findGameItem)

        if (isDrop) {
            for (i in 0 until itemStack.amount) {
                remove(user, itemStack, false)
                itemStack.amount--
            }
        } else {
            itemStack.amount--
        }

        user.update()
    }

    private fun create(gameName: String, type: ItemType): GameItem? {
        if (!inConfig(gameName, type)) return null

        val itemSection = getConfig(type).getConfigurationSection(gameName)

        val itemStack = ItemStack(Material.matchMaterial(itemSection.getString("material")))
        val itemManager = ItemManager(itemStack)
        if (itemSection.contains("color")) itemManager.color = ColoredItemUtil.ItemColor.valueOf(
            itemSection.getString("color").uppercase()

        )
        if (type != ItemType.CONSUMABLE) itemManager.createUUID()
        if (type == ItemType.WEAPON) {
            itemManager.isUnbreakable = true
            itemManager.removeAttributeModifiers()
        }
        itemManager.isGlow = itemSection.getBoolean("isGlow")

        itemManager.displayName = itemSection.getString("displayName")
        itemManager.lore = itemSection.getStringList("lore")

        val gameItem = GameItem(itemStack, type)
        gameItem.gameName = gameName
        gameItem.parentName = itemSection.getString("parentName")

        val fantasyConfigReader = FantasyConfigReader(itemSection)
        gameItem.properties.putAll(fantasyConfigReader.checkForProperties())
        gameItem.modifiers.putAll(fantasyConfigReader.checkForModifiers(gameItem.parentName))
        gameItem.actives.putAll(fantasyConfigReader.checkForActives())

        return gameItem
    }

    fun inConfig(gameName: String, type: ItemType): Boolean {
        return getConfig(type).getKeys(false).contains(gameName)
    }

    fun getConfig(type: ItemType): FileConfiguration {
        return when (type) {
            ItemType.ARTIFACT -> ARTIFACTS
            ItemType.WEAPON -> WEAPONS
            ItemType.CONSUMABLE -> CONSUMABLES
            ItemType.OFFHAND -> OFFHAND
        }
    }
}