package rong.RongRPG.ConfigLoad;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import com.nisovin.magicspells.MagicSpells;
import com.nisovin.magicspells.Spell;

import rong.RongRPG.RpgStorage;
import rong.RongRPG.Data.CustomItem;
import rong.RongRPG.Data.CustomItem.ItemType;
import rong.RongRPG.Data.GemItem;
import rong.RongRPG.Data.GemItem.GemType;
import rong.RongRPG.Data.MerchantData;
import rong.RongRPG.Data.MerchantData.MerchantStatus;
import rong.RongRPG.Data.MobSpawnerData;
import rong.RongRPG.Data.SkillData;
import rong.RongRPG.Data.TeleportData;
import rong.RongRPG.Util.RpgUtil;

public class Config 
{
	public Config()
	{
		loadConfig();
	}
	
	public void loadConfig()
	{
		loadItem();
		loadSkill();
		loadMerchant();
		loadNPCMerchant();
		loadMobSpawner();
		loadTeleportPoint();
	}
	
	public void loadItem()
	{
		RpgStorage.CustomItemMap.clear();
		String path = "./plugins/RongRPG/Item";
		File file = new File(path);
		
		RpgUtil.sendServerLog("info", "---[物品] 讀取中...");

		if(file.exists())
		{
			String[] list = file.list();
			
			for(String name : list)
			{
				file = new File(path + "/" + name);
				FileConfiguration config = YamlConfiguration.loadConfiguration(file);
				
				for(String id : config.getConfigurationSection("ItemList").getKeys(false))
				{
					ItemStack is = config.getItemStack("ItemList." + id + ".ItemStack");
					ItemType iType = ItemType.valueOf(config.getString("ItemList." + id + ".ItemType", "ITEM"));
					int coin = config.getInt("ItemList." + id + ".Coin", 1);
					CustomItem ci = new CustomItem(id, is, iType, coin);
					
					if(iType == ItemType.GEM)
					{
						GemType gType = GemType.valueOf(config.getString("ItemList." + id + ".GemType", null));
						ci = new GemItem(id, is, iType, coin, gType);
					}
					
					RpgStorage.CustomItemMap.put(id, ci);
				}
			}
			
			RpgUtil.sendServerLog("info", "---[物品] " + RpgStorage.CustomItemMap.size() + " 讀取完成!");
		}
	}
	
	public void loadSkill()
	{
		RpgStorage.SkillDataMap.clear();
		File file = new File("./plugins/RongRPG/Skill.yml");
		FileConfiguration config = YamlConfiguration.loadConfiguration(file);
		
		RpgUtil.sendServerLog("info", "---[技能] 讀取中...");
		
		if(file.exists())
		{
			for(String skillID : config.getConfigurationSection("Skill").getKeys(false))
			{
				String skillName = config.getString("Skill." + skillID + ".SkillName", null);
				Spell spell = MagicSpells.getSpellByInternalName(skillName);
				
				if(spell != null)
				{
					ItemStack item = config.getItemStack("Skill." + skillID + ".Item", null);
					SkillData sData = new SkillData(skillID, skillName, spell);
					CustomItem ci = new CustomItem(skillID, item, ItemType.SKILL_BOOK, 1);
					
					RpgStorage.SkillDataMap.put(skillID, sData);
					RpgStorage.CustomItemMap.put(skillID, ci);
					RpgUtil.debugMessage("ID: " + skillID + " Name: " + skillName);
				}
			}
			
			RpgUtil.sendServerLog("info", "---[技能] " + RpgStorage.SkillDataMap.size() + " 讀取完成!");
		}
		else
		{
			RpgStorage.plugin.saveResource("Skill.yml", true);
			loadSkill();
		}
	}
	
