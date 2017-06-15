package rong.RongRPG.Data;

import org.bukkit.Location;

public class TeleportData 
{
	private String ID;
	private Location Point1;
	private Location Point2;
	
	public TeleportData(String id)
	{
		this.ID = id;
	}
	
	public void setPoint1Location(Location loc)
	{
		this.Point1 = loc;
	}
	
	public void setPoint2Location(Location loc)
	{
		this.Point2 = loc;
	}
	
	public String getID()
	{
		return this.ID;
	}
	
	public Location getPoint1Location()
	{
		return this.Point1;
	}
	
	public Location getPoint2Location()
	{
		return this.Point2;
	}
}
