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
package npc.model;

import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.templates.npc.NpcTemplate;

public final class ZakenCandleInstance extends NpcInstance
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final int OHS_Weapon = 15280;
	private static final int THS_Weapon = 15281;
	private static final int BOW_Weapon = 15302;
	private static final int Anchor = 32468;
	private boolean used = false;
	
	public ZakenCandleInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
		setRHandId(OHS_Weapon);
		_hasRandomAnimation = false;
	}
	
	@Override
	public void showChatWindow(Player player, int val, Object... arg)
	{
		Reflection r = getReflection();
		if (r.isDefault() || used)
		{
			return;
		}
		for (NpcInstance npc : getAroundNpc(1000, 100))
		{
			if (npc.getNpcId() == Anchor)
			{
				setRHandId(BOW_Weapon);
				broadcastCharInfo();
				used = true;
				return;
			}
		}
		setRHandId(THS_Weapon);
		broadcastCharInfo();
		used = true;
	}
	
	@Override
	public void onBypassFeedback(Player player, String command)
	{
	}
}
