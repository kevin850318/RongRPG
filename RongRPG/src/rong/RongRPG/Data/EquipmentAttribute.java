package rong.RongRPG.Data;

public enum EquipmentAttribute 
{
	MAX_HEALTH("�ͩR", "MaxHealth"),
	REGAIN_HEALTH("�ͩR��_", "RegainHealth"),
	MAX_MANA("�]�O", "MaxMana"),
	REGAIN_MANA("�]�O��_", "RegainMana"),
	DAMAGE("�����O", "Damage"),
	DEFENSE("���m�O", "Defense"),
	RESISTANCE_DEFENSE("�ܩ�", "ResistanceDefense"),
	FIRE("���K", "Frie"),
	ICE("�H�B", "Ice"),
	THUNDER("�p�q", "Thunder"),
	WIND("����", "Wind")
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
