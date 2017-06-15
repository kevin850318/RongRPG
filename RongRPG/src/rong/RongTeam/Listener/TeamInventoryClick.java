package rong.RongTeam.Listener;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import rong.RongRPG.RpgStorage;
import rong.RongRPG.Data.PlayerData;
import rong.RongRPG.Data.PlayerInventoryType;
import rong.RongRPG.Util.RpgUtil;
import rong.RongTeam.TeamUtil;
import rong.RongTeam.Data.TeamData;
import rong.RongTeam.Data.TeamData.ExpMode;
import rong.RongTeam.Data.TeamData.ItemMode;

public class TeamInventoryClick
{
	public static void clickTeamInventory(InventoryClickEvent event)
	{
		event.setCancelled(true);
		
		Player player = (Player) event.getWhoClicked();
		PlayerData pData = RpgStorage.PlayerDataMap.get(player.getName());
		ItemStack is = event.getCurrentItem();
		int slot = event.getSlot();
		
		if(event.getClickedInventory().getType() == InventoryType.CHEST)
		{
			if(event.getClick() == ClickType.SHIFT_LEFT)
			{
				if(slot == 8)
				{
					TeamUtil.leaveTeam(player);
					player.closeInventory();
				}
			}
		}
		
		if(pData.getOpenedInventoryType() == PlayerInventoryType.TEAM_LIST)
		{
			if(event.getClickedInventory().getType() == InventoryType.CHEST)
			{
				TeamData team = pData.getTeamData();
				
				if(team.isLeader(player))
				{
					if(event.getClick() == ClickType.LEFT)
					{
						Inventory inv = event.getInventory();
						
						if(slot == 0)
						{
							ExpMode em = team.getExpMode();

							inv.setItem(27, RpgUtil.setItem(Material.STAINED_GLASS_PANE, 1, em == ExpMode.SHARE ? 5 : 8, "¡±f" + em.getName(), null, null, false));
							inv.setItem(28, RpgUtil.setItem(Material.STAINED_GLASS_PANE, 1, em == ExpMode.NO ? 5 : 8, "¡±f" + em.getName(), null, null, false));
							
							pData.setOpenedInventoryType(PlayerInventoryType.TEAM_EXP_SETTING);
						}
						else if(slot == 1)
						{
							ItemMode im = team.getItemMode();
							
							inv.setItem(27, RpgUtil.setItem(Material.STAINED_GLASS_PANE, 1, im == ItemMode.ORDER ? 5 : 8, "¡±f" + im.getName(), null, null, false));
							inv.setItem(28, RpgUtil.setItem(Material.STAINED_GLASS_PANE, 1, im == ItemMode.RANDOM ? 5 : 8, "¡±f" + im.getName(), null, null, false));
							inv.setItem(29, RpgUtil.setItem(Material.STAINED_GLASS_PANE, 1, im == ItemMode.NO ? 5 : 8, "¡±f" + im.getName(), null, null, false));
							
							pData.setOpenedInventoryType(PlayerInventoryType.TEAM_ITEM_SETTING);
						}
						else if(is.getType() == Material.SKULL_ITEM)
						{
							String mName = is.getItemMeta().getDisplayName();
							
							inv.setItem(27, RpgUtil.setItem(Material.STAINED_GLASS_PANE, 1, 14, "¡±c¡±l[Shift-¥ªÁä]: ½ð¥X¶¤¥î", null, null, false));
							
							pData.setInteractPlayerName(mName);
							pData.setOpenedInventoryType(PlayerInventoryType.TEAM_MEMBER_SETTING);
						}
					}
				}
			}
		}
		else if(pData.getOpenedInventoryType() == PlayerInventoryType.TEAM_MEMBER_SETTING)
		{
			if(event.getClickedInventory().getType() == InventoryType.CHEST)
			{
				if(event.getClick() == ClickType.SHIFT_LEFT)
				{
					if(slot == 27)
					{
						TeamUtil.kickMember(player, pData.getInteractPlayer());
						TeamUtil.openTeamInformationPage(player);
					}
				}
			}
		}
		else if(pData.getOpenedInventoryType() == PlayerInventoryType.TEAM_EXP_SETTING)
		{
			if(event.getClickedInventory().getType() == InventoryType.CHEST)
			{
				TeamData team = pData.getTeamData();
				
				if(slot == 27)
				{
					team.setExoMode(ExpMode.SHARE);
					TeamUtil.openTeamInformationPage(player);
				}
				else if(slot == 28)
				{
					team.setExoMode(ExpMode.NO);
					TeamUtil.openTeamInformationPage(player);
				}
			}
		}
		else if(pData.getOpenedInventoryType() == PlayerInventoryType.TEAM_ITEM_SETTING)
		{
			if(event.getClickedInventory().getType() == InventoryType.CHEST)
			{
				TeamData team = pData.getTeamData();
				
				if(slot == 27)
				{
					team.setItemMode(ItemMode.ORDER);
					TeamUtil.openTeamInformationPage(player);
				}
				else if(slot == 28)
				{
					team.setItemMode(ItemMode.RANDOM);
					TeamUtil.openTeamInformationPage(player);
				}
				else if(slot == 29)
				{
					team.setItemMode(ItemMode.NO);
					TeamUtil.openTeamInformationPage(player);
				}
			}
		}
	}
}
