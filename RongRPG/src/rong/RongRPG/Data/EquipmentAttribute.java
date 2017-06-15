package rong.RongRPG.Data;

public enum EquipmentAttribute 
{
	MAX_HEALTH("生命", "MaxHealth"),
	REGAIN_HEALTH("生命恢復", "RegainHealth"),
	MAX_MANA("魔力", "MaxMana"),
	REGAIN_MANA("魔力恢復", "RegainMana"),
	DAMAGE("攻擊力", "Damage"),
	DEFENSE("防禦力", "Defense"),
	RESISTANCE_DEFENSE("抗性", "ResistanceDefense"),
	FIRE("火焰", "Frie"),
	ICE("寒冰", "Ice"),
	THUNDER("雷電", "Thunder"),
	WIND("颶風", "Wind")
	;
	
	private String AttributeName;
	private String AttributeID;
	private int AttributeValue = 0;
	private boolean Percentage = false;
	
	EquipmentAttribute(String typeName, String id)
	{
		this.AttributeName = typeName;
		this.AttributeID = id;
	}
	
	public void setAttributeVault(String strValue)
	{
		if(strValue.endsWith("%"))
		{
			System.out.println("- %%%%");
//			this.Percentage = true;
		}
		
//		System.out.println("- strValue: " + strValue);
		strValue = strValue.replace("%", "");
		this.AttributeValue = Integer.parseInt(strValue);
	}
	
	public static EquipmentAttribute transformToEquipmentAttribute(String typeName)
	{
		for(EquipmentAttribute type : EquipmentAttribute.values())
		{
			if(type.AttributeName.equals(typeName))
			{
				return type;
			}
		}
		
		return null;
	}
	
	public String getAttributeName()
	{
		return this.AttributeName;
	}
	
	public String getAttributeID()
	{
		return this.AttributeID;
	}
	
	public int getAttributeValue()
	{
		return this.AttributeValue;
	}
	
	public boolean isPercentage()
	{
		return this.Percentage;
	}
}
