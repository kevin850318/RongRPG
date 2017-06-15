package rong.RongRPG.Data;

import java.io.File;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import rong.RongRPG.Util.RpgUtil;

public class MobSpawnData 
{
	public static Location SpawnCenter;
	public static HashMap<Integer, MobSpawnerData> MobSpawnerMap = new HashMap<>();
	public static File file;
	public static FileConfiguration config;
	
	public static void loadMobSpawnerData()
	{
		MobSpawnerMap.clear();
		RpgUtil.sendServerLog("info", "Loading 'Mob Spawner Data'...");
		
		file = new File("./plugins/RongRPG/MobSpawner.yml");
		config = YamlConfiguration.loadConfiguration(file);
		
		if(file.exists())
		{
			SpawnCenter = new Location(Bukkit.getWorld(config.getString("SpawnerCenterPoint.World", "Generated_World")), config.getInt("SpawnerCenterPoint.X", 0), config.getInt("SpawnerCenterPoint.Y", 63), config.getInt("SpawnerCenterPoint.Z", 0));
			
			for(String id : config.getConfigurationSection("SpawnerList").getKeys(false))
			{
				int ID = Integer.parseInt(id);
				MobSpawnerData msData = new MobSpawnerData(ID, config.getInt("SpawnerList." + id + ".MinDistance"), config.getInt("SpawnerList." + id + ".MaxDistance"));
				
				msData.setMobList(config.getStringList("SpawnerList." + id + ".Mob"));
				
				MobSpawnerMap.put(ID, msData);
			}
			
			RpgUtil.sendServerLog("info", "Load 'Mob Spawner Data' completed!");
			return;
		}
		
//		RpgUtil.sendServerLog("info", "'MobSpawner.yml' not found, Creating 'MobSpawner.yml'...");
//		List<String> list = new ArrayList<>();
//		list.add("TestMob");
//
//		config.set("MobSpawnPoint.World", "Generated_World");
//		config.set("MobSpawnPoint.X", 0);
//		config.set("MobSpawnPoint.Y", 63);
//		config.set("MobSpawnPoint.Z", 0);
//		config.set("MobSpawnerList.1.Mob", list);
//		RpgUtil.saveFileConfig(file, config);
//		
//		RpgUtil.sendServerLog("info", "Create 'MobSpawner.yml' successful!");
//		
//		loadMobSpawnerData();
	}
}
