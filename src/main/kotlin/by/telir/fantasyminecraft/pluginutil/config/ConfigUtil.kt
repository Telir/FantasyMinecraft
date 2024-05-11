package by.telir.fantasyminecraft.pluginutil.config

import by.telir.fantasyminecraft.FantasyMinecraft
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File

class ConfigUtil {
    companion object {
        private val PLUGIN: FantasyMinecraft = FantasyMinecraft.instance

        fun getConfig(configName: String): FileConfiguration {
            val configFile = File(PLUGIN.dataFolder, "$configName.yml")
            if (!configFile.exists()) {
                configFile.parentFile.mkdirs()
                PLUGIN.saveResource(configName, false)
            }
            return YamlConfiguration.loadConfiguration(configFile)
        }
    }
}