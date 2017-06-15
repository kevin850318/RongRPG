package rong.RongTeam.Data;

import java.util.ArrayList;

import org.bukkit.entity.Player;

import rong.RongRPG.RpgStorage;
import rong.RongRPG.Data.PlayerData;

public class TeamData 
{
	private Player Leader;
	private ArrayList<Player> Members = new ArrayList<>(); 
	private ArrayList<Player> Invited = new ArrayList<>();
	private ExpMode expMode = ExpMode.SHARE;
	private ItemMode itemMode = ItemMode.ORDER;
	private int OrderNumber = 0;
	
	public TeamData(Player leader)
	{
		this.Leader = leader;
		this.Members.add(leader);
	}
	
	public void sendTeamMessage(String msg)
	{
		for(Player member : this.getMembers())
		{
			member.sendMessage(msg);
		}
	}
	
	public void giveTeamExp(Player player, int exp)
	{
		PlayerData pData = RpgStorage.PlayerDataMap.get(player.getName());
		ArrayList<Player> near = getNearMembers(player);
		
		if(this.expMode == ExpMode.SHARE)
		{
			if(near.size() >= 2)
			{
				exp = (int) Math.round((exp + (exp * (near.size() * 0.05))) / near.size());

				for(Player nPlayer : near)
				{
					PlayerData nData = RpgStorage.PlayerDataMap.get(nPlayer.getName());
					
					nData.giveExp(exp);
					
					nPlayer.sendMessage(RpgStorage.TeamTitle + "§a獲得§f " + exp + " §a經驗值");
				}
			}
			else
			{
				pData.giveExp(exp);
				
				player.sendMessage(RpgStorage.TeamTitle + "§a獲得§f " + exp + " §a經驗值");
			}
		}
		else
		{
			if(near.size() >= 2)
			{
				exp = (int) Math.round((exp + (exp * (near.size() * 0.05))) / near.size());
				
				pData.giveExp(exp);
			}
			else
			{
				pData.giveExp(exp);
			}
			
			player.sendMessage(RpgStorage.TeamTitle + "§a獲得§f " + exp + " §a經驗值");
		}
	}
		
	public void addMember(Player member)
	{
		this.Members.add(member);
	}
	
	public void addInvited(Player player)
	{
		this.Invited.add(player);
	}
	
	public void removeMember(Player player)
	{
		this.Members.remove(player);
	}
	
	public void removeInvited(Player player)
	{
		this.Invited.remove(player);
	}
	
	public void setExoMode(ExpMode mode)
	{
		this.expMode = mode;
	}
	
	public void setItemMode(ItemMode mode)
	{
		this.itemMode = mode;
	}
	
	public ArrayList<Player> getNearMembers(Player player)
	{
		int range = 30;
		ArrayList<Player> near = new ArrayList<>();
		near.add(player);
		
		for(Player p : Members)
		{
			if(player.getNearbyEntities(range, range, range).contains(p))
			{
				near.add(p);
			}
		}
		return near;
	}
	
	public Player getLeader()
	{
		return this.Leader;
	}
	
	public ArrayList<Player> getMembers()
	{
		return this.Members;
	}
		
	public ArrayList<Player> getInvited()
	{
		return this.Invited;
	}
	
	public ExpMode getExpMode()
	{
		return this.expMode;
	}
	
	public ItemMode getItemMode()
	{
		return this.itemMode;
	}
	
	public int getOrderNumber()
	{
		this.OrderNumber ++;
		
		if(this.OrderNumber == this.Members.size())
		{
			this.OrderNumber = 0;
		}
		
		return this.OrderNumber;
	}
	
	public boolean isLeader(Player player)
	{
		return this.Leader == player;
	}
	
	public boolean isMember(Player target)
	{
		return this.Members.contains(target);
	}
	
	public boolean isInvited(Player target)
	{
		return this.Invited.contains(target);
	}
	
	public enum ExpMode
	{
		SHARE("平均分配"), NO("各自獲得");
		
		private String Name;
		
		ExpMode(String name)
		{
			this.Name = name;
		}
		
		public String getName()
		{
			return this.Name;
		}
	}
	
	public enum ItemMode
	{
		ORDER("順序分配"), RANDOM("隨機分配"), NO("各自獲得");
		
		private String Name;
		
		ItemMode(String name)
		{
			this.Name = name;
		}
		
		public String getName()
		{
			return this.Name;
		}
	}
}
