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

import lineage2.commons.math.SafeMath;
import lineage2.commons.util.Rnd;
import lineage2.gameserver.Config;
import lineage2.gameserver.data.xml.holder.ItemHolder;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.templates.item.ItemTemplate;

import org.apache.commons.lang3.ArrayUtils;

public class RewardData implements Cloneable
{
	private final ItemTemplate _item;
	private boolean _notRate = false;
	private long _mindrop;
	private long _maxdrop;
	private double _chance;
	private double _chanceInGroup;
	
	public RewardData(int itemId)
	{
		_item = ItemHolder.getInstance().getTemplate(itemId);
		if (_item.isArrow() || (Config.NO_RATE_EQUIPMENT && _item.isEquipment()) || (Config.NO_RATE_KEY_MATERIAL && _item.isKeyMatherial()) || (Config.NO_RATE_RECIPES && _item.isRecipe()) || ArrayUtils.contains(Config.NO_RATE_ITEMS, itemId))
		{
			_notRate = true;
		}
	}
	
	public RewardData(int itemId, long min, long max, double chance)
	{
		this(itemId);
		_mindrop = min;
		_maxdrop = max;
		_chance = chance;
	}
	
	public boolean notRate()
	{
		return _notRate;
	}
	
	public void setNotRate(boolean notRate)
	{
		_notRate = notRate;
	}
	
	public int getItemId()
	{
		return _item.getItemId();
	}
	
	public ItemTemplate getItem()
	{
		return _item;
	}
	
	public long getMinDrop()
	{
		return _mindrop;
	}
	
	public long getMaxDrop()
	{
		return _maxdrop;
	}
	
	public double getChance()
	{
		return _chance;
	}
	
	public void setMinDrop(long mindrop)
	{
		_mindrop = mindrop;
	}
	
	public void setMaxDrop(long maxdrop)
	{
		_maxdrop = maxdrop;
	}
	
	public void setChance(double chance)
	{
		_chance = chance;
	}
	
	public void setChanceInGroup(double chance)
	{
		_chanceInGroup = chance;
	}
	
	public double getChanceInGroup()
	{
		return _chanceInGroup;
	}
	
	@Override
	public String toString()
	{
		return "ItemID: " + getItem() + " Min: " + getMinDrop() + " Max: " + getMaxDrop() + " Chance: " + (getChance() / 10000.0) + "%";
	}
	
	@Override
	public RewardData clone()
	{
		return new RewardData(getItemId(), getMinDrop(), getMaxDrop(), getChance());
	}
	
	@Override
	public boolean equals(Object o)
	{
		if (o instanceof RewardData)
		{
			RewardData drop = (RewardData) o;
			return drop.getItemId() == getItemId();
		}
		return false;
	}
	
	public List<RewardItem> roll(Player player, double mod)
	{
		double rate;
		if (_item.isAdena())
		{
			rate = (Config.RATE_DROP_ADENA + player.getVitalityBonus()) * player.getRateAdena();
		}
		else
		{
			rate = (Config.RATE_DROP_ITEMS + player.getVitalityBonus()) * (player.getRateItems());
		}
		return roll(rate * mod);
	}
	
	public List<RewardItem> roll(double rate)
	{
		double mult = Math.ceil(rate);
		List<RewardItem> ret = new ArrayList<>(1);
		RewardItem t = null;
		long count;
		for (int n = 0; n < mult; n++)
		{
			if (Rnd.get(RewardList.MAX_CHANCE) <= (_chance * Math.min(rate - n, 1.0)))
			{
				if (getMinDrop() >= getMaxDrop())
				{
					count = getMinDrop();
				}
				else
				{
					count = Rnd.get(getMinDrop(), getMaxDrop());
				}
				if (t == null)
				{
					ret.add(t = new RewardItem(_item.getItemId()));
					t.count = count;
				}
				else
				{
					t.count = SafeMath.addAndLimit(t.count, count);
				}
			}
		}
		return ret;
	}
}
