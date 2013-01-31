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

import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.scripts.Functions;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class VitaminManager extends Functions
{
	/**
	 * Field PetCoupon. (value is 13273)
	 */
	private static final int PetCoupon = 13273;
	/**
	 * Field SpecialPetCoupon. (value is 14065)
	 */
	private static final int SpecialPetCoupon = 14065;
	/**
	 * Field WeaselNeck. (value is 13017)
	 */
	private static final int WeaselNeck = 13017;
	/**
	 * Field PrincNeck. (value is 13018)
	 */
	private static final int PrincNeck = 13018;
	/**
	 * Field BeastNeck. (value is 13019)
	 */
	private static final int BeastNeck = 13019;
	/**
	 * Field FoxNeck. (value is 13020)
	 */
	private static final int FoxNeck = 13020;
	/**
	 * Field KnightNeck. (value is 13548)
	 */
	private static final int KnightNeck = 13548;
	/**
	 * Field SpiritNeck. (value is 13549)
	 */
	private static final int SpiritNeck = 13549;
	/**
	 * Field OwlNeck. (value is 13550)
	 */
	private static final int OwlNeck = 13550;
	/**
	 * Field TurtleNeck. (value is 13551)
	 */
	private static final int TurtleNeck = 13551;
	
	/**
	 * Method giveWeasel.
	 */
	public void giveWeasel()
	{
		Player player = getSelf();
		NpcInstance npc = getNpc();
		String htmltext;
		if (getItemCount(player, PetCoupon) > 0)
		{
			removeItem(player, PetCoupon, 1);
			addItem(player, WeaselNeck, 1);
			htmltext = npc.getNpcId() + "-ok.htm";
		}
		else
		{
			htmltext = npc.getNpcId() + "-no.htm";
		}
		npc.showChatWindow(player, "default/" + htmltext);
	}
	
	/**
	 * Method givePrinc.
	 */
	public void givePrinc()
	{
		Player player = getSelf();
		NpcInstance npc = getNpc();
		String htmltext;
		if (getItemCount(player, PetCoupon) > 0)
		{
			removeItem(player, PetCoupon, 1);
			addItem(player, PrincNeck, 1);
			htmltext = npc.getNpcId() + "-ok.htm";
		}
		else
		{
			htmltext = npc.getNpcId() + "-no.htm";
		}
		npc.showChatWindow(player, "default/" + htmltext);
	}
	
	/**
	 * Method giveBeast.
	 */
	public void giveBeast()
	{
		Player player = getSelf();
		NpcInstance npc = getNpc();
		String htmltext;
		if (getItemCount(player, PetCoupon) > 0)
		{
			removeItem(player, PetCoupon, 1);
			addItem(player, BeastNeck, 1);
			htmltext = npc.getNpcId() + "-ok.htm";
		}
		else
		{
			htmltext = npc.getNpcId() + "-no.htm";
		}
		npc.showChatWindow(player, "default/" + htmltext);
	}
	
	/**
	 * Method giveFox.
	 */
	public void giveFox()
	{
		Player player = getSelf();
		NpcInstance npc = getNpc();
		String htmltext;
		if (getItemCount(player, PetCoupon) > 0)
		{
			removeItem(player, PetCoupon, 1);
			addItem(player, FoxNeck, 1);
			htmltext = npc.getNpcId() + "-ok.htm";
		}
		else
		{
			htmltext = npc.getNpcId() + "-no.htm";
		}
		npc.showChatWindow(player, "default/" + htmltext);
	}
	
	/**
	 * Method giveKnight.
	 */
	public void giveKnight()
	{
		Player player = getSelf();
		NpcInstance npc = getNpc();
		String htmltext;
		if (getItemCount(player, SpecialPetCoupon) > 0)
		{
			removeItem(player, SpecialPetCoupon, 1);
			addItem(player, KnightNeck, 1);
			htmltext = npc.getNpcId() + "-ok.htm";
		}
		else
		{
			htmltext = npc.getNpcId() + "-no.htm";
		}
		npc.showChatWindow(player, "default/" + htmltext);
	}
	
	/**
	 * Method giveSpirit.
	 */
	public void giveSpirit()
	{
		Player player = getSelf();
		NpcInstance npc = getNpc();
		String htmltext;
		if (getItemCount(player, SpecialPetCoupon) > 0)
		{
			removeItem(player, SpecialPetCoupon, 1);
			addItem(player, SpiritNeck, 1);
			htmltext = npc.getNpcId() + "-ok.htm";
		}
		else
		{
			htmltext = npc.getNpcId() + "-no.htm";
		}
		npc.showChatWindow(player, "default/" + htmltext);
	}
	
	/**
	 * Method giveOwl.
	 */
	public void giveOwl()
	{
		Player player = getSelf();
		NpcInstance npc = getNpc();
		String htmltext;
		if (getItemCount(player, SpecialPetCoupon) > 0)
		{
			removeItem(player, SpecialPetCoupon, 1);
			addItem(player, OwlNeck, 1);
			htmltext = npc.getNpcId() + "-ok.htm";
		}
		else
		{
			htmltext = npc.getNpcId() + "-no.htm";
		}
		npc.showChatWindow(player, "default/" + htmltext);
	}
	
	/**
	 * Method giveTurtle.
	 */
	public void giveTurtle()
	{
		Player player = getSelf();
		NpcInstance npc = getNpc();
		String htmltext;
		if (getItemCount(player, SpecialPetCoupon) > 0)
		{
			removeItem(player, SpecialPetCoupon, 1);
			addItem(player, TurtleNeck, 1);
			htmltext = npc.getNpcId() + "-ok.htm";
		}
		else
		{
			htmltext = npc.getNpcId() + "-no.htm";
		}
		npc.showChatWindow(player, "default/" + htmltext);
	}
}
