package rong.RongRPG.Util;

import java.util.EnumMap;

import net.md_5.bungee.api.chat.TextComponent;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import rong.RongRPG.RpgStorage;
import rong.RongRPG.Data.AnvilData;
import rong.RongRPG.Data.CustomItem;
import rong.RongRPG.Data.EquipmentItem;
import rong.RongRPG.Data.EquipmentPlayerData;
import rong.RongRPG.Data.MerchantData;
import rong.RongRPG.Data.EquipmentItem.EquipmentType;
import rong.RongRPG.Data.MerchantData.MerchantStatus;
import rong.RongRPG.Data.PlayerData;
import rong.RongRPG.Data.PlayerInventoryType;
import rong.RongRPG.Data.SkillData;
import rong.RongRPG.Data.SkillPlayerData;
import rong.RongRPG.Data.SkillPlayerData.SkillSlot;
import rong.RongRPG.Data.TalentType;

public class InventoryUtil 
{	
	public static void openPlayerInformationPage(Player player)
	{
		PlayerData pData = RpgStorage.PlayerDataMap.get(player.getName());
		EquipmentPlayerData epData = pData.getEquipmentPlayerData();
		SkillPlayerData spData = pData.getSkillPlayerData();
		Inventory inv = Bukkit.createInventory(player, 54, player.getName() + " 資訊");
		
		inv.setItem(5, playerInformationIcon(pData));
		inv.setItem(6, RpgUtil.setItem(Material.BOOKSHELF, 1, 0, "§f天賦", null, null, false));
		inv.setItem(7, RpgUtil.setItem(Material.ENCHANTED_BOOK, 1, 0, "§f技能", null, null, false));
		inv.setItem(8, RpgUtil.setItem(Material.SKULL_ITEM, 1, 3, "§f好友", null, null, false));
		inv.setItem(14, RpgUtil.setItem(Material.DIAMOND_SWORD, 1, 0, "§f隊伍", null, null, false));
		inv.setItem(15, RpgUtil.setItem(Material.BANNER, 1, 0, "§f公會", null, null, false));
		inv.setItem(16, RpgUtil.setItem(Material.STRUCTURE_VOID, 1, 0, "§f設定", null, null, false));

		for(EquipmentType type : EquipmentType.values())
		{
			EquipmentItem ei = epData.getEquipmentMap().get(type);
			
			if(ei != null)
			{
				inv.setItem(type.getEquipmentSlot(), ei.getItem());
				continue;
			}
			inv.setItem(type.getEquipmentSlot(), type.getItemIcon());
		}
		for(int i = 46; i < 54; i ++)
		{
			SkillSlot slot = SkillSlot.getSkillSlotBySlot(i);
			SkillData sData = spData.getSlotMap().get(slot);
			
			if(sData != null)
			{
				inv.setItem(i, RpgUtil.setItem(Material.ENCHANTED_BOOK, 1, 0, "§f技能圖示: " + sData.getSkillName(), null, null, false));
				continue;
			}
			
			inv.setItem(i, RpgUtil.setItem(Material.BOOK, 1, 0, "§c尚未設置技能", null, null, false));
		}
		for(int i = 36; i < 45; i ++)
		{
			if(i == 40)
			{
				inv.setItem(i, RpgUtil.setItem(Material.IRON_HOE, 1, 8, " ", null, new ItemFlag[]{ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE}, true));
				continue;
			}
			inv.setItem(i, RpgUtil.setItem(Material.IRON_HOE, 1, 3, " ", null, new ItemFlag[]{ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE}, true));
		}
		for(int i = 4; i < 32; i += 9)
		{
			inv.setItem(i, RpgUtil.setItem(Material.IRON_HOE, 1, 2, " ", null, new ItemFlag[]{ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE}, true));
		}

		player.openInventory(inv);
		pData.setOpenedInventoryType(PlayerInventoryType.INFORMATION);
	}
	
