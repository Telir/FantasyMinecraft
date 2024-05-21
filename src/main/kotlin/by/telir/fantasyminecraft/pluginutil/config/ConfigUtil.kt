package by.telir.fantasyminecraft.pluginutil.config

import by.telir.fantasyminecraft.instance
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File

object ConfigUtil {
    fun getConfig(configName: String): FileConfiguration {
        val configFile = File(instance.dataFolder, "$configName.yml")
        if (!configFile.exists()) {
            configFile.parentFile.mkdirs()
            instance.saveResource(configName, false)
        }
        return YamlConfiguration.loadConfiguration(configFile)
    }
}