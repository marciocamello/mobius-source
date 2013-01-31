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

import lineage2.gameserver.model.actor.instances.player.Macro;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class SendMacroList extends L2GameServerPacket
{
	/**
	 * Field _rev.
	 */
	private final int _rev;
	/**
	 * Field _count.
	 */
	private final int _count;
	/**
	 * Field _macro.
	 */
	private final Macro _macro;
	
	/**
	 * Constructor for SendMacroList.
	 * @param rev int
	 * @param count int
	 * @param macro Macro
	 */
	public SendMacroList(int rev, int count, Macro macro)
	{
		_rev = rev;
		_count = count;
		_macro = macro;
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected final void writeImpl()
	{
		writeC(0xe8);
		writeD(_rev);
		writeC(0);
		writeC(_count);
		writeC(_macro != null ? 1 : 0);
		if (_macro != null)
		{
			writeD(_macro.id);
			writeS(_macro.name);
			writeS(_macro.descr);
			writeS(_macro.acronym);
			writeC(_macro.icon);
			writeC(_macro.commands.length);
			for (int i = 0; i < _macro.commands.length; i++)
			{
				Macro.L2MacroCmd cmd = _macro.commands[i];
				writeC(i + 1);
				writeC(cmd.type);
				writeD(cmd.d1);
				writeC(cmd.d2);
				writeS(cmd.cmd);
			}
		}
	}
}
