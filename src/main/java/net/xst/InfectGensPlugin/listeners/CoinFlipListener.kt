package net.xst.InfectGensPlugin.listeners

import CoinFlipInventoryHolder
import com.google.inject.Inject
import net.xst.InfectGensPlugin.InfectGensCorePlugin
import net.xst.InfectGensPlugin.InfectGensCorePlugin.Companion.CFManager
import net.xst.InfectGensPlugin.InfectGensCorePlugin.Companion.accepted_challenges
import net.xst.InfectGensPlugin.InfectGensCorePlugin.Companion.pluginmsg
import net.xst.InfectGensPlugin.InfectGensCorePlugin.Companion.results
import net.xst.InfectGensPlugin.commands.CoinFlipCommand
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent

class CoinFlipListener @Inject constructor(private val plugin: InfectGensCorePlugin) : Listener {

	@EventHandler
	suspend fun onInventoryClick(event: InventoryClickEvent) {
		val player = event.whoClicked as? Player ?: return
		if (event.inventory.holder !is CoinFlipInventoryHolder) {
			return
		}
		event.isCancelled = true

		val inventoryHolder = event.inventory.holder as CoinFlipInventoryHolder
		val result = results[player] ?: return
		if (result != InfectGensCorePlugin.Companion.CoinFlipResult.PENDING) {
			return
		}

		val item = event.currentItem ?: return
		if (item.type != Material.PAPER && item.type != Material.DIAMOND) {
			return
		}

		val heads = item.type == Material.DIAMOND
		if(!accepted_challenges.containsKey(player)) return
		val challenger = accepted_challenges.get(player)!!
		results[player] = if (heads) InfectGensCorePlugin.Companion.CoinFlipResult.HEADS else InfectGensCorePlugin.Companion.CoinFlipResult.TAILS
		if(results[player]?.name == "HEADS"){
			results[challenger] = InfectGensCorePlugin.Companion.CoinFlipResult.TAILS
		}else{
			results[challenger] = InfectGensCorePlugin.Companion.CoinFlipResult.HEADS
		}
		player.sendMessage("You chose ${if (heads) "heads" else "tails"}!")
		inventoryHolder.getPlayer(challenger).sendMessage("${player.name} has chosen ${if (heads) "heads" else "tails"}!")
		CFManager.handelResults(player, challenger)
		player.closeInventory()
	}
}