	public static void openSkillPage(Player player)
	{
		PlayerData pData = RpgStorage.PlayerDataMap.get(player.getName());
		SkillPlayerData spData = pData.getSkillPlayerData();
		Inventory inv = Bukkit.createInventory(player, 54, "技能");
		
		int j = 0;
		for(SkillData sData : spData.getSkillList())
		{
			inv.setItem(j, RpgStorage.CustomItemMap.get(sData.getSkillID()).getOriginalItem());
			j++;
		}
		for(int slot = 36; slot < 45; slot ++)
		{
			inv.setItem(slot, RpgUtil.setItem(Material.IRON_HOE, 1, 3, " ", null, new ItemFlag[]{ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE}, true));
		}
		for(int i = 46; i < 54; i ++)
		{
			SkillSlot slot = SkillSlot.getSkillSlotBySlot(i);
			SkillData sData = spData.getSlotMap().get(slot);
			
			if(sData != null)
			{
				inv.setItem(i, RpgUtil.setItem(Material.WRITTEN_BOOK, 1, 0, "§f技能圖示: " + sData.getSkillName(), new String[]{"§a[左鍵]: 更換", "§c[右鍵]: 移除"}, null, false));
				continue;
			}
			
			inv.setItem(i, RpgUtil.setItem(Material.BOOK, 1, 0, "§c尚未設置技能", new String[]{"§a[左鍵]: 選擇"}, null, false));
		}
		inv.setItem(45, pData.getEquipmentPlayerData().getEquipmentMap().get(EquipmentType.WEAPON) != null ? pData.getEquipmentPlayerData().getEquipmentMap().get(EquipmentType.WEAPON).getItem() : null);
				
		player.openInventory(inv);
		pData.setOpenedInventoryType(PlayerInventoryType.SKILL);
	}
	
