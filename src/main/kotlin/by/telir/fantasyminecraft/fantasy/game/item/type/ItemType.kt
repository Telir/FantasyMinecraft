package by.telir.fantasyminecraft.fantasy.game.item.type

import net.minecraft.server.v1_12_R1.EnumItemSlot

enum class ItemType(val slot: EnumItemSlot?, val playerBased: Boolean) {
    ARTIFACT(null, true),
    WEAPON(EnumItemSlot.MAINHAND, false),
    CONSUMABLE(null, true),
    OFFHAND(EnumItemSlot.OFFHAND, false);
}