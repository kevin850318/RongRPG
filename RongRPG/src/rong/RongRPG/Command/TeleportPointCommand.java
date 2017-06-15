package rong.RongRPG.Command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import rong.RongRPG.RpgStorage;
import rong.RongRPG.Data.TeleportData;

public class TeleportPointCommand implements CommandExecutor
{
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) 
	{
		if(sender instanceof Player)
		{
			Player player = (Player) sender;
			
			if(args.length >= 1)
			{
				if(args[0].equalsIgnoreCase("Create"))
				{
					if(args.length == 2)
					{
						String id = args[1];
						
						if(!RpgStorage.TeleporDatatMap.containsKey(id))
						{
							TeleportData td = new TeleportData(id);
							RpgStorage.TeleporDatatMap.put(id, td);
							player.sendMessage(RpgStorage.SystemTitle + "§a建立§f " + id + " §a傳送點");
							return true;
						}
						
						player.sendMessage(RpgStorage.SystemTitle + "§cID 重複");
						return false;
					}
					
					player.sendMessage(RpgStorage.SystemTitle + "§c指令: /TeleportPoint create <ID>");
					return false;
				}
				
				if(args[0].equalsIgnoreCase("Set"))
				{
					if(args.length == 3)
					{
						String id = args[1];
						TeleportData td = RpgStorage.TeleporDatatMap.get(id);
						
						if(td != null)
						{
							switch(args[2])
							{
							case "point1":
								td.setPoint1Location(player.getLocation().getBlock().getLocation());
								player.sendMessage(RpgStorage.SystemTitle + id + "§aPoint 1  設置成功!");
								return true;
							case "point2":
								td.setPoint2Location(player.getLocation().getBlock().getLocation());
								player.sendMessage(RpgStorage.SystemTitle + id + "§aPoint 2  設置成功!");
								return true;
							}
							
							player.sendMessage(RpgStorage.SystemTitle + "§c指令: /rrpg TeleportPoint set <ID> <point1/point2>");
							return false;
						}
						
						player.sendMessage(RpgStorage.SystemTitle + "§c沒有此傳送點 ID");
						return false;
					}
					
					player.sendMessage(RpgStorage.SystemTitle + "§c指令: /rrpg TeleportPoint set <ID> <point1/point2>");
					return false;
				}
				
				if(args[0].equalsIgnoreCase("List"))
				{
					
				}
				
				if(args[0].equalsIgnoreCase("tp"))
				{
					if(args.length == 3)
					{
						String id = args[1];
						TeleportData td = RpgStorage.TeleporDatatMap.get(id);

						if(td != null)
						{
							switch(args[2])
							{
							case "point1":
								if(td.getPoint1Location() != null)
								{
									player.teleport(td.getPoint1Location());
									return true;
								}
								break;
							case "point2":
								if(td.getPoint2Location() != null)
								{
									player.teleport(td.getPoint2Location());
									return true;
								}
								break;
							}
						}
					}
				}
			}
		}
		return false;
	}
}
