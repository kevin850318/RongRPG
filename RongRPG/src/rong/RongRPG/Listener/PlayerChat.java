package rong.RongRPG.Listener;

import net.md_5.bungee.api.chat.TextComponent;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.ItemStack;

import rong.RongRPG.RpgStorage;
import rong.RongRPG.Data.PlayerData;
import rong.RongRPG.Util.NMSPacket;
import rong.RongRPG.Util.RpgUtil;
import rong.RongTeam.Data.TeamData;

public class PlayerChat implements Listener
{
	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent event)
	{
		event.setCancelled(true);
		
		Player player = event.getPlayer();
		PlayerData pData = RpgStorage.PlayerDataMap.get(player.getName());
		String message = event.getMessage();
		
		if(message.substring(0, 1).equals("#"))
		{
			TeamData team = pData.getTeamData();
			
			if(team != null)
			{
				message = message.substring(1, message.length());
				message = player.getName() + ": " + message;
				
				team.sendTeamMessage("§a§l【隊伍頻道】§f" + message);
				RpgUtil.sendServerLog("info", "【隊伍頻道】" + message);
				return;
			}
			
			player.sendMessage(RpgStorage.TeamTitle + "§c你沒有隊伍");
			return;
		}
		else if(message.substring(0, 1).equals("%"))
		{
			return;
		}
		else if(message.substring(0, 1).equals("$"))
		{
			ItemStack hand = player.getInventory().getItemInMainHand();
			
			if(RpgUtil.isRpgItem(hand))
			{
				for(Player other : Bukkit.getOnlinePlayers())
				{
					TextComponent text = new TextComponent("§6§l【世界頻道】§f" + player.getName() + ": ");
					text.addExtra(NMSPacket.ItemConvertTextComponent(hand));
					other.spigot().sendMessage(text);
				}
			}
			return;
		}
		
		player.sendMessage("§8§l【附近】§f" + player.getName() + ": " + message);
		RpgUtil.sendServerLog("info", "【附近】" + player.getName() + ": " + message);
		
		for(Entity other : player.getNearbyEntities(30, 30, 30))
		{
			if(other instanceof Player)
			{
				other.sendMessage("§8§l【附近】§f" + player.getName() + ": " + message);
			}
		}
	}
}
