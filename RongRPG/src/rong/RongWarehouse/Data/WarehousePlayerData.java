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
		RpgUtil.debugMessage("- [�ܮw] Ū����...");
		
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
			
			RpgUtil.debugMessage("- [�ܮw] Ū������!");
			return;
		}
		
		RpgUtil.sendServerLog("info", "- �ܮw�ɮ� ���s�b, �إߤ�...");
		
		config.set("Warehouse.0.Size", 9);
		config.createSection("Warehouse.0.Item");
		
		RpgUtil.saveFileConfig(file, config);
		
		RpgUtil.sendServerLog("info", "- �ܮw�ɮ� �إߦ��\!");
		
		loadWarehouseData();
	}
	
	public void saveWarehouseData()
	{
		RpgUtil.debugMessage("- [�ܮw] �x�s��...");
		
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
		
		RpgUtil.debugMessage("- [�ܮw] �x�s����!");
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
