package rong.RongRPG.Data;

public enum EquipmentLimit 
{
	STR("�O�q"),
	DEX("�ӱ�"),
	VIT("���"),
	WIS("���z"),
	LUK("���B"),
	MEN("�믫"),
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
