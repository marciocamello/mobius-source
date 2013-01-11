/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package lineage2.gameserver.model;

import gnu.trove.map.hash.TIntObjectHashMap;
import gnu.trove.set.hash.TIntHashSet;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import lineage2.gameserver.model.items.Inventory;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.tables.SkillTable;

public final class ArmorSet
{
	private final TIntHashSet _chests = new TIntHashSet();
	private final TIntHashSet _legs = new TIntHashSet();
	private final TIntHashSet _head = new TIntHashSet();
	private final TIntHashSet _gloves = new TIntHashSet();
	private final TIntHashSet _feet = new TIntHashSet();
	private final TIntHashSet _shield = new TIntHashSet();
	private final TIntObjectHashMap<List<Skill>> _skills = new TIntObjectHashMap<>();
	private final List<Skill> _shieldSkills = new ArrayList<>();
	private final List<Skill> _enchant6skills = new ArrayList<>();
	
	public ArmorSet(String[] chests, String[] legs, String[] head, String[] gloves, String[] feet, String[] shield, String[] shield_skills, String[] enchant6skills)
	{
		_chests.addAll(parseItemIDs(chests));
		_legs.addAll(parseItemIDs(legs));
		_head.addAll(parseItemIDs(head));
		_gloves.addAll(parseItemIDs(gloves));
		_feet.addAll(parseItemIDs(feet));
		_shield.addAll(parseItemIDs(shield));
		if (shield_skills != null)
		{
			for (String skill : shield_skills)
			{
				StringTokenizer st = new StringTokenizer(skill, "-");
				if (st.hasMoreTokens())
				{
					int skillId = Integer.parseInt(st.nextToken());
					int skillLvl = Integer.parseInt(st.nextToken());
					_shieldSkills.add(SkillTable.getInstance().getInfo(skillId, skillLvl));
				}
			}
		}
		if (enchant6skills != null)
		{
			for (String skill : enchant6skills)
			{
				StringTokenizer st = new StringTokenizer(skill, "-");
				if (st.hasMoreTokens())
				{
					int skillId = Integer.parseInt(st.nextToken());
					int skillLvl = Integer.parseInt(st.nextToken());
					_enchant6skills.add(SkillTable.getInstance().getInfo(skillId, skillLvl));
				}
			}
		}
	}
	
	private static int[] parseItemIDs(String[] items)
	{
		TIntHashSet result = new TIntHashSet();
		if (items != null)
		{
			for (String s_id : items)
			{
				int id = Integer.parseInt(s_id);
				if (id > 0)
				{
					result.add(id);
				}
			}
		}
		return result.toArray();
	}
	
	public void addSkills(int partsCount, String[] skills)
	{
		List<Skill> skillList = new ArrayList<>();
		if (skills != null)
		{
			for (String skill : skills)
			{
				StringTokenizer st = new StringTokenizer(skill, "-");
				if (st.hasMoreTokens())
				{
					int skillId = Integer.parseInt(st.nextToken());
					int skillLvl = Integer.parseInt(st.nextToken());
					skillList.add(SkillTable.getInstance().getInfo(skillId, skillLvl));
				}
			}
		}
		_skills.put(partsCount, skillList);
	}
	
	public boolean containAll(Player player)
	{
		Inventory inv = player.getInventory();
		ItemInstance chestItem = inv.getPaperdollItem(Inventory.PAPERDOLL_CHEST);
		ItemInstance legsItem = inv.getPaperdollItem(Inventory.PAPERDOLL_LEGS);
		ItemInstance headItem = inv.getPaperdollItem(Inventory.PAPERDOLL_HEAD);
		ItemInstance glovesItem = inv.getPaperdollItem(Inventory.PAPERDOLL_GLOVES);
		ItemInstance feetItem = inv.getPaperdollItem(Inventory.PAPERDOLL_FEET);
		int chest = 0;
		int legs = 0;
		int head = 0;
		int gloves = 0;
		int feet = 0;
		if (chestItem != null)
		{
			chest = chestItem.getItemId();
		}
		if (legsItem != null)
		{
			legs = legsItem.getItemId();
		}
		if (headItem != null)
		{
			head = headItem.getItemId();
		}
		if (glovesItem != null)
		{
			gloves = glovesItem.getItemId();
		}
		if (feetItem != null)
		{
			feet = feetItem.getItemId();
		}
		return containAll(chest, legs, head, gloves, feet);
	}
	
	public boolean containAll(int chest, int legs, int head, int gloves, int feet)
	{
		if (!_chests.isEmpty() && !_chests.contains(chest))
		{
			return false;
		}
		if (!_legs.isEmpty() && !_legs.contains(legs))
		{
			return false;
		}
		if (!_head.isEmpty() && !_head.contains(head))
		{
			return false;
		}
		if (!_gloves.isEmpty() && !_gloves.contains(gloves))
		{
			return false;
		}
		if (!_feet.isEmpty() && !_feet.contains(feet))
		{
			return false;
		}
		return true;
	}
	
