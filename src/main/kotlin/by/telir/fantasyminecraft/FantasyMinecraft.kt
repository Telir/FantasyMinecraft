package by.telir.fantasyminecraft

import by.telir.fantasyminecraft.fantasy.command.AttributeCommand
import by.telir.fantasyminecraft.fantasy.command.DebugCommand
import by.telir.fantasyminecraft.fantasy.command.EffectCommand
import by.telir.fantasyminecraft.fantasy.command.UserCommand
import by.telir.fantasyminecraft.fantasy.game.listener.combat.*
import by.telir.fantasyminecraft.fantasy.game.listener.help.AttackInfoEvent
import by.telir.fantasyminecraft.fantasy.game.listener.help.InventoryDropInfoEvent
import by.telir.fantasyminecraft.fantasy.game.listener.regen.HealthRegenEvent
import by.telir.fantasyminecraft.fantasy.game.listener.restrictions.AntiHungerEvent
import by.telir.fantasyminecraft.fantasy.game.listener.restrictions.DropGameItemEvent
import by.telir.fantasyminecraft.fantasy.game.listener.restrictions.MoveGameItemEvent
import by.telir.fantasyminecraft.fantasy.game.listener.restrictions.UntouchableItemEvent
import by.telir.fantasyminecraft.fantasy.game.user.User
import by.telir.fantasyminecraft.fantasy.game.user.listener.RegisterUserListener
import by.telir.fantasyminecraft.fantasy.game.user.util.UserUtil
import by.telir.fantasyminecraft.pluginutil.command.FlyCommand
import by.telir.fantasyminecraft.pluginutil.command.ReloadCommand
import by.telir.fantasyminecraft.pluginutil.listener.MinecraftCommandListener
import org.bukkit.Bukkit
import org.bukkit.command.CommandExecutor
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin
import java.util.*

class FantasyMinecraft : JavaPlugin() {
    companion object {
        lateinit var instance: FantasyMinecraft
    }

    val users = mutableMapOf<UUID, User>()

    override fun onLoad() {
        instance = this
    }

    override fun onEnable() {
        UserUtil.create(*Bukkit.getOnlinePlayers().map { it.uniqueId }.toTypedArray())
        UserUtil.runManaRegen()

        //region Commands
        executeCommand("rc", ReloadCommand())
        executeCommand("debug", DebugCommand())
        executeCommand("fly", FlyCommand())
        executeCommand("user", UserCommand())
        executeCommand("attribute", AttributeCommand())
        executeCommand("geffect", EffectCommand())
        //endregion

        //region Plugin events
        registerEvents(MinecraftCommandListener())
        registerEvents(RegisterUserListener())
        //endregion

        //region Info events
        registerEvents(AttackInfoEvent())
        registerEvents(InventoryDropInfoEvent())
        //endregion Info events

        //region Combat events
        registerEvents(BlindnessEvent())
        registerEvents(EvasionEvent())
        registerEvents(TrueStrikeEvent())

        registerEvents(ModifyOutDamageEvent())
        registerEvents(ReturnDamageEvent())
        registerEvents(ModifyIncDamageEvent())

        registerEvents(LifestealEvent())
        //endregion

        //region Heal event
        registerEvents(HealthRegenEvent())
        //endregion

        //region Restriction events
        registerEvents(AntiHungerEvent())
        registerEvents(MoveGameItemEvent())
        registerEvents(UntouchableItemEvent())
        registerEvents(DropGameItemEvent())
        //endregion
    }

    override fun onDisable() {

    }

    fun registerEvents(listener: Listener) {
        server.pluginManager.registerEvents(listener, this)
    }

    fun executeCommand(commandName: String, executor: CommandExecutor) {
        getCommand(commandName).executor = executor
    }
}