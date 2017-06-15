package rong.RongRPG.Data;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.inventory.ItemStack;

import rong.RongRPG.RpgStorage;

public class DropData 
{
	private List<String> ItemList = new ArrayList<>();
	
	public DropData()
	{
		
	}
	
	public void addItem(String itemStr)
	{
		this.ItemList.add(itemStr);
	}
	
	public List<ItemStack> getItemList()
	{
		List<ItemStack> itemList = new ArrayList<>();
		
		for(String str : this.ItemList)
		{
			CustomItem ci = RpgStorage.CustomItemMap.get(str.split(" ")[0]);
			float rate = Float.valueOf(str.split(" ")[1]);
			
			if(ci != null)
			{
				if(rate >= RpgStorage.random.nextFloat())
				{
					itemList.add(ci.getOriginalItem());
				}
			}
		}
		
		return itemList;
	}
}
