package rong.RongRPG.Listener;

import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import rong.RongRPG.RpgStorage;
import rong.RongRPG.Data.PlayerData;
import rong.RongRPG.Util.NMSPacket;
import rong.RongRPG.Util.RpgUtil;

public class PlayerAttack implements Listener
{
	@EventHandler
	public void onPlayerDamage(EntityDamageByEntityEvent event)
	{
		event.setDamage(0);
		
		if(event.getDamager() instanceof Player)
		{
			Player player = (Player) event.getDamager();
			Entity target = event.getEntity();
			PlayerData pData = RpgStorage.PlayerDataMap.get(player.getName());
			ItemStack handItem = player.getInventory().getItemInMainHand();
			int damage = 1;
			int playerCritRate = pData.getInt("CritRate");
			int targetHealth = (int) ((Damageable) target).getHealth();
			int targetAgiRate = 0;
			String message = "§6§l攻擊" + damage;
			
			if(RpgStorage.random.nextInt(101) < targetAgiRate)
			{
				message = "§3§l閃避";
				event.setCancelled(true);
			}
			else
			{
				if(RpgUtil.isRpgItem(handItem) && handItem.getItemMeta().getDisplayName().contains("武器圖示: "))
				{
					float minDamage = (float) (pData.getInt("Damage") * 0.9);
					float maxDamage = (float) (pData.getInt("Damage") * 1.1);
					damage = (int) (Math.random() * (maxDamage - minDamage) + minDamage);
					int fireDamage = (int)((pData.getInt("FireRate") *0.01) *damage);
					int iceDamage = (int)((pData.getInt("IceRate") *0.01) *damage);
					int thunderDamage = (int)((pData.getInt("ThunderRate") *0.01) *damage);
					int windDamage = (int)((pData.getInt("WindRate") *0.01) *damage);

					if(target instanceof Player)
					{
						Player tPlayer = (Player) target;
						PlayerData tData = RpgStorage.PlayerDataMap.get(tPlayer.getName());
						int targetDef = tData.getInt("Defense");
						targetAgiRate = tData.getInt("AgiRate");

						damage -= targetDef;
						
						if(pData.getTeamData() != null)
						{
							if(pData.getTeamData().isMember(tPlayer))
							{
								event.setCancelled(true);
								return;	
							}
						}
					}
					
					message = "§6§l攻擊" + damage;
					
					//玩家爆擊率
					if(playerCritRate > 0)
					{
						if(RpgStorage.random.nextInt(101) <= playerCritRate)
						{
							damage *= 2;
							message = "§d§l攻擊" + damage;
						}
					}
					
					if(fireDamage > 0)
					{
						damage += fireDamage;
						message += " §c§l火" + fireDamage;
					}
					if(iceDamage > 0)
					{
						damage += iceDamage;
						message += " §b§l冰" + iceDamage;
					}
					if(thunderDamage > 0)
					{
						damage += thunderDamage;
						message += " §e§l雷" + thunderDamage;
					}
					if(windDamage > 0)
					{
						damage += windDamage;
						message += " §f§l風" + windDamage;
					}
				}
			}
			
			//Set Target Health
			targetHealth -= damage;
			
			if(targetHealth <= 0)
			{
				targetHealth = 0;
			}
			((Damageable) target).setHealth(targetHealth);
			RpgUtil.updateEntityCustomName(target);
			NMSPacket.sendActionBar(player, "§f造成§f " + message);
		}
	}
}
