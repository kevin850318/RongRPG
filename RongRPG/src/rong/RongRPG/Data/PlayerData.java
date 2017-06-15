package rong.RongRPG.Data;

import java.io.File;
import java.util.EnumMap;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import rong.RongFriend.Data.FriendPlayerData;
import rong.RongRPG.RpgStorage;
import rong.RongRPG.Data.EquipmentItem.EquipmentType;
import rong.RongRPG.Data.SkillPlayerData.SkillSlot;
import rong.RongRPG.Util.NMSPacket;
import rong.RongRPG.Util.RpgUtil;
import rong.RongTeam.Data.TeamData;
import rong.RongWarehouse.Data.WarehousePlayerData;

public class PlayerData 
{
	private String PlayerName;
	private Player Player;
	private String Prefix = ""; // 前墜
	private int MaxHealth = 100; // 最大生命 = 100
	private int RegainHealth = 5; // 生命恢復 = 5
	private int Mana = 100; // 魔力 = 100
	private int MaxMana = 100; // 最大魔力 = 100
	private int RegainMana = 100; // 魔力恢復 = 100
	private int level = 1; // 等級 = 1
	private int Exp = 0; // 當前經驗值 = 0
	private int MaxExp = (int) Math.pow(100, 1.1); // 最大經驗值 = (level *100)^1.1
	private int Damage = 10;
	private int Defense = 0;
	private int ResistanceDefense = 0;
	private int FireRate = 0;
	private int IceRate = 0;
	private int ThunderRate = 0;
	private int WindRate = 0;
	private int CritRate = 0; // 爆擊率 = 0
	private int AgiRate = 0; // 迴避率 = 0
	private float Speed = 0.2f;
	
	private int TalentPoint = 3; // 屬性點 = 3
	private EnumMap<TalentType, TalentType> TalentMap = new EnumMap<>(TalentType.class);
	
	private int Coin = 0; // 金幣 = 0
	private int SealCoin = 0; // 席爾幣 = 0
	private int ExpAddition = 1; // 經驗加成 = 1
	
	private String InteractPlayer;
	private PlayerInventoryType OpenedInventoryType;

	private File PlayerDataFile;
	private FileConfiguration PlayerDataConfig;
	
	private WarehousePlayerData Warehouse;
	private EquipmentPlayerData Equipment;
	private SkillPlayerData Skill;
	private FriendPlayerData Friend;
	private TeamData Team;
	
	public PlayerData(String pName)
	{
		this.PlayerName = pName;
		this.PlayerDataFile = new File("./plugins/RongRPG/PlayerData/" + pName + "/Basic.yml");
		this.PlayerDataConfig = YamlConfiguration.loadConfiguration(this.PlayerDataFile);
		
		loadData();
	}
	
	public void loadData()
	{
		RpgUtil.debugMessage("--- 讀取 " + PlayerName + " 數據...");
		
		loadPlayerData();
		this.Equipment = new EquipmentPlayerData(this);
		this.Skill = new SkillPlayerData(this);
		this.Friend = new FriendPlayerData(PlayerName);
		this.Warehouse = new WarehousePlayerData(PlayerName);
		
		RpgUtil.debugMessage("--- " + PlayerName + " 讀取成功!");
	}
	
	public void saveData()
	{
		RpgUtil.debugMessage("--- 儲存 " + PlayerName + " 數據...");
		
		savePlayerData();
		this.Equipment.saveEquipmentData();
		this.Skill.saveSkillData();
		this.Friend.saveFriendData();
		this.Warehouse.saveWarehouseData();
		
		RpgUtil.debugMessage("--- " + PlayerName + " 數據儲存成功!");
	}
	
