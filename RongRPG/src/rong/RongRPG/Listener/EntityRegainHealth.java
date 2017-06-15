package rong.RongRPG.Listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;

import rong.RongRPG.Util.RpgUtil;

public class EntityRegainHealth implements Listener
{
	@EventHandler
	public void onEntityRegainHealth(EntityRegainHealthEvent event)
	{
		RegainReason reason = event.getRegainReason();
		
		if(reason == RegainReason.SATIATED)
		{
			event.setCancelled(true);
			return;
		}
		
		RpgUtil.updateEntityCustomName(event.getEntity());
	}
}
