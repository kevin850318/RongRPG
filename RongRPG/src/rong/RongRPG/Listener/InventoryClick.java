package rong.RongRPG.Listener;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import rong.RongFriend.FriendUtil;
import rong.RongFriend.Listener.FriendInventoryClick;
import rong.RongRPG.RpgStorage;
import rong.RongRPG.Data.AnvilData;
import rong.RongRPG.Data.AnvilData.AnvilStatus;
import rong.RongRPG.Data.CustomItem;
import rong.RongRPG.Data.CustomItem.ItemType;
import rong.RongRPG.Data.EquipmentItem.EquipmentType;
import rong.RongRPG.Data.EquipmentPlayerData;
import rong.RongRPG.Data.GemItem;
import rong.RongRPG.Data.MerchantData;
import rong.RongRPG.Data.MerchantData.MerchantStatus;
import rong.RongRPG.Data.EquipmentItem;
import rong.RongRPG.Data.PlayerData;
import rong.RongRPG.Data.PlayerInventoryType;
import rong.RongRPG.Data.SkillData;
import rong.RongRPG.Data.SkillPlayerData;
import rong.RongRPG.Data.SkillPlayerData.SkillSlot;
import rong.RongRPG.Data.TalentType;
import rong.RongRPG.Util.InventoryUtil;
import rong.RongRPG.Util.RpgUtil;
import rong.RongTeam.TeamUtil;
import rong.RongTeam.Listener.TeamInventoryClick;
import rong.RongWarehouse.Listener.WarehouseInventoryClick;