	public static void openTalentPage(Player player)
	{
		PlayerData pData = RpgStorage.PlayerDataMap.get(player.getName());
		Inventory inv = Bukkit.createInventory(player, 54, "天賦");
		EnumMap<TalentType, TalentType> map = pData.getTalentMap();
		int point = pData.getInt("TalentPoint");
		
		inv.setItem(4, RpgUtil.setItem(Material.PAPER, 1, 0, "§e-----天賦-----",
				new String[] {"§f" + map.get(TalentType.DAM).getTypeName() + ": " + map.get(TalentType.DAM).getValue() + 
				"    §f" + map.get(TalentType.STRONG).getTypeName() + ": " + map.get(TalentType.STRONG).getValue() + 
				"    §f" + map.get(TalentType.SPIRITUAL).getTypeName() + ": " + map.get(TalentType.SPIRITUAL).getValue(),
				"§f" + map.get(TalentType.RESISTANCE).getTypeName() + ": " + map.get(TalentType.RESISTANCE).getValue() + 
				"    §f" + map.get(TalentType.DEADLY).getTypeName() + ": " + map.get(TalentType.DEADLY).getValue() + 
				"    §f" + map.get(TalentType.SENSITIVE).getTypeName() + ": " + map.get(TalentType.SENSITIVE).getValue(),
				"§f" + map.get(TalentType.QUICKLY).getTypeName() + ": " + map.get(TalentType.QUICKLY).getValue() + 
				"    §f" + map.get(TalentType.FIRE_DAM).getTypeName() + ": " + map.get(TalentType.FIRE_DAM).getValue() + 
				"    §f" + map.get(TalentType.ICE_DAM).getTypeName() + ": " + map.get(TalentType.ICE_DAM).getValue(),
				"§f" + map.get(TalentType.THUNDER_DAM).getTypeName() + ": " + map.get(TalentType.THUNDER_DAM).getValue() + 
				"    §f" + map.get(TalentType.WIND_DAM).getTypeName() + ": " + map.get(TalentType.WIND_DAM).getValue(),
				}, null, false));
		inv.setItem(10, RpgUtil.setItem(Material.BOOK, 1, 0, "§f" + map.get(TalentType.DAM).getTypeName(), new String[] {"§f增加 攻擊力", "§f[左鍵]: 分配 1 點", "§f[Shift-左鍵]: 分配 " + point + " 點"}, null, false));
		inv.setItem(12, RpgUtil.setItem(Material.BOOK, 1, 0, "§f" + map.get(TalentType.STRONG).getTypeName(), new String[] {"§f增加 最大生命, 生命恢復", "§f[左鍵]: 分配 1 點", "§f[Shift-左鍵]: 分配 " + point + " 點"}, null, false));
		inv.setItem(14, RpgUtil.setItem(Material.BOOK, 1, 0, "§f" + map.get(TalentType.SPIRITUAL).getTypeName(), new String[] {"§f增加 最大魔力, 魔力恢復", "§f[左鍵]: 分配 1 點", "§f[Shift-左鍵]: 分配 " + point + " 點"}, null, false));
		inv.setItem(16, RpgUtil.setItem(Material.BOOK, 1, 0, "§f" + map.get(TalentType.RESISTANCE).getTypeName(), new String[] {"§f增加 抗性", "§f[左鍵]: 分配 1 點", "§f[Shift-左鍵]: 分配 " + point + " 點"}, null, false));
		inv.setItem(20, RpgUtil.setItem(Material.BOOK, 1, 0, "§f" + map.get(TalentType.DEADLY).getTypeName(), new String[] {"§f增加 爆擊機率", "§f[左鍵]: 分配 1 點", "§f[Shift-左鍵]: 分配 " + point + " 點"}, null, false));
		inv.setItem(22, RpgUtil.setItem(Material.BOOK, 1, 0, "§f" + map.get(TalentType.SENSITIVE).getTypeName(), new String[] {"§f增加迴避機率", "§f[左鍵]: 分配 1 點", "§f[Shift-左鍵]: 分配 " + point + " 點"}, null, false));
		inv.setItem(24, RpgUtil.setItem(Material.BOOK, 1, 0, "§f" + map.get(TalentType.QUICKLY).getTypeName(), new String[] {"§f增加移動速度", "§f[左鍵]: 分配 1 點", "§f[Shift-左鍵]: 分配 " + point + " 點"}, null, false));
		inv.setItem(28, RpgUtil.setItem(Material.BOOK, 1, 0, "§f" + map.get(TalentType.FIRE_DAM).getTypeName(), new String[] {"§f增加 火焰傷害", "§f[左鍵]: 分配 1 點", "§f[Shift-左鍵]: 分配 " + point + " 點"}, null, false));
		inv.setItem(30, RpgUtil.setItem(Material.BOOK, 1, 0, "§f" + map.get(TalentType.ICE_DAM).getTypeName(), new String[] {"§f增加 寒冰傷害", "§f[左鍵]: 分配 1 點", "§f[Shift-左鍵]: 分配 " + point + " 點"}, null, false));
		inv.setItem(32, RpgUtil.setItem(Material.BOOK, 1, 0, "§f" + map.get(TalentType.THUNDER_DAM).getTypeName(), new String[] {"§f增加 雷電傷害", "§f[左鍵]: 分配 1 點", "§f[Shift-左鍵]: 分配 " + point + " 點"}, null, false));
		inv.setItem(34, RpgUtil.setItem(Material.BOOK, 1, 0, "§f" + map.get(TalentType.WIND_DAM).getTypeName(), new String[] {"§f增加 颶風傷害", "§f[左鍵]: 分配 1 點", "§f[Shift-左鍵]: 分配 " + point + " 點"}, null, false));
		inv.setItem(49, playerInformationIcon(pData));

		player.openInventory(inv);
		pData.setOpenedInventoryType(PlayerInventoryType.TALENT);
	}
	
	public static void openPlayerInteractPage(Player player, Player target)
	{
		if(target != null)
		{
			Inventory inv = Bukkit.createInventory(player, 9, "互動");
			PlayerData pData = RpgStorage.PlayerDataMap.get(player.getName());
			String tName = target.getName();
			
			inv.setItem(1, RpgUtil.setItem(Material.SKULL_ITEM, 1, 3, "§f發送好友邀請", new String[]{"§f點擊向 " + tName + " §f發送§9§l 好友§f 邀請"}, null, false));
			inv.setItem(2, RpgUtil.setItem(Material.DIAMOND_SWORD, 1, 0, "§f發送隊伍邀請", new String[]{"§f點擊向 " + tName + " §f發送§a§l 隊伍§f 邀請"}, null, false));
			inv.setItem(3, RpgUtil.setItem(Material.BANNER, 1, 0, "§f發送公會邀請", new String[]{"§f點擊向 " + tName + " §f發送§6§l 公會§f 邀請"}, null, false));
		
			player.openInventory(inv);
			pData.setOpenedInventoryType(PlayerInventoryType.INTERACT);
			pData.setInteractPlayerName(tName);
		}
	}
	
