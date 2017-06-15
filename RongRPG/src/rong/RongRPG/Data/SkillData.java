package rong.RongRPG.Data;

import com.nisovin.magicspells.Spell;

public class SkillData 
{
	private String SkillID;
	private String SkillName;
	private Spell spell;
	
	public SkillData(String id, String name, Spell spell)
	{
		this.SkillID = id;
		this.SkillName = name;
		this.spell = spell;
	}
	
	public String getSkillID()
	{
		return this.SkillID;
	}
	
	public String getSkillName()
	{
		return this.SkillName;
	}
	
	public Spell getSpell()
	{
		return this.spell;
	}
}
