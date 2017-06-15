package rong.RongWarehouse.Data;

import java.util.HashMap;

import org.bukkit.inventory.ItemStack;

public class WarehouseData 
{
	private int size = 9;
	private HashMap<Integer, ItemStack> itemMap = new HashMap<>();
	
	public WarehouseData(int size)
	{
		this.size = size;
	}
	
	public void upgradeSize()
	{
		if(this.size < 54)
		{
			this.size += 9;
		}
	}
	
	public void addItem(int i, ItemStack is)
	{
		this.itemMap.put(i, is);
	}
	
	public int getSize()
	{
		return this.size;
	}
	
	public HashMap<Integer, ItemStack> getItemMap()
	{
		return this.itemMap;
	}
	
	public ItemStack getItem(int i)
	{
		return this.itemMap.get(i);
	}
}
