package rong.RongWarehouse.Listener;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

import rong.RongRPG.RpgStorage;
import rong.RongWarehouse.Data.WarehouseData;

public class WarehouseInventoryClose implements Listener
{
	@EventHandler
	public void onPlayerInventoryClose(InventoryCloseEvent event)
	{
		Player player = (Player) event.getPlayer();
		Inventory inv = event.getInventory();
		String invTitle = ChatColor.stripColor(inv.getTitle());
		
		if(invTitle.contains(" ¸¹­Ü®w"))
		{
			String[] split = invTitle.split(" ");
			int i = Integer.parseInt(invTitle.split(" ")[0]) - 1;
			WarehouseData data = RpgStorage.PlayerDataMap.get(player.getName()).getWarehousePlayerData().getWarehouseData(i);
			
			if(player.isOp())
			{
				data = RpgStorage.PlayerDataMap.get(split[3]).getWarehousePlayerData().getWarehouseData(i);
			}
			
			saveWarehouse(data, inv);
		}
	}
	
	void saveWarehouse(WarehouseData data, Inventory inv)
	{
		if(data != null)
		{
			for(int slot = 0; slot < inv.getSize(); slot ++)
			{
				if(inv.getItem(slot) != null)
				{
					data.addItem(slot, inv.getItem(slot));
				}
			}
		}
	}
}
