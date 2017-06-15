package rong.RongRPG.Listener;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import rong.RongFriend.Data.FriendPlayerData;
import rong.RongRPG.RpgStorage;
import rong.RongRPG.Data.PlayerData;

public class PlayerJoin implements Listener
{
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerJoin(PlayerJoinEvent event)
	{		
		event.setJoinMessage(null);
		
		Player player = event.getPlayer();
		PlayerData pData = RpgStorage.PlayerDataMap.get(player.getName());
		
		if(pData != null)
		{
			pData.setPlayer(player);
			pData.UpdateData();
			
			FriendJoin(pData);
		}
		else
		{
			pData = new PlayerData(player.getName());
			
			RpgStorage.PlayerDataMap.put(player.getName(), pData);
			
			pData.setPlayer(player);
			pData.UpdateData();
		}
	}
	
	void FriendJoin(PlayerData pData)
	{
		FriendPlayerData fpData = pData.getFriendPlayerData();
		
		for(String friend : fpData.getFriendList())
		{
			Player fPlayer = Bukkit.getPlayer(friend);
			
			if(fPlayer != null)
			{
				fPlayer.sendMessage(RpgStorage.FriendTitle + pData.getPlayerName() + " ¡±a¶i¤J¹CÀ¸");
			}
		}
	}
}