	public void loadPlayerData()
	{
		RpgUtil.debugMessage("- [基本數據] 讀取中...");
		
		if(PlayerDataFile.exists()) //檔案存在, 並讀取
		{
			this.Prefix = PlayerDataConfig.getString(PlayerName + ".Prefix", "");
			this.MaxHealth = PlayerDataConfig.getInt(PlayerName + ".MaxHealth", 100);
			this.RegainHealth = PlayerDataConfig.getInt(PlayerName + ".RegainHealth", 5);
			this.Mana = PlayerDataConfig.getInt(PlayerName + ".Mana", 100);
			this.MaxMana = PlayerDataConfig.getInt(PlayerName + ".MaxMana", 100);
			this.RegainMana = PlayerDataConfig.getInt(PlayerName + ".RegainMana", 5);
			this.level = PlayerDataConfig.getInt(PlayerName + ".Level", 1);
			this.Exp = PlayerDataConfig.getInt(PlayerName + ".Exp", 0);
			this.MaxExp = PlayerDataConfig.getInt(PlayerName + ".MaxExp", (int) Math.pow(100, 1.1));
//			this.Damage = PlayerDataConfig.getInt(PlayerName + ".Damage", 10);
//			this.Defense = PlayerDataConfig.getInt(PlayerName + ".Defense", 0);
//			this.ResistanceDefense = PlayerDataConfig.getInt(PlayerName + ".ResistanceDefense", 0);
//			this.CritRate = PlayerDataConfig.getInt(PlayerName + ".CritRate", 0);
//			this.AgiRate = PlayerDataConfig.getInt(PlayerName + ".AgiRate", 0);
			this.Speed = PlayerDataConfig.getLong(PlayerName + ".Speed", (long) 0.2);
			this.TalentPoint = PlayerDataConfig.getInt(PlayerName + ".AttributePoint", 3);
			for(String s : PlayerDataConfig.getConfigurationSection(PlayerName + ".Talent").getKeys(false))
			{
				TalentType type = TalentType.valueOf(s);
				
				type.setValue(PlayerDataConfig.getInt(PlayerName + ".Talent." + s, 0));
				
				this.TalentMap.put(TalentType.valueOf(s), type);
			}
			this.Coin = PlayerDataConfig.getInt(PlayerName + ".Coin", 0);
			this.SealCoin = PlayerDataConfig.getInt(PlayerName + ".SealCoin", 0);
			this.ExpAddition = PlayerDataConfig.getInt(PlayerName + ".ExpAddition", 1);

			RpgUtil.debugMessage("- [基本數據] 讀取完成!");
			return;
		}
		
		RpgUtil.debugMessage("- 基本檔案 不存在, 建立中...");
		//建立檔案
		PlayerDataConfig.createSection(PlayerName);
		PlayerDataConfig.set(PlayerName + ".Prefix", "");
		PlayerDataConfig.set(PlayerName + ".MaxHealth", 100);
		PlayerDataConfig.set(PlayerName + ".RegainHealth", 5);
		PlayerDataConfig.set(PlayerName + ".Mana", 100);
		PlayerDataConfig.set(PlayerName + ".MaxMana", 100);
		PlayerDataConfig.set(PlayerName + ".RegainMana", 5);
		PlayerDataConfig.set(PlayerName + ".Level", 1);
		PlayerDataConfig.set(PlayerName + ".Exp", 0);
		PlayerDataConfig.set(PlayerName + ".MaxExp", 99999);
//		PlayerDataConfig.set(PlayerName + ".Damage", 10);
//		PlayerDataConfig.set(PlayerName + ".Defense", 0);
//		PlayerDataConfig.set(PlayerName + ".ResistanceDefense", 0);
//		PlayerDataConfig.set(PlayerName + ".Crit", 2);
//		PlayerDataConfig.set(PlayerName + ".Agi", 0);
		PlayerDataConfig.set(PlayerName + ".Speed", 0.2);
		PlayerDataConfig.set(PlayerName + ".AttributePoint", 3);
		for(TalentType t : TalentType.values())
		{
			PlayerDataConfig.set(PlayerName + ".Talent." + t.name(), 0);
		}
		PlayerDataConfig.set(PlayerName + ".Coin", 0);
		PlayerDataConfig.set(PlayerName + ".SealCoin", 0);
		PlayerDataConfig.set(PlayerName + ".ExpAddition", 1);

		RpgUtil.saveFileConfig(PlayerDataFile, PlayerDataConfig);
		RpgUtil.debugMessage("- 基本數據 建立成功!");

		loadPlayerData();
	}
		
