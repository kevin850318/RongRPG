package rong.RongGuild.Listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

import rong.RongGuild.GuildUtil;

public class InventoryClose implements Listener
{
	@EventHandler
	public void onInventoryClose(InventoryCloseEvent event)
	{
		Player player = (Player) event.getPlayer();
		Inventory inv = event.getInventory();
		
		if(inv.getTitle().contains(" ¡±8Åv­­³]©w"))
		{
			GuildUtil.saveRankPermission(inv, player);
			return;
		}
	}
}
