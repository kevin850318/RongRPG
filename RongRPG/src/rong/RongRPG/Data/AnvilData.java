package rong.RongRPG.Data;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import rong.RongRPG.RpgStorage;
import rong.RongRPG.Util.RpgUtil;

public class AnvilData 
{
	private Player player;
	private Inventory Inv;
	private EquipmentItem UsedItem;
	private GemItem MixItem;
	private AnvilType Type;
	private AnvilStatus Status = AnvilStatus.NOT_CONFIRM;
	
	public AnvilData(Player player, Inventory inv)
	{
		this.player = player;
		this.Inv = inv;
	}
	
	public void updateAnvilInventory()
	{
		if(this.MixItem != null)
		{
			switch(this.MixItem.getGemType())
			{
			case IDENTIFY:
				this.Type = AnvilType.IDENTIFY;
				break;
			case GRID:
				this.Type = AnvilType.GRID;
				break;
			case UPGRADE:
				this.Type = AnvilType.UPGRADE;
				break;
			}
			
			if(this.UsedItem != null)
			{
				switch(this.Type)
				{
				case IDENTIFY:
					this.Status = this.UsedItem.getQualityType() == null ? AnvilStatus.CONFIRM : AnvilStatus.NOT_CONFIRM;
					break;
				case GRID:
					this.Status = this.UsedItem.canGrinded() ? AnvilStatus.CONFIRM : AnvilStatus.NOT_CONFIRM;
					break;
				case UPGRADE:
					this.Status = AnvilStatus.CONFIRM;
					break;
				}
			}else this.Status = AnvilStatus.NOT_CONFIRM;
		}else this.Status = AnvilStatus.NOT_CONFIRM;
		
		if(this.Status == AnvilStatus.CONFIRM) this.Inv.setItem(8, RpgUtil.setItem(Material.IRON_HOE, 1, 31, "§a確定", null, new ItemFlag[]{ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE}, true));
		else if(this.Status == AnvilStatus.NOT_CONFIRM) this.Inv.setItem(8, RpgUtil.setItem(Material.IRON_HOE, 1, 32, "§c確定", new String[]{"§f請放入§a§l 物品§f 及§a§l 素材"}, new ItemFlag[]{ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE}, true));
	}
	
	public void confirm()
	{
		if(this.Status == AnvilStatus.CONFIRM)
		{
			ItemStack resultItem = this.UsedItem.getItem();
			
			switch(this.Type)
			{
			case IDENTIFY:
				resultItem = this.UsedItem.getIdentifiedItem();
				break;
			case GRID:
				resultItem = this.UsedItem.getGridedItem();
				break;
			case UPGRADE:
				resultItem = this.UsedItem.getUpgradeItem();
				break;
			}
			
			for(int i = 0; i < 9; i ++)
			{
				this.Inv.setItem(i, RpgUtil.setItem(Material.STAINED_GLASS_PANE, 1, 7, " ", null, null, false));
			}
			this.Inv.setItem(4, resultItem);
			
			this.MixItem = null;
			this.UsedItem = new EquipmentItem(resultItem);
			this.Status = AnvilStatus.DONE;
		}
	}
	
	public void closeAnvil()
	{
		if(this.UsedItem != null)
		{
			this.player.getInventory().addItem(this.UsedItem.getItem());
		}
		
		if(this.MixItem != null)
		{
			this.player.getInventory().addItem(this.MixItem.getOriginalItem());
		}
		
		RpgStorage.AnvilDataMap.remove(player.getName());
		this.player.closeInventory();
	}
	
	public void setAnvilInventoryItem(int slot, ItemStack is)
	{
		this.Inv.setItem(slot, is);
	}
	
	public void setUsedItem(EquipmentItem ei)
	{
		this.UsedItem = ei;
	}
	
	public void setMixItem(GemItem gi)
	{
		this.MixItem = gi;
	}
	
	public void setAnvilType(AnvilType type)
	{
		this.Type = type;
	}
	
	public ItemStack getAnvilInventoryItem(int slot)
	{
		return this.Inv.getItem(slot);
	}
	
	public EquipmentItem getUsedItem()
	{
		return this.UsedItem;
	}
	
	public CustomItem getMixItem()
	{
		return this.MixItem;
	}
	
	public AnvilType getAnvilType()
	{
		return this.Type;
	}
	
	public AnvilStatus getAnvilStatus()
	{
		return this.Status;
	}
	
	public enum AnvilType
	{
		IDENTIFY, GRID, UPGRADE;
	}
	
	public enum AnvilStatus
	{
		NOT_CONFIRM, CONFIRM, DONE;
	}
}
