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

public class Bonus
{
	public static final int NO_BONUS = 0;
	public static final int BONUS_GLOBAL_ON_LOGINSERVER = 1;
	public static final int BONUS_GLOBAL_ON_GAMESERVER = 2;
	private double rateXp = 1.;
	private double rateSp = 1.;
	private double questRewardRate = 1.;
	private double questDropRate = 1.;
	private double dropAdena = 1.;
	private double dropItems = 1.;
	private double dropSpoil = 1.;
	private int bonusExpire;
	
	public double getRateXp()
	{
		return rateXp;
	}
	
	public void setRateXp(double rateXp)
	{
		this.rateXp = rateXp;
	}
	
	public double getRateSp()
	{
		return rateSp;
	}
	
	public void setRateSp(double rateSp)
	{
		this.rateSp = rateSp;
	}
	
	public double getQuestRewardRate()
	{
		return questRewardRate;
	}
	
	public void setQuestRewardRate(double questRewardRate)
	{
		this.questRewardRate = questRewardRate;
	}
	
	public double getQuestDropRate()
	{
		return questDropRate;
	}
	
	public void setQuestDropRate(double questDropRate)
	{
		this.questDropRate = questDropRate;
	}
	
	public double getDropAdena()
	{
		return dropAdena;
	}
	
	public void setDropAdena(double dropAdena)
	{
		this.dropAdena = dropAdena;
	}
	
	public double getDropItems()
	{
		return dropItems;
	}
	
	public void setDropItems(double dropItems)
	{
		this.dropItems = dropItems;
	}
	
	public double getDropSpoil()
	{
		return dropSpoil;
	}
	
	public void setDropSpoil(double dropSpoil)
	{
		this.dropSpoil = dropSpoil;
	}
	
	public int getBonusExpire()
	{
		return bonusExpire;
	}
	
	public void setBonusExpire(int bonusExpire)
	{
		this.bonusExpire = bonusExpire;
	}
}