	public void savePlayerData()
	{		
		RpgUtil.debugMessage("- [基本數據] 儲存中...");
		
		if(this.PlayerDataFile.exists()) //檔案存在, 並儲存
		{
			PlayerDataConfig.createSection(PlayerName);
			PlayerDataConfig.set(PlayerName + ".Prefix", this.Prefix);
			PlayerDataConfig.set(PlayerName + ".MaxHealth", this.MaxHealth);
			PlayerDataConfig.set(PlayerName + ".RegainHealth", this.RegainHealth);
			PlayerDataConfig.set(PlayerName + ".Mana", this.Mana);
			PlayerDataConfig.set(PlayerName + ".MaxMana", this.MaxMana);
			PlayerDataConfig.set(PlayerName + ".RegainMana", this.RegainMana);
			PlayerDataConfig.set(PlayerName + ".Level", this.level);
			PlayerDataConfig.set(PlayerName + ".Exp", this.Exp);
			PlayerDataConfig.set(PlayerName + ".MaxExp", this.MaxExp);
//			PlayerDataConfig.set(PlayerName + ".Damage", this.Damage);
//			PlayerDataConfig.set(PlayerName + ".Defense", this.Defense);
//			PlayerDataConfig.set(PlayerName + ".ResistanceDefense", this.ResistanceDefense);
//			PlayerDataConfig.set(PlayerName + ".Crit", this.CritRate);
//			PlayerDataConfig.set(PlayerName + ".Agi", this.AgiRate);
			PlayerDataConfig.set(PlayerName + ".Speed", this.Speed);
			PlayerDataConfig.set(PlayerName + ".AttributePoint", this.TalentPoint);
			for(TalentType t : this.TalentMap.values())
			{
				PlayerDataConfig.set(PlayerName + ".Talent." + t.name(), t.getValue());
			}
			PlayerDataConfig.set(PlayerName + ".Coin", this.Coin);
			PlayerDataConfig.set(PlayerName + ".SealCoin", this.SealCoin);
			PlayerDataConfig.set(PlayerName + ".ExpAddition", this.ExpAddition);

			RpgUtil.saveFileConfig(PlayerDataFile, PlayerDataConfig);
			
			RpgUtil.debugMessage("- [基本數據] 儲存完成!");
			return;
		}
		
		RpgUtil.debugMessage("- 基本檔案 遺失, 重新讀取!");
		loadPlayerData();
		savePlayerData();
	}
	
	public void UpdateData()
	{
		this.Damage = (int) (Math.pow(this.TalentMap.get(TalentType.DAM).getValue() * this.level, 0.7) *0.8);
		this.Defense = 0;
		this.MaxHealth = 100 + (this.TalentMap.get(TalentType.STRONG).getValue() *10);
		this.RegainHealth = 5 + this.TalentMap.get(TalentType.STRONG).getValue();
		this.MaxMana = 100 + (this.TalentMap.get(TalentType.SPIRITUAL).getValue() *10);
		this.RegainMana = 5 + this.TalentMap.get(TalentType.SPIRITUAL).getValue();
		this.FireRate = (int) (Math.pow(this.TalentMap.get(TalentType.FIRE_DAM).getValue() * this.level, 0.6) *0.7);
		this.IceRate = (int) (Math.pow(this.TalentMap.get(TalentType.ICE_DAM).getValue() * this.level, 0.6) *0.7);
		this.ThunderRate = (int) (Math.pow(this.TalentMap.get(TalentType.THUNDER_DAM).getValue() * this.level, 0.6) *0.7);
		this.WindRate = (int) (Math.pow(this.TalentMap.get(TalentType.WIND_DAM).getValue() * this.level, 0.6) *0.7);
		this.CritRate = (int) (Math.pow(this.TalentMap.get(TalentType.DEADLY).getValue() * this.level, 0.6) *0.3);
		this.AgiRate = (int) (Math.pow(this.TalentMap.get(TalentType.SENSITIVE).getValue() * this.level, 0.6) *0.3);
		
		this.Equipment.loadEquipmentAttribute();
		setWeaponIcon();
		setSkillIcon();
		
		this.Player.setHealthScale(20);
		this.Player.setMaxHealth(this.MaxHealth);
		this.Player.setLevel(this.level);
		this.Player.setExp((float)this.Exp / (float)this.MaxExp);
//		this.player.setCustomName(this.Prefix + PlayerName);
	}
	
