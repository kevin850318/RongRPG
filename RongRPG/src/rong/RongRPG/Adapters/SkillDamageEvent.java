package rong.RongRPG.Adapters;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class SkillDamageEvent extends Event
{
	private Player Player;
	private LivingEntity Target;
	private int Damage;
	private int FireDamage;
	private int IceDamage;
	private int ThunderDamage;
	private int WindDamage;

	public SkillDamageEvent(Player player, LivingEntity target, int damage, int firedamage, int icedamage, int thunderdamage, int winddamage)
	{
		this.Player = player;
		this.Target = target;
		this.Damage = damage;
		this.FireDamage = firedamage;
		this.IceDamage = icedamage;
		this.ThunderDamage = thunderdamage;
		this.WindDamage = winddamage;
	}
	
	public Player getPlayer()
	{
		return this.Player;
	}
	
	public LivingEntity getTarget()
	{
		return this.Target;
	}
	
	public int getDamage()
	{
		return this.Damage;
	}
	
	public int getFireDamage()
	{
		return this.FireDamage;
	}
	
	public int getIceDamage()
	{
		return this.IceDamage;
	}
	
	public int getThunderDamage()
	{
		return this.ThunderDamage;
	}
	
	public int getWindDamage()
	{
		return this.WindDamage;
	}
	
	private static final HandlerList handlers = new HandlerList();
	
	public HandlerList getHandlers() 
	{
	    return handlers;
	}

	public static HandlerList getHandlerList() 
	{
	    return handlers;
	}
}
