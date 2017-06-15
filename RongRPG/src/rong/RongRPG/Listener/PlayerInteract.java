package rong.RongRPG.Listener;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import com.nisovin.magicspells.Spell;

import rong.RongRPG.RpgStorage;
import rong.RongRPG.Data.CustomItem;
import rong.RongRPG.Data.CustomItem.ItemType;
import rong.RongRPG.Data.PlayerData;
import rong.RongRPG.Data.SkillData;
import rong.RongRPG.Data.SkillPlayerData;
import rong.RongRPG.Data.SkillPlayerData.SkillSlot;
import rong.RongRPG.Util.InventoryUtil;
import rong.RongRPG.Util.RpgUtil;

public class PlayerInteract implements Listener
{
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event)
	{
		Player player = event.getPlayer();
		PlayerData pData = RpgStorage.PlayerDataMap.get(player.getName());
		ItemStack is = event.getItem();
		
		if(is != null)
		{
			if(RpgUtil.isRpgItem(event.getItem()) && event.getItem().getItemMeta().getDisplayName().contains("技能圖示"))
			{
				PlayerInventory inv = player.getInventory();
				
				if(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)
				{
					SkillPlayerData spData = pData.getSkillPlayerData();
					SkillSlot sSlot = SkillSlot.getSkillSlotByHotBar(inv.getHeldItemSlot());
					SkillData sData = spData.getSlotMap().get(sSlot);
					Spell spell = sData.getSpell();
					
					spell.cast(player);
					inv.setHeldItemSlot(0);
					return;
				}
			}

			if(is.getType() == Material.ENCHANTED_BOOK)
			{
				if(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)
				{
					CustomItem ci = CustomItem.getCustomItem(is);
					
					if(ci != null)
					{
						if(ci.getItemType() == ItemType.SKILL_BOOK)
						{
							SkillData sData = RpgStorage.SkillDataMap.get(ci.getItemID());
							
							if(sData != null)
							{
								SkillPlayerData spData = pData.getSkillPlayerData();
								
								if(!spData.getSkillList().contains(sData))
								{
									spData.getSkillList().add(sData);
									player.getInventory().setItemInMainHand(null);
									player.sendMessage(RpgStorage.SystemTitle + "§f習得§f " + sData.getSkillName());
									return;
								}
								
								player.sendMessage(RpgStorage.SystemTitle + sData.getSkillName() + " §c已學習");
								return;
							}
							
							player.sendMessage(RpgStorage.SystemTitle + "§c無此技能, 請通知管理員");
							return;
						}
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onPlayerInteractEntity(PlayerInteractEntityEvent event)
	{
		Player player = event.getPlayer();
		Entity target = event.getRightClicked();
		
		if(player.isSneaking() && target instanceof Player)
		{
			InventoryUtil.openPlayerInteractPage(player, (Player)target);
		}
	}
}
