package rong.RongRPG.Data;

import java.io.File;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import rong.RongRPG.RpgStorage;
import rong.RongRPG.Util.RpgUtil;

public class SkillPlayerData 
{
	private PlayerData pData;
	private ArrayList<SkillData> SkillList = new ArrayList<>();
	private EnumMap<SkillSlot, SkillData> SlotMap = new EnumMap<>(SkillSlot.class);
	private SkillSlot ChoosedSkillSlot;
	
	private File file;
	private FileConfiguration config;
	
	public SkillPlayerData(PlayerData data)
	{
		this.pData = data;
		this.file = new File("./plugins/RongRPG/PlayerData/" + pData.getPlayerName() + "/Skill.yml");
		this.config = YamlConfiguration.loadConfiguration(file);
		
		loadSkillData();
	}
	
	public void loadSkillData()
	{
		RpgUtil.debugMessage("- [技能]讀取中...");

		if(file.exists())
		{
			List<String> sList = config.getStringList("Skill");
			
			for(String skillID : sList)
			{
				SkillData sData = RpgStorage.SkillDataMap.get(skillID);
				
				if(sData != null && (!this.SkillList.contains(sData)))
				{
					this.SkillList.add(sData);
				}
			}
			
			for(String strSlot : config.getConfigurationSection("Slot").getKeys(false))
			{
				SkillSlot slot = SkillSlot.valueOf(strSlot);
				SkillData sData = RpgStorage.SkillDataMap.get(config.getString("Slot." + strSlot));
				
				if(sData != null)
				{
					this.SlotMap.put(slot, sData);
				}
			}
			
			RpgUtil.debugMessage("- [技能] 讀取完成!");
		}
	}
	
	public void saveSkillData()
	{
		RpgUtil.debugMessage("- [技能]儲存中...");

		List<String> sList = new ArrayList<>();
		
		for(SkillData sData : this.SkillList)
		{
			sList.add(sData.getSkillID());
		}
		
		config.set("Skill", sList);
		
		for(SkillSlot slot : this.SlotMap.keySet())
		{
			config.set("Slot." + slot.name(), this.SlotMap.get(slot).getSkillID());
		}
		
		RpgUtil.saveFileConfig(file, config);
		
		RpgUtil.debugMessage("- [技能] 儲存完成!");
	}
	
	public void setChoosedSkillSlot(SkillSlot slot)
	{
		this.ChoosedSkillSlot = slot;
	}
	
	public ArrayList<SkillData> getSkillList()
	{
		return this.SkillList;
	}
	
	public EnumMap<SkillSlot, SkillData> getSlotMap()
	{
		return this.SlotMap;
	}
	
	public SkillSlot getChoosedSkillSlot()
	{
		return this.ChoosedSkillSlot;
	}
	
	public enum SkillSlot
	{
		ONE(46, 1), TWO(47, 2), THREE(48, 3), FOUR(49, 4), FIVE(50, 5), SIX(51, 6), SEVEN(52, 7), EIGHT(53, 8);
		
		private int EquipSlot;
		private int HotBarSlot;
		
		SkillSlot(int slot, int hotbar)
		{
			this.EquipSlot = slot;
			this.HotBarSlot = hotbar;
		}
		
		public static SkillSlot getSkillSlotBySlot(int slot)
		{
			for(SkillSlot sSlot : SkillSlot.values())
			{
				if(slot == sSlot.getEquipSlot())
				{
					return sSlot;
				}
			}
			
			return null;
		}
		
		public static SkillSlot getSkillSlotByHotBar(int hotbar)
		{
			for(SkillSlot slot : SkillSlot.values())
			{
				if(hotbar == slot.getHotBarSlot())
				{
					return slot;
				}
			}
			
			return null;
		}
		
		public int getEquipSlot()
		{
			return this.EquipSlot;
		}
		
		public int getHotBarSlot()
		{
			return this.HotBarSlot;
		}
	}
}
