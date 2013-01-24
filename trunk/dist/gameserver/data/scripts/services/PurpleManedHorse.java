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

import lineage2.commons.text.PrintfFormat;
import lineage2.gameserver.Config;
import lineage2.gameserver.data.xml.holder.MultiSellHolder;
import lineage2.gameserver.data.xml.holder.MultiSellHolder.MultiSellListContainer;
import lineage2.gameserver.model.base.MultiSellEntry;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.scripts.ScriptFile;

public class PurpleManedHorse extends Functions implements ScriptFile
{
	private static boolean Enabled = false;
	private static final int MultiSellID = -1001;
	private static final PrintfFormat dlg = new PrintfFormat("<br>[npc_%%objectId%%_Multisell %d|%s]");
	private static MultiSellListContainer list;
	
	@Override
	public void onLoad()
	{
		if (Config.SERVICES_SELLPETS.isEmpty())
		{
			return;
		}
		String[] SELLPETS = Config.SERVICES_SELLPETS.split(";");
		if (SELLPETS.length == 0)
		{
			return;
		}
		list = new MultiSellListContainer();
		list.setNoTax(true);
		list.setShowAll(true);
		list.setKeepEnchant(false);
		list.setNoKey(true);
		int entId = 1;
		for (String SELLPET : SELLPETS)
		{
			MultiSellEntry e = MultiSellHolder.parseEntryFromStr(SELLPET);
			if (e != null)
			{
				e.setEntryId(entId++);
				list.addEntry(e);
			}
		}
		if (list.getEntries().size() == 0)
		{
			return;
		}
		Enabled = true;
		loadMultiSell();
	}
	
	@Override
	public void onReload()
	{
	}
	
	@Override
	public void onShutdown()
	{
	}
	
	private static void loadMultiSell()
	{
		MultiSellHolder.getInstance().addMultiSellListContainer(MultiSellID, list);
	}
	
	public String PetManagersDialogAppend(Integer val)
	{
		if ((val == 0) && Enabled)
		{
			return dlg.sprintf(new Object[]
			{
				MultiSellID,
				"Buy New Pets"
			});
		}
		return "";
	}
	
	public String DialogAppend_30731(Integer val)
	{
		return PetManagersDialogAppend(val);
	}
	
	public String DialogAppend_30827(Integer val)
	{
		return PetManagersDialogAppend(val);
	}
	
	public String DialogAppend_30828(Integer val)
	{
		return PetManagersDialogAppend(val);
	}
	
	public String DialogAppend_30829(Integer val)
	{
		return PetManagersDialogAppend(val);
	}
	
	public String DialogAppend_30830(Integer val)
	{
		return PetManagersDialogAppend(val);
	}
	
	public String DialogAppend_30831(Integer val)
	{
		return PetManagersDialogAppend(val);
	}
	
	public String DialogAppend_30869(Integer val)
	{
		return PetManagersDialogAppend(val);
	}
	
	public String DialogAppend_31067(Integer val)
	{
		return PetManagersDialogAppend(val);
	}
	
	public String DialogAppend_31265(Integer val)
	{
		return PetManagersDialogAppend(val);
	}
	
	public String DialogAppend_31309(Integer val)
	{
		return PetManagersDialogAppend(val);
	}
	
	public String DialogAppend_31954(Integer val)
	{
		return PetManagersDialogAppend(val);
	}
}
