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
package lineage2.gameserver.model.pledge;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public enum Privilege
{
	FREE,
	CL_JOIN_CLAN,
	CL_GIVE_TITLE,
	CL_VIEW_WAREHOUSE,
	CL_MANAGE_RANKS,
	CL_PLEDGE_WAR,
	CL_DISMISS,
	CL_REGISTER_CREST,
	CL_APPRENTICE,
	CL_TROOPS_FAME,
	CL_SUMMON_AIRSHIP,
	CH_ENTER_EXIT,
	CH_USE_FUNCTIONS,
	CH_AUCTION,
	CH_DISMISS,
	CH_SET_FUNCTIONS,
	CS_FS_ENTER_EXIT,
	CS_FS_MANOR_ADMIN,
	CS_FS_SIEGE_WAR,
	CS_FS_USE_FUNCTIONS,
	CS_FS_DISMISS,
	CS_FS_MANAGER_TAXES,
	CS_FS_MERCENARIES,
	CS_FS_SET_FUNCTIONS;
	public static final int ALL = 16777214;
	public static final int NONE = 0;
	private final int _mask;
	
	/**
	 * Constructor for Privilege.
	 */
	Privilege()
	{
		_mask = ordinal() == 0 ? 0 : 1 << ordinal();
	}
	
	/**
	 * Method mask.
	 * @return int
	 */
	public int mask()
	{
		return _mask;
	}
}
