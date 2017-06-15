package rong.RongRPG.Data;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

import rong.RongRPG.RpgStorage;
import rong.RongRPG.Util.RpgUtil;

public class CustomItem 
{
	private ItemStack OriginalItem;
	private String ItemID;
	private ItemType Type = ItemType.ITEM; 
	private int Coin = 1;
	
	public CustomItem(String id, ItemStack is, ItemType type, int coin)
	{
		this.OriginalItem = is;
		this.ItemID = id;
		this.Type = type;
		this.Coin = coin;
	}
	
	public static CustomItem getCustomItem(ItemStack is)
	{
		if(RpgUtil.isRpgItem(is) && is.getItemMeta().hasLore())
		{
			List<String> Lore = is.getItemMeta().getLore();
			String id = ChatColor.stripColor(Lore.get(0));
			
			if(id.startsWith("["))
			{
				id = id.replace("[", "").replace("]", "");
				
				if(RpgStorage.CustomItemMap.containsKey(id))
				{
					return RpgStorage.CustomItemMap.get(id);
				}
			}
		}
		
		return null;
	}
		
	public void setItemType(ItemType type)
	{
		this.Type = type;
	}
	
	public ItemStack getOriginalItem()
	{
		return this.OriginalItem;
	}
	
	public String getItemID()
	{
		return this.ItemID;
	}
	
	public ItemType getItemType()
	{
		return this.Type;
	}
	
	public int getCoin()
	{
		return this.Coin;
	}
	
	public enum ItemType
	{
		ITEM, SKILL_BOOK, EQUIPMENT, GEM, SCROLL;
	}
}
