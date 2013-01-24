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
package lineage2.gameserver.model.reward;

import java.util.ArrayList;
import java.util.List;

import lineage2.gameserver.model.Player;

@SuppressWarnings("serial")
public class RewardList extends ArrayList<RewardGroup>
{
	public static final int MAX_CHANCE = 1000000;
	private final RewardType _type;
	private final boolean _autoLoot;
	
	public RewardList(RewardType rewardType, boolean a)
	{
		super(5);
		_type = rewardType;
		_autoLoot = a;
	}
	
	public List<RewardItem> roll(Player player)
	{
		return roll(player, 1.0, false, false);
	}
	
	public List<RewardItem> roll(Player player, double mod)
	{
		return roll(player, mod, false, false);
	}
	
	public List<RewardItem> roll(Player player, double mod, boolean isRaid)
	{
		return roll(player, mod, isRaid, false);
	}
	
	public List<RewardItem> roll(Player player, double mod, boolean isRaid, boolean isSiegeGuard)
	{
		List<RewardItem> temp = new ArrayList<>(size());
		for (RewardGroup g : this)
		{
			List<RewardItem> tdl = g.roll(_type, player, mod, isRaid, isSiegeGuard);
			if (!tdl.isEmpty())
			{
				for (RewardItem itd : tdl)
				{
					temp.add(itd);
				}
			}
		}
		return temp;
	}
	
	public boolean validate()
	{
		for (RewardGroup g : this)
		{
			int chanceSum = 0;
			for (RewardData d : g.getItems())
			{
				chanceSum += d.getChance();
			}
			if (chanceSum <= MAX_CHANCE)
			{
				return true;
			}
			double mod = MAX_CHANCE / chanceSum;
			for (RewardData d : g.getItems())
			{
				double chance = d.getChance() * mod;
				d.setChance(chance);
				g.setChance(MAX_CHANCE);
			}
		}
		return false;
	}
	
	public boolean isAutoLoot()
	{
		return _autoLoot;
	}
	
	public RewardType getType()
	{
		return _type;
	}
}
