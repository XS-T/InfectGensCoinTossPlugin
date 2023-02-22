package net.xst.InfectGensPlugin.commands

import CoinFlipInventoryHolder
import cloud.commandframework.annotations.Argument
import cloud.commandframework.annotations.CommandDescription
import cloud.commandframework.annotations.CommandMethod
import cloud.commandframework.annotations.CommandPermission
import com.google.inject.Inject
import net.milkbowl.vault.economy.EconomyResponse
import net.xst.InfectGensPlugin.InfectGensCorePlugin
import net.xst.InfectGensPlugin.InfectGensCorePlugin.Companion.CFManager
import net.xst.InfectGensPlugin.InfectGensCorePlugin.Companion.accepted_challenges
import net.xst.InfectGensPlugin.InfectGensCorePlugin.Companion.bets
import net.xst.InfectGensPlugin.InfectGensCorePlugin.Companion.challenges
import net.xst.InfectGensPlugin.InfectGensCorePlugin.Companion.pluginmsg
import net.xst.InfectGensPlugin.InfectGensCorePlugin.Companion.results
import net.xst.InfectGensPlugin.InfectGensCorePlugin.Companion.vault_econ
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable

class CoinFlipAcceptCommand @Inject constructor(private val plugin: InfectGensCorePlugin) {
	@CommandMethod("cf challenge accept <challenger>")
	@CommandDescription("Challenge a player to a coin flip game")
	@CommandPermission("infectgens.coinflip")
	fun onAcceptChallenge(player:Player,@Argument("challenger")challenger:Player){
		if(!challenges.containsKey(challenger)|| challenges[challenger] != player){
			player.sendMessage("$pluginmsg There is no pending challenge from that player.")
			return
		}
		val amount = bets[challenger]!!
		val response = vault_econ.withdrawPlayer(player,amount)
		if(response.type != EconomyResponse.ResponseType.SUCCESS){
			player.sendMessage("$pluginmsg You do not have enough money to accept that challenge.")
			return
		}
		player.sendMessage("$pluginmsg You have accepted ${challenger.name}'s coin flip challenge for $$amount money.")
		challenger.sendMessage("$pluginmsg ${player.name} has accepted your coin flip challenge.")
		if(challenges.containsKey(challenger)){
			challenges.remove(challenger)
		}
		accepted_challenges.put(player,challenger)

		results[player] = InfectGensCorePlugin.Companion.CoinFlipResult.PENDING
		results[challenger] = InfectGensCorePlugin.Companion.CoinFlipResult.PENDING
		//Flips for you W.I.P
		/*val timer = object : BukkitRunnable(){
			override fun run() {
				if(results.containsKey(player) && results[player] == InfectGensCorePlugin.Companion.CoinFlipResult.PENDING){
					results[player] = InfectGensCorePlugin.Companion.CoinFlipResult.TAILS
					player.sendMessage("$pluginmsg You did not choose heads or tails in time, flipping for tails")
				}
				if(results.containsKey(challenger) && results[challenger] == InfectGensCorePlugin.Companion.CoinFlipResult.PENDING){
					results[challenger] = InfectGensCorePlugin.Companion.CoinFlipResult.TAILS
					challenger.sendMessage("$pluginmsg The challenger did not choose heads or tails in time, flipping for tails.")
				}
			}
		}*/
		CFManager.handelResults(player,challenger)
		//timer.runTaskLaterAsynchronously(plugin,600L)

		Bukkit.getScheduler().runTask(plugin, Runnable {
			CoinFlipInventoryHolder(player, challenger, amount).openInventory(player)
		})
	}

}
