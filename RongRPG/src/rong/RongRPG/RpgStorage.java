package rong.RongRPG;

import java.util.HashMap;
import java.util.Random;

import org.bukkit.Location;

import net.elseland.xikage.MythicMobs.MythicMobs;

import com.comphenix.protocol.ProtocolManager;
import com.nisovin.magicspells.MagicSpells;

import rong.RongRPG.ConfigLoad.Config;
import rong.RongRPG.Data.AnvilData;
import rong.RongRPG.Data.CustomItem;
import rong.RongRPG.Data.LotteryData;
import rong.RongRPG.Data.MobSpawnerData;
import rong.RongRPG.Data.PlayerData;
import rong.RongRPG.Data.SkillData;
import rong.RongRPG.Data.MerchantData;
import rong.RongRPG.Data.TeleportData;

public class RpgStorage 
{
	public static RongRpg plugin;
	public static Config Config;
	public static ProtocolManager pm;
	public static MythicMobs mm;
	public static MagicSpells ms;
	public static String SystemTitle = "§c§l【系統】§f";
	public static String FriendTitle = "§9§l【好友】§f";
	public static String TeamTitle = "§a§l【隊伍】§f";
	public static HashMap<String, PlayerData> PlayerDataMap = new HashMap<>();
	public static HashMap<String, CustomItem> CustomItemMap = new HashMap<>();
	public static HashMap<String, SkillData> SkillDataMap = new HashMap<>();
	public static HashMap<String, AnvilData> AnvilDataMap = new HashMap<>();
	public static HashMap<String, MerchantData> PlayerMerchantMap = new HashMap<>();
	public static HashMap<Integer, MerchantData> MerchantDataMap = new HashMap<>();
	public static HashMap<Integer, MerchantData> NPCMerchantMap = new HashMap<>();
	public static HashMap<String, TeleportData> TeleporDatatMap = new HashMap<>();
	public static HashMap<Integer, MobSpawnerData> MobSpawnerMap = new HashMap<>();
	public static HashMap<String, LotteryData> LotteryMap = new HashMap<>();
	public static Random random = new Random();
	public static Location SpawnerCenterLocation;
	public static float ExpAddition = 1;
	public static boolean Debug;
}