	public void loadMerchant()
	{
		RpgStorage.MerchantDataMap.clear();
		File file = new File("./plugins/RongRPG/Merchant.yml");
		FileConfiguration config = YamlConfiguration.loadConfiguration(file);
		
		RpgUtil.sendServerLog("info", "---[商店] 讀取中...");

		if(file.exists())
		{
			if(config.contains("Merchant"))
			{
				for(String idStr : config.getConfigurationSection("Merchant").getKeys(false))
				{
					int id = Integer.parseInt(idStr);
					String name = config.getString("Merchant." + idStr + ".Name");
					MerchantData md = new MerchantData(id, name);
					md.setStatus(MerchantStatus.valueOf(config.getString("Merchant." + idStr + ".Status")));
					
					if(config.contains("Merchant." + idStr + ".Content"))
					{
						for(String slot : config.getConfigurationSection("Merchant." + idStr + ".Content").getKeys(false))
						{
							CustomItem ci = RpgStorage.CustomItemMap.get(config.getString("Merchant." + idStr + ".Content." + slot));
							
							if(ci != null)
							{
								int s = Integer.parseInt(slot);
								
								md.setItem(s, ci);
							}
						}
					}
					
					RpgStorage.MerchantDataMap.put(id, md);
					RpgUtil.debugMessage("ID: " + id + " Name: " + name + " Status: " + md.getStatus().name());
				}
				
				RpgUtil.sendServerLog("info", "---[商店] " + RpgStorage.MerchantDataMap.size() + " 讀取完成!");
			}
		}
	}
	
	public void loadNPCMerchant()
	{
		RpgStorage.NPCMerchantMap.clear();
		File file = new File("./plugins/RongRPG/NPCMerchant.yml");
		FileConfiguration config = YamlConfiguration.loadConfiguration(file);

		RpgUtil.sendServerLog("info", "---[NPC 商店] 讀取中...");
		
		if(file.exists())
		{
			for(String idStr : config.getConfigurationSection("").getKeys(false))
			{
				MerchantData md = RpgStorage.MerchantDataMap.get(config.getInt(idStr));
				
				if(md != null)
				{
					RpgStorage.NPCMerchantMap.put(Integer.parseInt(idStr), md);
					RpgUtil.debugMessage("NPC ID: " + idStr + " Merchant ID: " + md.getID());
				}
			}
			
			RpgUtil.sendServerLog("info", "---[NPC 商店] " + RpgStorage.NPCMerchantMap.size() + " 讀取完成!");
		}
	}
	
	public void loadMobSpawner()
	{
		RpgStorage.MobSpawnerMap.clear();
		File file = new File("./plugins/RongRPG/MobSpawner.yml");
		FileConfiguration config = YamlConfiguration.loadConfiguration(file);
		
		RpgUtil.sendServerLog("info", "---[怪物] 讀取中...");
		
		if(file.exists())
		{
			if(config.contains("SpawnerCenterPoint"))
			{
				String world = config.getString("SpawnerCenterPoint.World", "Generated_World");
				double x = config.getDouble("SpawnerCenterPoint.X", 0);
				double y = config.getDouble("SpawnerCenterPoint.Y", 64);
				double z = config.getDouble("SpawnerCenterPoint.Z", 0);
				RpgStorage.SpawnerCenterLocation = new Location(Bukkit.getWorld(world), x, y, z);
				RpgUtil.debugMessage("SpawnerCenterLocation: " + "World: " + world + " X: " + x + " Y: " + y + " Z: " + z);
			}
			
			if(config.contains("SpawnerList"))
			{
				for(String id : config.getConfigurationSection("SpawnerList").getKeys(false))
				{
					int ID = Integer.parseInt(id);
					MobSpawnerData msd = new MobSpawnerData(ID, config.getInt("SpawnerList." + id + ".MinDistance"), config.getInt("SpawnerList." + id + ".MaxDistance"));
					
					msd.setMobList(config.getStringList("SpawnerList." + id + ".Mob"));
					RpgStorage.MobSpawnerMap.put(ID, msd);
					RpgUtil.debugMessage("ID: " + ID + " Mobs: " + msd.getMobList());
				}
			}
			
			RpgUtil.sendServerLog("info", "---[怪物] " + RpgStorage.MobSpawnerMap.size() + " 讀取完成!");
		}
	}
	
