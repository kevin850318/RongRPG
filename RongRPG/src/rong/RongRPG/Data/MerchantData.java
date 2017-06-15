package rong.RongRPG.Data;

import java.util.HashMap;

public class MerchantData 
{
	private int ID;
	private String Name;
	private HashMap<Integer, CustomItem> ItemMap = new HashMap<>();
	private MerchantStatus Status = MerchantStatus.PREPARE;
	
	public MerchantData(int id, String name)
	{
		this.ID = id;
		this.Name = name;
	}
	
	public void setName(String name)
	{
		this.Name = name;
	}
	
	public void setItem(int slot, CustomItem ci)
	{
		this.ItemMap.put(slot, ci);
	}
	
	public void setStatus(MerchantStatus status)
	{
		this.Status = status;
	}
	
	public int getID()
	{
		return this.ID;
	}
	
	public String getName()
	{
		return this.Name;
	}
	
	public HashMap<Integer, CustomItem> getItemMap()
	{
		return this.ItemMap;
	}
	
	public CustomItem getItem(int slot)
	{
		return this.ItemMap.get(slot);
	}
	
	public MerchantStatus getStatus()
	{
		return this.Status;
	}
	
	public enum MerchantStatus
	{
		OPEN, PREPARE;
	}
}
