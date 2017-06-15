package rong.RongRPG.Data;

public enum EquipmentLimit 
{
	STR("力量"),
	DEX("敏捷"),
	VIT("體質"),
	WIS("智慧"),
	LUK("幸運"),
	MEN("精神"),
	;
	
	private String LimitName;
	private int LimitValue;
	
	EquipmentLimit(String name)
	{
		this.LimitName = name;
	}
	
	public void setStatusValue(int i)
	{
		this.LimitValue = i;
	}
	
	public static EquipmentLimit transformToEquipmentLimit(String name)
	{
		for(EquipmentLimit ps : EquipmentLimit.values())
		{
			if(name.equals(ps.getLimitName()))
			{
				return ps;
			}
		}
		
		return null;
	}
	
	public String getLimitName()
	{
		return this.LimitName;
	}
	
	public int getLimitValue()
	{
		return this.LimitValue;
	}
}
