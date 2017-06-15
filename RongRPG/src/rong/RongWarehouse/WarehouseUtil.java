package rong.RongWarehouse;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import rong.RongRPG.RpgStorage;
import rong.RongRPG.Util.RpgUtil;
import rong.RongWarehouse.Data.WarehouseData;
import rong.RongWarehouse.Data.WarehousePlayerData;

public class WarehouseUtil 
{
	public static void openWarehouseMenuPage(Player player, String target)
	{
		WarehousePlayerData data = RpgStorage.PlayerDataMap.get(player.getName()).getWarehousePlayerData();
		String pName = player.getName();
		
		if(target != null)
		{
			data = RpgStorage.PlayerDataMap.get(target).getWarehousePlayerData();
			pName = target;
		}
		
		Inventory inv = Bukkit.createInventory(player, 9, "�ܮw�M�� " + pName);
		
		for(int i = 0; i < data.getWarehouseDataList().size(); i ++)
		{
			inv.setItem(i, RpgUtil.setItem(Material.CHEST, 1, 0,  "��f" + (i + 1) + " ���ܮw", null, null, false));
		}
		
		player.openInventory(inv);
	}
	
	public static void openWarehouse(Player player, String target, int i)
	{
		WarehousePlayerData data = RpgStorage.PlayerDataMap.get(player.getName()).getWarehousePlayerData();
		String pName = player.getName();
		
		if(target != null)
		{
			data = RpgStorage.PlayerDataMap.get(target).getWarehousePlayerData();
			pName = target;
		}
		
		if(data.getWarehouseData(i) != null)
		{
			WarehouseData whData = data.getWarehouseData(i);
			Inventory inv = Bukkit.createInventory(player, whData.getSize(), (i + 1) + " ���ܮw" + pName);
			
			for(int slot : whData.getItemMap().keySet())
			{
				inv.setItem(slot, whData.getItem(slot));
			}
			
			player.openInventory(inv);
			return;
		}
		
		player.sendMessage(RpgStorage.SystemTitle + "��c���֦� " + (i + 1) + " ���ܮw");
	}

	public static void upgradeWarehouse(Player player)
	{
		WarehousePlayerData pData = RpgStorage.PlayerDataMap.get(player.getName()).getWarehousePlayerData();
		
		for(int i = 0; i < pData.getWarehouseDataList().size(); i ++)
		{
			WarehouseData whData = pData.getWarehouseData(i);
			
			if(whData.getSize() == 54)
			{
				if(pData.getWarehouseData(i + 1) == null)
				{
					pData.addWarehouse(new WarehouseData(9));
					return;
				}
			}
			
			whData.upgradeSize();
		}
		
		player.sendMessage(RpgStorage.SystemTitle + "��a�X�R���\");
	}
}
