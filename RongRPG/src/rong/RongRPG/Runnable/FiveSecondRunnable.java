package rong.RongRPG.Runnable;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import rong.RongRPG.RpgStorage;
import rong.RongRPG.Data.PlayerData;
import rong.RongRPG.Util.RpgUtil;

public class FiveSecondRunnable implements Runnable
{
	@Override
	public void run() 
	{
		PlayerData pData;
		
		for(Player player : Bukkit.getOnlinePlayers())
		{
			pData = RpgStorage.PlayerDataMap.get(player.getName());
			
			if(pData != null)
			{
				int health = (int) player.getHealth();
				int maxHealth = (int) player.getMaxHealth();
				int regainHealth = pData.getInt("RegainHealth");
				int mana = pData.getInt("Mana");
				int maxMana = pData.getInt("MaxMana");
				int regainMana = pData.getInt("RegainMana");
				
				if(health < maxHealth)
				{
					health += regainHealth;
					
					if(health > maxHealth)
					{
						health = maxHealth;
					}
					
					player.setHealth(health);
					RpgUtil.updateEntityCustomName(player);
				}
				
				if(mana < maxMana)
				{
					mana += regainMana;
					
					if(mana > maxMana)
					{
						mana = maxMana;
					}
					
					pData.setInt("Mana", mana);
				}
			}
		}
	}
}
