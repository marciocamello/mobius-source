package ai.talkingisland;

import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.utils.Location;

public class FourthGuard extends TIGuardSubAI
{
  public FourthGuard(NpcInstance actor)
  {
    super(actor);
    this._points = new Location[] { new Location(-116468, 255081, -1456), new Location(-115544, 254696, -1543), new Location(-114680, 254600, -1558), new Location(-114600, 254120, -1557), new Location(-114008, 254136, -1542), new Location(-113560, 254696, -1532), new Location(-112488, 254648, -1558), new Location(-113560, 254696, -1532), new Location(-114008, 254136, -1542), new Location(-114600, 254120, -1557), new Location(-114680, 254600, -1558), new Location(-115544, 254696, -1543) };
  }
}