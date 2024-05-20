package by.telir.fantasyminecraft.fantasy.util.item.dye

import org.bukkit.inventory.ItemStack

object ColoredItemUtil {
    fun setColor(itemStack: ItemStack, itemColor: ItemColor, isBlock: Boolean): ItemStack {
        if (isBlock) {
            itemStack.durability = itemColor.blockColor
            return itemStack
        } else {
            itemStack.durability = itemColor.dyeColor
            return itemStack
        }
    }

    enum class ItemColor(val blockColor: Short, val dyeColor: Short) {
        BLACK(15, 0),
        RED(14, 1),
        GREEN(13, 2),
        BROWN(12, 3),
        BLUE(11, 4),
        PURPLE(10, 5),
        CYAN(9, 6),
        LIGHT_GRAY(8, 7),
        GRAY(7, 8),
        PINK(6, 9),
        LIME(5, 10),
        YELLOW(4, 11),
        LIGHT_BLUE(3, 12),
        MAGENTA(2, 13),
        ORANGE(1, 14),
        WHITE(0, 15);
    }
}