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
			
			player.sendMessage(RpgStorage.TeamTitle + "��a�إߦ��\");
			return;
		}
		
		player.sendMessage(RpgStorage.TeamTitle + "��c�w������, �Х��h�X�ثe����");
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
						
						leader.sendMessage(RpgStorage.TeamTitle + "��a�o�e�ܽе���f " + tName + ", ��a�е��ݦ^��");

						TextComponent text = new TextComponent(RpgStorage.TeamTitle + leader.getName() + " ��a�ܽЧA�[�J�L������");
						TextComponent button = NMSPacket.ChatButton("��a[����]", "��a�I���[�J��f " + leader.getName() + " ��a������", "/team accept " + leader.getName());
						
						text.addExtra(button);
						
						target.spigot().sendMessage(text);
						return;
					}
					leader.sendMessage(RpgStorage.TeamTitle + "��c���w������");
					return;
				}
				leader.sendMessage(RpgStorage.TeamTitle + "��c�ؼФ��s�b");
				return;
			}
			leader.sendMessage(RpgStorage.TeamTitle + "��c�A���O����");
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
						
						player.sendMessage(RpgStorage.TeamTitle + "��a���\�[�J��f " + lName + " ��a������");
						team.sendTeamMessage(RpgStorage.TeamTitle + player.getName() + " ��a���\�[�J����");
						return;
					}
					player.sendMessage(RpgStorage.TeamTitle + "��c��趤���w��");
					return;
				}
				player.sendMessage(RpgStorage.TeamTitle + "��c���S���ܽЧA");
				return;
			}
			player.sendMessage(RpgStorage.TeamTitle + "��c���S���ܽЧA");
			return;
		}
		player.sendMessage(RpgStorage.TeamTitle + "��c���S���ܽЧA");
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
						
						team.sendTeamMessage(member.getName() + " ��c�h�X����");
						return;
					}
					leader.sendMessage(RpgStorage.TeamTitle + "��c��褣�O�A������");
					return;
				}
				leader.sendMessage(RpgStorage.TeamTitle + "��c��褣�O�A������");
				return;
			}
			leader.sendMessage(RpgStorage.TeamTitle + "��c�A���O����");
			return;
		}
		leader.sendMessage(RpgStorage.TeamTitle + "��c�A�S������");
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
				
				team.sendTeamMessage(RpgStorage.TeamTitle + player.getName() + " ��c�Ѵ�����");
			}
			else if(team.getMembers().contains(player))
			{
				RpgStorage.PlayerDataMap.get(player.getName()).setTeamData(null);
				
				team.sendTeamMessage(RpgStorage.TeamTitle + player.getName() + " ��c�h�X����");
			}
			return;
		}
		
		player.sendMessage(RpgStorage.TeamTitle + "��c�A�ثe�S������");
		return;
	}
	
	public static void openTeamInformationPage(Player player)
	{
		PlayerData pData = RpgStorage.PlayerDataMap.get(player.getName());
		TeamData team = pData.getTeamData();
		
		if(team != null)
		{
			int size = team.isLeader(player) ? 36 : 18;
			Inventory inv = Bukkit.createInventory(player, size, "�����T");
			int slot = 10;

			for(Player p : team.getMembers())
			{
				if(team.getLeader() == p)
				{
					continue;
				}
				
				inv.setItem(slot, RpgUtil.setSkull(p.getName(), new String[] {"��d��l����", "��a����: " + p.getLevel(), "��9��m: "}, p));
				slot ++;
			}
			
			inv.setItem(0, RpgUtil.setItem(Material.STAINED_GLASS_PANE, 1, 8, "��a��l�g����t: " + team.getExpMode().getName(), team.isLeader(player) ? new String[]{"��e[����]: �ק�"} : null, null, false));
			inv.setItem(1, RpgUtil.setItem(Material.STAINED_GLASS_PANE, 1, 8, "��a��l���~���t: " + team.getItemMode().getName(), team.isLeader(player) ? new String[]{"��e[����]: �ק�"} : null, null, false));
			inv.setItem(8, RpgUtil.setItem(Material.BARRIER, 1, 14, "��c[Shift-����]: �h�X����", null, null, false));
			inv.setItem(9, RpgUtil.setSkull(team.getLeader().getName(), new String[] {"��d��l����", "��a����: " + team.getLeader().getLevel()}, team.getLeader()));

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
		player.sendMessage(RpgStorage.TeamTitle + "��c�A�ثe�S������");
	}
}
