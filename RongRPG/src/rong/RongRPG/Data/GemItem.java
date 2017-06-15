package rong.RongRPG.Data;

import org.bukkit.inventory.ItemStack;

public class GemItem extends CustomItem
{
	private GemType Type;
	
	public GemItem(String id, ItemStack is, ItemType iType, int coin, GemType gType)
	{
		super(id, is, iType, coin);
		
		this.Type = gType;
	}
	
	public GemType getGemType()
	{
		return this.Type;
	}
	
	public enum GemType
	{
		IDENTIFY, GRID, UPGRADE;
	}
}
