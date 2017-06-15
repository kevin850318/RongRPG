package rong.RongTeam.Listener;

import java.util.ArrayList;

import net.md_5.bungee.api.chat.TextComponent;

import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;

import rong.RongRPG.RpgStorage;
import rong.RongRPG.Util.NMSPacket;
import rong.RongRPG.Util.RpgUtil;
import rong.RongTeam.Data.TeamData;
import rong.RongTeam.Data.TeamData.ItemMode;

public class TeamPickUpItem implements Listener
{
	@EventHandler
	public void onPlayerPick(PlayerPickupItemEvent event)
	{
		Item item = event.getItem();

		if(RpgUtil.isRpgItem(item.getItemStack()))
		{		
			Player winner = event.getPlayer();
			TeamData team = RpgStorage.PlayerDataMap.get(winner.getName()).getTeamData();

			if(team != null)
			{
				if(team.getItemMode() == ItemMode.RANDOM)
				{
					ArrayList<Player> near = team.getNearMembers(winner);
					int value = 0;
					
					for(Player p : near)
					{
						int i = RpgStorage.random.nextInt(101);
						
						while(i == value)
						{
							i = RpgStorage.random.nextInt(101);

						}
						
						if(i > value)
						{
							value = i;
							winner = p;
						}

						team.sendTeamMessage(RpgStorage.TeamTitle + p.getName() + " 擲出 " + i + " 點");
					}
					
					winner.getInventory().addItem(item.getItemStack());
					event.getItem().remove();
					event.setCancelled(true);
				}
				else if(team.getItemMode() == ItemMode.ORDER)
				{
					winner = team.getMembers().get(team.getOrderNumber());
					
					winner.getInventory().addItem(item.getItemStack());
					event.getItem().remove();
					event.setCancelled(true);
				}
				
				for(Player member : team.getMembers())
				{
					TextComponent text = new TextComponent(RpgStorage.TeamTitle + winner.getName() + " §a獲得§f ");
					TextComponent itemText = NMSPacket.ItemConvertTextComponent(item.getItemStack());
					
					text.addExtra(itemText);
					member.spigot().sendMessage(text);
				}
				
//				ItemStack is = item.getItemStack();
//				ItemMeta im = is.getItemMeta();
//				String iName = im.getDisplayName();
//				String iType = is.getType().toString().toLowerCase();
//				String iLore = "[]";
//				String iEnch = "[]";
//				
//				if(im.hasLore())
//				{
//					iLore = "[";
//					
//					for(String str : im.getLore())
//					{
//						iLore = iLore + str.replace(":", "：").replace(",", "，") + ",";
//					}
//					
//					iLore = iLore.substring(0, iLore.length() - 1) + "]";
//				}
//				
//				if(im.hasEnchants())
//				{
//					iEnch = "[";
//					
//					for(Enchantment ench : im.getEnchants().keySet())
//					{					
//						iEnch = iEnch + "{id:" + ench.getId() + ",lvl:" + im.getEnchants().get(ench).toString() + "},";
//					}
//					
//					iEnch = iEnch.substring(0, iEnch.length() - 1) + "]";
//				}
//				
//				if(iType.startsWith("gold_"))
//				{
//					if(!(iType.equals("gold_ore") && iType.equals("gold_block") && iType.equals("glod_ingot")))
//					{
//						iType = iType.replace("gold_", "golden_");
//					}
//				}
//				
//				for(Player p : team.getMembers())
//				{
//					NMSPacket.sendJson(p, "{\"text\":\"" + RpgStorage.TeamTitle + winner.getName() + " §a獲得§f [\",\"extra\":[{\"text\":\"" + iName + "\",\"hoverEvent\":{\"action\":\"show_item\",\"value\":\"{id:" + iType + ",tag:{display:{Name:" + iName + ",Lore:" + iLore + "},ench:" + iEnch + "}}\"}},{\"text\":\"§f]\"}]}");
//				}
				return;
			}
		}
	}
}
