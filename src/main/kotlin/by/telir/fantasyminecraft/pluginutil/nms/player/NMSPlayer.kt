package by.telir.fantasyminecraft.pluginutil.nms.player

import com.mojang.authlib.GameProfile
import net.minecraft.server.v1_12_R1.*
import org.bukkit.Bukkit
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer
import org.bukkit.entity.Player

class NMSPlayer(val player: Player) {
    fun getAttackCooldown(): Double {
        return (player as CraftPlayer).handle.n(0f).toDouble()
    }

    fun getAbsorption(): Float {
        return (player as CraftPlayer).handle.absorptionHearts
    }

    fun setAbsorption(value: Float) {
        val entityPlayer = (player as CraftPlayer).handle
        entityPlayer.absorptionHearts = value
    }

    fun sendActionBar(message: String) {
        val craftPlayer = player as CraftPlayer
        val actionbar = IChatBaseComponent.ChatSerializer.a("{\"text\": \"$message\"}")
        val packet = PacketPlayOutChat(actionbar, ChatMessageType.GAME_INFO)

        craftPlayer.handle.playerConnection.sendPacket(packet)
    }

    fun setPlayerName(name: String?) {
        val gp = (player as CraftPlayer).profile
        try {
            val nameField = GameProfile::class.java.getDeclaredField("name")
            nameField.isAccessible = true
            nameField[gp] = name
            nameField.isAccessible = false
        } catch (ex: IllegalAccessException) {
            throw IllegalStateException(ex)
        } catch (ex: NoSuchFieldException) {
            throw IllegalStateException(ex)
        }
        for (pl in Bukkit.getOnlinePlayers()) {
            if (pl === player) continue
            (pl as CraftPlayer).handle.playerConnection.sendPacket(PacketPlayOutEntityDestroy(player.getEntityId()))
            pl.handle.playerConnection.sendPacket(
                PacketPlayOutPlayerInfo(
                    PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER,
                    player.handle
                )
            )
            pl.handle.playerConnection.sendPacket(
                PacketPlayOutPlayerInfo(
                    PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER,
                    player.handle
                )
            )
            pl.handle.playerConnection.sendPacket(PacketPlayOutNamedEntitySpawn(player.handle))
        }
    }

    fun setMovementSpeed(value: Double) {
        val nmsPlayer = (player as CraftPlayer).handle
        nmsPlayer.abilities.walkSpeed = value.toFloat()
        nmsPlayer.updateAbilities()
    }

    fun setFlySpeed(value: Double) {
        val nmsPlayer = (player as CraftPlayer).handle
        nmsPlayer.abilities.flySpeed = value.toFloat()
        nmsPlayer.updateAbilities()
    }
}