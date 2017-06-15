package rong.RongRPG.Listener;

import net.elseland.xikage.MythicMobs.API.Bukkit.BukkitMobsAPI;
import net.elseland.xikage.MythicMobs.Mobs.ActiveMob;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import rong.RongRPG.RpgStorage;
import rong.RongRPG.Adapters.SkillDamageEvent;
import rong.RongRPG.Data.PlayerData;
import rong.RongRPG.Util.NMSPacket;
import rong.RongRPG.Util.RpgUtil;

public class SkillDamage implements Listener
{
	BukkitMobsAPI mmobsAPI = new BukkitMobsAPI();
	
	@EventHandler
	public void onSkillDamage(SkillDamageEvent event)
	{
		Player player = event.getPlayer();
		PlayerData pData = RpgStorage.PlayerDataMap.get(player.getName());
		LivingEntity target = event.getTarget();
		int playerCritRate = pData.getInt("CritRate");
		int targetHealth = (int) target.getHealth();
		int damage = event.getDamage();
		int fireDamage = event.getFireDamage();
		int iceDamage = event.getIceDamage();
		int thunderDamage = event.getThunderDamage();
		int windDamage = event.getWindDamage();
		String message = "§6§l攻擊" + damage;

		if(target instanceof Player)
		{
			PlayerData tData = RpgStorage.PlayerDataMap.get(target.getName());
			int targetDef = tData.getInt("Defense");
			damage -= targetDef;
		}
		
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
		
		targetHealth -= damage;
		if(targetHealth <= 0)
		{
			targetHealth = 0;
			if(RpgStorage.mm.getAPI().getMobAPI().isMythicMob(target))
			{
				ActiveMob am = RpgStorage.mm.getAPI().getMobAPI().getMythicMobInstance(target);
				am.setDead();
				target.damage(0, player);
			}
		}
		target.setHealth(targetHealth);
		RpgUtil.updateEntityCustomName(target);
		NMSPacket.sendActionBar(player, "§f造成§f " + message);
	}
}