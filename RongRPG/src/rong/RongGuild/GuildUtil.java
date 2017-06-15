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
	//�إߤ��|
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
				
				leader.sendMessage(GuildStorage.title + guildName + " ��a�إߦ��\");
				return;
			}
		}
		leader.sendMessage(GuildStorage.title + "��c�w�����|");
	}
	//�s�W���|����
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
							
							TextComponent msg = new TextComponent(GuildStorage.title + player.getName() + " ��a�ܽЧA�[�J��f " + guild.getGuildName() + " ��a���|");
							TextComponent text = NMSPacket.ChatButton("��f��l[����]", "��f�I���[�J��f " + guild.getGuildName(), "/guild accept " + player.getName());
							msg.addExtra(text);
							
							target.spigot().sendMessage(msg);
							player.sendMessage(GuildStorage.title + "��a���\�o�e�ܽ� �е��ݦ^��");
							
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
						player.sendMessage(GuildStorage.title + target + " ��c�w�����|");
						return;
					}
					player.sendMessage(GuildStorage.title + "��c�S���ܽ��v��");
					return;
				}
			}
		}
		player.sendMessage(GuildStorage.title + "��c�S�����|");
	}
	//�����ܽ�
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
					
					guild.sendGuildMessage(GuildStorage.title + player.getName() + " ��a�[�J���|");
					return;
				}
				player.sendMessage(GuildStorage.title + target + " ��c�S���ܽЧA");
				return;
			}
			player.sendMessage(GuildStorage.title + target + " ��c�S���ܽЧA");
			return;
		}
		player.sendMessage(GuildStorage.title + target + " ��c�w�g�����|�F");
	}
	//��X����
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
								
								player.sendMessage(GuildStorage.title + target + " ��c��X���|");
								return;
							}
							player.sendMessage(GuildStorage.title + "��c�L�k��X�춥������");
							return;
						}
						player.sendMessage(GuildStorage.title + target + " ��c���O���|����");
						return;
					}
					player.sendMessage(GuildStorage.title + "��c�S����X�v��");
					return;
				}
			}
		}
		player.sendMessage(GuildStorage.title + "��c�S�����|");
	}
	//���}���|
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
							
							player.sendMessage(GuildStorage.title + "��c�Ѵ����|");
							return;
						}
						
						player.sendMessage(GuildStorage.title + "��c�A�O���|�� �Х��汵�U�@���|��");
						return;
					}
					
					guild.removeGuildMember(player.getName());
					GuildStorage.PlayerGuildMap.remove(player.getName());
					
					player.sendMessage(GuildStorage.title + "��c�h�X���|");
					return;
				}
			}
		}
		player.sendMessage(GuildStorage.title + "��c�S�����|");
	}
	//���|��T����
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
					Inventory inv = Bukkit.createInventory(player, 54, guildName + " ��8���|��T");

					inv.setItem(0, RpgUtil.setItem(Material.BOOK, 1, (short)0, "��f���i", guild.getGuildRank(gpd.getRank()).hasMotdPermission() ? new String[]{guild.getGuildMotd(), "��a[Shift-����]: �ק綠�i"} : new String[]{guild.getGuildMotd()}, null, false));
					inv.setItem(8, RpgUtil.setItem(Material.WOOL, 1, (short)14, "��c[Shift-����]: �h�X���|", null, null, false));
					if(gpd.getRank() == 1) inv.setItem(7, RpgUtil.setItem(Material.COMMAND, 1, (short)0, "��f�v���]�w", new String[]{"��c�Ȥ��|���i�ϥ�"}, null, false));
						
					for(String member : guild.getMemberList().keySet())
					{
						slot ++;

						if(Bukkit.getPlayer(member) != null)
						{
							Player memberp = Bukkit.getPlayer(member);
							
							if(memberp == player)
							{
								inv.setItem(slot, RpgUtil.setSkull(member, new String[]{"��d���š�f: " + guild.getGuildRank(guild.getMemberData(member).getRank()).getRankTitle(), "��a����: "}, memberp));
								continue;
							}
							
							inv.setItem(slot, RpgUtil.setSkull(member, new String[]{"��d���š�f: " + guild.getGuildRank(guild.getMemberData(member).getRank()).getRankTitle(), "��a����: ", "��a[Shift-����]: �i�J���ʤ���"}, memberp));
						}
						else
						{
							inv.setItem(slot, RpgUtil.setItem(Material.SKULL_ITEM, 1, (short)1, member, new String[]{"��a[Shift-����]: �i�J���ʤ���"}, null, false));
						}
					}
					
					player.openInventory(inv);
					return;

				}
			}
		}
		player.sendMessage(GuildStorage.title + "��c�S�����|");
	}
	//���|�v������
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
						Inventory inv = Bukkit.createInventory(player, 54, guild.getGuildName() + " ��8�v���]�w");
						ItemStack True = RpgUtil.setItem(Material.WOOL, 1, (short)5, "��a�O", new String[]{"��c[Shift-����]: ����"}, null, false);
						ItemStack False = RpgUtil.setItem(Material.WOOL, 1, (short)14, "��c�_", new String[]{"��a[Shift-����]: �}��"}, null, false);
						int[] slot = {9, 18, 27, 36};
						int[] subID = {10, 3, 5, 0};
						                                            //Chat        //Invite      //Kick        //Motd
						int[][] PermissionSlot = {{10, 11, 12, 13}, {19, 28, 37}, {20, 29, 38}, {21, 30, 39}, {22, 31, 40}};
						//�v��
						inv.setItem(1, RpgUtil.setItem(Material.APPLE, 1, (short)0, "��f�T��", new String[]{"��f�O�_�֦��o�e���|�T���v��"}, null, false));
						inv.setItem(2, RpgUtil.setItem(Material.APPLE, 1, (short)0, "��f�ܽ�", new String[]{"��f�O�_�֦��ܽ��v��"}, null, false));
						inv.setItem(3, RpgUtil.setItem(Material.APPLE, 1, (short)0, "��f��X", new String[]{"��f�O�_�֦���X�v��"}, null, false));
						inv.setItem(4, RpgUtil.setItem(Material.APPLE, 1, (short)0, "��f���i", new String[]{"��f�O�_�֦��ק綠�i�v��"}, null, false));
						//���ŦW��
						for(int i = 0; i < 4; i ++)
						{
							inv.setItem(slot[i], RpgUtil.setItem(Material.STAINED_CLAY, 1, (short)subID[i], guild.getGuildRank(i + 1).getRankTitle(), new String[]{"��a[Shift-����]: �קﶥ�ŦW��", "��c�r�ƤW���� 5 �Ӧr��"}, null, false));
							//�v���O�_
							if(i <= 2)
							{
								inv.setItem(PermissionSlot[1][i], guild.getGuildRank(i + 2).hasChatPermission() ? True : False);
								inv.setItem(PermissionSlot[2][i], guild.getGuildRank(i + 2).hasInvitePermission() ? True : False);
								inv.setItem(PermissionSlot[3][i], guild.getGuildRank(i + 2).hasKickPermission() ? True : False);
								inv.setItem(PermissionSlot[4][i], guild.getGuildRank(i + 2).hasMotdPermission() ? True : False);
							}

							for(int j = 0; j < PermissionSlot[0].length; j++)
							{
								//���|���v��
								inv.setItem(PermissionSlot[0][j], RpgUtil.setItem(Material.WOOL, 1, (short)5, "��a�O", null, null, false));
							}
						}
						
						player.openInventory(inv);
						return;
					}
					
					player.sendMessage(GuildStorage.title + "��c���O���|��");
					return;
				}
			}
		}
		player.sendMessage(GuildStorage.title + "��c�S�����|");
	}
	//���ʭ���
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
						Inventory inv = Bukkit.createInventory(player, 54, target + " ��8���|���ʤ���");

						if(gpd.getRank() == 1)
						{
							inv.setItem(0, RpgUtil.setItem(Material.WOOL, 1, (short)5, "��a���ɶ���", new String[]{"��a[Shift-����]: �T�w����"}, null, false));
							inv.setItem(1, RpgUtil.setItem(Material.WOOL, 1, (short)14, "��c���C����", new String[]{"��c[Shift-����]: �T�w���C"}, null, false));
							inv.setItem(7, RpgUtil.setItem(Material.WOOL, 1, (short)14, "��c�汵���|��", new String[]{"��c[Shift-����]: �T�w�汵"}, null, false));
						}
						
						if(guild.getGuildRank(gpd.getRank()).hasKickPermission())
						{
							inv.setItem(8, RpgUtil.setItem(Material.WOOL, 1, (short)5, "��c��X����", new String[]{"��c[Shift-����]: �T�w��X"}, null, false));
						}
						
						player.openInventory(inv);
					}
				}
			}
		}
		player.sendMessage(GuildStorage.title + "��c�S�����|");
	}
	//���ɶ���
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
							
							guild.sendGuildMessage(GuildStorage.title + target + " ��a���ɬ���f " + guild.getGuildRank(tgpd.getRank()));
							return;
						}
						
						player.sendMessage(GuildStorage.title + target + " ��c���O���|����");
						return;
					}
				}
			}
		}
		player.sendMessage(GuildStorage.title + "��c�S�����|");
	}
	//���C����
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
							
							player.sendMessage(GuildStorage.title + target + " ��c���C����f " + guild.getGuildRank(tgpd.getRank()));
							return;
						}
						
						player.sendMessage(GuildStorage.title + target + " ��c���O���|����");
						return;
					}
				}
			}
		}
		player.sendMessage(GuildStorage.title + "��c�S�����|");
	}
	//�x�s�����v��
	public static void saveRankPermission(Inventory inv, Player player)
	{
		if(GuildStorage.PlayerGuildMap.containsKey(player.getName()))
		{
			String guildName = inv.getTitle().replace(" ��8�v���]�w", "");
			
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
											if(inv.getItem(PermissionSlot[j][i]).getItemMeta().getDisplayName().contains("��a�O"))
											{
												rank.setChatPermission(true);
											}
											else
											{
												rank.setChatPermission(false);
											}
											break;
										case 1:
											if(inv.getItem(PermissionSlot[j][i]).getItemMeta().getDisplayName().contains("��a�O"))
											{
												rank.setInvitePermission(true);;
											}
											else
											{
												rank.setInvitePermission(false);;
											}
											break;
										case 2:
											if(inv.getItem(PermissionSlot[j][i]).getItemMeta().getDisplayName().contains("��a�O"))
											{
												rank.setKickPermission(true);
											}
											else
											{
												rank.setKickPermission(false);
											}
											break;
										case 3:
											if(inv.getItem(PermissionSlot[j][i]).getItemMeta().getDisplayName().contains("��a�O"))
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
	//Ū���Ҧ����|�ƾ�
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
	//�x�s�Ҧ����|�ƾ�
	public static void saveAllGuildData()
	{
		for(GuildData guild : GuildStorage.GuildDataMap.values())
		{
			guild.saveGuildData();
		}
		
		GuildStorage.GuildDataMap.clear();
		GuildStorage.PlayerGuildMap.clear();
	}
	//��Ū�Ҧ����|�ƾ�
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