	public void loadTeleportPoint()
	{
		RpgStorage.TeleporDatatMap.clear();
		File file = new File("./plugins/RongRPG/NPCMerchant.yml");
		FileConfiguration config = YamlConfiguration.loadConfiguration(file);
	
		if(file.exists())
		{
			for(String id : config.getConfigurationSection("").getKeys(false))
			{
				TeleportData td = new TeleportData(id);
				
				if(config.contains(id + ".Point1"))
				{
					Location loc = new Location(Bukkit.getWorld(config.getString(id + ".Point1.World" )),
							config.getDouble(id + ".Point1.X"),
							config.getDouble(id + ".Point1.Y"),
							config.getDouble(id + ".Point1.Z"));
					td.setPoint1Location(loc);
				}
				
				if(config.contains(id + ".Point2"))
				{
					Location loc = new Location(Bukkit.getWorld(config.getString(id + ".Point2.World" )),
							config.getDouble(id + ".Point2.X"),
							config.getDouble(id + ".Point2.Y"),
							config.getDouble(id + ".Point2.Z"));
					td.setPoint1Location(loc);
				}
				
				RpgStorage.TeleporDatatMap.put(id, td);
			}
		}
	}
	
	public void saveConfig()
	{
		saveMerchant();
		saveNPCMerchant();
	}
	
	public void saveMerchant()
	{
		File file = new File("./plugins/RongRPG/Merchant.yml");
		FileConfiguration config = YamlConfiguration.loadConfiguration(file);

		RpgUtil.sendServerLog("info", "---[商店] 儲存中...");

		for(MerchantData md : RpgStorage.MerchantDataMap.values())
		{
			config.createSection("Merchant." + md.getID());
			config.set("Merchant." + md.getID() + ".Name", md.getName());
			config.set("Merchant." + md.getID() + ".Status", md.getStatus().name());
			for(int slot : md.getItemMap().keySet())
			{
				config.set("Merchant." + md.getID() + ".Content." + slot, md.getItemMap().get(slot).getItemID());
			}
		}
		
		RpgUtil.saveFileConfig(file, config);
	}
	
	public void saveNPCMerchant()
	{
		File file = new File("./plugins/RongRPG/NPCMerchant.yml");
		FileConfiguration config = YamlConfiguration.loadConfiguration(file);
		
		for(int id : RpgStorage.NPCMerchantMap.keySet())
		{
			config.set(String.valueOf(id), RpgStorage.NPCMerchantMap.get(id).getID());
		}
		
		RpgUtil.saveFileConfig(file, config);
	}
	
	public void saveTeleportPoint()
	{
		File file = new File("./plugins/RongRPG/NPCMerchant.yml");
		FileConfiguration config = YamlConfiguration.loadConfiguration(file);
		
		for(String id : RpgStorage.TeleporDatatMap.keySet())
		{
			TeleportData td = RpgStorage.TeleporDatatMap.get(id);
			
			config.createSection(id);
			if(td.getPoint1Location() != null)
			{
				config.set(id + ".Point1.World", td.getPoint1Location().getWorld().getName());
				config.set(id + ".Point1.X", td.getPoint1Location().getBlockX());
				config.set(id + ".Point1.Y", td.getPoint1Location().getBlockY());
				config.set(id + ".Point1.Z", td.getPoint1Location().getBlockZ());
			}
			
			if(td.getPoint2Location() != null)
			{
				config.set(id + ".Point2.World", td.getPoint2Location().getWorld().getName());
				config.set(id + ".Point2.X", td.getPoint2Location().getBlockX());
				config.set(id + ".Point2.Y", td.getPoint2Location().getBlockY());
				config.set(id + ".Point2.Z", td.getPoint2Location().getBlockZ());
			}
		}
		
		RpgUtil.saveFileConfig(file, config);
	}
}