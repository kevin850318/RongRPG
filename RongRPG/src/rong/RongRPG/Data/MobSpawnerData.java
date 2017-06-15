package rong.RongRPG.Data;

import java.util.ArrayList;
import java.util.List;

public class MobSpawnerData 
{
	private int ID;
	private int minDistance;
	private int maxDistance;
	private List<String> mobList = new ArrayList<>();
	
	public MobSpawnerData(int id, int min, int max)
	{
		this.ID = id;
		this.minDistance = min;
		this.maxDistance = max;
	}
	
	public void setMobList(List<String> list)
	{
		this.mobList = list;
	}
	
	public int getID()
	{
		return this.ID;
	}
	
	public int getMinDistance()
	{
		return this.minDistance;
	}
	
	public int getMaxDistance()
	{
		return this.maxDistance;
	}
	
	public List<String> getMobList()
	{
		return this.mobList;
	}
}
