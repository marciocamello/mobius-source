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
 * @version $Revision: 1.0 $
 */
public final class SupportMagic extends Functions
{
	private final static int minSupLvl = 1;
	private final static int maxSupLvl = 85;
	
	// @formatter:off
	private static final int[][] _mageBuff = new int[][] {{minSupLvl, maxSupLvl, 15642, 1}, {minSupLvl, maxSupLvl, 15643, 1}, {minSupLvl, maxSupLvl, 15644, 1}, {minSupLvl, maxSupLvl, 15645, 1}, {minSupLvl, maxSupLvl, 15646, 1}, {minSupLvl, maxSupLvl, 15647, 1}, {minSupLvl, maxSupLvl, 15651, 1}, {minSupLvl, maxSupLvl, 15652, 1}, {minSupLvl, maxSupLvl, 15653, 1}};
	private static final int[][] _warrBuff = new int[][] {{minSupLvl, maxSupLvl, 15642, 1}, {minSupLvl, maxSupLvl, 15643, 1}, {minSupLvl, maxSupLvl, 15644, 1}, {minSupLvl, maxSupLvl, 15645, 1}, {minSupLvl, maxSupLvl, 15646, 1}, {minSupLvl, maxSupLvl, 15647, 1}, {minSupLvl, maxSupLvl, 15651, 1}, {minSupLvl, maxSupLvl, 15652, 1}, {minSupLvl, maxSupLvl, 15653, 1}};
	private static final int[][] _summonBuff = new int[][] {{minSupLvl, maxSupLvl, 15642, 1}, {minSupLvl, maxSupLvl, 15643, 1}, {minSupLvl, maxSupLvl, 15644, 1}, {minSupLvl, maxSupLvl, 15645, 1}, {minSupLvl, maxSupLvl, 15646, 1}, {minSupLvl, maxSupLvl, 15647, 1}, {minSupLvl, maxSupLvl, 15651, 1}, {minSupLvl, maxSupLvl, 15652, 1}, {minSupLvl, maxSupLvl, 15653, 1}};
	// @formatter:on
	
	/**
	 * Method getSupportMagic.
	 */
	public void getSupportMagic()
	{
		Player player = getSelf();
		NpcInstance npc = getNpc();
		doSupportMagic(npc, player, false);
	}
	
	/**
	 * Method getSupportServitorMagic.
	 */
	public void getSupportServitorMagic()
	{
		Player player = getSelf();
		NpcInstance npc = getNpc();
		doSupportMagic(npc, player, true);
	}
	
	/**
	 * Method getProtectionBlessing.
	 */
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
	
	/**
	 * Method doSupportMagic.
	 * @param npc NpcInstance
	 * @param player Player
	 * @param servitor boolean
	 */
	public static void doSupportMagic(NpcInstance npc, Player player, boolean servitor)
	{
		if (player.isCursedWeaponEquipped())
		{
			return;
		}
		
		int lvl = player.getLevel();
		
		if (servitor && (player.getSummonList().getFirstServitor() == null))
		{
			show("default/newbie_nosupport_servitor.htm", player, npc);
			return;
		}
		
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
		
		if (servitor)
		{
			for (Summon summon : player.getSummonList())
			{
				target.add(summon);
				
				for (int[] buff : _summonBuff)
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
			
			if (!player.isMageClass() || (player.getTemplate().getRace() == Race.orc))
			{
				for (int[] buff : _warrBuff)
				{
					if ((lvl >= buff[0]) && (lvl <= buff[1]))
					{
						npc.broadcastPacket(new MagicSkillUse(npc, player, buff[2], buff[3], 0, 0));
						npc.callSkill(SkillTable.getInstance().getInfo(buff[2], buff[3]), target, true);
					}
				}
			}
			else
			{
				for (int[] buff : _mageBuff)
				{
					if ((lvl >= buff[0]) && (lvl <= buff[1]))
					{
						npc.broadcastPacket(new MagicSkillUse(npc, player, buff[2], buff[3], 0, 0));
						npc.callSkill(SkillTable.getInstance().getInfo(buff[2], buff[3]), target, true);
					}
				}
			}
		}
	}
}
