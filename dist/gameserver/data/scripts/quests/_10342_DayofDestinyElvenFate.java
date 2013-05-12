package quests;

import lineage2.gameserver.model.base.Race;
import lineage2.gameserver.scripts.ScriptFile;

public class _10342_DayofDestinyElvenFate extends SagasSuperclass implements ScriptFile
{
	@Override
	public void onLoad()
	{
	}
	
	@Override
	public void onReload()
	{
	}
	
	@Override
	public void onShutdown()
	{
	}
	
	public _10342_DayofDestinyElvenFate()
	{
		super(false);
		
		StartNPC = 30856;
		StartRace = Race.elf;
		
		init();
	}
}
