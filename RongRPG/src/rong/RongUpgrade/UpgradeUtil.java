package rong.RongUpgrade;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;

import rong.RongRPG.RpgStorage;
import rong.RongRPG.Util.RpgUtil;

public class UpgradeUtil 
{
	public static void openUpgradePage(Player player)
	{
		Inventory inv = Bukkit.createInventory(player, 9, "��8�j�Ƥ���");
		
		for(int i = 0; i < inv.getSize(); i++)
		{
			inv.setItem(i, RpgUtil.setItem(Material.STAINED_GLASS_PANE, 1, (short)15, "", null, null, false));
		}
		
		inv.setItem(0, RpgUtil.setItem(Material.DIAMOND_CHESTPLATE, 1, (short)0, "��f�˳�", new String[]{"��f���I�����j�Ƥ��˳�"}, null, false));
		inv.setItem(3, RpgUtil.setItem(Material.NETHER_STAR, 1, (short)0, "��f�j�ƥ�", new String[]{"��f���I���j�ƥ�"}, null, false));
		inv.setItem(8, RpgUtil.setItem(Material.WOOL, 1, (short)14, "��f�T�w", new String[]{"��a[Shift-����]: �T�w�j��"}, null, false));
		
		player.openInventory(inv);
	}
	
	public static void updateUpgradePage(Inventory inv)
	{
		if(inv.getItem(0).getType() != Material.AIR && inv.getItem(3).getType() != Material.AIR)
		{
			inv.setItem(8, RpgUtil.setItem(Material.WOOL, 1, (short)5, "��f�T�w", new String[]{"��a[Shift-����]: �T�w�j��"}, null, false));
		}
		else
		{
			inv.setItem(8, RpgUtil.setItem(Material.WOOL, 1, (short)14, "��f�T�w", new String[]{"��a[Shift-����]: �T�w�j��"}, null, false));
		}
	}
	
	public static void upgrade(Player player)
	{
		final Inventory inv = Bukkit.createInventory(player, 9, "��f�j�Ƥ�-�еy��");
		
		new BukkitRunnable()
		{
			int slot = 0;

			@Override
			public void run() 
			{
				if(slot == 9)
				{
					this.cancel();
					return;
				}
				
				inv.setItem(slot, RpgUtil.setItem(Material.STAINED_GLASS_PANE, 1, 5, "", null, null, false));
				
				slot ++;
			}
		}.runTaskTimer(RpgStorage.plugin, 0, 6);
		
		player.openInventory(inv);
	}
}