	public static void openAnvilInventory(Player player)
	{
		Inventory inv = Bukkit.createInventory(player, 9, "鐵匠");

		inv.setItem(1, RpgUtil.setItem(Material.IRON_HOE, 1, 9, " ", null, new ItemFlag[]{ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE}, true));
		inv.setItem(2, RpgUtil.setItem(Material.IRON_HOE, 1, 1, " ", null, new ItemFlag[]{ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE}, true));
		inv.setItem(3, RpgUtil.setItem(Material.IRON_HOE, 1, 10, " ", null, new ItemFlag[]{ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE}, true));
		inv.setItem(5, RpgUtil.setItem(Material.IRON_HOE, 1, 9, " ", null, new ItemFlag[]{ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE}, true));
		inv.setItem(6, RpgUtil.setItem(Material.IRON_HOE, 1, 1, " ", null, new ItemFlag[]{ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE}, true));
		inv.setItem(7, RpgUtil.setItem(Material.IRON_HOE, 1, 10, " ", null, new ItemFlag[]{ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE}, true));
		inv.setItem(8, RpgUtil.setItem(Material.IRON_HOE, 1, 32, "§c確定", new String[]{"§f請放入§a§l 物品§f 及§a§l 素材"}, new ItemFlag[]{ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE}, true));
		
		player.openInventory(inv);
		RpgStorage.PlayerDataMap.get(player.getName()).setOpenedInventoryType(PlayerInventoryType.ANVIL);
		
		AnvilData ad = new AnvilData(player, inv);
		RpgStorage.AnvilDataMap.put(player.getName(), ad);
	}
		
	public static void openMerchantListInventory(Player player)
	{
		Inventory inv = Bukkit.createInventory(null, 54, "商店清單");
				
		int slot = 0;
		for(MerchantData md : RpgStorage.MerchantDataMap.values())
		{
			inv.setItem(slot, RpgUtil.setItem(Material.CHEST, 1, 0, "§f商店ID: " + md.getID(), new String[]{"§f商店名稱: " + md.getName()}, null, false));
			slot ++;
		}
		
		player.openInventory(inv);
	}
		
	public static void openMerchantInventory(Player player, int id)
	{
		MerchantData md = RpgStorage.MerchantDataMap.get(id);
		
		if(md != null)
		{
			Inventory inv = Bukkit.createInventory(null, 54, md.getName());
			
			for(int slot : md.getItemMap().keySet())
			{
				inv.setItem(slot, md.getItem(slot).getOriginalItem());
			}
			
			if(player.isOp())
			{
				ItemStack option;
				
				if(md.getStatus() == MerchantStatus.OPEN) option = RpgUtil.setItem(Material.IRON_HOE, 1, 31, "§a開放中", new String[]{"§c點擊切換準備模式"}, new ItemFlag[]{ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_ATTRIBUTES}, true);					
				else option = RpgUtil.setItem(Material.IRON_HOE, 1, 32, "§c準備中", new String[]{"§a點擊切換開放模式"}, new ItemFlag[]{ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_ATTRIBUTES}, true);
				
				inv.setItem(49, option);
			}
			
			player.openInventory(inv);
			RpgStorage.PlayerMerchantMap.put(player.getName(), md);
			RpgStorage.PlayerDataMap.get(player.getName()).setOpenedInventoryType(PlayerInventoryType.MERCHANT);
		}
	}
	
