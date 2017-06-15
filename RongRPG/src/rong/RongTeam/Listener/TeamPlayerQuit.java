package rong.RongTeam.Listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import rong.RongRPG.RpgStorage;
import rong.RongTeam.TeamUtil;

public class TeamPlayerQuit implements Listener
{
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event)
	{
		if(RpgStorage.PlayerDataMap.get(event.getPlayer().getName()).getTeamData() != null)
		{
			TeamUtil.leaveTeam(event.getPlayer());
		}
	}
}
