package ai.talkingisland;

import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.utils.Location;

public class SecondGuard extends TIGuardSubAI
{
  public SecondGuard(NpcInstance actor)
  {
    super(actor);
    this._points = new Location[] { new Location(-114680, 255944, -1537), new Location(-114728, 254760, -1558), new Location(-114696, 253624, -1558), new Location(-114680, 252760, -1571), new Location(-114696, 253624, -1558), new Location(-114728, 254760, -1558) };
  }
}