	public void setWeaponIcon()
	{
		Inventory inv = Player.getInventory();
		EquipmentItem eiWeapon = Equipment.getEquipmentMap().get(EquipmentType.WEAPON);
		ItemStack hotbarItem = inv.getItem(0);
		
		if(eiWeapon != null)
		{
			ItemStack isWeapon = eiWeapon.getItem();
			
			if(hotbarItem == null || (RpgUtil.isRpgItem(hotbarItem) && hotbarItem.getItemMeta().getDisplayName().contains("武器圖示")))
			{				
				int minAtk = (int) (this.Damage * 0.9);
				int maxAtk = (int) (this.Damage * 1.1);
				ItemStack weaponIcon = RpgUtil.setItem(isWeapon.getType(), 1, isWeapon.getDurability(),
														"§f武器圖示: " + isWeapon.getItemMeta().getDisplayName(),
														new String[]{"§f攻擊力: " + minAtk + " - " + maxAtk,
																	"", "§c無法移動", "§c無法丟棄"}, null, true);
				inv.setItem(0, weaponIcon);
			}
			else
			{
				if(hotbarItem != null)
				{
					Player.sendMessage(RpgStorage.SystemTitle + "§c武器無法放置, 請清空快捷鍵欄位§f 1 §c物品");
				}
			}
		}
		else
		{
			if(RpgUtil.isRpgItem(hotbarItem) && hotbarItem.getItemMeta().getDisplayName().contains("武器圖示"))
			{
				inv.setItem(0, null);
			}
		}
	}
	
	public void setSkillIcon()
	{
		Inventory inv = this.Player.getInventory();
		
		for(SkillSlot slot : SkillSlot.values())
		{
			ItemStack hbItem = inv.getItem(slot.getHotBarSlot());
			SkillData sData = this.Skill.getSlotMap().get(slot);
			
			if(sData != null)
			{
				if(hbItem == null || (RpgUtil.isRpgItem(hbItem) && hbItem.getItemMeta().getDisplayName().contains("技能圖示")))
				{
					ItemStack skillIcon = RpgUtil.setItem(Material.ENCHANTED_BOOK, 1, 0,
							"§f技能圖示: " + sData.getSkillName(),
							new String[]{"§c無法移動", "§c無法丟棄"}, null, true);
					
					inv.setItem(slot.getHotBarSlot(), skillIcon);
				}
				else
				{
					if(hbItem != null)
					{
						Player.sendMessage(RpgStorage.SystemTitle + "§c技能欄位§f " + slot.getHotBarSlot() + " §c無法放置, 請清空快捷鍵§f " + (slot.getHotBarSlot() +1) + " §c物品 ");						
					}
				}
			}
			else
			{
				if(RpgUtil.isRpgItem(hbItem) && hbItem.getItemMeta().getDisplayName().contains("技能圖示"))
				{
					inv.setItem(slot.getHotBarSlot(), null);
				}
			}
		}
	}
	
	public void addTalentPoint(TalentType t, int value)
	{
		if(value <= this.TalentPoint)
		{
			TalentType type = this.TalentMap.get(t);
			
			type.setValue(type.getValue() + value);
			
			this.TalentPoint -= value;
			this.Player.sendMessage(RpgStorage.SystemTitle + "§e" + type.getTypeName() + " §a增加§f " + value + " §a點.");
			return;
		}
		
		this.Player.sendMessage(RpgStorage.SystemTitle + "§9屬性點§c 不足");
	}
	
	public void giveExp(int exp)
	{
		this.Exp += exp;;
		this.Player.setExp((float)(this.Exp /this.MaxExp));
		
		if(this.Exp >= this.MaxExp)
		{
			this.Exp = 0;
			this.MaxExp = (int)(Math.pow(this.level *100, 1.1));
			this.level ++;
			this.TalentPoint += 3;
			this.Player.setHealth(this.MaxHealth);
			this.Player.setExp(this.Exp);
			this.Player.setLevel(this.level);
			
			NMSPacket.sendTitle(this.Player, 1, 3, 1, null, "§a等級提升至§f " + this.level + " §a級");
			NMSPacket.sendActionBar(this.Player, "§a獲得§f 3 §9屬性點");
		}
	}
	
