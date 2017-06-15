package rong.RongRPG.Listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

public class PlayerDropItem implements Listener
{
	@EventHandler
	public void onPlayerDropItem(PlayerDropItemEvent event)
	{
		if(event.getItemDrop().getItemStack().hasItemMeta() && event.getItemDrop().getItemStack().getItemMeta().getLore().contains("¡±cµLªk¥á±ó"))
		{
			event.setCancelled(true);
		}
	}
}
