package rong.RongWarehouse.Data;

import java.io.File;
import java.util.ArrayList;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import rong.RongRPG.Util.RpgUtil;

public class WarehousePlayerData 
{
	private String PlayerName;
	private ArrayList<WarehouseData> WarehouseList = new ArrayList<>();
	private File file;
	private FileConfiguration config;
	
	public WarehousePlayerData(String pName)
	{
		this.PlayerName = pName;
		this.file = new File("./plugins/RongRPG/PlayerData/" + pName + "/Warehouse.yml"); 
		this.config = YamlConfiguration.loadConfiguration(file);

		loadWarehouseData();
	}
	
	public void loadWarehouseData()
	{
		RpgUtil.debugMessage("- [倉庫] 讀取中...");
		
		if(file.exists())
		{
			for(String s1 : config.getConfigurationSection("Warehouse").getKeys(false))
			{
				WarehouseData whd = new WarehouseData(config.getInt("Warehouse." + s1 + ".Size", 9));
				
				for(String s2 : config.getConfigurationSection("Warehouse").getKeys(false))
				{
					int slot = Integer.parseInt(s2);
					
					whd.addItem(slot, config.getItemStack("Warehouse." + s1 + ".Item." + slot, null));
				}
				
				this.WarehouseList.add(whd);
			}
			
			RpgUtil.debugMessage("- [倉庫] 讀取完成!");
			return;
		}
		
		RpgUtil.sendServerLog("info", "- 倉庫檔案 不存在, 建立中...");
		
		config.set("Warehouse.0.Size", 9);
		config.createSection("Warehouse.0.Item");
		
		RpgUtil.saveFileConfig(file, config);
		
		RpgUtil.sendServerLog("info", "- 倉庫檔案 建立成功!");
		
		loadWarehouseData();
	}
	
	public void saveWarehouseData()
	{
		RpgUtil.debugMessage("- [倉庫] 儲存中...");
		
		int i = 0;
		
		for(WarehouseData whd : this.WarehouseList)
		{
			config.set("Warehouse." + i + ".Size", whd.getSize());

			for(int j : whd.getItemMap().keySet())
			{
				config.set("Warehouse." + i + ".Item." + j, whd.getItem(j));
			}
			
			i ++;
		}
		
		RpgUtil.saveFileConfig(file, config);
		
		RpgUtil.debugMessage("- [倉庫] 儲存完成!");
	}
	
	public void addWarehouse(WarehouseData whData)
	{
		this.WarehouseList.add(whData);
	}
	
	public String getPlayerName()
	{
		return this.PlayerName;
	}
	
	public ArrayList<WarehouseData> getWarehouseDataList()
	{
		return this.WarehouseList;
	}
	
	public WarehouseData getWarehouseData(int i)
	{
		if(i < this.WarehouseList.size())
		{
			return this.WarehouseList.get(i);
		}
		return null;
	}
}
