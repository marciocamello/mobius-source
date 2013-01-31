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
package lineage2.gameserver.data.xml.holder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lineage2.commons.collections.GArray;
import lineage2.commons.data.xml.AbstractHolder;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.SkillLearn;
import lineage2.gameserver.model.base.AcquireType;
import lineage2.gameserver.model.base.ClassId;
import lineage2.gameserver.model.pledge.Clan;
import lineage2.gameserver.model.pledge.SubUnit;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class SkillAcquireHolder extends AbstractHolder
{
	/**
	 * Field _instance.
	 */
	private static final SkillAcquireHolder _instance = new SkillAcquireHolder();
	
	/**
	 * Method getInstance.
	 * @return SkillAcquireHolder
	 */
	public static SkillAcquireHolder getInstance()
	{
		return _instance;
	}
	
	/**
	 * Field _normalSkillTree.
	 */
	private static HashMap<Integer, List<SkillLearn>> _normalSkillTree = new HashMap<>();
	/**
	 * Field _transferSkillTree.
	 */
	private static HashMap<Integer, List<SkillLearn>> _transferSkillTree = new HashMap<>();
	/**
	 * Field _fishingSkillTree.
	 */
	private static HashMap<Integer, List<SkillLearn>> _fishingSkillTree = new HashMap<>();
	/**
	 * Field _transformationSkillTree.
	 */
	private static HashMap<Integer, List<SkillLearn>> _transformationSkillTree = new HashMap<>();
	/**
	 * Field _certificationSkillTree.
	 */
	private static GArray<SkillLearn> _certificationSkillTree = new GArray<>();
	/**
	 * Field _collectionSkillTree.
	 */
	private static GArray<SkillLearn> _collectionSkillTree = new GArray<>();
	/**
	 * Field _pledgeSkillTree.
	 */
	private static GArray<SkillLearn> _pledgeSkillTree = new GArray<>();
	/**
	 * Field _subUnitSkillTree.
	 */
	private static GArray<SkillLearn> _subUnitSkillTree = new GArray<>();
	
	/**
	 * Method getMinLevelForNewSkill.
	 * @param player Player
	 * @param type AcquireType
	 * @return int
	 */
	public int getMinLevelForNewSkill(Player player, AcquireType type)
	{
		GArray<SkillLearn> skills = new GArray<>();
		switch (type)
		{
			case NORMAL:
			{
				skills.addAll(_normalSkillTree.get(player.getActiveClassId()));
				if (skills.isEmpty())
				{
					info("skill tree for class " + player.getActiveClassId() + " is not defined !");
					return 0;
				}
			}
				break;
			case TRANSFORMATION:
				skills.addAll(_transformationSkillTree.get(player.getRace().ordinal()));
				if (skills.isEmpty())
				{
					info("skill tree for race " + player.getRace().ordinal() + " is not defined !");
					return 0;
				}
				break;
			case FISHING:
				skills.addAll(_fishingSkillTree.get(player.getRace().ordinal()));
				if (skills.isEmpty())
				{
					info("skill tree for race " + player.getRace().ordinal() + " is not defined !");
					return 0;
				}
				break;
			default:
				return 0;
		}
		int minlevel = 0;
		for (SkillLearn temp : skills)
		{
			if (temp.getMinLevel() > player.getLevel())
			{
				if ((minlevel == 0) || (temp.getMinLevel() < minlevel))
				{
					minlevel = temp.getMinLevel();
				}
			}
		}
		return minlevel;
	}
	
	/**
	 * Method getAvailableSkills.
	 * @param player Player
	 * @param type AcquireType
	 * @return Collection<SkillLearn>
	 */
	public Collection<SkillLearn> getAvailableSkills(Player player, AcquireType type)
	{
		return getAvailableSkills(player, type, null);
	}
	
	/**
	 * Method getAvailableSkills.
	 * @param player Player
	 * @param type AcquireType
	 * @param subUnit SubUnit
	 * @return Collection<SkillLearn>
	 */
	public Collection<SkillLearn> getAvailableSkills(Player player, AcquireType type, SubUnit subUnit)
	{
		GArray<SkillLearn> skills = new GArray<>();
		switch (type)
		{
			case NORMAL:
			{
				skills.addAll(_normalSkillTree.get(player.getActiveClassId()));
				if (skills.isEmpty())
				{
					info("skill tree for class " + player.getActiveClassId() + " is not defined !");
					return Collections.emptyList();
				}
				return getAvaliableList(skills, player.getAllSkillsArray(), player.getLevel());
			}
			case COLLECTION:
				skills.addAll(_collectionSkillTree);
				if (skills.isEmpty())
				{
					info("skill tree for class " + player.getActiveClassId() + " is not defined !");
					return Collections.emptyList();
				}
				return getAvaliableList(skills, player.getAllSkillsArray(), player.getLevel());
			case TRANSFORMATION:
				skills.addAll(_transformationSkillTree.get(player.getRace().ordinal()));
				if (skills.isEmpty())
				{
					info("skill tree for race " + player.getRace().ordinal() + " is not defined !");
					return Collections.emptyList();
				}
				return getAvaliableList(skills, player.getAllSkillsArray(), player.getLevel());
			case TRANSFER_EVA_SAINTS:
			case TRANSFER_SHILLIEN_SAINTS:
			case TRANSFER_CARDINAL:
				skills.addAll(_transferSkillTree.get(type.transferClassId()));
				if (skills.isEmpty())
				{
					info("skill tree for class " + type.transferClassId() + " is not defined !");
					return Collections.emptyList();
				}
				if (player == null)
				{
					return skills;
				}
				List<SkillLearn> skillLearnMap = new ArrayList<>();
				for (SkillLearn temp : skills)
				{
					if (temp.getMinLevel() <= player.getLevel())
					{
						int knownLevel = player.getSkillLevel(temp.getId());
						if (knownLevel == -1)
						{
							skillLearnMap.add(temp);
						}
					}
				}
				return skillLearnMap;
			case FISHING:
				skills.addAll(_fishingSkillTree.get(player.getRace().ordinal()));
				if (skills.isEmpty())
				{
					info("skill tree for race " + player.getRace().ordinal() + " is not defined !");
					return Collections.emptyList();
				}
				return getAvaliableList(skills, player.getAllSkillsArray(), player.getLevel());
			case CLAN:
				skills.addAll(_pledgeSkillTree);
				Collection<Skill> skls = player.getClan().getSkills();
				return getAvaliableList(skills, skls.toArray(new Skill[skls.size()]), player.getClan().getLevel());
			case SUB_UNIT:
				skills.addAll(_subUnitSkillTree);
				Collection<Skill> st = subUnit.getSkills();
				return getAvaliableList(skills, st.toArray(new Skill[st.size()]), player.getClan().getLevel());
			case CERTIFICATION:
				skills.addAll(_certificationSkillTree);
				if (player == null)
				{
					return skills;
				}
				return getAvaliableList(skills, player.getAllSkillsArray(), player.getLevel());
			default:
				return Collections.emptyList();
		}
	}
	
	/**
	 * Method getAvailableAllSkills.
	 * @param player Player
	 * @return Collection<SkillLearn>
	 */
	public Collection<SkillLearn> getAvailableAllSkills(Player player)
	{
		GArray<SkillLearn> skills = new GArray<>();
		skills.addAll(_normalSkillTree.get(player.getActiveClassId()));
		
		if (skills.isEmpty())
		{
			info("skill tree for class " + player.getActiveClassId() + " is not defined !");
			return Collections.emptyList();
		}
		return getAvaliableAllList(skills, player.getAllSkillsArray(), player.getLevel());
	}
	
	/**
	 * Method getAvailableAllSkillsForDellet.
	 * @param player Player
	 * @param newClassId int
	 * @return Collection<SkillLearn>
	 */
	public Collection<SkillLearn> getAvailableAllSkillsForDellet(Player player, int newClassId)
	{
		GArray<SkillLearn> skills = new GArray<>();
		skills.addAll(_normalSkillTree.get(newClassId));
		
		if (skills.isEmpty())
		{
			info("skill tree for class " + newClassId + " is not defined !");
			return Collections.emptyList();
		}
		return getAvaliableAllList(skills, player.getAllSkillsArray(), player.getLevel());
	}
	
	/**
	 * Method getAvaliableAllList.
	 * @param skillLearns Collection<SkillLearn>
	 * @param skills Skill[]
	 * @param level int
	 * @return Collection<SkillLearn>
	 */
	private Collection<SkillLearn> getAvaliableAllList(Collection<SkillLearn> skillLearns, Skill[] skills, int level)
	{
		Set<SkillLearn> skillLearnMap = new HashSet<>();
		loop:
		for (SkillLearn temp : skillLearns)
		{
			for (Skill s : skills)
			{
				if (temp.getId() == s.getId())
				{
					if ((temp.getLevel() - 1) == s.getLevel())
					{
						skillLearnMap.add(temp);
					}
					continue loop;
				}
				if (s.isRelationSkill())
				{
					for (int ds : s.getRelationSkills())
					{
						if (temp.getId() == ds)
						{
							continue loop;
						}
					}
				}
				
			}
			if (temp.getLevel() == 1)
			{
				skillLearnMap.add(temp);
			}
		}
		
		return skillLearnMap;
	}
	
	/**
	 * Method getAvaliableList.
	 * @param skillLearns Collection<SkillLearn>
	 * @param skills Skill[]
	 * @param level int
	 * @return Collection<SkillLearn>
	 */
	private Collection<SkillLearn> getAvaliableList(final Collection<SkillLearn> skillLearns, Skill[] skills, int level)
	{
		Set<SkillLearn> skillLearnMap = new HashSet<>();
		for (SkillLearn temp : skillLearns)
		{
			if (temp.getMinLevel() <= level)
			{
				boolean knownSkill = false;
				m:
				for (int j = 0; (j < skills.length) && !knownSkill; j++)
				{
					if (skills[j].isRelationSkill())
					{
						for (int _k : skills[j].getRelationSkills())
						{
							if (temp.getId() == _k)
							{
								knownSkill = true;
								break m;
							}
						}
					}
					if (skills[j].getId() == temp.getId())
					{
						knownSkill = true;
						if (skills[j].getLevel() == (temp.getLevel() - 1))
						{
							skillLearnMap.add(temp);
						}
					}
				}
				if (!knownSkill && (temp.getLevel() == 1))
				{
					skillLearnMap.add(temp);
				}
			}
		}
		
		return skillLearnMap;
	}
	
	/**
	 * Method getSkillLearn.
	 * @param player Player
	 * @param id int
	 * @param level int
	 * @param type AcquireType
	 * @return SkillLearn
	 */
	public SkillLearn getSkillLearn(Player player, int id, int level, AcquireType type)
	{
		GArray<SkillLearn> skills = new GArray<>();
		switch (type)
		{
			case NORMAL:
				skills.addAll(_normalSkillTree.get(player.getActiveClassId()));
				
				break;
			case COLLECTION:
				skills.addAll(_collectionSkillTree);
				break;
			case TRANSFORMATION:
				skills.addAll(_transformationSkillTree.get(player.getRace().ordinal()));
				break;
			case TRANSFER_CARDINAL:
			case TRANSFER_SHILLIEN_SAINTS:
			case TRANSFER_EVA_SAINTS:
				skills.addAll(_transferSkillTree.get(player.getActiveClassId()));
				break;
			case FISHING:
				skills.addAll(_fishingSkillTree.get(player.getRace().ordinal()));
				break;
			case CLAN:
				skills.addAll(_pledgeSkillTree);
				break;
			case SUB_UNIT:
				skills.addAll(_subUnitSkillTree);
				break;
			case CERTIFICATION:
				skills.addAll(_certificationSkillTree);
				break;
			default:
				return null;
		}
		
		if (skills.isEmpty())
		{
			return null;
		}
		
		for (SkillLearn temp : skills)
		{
			if ((temp.getLevel() == level) && (temp.getId() == id))
			{
				return temp;
			}
		}
		
		return null;
	}
	
	/**
	 * Method isSkillPossible.
	 * @param player Player
	 * @param skill Skill
	 * @param type AcquireType
	 * @return boolean
	 */
	public boolean isSkillPossible(Player player, Skill skill, AcquireType type)
	{
		Clan clan = null;
		GArray<SkillLearn> skills = new GArray<>();
		switch (type)
		{
			case NORMAL:
				skills.addAll(_normalSkillTree.get(player.getActiveClassId()));
				break;
			case COLLECTION:
				skills.addAll(_collectionSkillTree);
				break;
			case TRANSFORMATION:
				skills.addAll(_transformationSkillTree.get(player.getRace().ordinal()));
				break;
			case FISHING:
				skills.addAll(_fishingSkillTree.get(player.getRace().ordinal()));
				break;
			case TRANSFER_CARDINAL:
			case TRANSFER_EVA_SAINTS:
			case TRANSFER_SHILLIEN_SAINTS:
				int transferId = type.transferClassId();
				if (player.getActiveClassId() != transferId)
				{
					return false;
				}
				
				skills.addAll(_transferSkillTree.get(transferId));
				break;
			case CLAN:
				clan = player.getClan();
				if (clan == null)
				{
					return false;
				}
				skills.addAll(_pledgeSkillTree);
				break;
			case SUB_UNIT:
				clan = player.getClan();
				if (clan == null)
				{
					return false;
				}
				
				skills.addAll(_subUnitSkillTree);
				break;
			case CERTIFICATION:
				skills.addAll(_certificationSkillTree);
				break;
			default:
				return false;
		}
		
		return isSkillPossible(skills, skill);
	}
	
	/**
	 * Method isSkillPossible.
	 * @param skills Collection<SkillLearn>
	 * @param skill Skill
	 * @return boolean
	 */
	private boolean isSkillPossible(final Collection<SkillLearn> skills, Skill skill)
	{
		for (SkillLearn learn : skills)
		{
			if ((learn.getId() == skill.getId()) && (learn.getLevel() <= skill.getLevel()))
			{
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Method isSkillPossible.
	 * @param player Player
	 * @param skill Skill
	 * @return boolean
	 */
	public boolean isSkillPossible(Player player, Skill skill)
	{
		for (AcquireType aq : AcquireType.VALUES)
		{
			if (isSkillPossible(player, skill, aq))
			{
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Method getSkillLearnListByItemId.
	 * @param player Player
	 * @param itemId int
	 * @return List<SkillLearn>
	 */
	public List<SkillLearn> getSkillLearnListByItemId(Player player, int itemId)
	{
		List<SkillLearn> learns = _normalSkillTree.get(player.getActiveClassId());
		if (learns == null)
		{
			return Collections.emptyList();
		}
		
		List<SkillLearn> l = new ArrayList<>(1);
		for (SkillLearn $i : learns)
		{
			if ($i.getItemId() == itemId)
			{
				l.add($i);
			}
		}
		
		return l;
	}
	
	/**
	 * Method getAllNormalSkillTreeWithForgottenScrolls.
	 * @return List<SkillLearn>
	 */
	public List<SkillLearn> getAllNormalSkillTreeWithForgottenScrolls()
	{
		List<SkillLearn> a = new ArrayList<>();
		for (List<SkillLearn> i : _normalSkillTree.values())
		{
			for (SkillLearn learn : i.toArray(new SkillLearn[i.size()]))
			{
				if ((learn.getItemId() > 0) && learn.isClicked())
				{
					a.add(learn);
				}
			}
		}
		
		return a;
	}
	
	/**
	 * Method addAllNormalSkillLearns.
	 * @param map HashMap<Integer,List<SkillLearn>>
	 */
	public void addAllNormalSkillLearns(HashMap<Integer, List<SkillLearn>> map)
	{
		int classID;
		
		for (ClassId classId : ClassId.VALUES)
		{
			if (classId.name().startsWith("dummyEntry"))
			{
				continue;
			}
			
			classID = classId.getId();
			
			List<SkillLearn> temp;
			
			temp = map.get(classID);
			if (temp == null)
			{
				if (!(((classID >= 58) && (classID <= 87)) || ((classID >= 119) && (classID <= 122)) || (classID == 137) || (classID == 138)))
				{
					info("Not found NORMAL skill learn for class " + classID);
				}
				continue;
			}
			
			_normalSkillTree.put(classId.getId(), temp);
			
			ClassId secondparent = classId.getParent(1);
			if (secondparent == classId.getParent(0))
			{
				secondparent = null;
			}
			
			classId = classId.getParent(0);
			
			while (classId != null)
			{
				List<SkillLearn> parentList = _normalSkillTree.get(classId.getId());
				temp.addAll(parentList);
				
				classId = classId.getParent(0);
				if ((classId == null) && (secondparent != null))
				{
					classId = secondparent;
					secondparent = secondparent.getParent(1);
				}
			}
		}
	}
	
	/**
	 * Method addAllFishingLearns.
	 * @param race int
	 * @param s List<SkillLearn>
	 */
	public void addAllFishingLearns(int race, List<SkillLearn> s)
	{
		_fishingSkillTree.put(race, s);
	}
	
	/**
	 * Method addAllTransferLearns.
	 * @param classId int
	 * @param s List<SkillLearn>
	 */
	public void addAllTransferLearns(int classId, List<SkillLearn> s)
	{
		_transferSkillTree.put(classId, s);
	}
	
	/**
	 * Method addAllTransformationLearns.
	 * @param race int
	 * @param s List<SkillLearn>
	 */
	public void addAllTransformationLearns(int race, List<SkillLearn> s)
	{
		_transformationSkillTree.put(race, s);
	}
	
	/**
	 * Method addAllCertificationLearns.
	 * @param s List<SkillLearn>
	 */
	public void addAllCertificationLearns(List<SkillLearn> s)
	{
		_certificationSkillTree.addAll(s);
	}
	
	/**
	 * Method addAllCollectionLearns.
	 * @param s List<SkillLearn>
	 */
	public void addAllCollectionLearns(List<SkillLearn> s)
	{
		_collectionSkillTree.addAll(s);
	}
	
	/**
	 * Method addAllSubUnitLearns.
	 * @param s List<SkillLearn>
	 */
	public void addAllSubUnitLearns(List<SkillLearn> s)
	{
		_subUnitSkillTree.addAll(s);
	}
	
	/**
	 * Method addAllPledgeLearns.
	 * @param s List<SkillLearn>
	 */
	public void addAllPledgeLearns(List<SkillLearn> s)
	{
		_pledgeSkillTree.addAll(s);
	}
	
	/**
	 * Method log.
	 */
	@Override
	public void log()
	{
		info("load " + sizeHashMap(_normalSkillTree) + " normal learns for " + _normalSkillTree.size() + " classes.");
		info("load " + sizeHashMap(_transferSkillTree) + " transfer learns for " + _transferSkillTree.size() + " classes.");
		info("load " + sizeHashMap(_transformationSkillTree) + " transformation learns for " + _transformationSkillTree.size() + " races.");
		info("load " + sizeHashMap(_fishingSkillTree) + " fishing learns for " + _fishingSkillTree.size() + " races.");
		info("load " + _certificationSkillTree.size() + " certification learns.");
		info("load " + _collectionSkillTree.size() + " collection learns.");
		info("load " + _pledgeSkillTree.size() + " pledge learns.");
		info("load " + _subUnitSkillTree.size() + " sub unit learns.");
	}
	
	/**
	 * Method size.
	 * @return int
	 */
	@Deprecated
	@Override
	public int size()
	{
		return 0;
	}
	
	/**
	 * Method clear.
	 */
	@Override
	public void clear()
	{
		_normalSkillTree.clear();
		_fishingSkillTree.clear();
		_transferSkillTree.clear();
		_certificationSkillTree.clear();
		_collectionSkillTree.clear();
		_pledgeSkillTree.clear();
		_subUnitSkillTree.clear();
	}
	
	/**
	 * Method sizeHashMap.
	 * @param a HashMap<Integer,List<SkillLearn>>
	 * @return int
	 */
	private int sizeHashMap(HashMap<Integer, List<SkillLearn>> a)
	{
		int i = 0;
		for (List<SkillLearn> iterator : a.values())
		{
			i += iterator.size();
		}
		
		return i;
	}
}