	public String getPlayerName()
	{
		return this.PlayerName;
	}
	
	public Player getPlayer()
	{
		return this.Player;
	}
	
	public void setPlayer(Player player)
	{
		this.Player = player;
	}
	
	public void setInt(String str, int i)
	{
		switch(str.toLowerCase())
		{
			case "maxhealth":
				this.MaxHealth = i;
				break;
			case "regainhealth":
				this.RegainHealth = i;
				break;
			case "mana":
				this.Mana = i;
				break;
			case "maxmana":
				this.MaxMana = i;
				break;
			case "regainmana":
				this.RegainMana = i;
				break;
			case "damage":
				this.Damage = i;
				break;
			case "defense":
				this.Defense = i;
				break;
			case "resistancedefense":
				this.ResistanceDefense = i;
				break;
			case "fire":
				this.TalentMap.get(TalentType.FIRE_DAM).setValue(i);
				break;
			case "ice":
				this.TalentMap.get(TalentType.ICE_DAM).setValue(i);
				break;
			case "thunder":
				this.TalentMap.get(TalentType.THUNDER_DAM).setValue(i);
				break;
			case "wind":
				this.TalentMap.get(TalentType.WIND_DAM).setValue(i);
				break;
			case "talentpoint":
				this.TalentPoint = i;
				break;
			case "coin":
				this.Coin = i;
				break;
			default:
				this.Player.sendMessage(RpgStorage.SystemTitle + "§c找不到§f " + str + " §c整數, 無法設置");
		}
	}
	
	public int getInt(String str)
	{
		switch(str.toLowerCase())
		{
			case "maxhealth":
				return this.MaxHealth;
			case "regainhealth":
				return this.RegainHealth;
			case "mana":
				return this.Mana;
			case "maxmana":
				return this.MaxMana;
			case "regainmana":
				return this.RegainMana;
			case "level":
				return this.level;
			case "exp":
				return this.Exp;
			case "maxexp":
				return this.MaxExp;
			case "damage":
				return this.Damage;
			case "defense":
				return this.Defense;
			case "resistancedefense":
				return this.ResistanceDefense;
			case "fire":
				return this.TalentMap.get(TalentType.FIRE_DAM).getValue();
			case "ice":
				return this.TalentMap.get(TalentType.ICE_DAM).getValue();
			case "thunder":
				return this.TalentMap.get(TalentType.THUNDER_DAM).getValue();
			case "wind":
				return this.TalentMap.get(TalentType.WIND_DAM).getValue();
			case "firerate":
				return this.FireRate;
			case "icerate":
				return this.IceRate;
			case "thunderrate":
				return this.ThunderRate;
			case "windrate":
				return this.WindRate;
			case "critrate":
				return this.CritRate;
			case "agirate":
				return this.AgiRate;
			case "talentpoint":
				return this.TalentPoint;
			case "coin":
				return this.Coin;
			case "sealcoin":
				return this.SealCoin;
			case "expaddition":
				return this.ExpAddition;
			default:
				this.Player.sendMessage(RpgStorage.SystemTitle + "§c找不到§f " + str + " §c整數, 無法取得");
		}
		
		return 0;
	}
	
	public EnumMap<TalentType, TalentType> getTalentMap()
	{
		return this.TalentMap;
	}
	
	public void setInteractPlayerName(String target)
	{
		this.InteractPlayer = target;
	}
	
	public void setTeamData(TeamData team)
	{
		this.Team = team;
	}
	
	public void setOpenedInventoryType(PlayerInventoryType type)
	{
		this.OpenedInventoryType = type;
	}
	
	public String getInteractPlayer()
	{
		return this.InteractPlayer;
	}
	
	public EquipmentPlayerData getEquipmentPlayerData()
	{
		return this.Equipment;
	}
	
	public SkillPlayerData getSkillPlayerData()
	{
		return this.Skill;
	}
	
	public FriendPlayerData getFriendPlayerData()
	{
		return this.Friend;
	}
	
	public WarehousePlayerData getWarehousePlayerData()
	{
		return this.Warehouse;
	}
	
	public TeamData getTeamData()
	{
		return this.Team;
	}
	
	public PlayerInventoryType getOpenedInventoryType()
	{
		return this.OpenedInventoryType;
	}
}
