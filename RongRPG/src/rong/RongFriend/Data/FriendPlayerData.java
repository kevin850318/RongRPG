package rong.RongFriend.Data;

import java.io.File;
import java.util.ArrayList;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import rong.RongRPG.Util.RpgUtil;

public class FriendPlayerData
{
	private String PlayerName;
	private String Title;
	private ArrayList<String> FriendList = new ArrayList<>();
	private ArrayList<String> InviterList = new ArrayList<>();
	private File file;
	private FileConfiguration config;
	
	public FriendPlayerData(String pName)
	{
		this.PlayerName = pName;
		this.file = new File("./plugins/RongRPG/PlayerData/" + pName + "/Friend.yml"); 
		this.config = YamlConfiguration.loadConfiguration(file);
		
		loadFriendData();
	}
	
	public void loadFriendData()
	{
		RpgUtil.debugMessage("- [好友] 讀取中...");
		
		if(file.exists())
		{
			if(config.contains("Friend"))
			{
				for(String friend : config.getConfigurationSection("Friend").getKeys(false))
				{
					FriendList.add(friend);
				}
			}
			
			if(config.contains("Inviter"))
			{
				for(String inviter : config.getConfigurationSection("Inviter").getKeys(false))
				{
					InviterList.add(inviter);
				}
			}
			
			RpgUtil.debugMessage("- [好友] 讀取完成!");
			return;
		}
	}
	
	public void saveFriendData()
	{
		RpgUtil.debugMessage("- [好友] 儲存中...");
		
		for(String friend : FriendList)
		{
			config.createSection("Friend." + friend);
		}
		
		for(String inviter : InviterList)
		{
			config.createSection("Inviter." + inviter);
		}
		
		RpgUtil.saveFileConfig(file, config);
		
		RpgUtil.debugMessage("- 好友數據 儲存成功!");
	}
	
	public void setTitle(String str)
	{
		this.Title = str;
	}
	
	public void addFriend(String target)
	{
		this.FriendList.add(target);
	}
	
	public void addInviter(String target)
	{
		this.InviterList.add(target);
	}
	
	public void removeFriend(String target)
	{
		this.FriendList.remove(target);
	}
	
	public void removeInviter(String target)
	{
		this.InviterList.remove(target);
	}
	
	public String getPlayerName()
	{
		return this.PlayerName;
	}
	
	public String getTitle()
	{
		return this.Title;
	}
	
	public ArrayList<String> getFriendList()
	{
		return this.FriendList;
	}
	
	public ArrayList<String> getInviterList()
	{
		return this.InviterList;
	}
	
	public boolean isFrined(String target)
	{
		return this.FriendList.contains(target);
	}
	
	public boolean isInviter(String target)
	{
		return this.InviterList.contains(target);
	}
}
