package rong.RongRPG.Command;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import rong.RongRPG.RpgStorage;
import rong.RongRPG.Data.MerchantData;
import rong.RongRPG.Util.InventoryUtil;

public class MerchantCommand implements CommandExecutor
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
					if(player.isOp())
					{
						if(args.length == 2)
						{
							int id = RpgStorage.random.nextInt(1001);
							String name = args[1];
							
							while(RpgStorage.MerchantDataMap.containsKey(id))
							{
								id = RpgStorage.random.nextInt(1001);
							}

							MerchantData md = new MerchantData(id, name);
							
							RpgStorage.MerchantDataMap.put(id, md);
							InventoryUtil.openMerchantInventory(player, id);
							player.sendMessage(RpgStorage.SystemTitle + name + " §a商店建立§f, ID: " + id);
							return true;
						}
						
						player.sendMessage(RpgStorage.SystemTitle + "§c指令: /Merchant create <商店 名稱>");
						return false;
					}
					
					player.sendMessage(RpgStorage.SystemTitle + "§c沒有權限!");
					return false;
				}
				else if(args[0].equalsIgnoreCase("open"))
				{
					if(args.length == 2)
					{
						int id = Integer.parseInt(args[1]);
						
						if(RpgStorage.MerchantDataMap.containsKey(id))
						{
							InventoryUtil.openMerchantInventory(player, id);
							return true;
						}
						
						player.sendMessage(RpgStorage.SystemTitle + "§c沒有此商店ID!");
						return false;
					}
					
					player.sendMessage(RpgStorage.SystemTitle + "§c指令: /Merchant open <商店 ID>");
					return false;
				}
				else if(args[0].equalsIgnoreCase("list"))
				{
					if(player.isOp())
					{
						player.sendMessage("§f-----[商店]-----");
						
						for(MerchantData md : RpgStorage.MerchantDataMap.values())
						{
							player.sendMessage("ID: " + md.getID() + " Name: " + md.getName());
						}
						return true;
					}
				}
				else if(args[0].equalsIgnoreCase("set"))
				{
					if(args.length == 3)
					{
						NPC npc = CitizensAPI.getNPCRegistry().getById(Integer.parseInt(args[1]));
						
						if(npc != null)
						{
							MerchantData md = RpgStorage.MerchantDataMap.get(Integer.parseInt(args[2]));
							
							if(md != null)
							{
								RpgStorage.NPCMerchantMap.put(npc.getId(), md);
								player.sendMessage(RpgStorage.SystemTitle + npc.getName() + " §a設置§f " + md.getName() + " §a商店");
								return true;
							}
							
							player.sendMessage(RpgStorage.SystemTitle + "§c沒有此商店ID!");
							return false;
						}
						
						player.sendMessage(RpgStorage.SystemTitle + "§c沒有此 NPC ID!");
						return false;
					}
					
					player.sendMessage(RpgStorage.SystemTitle + "§c指令: /Merchant set <NPC-ID> <商店 ID>");
					return false;
				}
				else if(args[0].equalsIgnoreCase("rename"))
				{
					if(args.length == 3)
					{
						MerchantData md = RpgStorage.MerchantDataMap.get(Integer.parseInt(args[1]));
						
						if(md != null)
						{
							md.setName(args[2]);
							player.sendMessage(RpgStorage.SystemTitle + "§a商店§f " + md.getID() + " §a名稱更改為§f " + md.getName());
							return true;
						}
						
						player.sendMessage(RpgStorage.SystemTitle + "§c沒有此商店ID!");
						return false;
					}
					
					player.sendMessage(RpgStorage.SystemTitle + "§c指令: /Merchant rename <商店 ID> <Name>");
					return false;
				}
				else if(args[0].equalsIgnoreCase("remove"))
				{
					if(args.length == 2)
					{
						int npcID = Integer.parseInt(args[1]);
						
						if(RpgStorage.NPCMerchantMap.containsKey(npcID))
						{
							RpgStorage.NPCMerchantMap.remove(npcID);
							return true;
						}
						
						player.sendMessage(RpgStorage.SystemTitle + "§c沒有此 NPC ID!");
						return false;
					}
					
					player.sendMessage(RpgStorage.SystemTitle + "§c指令: /Merchant remove <NPC-ID>");
					return false;
				}
			}
		}
		return false;
	}
}
