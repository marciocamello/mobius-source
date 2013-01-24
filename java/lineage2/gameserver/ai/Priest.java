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
package lineage2.gameserver.ai;

import lineage2.gameserver.model.instances.NpcInstance;

public class Priest extends DefaultAI
{
	public Priest(NpcInstance actor)
	{
		super(actor);
	}
	
	@Override
	protected boolean thinkActive()
	{
		return super.thinkActive() || defaultThinkBuff(10, 5);
	}
	
	@Override
	protected boolean createNewTask()
	{
		return defaultFightTask();
	}
	
	@Override
	public int getRatePHYS()
	{
		return 25;
	}
	
	@Override
	public int getRateDOT()
	{
		return 40;
	}
	
	@Override
	public int getRateDEBUFF()
	{
		return 40;
	}
	
	@Override
	public int getRateDAM()
	{
		return 75;
	}
	
	@Override
	public int getRateSTUN()
	{
		return 10;
	}
	
	@Override
	public int getRateBUFF()
	{
		return 25;
	}
	
	@Override
	public int getRateHEAL()
	{
		return 90;
	}
}
