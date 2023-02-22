package net.xst.InfectGensPlugin

import net.milkbowl.vault.economy.Economy
import net.xst.InfectGensPlugin.Utils.CoinFlipManager
import net.xst.InfectGensPlugin.commands.CoinFlipAcceptCommand
import net.xst.InfectGensPlugin.commands.CoinFlipCommand
import net.xst.InfectGensPlugin.listeners.CoinFlipListener
import net.xst.common.InfectGensPlugin
import org.bukkit.ChatColor
import org.bukkit.entity.Player


class InfectGensCorePlugin : InfectGensPlugin() {
	companion object{
		lateinit var plugin:InfectGensCorePlugin
			private set
		lateinit var vault_econ:Economy
		lateinit var challenges:MutableMap<Player,Player>
		lateinit var bets:MutableMap<Player,Double>
		lateinit var results:MutableMap<Player,CoinFlipResult>
		lateinit var pluginmsg:String
		lateinit var CFManager:CoinFlipManager
		lateinit var accepted_challenges:MutableMap<Player,Player>
		enum class CoinFlipResult{
			PENDING,HEADS,TAILS
		}

	}
	override suspend fun onEnableAsync() {
		super.onEnableAsync()

		//Inits
		plugin = this
		pluginmsg = ChatColor.translateAlternateColorCodes('&',plugin.config.getString("InfectGens.pluginmsg")!!)
		CFManager = CoinFlipManager(this)
		challenges = mutableMapOf()
		bets = mutableMapOf()
		results = mutableMapOf()
		accepted_challenges = mutableMapOf()

		//Setsup VaultEcon
		if (!setupEconomy()) {
			logger.severe(String.format("[%s] - Disabled due to no Vault dependency found!", description.name))
			server.pluginManager.disablePlugin(this)
			return
		}
		//CoinFlip
		registerCommands(CoinFlipCommand::class,CoinFlipAcceptCommand::class)
		registerListeners(CoinFlipListener::class)

		//Config
		plugin.config.options().copyDefaults()
		plugin.saveDefaultConfig()

	}

	override suspend fun onDisableAsync() {
		super.onDisableAsync()
	}

	private fun setupEconomy(): Boolean {
		if (server.pluginManager.getPlugin("Vault") == null) {
			return false
		}
		val rsp = server.servicesManager.getRegistration(Economy::class.java)
			?: return false
		vault_econ = rsp.provider
		return true
	}
}