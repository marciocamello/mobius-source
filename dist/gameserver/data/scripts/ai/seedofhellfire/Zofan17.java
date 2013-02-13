package ai.seedofhellfire;

import lineage2.gameserver.ai.CtrlEvent;
import lineage2.gameserver.ai.Fighter;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.instances.NpcInstance;
import java.util.List;

public class Zofan17 extends Fighter
{
  public Zofan17(NpcInstance actor)
  {
    super(actor);
  }

  @Override
protected void onEvtAttacked(Creature attacker, int damage)
  {
    if ((attacker == null) || (attacker.isPlayable()))
      return;
    super.onEvtAttacked(attacker, damage);
  }

  @Override
public boolean checkAggression(Creature target)
  {
    if (target.isPlayable())
      return false;
    return super.checkAggression(target);
  }

  @Override
protected boolean thinkActive()
  {
    NpcInstance actor = getActor();
    if (actor.isDead()) {
      return false;
    }
    List<NpcInstance> around = actor.getAroundNpc(800, 500);
    if ((around != null) && (!around.isEmpty()))
      for (NpcInstance npc : around)
        if (npc.getNpcId() == 51018)
          actor.getAI().notifyEvent(CtrlEvent.EVT_ATTACKED, npc, Integer.valueOf(500));
    return true;
  }
}