package rong.RongTeam;

import net.md_5.bungee.api.chat.TextComponent;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import rong.RongRPG.RpgStorage;
import rong.RongRPG.Data.PlayerData;
import rong.RongRPG.Data.PlayerInventoryType;
import rong.RongRPG.Util.NMSPacket;
import rong.RongRPG.Util.RpgUtil;
import rong.RongTeam.Data.TeamData;

public class TeamUtil 
{
	public static void createTeam(Player player)
	{
		PlayerData pData = RpgStorage.PlayerDataMap.get(player.getName());
		
		if(pData.getTeamData() == null)
		{
			pData.setTeamData(new TeamData(player));
			
			player.sendMessage(RpgStorage.TeamTitle + "§a建立成功");
			return;
		}
		
		player.sendMessage(RpgStorage.TeamTitle + "§c已有隊伍, 請先退出目前隊伍");
		return;
	}
	
	public static void invitePlayer(Player leader, String tName)
	{
		TeamData pTeam = RpgStorage.PlayerDataMap.get(leader.getName()).getTeamData();
		
		if(pTeam != null)
		{
			if(pTeam.getLeader() == leader)
			{
				Player target = Bukkit.getPlayer(tName);
				
				if(target != null)
				{
					TeamData tTeam = RpgStorage.PlayerDataMap.get(tName).getTeamData();
					
					if(tTeam == null)
					{
						if(!pTeam.isInvited(target))
						{
							pTeam.addInvited(target);
						}
						
						leader.sendMessage(RpgStorage.TeamTitle + "§a發送邀請給§f " + tName + ", §a請等待回覆");

						TextComponent text = new TextComponent(RpgStorage.TeamTitle + leader.getName() + " §a邀請你加入他的隊伍");
						TextComponent button = NMSPacket.ChatButton("§a[接受]", "§a點擊加入§f " + leader.getName() + " §a的隊伍", "/team accept " + leader.getName());
						
						text.addExtra(button);
						
						target.spigot().sendMessage(text);
						return;
					}
					leader.sendMessage(RpgStorage.TeamTitle + "§c對方已有隊伍");
					return;
				}
				leader.sendMessage(RpgStorage.TeamTitle + "§c目標不存在");
				return;
			}
			leader.sendMessage(RpgStorage.TeamTitle + "§c你不是隊長");
			return;
		}
		else
		{
			createTeam(leader);
			invitePlayer(leader, tName);
		}
	}
	
	public static void acceptTeam(Player player, String lName)
	{
		Player leader = Bukkit.getPlayer(lName);
		
		if(leader != null)
		{
			TeamData team = RpgStorage.PlayerDataMap.get(lName).getTeamData();

			if(team != null)
			{
				if(team.getLeader() == leader && team.isInvited(player))
				{
					if(team.getMembers().size() < 8)
					{
						team.removeInvited(player);
						team.addMember(player);
						RpgStorage.PlayerDataMap.get(player.getName()).setTeamData(team);
						
						player.sendMessage(RpgStorage.TeamTitle + "§a成功加入§f " + lName + " §a的隊伍");
						team.sendTeamMessage(RpgStorage.TeamTitle + player.getName() + " §a成功加入隊伍");
						return;
					}
					player.sendMessage(RpgStorage.TeamTitle + "§c對方隊員已滿");
					return;
				}
				player.sendMessage(RpgStorage.TeamTitle + "§c對方沒有邀請你");
				return;
			}
			player.sendMessage(RpgStorage.TeamTitle + "§c對方沒有邀請你");
			return;
		}
		player.sendMessage(RpgStorage.TeamTitle + "§c對方沒有邀請你");
	}
	
	public static void kickMember(Player leader, String mName)
	{
		TeamData team = RpgStorage.PlayerDataMap.get(leader.getName()).getTeamData();
		
		if(team != null)
		{
			if(team.getLeader() == leader)
			{
				Player member = Bukkit.getPlayer(mName);
				
				if(member != null)
				{
					if(team.isMember(member))
					{
						team.removeMember(member);
						RpgStorage.PlayerDataMap.get(mName).setTeamData(null);
						
						team.sendTeamMessage(member.getName() + " §c退出隊伍");
						return;
					}
					leader.sendMessage(RpgStorage.TeamTitle + "§c對方不是你的隊員");
					return;
				}
				leader.sendMessage(RpgStorage.TeamTitle + "§c對方不是你的隊員");
				return;
			}
			leader.sendMessage(RpgStorage.TeamTitle + "§c你不是隊長");
			return;
		}
		leader.sendMessage(RpgStorage.TeamTitle + "§c你沒有隊伍");
	}
	
	public static void leaveTeam(Player player)
	{
		TeamData team = RpgStorage.PlayerDataMap.get(player.getName()).getTeamData();
		
		if(team != null)
		{
			if(team.getLeader() == player)
			{
				for(Player member : team.getMembers())
				{
					RpgStorage.PlayerDataMap.get(member.getName()).setTeamData(null);
				}
				
				team.sendTeamMessage(RpgStorage.TeamTitle + player.getName() + " §c解散隊伍");
			}
			else if(team.getMembers().contains(player))
			{
				RpgStorage.PlayerDataMap.get(player.getName()).setTeamData(null);
				
				team.sendTeamMessage(RpgStorage.TeamTitle + player.getName() + " §c退出隊伍");
			}
			return;
		}
		
		player.sendMessage(RpgStorage.TeamTitle + "§c你目前沒有隊伍");
		return;
	}
	
	public static void openTeamInformationPage(Player player)
	{
		PlayerData pData = RpgStorage.PlayerDataMap.get(player.getName());
		TeamData team = pData.getTeamData();
		
		if(team != null)
		{
			int size = team.isLeader(player) ? 36 : 18;
			Inventory inv = Bukkit.createInventory(player, size, "隊伍資訊");
			int slot = 10;

			for(Player p : team.getMembers())
			{
				if(team.getLeader() == p)
				{
					continue;
				}
				
				inv.setItem(slot, RpgUtil.setSkull(p.getName(), new String[] {"§d§l隊員", "§a等級: " + p.getLevel(), "§9位置: "}, p));
				slot ++;
			}
			
			inv.setItem(0, RpgUtil.setItem(Material.STAINED_GLASS_PANE, 1, 8, "§a§l經驗分配: " + team.getExpMode().getName(), team.isLeader(player) ? new String[]{"§e[左鍵]: 修改"} : null, null, false));
			inv.setItem(1, RpgUtil.setItem(Material.STAINED_GLASS_PANE, 1, 8, "§a§l物品分配: " + team.getItemMode().getName(), team.isLeader(player) ? new String[]{"§e[左鍵]: 修改"} : null, null, false));
			inv.setItem(8, RpgUtil.setItem(Material.BARRIER, 1, 14, "§c[Shift-左鍵]: 退出隊伍", null, null, false));
			inv.setItem(9, RpgUtil.setSkull(team.getLeader().getName(), new String[] {"§d§l隊長", "§a等級: " + team.getLeader().getLevel()}, team.getLeader()));

			if(team.isLeader(player))
			{
				for(int i = 18; i < 26; i++)
				{
					inv.setItem(i, RpgUtil.setItem(Material.STAINED_GLASS_PANE, 1, 7, "", null, null, false));
				}
			}
			
			player.openInventory(inv);
			pData.setOpenedInventoryType(PlayerInventoryType.TEAM_LIST);
			return;
		}
		player.sendMessage(RpgStorage.TeamTitle + "§c你目前沒有隊伍");
	}
}
