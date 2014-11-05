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

import lineage2.gameserver.model.Player;
import lineage2.gameserver.skills.effects.EffectCubic;

/**
 * @author blacksmoke
 */
public class ExUserInfoCubic extends L2GameServerPacket
{
	private final Player _activeChar;
	private final EffectCubic[] _cubics;
	
	public ExUserInfoCubic(Player activeChar)
	{
		_activeChar = activeChar;
		_cubics = activeChar.getCubics().toArray(new EffectCubic[activeChar.getCubics().size()]);
	}
	
	@Override
	protected void writeImpl()
	{
		writeEx(0x157);
		writeD(_activeChar.getObjectId());
		writeH(_cubics.length);
		for (EffectCubic cubic : _cubics)
		{
			writeH(cubic == null ? 0 : cubic.getId());
		}
		writeD(_activeChar.getAgathionId());
	}
	
}
