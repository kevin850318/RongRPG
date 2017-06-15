package rong.RongGuild.Listener;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import rong.RongGuild.GuildStorage;
import rong.RongGuild.GuildUtil;
import rong.RongGuild.Data.GuildData;
import rong.RongGuild.Data.GuildPlayerData;
import rong.RongRPG.Util.RpgUtil;

public class InventoryClick implements Listener
{
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event)
	{
		Player player = (Player) event.getWhoClicked();
		Inventory inv = event.getClickedInventory();
		ItemStack item = event.getCurrentItem();
		int slot = event.getSlot();
		
		if(inv.getTitle().contains(" §8公會資訊"))
		{
			event.setCancelled(true);
			
			if(inv.getType() == InventoryType.CHEST && inv.getType() != InventoryType.PLAYER)
			{
				if(event.getClick() == ClickType.LEFT)
				{
					if(item.getItemMeta().getDisplayName().equals("§f權限設定"))
					{
						GuildUtil.GuildPermissionPage(player);
						return;
					}
				}
				else if(event.getClick() == ClickType.SHIFT_LEFT)
				{
					if(item.getItemMeta().getLore().contains("§a[Shift-左鍵]: 進入互動介面"))
					{
						String member = item.getItemMeta().getDisplayName();

						GuildUtil.MemberInteractPage(player, member);
						return;
					}
					
					if(item.getItemMeta().getDisplayName().equals("§c[Shift-左鍵]: 退出公會"))
					{
						GuildUtil.leaveGuild(player);
						player.closeInventory();
						return;
					}
				}
			}
		}
		else if(inv.getTitle().contains(" §8公會互動介面"))
		{
			event.setCancelled(true);
			
			if(inv.getType() == InventoryType.CHEST && inv.getType() != InventoryType.PLAYER)
			{
				if(event.getClick() == ClickType.SHIFT_LEFT)
				{
					String member = inv.getTitle().replace(" §8公會互動介面", "");
					
					if(item.getItemMeta().getLore().contains("§c[Shift-左鍵]: 確定踢出"))
					{						
						GuildUtil.kickGuildMember(player, member);
						player.closeInventory();
						return;
					}
					
					if(item.getItemMeta().getLore().contains("§a[Shift-左鍵]: 確定提升"))
					{
						GuildUtil.UpRankLevel(player, member);
						player.closeInventory();
						return;
					}
					
					if(item.getItemMeta().getLore().contains("§c[Shift-左鍵]: 確定降低"))
					{
						GuildUtil.DownRankLevel(player, member);
						player.closeInventory();
						return;
					}
				}
			}
		}
		else if(inv.getTitle().contains(" §8權限設定"))
		{
			event.setCancelled(true);
			
			if(inv.getType() == InventoryType.CHEST && inv.getType() != InventoryType.PLAYER)
			{
				if(GuildStorage.PlayerGuildMap.containsKey(player.getName()))
				{
					String guildName = GuildUtil.getPlayerGuild(player.getName());
					
					if(GuildUtil.getGuildData(guildName) != null)
					{
						GuildData guild = GuildUtil.getGuildData(guildName);
						
						if(guild.getMemberList().containsKey(player.getName()))
						{
							GuildPlayerData gpd = guild.getMemberData(guildName);

							if(event.getClick() == ClickType.SHIFT_LEFT)
							{
								if(inv.getTitle().contains(guildName))
								{
									if(item.getType() == Material.WOOL)
									{
										if(RpgUtil.isRpgItem(item))
										{
											if(item.getItemMeta().getLore().contains("§c[Shift-左鍵]: 關閉") || item.getItemMeta().getLore().contains("§a[Shift-左鍵]: 開啟"))
											{
												ItemStack True = RpgUtil.setItem(Material.WOOL, 1, (short)5, "§a是", new String[]{"§c[Shift-左鍵]: 關閉"}, null, false);
												ItemStack False = RpgUtil.setItem(Material.WOOL, 1, (short)14, "§c否", new String[]{"§a[Shift-左鍵]: 開啟"}, null, false);

												inv.setItem(slot, item.getItemMeta().getDisplayName().equals("§a是") ? False : True);
											}
										}
									}
									else if(item.getType() == Material.STAINED_CLAY)
									{			
										if(item.getItemMeta().getLore().contains("§a[Shift-左鍵]: 修改位階名稱"))
										{
											gpd.setChangeRankTitle(true);
											
											switch(slot)
											{
												case 9:
													gpd.setChangingRank(1);
												case 18:
													gpd.setChangingRank(2);
												case 27:
													gpd.setChangingRank(3);
												case 36:
													gpd.setChangingRank(4);
												default:
													gpd.setChangingRank(0);
													gpd.setChangeRankTitle(false);
											}
											
											player.closeInventory();
											player.sendMessage(GuildStorage.title + "§a請輸入'>[位階名稱]'修改位階名稱 (字數上限為 5 個字元)");
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}
}
