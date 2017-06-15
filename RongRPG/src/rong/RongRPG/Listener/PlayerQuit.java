package rong.RongRPG.Listener;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import rong.RongFriend.Data.FriendPlayerData;
import rong.RongRPG.RpgStorage;
import rong.RongRPG.Data.PlayerData;

public class PlayerQuit implements Listener
{
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event)
	{		
		event.setQuitMessage(null);
		
		Player player = event.getPlayer();
		PlayerData pData = RpgStorage.PlayerDataMap.get(player.getName());
		
		if(pData != null)
		{
			pData.saveData();
			pData.setPlayer(null);
			
			FriendQuit(pData);
		}
	}
	
	void FriendQuit(PlayerData pData)
	{
		FriendPlayerData fpData = pData.getFriendPlayerData();
		
		for(String friend : fpData.getFriendList())
		{
			Player fPlayer = Bukkit.getPlayer(friend);
			
			if(fPlayer != null)
			{
				fPlayer.sendMessage(RpgStorage.FriendTitle + pData.getPlayerName() + " ¡±cÂ÷¶}¹CÀ¸");
			}
		}
	}
}
