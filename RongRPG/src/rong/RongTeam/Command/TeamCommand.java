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
				player.sendMessage(RpgStorage.TeamTitle + "��c���O���~! /team invite <���a�W��>");
				return true;
			}
			
			if(args[0].equalsIgnoreCase("accept"))
			{
				if(args.length == 2)
				{
					TeamUtil.acceptTeam(player, args[1]);
					return true;
				}
				player.sendMessage(RpgStorage.TeamTitle + "��c���O���~! /team invite <���a�W��>");
				return true;
			}
			
			if(args[0].equalsIgnoreCase("kick"))
			{
				if(args.length == 2)
				{
					TeamUtil.kickMember(player, args[1]);
					return true;
				}
				player.sendMessage(RpgStorage.TeamTitle + "��c���O���~! /team invite <���a�W��>");
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
					player.sendMessage(RpgStorage.TeamTitle + "��cConfig.yml ��Ū���\");
					return true;
				}
				player.sendMessage(RpgStorage.TeamTitle + "��c�S���v��");
				return true;
			}
		}
		
		player.sendMessage("��a-----��f" + RpgStorage.TeamTitle + "��a-----��f");
		player.sendMessage("��f/team create - �Ыض���");
		player.sendMessage("��f/team leave - �h�X����");
		player.sendMessage("��f/team invite <���a�W��> - �ܽЪ��a�i�J����");
		player.sendMessage("��f/team accept <���a�W��> - �������a���ܽ�");
		player.sendMessage("��f/team kick <���a�W��> - ��X���a");
		player.sendMessage("��f/team info - �d�ݥثe�����T");
		if(player.isOp())
		{
			player.sendMessage("��f/team reload - ��Ū Config.yml");
		}
		player.sendMessage("��a��l-----By Rong-----");
		return false;
	}
}
