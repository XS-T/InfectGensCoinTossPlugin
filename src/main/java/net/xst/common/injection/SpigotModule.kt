package net.xst.common.injection

import dev.misfitlabs.kotlinguice4.KotlinModule
import net.xst.common.InfectGensPlugin
import org.bukkit.Server
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.PluginManager

class SpigotModule(private val plugin: InfectGensPlugin) : KotlinModule() {
	override fun configure() {
		bind<Server>().toInstance(plugin.server)
		bind<Plugin>().toInstance(plugin)
		bind<PluginManager>().toInstance(plugin.server.pluginManager)
		bind(plugin.javaClass).toInstance(plugin)
	}
}