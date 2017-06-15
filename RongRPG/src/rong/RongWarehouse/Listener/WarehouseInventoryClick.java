package rong.RongWarehouse.Listener;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;

import rong.RongRPG.RpgStorage;
import rong.RongRPG.Data.PlayerData;
import rong.RongRPG.Data.PlayerInventoryType;
import rong.RongWarehouse.WarehouseUtil;

public class WarehouseInventoryClick 
{	
	public static void clickWarehouseInventory(InventoryClickEvent event)
	{
		Player player = (Player) event.getWhoClicked();
		PlayerData pd = RpgStorage.PlayerDataMap.get(player.getName());

		if(pd.getOpenedInventoryType() == PlayerInventoryType.WAREHOUSE_MENU)
		{
			event.setCancelled(true);
			
			if(event.getInventory().getType() == InventoryType.CHEST)
			{
				WarehouseUtil.openWarehouse(player, null, event.getSlot());
				return;
			}
		}
	}
}
