package rong.RongGuild.Listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import rong.RongGuild.GuildStorage;
import rong.RongGuild.GuildUtil;
import rong.RongGuild.Data.GuildData;
import rong.RongGuild.Data.GuildPlayerData;

public class PlayerChatEvent implements Listener
{
	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent event)
	{
		Player player = event.getPlayer();
		String message = event.getMessage();
		
		if(message.substring(0, 1).equals("!"))
		{			
			if(GuildStorage.PlayerGuildMap.containsKey(player.getName()))
			{
				String guildName = GuildUtil.getPlayerGuild(player.getName());
				
				if(GuildUtil.getGuildData(guildName) != null)
				{
					GuildData guild = GuildUtil.getGuildData(guildName);
					
					if(guild.getMemberList().containsKey(player.getName()))
					{
						event.setCancelled(true);
						
						GuildPlayerData gpd = guild.getMemberData(guildName);
						message = event.getMessage().substring(1, message.length());

						guild.sendGuildMessage("¡±a¡±l¡i¶¤¥îÀW¹D¡j¡±f" + "¡±f¡i¡±f" + guild.getGuildRank(gpd.getRank()).getRankTitle() + "¡±f¡j¡±f" + player.getName() + " : " + message);
						return;
					}
				}
			}
		}
		
		if(message.substring(0, 1).equals(">"))
		{			
			if(GuildStorage.PlayerGuildMap.containsKey(player.getName()))
			{
				String guildName = GuildUtil.getPlayerGuild(player.getName());
				
				if(GuildUtil.getGuildData(guildName) != null)
				{
					GuildData guild = GuildUtil.getGuildData(guildName);
					
					if(guild.getMemberList().containsKey(player.getName()))
					{
						GuildPlayerData gpd = guild.getMemberData(player.getName());
						
						if(gpd.isChangeRankTitle())
						{
							if(GuildUtil.getGuildData(gpd.getGuildName()) != null)
							{
								event.setCancelled(true);
								
								message = event.getMessage().substring(1, 5);

								guild.getGuildRank(gpd.getChangingRank()).setRankTitle(message);
								gpd.setChangeRankTitle(false);
								return;
							}
						}
					}
				}
			}
		}
	}
}
