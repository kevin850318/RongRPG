package rong.RongRPG.Data;

public enum StatusType 
{
	HEALTH("�ͩR"),
	MAX_HEALTH("�ͩR"),
	REGAIN_HEALTH("�ͩR"),
	MANA("�ͩR"),
	MAX_MANA("�ͩR"),
	REGAIN_MANA("�ͩR"),
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
