package by.telir.fantasyminecraft.fantasy.game.attribute.type

enum class AttributeType(val minecraftAttribute: MinecraftAttribute?, val defaultValue: Double) {
    DAMAGE(MinecraftAttribute.GENERIC_ATTACK_DAMAGE, 1.0),
    DEFENSE(MinecraftAttribute.GENERIC_ARMOR, 0.0),
    TOUGHNESS(MinecraftAttribute.GENERIC_ARMOR_TOUGHNESS, 0.0),
    ATTACK_SPEED(MinecraftAttribute.GENERIC_ATTACK_SPEED, 4.0),
    MOVEMENT_SPEED(MinecraftAttribute.GENERIC_MOVEMENT_SPEED, 0.1),
    HEALTH(MinecraftAttribute.GENERIC_MAX_HEALTH, 20.0),
    HEALTH_REGEN(null, 1.0),
    MANA(null, 0.0),
    MANA_REGEN(null, 0.0),
    MAGIC_RESISTANCE(null, 0.0),
    STRENGTH(null, 0.0),
    AGILITY(null, 0.0),
    INTELLIGENCE(null, 0.0);
}