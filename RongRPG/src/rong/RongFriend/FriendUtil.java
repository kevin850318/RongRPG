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
					player.sendMessage(RpgStorage.SystemTitle + "��a�w�V��f " + target + " ��a���X��9��l �n�͡�a �ӽ�, �е��ݦ^��");
					
					TextComponent text = new TextComponent(RpgStorage.SystemTitle + player.getName() + " ��a��A���X�n�ͥӽ�.");
					TextComponent button = NMSPacket.ChatButton("��a��l[����]", "��a�I��������f " + player.getName() + " ��a���n�ͥӽ�", "/friend accept " + player.getName());
					
					text.addExtra(button);
					tPlayer.spigot().sendMessage(text);
					return;
				}
				player.sendMessage(RpgStorage.SystemTitle + "��a�w�V��f " + target + " ��a���X��9��l �n�͡�a �ӽ�, �е��ݦ^��");
				return;
			}
			player.sendMessage(RpgStorage.SystemTitle + target + " ��c�w�g�O�A���n�ͤF");
			return;
		}
		
		player.sendMessage(RpgStorage.SystemTitle + "��c�䤣�족f " + target);
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
				tPlayer.sendMessage(RpgStorage.SystemTitle + player.getName() + " ��a�����A���n�ͥӽ�");
			}
			
			player.sendMessage(RpgStorage.SystemTitle + "��a������f " + tName + " ��a���n�ͥӽ�");
			return;
		}
		
		player.sendMessage(RpgStorage.SystemTitle + tName + " ��c�S���h�A���X�n���ܽ�");
	}
	
	public static void denyFriend(Player player, String target)
	{
		FriendPlayerData fpData = RpgStorage.PlayerDataMap.get(player.getName()).getFriendPlayerData();
		
		if(fpData.isInviter(target))
		{
			fpData.removeInviter(target);
			
			player.sendMessage(RpgStorage.SystemTitle + " ��c�ڵ���f " + target + " ��c���n�ͥӽ�");
			return;
		}
		
		player.sendMessage(RpgStorage.SystemTitle + target + " ��c�S����A���X�n�ͥӽ�");
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
		
		player.sendMessage(RpgStorage.SystemTitle + target + " ��c���O�A���n��");
	}
	
	public static void openFriendListBook(Player player)
	{
		FriendPlayerData fpd = RpgStorage.PlayerDataMap.get(player.getName()).getFriendPlayerData();
		TextComponent page1 = new TextComponent("��0�n�ͦW��");
		page1.addExtra("\n");
		
		for(String fName : fpd.getFriendList())
		{
			FriendPlayerData ffpd = RpgStorage.PlayerDataMap.get(fName).getFriendPlayerData();
			String str = Bukkit.getPlayer(fName) != null ? "��0��l" : "��7��l";
			
			page1.addExtra(NMSPacket.addTextComponentHoverEvent(new TextComponent(str + fName), ffpd.getTitle()));
			page1.addExtra("   ");
			page1.addExtra(NMSPacket.ChatButton("��c��l[����]", "��f������f " + fName, "/friend remove " + fName));
			page1.addExtra("\n");
		}
		
		NMSPacket.openBook(player, page1);
	}
	
	public static void openInviterListBook(Player player)
	{
		FriendPlayerData fpd = RpgStorage.PlayerDataMap.get(player.getName()).getFriendPlayerData();
		TextComponent page1 = new TextComponent("��0�ӽЦW��");
		page1.addExtra("\n");

		for(String fName : fpd.getInviterList())
		{
			page1.addExtra("��0" + fName);
			page1.addExtra("   ");
			page1.addExtra(NMSPacket.ChatButton("��a��l[����]", "��a������f " + fName + " ��f�ӽ�", "/friend accept " + fName));
			page1.addExtra("   ");
			page1.addExtra(NMSPacket.ChatButton("��c��l[�ڵ�]", "��c�ڵ���f " + fName + " ��f�ӽ�", "/friend deny " + fName));
			page1.addExtra("\n");
		}
		
		NMSPacket.openBook(player, page1);
	}
	
	public static void openFriendPage(Player player)
	{
		Inventory inv = Bukkit.createInventory(player, 9, "�n�Ϳ��");
		
		inv.setItem(0, RpgUtil.setItem(Material.STAINED_GLASS_PANE, 1, 7, "��f�ܽЦn��", null, null, false));
		inv.setItem(2, RpgUtil.setItem(Material.STAINED_GLASS_PANE, 1, 7, "��f�n�ͦW��", null, null, false));
		inv.setItem(3, RpgUtil.setItem(Material.STAINED_GLASS_PANE, 1, 7, "��f�ӽЦW��", null, null, false));
		
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
			ItemStack skull = RpgUtil.setItem(Material.SKULL_ITEM, 1, 1, friend, new String[]{"��8��l���u..."}, null, false);
		
			if(fPlayer != null)
			{
				FriendPlayerData tData = RpgStorage.PlayerDataMap.get(friend).getFriendPlayerData();
				
				skull = RpgUtil.setSkull(friend, new String[]{"����: " + tData.getTitle()}, fPlayer);
			}
			
			inv.setItem(i - page, skull);
		}
		
		if(page + 45 <= fList.size())
		{
			inv.setItem(49, RpgUtil.setItem(Material.PAPER, page + 1, 0, "" + page, null, null, false));
			inv.setItem(51, RpgUtil.setItem(Material.ITEM_FRAME, page + 2, 0, "��f�U�@��", null, null, false));
		}
		
		if(page > 0)
		{
			inv.setItem(47, RpgUtil.setItem(Material.ITEM_FRAME, page, 0, "��f�W�@��", null, null, false));
		}
		
		inv.setItem(0, RpgUtil.setItem(Material.STAINED_GLASS_PANE, 1, 7, "��f�n��", null, null, false));
		pData.setOpenedInventoryType(PlayerInventoryType.FRIEND_LIST);
	}
	
	public static void openFriendSettingPage(Player player, String friend)
	{
		Inventory inv = Bukkit.createInventory(player, 9, "�n�Ϳﶵ " + friend);

		inv.setItem(8, RpgUtil.setItem(Material.WOOL, 1, 14, "��c�����n��", null, null, false));
		
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
			ItemStack skull = RpgUtil.setItem(Material.SKULL_ITEM, 1, 1, inviter, new String[]{"��8��l���u..."}, null, false);
		
			if(iPlayer != null)
			{
				skull = RpgUtil.setSkull(inviter, new String[]{"��a[����]: ����", "��c[�k��]: �ڵ�"}, iPlayer);
			}
			
			inv.setItem(i - page, skull);
		}
		
		if(page + 45 <= iList.size())
		{
			inv.setItem(51, RpgUtil.setItem(Material.ITEM_FRAME, page + 2, 0, "��f�U�@��", null, null, false));
		}
		
		if(page > 0)
		{
			inv.setItem(47, RpgUtil.setItem(Material.ITEM_FRAME, page, 0, "��f�W�@��", null, null, false));
		}
		
		inv.setItem(1, RpgUtil.setItem(Material.STAINED_GLASS_PANE, 1, 7, "��f�ӽ�", null, null, false));
		pData.setOpenedInventoryType(PlayerInventoryType.FRIEND_INVITER);
	}
	
	public static void openInvitePage(Player player)
	{
		AnvilInventory inv = (AnvilInventory) Bukkit.createInventory(player, InventoryType.ANVIL, "�ܽ�");
		
		inv.setItem(2, RpgUtil.setItem(Material.STAINED_GLASS_PANE, 1, 7, "��f�T�w", null, null, false));
		
		player.openInventory(inv);
		RpgStorage.PlayerDataMap.get(player.getName()).setOpenedInventoryType(PlayerInventoryType.FRIEND_INVITE);
	}
}
