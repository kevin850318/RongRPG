package rong.RongRPG.Listener;

import java.util.List;

import net.elseland.xikage.MythicMobs.API.Exceptions.InvalidMobTypeException;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

import rong.RongRPG.RpgStorage;
import rong.RongRPG.Data.MobSpawnerData;

public class EntitySpawn implements Listener
{
	@EventHandler
	public void onEntitySpawn(CreatureSpawnEvent event)
	{
		Entity entity = event.getEntity();
		
		if(entity instanceof Monster)
		{
			event.setCancelled(true);
			
			Location loc = entity.getLocation();
			int distance = (int) loc.distance(RpgStorage.SpawnerCenterLocation);
			int spawnerID = (int) (distance/100);
			MobSpawnerData msd = RpgStorage.MobSpawnerMap.get(spawnerID);
			
			if(msd != null)
			{
				List<String> mobList = msd.getMobList();
				String mobName = mobList.get(RpgStorage.random.nextInt(mobList.size()));
				
				try 
				{
					if(RpgStorage.mm.getAPI().getMobAPI().getMythicMob(mobName) != null)
					{
						RpgStorage.mm.getAPI().getMobAPI().spawnMythicMob(mobName, loc);
					}
				} 
				catch (InvalidMobTypeException e) 
				{
					e.printStackTrace();
				}
			}
		}
	}
}
