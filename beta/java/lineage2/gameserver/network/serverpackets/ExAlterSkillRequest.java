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

public class ExAlterSkillRequest extends L2GameServerPacket
{
	private static final String _S__FE_113_EXALTERSKILLREQUEST = "[S] FE:113 ExAlterSkillRequest";
	private final int nextSkillId;
	private final int currentSkillId;
	private final int alterTime;
	
	public ExAlterSkillRequest(int nextSkillId, int currentSkillId, int alterTime)
	{
		this.nextSkillId = nextSkillId;
		this.currentSkillId = currentSkillId;
		this.alterTime = alterTime;
	}
	
	@Override
	protected final void writeImpl()
	{
		writeEx(0x10F);
		writeD(nextSkillId);
		writeD(currentSkillId);
		writeD(alterTime);
	}
	
	@Override
	public String getType()
	{
		return _S__FE_113_EXALTERSKILLREQUEST;
	}
}
