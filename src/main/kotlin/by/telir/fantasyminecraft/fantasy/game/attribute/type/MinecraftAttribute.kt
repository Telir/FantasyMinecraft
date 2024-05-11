package by.telir.fantasyminecraft.fantasy.game.attribute.type

import org.bukkit.attribute.Attribute

enum class MinecraftAttribute(val realName: String, val bukkitAttribute: Attribute) {
    GENERIC_MAX_HEALTH("generic.maxHealth", Attribute.GENERIC_MAX_HEALTH),
    GENERIC_FOLLOW_RANGE("generic.followRange", Attribute.GENERIC_FOLLOW_RANGE),
    GENERIC_KNOCKBACK_RESISTANCE("generic.knockbackResistance", Attribute.GENERIC_KNOCKBACK_RESISTANCE),
    GENERIC_MOVEMENT_SPEED("generic.movementSpeed", Attribute.GENERIC_MOVEMENT_SPEED),
    GENERIC_FLYING_SPEED("generic.flyingSpeed", Attribute.GENERIC_FLYING_SPEED),
    GENERIC_ATTACK_DAMAGE("generic.attackDamage", Attribute.GENERIC_ATTACK_DAMAGE),
    GENERIC_ATTACK_SPEED("generic.attackSpeed", Attribute.GENERIC_ATTACK_SPEED),
    GENERIC_ARMOR("generic.armor", Attribute.GENERIC_ARMOR),
    GENERIC_ARMOR_TOUGHNESS("generic.armorToughness", Attribute.GENERIC_ARMOR_TOUGHNESS),
    GENERIC_LUCK("generic.luck", Attribute.GENERIC_LUCK),
    HORSE_JUMP_STRENGTH("horseJumpStrength", Attribute.HORSE_JUMP_STRENGTH),
    ZOMBIE_SPAWN_REINFORCEMENTS("zombieSpawnReinforcements", Attribute.ZOMBIE_SPAWN_REINFORCEMENTS);
}