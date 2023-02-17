package net.xst.RawGensPlugin

import net.luckperms.api.LuckPerms
import net.xst.RawGensPlugin.Utils.VanishManager
import net.xst.RawGensPlugin.Utils.WarnsManager
import net.xst.RawGensPlugin.commands.Spawn.spawn_command
import net.xst.RawGensPlugin.commands.Vanish.VanishCommand
import net.xst.RawGensPlugin.commands.Vanish.listeners.VanishPlayerOnJoin
import net.xst.RawGensPlugin.commands.Warn.history_command
import net.xst.RawGensPlugin.commands.Warn.unwarn_command
import net.xst.RawGensPlugin.commands.Warn.warn_command
import net.xst.common.RawGensPlugin
import org.bukkit.Bukkit
import java.io.File
import java.util.*

class RawGensCorePlugin : RawGensPlugin() {
	companion object{
		lateinit var plugin:RawGensCorePlugin
			private set
		lateinit var warnsmanager:WarnsManager
		lateinit var api:LuckPerms
		lateinit var warnsDir:File
		lateinit var vanishManager:VanishManager
	}
	override suspend fun onEnableAsync() {
		super.onEnableAsync()

		//Inits
		plugin = this
		warnsmanager = WarnsManager(this)
		vanishManager = VanishManager(this)
		warnsDir = File(plugin.dataFolder,"warns")
		if(!warnsDir.exists()){
			warnsDir.mkdirs()
		}

		//Luckperms
		api = Bukkit.getServicesManager().getRegistration(LuckPerms::class.java)!!.provider

		//Staff utils
			//warns
			registerCommands(warn_command::class,unwarn_command::class,history_command::class)
			//spawn
			registerCommands(spawn_command::class)
			//Vanish
			registerCommands(VanishCommand::class)
			registerListeners(VanishPlayerOnJoin::class)
		//Announce

		//Config
		plugin.config.options().copyDefaults()
		plugin.saveDefaultConfig()

	}

	override suspend fun onDisableAsync() {
		super.onDisableAsync()
	}
}