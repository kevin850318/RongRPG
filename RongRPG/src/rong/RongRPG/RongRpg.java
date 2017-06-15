package rong.RongRPG;

import net.elseland.xikage.MythicMobs.MythicMobs;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers.ClientCommand;
import com.nisovin.magicspells.MagicSpells;

import rong.RongFriend.Command.FriendCommand;
import rong.RongRPG.Command.Cmd;
import rong.RongRPG.Command.MerchantCommand;
import rong.RongRPG.Command.TeleportPointCommand;
import rong.RongRPG.ConfigLoad.Config;
import rong.RongRPG.Listener.EntityAttack;
import rong.RongRPG.Listener.EntityCombust;
import rong.RongRPG.Listener.InventoryClick;
import rong.RongRPG.Listener.InventoryClose;
import rong.RongRPG.Listener.ItemSpawn;
import rong.RongRPG.Listener.MagicSpellCast;
import rong.RongRPG.Listener.MythicMobsDeath;
import rong.RongRPG.Listener.NPCRightClick;
import rong.RongRPG.Listener.PlayerAttack;
import rong.RongRPG.Listener.PlayerChat;
import rong.RongRPG.Listener.PlayerDropItem;
import rong.RongRPG.Listener.PlayerExpChange;
import rong.RongRPG.Listener.PlayerInteract;
import rong.RongRPG.Listener.PlayerJoin;
import rong.RongRPG.Listener.PlayerQuit;
import rong.RongRPG.Listener.SkillDamage;
import rong.RongRPG.Listener.WeatherChange;
import rong.RongRPG.Runnable.FiveSecondRunnable;
import rong.RongRPG.Runnable.TenSecondRunnable;
import rong.RongRPG.Util.InventoryUtil;
import rong.RongRPG.Util.RpgUtil;
import rong.RongTeam.Listener.TeamPickUpItem;
import rong.RongTeam.Listener.TeamPlayerQuit;
import rong.RongWarehouse.Listener.WarehouseInventoryClose;

public class RongRpg extends JavaPlugin
{	
	@Override
	public void onEnable()
	{
//		RpgUtil.sendServerLog("info", "啟動...");
		
		RpgStorage.plugin = this;
		RpgStorage.Debug = getConfig().getBoolean("Debug", false);
		
		registerSupportPlugin();
		registerEvent();
		registerCommand();
		RpgStorage.Config = new Config();
		//ProtocolLib Packet
		RpgStorage.pm.addPacketListener(new PacketAdapter(this, ListenerPriority.NORMAL, new PacketType[] {PacketType.Play.Client.CLIENT_COMMAND})
		{
			public void onPacketReceiving(final PacketEvent event)
			{
				final Player player = event.getPlayer();
				
				if(event.getPacket().getClientCommands().read(0) == ClientCommand.OPEN_INVENTORY_ACHIEVEMENT && event.getPlayer().isSneaking())
				{
					Bukkit.getScheduler().runTask(RpgStorage.plugin, new Runnable()
					{
						@Override
						public void run() 
						{
							InventoryUtil.openPlayerInformationPage(player);
						}
					});
				}
			}
		});
		//Load Online Player Data
		RpgUtil.loadAllPlayerData();
//		MobSpawnData.loadMobSpawnerData();
		
		Bukkit.getScheduler().runTaskTimer(RpgStorage.plugin, new FiveSecondRunnable(), 0, 100);
		Bukkit.getScheduler().runTaskTimer(RpgStorage.plugin, new TenSecondRunnable(), 0, 200);
		
		saveDefaultConfig();
	}
	
	@Override
	public void onDisable()
	{	
		RpgUtil.sendServerLog("info", "關閉...");
		
		//Save Online Player Data
		RpgUtil.saveAllPlayerData();
		RpgStorage.Config.saveConfig();
		
		saveDefaultConfig();
	}
	
	void registerCommand()
	{
		getCommand("rrpg").setExecutor(new Cmd());
		getCommand("friend").setExecutor(new FriendCommand());		
		getCommand("merchant").setExecutor(new MerchantCommand());
		getCommand("teleportpoint").setExecutor(new TeleportPointCommand());
	}
	
	void registerEvent()
	{
		//PlayerEvent
		getServer().getPluginManager().registerEvents(new PlayerJoin(), this);
		getServer().getPluginManager().registerEvents(new PlayerQuit(), this);
		getServer().getPluginManager().registerEvents(new PlayerExpChange(), this);
		getServer().getPluginManager().registerEvents(new PlayerDropItem(), this);		
		getServer().getPluginManager().registerEvents(new PlayerInteract(), this);		
		getServer().getPluginManager().registerEvents(new PlayerChat(), this);
		getServer().getPluginManager().registerEvents(new PlayerAttack(), this);
		//InventoryEvent
		getServer().getPluginManager().registerEvents(new InventoryClose(), this);
		getServer().getPluginManager().registerEvents(new InventoryClick(), this);
		//EntityEvent
		getServer().getPluginManager().registerEvents(new EntityAttack(), this);
		getServer().getPluginManager().registerEvents(new EntityCombust(), this);
		//OtherEvent
		getServer().getPluginManager().registerEvents(new ItemSpawn(), this);
		getServer().getPluginManager().registerEvents(new SkillDamage(), this);
		getServer().getPluginManager().registerEvents(new WeatherChange(), this);
		//Team Event
		getServer().getPluginManager().registerEvents(new TeamPickUpItem(), this);
		getServer().getPluginManager().registerEvents(new TeamPlayerQuit(), this);
		//Warehouse Event
		getServer().getPluginManager().registerEvents(new WarehouseInventoryClose(), this);
		getServer().getPluginManager().registerEvents(new NPCRightClick(), this);
	}
	
	void registerSupportPlugin()
	{
		registerProtocolLibPlugin();
		registerMythicMobsPlugin();
		registerMagicSpellsPlugin();
	}
	
	void registerProtocolLibPlugin()
	{
		if(Bukkit.getPluginManager().getPlugin("ProtocolLib") != null)
		{
			RpgStorage.pm = ProtocolLibrary.getProtocolManager();
			RpgUtil.sendServerLog("info", "ProtocolLib 支援啟動!");
		}
		else
		{
			RpgUtil.sendServerLog("info", "ProtocolLib 支援未啟動!");
		}
	}
	
	void registerMythicMobsPlugin()
	{
		if(Bukkit.getPluginManager().getPlugin("MythicMobs") != null)
		{
			RpgStorage.mm = (MythicMobs) Bukkit.getPluginManager().getPlugin("MythicMobs");
			RpgUtil.sendServerLog("info", "MythicMobs 支援啟動!");
			
			getServer().getPluginManager().registerEvents(new MythicMobsDeath(),  this);
		}
		else
		{
			RpgUtil.sendServerLog("info", "MythicMobs 支援未啟動!");
		}
	}
	
	void registerMagicSpellsPlugin()
	{
		if(Bukkit.getPluginManager().getPlugin("MagicSpells") != null)
		{
			RpgStorage.ms = (MagicSpells) Bukkit.getPluginManager().getPlugin("MagicSpells");
			RpgUtil.sendServerLog("info", "MagicSpells 支援啟動!");
			getServer().getPluginManager().enablePlugin(RpgStorage.ms);
			getServer().getPluginManager().registerEvents(new MagicSpellCast(), this);
		}
		else
		{
			RpgUtil.sendServerLog("info", "MagicSpells 支援未啟動!");
		}
	}
}
