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
package services;

import java.util.ArrayList;
import java.util.List;

import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Summon;
import lineage2.gameserver.model.base.ClassLevel;
import lineage2.gameserver.model.base.Race;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.MagicSkillUse;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.tables.SkillTable;

/**
 * @author Mobius
 */
public final class SupportMagic extends Functions
{
	private final static int minSupLvl = 1;
	private final static int maxSupLvl = 85;
	
	// @formatter:off
	private static final int[][] _buffList = new int[][] {{minSupLvl, maxSupLvl, 15642, 1}, {minSupLvl, maxSupLvl, 15643, 1}, {minSupLvl, maxSupLvl, 15644, 1}, {minSupLvl, maxSupLvl, 15645, 1}, {minSupLvl, maxSupLvl, 15646, 1}, {minSupLvl, maxSupLvl, 15647, 1}, {minSupLvl, maxSupLvl, 15651, 1}, {minSupLvl, maxSupLvl, 15652, 1}, {minSupLvl, maxSupLvl, 15653, 1}};
	private static final int[] _mageBuff = new int[] {minSupLvl, maxSupLvl, 15650, 1};
	private static final int[] _warrBuff = new int[] {minSupLvl, maxSupLvl, 15649, 1};
	private static final int[] _knightBuff = new int[] {minSupLvl, maxSupLvl, 15648, 1};
	// @formatter:on
	
	public static void getSupportMagic(NpcInstance npc, Player player)
	{
		if (!player.isMageClass() || (player.getTemplate().getRace() == Race.orc))
		{
			castSupportMagic(npc, player, _warrBuff, false);
		}
		else
		{
			castSupportMagic(npc, player, _mageBuff, false);
		}
	}
	
	public void getSupportMagicWizard()
	{
		Player player = getSelf();
		NpcInstance npc = getNpc();
		castSupportMagic(npc, player, _mageBuff, false);
	}
	
	public void getSupportMagicWarrior()
	{
		Player player = getSelf();
		NpcInstance npc = getNpc();
		castSupportMagic(npc, player, _warrBuff, false);
	}
	
	public void getSupportMagicKnight()
	{
		Player player = getSelf();
		NpcInstance npc = getNpc();
		castSupportMagic(npc, player, _knightBuff, false);
	}
	
	public static void getSupportServitorMagic(NpcInstance npc, Player player)
	{
		castSupportMagic(npc, player, null, true);
	}
	
	public void getProtectionBlessing()
	{
		Player player = getSelf();
		NpcInstance npc = getNpc();
		
		if (player.getKarma() > 0)
		{
			return;
		}
		
		if ((player.getLevel() > maxSupLvl) || player.getClassId().isOfLevel(ClassLevel.First))
		{
			show("default/newbie_blessing_no.htm", player, npc);
			return;
		}
		
		npc.doCast(SkillTable.getInstance().getInfo(5182, 1), player, true);
	}
	
	public void getNoblesseBlessing()
	{
		Player player = getSelf();
		NpcInstance npc = getNpc();
		
		if (player.getKarma() > 0)
		{
			return;
		}
		
		npc.doCast(SkillTable.getInstance().getInfo(1323, 1), player, true);
	}
	
	private static void castSupportMagic(NpcInstance npc, Player player, int[] extraBuff, Boolean isServitor)
	{
		if (player.isCursedWeaponEquipped())
		{
			return;
		}
		
		int lvl = player.getLevel();
		
		if (lvl < minSupLvl)
		{
			show("default/newbie_nosupport_min.htm", player, npc);
			return;
		}
		
		if (lvl > maxSupLvl)
		{
			show("default/newbie_nosupport_max.htm", player, npc);
			return;
		}
		
		List<Creature> target = new ArrayList<>();
		
		if (isServitor)
		{
			if (player.getSummonList().getFirstServitor() == null)
			{
				show("default/newbie_nosupport_servitor.htm", player, npc);
				return;
			}
			
			for (Summon summon : player.getSummonList())
			{
				target.add(summon);
				
				for (int[] buff : _buffList)
				{
					if ((lvl >= buff[0]) && (lvl <= buff[1]))
					{
						npc.broadcastPacket(new MagicSkillUse(npc, summon, buff[2], buff[3], 0, 0));
						npc.callSkill(SkillTable.getInstance().getInfo(buff[2], buff[3]), target, true);
					}
				}
			}
		}
		else
		{
			target.add(player);
			
			for (int[] buff : _buffList)
			{
				if ((lvl >= buff[0]) && (lvl <= buff[1]))
				{
					npc.broadcastPacket(new MagicSkillUse(npc, player, buff[2], buff[3], 0, 0));
					npc.callSkill(SkillTable.getInstance().getInfo(buff[2], buff[3]), target, true);
				}
			}
			
			if ((lvl >= extraBuff[0]) && (lvl <= extraBuff[1]))
			{
				npc.broadcastPacket(new MagicSkillUse(npc, player, extraBuff[2], extraBuff[3], 0, 0));
				npc.callSkill(SkillTable.getInstance().getInfo(extraBuff[2], extraBuff[3]), target, true);
			}
		}
	}
}