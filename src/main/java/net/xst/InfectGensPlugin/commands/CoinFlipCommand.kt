package net.xst.InfectGensPlugin.commands

import cloud.commandframework.annotations.Argument
import cloud.commandframework.annotations.CommandDescription
import cloud.commandframework.annotations.CommandMethod
import cloud.commandframework.annotations.CommandPermission
import com.google.inject.Inject
import net.milkbowl.vault.economy.EconomyResponse
import net.xst.InfectGensPlugin.InfectGensCorePlugin
import net.xst.InfectGensPlugin.InfectGensCorePlugin.Companion.bets
import net.xst.InfectGensPlugin.InfectGensCorePlugin.Companion.challenges
import net.xst.InfectGensPlugin.InfectGensCorePlugin.Companion.pluginmsg
import net.xst.InfectGensPlugin.InfectGensCorePlugin.Companion.results
import net.xst.InfectGensPlugin.InfectGensCorePlugin.Companion.vault_econ
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable

class CoinFlipCommand @Inject constructor(private val plugin:InfectGensCorePlugin){
	@CommandMethod("cf challenge <target> <ammount>")
	@CommandDescription("Challenge a player to a coin flip game")
	@CommandPermission("infectgens.coinflip")
	fun coinFlip(player:Player,@Argument("target") target:Player,@Argument("ammount") ammount:Double){
		if(challenges.containsKey(player)){
			player.sendMessage("$pluginmsg You already have a pending challenge.")
			return
		}
		if(challenges.containsKey(target)){
			player.sendMessage("$pluginmsg You cannot challenge someone who has already challenged you.")
			return
		}
		if(ammount < 1){
			player.sendMessage("$pluginmsg You must bet at least 1 money.")
			return
		}
		val response = vault_econ.withdrawPlayer(player,ammount)
		if(response.type != EconomyResponse.ResponseType.SUCCESS){
			player.sendMessage("$pluginmsg You do not have enough money to place that bet.")
			return
		}
		player.sendMessage("$pluginmsg You hace challenged ${target.displayName} to a coin flip for $$ammount money.")
		target.sendMessage("$pluginmsg ${player.displayName} has challenged you to a coin flip for $$ammount of money.")
		target.sendMessage("$pluginmsg Type /cf challenge accept ${player.displayName} to accept the challenge.")
		challenges[player] = target
		bets[player] = ammount
		bets[target] = ammount
		results[player] = InfectGensCorePlugin.Companion.CoinFlipResult.PENDING
		val timer = object : BukkitRunnable(){
			override fun run() {
				if (challenges.containsKey(player)) {
					challenges.remove(player)
					bets.remove(player)
					results.remove(player)
					vault_econ.depositPlayer(player, ammount)
					player.sendMessage("$pluginmsg Challenge expired.")
					target.sendMessage("$pluginmsg Challenge from ${player.displayName} has expired")
				}
			}
		}
		//Will make this configuerable
		timer.runTaskLater(plugin,600L)
		player.sendMessage("$pluginmsg Challenge sent to ${target.displayName}")
	}
}