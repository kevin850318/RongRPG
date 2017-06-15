package rong.RongRPG.Listener;

import net.elseland.xikage.MythicMobs.API.Bukkit.BukkitMobsAPI;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityCombustEvent;

public class EntityCombust implements Listener
{
	BukkitMobsAPI mmobAPI = new BukkitMobsAPI();
	
	@EventHandler
	public void onEntityCombust(EntityCombustEvent event)
	{
		if(mmobAPI.isMythicMob(event.getEntity()))
		{
			event.setCancelled(true);
		}
	}
}
