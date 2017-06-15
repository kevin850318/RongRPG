package rong.RongRPG.Listener;

import net.elseland.xikage.MythicMobs.API.Bukkit.Events.MythicMobDeathEvent;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import rong.RongRPG.RpgStorage;
import rong.RongRPG.Data.PlayerData;
import rong.RongRPG.Util.NMSPacket;

public class MythicMobsDeath implements Listener
{
	@EventHandler
	public void onMMobsDeath(MythicMobDeathEvent event)
	{
		LivingEntity attacker = event.getKiller();
		int exp = (int) (event.getExp() * RpgStorage.ExpAddition);
		event.setExp(0);
		
		if(attacker instanceof Player)
		{
			Player player = (Player) attacker;
			PlayerData pData = RpgStorage.PlayerDataMap.get(attacker.getName());
						
			if(pData.getTeamData() == null)
			{
				pData.giveExp(exp);
			}
			else
			{
				pData.getTeamData().giveTeamExp(player, exp);
			}
			
			NMSPacket.sendActionBar(player, "§f獲得§a§l 經驗值 " + exp);
		}
	}
}