	public static void openPlayerInformationBook(PlayerData pd)
	{
		TextComponent page1 = new TextComponent(
				"§0§l玩家名稱: " + pd.getPlayerName() + "\n" + 
				"§2§l等級: " + pd.getInt("Level") + "  §2§l經驗值: " + pd.getInt("Exp") + "/" + pd.getInt("MaxExp") + "\n" + 
				"§4§l生命: " + pd.getPlayer().getHealth() + "/" + pd.getInt("MaxHealth") + "\n§4§l生命恢復: " + pd.getInt("RegainHealth") + "/5s\n" + 
				"§1§l魔力: " + pd.getInt("Mana") + "/" + pd.getInt("MaxMana") + "\n§1§l魔力恢復: " + pd.getInt("RegainMana") + "/5s\n" +
				"§0§l攻擊力: " + pd.getInt("Damage") + "  §0§l防禦力: " + pd.getInt("Defense") + "\n§0§l§m抗性: " + pd.getInt("ResistanceDefense") + "\n" +
				"§6§l爆擊率: " + pd.getInt("CritRate") + "%  §3§l迴避率: " + pd.getInt("AgiRate") + "%\n" +
				"§0§l火焰: " + pd.getInt("FireRate") + "%  §0§l寒冰: " + pd.getInt("IceRate") + "%\n" + 
				"§0§l雷電: " + pd.getInt("ThunderRate") + "%  §0§l颶風: " + pd.getInt("WindRate") + "%\n" + 
				"§9§l天賦點: " + pd.getInt("TalentPoint") + "\n" + 
				"§6§l金幣: " + pd.getInt("Coin") + "  §0§l§m席爾幣: " + pd.getInt("SealCoin"));
		
		TextComponent page2 = new TextComponent("");
		for(TalentType type : pd.getTalentMap().values())
		{
			page2.addExtra(NMSPacket.addTextComponentHoverEvent(new TextComponent("§0§l§n" + type.getTypeName()), "§f" + type.getDepiction()));
			page2.addExtra("§0: " + type.getValue() + "  ");
			if(pd.getInt("TalentPoint") > 0)
			{
				page2.addExtra(NMSPacket.ChatButton("§0§l§n[+1]", "§f分配 1 點至§f " + type.getTypeName(), ""));
				page2.addExtra("§f  ");
				page2.addExtra(NMSPacket.ChatButton("§0§l§n[+" + pd.getInt("TalentPoint") + "]", "§f分配§f " + pd.getInt("TalentPoint") + " §f點至§f " + type.getTypeName(), ""));
			}
			page2.addExtra("\n");
		}
		
		NMSPacket.openBook(pd.getPlayer(), page1, page2);
	}
	
	public static ItemStack playerInformationIcon(PlayerData pData)
	{
		ItemStack is = RpgUtil.setSkull("§e-----玩家數值-----§f", new String[] {
				"§f玩家名稱: §f" + pData.getPlayer().getName(),
				"§c生命: " + pData.getPlayer().getHealth() + " / " + pData.getInt("MaxHealth") + "    §c生命恢復: " + pData.getInt("RegainHealth") + "/5s",
				"§9魔力: " + pData.getInt("Mana") + " / " + pData.getInt("MaxMana") + "    §9魔力恢復: " + pData.getInt("RegainMana") + "/5s",
				"§a等級: " + pData.getInt("Level") + "    §a經驗值: " + pData.getInt("Exp") + " / " + pData.getInt("MaxExp"),
				"§e攻擊力: " + pData.getInt("Damage") + "    §e防禦力: " + pData.getInt("Defense") + "    §e§m抗性: " + pData.getInt("ResistanceDefense"),
				"§6爆擊率: " + pData.getInt("CritRate") + "%    §3迴避率: " + pData.getInt("AgiRate") + "%",
				"§e火焰: " + pData.getTalentMap().get(TalentType.FIRE_DAM).getValue() + "(" + pData.getInt("FireRate") + "%)    §e寒冰: " + pData.getTalentMap().get(TalentType.ICE_DAM).getValue() + "(" + pData.getInt("IceRate") + "%)",
				"§e雷電: " + pData.getTalentMap().get(TalentType.THUNDER_DAM).getValue() + "(" + pData.getInt("ThunderRate") + "%)    §e颶風: " + pData.getTalentMap().get(TalentType.WIND_DAM).getValue() + "(" + pData.getInt("WindRate") + "%)",	
				"§9天賦點: " + pData.getInt("TalentPoint"),
				"§6金幣: " + pData.getInt("Coin") + "    §e§m席爾幣: " + pData.getInt("SealCoin")}, pData.getPlayer());
		
		return is;
	}
	
	public static void openCustomItemPage(Player player, int page)
	{
		Inventory inv = Bukkit.createInventory(player, 54, "物品清單");
		int i = 0;
		
		for(CustomItem ci : RpgStorage.CustomItemMap.values())
		{
			inv.setItem(i, ci.getOriginalItem());
			i++;
		}
		
		player.openInventory(inv);
		RpgStorage.PlayerDataMap.get(player.getName()).setOpenedInventoryType(PlayerInventoryType.ITEM_LIST);
	}
}
