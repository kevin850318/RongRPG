package rong.RongRPG.Listener;

import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemSpawnEvent;

public class ItemSpawn implements Listener
{
	@EventHandler
	public void onItemSpawn(ItemSpawnEvent event)
	{
		Item item = event.getEntity();
		if(item.getItemStack().hasItemMeta())
		{
			item.setCustomName(item.getItemStack().getItemMeta().getDisplayName());
			item.setCustomNameVisible(true);
		}
	}
}
