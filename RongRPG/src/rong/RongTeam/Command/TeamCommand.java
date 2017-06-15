package rong.RongTeam.Command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import rong.RongRPG.RpgStorage;
import rong.RongTeam.TeamUtil;

public class TeamCommand implements CommandExecutor
{
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) 
	{
		Player player = (Player) sender;
		
		if(args.length > 0)
		{
			if(args[0].equalsIgnoreCase("create"))
			{
				TeamUtil.createTeam(player);
				return true;
			}
			
			if(args[0].equalsIgnoreCase("invite"))
			{
				if(args.length == 2)
				{
					TeamUtil.invitePlayer(player, args[1]);
					return true;
				}
				player.sendMessage(RpgStorage.TeamTitle + "§c指令錯誤! /team invite <玩家名稱>");
				return true;
			}
			
			if(args[0].equalsIgnoreCase("accept"))
			{
				if(args.length == 2)
				{
					TeamUtil.acceptTeam(player, args[1]);
					return true;
				}
				player.sendMessage(RpgStorage.TeamTitle + "§c指令錯誤! /team invite <玩家名稱>");
				return true;
			}
			
			if(args[0].equalsIgnoreCase("kick"))
			{
				if(args.length == 2)
				{
					TeamUtil.kickMember(player, args[1]);
					return true;
				}
				player.sendMessage(RpgStorage.TeamTitle + "§c指令錯誤! /team invite <玩家名稱>");
				return true;
			}
			
			if(args[0].equalsIgnoreCase("leave"))
			{
				TeamUtil.leaveTeam(player);
				return true;
			}
			
			if(args[0].equalsIgnoreCase("info"))
			{
				TeamUtil.openTeamInformationPage(player);
				return true;
			}
			
			if(args[0].equalsIgnoreCase("reload"))
			{
				if(player.isOp())
				{
//					TeamStorage.team.reloadConfig();
//					TeamStorage.team.loadConfig();
					player.sendMessage(RpgStorage.TeamTitle + "§cConfig.yml 重讀成功");
					return true;
				}
				player.sendMessage(RpgStorage.TeamTitle + "§c沒有權限");
				return true;
			}
		}
		
		player.sendMessage("§a-----§f" + RpgStorage.TeamTitle + "§a-----§f");
		player.sendMessage("§f/team create - 創建隊伍");
		player.sendMessage("§f/team leave - 退出隊伍");
		player.sendMessage("§f/team invite <玩家名稱> - 邀請玩家進入隊伍");
		player.sendMessage("§f/team accept <玩家名稱> - 接受玩家的邀請");
		player.sendMessage("§f/team kick <玩家名稱> - 踢出玩家");
		player.sendMessage("§f/team info - 查看目前隊伍資訊");
		if(player.isOp())
		{
			player.sendMessage("§f/team reload - 重讀 Config.yml");
		}
		player.sendMessage("§a§l-----By Rong-----");
		return false;
	}
}
