package rong.RongRPG.Util;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;

import rong.RongRPG.RpgStorage;
import rong.RongRPG.Data.PlayerData;

public class RpgUtil 
{
	public static ItemStack setSkull(String displayname, String[] lore, Player player)
	{
	    ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
	    SkullMeta meta = (SkullMeta)skull.getItemMeta();
	    meta.setOwner(player.getName());
	    meta.setDisplayName(displayname);
	    meta.setLore(Arrays.asList(lore));
	    skull.setItemMeta(meta);
	    return skull;
	}

	public static ItemStack setItem(Material material, int amount, int data, String displayname, String[] lore, ItemFlag[] flag, boolean unbreakable)
	{
		ItemStack item = new ItemStack(material, amount, (short)data);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(displayname);
		if(lore != null)
		{
			meta.setLore(Arrays.asList(lore));
		}
		if(flag != null)
		{
			meta.addItemFlags(flag);
		}
		if(unbreakable)
		{
			meta.spigot().setUnbreakable(true);
		}
		
		item.setItemMeta(meta);
		return item;
	}
	
	public static void showHologram(long time, Location loc, String... str)
	{
		for(int i = 0; i < str.length; i ++)
		{
			loc.add(0, 0.2, 0);
		}
		
		Hologram h = HologramsAPI.createHologram(RpgStorage.plugin, loc);

		for(String s : str)
		{
			h.appendTextLine(s);
		}
		
		final Hologram h2 = h;
		
		Bukkit.getScheduler().runTaskLater(RpgStorage.plugin, new Runnable()
		{
			@Override
			public void run()
			{
				h2.delete();
			}
		}, time);
	}
	
	public static void loadAllPlayerData()
	{
		RpgStorage.PlayerDataMap.clear();
		
		File file = new File("./plugins/RongRPG/PlayerData");
		
		sendServerLog("info", "===== 讀取所有玩家數據...");
		
		if(file.exists())
		{
			String[] list = file.list();
			
			for(String fName : list)
			{
				PlayerData pData = new PlayerData(fName);
				
				RpgStorage.PlayerDataMap.put(fName, pData);
			}
		}
		
		for(Player player : Bukkit.getOnlinePlayers())
		{
			if(RpgStorage.PlayerDataMap.containsKey(player.getName()))
			{
				PlayerData pData = RpgStorage.PlayerDataMap.get(player.getName());
				
				pData.setPlayer(player);
				pData.UpdateData();
			}
			else
			{
				PlayerData pData = new PlayerData(player.getName());
				
				RpgStorage.PlayerDataMap.put(player.getName(), pData);
			}
		}
		
		sendServerLog("info", "===== 所有玩家數據讀取成功!");
	}
	
	public static void saveAllPlayerData()
	{
		sendServerLog("info", "===== 儲存所有玩家數據...");
		
		for(PlayerData pData : RpgStorage.PlayerDataMap.values())
		{
			pData.saveData();
		}
		
		RpgStorage.PlayerDataMap.clear();
		sendServerLog("info", "===== 所有玩家數據儲存成功!");
	}
	
	public static void saveFileConfig(File file, FileConfiguration config)
	{
		try {
			config.save(file);
		} catch (IOException e) {
			sendServerLog("warning", file.getPath() + " 檔案儲存錯誤");
			e.printStackTrace();
		}
	}
	
	public static void showHoloMessage(final Hologram h)
	{
		Bukkit.getScheduler().runTaskLater(RpgStorage.plugin, new Runnable()
		{
			@Override
			public void run()
			{
				h.delete();
			}
		}, 20L);
	}
	
	public static boolean isRpgItem(ItemStack item)
	{
		if(item != null && item.hasItemMeta() && item.getItemMeta().hasDisplayName())
		{
			return true;	
		}
		return false;
	}
	
	public static void sendServerLog(String level, String message)
	{
		switch(level)
		{
		case "info":
			RpgStorage.plugin.getLogger().info(message);
			break;
		case "warning":
			RpgStorage.plugin.getLogger().warning(message);
			break;
		}
	}
	
	public static void debugMessage(String message)
	{
		if(RpgStorage.Debug)
		{
			sendServerLog("info", message);
		}
	}
	
	public static void updateEntityCustomName(Entity entity)
	{
		if(entity instanceof LivingEntity)
		{
			String cName = entity.getCustomName();
			
			if(entity instanceof Player)
			{
				Player player = (Player) entity;
				
				cName = player.getName() + " §c" + player.getHealth() + " / " + player.getMaxHealth();
			}
			else if(entity instanceof Monster || entity instanceof Animals)
			{
				if(cName != null)
				{
					LivingEntity e = (LivingEntity) entity;
					String[] split = entity.getCustomName().split(" ");
					String name = split[0];
					
					cName = name + " §c" + (e.getHealth() + " / " + e.getMaxHealth());
				}
			}
			
			entity.setCustomName(cName);
		}
	}
}
