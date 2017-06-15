package rong.RongGuild.Data;

public class GuildPlayerData 
{
	private String playerName;
	private String GuildName;
	private int rank;
	private int changingRank;
	private boolean changeRankTitle;
	
	public GuildPlayerData(String playerName, String GuildName, int rank)
	{
		this.playerName = playerName;
		this.GuildName = GuildName;
		this.rank = rank;
	}
	
	public void upRank()
	{
		this.rank -= 1;
	}
	
	public void downRank()
	{
		this.rank += 1;
	}
	
	public String getPlayerName()
	{
		return this.playerName;
	}
	
	public String getGuildName()
	{
		return this.GuildName;
	}
		
	public int getRank()
	{
		return this.rank;
	}
	
	public int getChangingRank()
	{
		if(changeRankTitle)
		{
			return this.changingRank;
		}
		
		return 0;
	}
	
	public boolean isChangeRankTitle()
	{
		if(changeRankTitle)
		{
			return true;
		}
		
		return false;
	}
	
	public void setChangeRankTitle(boolean bool)
	{
		this.changeRankTitle = bool;
	}
	
	public void setChangingRank(int i)
	{
		this.changingRank = i;
	}
}
