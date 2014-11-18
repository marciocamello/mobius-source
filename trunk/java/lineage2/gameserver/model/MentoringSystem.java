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
package lineage2.gameserver.model;

import java.util.ArrayList;
import java.util.List;

import lineage2.gameserver.Config;
import lineage2.gameserver.dao.MentoringDAO;
import lineage2.gameserver.network.serverpackets.ExMentorList;
import lineage2.gameserver.network.serverpackets.SystemMessage2;
import lineage2.gameserver.network.serverpackets.components.SystemMsg;
import lineage2.gameserver.utils.MentorUtil;

import org.apache.commons.lang3.StringUtils;

/**
 * @author Smo
 */
public class MentoringSystem
{
	private List<MenteeInfo> menteeInfo = new ArrayList<>();
	private final Player _mentor;
	
	MentoringSystem(Player mentor)
	{
		_mentor = mentor;
	}
	
	public void addMentee(Player mentee)
	{
		if (menteeInfo.size() > 3)
		{
			return;
		}
		
		menteeInfo.add(new MenteeInfo(mentee, false));
		MentoringDAO.getInstance().insert(_mentor, mentee);
	}
	
	public void addMentor(Player mentor)
	{
		if (menteeInfo.size() != 0)
		{
			return;
		}
		
		menteeInfo.add(new MenteeInfo(mentor, true));
		MentorUtil.addSkillsToMentor(mentor);
	}
	
	public void remove(String name, boolean isMentor, boolean notify)
	{
		if (StringUtils.isEmpty(name))
		{
			return;
		}
		
		int objectId = removeMentee(name);
		
		if ((objectId <= 0) || (!(notify)))
		{
			return;
		}
		
		Player otherSideMentee = World.getPlayer(name);
		
		if (otherSideMentee != null)
		{
			otherSideMentee.sendPacket(new SystemMessage2(SystemMsg.THE_MENTORING_RELATIONSHIP_WITH_S1_HAS_BEEN_CANCELED).addString((isMentor) ? name : _mentor.getName()));
			MentorUtil.removeEffectsFromPlayer(otherSideMentee);
		}
		
		_mentor.sendPacket(new SystemMessage2(SystemMsg.THE_MENTORING_RELATIONSHIP_WITH_S1_HAS_BEEN_CANCELED).addString((isMentor) ? name : _mentor.getName()));
	}
	
	private int removeMentee(String name)
	{
		if (name == null)
		{
			return 0;
		}
		
		MenteeInfo removedMentee = null;
		
		for (MenteeInfo entry : menteeInfo)
		{
			if (name.equals(entry.getName()))
			{
				removedMentee = entry;
				break;
			}
		}
		
		if (removedMentee != null)
		{
			menteeInfo.remove(removedMentee);
			MentoringDAO.getInstance().delete(_mentor.getObjectId(), removedMentee.getObjectId());
			return removedMentee.getObjectId();
		}
		
		return 0;
	}
	
	public MenteeInfo getMenteeById(int id)
	{
		for (MenteeInfo info : menteeInfo)
		{
			if (info.getObjectId() == id)
			{
				return info;
			}
		}
		
		return null;
	}
	
	public List<MenteeInfo> getMenteeInfo()
	{
		return menteeInfo;
	}
	
	public int getMentor()
	{
		for (MenteeInfo menteeInfo : getMenteeInfo())
		{
			if (menteeInfo.isMentor())
			{
				return menteeInfo.getObjectId();
			}
		}
		
		return 0;
	}
	
	public int getMentee()
	{
		for (MenteeInfo menteeInfo : getMenteeInfo())
		{
			if (!(menteeInfo.isMentor()))
			{
				return menteeInfo.getObjectId();
			}
		}
		
		return 0;
	}
	
	void restore()
	{
		menteeInfo = MentoringDAO.getInstance().selectMenteeList(_mentor);
	}
	
	boolean whoIsOnline(boolean login)
	{
		for (MenteeInfo mentee : menteeInfo)
		{
			Player menteePlayer = World.getPlayer(mentee.getObjectId());
			
			if (menteePlayer != null)
			{
				MenteeInfo thisMentee = menteePlayer.getMentorSystem().checkInList(_mentor.getObjectId());
				
				if (thisMentee == null)
				{
					continue;
				}
				
				thisMentee.update(_mentor, login);
				
				if (menteePlayer.isOnline())
				{
					return true;
				}
				
				if (menteePlayer.isInOfflineMode() && Config.ALLOW_MENTOR_BUFFS_IN_OFFLINE_MODE)
				{
					return true;
				}
			}
		}
		
		return false;
	}
	
	void notify(boolean online)
	{
		for (MenteeInfo mentee : menteeInfo)
		{
			Player menteePlayer = World.getPlayer(mentee.getObjectId());
			
			if (menteePlayer != null)
			{
				MenteeInfo thisMentee = menteePlayer.getMentorSystem().checkInList(_mentor.getObjectId());
				
				if (thisMentee == null)
				{
					continue;
				}
				
				thisMentee.update(_mentor, online);
				
				if (online)
				{
					menteePlayer.sendPacket(new SystemMessage2((mentee.isMentor()) ? SystemMsg.YOU_MENTEE_S1_HAS_CONNECTED : SystemMsg.YOU_MENTOR_S1_HAS_CONNECTED).addString(_mentor.getName()));
				}
				else
				{
					menteePlayer.sendPacket(new SystemMessage2((mentee.isMentor()) ? SystemMsg.YOU_MENTEE_S1_HAS_DISCONNECTED : SystemMsg.YOU_MENTOR_S1_HAS_DISCONNECTED).addString(_mentor.getName()));
				}
				
				menteePlayer.sendPacket(new ExMentorList(menteePlayer));
				mentee.update(menteePlayer, online);
			}
		}
	}
	
	private MenteeInfo checkInList(int objectId)
	{
		for (MenteeInfo mentee : menteeInfo)
		{
			if (mentee.getObjectId() == objectId)
			{
				return mentee;
			}
		}
		
		return null;
	}
	
	@Override
	public String toString()
	{
		return "MentoringSystem[owner=" + _mentor.getName() + "]";
	}
}