	public boolean containItem(int slot, int itemId)
	{
		switch (slot)
		{
			case Inventory.PAPERDOLL_CHEST:
				return _chests.contains(itemId);
			case Inventory.PAPERDOLL_LEGS:
				return _legs.contains(itemId);
			case Inventory.PAPERDOLL_HEAD:
				return _head.contains(itemId);
			case Inventory.PAPERDOLL_GLOVES:
				return _gloves.contains(itemId);
			case Inventory.PAPERDOLL_FEET:
				return _feet.contains(itemId);
			default:
				return false;
		}
	}
	
	public int getEquipedSetPartsCount(Player player)
	{
		Inventory inv = player.getInventory();
		ItemInstance chestItem = inv.getPaperdollItem(Inventory.PAPERDOLL_CHEST);
		ItemInstance legsItem = inv.getPaperdollItem(Inventory.PAPERDOLL_LEGS);
		ItemInstance headItem = inv.getPaperdollItem(Inventory.PAPERDOLL_HEAD);
		ItemInstance glovesItem = inv.getPaperdollItem(Inventory.PAPERDOLL_GLOVES);
		ItemInstance feetItem = inv.getPaperdollItem(Inventory.PAPERDOLL_FEET);
		int chest = 0;
		int legs = 0;
		int head = 0;
		int gloves = 0;
		int feet = 0;
		if (chestItem != null)
		{
			chest = chestItem.getItemId();
		}
		if (legsItem != null)
		{
			legs = legsItem.getItemId();
		}
		if (headItem != null)
		{
			head = headItem.getItemId();
		}
		if (glovesItem != null)
		{
			gloves = glovesItem.getItemId();
		}
		if (feetItem != null)
		{
			feet = feetItem.getItemId();
		}
		int result = 0;
		if (!_chests.isEmpty() && _chests.contains(chest))
		{
			result++;
		}
		if (!_legs.isEmpty() && _legs.contains(legs))
		{
			result++;
		}
		if (!_head.isEmpty() && _head.contains(head))
		{
			result++;
		}
		if (!_gloves.isEmpty() && _gloves.contains(gloves))
		{
			result++;
		}
		if (!_feet.isEmpty() && _feet.contains(feet))
		{
			result++;
		}
		return result;
	}
	
	public List<Skill> getSkills(int partsCount)
	{
		if (_skills.get(partsCount) == null)
		{
			return new ArrayList<>();
		}
		return _skills.get(partsCount);
	}
	
	public List<Skill> getSkillsToRemove()
	{
		List<Skill> result = new ArrayList<>();
		for (int i : _skills.keys())
		{
			List<Skill> skills = _skills.get(i);
			if (skills != null)
			{
				for (Skill skill : skills)
				{
					result.add(skill);
				}
			}
		}
		return result;
	}
	
	public List<Skill> getShieldSkills()
	{
		return _shieldSkills;
	}
	
	public List<Skill> getEnchant6skills()
	{
		return _enchant6skills;
	}
	
	public boolean containShield(Player player)
	{
		Inventory inv = player.getInventory();
		ItemInstance shieldItem = inv.getPaperdollItem(Inventory.PAPERDOLL_LHAND);
		if ((shieldItem != null) && _shield.contains(shieldItem.getItemId()))
		{
			return true;
		}
		return false;
	}
	
	public boolean containShield(int shield_id)
	{
		if (_shield.isEmpty())
		{
			return false;
		}
		return _shield.contains(shield_id);
	}
	
	public boolean isEnchanted6(Player player)
	{
		if (!containAll(player))
		{
			return false;
		}
		Inventory inv = player.getInventory();
		ItemInstance chestItem = inv.getPaperdollItem(Inventory.PAPERDOLL_CHEST);
		ItemInstance legsItem = inv.getPaperdollItem(Inventory.PAPERDOLL_LEGS);
		ItemInstance headItem = inv.getPaperdollItem(Inventory.PAPERDOLL_HEAD);
		ItemInstance glovesItem = inv.getPaperdollItem(Inventory.PAPERDOLL_GLOVES);
		ItemInstance feetItem = inv.getPaperdollItem(Inventory.PAPERDOLL_FEET);
		if (!_chests.isEmpty() && (chestItem.getEnchantLevel() < 6))
		{
			return false;
		}
		if (!_legs.isEmpty() && (legsItem.getEnchantLevel() < 6))
		{
			return false;
		}
		if (!_gloves.isEmpty() && (glovesItem.getEnchantLevel() < 6))
		{
			return false;
		}
		if (!_head.isEmpty() && (headItem.getEnchantLevel() < 6))
		{
			return false;
		}
		if (!_feet.isEmpty() && (feetItem.getEnchantLevel() < 6))
		{
			return false;
		}
		return true;
	}
	
	public int[] getChestIds()
	{
		return _chests.toArray();
	}
	
	public int[] getLegIds()
	{
		return _legs.toArray();
	}
	
	public int[] getHeadIds()
	{
		return _head.toArray();
	}
	
	public int[] getGlovesIds()
	{
		return _gloves.toArray();
	}
	
	public int[] getFeetIds()
	{
		return _feet.toArray();
	}
	
	public int[] getShieldIds()
	{
		return _shield.toArray();
	}
}
