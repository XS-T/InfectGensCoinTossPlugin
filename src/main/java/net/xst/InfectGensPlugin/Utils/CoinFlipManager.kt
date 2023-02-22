package net.xst.InfectGensPlugin.Utils

import com.google.inject.Inject
import net.xst.InfectGensPlugin.InfectGensCorePlugin
import net.xst.InfectGensPlugin.InfectGensCorePlugin.Companion.CFManager
import net.xst.InfectGensPlugin.InfectGensCorePlugin.Companion.bets
import net.xst.InfectGensPlugin.InfectGensCorePlugin.Companion.pluginmsg
import net.xst.InfectGensPlugin.InfectGensCorePlugin.Companion.results
import net.xst.InfectGensPlugin.InfectGensCorePlugin.Companion.vault_econ
import org.bukkit.entity.Player
import java.util.*

class CoinFlipManager @Inject constructor(private val plugin: InfectGensCorePlugin) {
	fun handelResults(player: Player, target: Player){
		if(results[player] == InfectGensCorePlugin.Companion.CoinFlipResult.PENDING || results[target] == InfectGensCorePlugin.Companion.CoinFlipResult.PENDING){
			return
		}
		val winner: Player
		val loser: Player
		if(results[player] == results[target]){
			player.sendMessage("$pluginmsg It's a tie! ${results[player]}")
			target.sendMessage("$pluginmsg It's a tie! ${results[target]}")
			vault_econ.depositPlayer(player,bets[player]!!)
			vault_econ.depositPlayer(target,bets[target]!!)
			return
		}
		if(CFManager.coinToss() == 0 && results[player] == InfectGensCorePlugin.Companion.CoinFlipResult.HEADS){
			winner = player
			loser = target
		}else{
			winner = target
			loser = player
		}

		val winnings = bets[player]!! + bets[target]!!
		vault_econ.depositPlayer(winner,winnings)
		winner.sendMessage("$pluginmsg You won the coin flip and received $${winnings.toInt()} money")
		loser.sendMessage("$pluginmsg You lost the coin flip and lost $${bets[player]} money")
	}

	fun coinToss(): Int {
		val random = Random()
		val result = random.nextInt(2) == 0 // Generate 0 or 1 randomly
		if (result) {
			return 0
		} else {
			return 1
		}
	}
}