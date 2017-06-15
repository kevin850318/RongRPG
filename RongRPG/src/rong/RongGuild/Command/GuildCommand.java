package rong.RongGuild.Command;

import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import rong.RongGuild.GuildStorage;
import rong.RongGuild.GuildUtil;
import rong.RongRPG.RpgStorage;

public class GuildCommand implements CommandExecutor
{
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lable, String[] args) 
	{
		if(sender instanceof Player)
		{
			Player player = (Player) sender;
			
			if(args.length >= 1)
			{
				if(args[0].equalsIgnoreCase("create"))
				{
					if(args.length == 2)
					{
						GuildUtil.createGuild(player, args[1]);
						return true;
					}
					player.sendMessage(GuildStorage.title + "��c�п�J���|�W��");
					return true;
				}
				
				if(args[0].equalsIgnoreCase("add"))
				{
					if(args.length == 2)
					{
						if(Bukkit.getPlayer(args[1])!= null)
						{
							Player target = Bukkit.getPlayer(args[1]);
							
							GuildUtil.addGuildMember(player, target);
							return true;
						}
						player.sendMessage(GuildStorage.title + args[1] + " ��c���b�u�W");
						return true;
					}
					player.sendMessage(GuildStorage.title + "��c�п�J���a�W��");
					return true;
				}
				
				if(args[0].equalsIgnoreCase("accept"))
				{
					if(args.length == 2)
					{
						GuildUtil.acceptGuildInvite(player, args[1]);
						return true;
					}
					player.sendMessage(GuildStorage.title + "��c�п�J���a�W��");
					return true;
				}
				
				if(args[0].equalsIgnoreCase("kick"))
				{
					if(args.length == 2)
					{
						GuildUtil.kickGuildMember(player, args[1]);
						return true;
					}
					player.sendMessage(GuildStorage.title + "��c�п�J���a�W��");
					return true;
				}
				
				if(args[0].equalsIgnoreCase("leave"))
				{
					GuildUtil.leaveGuild(player);
					return true;
				}
				
				if(args[0].equalsIgnoreCase("info"))
				{
					GuildUtil.openGuildInformationPage(player);
					return true;
				}
				
				if(args[0].equalsIgnoreCase("save"))
				{
					if(player.isOp())
					{
						GuildUtil.saveAllGuildData();
						
						player.sendMessage(GuildStorage.title + "��a�x�s���\");
					}
					player.sendMessage(GuildStorage.title + "��c�S���v��");
					return true;
				}
				
				if(args[0].equalsIgnoreCase("reload"))
				{
					if(player.isOp())
					{
						GuildUtil.reloadGuildData();
						
						player.sendMessage(GuildStorage.title + "��a��Ū���\");
					}
					player.sendMessage(GuildStorage.title + "��c�S���v��");
					return true;
				}
			}
		}
		
		if(args[0].equalsIgnoreCase("save"))
		{
			GuildUtil.saveAllGuildData();
			
			RpgStorage.plugin.getLogger().log(Level.INFO, "�x�s���\");
			return true;
		}
		
		if(args[0].equalsIgnoreCase("reload"))
		{
			GuildUtil.reloadGuildData();
			
			RpgStorage.plugin.getLogger().log(Level.INFO, "��Ū���\");
			return true;
		}
		
		RpgStorage.plugin.getLogger().log(Level.INFO, "���O���~");
		return false;
	}
}
