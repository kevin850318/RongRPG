package rong.RongGuild.Data;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import rong.RongGuild.GuildStorage;
import rong.RongRPG.RpgStorage;
import rong.RongRPG.Util.RpgUtil;

public class GuildData 
{
	private String GuildName;
	private String GuildMotd;
	private int GuildLevel;
	private int MemberLimit;
	private HashMap<String, GuildPlayerData> GuildMembers = new HashMap<>();
	private HashMap<Integer, GuildRank> GuildRank = new HashMap<>();
	private ArrayList<String> GuildInvited = new ArrayList<>();
	private File GuildFile;
	private FileConfiguration GuildConfig;
	
	public GuildData(String guildName)
	{
		this.GuildName = guildName;
		this.GuildFile = new File("./plugins/RongGuild/GuildData/" + this.GuildName + ".yml");
		this.GuildConfig = YamlConfiguration.loadConfiguration(GuildFile);
		
		if(!GuildFile.exists())
		{
			//建立公會資料
			GuildConfig.createSection(GuildName);
			GuildConfig.set(GuildName + ".Motd", GuildName + " §f歡迎你的加入");
			GuildConfig.set(GuildName + ".Level", 1);
			GuildConfig.set(GuildName + ".MemberLimit", 20);
			GuildConfig.createSection(GuildName + ".Members");
			GuildConfig.set(GuildName + ".Rank.1.Title", "§d公會長");
			GuildConfig.set(GuildName + ".Rank.1.ChatPermission", true);
			GuildConfig.set(GuildName + ".Rank.1.InvitePermission", true);
			GuildConfig.set(GuildName + ".Rank.1.KickPermission", true);
			GuildConfig.set(GuildName + ".Rank.2.Title", "§b副公會長");
			GuildConfig.set(GuildName + ".Rank.2.ChatPermission", true);
			GuildConfig.set(GuildName + ".Rank.2.InvitePermission", true);
			GuildConfig.set(GuildName + ".Rank.2.KickPermission", false);
			GuildConfig.set(GuildName + ".Rank.3.Title", "§9公會幹部");
			GuildConfig.set(GuildName + ".Rank.3.ChatPermission", true);
			GuildConfig.set(GuildName + ".Rank.3.InvitePermission", true);
			GuildConfig.set(GuildName + ".Rank.3.KickPermission", false);
			GuildConfig.set(GuildName + ".Rank.4.Title", "§f公會成員");
			GuildConfig.set(GuildName + ".Rank.4.ChatPermission", true);
			GuildConfig.set(GuildName + ".Rank.4.InvitePermission", false);
			GuildConfig.set(GuildName + ".Rank.4.KickPermission", false);
			RpgUtil.saveFileConfig(GuildFile, GuildConfig);
			
			RpgStorage.plugin.getLogger().log(Level.INFO, GuildName + " 建立成功");
		}
		
		loadGuildData();
	}
		
	public void loadGuildData()
	{
		GuildMotd = GuildConfig.getString(GuildName + ".Motd", GuildName + " §f歡迎你的加入");
		GuildLevel = GuildConfig.getInt(GuildName + "Level", 1);
		MemberLimit = GuildConfig.getInt(GuildName + "MemberLimit", 20);
		//讀取階級資料
		for(String rankLevel : GuildConfig.getConfigurationSection(GuildName + ".Rank").getKeys(false))
		{
			int level = Integer.parseInt(rankLevel);
			GuildRank rank = new GuildRank(level, GuildConfig.getString(GuildName + ".Rank." + rankLevel + ".Title", "§c錯誤"));
			rank.setChatPermission(GuildConfig.getBoolean(GuildName + ".Rank." + rankLevel + ".ChatPermission", false));
			rank.setInvitePermission(GuildConfig.getBoolean(GuildName + ".Rank." + rankLevel + ".InvitePermission", false));
			rank.setKickPermission(GuildConfig.getBoolean(GuildName + ".Rank." + rankLevel + ".KickPermission", false));
			
			GuildRank.put(level, rank);
		}
		//讀取成員資料
		for(String member : GuildConfig.getConfigurationSection(GuildName + ".Members").getKeys(false))
		{
			GuildPlayerData guildPlayerData = new GuildPlayerData(member, GuildName, GuildConfig.getInt(GuildName + ".Members." + member + ".Rank"));
			
			GuildMembers.put(member, guildPlayerData);
			GuildStorage.PlayerGuildMap.put(member, GuildName);
		}
		
		RpgStorage.plugin.getLogger().log(Level.INFO, GuildName + " 讀取成功");
	}
	
	public void saveGuildData()
	{
		GuildConfig.set(GuildName + ".Motd", GuildMotd);
		GuildConfig.set(GuildName + ".Level", GuildLevel);
		GuildConfig.set(GuildName + ".MemberLimit", MemberLimit);
		
		for(String member : GuildMembers.keySet())
		{
			GuildConfig.set(GuildName + ".Members." + member + ".Rank", GuildMembers.get(member).getRank());
		}
		
		for(GuildRank rank : GuildRank.values())
		{
			GuildConfig.set(GuildName + ".Rank." + rank.getRankLevel() + ".Title", rank.getRankTitle());
			GuildConfig.set(GuildName + ".Rank." + rank.getRankLevel() + ".ChatPermission", rank.hasChatPermission());
			GuildConfig.set(GuildName + ".Rank." + rank.getRankLevel() + ".InvitePermission", rank.hasInvitePermission());
			GuildConfig.set(GuildName + ".Rank." + rank.getRankLevel() + ".KickPermission", rank.hasKickPermission());
		}
		
		RpgUtil.saveFileConfig(GuildFile, GuildConfig);
		RpgStorage.plugin.getLogger().log(Level.INFO, GuildName + " 儲存成功");
	}
	
	public void sendGuildMessage(String msg)
	{
		for(String member : GuildMembers.keySet())
		{
			if(Bukkit.getPlayer(member) != null)
			{
				Player m = Bukkit.getPlayer(member);
				
				m.sendMessage(msg);
			}
		}
	}
			
	public void addGuildMember(String member, GuildPlayerData gpd)
	{
		this.GuildMembers.put(member, gpd);
	}
	
	public void addGuildInvited(String target)
	{
		this.GuildInvited.add(target);
	}
	
	public void removeGuildMember(String member)
	{
		this.GuildMembers.remove(member);
	}
	
	public void removeGuildInvited(String target)
	{
		this.GuildInvited.remove(target);
	}
	
	public String getGuildName()
	{
		return this.GuildName;
	}
	
	public String getGuildMotd()
	{
		return this.GuildMotd;
	}
	
	public int getGuildLevel()
	{
		return this.GuildLevel;
	}
	
	public int getMemberLimit()
	{
		return this.MemberLimit;
	}
	
	public HashMap<String, GuildPlayerData> getMemberList()
	{
		return this.GuildMembers;
	}
	
	public GuildPlayerData getMemberData(String name)
	{
		for(String member : GuildMembers.keySet())
		{
			if(member.equals(name))
			{
				return GuildMembers.get(name);
			}
		}
		return null;
	}
	
	public GuildRank getGuildRank(int RankLevel)
	{
		if(GuildRank.containsKey(RankLevel))
		{
			return this.GuildRank.get(RankLevel);
		}
		return null;
	}
	
	public ArrayList<String> getGuildInvited()
	{
		return this.GuildInvited;
	}
}
