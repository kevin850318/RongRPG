package rong.RongGuild;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import net.md_5.bungee.api.chat.TextComponent;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import rong.RongGuild.Data.GuildData;
import rong.RongGuild.Data.GuildPlayerData;
import rong.RongGuild.Data.GuildRank;
import rong.RongRPG.RpgStorage;
import rong.RongRPG.Util.NMSPacket;
import rong.RongRPG.Util.RpgUtil;

public class GuildUtil 
{
	//建立公會
	public static void createGuild(Player leader, String guildName)
	{
		if(!GuildStorage.PlayerGuildMap.containsKey(leader.getName()))
		{
			if(getGuildData(guildName) == null)
			{
				GuildPlayerData gpd = new GuildPlayerData(leader.getName(),guildName, 1);
				GuildData guild = new GuildData(guildName);
								
				guild.addGuildMember(leader.getName(), gpd);
				GuildStorage.PlayerGuildMap.put(leader.getName(), guildName);
				GuildStorage.GuildDataMap.put(guildName, guild);
				
				leader.sendMessage(GuildStorage.title + guildName + " §a建立成功");
				return;
			}
		}
		leader.sendMessage(GuildStorage.title + "§c已有公會");
	}
	//新增公會成員
	public static void addGuildMember(Player player, final Player target)
	{
		if(GuildStorage.PlayerGuildMap.containsKey(player.getName()))
		{
			String guildName = getPlayerGuild(player.getName());
			final GuildData guild = getGuildData(guildName);			
			
			if(guild != null)
			{
				if(guild.getMemberList().containsKey(player.getName()))
				{
					GuildPlayerData gpd = guild.getMemberData(player.getName());
					
					if(guild.getGuildRank(gpd.getRank()).hasInvitePermission())
					{
						if(!GuildStorage.PlayerGuildMap.containsKey(target.getName()))
						{
							guild.addGuildInvited(target.getName());
							
							TextComponent msg = new TextComponent(GuildStorage.title + player.getName() + " §a邀請你加入§f " + guild.getGuildName() + " §a公會");
							TextComponent text = NMSPacket.ChatButton("§f§l[接受]", "§f點擊加入§f " + guild.getGuildName(), "/guild accept " + player.getName());
							msg.addExtra(text);
							
							target.spigot().sendMessage(msg);
							player.sendMessage(GuildStorage.title + "§a成功發送邀請 請等待回覆");
							
							Bukkit.getScheduler().runTaskLater(RpgStorage.plugin, new Runnable()
							{
								@Override
								public void run()
								{
									guild.removeGuildInvited(target.getName());
								}
							}, 1200L);
							return;
						}
						player.sendMessage(GuildStorage.title + target + " §c已有公會");
						return;
					}
					player.sendMessage(GuildStorage.title + "§c沒有邀請權限");
					return;
				}
			}
		}
		player.sendMessage(GuildStorage.title + "§c沒有公會");
	}
	//接受邀請
	public static void acceptGuildInvite(Player player, String target)
	{
		if(!GuildStorage.PlayerGuildMap.containsKey(player.getName()))
		{
			if(GuildStorage.PlayerGuildMap.containsKey(target))
			{
				String guildName = getPlayerGuild(target);
				GuildData guild = getGuildData(guildName);
				
				if(guild != null)
				{
					GuildPlayerData pgpd = new GuildPlayerData(player.getName(), guildName, 4);
					
					guild.addGuildMember(player.getName(), pgpd);
					GuildStorage.PlayerGuildMap.put(player.getName(), guildName);
					
					guild.sendGuildMessage(GuildStorage.title + player.getName() + " §a加入公會");
					return;
				}
				player.sendMessage(GuildStorage.title + target + " §c沒有邀請你");
				return;
			}
			player.sendMessage(GuildStorage.title + target + " §c沒有邀請你");
			return;
		}
		player.sendMessage(GuildStorage.title + target + " §c已經有公會了");
	}
	//踢出成員
	public static void kickGuildMember(Player player, String target)
	{
		if(GuildStorage.PlayerGuildMap.containsKey(player.getName()))
		{
			String guildName = getPlayerGuild(player.getName());
			GuildData guild = getGuildData(guildName);
			
			if(guild != null)
			{
				if(guild.getMemberList().containsKey(player.getName()))
				{
					int pRank = guild.getMemberData(player.getName()).getRank();
					
					if(guild.getGuildRank(pRank).hasKickPermission())
					{
						if(guild.getMemberList().containsKey(target))
						{
							GuildPlayerData tgpd = guild.getMemberData(target);
							int tRank = tgpd.getRank();
							
							if(pRank >= tRank)
							{
								guild.removeGuildMember(target);
								GuildStorage.PlayerGuildMap.remove(target);
								
								player.sendMessage(GuildStorage.title + target + " §c踢出公會");
								return;
							}
							player.sendMessage(GuildStorage.title + "§c無法踢出位階較高者");
							return;
						}
						player.sendMessage(GuildStorage.title + target + " §c不是公會成員");
						return;
					}
					player.sendMessage(GuildStorage.title + "§c沒有踢出權限");
					return;
				}
			}
		}
		player.sendMessage(GuildStorage.title + "§c沒有公會");
	}
	//離開公會
	public static void leaveGuild(Player player)
	{
		if(GuildStorage.PlayerGuildMap.containsKey(player.getName()))
		{
			String guildName = getPlayerGuild(player.getName());
			GuildData guild = getGuildData(guildName);
			
			if(guild != null)
			{
				if(guild.getMemberList().containsKey(player.getName()))
				{
					int rank = guild.getMemberData(player.getName()).getRank();
					
					if(rank == 1)
					{
						if(guild.getMemberList().size() == 1)
						{
							guild.removeGuildMember(player.getName());
							GuildStorage.PlayerGuildMap.remove(player.getName());
							GuildStorage.GuildDataMap.remove(player.getName());
							
							player.sendMessage(GuildStorage.title + "§c解散公會");
							return;
						}
						
						player.sendMessage(GuildStorage.title + "§c你是公會長 請先交接下一任會長");
						return;
					}
					
					guild.removeGuildMember(player.getName());
					GuildStorage.PlayerGuildMap.remove(player.getName());
					
					player.sendMessage(GuildStorage.title + "§c退出公會");
					return;
				}
			}
		}
		player.sendMessage(GuildStorage.title + "§c沒有公會");
	}
	//公會資訊頁面
	public static void openGuildInformationPage(Player player)
	{
		if(GuildStorage.PlayerGuildMap.containsKey(player.getName()))
		{
			String guildName = getPlayerGuild(player.getName());
			GuildData guild = getGuildData(guildName);
			
			if(guild != null)
			{
				if(guild.getMemberList().containsKey(player.getName()))
				{
					GuildPlayerData gpd = guild.getMemberData(player.getName());
					int slot = 8;
					Inventory inv = Bukkit.createInventory(player, 54, guildName + " §8公會資訊");

					inv.setItem(0, RpgUtil.setItem(Material.BOOK, 1, (short)0, "§f公告", guild.getGuildRank(gpd.getRank()).hasMotdPermission() ? new String[]{guild.getGuildMotd(), "§a[Shift-左鍵]: 修改公告"} : new String[]{guild.getGuildMotd()}, null, false));
					inv.setItem(8, RpgUtil.setItem(Material.WOOL, 1, (short)14, "§c[Shift-左鍵]: 退出公會", null, null, false));
					if(gpd.getRank() == 1) inv.setItem(7, RpgUtil.setItem(Material.COMMAND, 1, (short)0, "§f權限設定", new String[]{"§c僅公會長可使用"}, null, false));
						
					for(String member : guild.getMemberList().keySet())
					{
						slot ++;

						if(Bukkit.getPlayer(member) != null)
						{
							Player memberp = Bukkit.getPlayer(member);
							
							if(memberp == player)
							{
								inv.setItem(slot, RpgUtil.setSkull(member, new String[]{"§d階級§f: " + guild.getGuildRank(guild.getMemberData(member).getRank()).getRankTitle(), "§a等級: "}, memberp));
								continue;
							}
							
							inv.setItem(slot, RpgUtil.setSkull(member, new String[]{"§d階級§f: " + guild.getGuildRank(guild.getMemberData(member).getRank()).getRankTitle(), "§a等級: ", "§a[Shift-左鍵]: 進入互動介面"}, memberp));
						}
						else
						{
							inv.setItem(slot, RpgUtil.setItem(Material.SKULL_ITEM, 1, (short)1, member, new String[]{"§a[Shift-左鍵]: 進入互動介面"}, null, false));
						}
					}
					
					player.openInventory(inv);
					return;

				}
			}
		}
		player.sendMessage(GuildStorage.title + "§c沒有公會");
	}
	//公會權限頁面
	public static void GuildPermissionPage(Player player)
	{
		if(GuildStorage.PlayerGuildMap.containsKey(player.getName()))
		{
			String guildName = getPlayerGuild(player.getName());
			GuildData guild = getGuildData(guildName);
			
			if(guild != null)
			{
				if(guild.getMemberList().containsKey(player.getName()))
				{
					if(guild.getMemberData(player.getName()).getRank() == 1)
					{
						Inventory inv = Bukkit.createInventory(player, 54, guild.getGuildName() + " §8權限設定");
						ItemStack True = RpgUtil.setItem(Material.WOOL, 1, (short)5, "§a是", new String[]{"§c[Shift-左鍵]: 關閉"}, null, false);
						ItemStack False = RpgUtil.setItem(Material.WOOL, 1, (short)14, "§c否", new String[]{"§a[Shift-左鍵]: 開啟"}, null, false);
						int[] slot = {9, 18, 27, 36};
						int[] subID = {10, 3, 5, 0};
						                                            //Chat        //Invite      //Kick        //Motd
						int[][] PermissionSlot = {{10, 11, 12, 13}, {19, 28, 37}, {20, 29, 38}, {21, 30, 39}, {22, 31, 40}};
						//權限
						inv.setItem(1, RpgUtil.setItem(Material.APPLE, 1, (short)0, "§f訊息", new String[]{"§f是否擁有發送公會訊息權限"}, null, false));
						inv.setItem(2, RpgUtil.setItem(Material.APPLE, 1, (short)0, "§f邀請", new String[]{"§f是否擁有邀請權限"}, null, false));
						inv.setItem(3, RpgUtil.setItem(Material.APPLE, 1, (short)0, "§f踢出", new String[]{"§f是否擁有踢出權限"}, null, false));
						inv.setItem(4, RpgUtil.setItem(Material.APPLE, 1, (short)0, "§f公告", new String[]{"§f是否擁有修改公告權限"}, null, false));
						//階級名稱
						for(int i = 0; i < 4; i ++)
						{
							inv.setItem(slot[i], RpgUtil.setItem(Material.STAINED_CLAY, 1, (short)subID[i], guild.getGuildRank(i + 1).getRankTitle(), new String[]{"§a[Shift-左鍵]: 修改階級名稱", "§c字數上限為 5 個字元"}, null, false));
							//權限是否
							if(i <= 2)
							{
								inv.setItem(PermissionSlot[1][i], guild.getGuildRank(i + 2).hasChatPermission() ? True : False);
								inv.setItem(PermissionSlot[2][i], guild.getGuildRank(i + 2).hasInvitePermission() ? True : False);
								inv.setItem(PermissionSlot[3][i], guild.getGuildRank(i + 2).hasKickPermission() ? True : False);
								inv.setItem(PermissionSlot[4][i], guild.getGuildRank(i + 2).hasMotdPermission() ? True : False);
							}

							for(int j = 0; j < PermissionSlot[0].length; j++)
							{
								//公會長權限
								inv.setItem(PermissionSlot[0][j], RpgUtil.setItem(Material.WOOL, 1, (short)5, "§a是", null, null, false));
							}
						}
						
						player.openInventory(inv);
						return;
					}
					
					player.sendMessage(GuildStorage.title + "§c不是公會長");
					return;
				}
			}
		}
		player.sendMessage(GuildStorage.title + "§c沒有公會");
	}
	//互動頁面
	public static void MemberInteractPage(Player player, String target)
	{
		if(GuildStorage.PlayerGuildMap.containsKey(player.getName()))
		{
			String guildName = getPlayerGuild(player.getName());
			GuildData guild = getGuildData(guildName);
			
			if(guild != null)
			{
				if(guild.getMemberList().containsKey(player.getName()))
				{
					GuildPlayerData gpd = guild.getMemberData(player.getName());
					
					if(guild.getMemberList().containsKey(target))
					{
						Inventory inv = Bukkit.createInventory(player, 54, target + " §8公會互動介面");

						if(gpd.getRank() == 1)
						{
							inv.setItem(0, RpgUtil.setItem(Material.WOOL, 1, (short)5, "§a提升階級", new String[]{"§a[Shift-左鍵]: 確定提升"}, null, false));
							inv.setItem(1, RpgUtil.setItem(Material.WOOL, 1, (short)14, "§c降低階級", new String[]{"§c[Shift-左鍵]: 確定降低"}, null, false));
							inv.setItem(7, RpgUtil.setItem(Material.WOOL, 1, (short)14, "§c交接公會長", new String[]{"§c[Shift-左鍵]: 確定交接"}, null, false));
						}
						
						if(guild.getGuildRank(gpd.getRank()).hasKickPermission())
						{
							inv.setItem(8, RpgUtil.setItem(Material.WOOL, 1, (short)5, "§c踢出成員", new String[]{"§c[Shift-左鍵]: 確定踢出"}, null, false));
						}
						
						player.openInventory(inv);
					}
				}
			}
		}
		player.sendMessage(GuildStorage.title + "§c沒有公會");
	}
	//提升階級
	public static void UpRankLevel(Player player, String target)
	{
		if(GuildStorage.PlayerGuildMap.containsKey(player.getName()))
		{
			String guildName = getPlayerGuild(player.getName());
			GuildData guild = getGuildData(guildName);
			
			if(guild != null)
			{
				if(guild.getMemberList().containsKey(player.getName()))
				{
					GuildPlayerData gpd = guild.getMemberData(player.getName());
					
					if(gpd.getRank() == 1)
					{
						if(guild.getMemberList().containsKey(target))
						{
							GuildPlayerData tgpd = guild.getMemberData(target);
							
							tgpd.upRank();
							
							guild.sendGuildMessage(GuildStorage.title + target + " §a提升為§f " + guild.getGuildRank(tgpd.getRank()));
							return;
						}
						
						player.sendMessage(GuildStorage.title + target + " §c不是公會成員");
						return;
					}
				}
			}
		}
		player.sendMessage(GuildStorage.title + "§c沒有公會");
	}
	//降低階級
	public static void DownRankLevel(Player player, String target)
	{
		if(GuildStorage.PlayerGuildMap.containsKey(player.getName()))
		{
			String guildName = getPlayerGuild(player.getName());
			GuildData guild = getGuildData(guildName);
			
			if(guild != null)
			{				
				if(guild.getMemberList().containsKey(player.getName()))
				{
					GuildPlayerData gpd = guild.getMemberData(player.getName());
					
					if(gpd.getRank() == 1)
					{
						if(guild.getMemberList().containsKey(target))
						{
							GuildPlayerData tgpd = guild.getMemberData(target);
							
							tgpd.downRank();
							
							player.sendMessage(GuildStorage.title + target + " §c降低為§f " + guild.getGuildRank(tgpd.getRank()));
							return;
						}
						
						player.sendMessage(GuildStorage.title + target + " §c不是公會成員");
						return;
					}
				}
			}
		}
		player.sendMessage(GuildStorage.title + "§c沒有公會");
	}
	//儲存階級權限
	public static void saveRankPermission(Inventory inv, Player player)
	{
		if(GuildStorage.PlayerGuildMap.containsKey(player.getName()))
		{
			String guildName = inv.getTitle().replace(" §8權限設定", "");
			
			if(getPlayerGuild(player.getName()).equals(guildName))
			{
				if(GuildStorage.GuildDataMap.containsKey(guildName))
				{
					GuildData guild = getGuildData(guildName);
					
					if(guild.getMemberList().containsKey(player.getName()))
					{
						GuildPlayerData gpd = guild.getMemberData(player.getName());
						
						if(gpd.getRank() == 1)
						{
							int[][] PermissionSlot = {{19, 28, 37}, {20, 29, 38}, {21, 30, 39}, {22, 31, 40}};

							for(int i = 0; i < 3; i ++)
							{
								for(int j = 0; j < PermissionSlot.length; j ++)
								{
									GuildRank rank = guild.getGuildRank(i + 2);
									  
									switch(j)
									{
										case 0:
											if(inv.getItem(PermissionSlot[j][i]).getItemMeta().getDisplayName().contains("§a是"))
											{
												rank.setChatPermission(true);
											}
											else
											{
												rank.setChatPermission(false);
											}
											break;
										case 1:
											if(inv.getItem(PermissionSlot[j][i]).getItemMeta().getDisplayName().contains("§a是"))
											{
												rank.setInvitePermission(true);;
											}
											else
											{
												rank.setInvitePermission(false);;
											}
											break;
										case 2:
											if(inv.getItem(PermissionSlot[j][i]).getItemMeta().getDisplayName().contains("§a是"))
											{
												rank.setKickPermission(true);
											}
											else
											{
												rank.setKickPermission(false);
											}
											break;
										case 3:
											if(inv.getItem(PermissionSlot[j][i]).getItemMeta().getDisplayName().contains("§a是"))
											{
												rank.setMotdPermission(true);
											}
											else
											{
												rank.setMotdPermission(false);
											}
											break;
									}
								}
							}
						}
					}
				}
			}
		}
	}
	//讀取所有公會數據
	public static void loadAllGuildData()
	{
		File dataFile = new File("./plugins/RongGuild/GuildData");
		
		if(dataFile.exists())
		{
			List<String> GuildNames = Arrays.asList(dataFile.list());
						
			for(String GuildName : GuildNames)
			{
				GuildName = GuildName.replace(".yml", "");
				
				GuildData guild = new GuildData(GuildName);
				
				GuildStorage.GuildDataMap.put(GuildName, guild);
			}
		}
	}
	//儲存所有公會數據
	public static void saveAllGuildData()
	{
		for(GuildData guild : GuildStorage.GuildDataMap.values())
		{
			guild.saveGuildData();
		}
		
		GuildStorage.GuildDataMap.clear();
		GuildStorage.PlayerGuildMap.clear();
	}
	//重讀所有公會數據
	public static void reloadGuildData()
	{
		saveAllGuildData();
				
		loadAllGuildData();
	}
	
	public static String getPlayerGuild(String player)
	{
		if(GuildStorage.PlayerGuildMap.containsKey(player))
		{
			return GuildStorage.PlayerGuildMap.get(player);
		}
		
		return null;
	}
	
	public static GuildData getGuildData(String guildName)
	{
		if(GuildStorage.GuildDataMap.containsKey(guildName))
		{
			return GuildStorage.GuildDataMap.get(guildName);
		}
		
		for(GuildData gd : GuildStorage.GuildDataMap.values())
		{
			if(gd.getGuildName().equals(guildName))
			{
				return gd;
			}
		}
		return null;
	}
}
