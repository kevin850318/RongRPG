package rong.RongRPG.Runnable;

import java.util.List;

import net.elseland.xikage.MythicMobs.API.Exceptions.InvalidMobTypeException;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import rong.RongRPG.RpgStorage;
import rong.RongRPG.Data.MobSpawnerData;

public class TenSecondRunnable implements Runnable
{
	@Override
	public void run() 
	{
		for(Player player : RpgStorage.SpawnerCenterLocation.getWorld().getPlayers())
		{
			Location pLocation = player.getLocation();
			int distance = (int) pLocation.distance(RpgStorage.SpawnerCenterLocation);
			int spawnerID = (int) (distance/100);
			
			if(RpgStorage.MobSpawnerMap.containsKey(spawnerID))
			{
				int mobAmount = 0;
				
				for(Entity near : player.getNearbyEntities(20, 20, 20))
				{
					if(RpgStorage.mm.getAPI().getMobAPI().isMythicMob(near))
					{
						mobAmount ++;
					}
				}
				
				for(int i = 0; i < 10 - mobAmount; i ++)
				{
					try
					{
						MobSpawnerData msData = RpgStorage.MobSpawnerMap.get(spawnerID);
						List<String> mobList = msData.getMobList();
						String mobName = mobList.get(RpgStorage.random.nextInt(mobList.size()));
						
						if(RpgStorage.mm.getAPI().getMobAPI().getMythicMob(mobName) != null)
						{
							Location sLocation = getSpawnLocation(pLocation);

							if(sLocation.getBlock().getType() == Material.AIR)
							{
								RpgStorage.mm.getAPI().getMobAPI().spawnMythicMob(mobName, sLocation);
							}
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
	
	public Location getSpawnLocation(Location pLocation)
	{
		int x = RpgStorage.random.nextInt(40) - 20;
		int z = RpgStorage.random.nextInt(40) - 20;
		
		pLocation.setX(pLocation.getX() + x);
		pLocation.setY(pLocation.getY() + 1);
		pLocation.setZ(pLocation.getZ() + z);
		
		return pLocation;
	}
}
