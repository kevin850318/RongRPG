package rong.RongRPG.Data;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.inventory.ItemStack;

import rong.RongRPG.RpgStorage;

public class LotteryData 
{
	private String ID;
	private List<String> ItemList = new ArrayList<>();
	
	public LotteryData(String id)
	{
		this.ID = id;
	}
	
	public void setItemList(String item)
	{
		this.ItemList.add(item);
	}
	
	public String getID()
	{
		return this.ID;
	}
	
	public ItemStack getWinItem()
	{
		for(String str : this.ItemList)
		{
			String id = str.split(" ")[0];
			
			if(RpgStorage.CustomItemMap.containsKey(id))
			{
				float rate = Float.valueOf(str.split(" ")[1]);

				if(rate > RpgStorage.random.nextFloat())
				{
					return RpgStorage.CustomItemMap.get(id).getOriginalItem();
				}
			}
		}
		
		return null;
	}
}
