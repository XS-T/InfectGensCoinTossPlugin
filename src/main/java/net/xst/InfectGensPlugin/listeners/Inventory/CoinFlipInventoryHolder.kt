import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.InventoryHolder
import org.bukkit.inventory.ItemStack

class CoinFlipInventoryHolder(private val player: Player, private val opponent: Player, private val bet: Double) :
	InventoryHolder {
	private val inventory = Bukkit.createInventory(this, 9, "Coin Flip - ${opponent.name}")

	init {
		inventory.setItem(2, ItemStack(Material.DIAMOND).apply {
			val meta = itemMeta
			meta.setDisplayName("${ChatColor.GOLD}${ChatColor.BOLD}Heads")
			meta.lore = listOf("", "${ChatColor.WHITE}Click to select Heads")
			itemMeta = meta
		})
		inventory.setItem(6, ItemStack(Material.PAPER).apply {
			val meta = itemMeta
			meta.setDisplayName("${ChatColor.GOLD}${ChatColor.BOLD}Tails")
			meta.lore = listOf("","${ChatColor.WHITE}Click to select Tails")
			itemMeta = meta
		})
	}

	fun openInventory(player:Player) {
		player.openInventory(inventory)
	}
	fun closeInventory(player:Player){
		player.closeInventory()
	}

	override fun getInventory(): org.bukkit.inventory.Inventory {
		return inventory
	}

	fun getPlayer(player: Player): Player {
		return player
	}

	fun getOpponent(opponent: Player): Player {
		return opponent
	}

	fun getBet(): Double {
		return bet
	}
}
