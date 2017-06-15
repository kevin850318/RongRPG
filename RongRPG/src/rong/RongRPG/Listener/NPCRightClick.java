package rong.RongRPG.Listener;

import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.npc.NPC;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import rong.RongRPG.RpgStorage;
import rong.RongRPG.Data.MerchantData;
import rong.RongRPG.Data.MerchantData.MerchantStatus;
import rong.RongRPG.Util.InventoryUtil;
import rong.RongWarehouse.WarehouseUtil;

public class NPCRightClick implements Listener
{
	@EventHandler
	public void onNPCRightClick(NPCRightClickEvent event)
	{
		NPC npc = event.getNPC();
		Player player = event.getClicker();
		
		if(npc.getEntity().getType() == EntityType.VILLAGER)
		{
			Villager v = (Villager) npc.getEntity();
			
			if(v.getProfession() == Villager.Profession.BLACKSMITH)
			{
				InventoryUtil.openAnvilInventory(player);
				return;
			}
			else if(v.getProfession() == Villager.Profession.BUTCHER)
			{
				WarehouseUtil.openWarehouseMenuPage(player, null);
				return;
			}
		}
		
		if(RpgStorage.NPCMerchantMap.containsKey(npc.getId()))
		{
			MerchantData md = RpgStorage.NPCMerchantMap.get(npc.getId());
			
			if(md.getStatus() == MerchantStatus.OPEN)
			{
				InventoryUtil.openMerchantInventory(player, md.getID());
			}
		}
	}
}
