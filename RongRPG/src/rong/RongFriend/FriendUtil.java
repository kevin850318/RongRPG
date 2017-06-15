package rong.RongFriend;

import java.util.ArrayList;

import net.md_5.bungee.api.chat.TextComponent;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import rong.RongFriend.Data.FriendPlayerData;
import rong.RongRPG.RpgStorage;
import rong.RongRPG.Data.PlayerData;
import rong.RongRPG.Data.PlayerInventoryType;
import rong.RongRPG.Util.NMSPacket;
import rong.RongRPG.Util.RpgUtil;

public class FriendUtil 
{
	public static void addFriend(Player player, String target)
	{
		Player tPlayer = Bukkit.getPlayer(target);
		FriendPlayerData fpData = RpgStorage.PlayerDataMap.get(player.getName()).getFriendPlayerData();
		
		if(target != null)
		{
			if(!fpData.isFrined(target))
			{
				if(!fpData.isInviter(target))
				{
					FriendPlayerData tfpData = RpgStorage.PlayerDataMap.get(target).getFriendPlayerData();
					
					tfpData.addInviter(player.getName());
					player.sendMessage(RpgStorage.SystemTitle + "§a已向§f " + target + " §a提出§9§l 好友§a 申請, 請等待回覆");
					
					TextComponent text = new TextComponent(RpgStorage.SystemTitle + player.getName() + " §a對你提出好友申請.");
					TextComponent button = NMSPacket.ChatButton("§a§l[接受]", "§a點擊接受§f " + player.getName() + " §a的好友申請", "/friend accept " + player.getName());
					
					text.addExtra(button);
					tPlayer.spigot().sendMessage(text);
					return;
				}
				player.sendMessage(RpgStorage.SystemTitle + "§a已向§f " + target + " §a提出§9§l 好友§a 申請, 請等待回覆");
				return;
			}
			player.sendMessage(RpgStorage.SystemTitle + target + " §c已經是你的好友了");
			return;
		}
		
		player.sendMessage(RpgStorage.SystemTitle + "§c找不到§f " + target);
	}
	
	public static void acceptFriend(Player player, String tName)
	{
		FriendPlayerData fpData = RpgStorage.PlayerDataMap.get(player.getName()).getFriendPlayerData();

		if(fpData.isInviter(tName))
		{
			Player tPlayer = Bukkit.getPlayer(tName);
			FriendPlayerData tfpData = RpgStorage.PlayerDataMap.get(tName).getFriendPlayerData();
			
			fpData.addFriend(tName);
			fpData.removeInviter(tName);
			tfpData.addFriend(player.getName());
			
			if(tPlayer != null)
			{
				tPlayer.sendMessage(RpgStorage.SystemTitle + player.getName() + " §a接受你的好友申請");
			}
			
			player.sendMessage(RpgStorage.SystemTitle + "§a接受§f " + tName + " §a的好友申請");
			return;
		}
		
		player.sendMessage(RpgStorage.SystemTitle + tName + " §c沒有退你提出好友邀請");
	}
	
	public static void denyFriend(Player player, String target)
	{
		FriendPlayerData fpData = RpgStorage.PlayerDataMap.get(player.getName()).getFriendPlayerData();
		
		if(fpData.isInviter(target))
		{
			fpData.removeInviter(target);
			
			player.sendMessage(RpgStorage.SystemTitle + " §c拒絕§f " + target + " §c的好友申請");
			return;
		}
		
		player.sendMessage(RpgStorage.SystemTitle + target + " §c沒有對你提出好友申請");
	}
	
	public static void removeFriend(Player player, String target)
	{
		FriendPlayerData fpData = RpgStorage.PlayerDataMap.get(player.getName()).getFriendPlayerData();
		
		if(fpData.isFrined(target))
		{
			FriendPlayerData tfpData = RpgStorage.PlayerDataMap.get(target).getFriendPlayerData();
			
			fpData.removeFriend(target);
			tfpData.removeFriend(player.getName());
			return;
		}
		
		player.sendMessage(RpgStorage.SystemTitle + target + " §c不是你的好友");
	}
	
	public static void openFriendListBook(Player player)
	{
		FriendPlayerData fpd = RpgStorage.PlayerDataMap.get(player.getName()).getFriendPlayerData();
		TextComponent page1 = new TextComponent("§0好友名單");
		page1.addExtra("\n");
		
		for(String fName : fpd.getFriendList())
		{
			FriendPlayerData ffpd = RpgStorage.PlayerDataMap.get(fName).getFriendPlayerData();
			String str = Bukkit.getPlayer(fName) != null ? "§0§l" : "§7§l";
			
			page1.addExtra(NMSPacket.addTextComponentHoverEvent(new TextComponent(str + fName), ffpd.getTitle()));
			page1.addExtra("   ");
			page1.addExtra(NMSPacket.ChatButton("§c§l[移除]", "§f移除§f " + fName, "/friend remove " + fName));
			page1.addExtra("\n");
		}
		
		NMSPacket.openBook(player, page1);
	}
	
