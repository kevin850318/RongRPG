package rong.RongRPG.Listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import rong.RongRPG.RpgStorage;
import rong.RongRPG.Data.PlayerData;

import com.nisovin.magicspells.Spell;
import com.nisovin.magicspells.events.SpellCastEvent;

public class MagicSpellCast implements Listener 
{
	@EventHandler
	public void onCast(SpellCastEvent event)
	{
		Spell spell = event.getSpell();
		PlayerData pd = RpgStorage.PlayerDataMap.get(event.getCaster().getName());
		
		pd.setInt("Mana", pd.getInt("Mana") - spell.getReagents().getMana());
	}
}
