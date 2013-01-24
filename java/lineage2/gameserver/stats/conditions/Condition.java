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
package lineage2.gameserver.stats.conditions;

import lineage2.gameserver.network.serverpackets.components.SystemMsg;
import lineage2.gameserver.stats.Env;

public abstract class Condition
{
	public static final Condition[] EMPTY_ARRAY = new Condition[0];
	private SystemMsg _message;
	
	public final void setSystemMsg(int msgId)
	{
		_message = SystemMsg.valueOf(msgId);
	}
	
	public final SystemMsg getSystemMsg()
	{
		return _message;
	}
	
	public final boolean test(Env env)
	{
		return testImpl(env);
	}
	
	protected abstract boolean testImpl(Env env);
}
