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
		
		if(inv.getTitle().contains(" ��8���|��T"))
		{
			event.setCancelled(true);
			
			if(inv.getType() == InventoryType.CHEST && inv.getType() != InventoryType.PLAYER)
			{
				if(event.getClick() == ClickType.LEFT)
				{
					if(item.getItemMeta().getDisplayName().equals("��f�v���]�w"))
					{
						GuildUtil.GuildPermissionPage(player);
						return;
					}
				}
				else if(event.getClick() == ClickType.SHIFT_LEFT)
				{
					if(item.getItemMeta().getLore().contains("��a[Shift-����]: �i�J���ʤ���"))
					{
						String member = item.getItemMeta().getDisplayName();

						GuildUtil.MemberInteractPage(player, member);
						return;
					}
					
					if(item.getItemMeta().getDisplayName().equals("��c[Shift-����]: �h�X���|"))
					{
						GuildUtil.leaveGuild(player);
						player.closeInventory();
						return;
					}
				}
			}
		}
		else if(inv.getTitle().contains(" ��8���|���ʤ���"))
		{
			event.setCancelled(true);
			
			if(inv.getType() == InventoryType.CHEST && inv.getType() != InventoryType.PLAYER)
			{
				if(event.getClick() == ClickType.SHIFT_LEFT)
				{
					String member = inv.getTitle().replace(" ��8���|���ʤ���", "");
					
					if(item.getItemMeta().getLore().contains("��c[Shift-����]: �T�w��X"))
					{						
						GuildUtil.kickGuildMember(player, member);
						player.closeInventory();
						return;
					}
					
					if(item.getItemMeta().getLore().contains("��a[Shift-����]: �T�w����"))
					{
						GuildUtil.UpRankLevel(player, member);
						player.closeInventory();
						return;
					}
					
					if(item.getItemMeta().getLore().contains("��c[Shift-����]: �T�w���C"))
					{
						GuildUtil.DownRankLevel(player, member);
						player.closeInventory();
						return;
					}
				}
			}
		}
		else if(inv.getTitle().contains(" ��8�v���]�w"))
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
											if(item.getItemMeta().getLore().contains("��c[Shift-����]: ����") || item.getItemMeta().getLore().contains("��a[Shift-����]: �}��"))
											{
												ItemStack True = RpgUtil.setItem(Material.WOOL, 1, (short)5, "��a�O", new String[]{"��c[Shift-����]: ����"}, null, false);
												ItemStack False = RpgUtil.setItem(Material.WOOL, 1, (short)14, "��c�_", new String[]{"��a[Shift-����]: �}��"}, null, false);

												inv.setItem(slot, item.getItemMeta().getDisplayName().equals("��a�O") ? False : True);
											}
										}
									}
									else if(item.getType() == Material.STAINED_CLAY)
									{			
										if(item.getItemMeta().getLore().contains("��a[Shift-����]: �ק�춥�W��"))
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
											player.sendMessage(GuildStorage.title + "��a�п�J'>[�춥�W��]'�ק�춥�W�� (�r�ƤW���� 5 �Ӧr��)");
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