	public static void openInviterListBook(Player player)
	{
		FriendPlayerData fpd = RpgStorage.PlayerDataMap.get(player.getName()).getFriendPlayerData();
		TextComponent page1 = new TextComponent("§0申請名單");
		page1.addExtra("\n");

		for(String fName : fpd.getInviterList())
		{
			page1.addExtra("§0" + fName);
			page1.addExtra("   ");
			page1.addExtra(NMSPacket.ChatButton("§a§l[接受]", "§a接受§f " + fName + " §f申請", "/friend accept " + fName));
			page1.addExtra("   ");
			page1.addExtra(NMSPacket.ChatButton("§c§l[拒絕]", "§c拒絕§f " + fName + " §f申請", "/friend deny " + fName));
			page1.addExtra("\n");
		}
		
		NMSPacket.openBook(player, page1);
	}
	
	public static void openFriendPage(Player player)
	{
		Inventory inv = Bukkit.createInventory(player, 9, "好友選單");
		
		inv.setItem(0, RpgUtil.setItem(Material.STAINED_GLASS_PANE, 1, 7, "§f邀請好友", null, null, false));
		inv.setItem(2, RpgUtil.setItem(Material.STAINED_GLASS_PANE, 1, 7, "§f好友名單", null, null, false));
		inv.setItem(3, RpgUtil.setItem(Material.STAINED_GLASS_PANE, 1, 7, "§f申請名單", null, null, false));
		
		player.openInventory(inv);
	}
	
	public static void openFriendListPage(Player player, Inventory inv, int page)
	{
		PlayerData pData = RpgStorage.PlayerDataMap.get(player.getName());
		FriendPlayerData fpData = pData.getFriendPlayerData();
		ArrayList<String> fList = fpData.getFriendList();
		
		for(int i = page; i < page + 45; i ++)
		{
			if(i >= fList.size())
			{
				break;
			}
			
			String friend = fList.get(i);
			Player fPlayer = Bukkit.getPlayer(friend);
			ItemStack skull = RpgUtil.setItem(Material.SKULL_ITEM, 1, 1, friend, new String[]{"§8§l離線..."}, null, false);
		
			if(fPlayer != null)
			{
				FriendPlayerData tData = RpgStorage.PlayerDataMap.get(friend).getFriendPlayerData();
				
				skull = RpgUtil.setSkull(friend, new String[]{"等級: " + tData.getTitle()}, fPlayer);
			}
			
			inv.setItem(i - page, skull);
		}
		
		if(page + 45 <= fList.size())
		{
			inv.setItem(49, RpgUtil.setItem(Material.PAPER, page + 1, 0, "" + page, null, null, false));
			inv.setItem(51, RpgUtil.setItem(Material.ITEM_FRAME, page + 2, 0, "§f下一頁", null, null, false));
		}
		
		if(page > 0)
		{
			inv.setItem(47, RpgUtil.setItem(Material.ITEM_FRAME, page, 0, "§f上一頁", null, null, false));
		}
		
		inv.setItem(0, RpgUtil.setItem(Material.STAINED_GLASS_PANE, 1, 7, "§f好友", null, null, false));
		pData.setOpenedInventoryType(PlayerInventoryType.FRIEND_LIST);
	}
	
	public static void openFriendSettingPage(Player player, String friend)
	{
		Inventory inv = Bukkit.createInventory(player, 9, "好友選項 " + friend);

		inv.setItem(8, RpgUtil.setItem(Material.WOOL, 1, 14, "§c移除好友", null, null, false));
		
		player.openInventory(inv);
		RpgStorage.PlayerDataMap.get(player.getName()).setOpenedInventoryType(PlayerInventoryType.FRIEND_SETTING);
	}
	
	public static void openInviterListPage(Player player, Inventory inv, int page)
	{
		PlayerData pData = RpgStorage.PlayerDataMap.get(player.getName());;
		FriendPlayerData pfData = pData.getFriendPlayerData();
		ArrayList<String> iList = pfData.getInviterList();
		
		for(int i = page; i < page + 45; i ++)
		{
			if(i >= iList.size())
			{
				break;
			}
			
			String inviter = iList.get(i);
			Player iPlayer = Bukkit.getPlayer(inviter);
			ItemStack skull = RpgUtil.setItem(Material.SKULL_ITEM, 1, 1, inviter, new String[]{"§8§l離線..."}, null, false);
		
			if(iPlayer != null)
			{
				skull = RpgUtil.setSkull(inviter, new String[]{"§a[左鍵]: 接受", "§c[右鍵]: 拒絕"}, iPlayer);
			}
			
			inv.setItem(i - page, skull);
		}
		
		if(page + 45 <= iList.size())
		{
			inv.setItem(51, RpgUtil.setItem(Material.ITEM_FRAME, page + 2, 0, "§f下一頁", null, null, false));
		}
		
		if(page > 0)
		{
			inv.setItem(47, RpgUtil.setItem(Material.ITEM_FRAME, page, 0, "§f上一頁", null, null, false));
		}
		
		inv.setItem(1, RpgUtil.setItem(Material.STAINED_GLASS_PANE, 1, 7, "§f申請", null, null, false));
		pData.setOpenedInventoryType(PlayerInventoryType.FRIEND_INVITER);
	}
	
	public static void openInvitePage(Player player)
	{
		AnvilInventory inv = (AnvilInventory) Bukkit.createInventory(player, InventoryType.ANVIL, "邀請");
		
		inv.setItem(2, RpgUtil.setItem(Material.STAINED_GLASS_PANE, 1, 7, "§f確定", null, null, false));
		
		player.openInventory(inv);
		RpgStorage.PlayerDataMap.get(player.getName()).setOpenedInventoryType(PlayerInventoryType.FRIEND_INVITE);
	}
}
