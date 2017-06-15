package rong.RongRPG.Listener;

import net.elseland.xikage.MythicMobs.API.Bukkit.BukkitMobsAPI;
import net.elseland.xikage.MythicMobs.Mobs.ActiveMob;

import org.bukkit.entity.Animals;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;

import rong.RongRPG.RpgStorage;
import rong.RongRPG.Data.PlayerData;
import rong.RongRPG.Util.RpgUtil;

public class EntityAttack implements Listener
{
	BukkitMobsAPI mmobApi = new BukkitMobsAPI();

	@EventHandler
	public void onLivingEntityDamage(EntityDamageByEntityEvent event)
	{
		Entity attacker = event.getDamager();
		
		if(attacker instanceof Arrow)
		{
			attacker = (Entity) ((Arrow)attacker).getShooter();
		}
		
		if(attacker instanceof Monster || attacker instanceof Animals)
		{
			if(event.getEntity() instanceof Player)
			{
				Player player = (Player) event.getEntity();
				PlayerData pData = RpgStorage.PlayerDataMap.get(player.getName());
				int playerHealth = (int) player.getHealth();
				int playerDef = pData.getInt("Defense");
				int playerAgiRate = pData.getInt("AgiRate");

				if(mmobApi.isMythicMob(attacker))
				{
					ActiveMob mob = mmobApi.getMythicMobInstance(attacker);
					int damage = (int) mob.getDamage();
					
					damage -= playerDef;
					if(damage <= 0)
					{
						damage = 1;
					}
					
					String message = "§6§l" + damage;
					
					if(playerAgiRate > 0)
					{
						if(RpgStorage.random.nextInt(101) <= playerAgiRate)
						{
							damage = 0;
							message = "§3§l閃避";
							event.setCancelled(true);
						}
					}
					
					playerHealth -= damage;
					if(playerHealth <= 0)
					{
						playerHealth = 0;
					}
					player.setHealth(playerHealth);
					
					Hologram h = HologramsAPI.createHologram(RpgStorage.plugin, player.getLocation().add(0, 3.2, 0)); 
					h.appendTextLine(message);
					RpgUtil.showHoloMessage(h);
					return;
				}
			}
		}
	}
}
