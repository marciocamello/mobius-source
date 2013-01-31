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

import lineage2.gameserver.model.pledge.Alliance;
import lineage2.gameserver.model.pledge.Clan;

import org.apache.commons.lang3.StringUtils;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class PledgeShowInfoUpdate extends L2GameServerPacket
{
	/**
	 * Field ally_id. Field crest_id. Field clan_rep. Field clan_rank. Field clan_level. Field clan_id.
	 */
	private final int clan_id, clan_level, clan_rank, clan_rep, crest_id, ally_id;
	/**
	 * Field ally_crest.
	 */
	private int ally_crest;
	/**
	 * Field atwar.
	 */
	private final int atwar;
	/**
	 * Field _territorySide.
	 */
	private final int _territorySide;
	/**
	 * Field ally_name.
	 */
	private String ally_name = StringUtils.EMPTY;
	/**
	 * Field HasFortress. Field HasHideout. Field HasCastle.
	 */
	private final int HasCastle, HasHideout, HasFortress;
	
	/**
	 * Constructor for PledgeShowInfoUpdate.
	 * @param clan Clan
	 */
	public PledgeShowInfoUpdate(final Clan clan)
	{
		clan_id = clan.getClanId();
		clan_level = clan.getLevel();
		HasCastle = clan.getCastle();
		HasHideout = clan.getHasHideout();
		HasFortress = clan.getHasFortress();
		clan_rank = clan.getRank();
		clan_rep = clan.getReputationScore();
		crest_id = clan.getCrestId();
		ally_id = clan.getAllyId();
		atwar = clan.isAtWar();
		_territorySide = clan.getWarDominion();
		Alliance ally = clan.getAlliance();
		if (ally != null)
		{
			ally_name = ally.getAllyName();
			ally_crest = ally.getAllyCrestId();
		}
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected final void writeImpl()
	{
		writeC(0x8e);
		writeD(clan_id);
		writeD(crest_id);
		writeD(clan_level);
		writeD(HasCastle);
		writeD(HasHideout);
		writeD(HasFortress);
		writeD(0);
		writeD(clan_rank);
		writeD(clan_rep);
		writeD(0);
		writeD(0);
		writeD(ally_id);
		writeS(ally_name);
		writeD(ally_crest);
		writeD(atwar);
		writeD(0x00);
		writeD(_territorySide);
	}
}
