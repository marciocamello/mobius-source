/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package lineage2.gameserver.network.serverpackets;

import lineage2.gameserver.model.entity.residence.ClanHall;
import lineage2.gameserver.model.entity.residence.ResidenceFunction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class AgitDecoInfo extends L2GameServerPacket
{
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(AgitDecoInfo.class);
	/**
	 * Field _buff.
	 */
	private static int[] _buff =
	{
		0,
		1,
		1,
		1,
		2,
		2,
		2,
		2,
		2,
		0,
		0,
		1,
		1,
		1,
		2,
		2,
		2,
		2,
		2
	};
	/**
	 * Field _itCr8.
	 */
	private static int[] _itCr8 =
	{
		0,
		1,
		2,
		2
	};
	/**
	 * Field _id.
	 */
	private final int _id;
	/**
	 * Field hp_recovery.
	 */
	private final int hp_recovery;
	/**
	 * Field mp_recovery.
	 */
	private final int mp_recovery;
	/**
	 * Field exp_recovery.
	 */
	private final int exp_recovery;
	/**
	 * Field teleport.
	 */
	private final int teleport;
	/**
	 * Field curtains.
	 */
	private final int curtains;
	/**
	 * Field itemCreate.
	 */
	private final int itemCreate;
	/**
	 * Field support.
	 */
	private final int support;
	/**
	 * Field platform.
	 */
	private final int platform;
	
	/**
	 * Constructor for AgitDecoInfo.
	 * @param clanHall ClanHall
	 */
	public AgitDecoInfo(ClanHall clanHall)
	{
		_id = clanHall.getId();
		hp_recovery = getHpRecovery(clanHall.isFunctionActive(ResidenceFunction.RESTORE_HP) ? clanHall.getFunction(ResidenceFunction.RESTORE_HP).getLevel() : 0);
		mp_recovery = getMpRecovery(clanHall.isFunctionActive(ResidenceFunction.RESTORE_MP) ? clanHall.getFunction(ResidenceFunction.RESTORE_MP).getLevel() : 0);
		exp_recovery = getExpRecovery(clanHall.isFunctionActive(ResidenceFunction.RESTORE_EXP) ? clanHall.getFunction(ResidenceFunction.RESTORE_EXP).getLevel() : 0);
		teleport = clanHall.isFunctionActive(ResidenceFunction.TELEPORT) ? clanHall.getFunction(ResidenceFunction.TELEPORT).getLevel() : 0;
		curtains = clanHall.isFunctionActive(ResidenceFunction.CURTAIN) ? clanHall.getFunction(ResidenceFunction.CURTAIN).getLevel() : 0;
		itemCreate = clanHall.isFunctionActive(ResidenceFunction.ITEM_CREATE) ? _itCr8[clanHall.getFunction(ResidenceFunction.ITEM_CREATE).getLevel()] : 0;
		support = clanHall.isFunctionActive(ResidenceFunction.SUPPORT) ? _buff[clanHall.getFunction(ResidenceFunction.SUPPORT).getLevel()] : 0;
		platform = clanHall.isFunctionActive(ResidenceFunction.PLATFORM) ? clanHall.getFunction(ResidenceFunction.PLATFORM).getLevel() : 0;
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected final void writeImpl()
	{
		writeC(0xFD);
		writeD(_id);
		writeC(hp_recovery);
		writeC(mp_recovery);
		writeC(mp_recovery);
		writeC(exp_recovery);
		writeC(teleport);
		writeC(0);
		writeC(curtains);
		writeC(itemCreate);
		writeC(support);
		writeC(support);
		writeC(platform);
		writeC(itemCreate);
		writeD(0);
		writeD(0);
		writeD(0);
		writeD(0);
		writeD(0);
	}
	
	/**
	 * Method getHpRecovery.
	 * @param percent int
	 * @return int
	 */
	private static int getHpRecovery(int percent)
	{
		switch (percent)
		{
			case 0:
				return 0;
			case 20:
			case 40:
			case 80:
			case 120:
			case 140:
				return 1;
			case 160:
			case 180:
			case 200:
			case 220:
			case 240:
			case 260:
			case 280:
			case 300:
				return 2;
			default:
				_log.warn("Unsupported percent " + percent + " in hp recovery");
				return 0;
		}
	}
	
	/**
	 * Method getMpRecovery.
	 * @param percent int
	 * @return int
	 */
	private static int getMpRecovery(int percent)
	{
		switch (percent)
		{
			case 0:
				return 0;
			case 5:
			case 10:
			case 15:
			case 20:
				return 1;
			case 25:
			case 30:
			case 35:
			case 40:
			case 45:
			case 50:
				return 2;
			default:
				_log.warn("Unsupported percent " + percent + " in mp recovery");
				return 0;
		}
	}
	
	/**
	 * Method getExpRecovery.
	 * @param percent int
	 * @return int
	 */
	private static int getExpRecovery(int percent)
	{
		switch (percent)
		{
			case 0:
				return 0;
			case 5:
			case 10:
			case 15:
			case 20:
				return 1;
			case 25:
			case 30:
			case 35:
			case 40:
			case 45:
			case 50:
				return 2;
			default:
				_log.warn("Unsupported percent " + percent + " in exp recovery");
				return 0;
		}
	}
}
