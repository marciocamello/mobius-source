package quests;

import lineage2.gameserver.model.base.Race;
import lineage2.gameserver.scripts.ScriptFile;

public class _10345_DayOfDestinyDwarfsFate extends SagasSuperclass implements ScriptFile 
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

	public _10345_DayOfDestinyDwarfsFate()
	{
		super(false);

		StartNPC = 30847;
		StartRace = Race.dwarf;

		init();
	}
}

