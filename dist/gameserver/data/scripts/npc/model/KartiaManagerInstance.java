package npc.model;

import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.templates.npc.NpcTemplate;
import lineage2.gameserver.utils.Location;
import lineage2.gameserver.utils.ReflectionUtils;
import instances.KartiaLabyrinth85Solo;
import instances.KartiaLabyrinth90Solo;
import instances.KartiaLabyrinth95Solo;

@SuppressWarnings("serial")
public final class KartiaManagerInstance extends NpcInstance
{
  private static final Location TELEPORT_POSITION = new Location(-109032, -10440, -11949);

  public KartiaManagerInstance(int objectId, NpcTemplate template)
  {
    super(objectId, template);
  }

  @Override
public void onBypassFeedback(Player player, String command)
  {
    if (!canBypassCheck(player, this)) {
      return;
    }
    if (command.equalsIgnoreCase("request_zellaka_solo"))
    {
      Reflection r = player.getActiveReflection();
      if (r != null)
      {
        if (player.canReenterInstance(205))
          player.teleToLocation(r.getTeleportLoc(), r);
      }
      else if (player.canEnterInstance(205))
      {
        ReflectionUtils.enterReflection(player, new KartiaLabyrinth85Solo(), 205);
      }
    }
    if (command.equalsIgnoreCase("request_pelline_solo"))
    {
      Reflection r = player.getActiveReflection();
      if (r != null)
      {
        if (player.canReenterInstance(206))
          player.teleToLocation(r.getTeleportLoc(), r);
      }
      else if (player.canEnterInstance(206))
      {
        ReflectionUtils.enterReflection(player, new KartiaLabyrinth90Solo(), 206);
      }
    }
    if (command.equalsIgnoreCase("request_kalios_solo"))
    {
      Reflection r = player.getActiveReflection();
      if (r != null)
      {
        if (player.canReenterInstance(207))
          player.teleToLocation(r.getTeleportLoc(), r);
      }
      else if (player.canEnterInstance(207))
      {
        ReflectionUtils.enterReflection(player, new KartiaLabyrinth95Solo(), 207);
      }
    }
    if (command.startsWith("start_zellaka_solo"))
    {
      player.teleToLocation(TELEPORT_POSITION);
    }
    super.onBypassFeedback(player, command);
  }
}