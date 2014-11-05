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
package lineage2.gameserver.model.quest.dynamic;

public class DynamicQuestParticipant implements Comparable<DynamicQuestParticipant>
{
	private final String name;
	private int currentPoints;
	private int additionalPoints;
	
	public DynamicQuestParticipant(String name)
	{
		this.name = name;
	}
	
	public String getName()
	{
		return name;
	}
	
	public int getCurrentPoints()
	{
		return currentPoints;
	}
	
	public int getAdditionalPoints()
	{
		return additionalPoints;
	}
	
	public void setAdditionalPoints(int additionalPoints)
	{
		this.additionalPoints = additionalPoints;
	}
	
	public void increaseCurrentPoints(int points)
	{
		currentPoints += points;
	}
	
	@Override
	public int compareTo(DynamicQuestParticipant participant)
	{
		if ((getCurrentPoints() + getAdditionalPoints()) > (participant.getCurrentPoints() + participant.getAdditionalPoints()))
		{
			return 1;
		}
		else if ((getCurrentPoints() + getAdditionalPoints()) > (participant.getCurrentPoints() + participant.getAdditionalPoints()))
		{
			return 0;
		}
		else
		{
			return -1;
		}
	}
}