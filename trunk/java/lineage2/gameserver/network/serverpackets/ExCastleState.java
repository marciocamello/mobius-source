package lineage2.gameserver.network.serverpackets;

import lineage2.gameserver.model.entity.residence.Castle;
import lineage2.gameserver.model.entity.residence.ResidenceSide;
import lineage2.gameserver.network.serverpackets.L2GameServerPacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Bonux
 */
public class ExCastleState extends L2GameServerPacket
{
	private static final Logger _log = LoggerFactory.getLogger(ExCastleState.class);
	private final int _id;
	private final ResidenceSide _side;
	
	public ExCastleState(Castle castle)
	{
		_id = castle.getId();
		_side = castle.getResidenceSide();
	}

	protected void writeImpl()
	{
		writeEx(0x133);
		writeD(_id);
		if(_side==ResidenceSide.NEUTRAL)
		{
		writeD(_side.ordinal());
		_log.info("neutral");
		}else if(_side==ResidenceSide.LIGHT)
		{
		writeD(_side.ordinal()+1);
		_log.info("light");
		}else
		{
		writeD(_side.ordinal()+2);
		_log.info("dark");
		}
		
	}
}