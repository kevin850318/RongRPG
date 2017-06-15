package rong.RongRPG.Data;

import java.io.File;
import java.util.EnumMap;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import rong.RongRPG.RpgStorage;
import rong.RongRPG.Data.EquipmentItem.EquipmentType;
import rong.RongRPG.Util.RpgUtil;

public class EquipmentPlayerData 
{
	private PlayerData pData;
	private EnumMap<EquipmentType, EquipmentItem> EquipmentMap = new EnumMap<>(EquipmentType.class);
	
	private File file;
	private FileConfiguration config;
	
	public EquipmentPlayerData(PlayerData data)
	{
		this.pData = data;
		this.file = new File("./plugins/RongRPG/PlayerData/" + data.getPlayerName() + "/Equipment.yml");
		this.config = YamlConfiguration.loadConfiguration(file);

		loadEquipmentData();
	}
	
	public void loadEquipmentData()
	{
		RpgUtil.debugMessage("- [裝備] 讀取中...");
		
		if(file.exists())
		{
			for(EquipmentType type : EquipmentType.values())
			{
				ItemStack equip = config.getItemStack("Equipment." + type.name(), null);
				CustomItem ci = CustomItem.getCustomItem(equip);
				
				if(ci != null)
				{
					EquipmentItem ei = new EquipmentItem(equip);
					
					this.setEquipment(type, ei);
				}
			}
			
			RpgUtil.debugMessage("- [裝備] 讀取完成!");
			return;
		}
	}
	
	public void saveEquipmentData()
	{
		RpgUtil.debugMessage("- [裝備] 儲存中...");

		for(EquipmentType type : EquipmentType.values())
		{
			ItemStack item = this.EquipmentMap.get(type) == null ? null : this.EquipmentMap.get(type).getItem();
			config.set("Equipment." + type.name(), item);
		}
		
		RpgUtil.saveFileConfig(file, config);
		
		RpgUtil.debugMessage("- [裝備] 儲存完成!");
	}
	
	public void loadEquipmentAttribute()
	{
		for(EquipmentType type : this.EquipmentMap.keySet())
		{
			EquipmentItem ei = this.EquipmentMap.get(type);
			
			if(type == ei.getEquipmentType())
			{
				for(EquipmentAttribute ea : ei.getAttributeMap().values())
				{
					int attValue = ea.getAttributeValue();
					int playerValue = this.pData.getInt(ea.getAttributeID());
					
					playerValue += attValue;
					
					this.pData.setInt(ea.getAttributeID(), playerValue);
				}
			}
			else
			{
				pData.getPlayer().sendMessage(RpgStorage.SystemTitle + type.getTypeName() + " §c欄位的裝備類別錯誤");
			}
		}
	}
	
//	public void setEquipment(EquipmentType type, ItemStack is)
//	{
//		if(is != null)
//		{
//			EquipmentItem ei = new EquipmentItem(is);
//			
//			ei.loadEquipmentContent(this.pData);
//			this.EquipmentMap.put(type, ei);
//			return;
//		}
//		
//		this.EquipmentMap.remove(type);
//	}
	
	public void setEquipment(EquipmentType type, EquipmentItem ei)
	{
		if(ei != null)
		{
			ei.loadEquipmentContent(this.pData);
			this.EquipmentMap.put(type, ei);
			return;
		}
		
		this.EquipmentMap.remove(type);
	}
	
	public EnumMap<EquipmentType, EquipmentItem> getEquipmentMap()
	{
		return this.EquipmentMap;
	}
}
