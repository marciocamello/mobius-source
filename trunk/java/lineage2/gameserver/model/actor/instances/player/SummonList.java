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
package lineage2.gameserver.model.actor.instances.player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import lineage2.gameserver.Config;
import lineage2.gameserver.dao.EffectsDAO;
import lineage2.gameserver.dao.ServitorsDAO;
import lineage2.gameserver.data.xml.holder.NpcHolder;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.Summon;
import lineage2.gameserver.model.base.SummonType;
import lineage2.gameserver.model.instances.PetBabyInstance;
import lineage2.gameserver.model.instances.PetInstance;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.skills.skillclasses.SummonServitor;
import lineage2.gameserver.tables.PetDataTable;
import lineage2.gameserver.tables.SkillTable;
import lineage2.gameserver.templates.npc.NpcTemplate;
import lineage2.gameserver.utils.Location;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SummonList implements Iterable<Summon>
{
	public Player _owner;
	private PetInstance _pet;
	private final Map<Integer, Summon> _summonList;
	static final Logger _log = LoggerFactory.getLogger(SummonList.class);
	private int _usedPoints = 0;
	
	public SummonList(Player owner)
	{
		_owner = owner;
		_summonList = new HashMap<>(3);
	}
	
	public boolean canSummon(SummonType summonType, int summonPoint)
	{
		if ((_usedPoints + summonPoint) > _owner.getSummonPointMax())
		{
			return false;
		}
		if (summonType == SummonType.PET)
		{
			return _pet == null;
		}
		synchronized (_summonList)
		{
			if ((summonType == SummonType.SERVITOR) && (_summonList.size() > 0))
			{
				return false;
			}
			if (summonType == SummonType.MULTI_SERVITOR)
			{
				if (_summonList.size() >= 4)
				{
					return false;
				}
				Summon summon = getFirstServitor();
				if (summon != null)
				{
					Skill skill = SkillTable.getInstance().getInfo(summon.getSummonSkillId(), summon.getSummonSkillLvl());
					if (skill != null)
					{
						return (skill instanceof SummonServitor) && (((SummonServitor) skill).getSummonType() == SummonType.MULTI_SERVITOR);
					}
				}
			}
		}
		return true;
	}
	
	public void addSummon(Summon summon)
	{
		if (summon.isServitor())
		{
			synchronized (_summonList)
			{
				_summonList.put(summon.getObjectId(), summon);
			}
			_usedPoints += summon.getSummonPoint();
		}
		else if (summon.isPet())
		{
			_pet = (PetInstance) summon;
		}
		summon.setTitle(_owner.getName());
		_owner.autoShot();
	}
	
	public void unsummonAll(boolean logout)
	{
		unsummonPet(logout);
		unsummonAllServitors();
	}
	
	public void unsummonPet(boolean logout)
	{
		if (_pet != null)
		{
			if (!logout)
			{
				_owner.unsetVar("petss@");
			}
			_pet.unSummon();
			_pet = null;
			_owner.setPetControlItem(null);
		}
	}
	
	public void unsummonAllServitors()
	{
		synchronized (_summonList)
		{
			for (Summon summon : _summonList.values())
			{
				summon.unSummon();
			}
			_summonList.clear();
		}
		_usedPoints = 0;
	}
	
	public int getUsedPoints()
	{
		return _usedPoints;
	}
	
	public boolean isInCombat()
	{
		boolean isCombat = false;
		if (_summonList.size() > 0)
		{
			synchronized (_summonList)
			{
				for (Summon summon : _summonList.values())
				{
					if (summon.isInCombat())
					{
						isCombat = true;
						break;
					}
				}
			}
		}
		return isCombat || ((_pet != null) && _pet.isInCombat());
	}
	
	public void store(boolean storeServitors)
	{
		if (storeServitors)
		{
			if (_summonList.size() > 0)
			{
				synchronized (_summonList)
				{
					for (Summon summon : _summonList.values())
					{
						ServitorsDAO.getInstance().store(summon);
						summon.saveEffects();
					}
				}
			}
		}
		if (_pet != null)
		{
			_owner.setVar("petss@", _owner.getPetControlItem().getObjectId(), -1);
			_pet.saveEffects();
			_pet.store();
		}
	}
	
	public void restore()
	{
		restoreServitors();
		restorePet();
	}
	
	public void restorePet()
	{
		int controlItemId = _owner.getVarInt("petss@");
		if (controlItemId > 0)
		{
			ItemInstance controlItem = _owner.getInventory().getItemByObjectId(controlItemId);
			if (controlItem == null)
			{
				return;
			}
			_owner.setPetControlItem(controlItem);
			int npcId = PetDataTable.getSummonId(controlItem);
			if (npcId == 0)
			{
				return;
			}
			NpcTemplate petTemplate = NpcHolder.getInstance().getTemplate(npcId);
			if (petTemplate == null)
			{
				return;
			}
			PetInstance pet = PetInstance.restore(controlItem, petTemplate, _owner);
			if (pet == null)
			{
				return;
			}
			if (!pet.isRespawned())
			{
				pet.setCurrentHp(pet.getMaxHp(), false);
				pet.setCurrentMp(pet.getMaxMp());
				pet.setCurrentFed(pet.getMaxFed());
				pet.updateControlItem();
				pet.store();
			}
			pet.getInventory().restore();
			addSummon(pet);
		}
	}
	
	public void restoreServitors()
	{
		List<Summon> summons = ServitorsDAO.getInstance().restore(_owner);
		if (summons.size() == 0)
		{
			return;
		}
		synchronized (_summonList)
		{
			for (Summon summon : summons)
			{
				addSummon(summon);
			}
		}
	}
	
	public void summonAll()
	{
		if (_summonList.size() > 0)
		{
			synchronized (_summonList)
			{
				for (Summon summon : _summonList.values())
				{
					EffectsDAO.getInstance().restoreEffects(summon);
					summon.setNonAggroTime(System.currentTimeMillis() + Config.NONAGGRO_TIME_ONTELEPORT);
					summon.setReflection(_owner.getReflection());
					summon.spawnMe(Location.findPointToStay(_owner, 50, 70));
					summon.setRunning();
					summon.setFollowMode(true);
				}
			}
		}
		if (_pet != null)
		{
			if (!_pet.isRespawned())
			{
				_pet.setCurrentHp(_pet.getMaxHp(), false);
				_pet.setCurrentMp(_pet.getMaxMp());
				_pet.setCurrentFed(_pet.getMaxFed());
				_pet.updateControlItem();
				_pet.store();
			}
			_pet.getInventory().restore();
			_pet.setNonAggroTime(System.currentTimeMillis() + Config.NONAGGRO_TIME_ONTELEPORT);
			_pet.setReflection(_owner.getReflection());
			_pet.spawnMe(Location.findPointToStay(_owner, 50, 70));
			_pet.setRunning();
			_pet.setFollowMode(true);
			_pet.getInventory().validateItems();
			if (_pet instanceof PetBabyInstance)
			{
				((PetBabyInstance) _pet).startBuffTask();
			}
		}
	}
	
	public PetInstance getPet()
	{
		return _pet;
	}
	
	public Summon getFirstServitor()
	{
		if (_summonList.size() == 1)
		{
			Summon summon = _summonList.values().iterator().next();
			Skill skill = SkillTable.getInstance().getInfo(summon.getSummonSkillId(), summon.getSummonSkillLvl());
			if ((skill == null) || (skill.getSkillType() != Skill.SkillType.SUMMON) || (((SummonServitor) skill).getSummonType() == SummonType.MULTI_SERVITOR))
			{
				return null;
			}
			return summon;
		}
		return null;
	}
	
	public Summon getSecondServitor()
	{
		if (_summonList.size() == 1)
		{
			Summon summon = _summonList.values().iterator().next();
			Skill skill = SkillTable.getInstance().getInfo(summon.getSummonSkillId(), summon.getSummonSkillLvl());
			if ((skill == null) || (skill.getSkillType() != Skill.SkillType.SUMMON))
			{
				return null;
			}
			return summon;
		}
		return null;
	}
	
	public List<Summon> getServitors()
	{
		if (_summonList.size() > 0)
		{
			List<Summon> servitors = new ArrayList<>();
			synchronized (_summonList)
			{
				for (Summon summon : _summonList.values())
				{
					servitors.add(summon);
				}
			}
			return servitors;
		}
		return Collections.emptyList();
	}
	
	public boolean contains(Creature creature)
	{
		if (creature == null)
		{
			return false;
		}
		if (_pet == creature)
		{
			return true;
		}
		if (_summonList.size() == 0)
		{
			return false;
		}
		synchronized (_summonList)
		{
			for (Summon summon : _summonList.values())
			{
				if (summon == creature)
				{
					return true;
				}
			}
		}
		return false;
	}
	
	@Override
	public Iterator<Summon> iterator()
	{
		List<Summon> summons = new ArrayList<>(4);
		if (_pet != null)
		{
			summons.add(_pet);
		}
		if (_summonList.size() > 0)
		{
			synchronized (_summonList)
			{
				summons.addAll(_summonList.values());
			}
		}
		return Collections.unmodifiableList(summons).iterator();
	}
	
	public int size()
	{
		return _summonList.size() + (_pet != null ? 1 : 0);
	}
}
