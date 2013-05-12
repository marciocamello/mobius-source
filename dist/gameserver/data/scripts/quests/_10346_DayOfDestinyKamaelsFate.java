package quests;

import lineage2.gameserver.model.base.Race;
import lineage2.gameserver.scripts.ScriptFile;

public class _10346_DayOfDestinyKamaelsFate extends SagasSuperclass implements ScriptFile 
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

	public _10346_DayOfDestinyKamaelsFate()
	{
		super(false);

		StartNPC = 32221;
		StartRace = Race.kamael;

		init();
	}
}
