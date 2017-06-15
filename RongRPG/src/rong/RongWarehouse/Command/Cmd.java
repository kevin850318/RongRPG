package rong.RongWarehouse.Command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import rong.RongRPG.RpgStorage;
import rong.RongWarehouse.WarehouseUtil;

public class Cmd implements CommandExecutor
{
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lable, String[] args) 
	{
		if(args.length == 0)
		{
			sender.sendMessage("-----" + RpgStorage.SystemTitle + "-----");
			sender.sendMessage("/rwh menu - open warehouse menu");
			sender.sendMessage("/rwh open <value> - open warehouse");
			sender.sendMessage("/rwh upgrade - upgrade warehouse(OP only)");
			return true;
		}
		
		if(sender instanceof Player)
		{
			Player player = (Player) sender;
			
			if(args[0].equalsIgnoreCase("menu"))
			{
				if(args.length == 2 && player.isOp())
				{
					if(RpgStorage.PlayerDataMap.get(args[1]) != null)
					{
						WarehouseUtil.openWarehouseMenuPage(player, args[1]);
						return true;
					}
					
					player.sendMessage(RpgStorage.SystemTitle + "§c目標不存在");
					return true;
				}
				
				WarehouseUtil.openWarehouseMenuPage(player, null);
				return true;
			}
			
			if(args[0].equalsIgnoreCase("open"))
			{
				int i = 0;
				
				if(args.length >= 2)
				{
					i = Integer.parseInt(args[1]);
				}
				
				if(args.length == 3)
				{
					if(RpgStorage.PlayerDataMap.get(args[2]) != null)
					{
						WarehouseUtil.openWarehouse(player, args[2], i);
						return true;
					}
					
					player.sendMessage(RpgStorage.SystemTitle + "§c目標不存在");
					return true;
				}
				
				WarehouseUtil.openWarehouse(player, null, i);
				return true;
			}
			
			if(args[0].equalsIgnoreCase("upgrade") && player.isOp())
			{
				WarehouseUtil.upgradeWarehouse(player);
				return true;
			}
		}
		return false;
	}
}
