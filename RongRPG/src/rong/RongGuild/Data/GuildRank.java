package rong.RongGuild.Data;

public class GuildRank 
{
	private int RankLevel;
	private String RankTitle;
	private boolean ChatPermission;
	private boolean InvitePermission;
	private boolean KickPermission;
	private boolean MotdPermission;
	
	public GuildRank(int level, String title)
	{
		this.RankLevel = level;
		this.RankTitle = title;
	}
	
	public int getRankLevel()
	{
		return this.RankLevel;
	}
	
	public String getRankTitle()
	{
		return this.RankTitle;
	}
	
	public boolean hasChatPermission()
	{
		if(ChatPermission)
		{
			return true;
		}
		return false;
	}
	
	public boolean hasInvitePermission()
	{
		if(InvitePermission)
		{
			return true;
		}
		return false;
	}
	
	public boolean hasKickPermission()
	{
		if(KickPermission)
		{
			return true;
		}
		return false;
	}
	
	public boolean hasMotdPermission()
	{
		if(MotdPermission)
		{
			return true;
		}
		return false;
	}
	
	public void setRankTitle(String title)
	{
		this.RankTitle = title;
	}
	
	public void setChatPermission(boolean bool)
	{
		this.ChatPermission = bool;
	}
	
	public void setInvitePermission(boolean bool)
	{
		this.InvitePermission = bool;
	}
	
	public void setKickPermission(boolean bool)
	{
		this.KickPermission = bool;
	}
	
	public void setMotdPermission(boolean bool)
	{
		this.MotdPermission = bool;
	}
}
