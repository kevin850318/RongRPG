package rong.RongRPG.Data;

public enum TalentType 
{
	DAM("狂暴", "增加 攻擊力", 10),
	STRONG("強壯", "增加 最大生命, 生命恢復", 12),
	SPIRITUAL("精神", "增加 最大魔力, 魔力恢復", 14),
	RESISTANCE("鐵壁", "增加 抗性", 16),
	DEADLY("致命", "增加 爆擊機率", 20),
	SENSITIVE("敏捷", "增加迴避機率", 22),
	QUICKLY("迅速", "增加移動速度", 24),
	FIRE_DAM("火焰", "增加 火焰傷害", 28),
	ICE_DAM("寒冰", "增加 寒冰傷害", 30),
	THUNDER_DAM("雷電", "增加 雷電傷害", 32),
	WIND_DAM("颶風", "增加 颶風傷害", 34),
	;
	
	private String TypeName;
	private String Depiction;
	private int Value = 0;
	private int Slot;
	
	TalentType(String name, String depiction, int slot)
	{
		this.TypeName = name;
		this.Depiction = depiction;
		this.Slot = slot;
	}
	
	public void setValue(int value)
	{
		this.Value = value;
	}
	
	public String getTypeName()
	{
		return this.TypeName;
	}
	
	public String getDepiction()
	{
		return this.Depiction;
	}
	
	public int getValue()
	{
		return this.Value;
	}
	
	public int getSlot()
	{
		return this.Slot;
	}
	
	public static TalentType getTalentType(int slot)
	{
		for(TalentType type : TalentType.values())
		{
			if(slot == type.getSlot())
			{
				return type;
			}
		}
		
		return null;
	}
}
