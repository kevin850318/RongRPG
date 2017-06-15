package rong.RongFriend.Listener;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import rong.RongFriend.FriendUtil;
import rong.RongRPG.RpgStorage;
import rong.RongRPG.Data.PlayerData;
import rong.RongRPG.Data.PlayerInventoryType;
import rong.RongRPG.Util.RpgUtil;

public class FriendInventoryClick 
{
	public static void clickFriendInventory(InventoryClickEvent event)
	{
		event.setCancelled(true);

		Player player = (Player) event.getWhoClicked();
		PlayerData pData = RpgStorage.PlayerDataMap.get(player.getName());
		ItemStack is = event.getCurrentItem();
		Inventory inv = event.getInventory();
		int slot = event.getSlot();
		
		if(event.getClickedInventory().getType() == InventoryType.CHEST)
		{
			switch(slot)
			{
			case 0:
				FriendUtil.openFriendListPage(player, inv, 0);
				break;
			case 1:
				FriendUtil.openInviterListPage(player, inv, 0);
				break;
			case 2:
				break;
			}
			
			if(pData.getOpenedInventoryType() == PlayerInventoryType.FRIEND_LIST)
			{
				if(event.getSlot() == 47 || event.getSlot() == 51)
				{
					FriendUtil.openFriendListPage(player, inv, is.getAmount() - 1);
				}
				
				if(event.getClick() == ClickType.LEFT)
				{
					if(is.getType() == Material.SKULL_ITEM)
					{
						String fName = is.getItemMeta().getDisplayName();
						
						pData.setInteractPlayerName(fName);
						
						inv.setItem(8, RpgUtil.setItem(Material.STAINED_GLASS_PANE, 1, 14, "§c移除§f " + fName, new String[] {"§c[左鍵]: 移除好友"}, null, false));
					}
					
					if(event.getSlot() == 8)
					{
						if(pData.getInteractPlayer() != null)
						{
							FriendUtil.removeFriend(player, pData.getInteractPlayer());
							
							FriendUtil.openFriendPage(player);
						}
					}
				}
			}
			else if(pData.getOpenedInventoryType() == PlayerInventoryType.FRIEND_INVITER)
			{
				if(event.getSlot() == 47 || event.getSlot() == 51)
				{
					FriendUtil.openInviterListPage(player, inv, is.getAmount() - 1);
				}
				
				if(event.getClick() == ClickType.LEFT)
				{
					if(is.getType() == Material.SKULL_ITEM)
					{
						String fName = is.getItemMeta().getDisplayName();
						
						pData.setInteractPlayerName(fName);
						
						inv.setItem(7, RpgUtil.setItem(Material.STAINED_GLASS_PANE, 1, 5, "§c接受§f " + fName, new String[] {"§c[左鍵]: 接受申請"}, null, false));
						inv.setItem(8, RpgUtil.setItem(Material.STAINED_GLASS_PANE, 1, 14, "§c拒絕§f " + fName, new String[] {"§c[左鍵]: 拒絕申請"}, null, false));
					}
					
					if(event.getSlot() == 7 || event.getSlot() == 8)
					{
						String iTarget = pData.getInteractPlayer();
						
						if(iTarget != null)
						{
							if(event.getSlot() == 7)
							{
								FriendUtil.acceptFriend(player, iTarget);
							}
							else
							{
								FriendUtil.denyFriend(player, iTarget);
							}
							
							FriendUtil.openFriendPage(player);
						}
					}
				}
			}
		}
		else if(event.getClickedInventory().getType() == InventoryType.ANVIL)
		{
		}
	}
}
