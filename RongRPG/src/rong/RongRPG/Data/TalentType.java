package rong.RongRPG.Data;

public enum TalentType 
{
	DAM("�g��", "�W�[ �����O", 10),
	STRONG("�j��", "�W�[ �̤j�ͩR, �ͩR��_", 12),
	SPIRITUAL("�믫", "�W�[ �̤j�]�O, �]�O��_", 14),
	RESISTANCE("�K��", "�W�[ �ܩ�", 16),
	DEADLY("�P�R", "�W�[ �z�����v", 20),
	SENSITIVE("�ӱ�", "�W�[�j�׾��v", 22),
	QUICKLY("���t", "�W�[���ʳt��", 24),
	FIRE_DAM("���K", "�W�[ ���K�ˮ`", 28),
	ICE_DAM("�H�B", "�W�[ �H�B�ˮ`", 30),
	THUNDER_DAM("�p�q", "�W�[ �p�q�ˮ`", 32),
	WIND_DAM("����", "�W�[ �����ˮ`", 34),
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
