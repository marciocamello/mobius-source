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

import java.util.ArrayList;
import java.util.List;

import lineage2.gameserver.data.xml.holder.PlayerTemplateHolder;
import lineage2.gameserver.model.base.ClassId;
import lineage2.gameserver.model.base.ClassLevel;
import lineage2.gameserver.model.base.Sex;
import lineage2.gameserver.templates.player.PlayerTemplate;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class NewCharacterSuccess extends L2GameServerPacket
{
	/**
	 * Field _chars.
	 */
	private final List<ClassId> _chars = new ArrayList<>();
	
	/**
	 * Constructor for NewCharacterSuccess.
	 */
	public NewCharacterSuccess()
	{
		for (ClassId classId : ClassId.VALUES)
		{
			if (classId.isOfLevel(ClassLevel.NONE))
			{
				_chars.add(classId);
			}
		}
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected final void writeImpl()
	{
		writeC(0x0d);
		writeD(_chars.size());
		for (ClassId temp : _chars)
		{
			PlayerTemplate template = PlayerTemplateHolder.getInstance().getPlayerTemplate(temp.getRace(), temp, Sex.MALE);
			writeD(temp.getRace().ordinal());
			writeD(temp.getId());
			writeD(0x46);
			writeD(template.getBaseAttr().getSTR());
			writeD(0x0a);
			writeD(0x46);
			writeD(template.getBaseAttr().getDEX());
			writeD(0x0a);
			writeD(0x46);
			writeD(template.getBaseAttr().getCON());
			writeD(0x0a);
			writeD(0x46);
			writeD(template.getBaseAttr().getINT());
			writeD(0x0a);
			writeD(0x46);
			writeD(template.getBaseAttr().getWIT());
			writeD(0x0a);
			writeD(0x46);
			writeD(template.getBaseAttr().getMEN());
			writeD(0x0a);
		}
	}
}
