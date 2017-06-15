package rong.RongFriend.Command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import rong.RongFriend.FriendUtil;
import rong.RongRPG.RpgStorage;

public class FriendCommand implements CommandExecutor
{
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lable, String[] args) 
	{
		if(sender instanceof Player)
		{
			Player player = (Player) sender;
			
			if(args.length >= 1)
			{
				if(args[0].equalsIgnoreCase("add"))
				{
					if(args.length == 2)
					{						
						FriendUtil.addFriend(player, args[1]);
						return true;
					}
					player.sendMessage(RpgStorage.FriendTitle + "§c指令: /friend add <PlayerName>");
					return false;
				}
				
				if(args[0].equalsIgnoreCase("accept"))
				{
					if(args.length == 2)
					{
						FriendUtil.acceptFriend(player, args[1]);
						return true;
					}
					player.sendMessage(RpgStorage.FriendTitle + "§c指令: /friend accept <PlayerName>");
					return false;
				}
				
				if(args[0].equalsIgnoreCase("deny"))
				{
					if(args.length == 2)
					{
						FriendUtil.denyFriend(player, args[1]);
						return true;
					}
					player.sendMessage(RpgStorage.FriendTitle + "§c指令: /friend deny <PlayerName>");
					return false;
				}
				
				if(args[0].equalsIgnoreCase("remove"))
				{
					if(args.length == 2)
					{
						FriendUtil.removeFriend(player, args[1]);
						return true;
					}
					player.sendMessage(RpgStorage.FriendTitle + "§c指令: /friend remove <PlayerName>");
					return false;
				}
				
				if(args[0].equalsIgnoreCase("list"))
				{
					FriendUtil.openFriendPage(player);
					return true;
				}
			}
		}
		return false;
	}
}
