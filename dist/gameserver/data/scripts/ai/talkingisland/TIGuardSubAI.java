package ai.talkingisland;

import lineage2.commons.util.Rnd;
import lineage2.gameserver.ai.DefaultAI;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.network.serverpackets.components.NpcString;
import lineage2.gameserver.utils.Location;

public class TIGuardSubAI extends DefaultAI
{
  protected Location[] _points;
  private int _lastPoint = 0;

  public TIGuardSubAI(NpcInstance actor)
  {
    super(actor);
  }

  @Override
  protected void onEvtAttacked(Creature attacker, int damage)
  {
  }

  @Override
  protected void onEvtAggression(Creature target, int aggro)
  {
  }

  @Override
  protected boolean thinkActive() {
    if (!this._def_think)
      startMoveTask();
    return true;
  }

  @Override
  protected void onEvtArrived()
  {
    startMoveTask();
    if (Rnd.chance(52))
      sayRndMsg();
    super.onEvtArrived();
  }

  @Override
  public boolean isGlobalAI()
  {
    return true;
  }

  private void sayRndMsg()
  {
    NpcInstance actor = getActor();
    if (actor == null)
      return;
    NpcString ns;
    switch (Rnd.get(6))
    {
    case 1:
      ns = NpcString.SOMETHING_LIKE_THAT_COMES_OUT_OF_THE_RUINS;
      break;
    case 2:
      ns = NpcString.SOMETHING_LIKE_THAT_COMES_OUT_OF_THE_RUINS;
      break;
    case 3:
      ns = NpcString.SOMETHING_LIKE_THAT_COMES_OUT_OF_THE_RUINS;
      break;
    case 4:
      ns = NpcString.SOMETHING_LIKE_THAT_COMES_OUT_OF_THE_RUINS;
      break;
    case 5:
      ns = NpcString.SOMETHING_LIKE_THAT_COMES_OUT_OF_THE_RUINS;
      break;
    default:
      ns = NpcString.SOMETHING_LIKE_THAT_COMES_OUT_OF_THE_RUINS;
    }

    Functions.npcSay(actor, ns, new String[0]);
  }

  private void startMoveTask()
  {
    this._lastPoint += 1;
    if (this._lastPoint >= this._points.length)
      this._lastPoint = 0;
    addTaskMove(this._points[this._lastPoint], false);
    doTask();
  }
}