package by.telir.fantasyminecraft

import by.telir.fantasyminecraft.fantasy.command.AttributeCommand
import by.telir.fantasyminecraft.fantasy.command.DebugCommand
import by.telir.fantasyminecraft.fantasy.command.UserCommand
import by.telir.fantasyminecraft.fantasy.game.listener.combat.*
import by.telir.fantasyminecraft.fantasy.game.listener.gameitem.ActiveUseEvent
import by.telir.fantasyminecraft.fantasy.game.listener.gameitem.DropGameItemEvent
import by.telir.fantasyminecraft.fantasy.game.listener.gameitem.PickupGameItemEvent
import by.telir.fantasyminecraft.fantasy.game.listener.gameitem.UntouchableItemEvent
import by.telir.fantasyminecraft.fantasy.game.listener.help.AttackInfoEvent
import by.telir.fantasyminecraft.fantasy.game.listener.regen.HealthRegenEvent
import by.telir.fantasyminecraft.fantasy.game.listener.restrictions.AntiHungerEvent
import by.telir.fantasyminecraft.fantasy.game.listener.restrictions.MoveGameItemEvent
import by.telir.fantasyminecraft.fantasy.game.showhealth.listener.ShowHealthEvent
import by.telir.fantasyminecraft.fantasy.game.showhealth.manager.ShowHealthManager
import by.telir.fantasyminecraft.fantasy.game.user.User
import by.telir.fantasyminecraft.fantasy.game.user.listener.RegisterUserListener
import by.telir.fantasyminecraft.fantasy.game.user.util.UserUtil
import by.telir.fantasyminecraft.pluginutil.command.ReloadCommand
import by.telir.fantasyminecraft.pluginutil.listener.MinecraftCommandListener
import org.bukkit.Bukkit
import org.bukkit.command.CommandExecutor
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin
import java.util.*


lateinit var instance: FantasyMinecraft

class FantasyMinecraft : JavaPlugin() {
    val users = mutableMapOf<UUID, User>()
    lateinit var showHealthManager: ShowHealthManager

    override fun onEnable() {
        instance = this
        showHealthManager = ShowHealthManager()

        UserUtil.create(*Bukkit.getOnlinePlayers().map { it.uniqueId }.toTypedArray())
        UserUtil.runManaRegen()

        showHealthManager.setupBelowHealthbar()
        Bukkit.getOnlinePlayers().forEach { showHealthManager.updateHealthbarBelow(it) }

        //region Commands
        executeCommand("rc", ReloadCommand())
        executeCommand("user", UserCommand())
        executeCommand("attribute", AttributeCommand())
        executeCommand("debug", DebugCommand())
        //endregion

        //region Plugin events
        registerEvents(MinecraftCommandListener())
        registerEvents(RegisterUserListener())
        //endregion

        //region Info events
        registerEvents(AttackInfoEvent())
        //endregion Info events

        //region Combat events
        registerEvents(BlindnessEvent())
        registerEvents(EvasionEvent())
        registerEvents(TrueStrikeEvent())

        registerEvents(ModifyOutDamageEvent())
        registerEvents(ReturnDamageEvent())
        registerEvents(ModifyIncDamageEvent())
        registerEvents(HitEffectEvent())

        registerEvents(LifestealEvent())
        //endregion

        //region Other events
        registerEvents(HealthRegenEvent())
        registerEvents(ShowHealthEvent())
        //endregion

        //region GameItem events
        registerEvents(ActiveUseEvent())
        registerEvents(DropGameItemEvent())
        registerEvents(PickupGameItemEvent())
        //endregion

        //region Restriction events
        registerEvents(AntiHungerEvent())
        registerEvents(MoveGameItemEvent())
        registerEvents(UntouchableItemEvent())
        //endregion
    }

    override fun onDisable() {
        showHealthManager.removeBelowHealthbar()
        server.scoreboardManager.mainScoreboard.teams.forEach { it.unregister() }
    }

    private fun registerEvents(listener: Listener) {
        server.pluginManager.registerEvents(listener, this)
    }

    private fun executeCommand(commandName: String, executor: CommandExecutor) {
        getCommand(commandName).executor = executor
    }
}