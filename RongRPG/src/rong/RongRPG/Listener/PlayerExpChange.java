package rong.RongRPG.Listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerExpChangeEvent;

public class PlayerExpChange implements Listener
{
	@EventHandler
	public void onPlayerExpChange(PlayerExpChangeEvent event)
	{
		event.setAmount(0);
	}
}
