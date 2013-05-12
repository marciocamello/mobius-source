package zones;

import lineage2.gameserver.listener.zone.OnZoneEnterLeaveListener;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Zone;
import lineage2.gameserver.scripts.ScriptFile;
import lineage2.gameserver.utils.Location;
import lineage2.gameserver.utils.ReflectionUtils;

public class BirthingRoom implements ScriptFile {

    private static final Location TELEPORT_LOC = new Location(-185839, 147909, -15312);
    private static final String[] zones = {"[telzone_Annihilation_0]", "[telzone_Annihilation_1]"};
    private static ZoneListener _zoneListener;

    private void init() {
        _zoneListener = new ZoneListener();

        for (String s : zones) {
            Zone zone = ReflectionUtils.getZone(s);
            zone.addListener(_zoneListener);
        }
    }

    @Override
    public void onLoad() {
        init();
    }

    @Override
    public void onReload() {
    }

    @Override
    public void onShutdown() {
    }

    public class ZoneListener implements OnZoneEnterLeaveListener {

        @Override
        public void onZoneEnter(Zone zone, Creature cha) {
            if (zone == null) {
                return;
            }

            if (cha == null) {
                return;
            }

            if ((zone.getName().equalsIgnoreCase("[telzone_Annihilation_0]"))
                    || (zone.getName().equalsIgnoreCase("[telzone_Annihilation_1]"))) {
                cha.teleToLocation(TELEPORT_LOC);
            }
        }

        @Override
        public void onZoneLeave(Zone zone, Creature cha) {
        }
    }
}