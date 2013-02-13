package ai.talkingisland;

import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.utils.Location;

public class ThirdGuard extends TIGuardSubAI
{
  public ThirdGuard(NpcInstance actor)
  {
    super(actor);
    this._points = new Location[] { new Location(-112552, 254712, -1558), new Location(-113800, 254760, -1548), new Location(-114376, 255208, -1546), new Location(-114744, 255128, -1550), new Location(-115160, 254728, -1547), new Location(-116056, 254824, -1534), new Location(-116536, 255192, -1475), new Location(-116056, 254824, -1534), new Location(-115160, 254728, -1547), new Location(-114744, 255128, -1550), new Location(-114376, 255208, -1546), new Location(-113800, 254760, -1548) };
  }
}