public class InventoryClick implements Listener
{
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event)
	{
		if(event.getClickedInventory() == null)
		{
			return;
		}
		
		Player player = (Player) event.getWhoClicked();
		PlayerData pData = RpgStorage.PlayerDataMap.get(player.getName());
		ItemStack is = event.getCurrentItem();
		
//		player.sendMessage("Slot: " + event.getInventory().getItem(event.getSlot())); //null
//		player.sendMessage("CurrentItem: " + event.getCurrentItem()); //Material.AIR in slot
//		player.sendMessage("CursorItem: " + event.getCursor()); //Material.AIR in cursor
		
		if(RpgUtil.isRpgItem(is)) //無法移動
		{
			if(is.getItemMeta().hasLore() && is.getItemMeta().getLore().contains("§c無法移動")) event.setCancelled(true); 
			if(is.getItemMeta().getDisplayName().equals(" ")) event.setCancelled(true);
		}
		
		if(pData.getOpenedInventoryType() != null)
		{
			if(pData.getOpenedInventoryType() == PlayerInventoryType.INFORMATION)
			{
				clickInformationInventory(event);
			}
			else if(pData.getOpenedInventoryType() == PlayerInventoryType.TALENT)
			{
				clickTalentInventory(event);
			}
			else if(pData.getOpenedInventoryType() == PlayerInventoryType.SKILL)
			{
				clickSkillInventory(event);
			}
			else if(pData.getOpenedInventoryType() == PlayerInventoryType.INTERACT)
			{
				clickInteractInventory(event);
			}
			else if(pData.getOpenedInventoryType() == PlayerInventoryType.ANVIL)
			{
				clickAnvilInventory(event);
			}
			else if(pData.getOpenedInventoryType() == PlayerInventoryType.MERCHANT)
			{
				clickMerchantInventory(event);
			}
			else if(pData.getOpenedInventoryType() == PlayerInventoryType.ITEM_LIST)
			{
				event.setCancelled(true);
				
				if(event.getClickedInventory().getType() == InventoryType.CHEST)
				{
					player.getInventory().addItem(is);
				}
			}
			else if(pData.getOpenedInventoryType().name().contains("FRIEND"))
			{
				FriendInventoryClick.clickFriendInventory(event);
			}
			else if(pData.getOpenedInventoryType().name().contains("TEAM"))
			{
				TeamInventoryClick.clickTeamInventory(event);
			}
			else if(pData.getOpenedInventoryType().name().contains("WAREHOUSE"))
			{
				WarehouseInventoryClick.clickWarehouseInventory(event);
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	void clickInformationInventory(InventoryClickEvent event)
	{
		if(event.getClickedInventory().getType() == InventoryType.CHEST)
		{
			event.setCancelled(true);

			if(event.getClick() == ClickType.LEFT)
			{
				Player player = (Player) event.getWhoClicked();
				PlayerData pData = RpgStorage.PlayerDataMap.get(player.getName());
				Inventory inv = event.getInventory();
				ItemStack is = event.getCurrentItem();
				int slot = event.getSlot();

				switch(slot)
				{
				case 5:
					InventoryUtil.openPlayerInformationBook(pData);
					return;
				case 6:
					InventoryUtil.openTalentPage(player);
					return;
				case 7:
					InventoryUtil.openSkillPage(player);
					return;
				case 8:
					FriendUtil.openFriendPage(player);
					return;
				}
							
				EquipmentType type = EquipmentType.getEquipmentType(slot);
				
				if(type != null)
				{
					EquipmentPlayerData epData = pData.getEquipmentPlayerData();
					ItemStack cursor = event.getCursor().clone();
					CustomItem ci = CustomItem.getCustomItem(cursor);
					
					if(ci != null)
					{
						if(ci.getItemType() == ItemType.EQUIPMENT)
						{
							EquipmentItem ei = new EquipmentItem(cursor);
							ei.loadEquipmentType();
							
							if(ei.getEquipmentType() == type)
							{					
								event.setCancelled(false);
								
								if(is.isSimilar(type.getItemIcon()))
								{
									event.setCurrentItem(null);
								}
								
								epData.setEquipment(type, ei);
								pData.UpdateData();
								inv.setItem(5, InventoryUtil.playerInformationIcon(pData));
							}
						}
					}
					else if(!(is.isSimilar(type.getItemIcon())))
					{
						ItemStack slotItem = inv.getItem(slot);
						
						event.setCursor(slotItem);
						inv.setItem(slot, type.getItemIcon());
						
						epData.getEquipmentMap().remove(type);
						pData.UpdateData();
						inv.setItem(5, InventoryUtil.playerInformationIcon(pData));
					}
				}
			}
		}
	}
	
	void clickTalentInventory(InventoryClickEvent event)
	{
		event.setCancelled(true);
		
		if(event.getClickedInventory().getType() == InventoryType.CHEST)
		{
			Player player = (Player) event.getWhoClicked();
			PlayerData pData = RpgStorage.PlayerDataMap.get(player.getName());
			int slot = event.getSlot();

			if(event.getClick() == ClickType.LEFT)
			{
				TalentType type = TalentType.getTalentType(slot);
				
				if(type != null)
				{
					pData.addTalentPoint(type, 1);
					
					InventoryUtil.openTalentPage(pData.getPlayer());
				}
			}
			else if(event.getClick() == ClickType.SHIFT_LEFT)
			{
				int talentPoint = pData.getInt("TalentPoint");

				TalentType type = TalentType.getTalentType(slot);
				
				if(type != null)
				{
					pData.addTalentPoint(type, talentPoint);
					
					InventoryUtil.openTalentPage(pData.getPlayer());
				}
				
				InventoryUtil.openTalentPage(pData.getPlayer());
			}
		}
	}
	
	void clickSkillInventory(InventoryClickEvent event)
	{
		event.setCancelled(true);
		
		if(event.getClickedInventory().getType() == InventoryType.CHEST)
		{
			Player player = (Player) event.getWhoClicked();
			SkillPlayerData spData = RpgStorage.PlayerDataMap.get(player.getName()).getSkillPlayerData();
			ItemStack is = event.getCurrentItem();
			Inventory inv = event.getInventory();
			int slot = event.getSlot();

			if(slot > 45 && slot < 54)
			{
				if(event.getClick() == ClickType.LEFT)
				{
					SkillSlot sSlot = SkillSlot.getSkillSlotBySlot(slot);
					
					if(spData.getChoosedSkillSlot() != null)
					{
						SkillSlot cSlot = spData.getChoosedSkillSlot();
						SkillData sData = spData.getSlotMap().get(cSlot);
						
						if(sData != null)
						{
							inv.setItem(cSlot.getEquipSlot(), RpgUtil.setItem(Material.WRITTEN_BOOK, 1, 0, "§f技能圖示: " + sData.getSkillName(), new String[]{"§a[左鍵]: 更換", "§c[右鍵]: 移除"}, null, false));
						}
						else
						{
							inv.setItem(cSlot.getEquipSlot(), RpgUtil.setItem(Material.BOOK, 1, 0, "§f技能", new String[]{"§a[左鍵]: 選擇"}, null, false));
						}
					}
				
					inv.setItem(slot, RpgUtil.setItem(Material.BOOK_AND_QUILL, 1, 0, "§f選擇", new String[]{"§f點擊欲放置技能", "§c[右鍵]: 取消"}, null, false));
					spData.setChoosedSkillSlot(sSlot);
				}
				if(event.getClick() == ClickType.RIGHT)
				{
					if(is.getType() == Material.BOOK_AND_QUILL)
					{
						SkillSlot sSlot = SkillSlot.getSkillSlotBySlot(slot);
						SkillData sData = spData.getSlotMap().get(sSlot);
						
						if(sData != null)
						{
							inv.setItem(sSlot.getEquipSlot(), RpgUtil.setItem(Material.WRITTEN_BOOK, 1, 0, "§f技能圖示: " + sData.getSkillName(), new String[]{"§a[左鍵]: 更換", "§c[右鍵]: 移除"}, null, false));
							return;
						}
						
						inv.setItem(slot, RpgUtil.setItem(Material.BOOK, 1, 0, "§f技能", new String[]{"§a[左鍵]: 選擇"}, null, false));
						spData.setChoosedSkillSlot(null);
					}
					else if(is.getType() == Material.WRITTEN_BOOK)
					{
						SkillSlot sSlot = SkillSlot.getSkillSlotBySlot(slot);
						
						inv.setItem(slot, RpgUtil.setItem(Material.BOOK, 1, 0, "§f技能", new String[]{"§a[左鍵]: 選擇"}, null, false));
						spData.getSlotMap().remove(sSlot);
					}
				}
			}
			else if(slot >= 0 && slot < 36)
			{
				if(is.getType() == Material.ENCHANTED_BOOK)
				{
					if(spData.getChoosedSkillSlot() != null)
					{
						SkillSlot cSlot = spData.getChoosedSkillSlot();
						CustomItem ci = CustomItem.getCustomItem(is);
						
						if(ci != null)
						{
							SkillData sData = RpgStorage.SkillDataMap.get(ci.getItemID());
							
							inv.setItem(cSlot.getEquipSlot(), RpgUtil.setItem(Material.WRITTEN_BOOK, 1, 0, "§f技能圖示: " + sData.getSkillName(), new String[]{"§a[左鍵]: 更換", "§c[右鍵]: 移除"}, null, false));
							spData.getSlotMap().put(cSlot, sData);
							spData.setChoosedSkillSlot(null);
						}
					}
				}
			}
		}
	}
	
	void clickInteractInventory(InventoryClickEvent event)
	{
		event.setCancelled(true);
		
		if(event.getClickedInventory().getType() == InventoryType.CHEST)
		{
			Player player = (Player) event.getWhoClicked();
			PlayerData pData = RpgStorage.PlayerDataMap.get(player.getName());
			String tName = pData.getInteractPlayer();
			int slot = event.getSlot();
			
			if(event.getClick() == ClickType.LEFT)
			{
				switch(slot)
				{
				case 1:
					FriendUtil.addFriend(player, tName);
					break;
				case 2:
					TeamUtil.invitePlayer(player, tName);
					break;
				}
				
				player.closeInventory();
			}
		}
	}
		
	void clickAnvilInventory(InventoryClickEvent event)
	{
		event.setCancelled(true);
		
		Player player = (Player) event.getWhoClicked();
		AnvilData ad = RpgStorage.AnvilDataMap.get(player.getName());
		
		if(ad != null)
		{
			if(ad.getAnvilStatus() == AnvilStatus.DONE)
			{
				ad.closeAnvil();
				return;
			}
			
			ItemStack is = event.getCurrentItem().clone();
			CustomItem ci = CustomItem.getCustomItem(is);
			int slot = event.getSlot();
			
			if(event.getClickedInventory().getType() == InventoryType.PLAYER)
			{
				if(ci != null)
				{
					if(ci.getItemType() == ItemType.EQUIPMENT)
					{
						EquipmentItem ei = new EquipmentItem(is);
						ei.loadEquipmentContent(RpgStorage.PlayerDataMap.get(player.getName()));
						
						if(ad.getUsedItem() != null)
						{
							player.getInventory().addItem(ad.getUsedItem().getItem());
						}
						
						ad.setAnvilInventoryItem(0, is);
						event.setCurrentItem(null);
						ad.setUsedItem(ei);
					}
					else if(ci.getItemType() == ItemType.GEM)
					{
						GemItem gi = (GemItem) RpgStorage.CustomItemMap.get(ci.getItemID());
						ItemStack tempItem = is.clone();
						
						if(ad.getMixItem() != null)
						{
							player.getInventory().addItem(ad.getAnvilInventoryItem(4));
						}
						
						if(is.getAmount() >= 1)
						{
							tempItem.setAmount(1);
							is.setAmount(is.getAmount() - 1);
							
							if(is.getAmount() <= 0)
							{
								is = null;
							}
						}
						ad.setAnvilInventoryItem(4, tempItem);
						event.setCurrentItem(is);
						ad.setMixItem(gi);
					}
				}
			}
			else if(event.getClickedInventory().getType() == InventoryType.CHEST)
			{
				switch(slot)
				{
				case 0:
					player.getInventory().addItem(is);
					ad.setAnvilInventoryItem(slot, null);
					ad.setUsedItem(null);
					break;
				case 4:
					player.getInventory().addItem(is);
					ad.setAnvilInventoryItem(slot, null);
					ad.setMixItem(null);
					break;
				}
				
				if(slot == 8)
				{
					ad.confirm();
					return;
				}
			}
			
			ad.updateAnvilInventory();
			return;
		}
		
		player.closeInventory();
	}
	
	@SuppressWarnings("deprecation")
	void clickMerchantInventory(InventoryClickEvent event)
	{
		Player player = (Player) event.getWhoClicked();
		PlayerData pData = RpgStorage.PlayerDataMap.get(player.getName());
		MerchantData md = RpgStorage.PlayerMerchantMap.get(player.getName());
		int slot = event.getSlot();
		
		if(md != null)
		{	
			if(event.getClickedInventory().getType() == InventoryType.CHEST)
			{
				if(slot == 49)
				{
					event.setCancelled(true);
					
					if(player.isOp())
					{
						if(md.getStatus() == MerchantStatus.PREPARE)
						{
							md.setStatus(MerchantStatus.OPEN);
							player.sendMessage(RpgStorage.SystemTitle + "§a商店開放");
							InventoryUtil.openMerchantInventory(player, md.getID());
						}
						else
						{
							md.setStatus(MerchantStatus.PREPARE);
							player.sendMessage(RpgStorage.SystemTitle + "§c商店關閉");
							InventoryUtil.openMerchantInventory(player, md.getID());
						}
						return;
					}
				}
				
				if(md.getStatus() == MerchantStatus.PREPARE)
				{
					if(event.getCursor().getType() != Material.AIR)
					{
						CustomItem ci = CustomItem.getCustomItem(event.getCursor().clone());
						
						if(ci != null)
						{
							md.setItem(slot, ci);
						}
					}
				}
				else
				{
					event.setCancelled(true);
					
					if(event.getCursor().getType() != Material.AIR)
					{
						CustomItem ci = CustomItem.getCustomItem(event.getCursor().clone());
						int coin = 1;
						
						if(ci != null)
						{
							coin = (int) (ci.getCoin() *0.8);
						}
						
						pData.setInt("Coin", pData.getInt("Coin") + coin);
						player.sendMessage(RpgStorage.SystemTitle + "§a獲得§6 " + coin + " §6金幣");
						
						ItemStack item = event.getCursor().clone();
						item.setAmount(item.getAmount() - 1);
						if(item.getAmount() <= 0)
						{
							item = null;
						}
						
						event.setCursor(item);
						return;
					}
					
					CustomItem ci = md.getItem(slot);
					
					if(ci != null)
					{
						int coin = ci.getCoin();
						
						if(pData.getInt("Coin") >= coin)
						{
							player.getInventory().addItem(ci.getOriginalItem());
							pData.setInt("Coin", pData.getInt("Coin") - coin);
							player.sendMessage(RpgStorage.SystemTitle + "§c花費§6 " + coin + " §6金幣§f, 購買§f " + ci.getOriginalItem().getItemMeta().getDisplayName());
							return;
						}
						
						player.sendMessage(RpgStorage.SystemTitle + "§6金幣§f §c不足");
					}
				}
			}
			
			return;
		}
		
		player.closeInventory();
	}
	
	public static void changeItem(ItemStack current, ItemStack cursor)
	{
		
	}
}