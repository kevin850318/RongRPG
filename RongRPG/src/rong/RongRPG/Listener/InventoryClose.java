package rong.RongRPG.Listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

import rong.RongRPG.RpgStorage;
import rong.RongRPG.Data.AnvilData;
import rong.RongRPG.Data.PlayerData;
import rong.RongRPG.Data.PlayerInventoryType;
import rong.RongRPG.Data.SkillPlayerData;

public class InventoryClose implements Listener
{
	@EventHandler
	public void onInventoryClose(InventoryCloseEvent event)
	{
		Player player = (Player) event.getPlayer();
		PlayerData pData = RpgStorage.PlayerDataMap.get(player.getName());
		
		if(pData.getOpenedInventoryType() == PlayerInventoryType.INFORMATION)
		{
			pData.UpdateData();
		}
		else if(pData.getOpenedInventoryType() == PlayerInventoryType.SKILL)
		{
			SkillPlayerData spData = pData.getSkillPlayerData();
			
			spData.setChoosedSkillSlot(null);
			pData.UpdateData();
		}
		else if(pData.getOpenedInventoryType() == PlayerInventoryType.ANVIL)
		{
			AnvilData ad = RpgStorage.AnvilDataMap.get(player.getName());
			
			if(ad != null)
			{
				ad.closeAnvil();
			}
		}
		else if(pData.getOpenedInventoryType() == PlayerInventoryType.MERCHANT)
		{
			RpgStorage.PlayerMerchantMap.remove(player.getName());
		}
		
		pData.setOpenedInventoryType(null);
		pData.setInteractPlayerName(null);
	}
}
