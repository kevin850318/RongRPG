package rong.RongRPG.Data;

public enum StatusType 
{
	HEALTH("生命"),
	MAX_HEALTH("生命"),
	REGAIN_HEALTH("生命"),
	MANA("生命"),
	MAX_MANA("生命"),
	REGAIN_MANA("生命"),
	;
	
	private String TypeName;
	private int Value;
	
	StatusType(String name)
	{
		this.TypeName = name;
	}
	
	public void setValue(int value)
	{
		this.Value = value;
	}
	
	public String getTypeName()
	{
		return this.TypeName;
	}
	
	public int getValue()
	{
		return this.Value;
	}
}
