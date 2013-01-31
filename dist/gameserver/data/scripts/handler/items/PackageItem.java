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
package handler.items;

import gnu.trove.map.hash.TIntObjectHashMap;
import lineage2.commons.util.Rnd;
import lineage2.gameserver.cache.Msg;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.utils.ItemFunctions;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class PackageItem extends SimpleItemHandler
{
	/**
	 * Field rewards.
	 */
	private static TIntObjectHashMap<double[][]> rewards = new TIntObjectHashMap<>();
	static
	{
		rewards.put(32727, new double[][]
		{
			{
				32738,
				1,
				6
			},
			{
				32739,
				1,
				6
			},
			{
				32740,
				1,
				6
			},
			{
				32741,
				1,
				6
			},
			{
				32736,
				1,
				6
			},
			{
				32737,
				1,
				6
			},
			{
				34693,
				1,
				6
			},
			{
				32732,
				1,
				6
			},
			{
				32733,
				1,
				6
			},
			{
				32734,
				1,
				6
			},
			{
				32735,
				1,
				6
			},
			{
				32730,
				1,
				6
			},
			{
				32731,
				1,
				6
			},
			{
				32728,
				1,
				6
			},
			{
				32729,
				1,
				6
			}
		});
		rewards.put(32729, new double[][]
		{
			{
				32767,
				1,
				3
			},
			{
				32768,
				1,
				3
			},
			{
				32769,
				1,
				3
			},
			{
				32764,
				1,
				3
			},
			{
				32765,
				1,
				3
			},
			{
				32766,
				1,
				3
			},
			{
				32762,
				1,
				3
			},
			{
				32763,
				1,
				3
			},
			{
				32760,
				1,
				3
			},
			{
				32761,
				1,
				3
			},
			{
				32758,
				1,
				3
			},
			{
				32759,
				1,
				3
			},
			{
				32756,
				1,
				3
			},
			{
				32757,
				1,
				3
			},
			{
				34635,
				1,
				3
			},
			{
				34636,
				1,
				3
			},
			{
				34637,
				1,
				3
			},
			{
				34638,
				1,
				3
			},
			{
				34639,
				1,
				3
			},
			{
				34640,
				1,
				3
			},
			{
				34641,
				1,
				3
			},
			{
				34642,
				1,
				3
			},
			{
				34643,
				1,
				3
			},
			{
				34644,
				1,
				3
			},
			{
				34645,
				1,
				3
			},
			{
				34646,
				1,
				3
			},
			{
				34647,
				1,
				3
			},
			{
				34648,
				1,
				3
			}
		});
		rewards.put(32730, new double[][]
		{
			{
				959,
				1,
				40
			},
			{
				960,
				1,
				40
			},
			{
				6577,
				1,
				10
			},
			{
				6578,
				1,
				10
			}
		});
		rewards.put(32731, new double[][]
		{
			{
				17526,
				1,
				40
			},
			{
				17527,
				1,
				40
			},
			{
				19447,
				1,
				10
			},
			{
				19448,
				1,
				10
			}
		});
		rewards.put(32732, new double[][]
		{
			{
				955,
				1,
				40
			},
			{
				956,
				1,
				40
			},
			{
				6575,
				1,
				10
			},
			{
				6576,
				1,
				10
			}
		});
		rewards.put(32733, new double[][]
		{
			{
				951,
				1,
				40
			},
			{
				952,
				1,
				40
			},
			{
				6573,
				1,
				10
			},
			{
				6574,
				1,
				10
			}
		});
		rewards.put(32734, new double[][]
		{
			{
				947,
				1,
				40
			},
			{
				948,
				1,
				40
			},
			{
				6571,
				1,
				10
			},
			{
				6572,
				1,
				10
			}
		});
		rewards.put(32735, new double[][]
		{
			{
				729,
				1,
				40
			},
			{
				730,
				1,
				40
			},
			{
				6569,
				1,
				10
			},
			{
				6570,
				1,
				10
			}
		});
		rewards.put(32728, new double[][]
		{
			{
				8921,
				1,
				1,
				8.3
			},
			{
				8920,
				1,
				1,
				8.3
			},
			{
				7683,
				1,
				1,
				8.3
			},
			{
				7680,
				1,
				1,
				8.3
			},
			{
				6843,
				1,
				1,
				8.3
			},
			{
				8565,
				1,
				1,
				8.3
			},
			{
				6845,
				1,
				1,
				8.3
			},
			{
				7681,
				1,
				1,
				8.3
			},
			{
				8916,
				1,
				1,
				8.3
			},
			{
				8184,
				1,
				1,
				8.3
			},
			{
				13494,
				1,
				1,
				8.3
			},
			{
				13495,
				1,
				1,
				8.3
			}
		});
		rewards.put(32736, new double[][]
		{
			{
				8627,
				5,
				100
			},
			{
				8633,
				5,
				100
			},
			{
				8639,
				5,
				100
			}
		});
		rewards.put(32737, new double[][]
		{
			{
				32316,
				1,
				100
			},
			{
				30357,
				1,
				100
			},
			{
				30358,
				1,
				100
			},
			{
				30359,
				1,
				100
			}
		});
		rewards.put(32738, new double[][]
		{
			{
				8623,
				1,
				26
			},
			{
				8629,
				1,
				33
			},
			{
				8635,
				1,
				41
			}
		});
		rewards.put(32739, new double[][]
		{
			{
				8624,
				1,
				27
			},
			{
				8630,
				1,
				38
			},
			{
				8636,
				1,
				35
			}
		});
		rewards.put(32740, new double[][]
		{
			{
				8625,
				1,
				100
			},
			{
				8631,
				1,
				100
			},
			{
				8637,
				1,
				100
			}
		});
		rewards.put(32741, new double[][]
		{
			{
				8626,
				1,
				100
			},
			{
				8632,
				1,
				100
			},
			{
				8638,
				1,
				100
			}
		});
		rewards.put(34693, new double[][]
		{
			{
				34649,
				5,
				100
			},
			{
				34650,
				5,
				100
			},
			{
				34651,
				5,
				100
			},
			{
				34652,
				5,
				100
			},
			{
				34653,
				5,
				100
			},
			{
				34654,
				5,
				100
			}
		});
	}
	
	/**
	 * Method useItemImpl.
	 * @param player Player
	 * @param item ItemInstance
	 * @param ctrl boolean
	 * @return boolean
	 */
	@Override
	protected boolean useItemImpl(Player player, ItemInstance item, boolean ctrl)
	{
		int itemId = item.getItemId();
		if (player._identItem)
		{
			return false;
		}
		if (!canBeExtracted(itemId, player))
		{
			return false;
		}
		double[][] _rewards = rewards.get(itemId);
		if ((_rewards == null) || (_rewards.length <= 0))
		{
			return false;
		}
		if (Functions.removeItem(player, itemId, 1) != 1)
		{
			return false;
		}
		double rndNum = 100.0D * Rnd.nextDouble();
		double chance = 0.0D;
		double chanceFrom = 0.0D;
		for (double[] reward : _rewards)
		{
			chance = reward[2];
			if ((rndNum >= chanceFrom) && (rndNum <= (chance + chanceFrom)))
			{
				ItemFunctions.addItem(player.getPlayer(), (int) reward[0], (int) reward[1], true);
				break;
			}
			chanceFrom += chance;
		}
		return true;
	}
	
	/**
	 * Method canBeExtracted.
	 * @param itemId int
	 * @param player Player
	 * @return boolean
	 */
	public static boolean canBeExtracted(int itemId, Player player)
	{
		if (player == null)
		{
			return false;
		}
		if ((player.getWeightPenalty() >= 3) || (player.getInventory().getSize() > (player.getInventoryLimit() - 10)))
		{
			player.sendPacket(Msg.YOUR_INVENTORY_IS_FULL, new SystemMessage(SystemMessage.S1_CANNOT_BE_USED_DUE_TO_UNSUITABLE_TERMS).addItemName(itemId));
			return false;
		}
		return true;
	}
	
	/**
	 * Method getItemIds.
	 * @return int[] * @see lineage2.gameserver.handler.items.IItemHandler#getItemIds()
	 */
	@Override
	public int[] getItemIds()
	{
		return rewards.keys();
	}
}
