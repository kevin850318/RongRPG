package rong.RongRPG.Command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import rong.RongRPG.RpgStorage;
import rong.RongRPG.Data.CustomItem;
import rong.RongRPG.Data.PlayerData;
import rong.RongRPG.Util.InventoryUtil;

public class Cmd implements CommandExecutor
{
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) 
	{
		if(sender instanceof Player)
		{
			Player player = (Player) sender;
			
			if(args.length >= 1)
			{
				if(args[0].equalsIgnoreCase("ShowItem"))
				{
					if(args.length == 2)
					{
						CustomItem ci = RpgStorage.CustomItemMap.get(args[1]);
						
						if(ci != null)
						{
							player.sendMessage(RpgStorage.SystemTitle + "ItemStack: " + ci.getOriginalItem());
							player.sendMessage(RpgStorage.SystemTitle + "ItemID: " + ci.getItemID());
							player.sendMessage(RpgStorage.SystemTitle + "ItemType: " + ci.getItemType());
							player.sendMessage(RpgStorage.SystemTitle + "Coin: " + ci.getCoin());
						}
					}
					
					return true;
				}
				
				if(args[0].equalsIgnoreCase("Talent"))
				{
					if(args.length >= 2)
					{
						PlayerData pData = RpgStorage.PlayerDataMap.get(player.getName());
						
						if(args[1].equalsIgnoreCase("add"))
						{
						}
						
						if(player.isOp())
						{
							if(args[1].equalsIgnoreCase("point"))
							{
								int i = 1;

								if(args.length == 3)
								{
									try
									{
										i = Integer.parseInt(args[2]);
										int value = pData.getInt("TalentPoint") + i;
										
										pData.setInt("TalentPoint", value);
										player.sendMessage(RpgStorage.SystemTitle + "§f屬性點 + " + i + " 點");
									}
									catch (Exception e)
									{
										player.sendMessage(RpgStorage.SystemTitle + "§c請輸入數字");
									}
								}
							}
						}
					}
					return true;
				}

				if(args[0].equalsIgnoreCase("reload") && player.isOp())
				{
					RpgStorage.plugin.reloadConfig();
					RpgStorage.Config.loadConfig();
					
					player.sendMessage(RpgStorage.SystemTitle + "§aConfig 重讀成功");
					return true;
				}

				if(args[0].equalsIgnoreCase("ItemList"))
				{
					InventoryUtil.openCustomItemPage(player, 0);
					return true;
				}
				
				if(args[0].equalsIgnoreCase("Anvil"))
				{
					InventoryUtil.openAnvilInventory(player);
					return true;
				}
			}
		}
		return false;
	}
}
