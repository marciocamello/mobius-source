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
package quests;

import java.util.ArrayList;
import java.util.List;

import lineage2.commons.dao.JdbcEntityState;
import lineage2.commons.threading.RunnableImpl;
import lineage2.commons.util.Rnd;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.ai.CtrlEvent;
import lineage2.gameserver.data.xml.holder.NpcHolder;
import lineage2.gameserver.model.Playable;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.SimpleSpawner;
import lineage2.gameserver.model.Summon;
import lineage2.gameserver.model.World;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.network.serverpackets.InventoryUpdate;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.scripts.ScriptFile;
import lineage2.gameserver.tables.PetDataTable;
import lineage2.gameserver.tables.PetDataTable.L2Pet;
import lineage2.gameserver.templates.npc.NpcTemplate;
import lineage2.gameserver.utils.Location;

public class Q00421_LittleWingsBigAdventure extends Quest implements ScriptFile
{
	private static final int Cronos = 30610;
	private static final int Mimyu = 30747;
	private static final int Fairy_Tree_of_Wind = 27185;
	private static final int Fairy_Tree_of_Star = 27186;
	private static final int Fairy_Tree_of_Twilight = 27187;
	private static final int Fairy_Tree_of_Abyss = 27188;
	static final int Soul_of_Tree_Guardian = 27189;
	private static final int Dragonflute_of_Wind = L2Pet.HATCHLING_WIND.getControlItemId();
	private static final int Dragonflute_of_Star = L2Pet.HATCHLING_STAR.getControlItemId();
	private static final int Dragonflute_of_Twilight = L2Pet.HATCHLING_TWILIGHT.getControlItemId();
	private static final int Dragon_Bugle_of_Wind = L2Pet.STRIDER_WIND.getControlItemId();
	private static final int Dragon_Bugle_of_Star = L2Pet.STRIDER_STAR.getControlItemId();
	private static final int Dragon_Bugle_of_Twilight = L2Pet.STRIDER_TWILIGHT.getControlItemId();
	private static final int Fairy_Leaf = 4325;
	private static final int Min_Fairy_Tree_Attaks = 110;
	
	public Q00421_LittleWingsBigAdventure()
	{
		super(false);
		addStartNpc(Cronos);
		addTalkId(Mimyu);
		addKillId(Fairy_Tree_of_Wind, Fairy_Tree_of_Star, Fairy_Tree_of_Twilight, Fairy_Tree_of_Abyss);
		addAttackId(Fairy_Tree_of_Wind, Fairy_Tree_of_Star, Fairy_Tree_of_Twilight, Fairy_Tree_of_Abyss);
		addQuestItem(Fairy_Leaf);
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		final int _state = qs.getState();
		final ItemInstance dragonflute = GetDragonflute(qs);
		final int dragonflute_id = qs.getInt("dragonflute");
		
		switch (event)
		{
			case "30610_05.htm":
				if (_state == CREATED)
				{
					qs.setState(STARTED);
					qs.setCond(1);
					qs.playSound(SOUND_ACCEPT);
				}
				break;
			
			case "30747_03.htm":
			case "30747_04.htm":
				if ((_state == STARTED) && (qs.getCond() == 1))
				{
					if (dragonflute == null)
					{
						return "noquest";
					}
					
					if (dragonflute.getObjectId() != dragonflute_id)
					{
						if (Rnd.chance(10))
						{
							qs.takeItems(dragonflute.getId(), 1);
							qs.playSound(SOUND_FINISH);
							qs.exitCurrentQuest(true);
						}
						
						return "30747_00.htm";
					}
					
					if (!HatchlingSummoned(qs, false))
					{
						return event.equals("30747_04.htm") ? "30747_04a.htm" : "30747_02.htm";
					}
					
					if (event.equals("30747_04.htm"))
					{
						qs.setCond(2);
						qs.takeItems(Fairy_Leaf, -1);
						qs.giveItems(Fairy_Leaf, 4);
						qs.playSound(SOUND_MIDDLE);
					}
				}
				break;
		}
		
		return event;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		int _state = qs.getState();
		int npcId = npc.getId();
		int cond = qs.getCond();
		ItemInstance dragonflute = GetDragonflute(qs);
		int dragonflute_id = qs.getInt("dragonflute");
		
		if (_state == CREATED)
		{
			if (npcId != Cronos)
			{
				return "noquest";
			}
			
			if (qs.getPlayer().getLevel() < 45)
			{
				qs.exitCurrentQuest(true);
				return "30610_01.htm";
			}
			
			if (dragonflute == null)
			{
				qs.exitCurrentQuest(true);
				return "30610_02.htm";
			}
			
			if (dragonflute.getEnchantLevel() < 55)
			{
				qs.exitCurrentQuest(true);
				return "30610_03.htm";
			}
			
			qs.setCond(0);
			qs.set("dragonflute", String.valueOf(dragonflute.getObjectId()));
			return "30610_04.htm";
		}
		else if (_state != STARTED)
		{
			return "noquest";
		}
		
		switch (npcId)
		{
			case Cronos:
				if (dragonflute == null)
				{
					return "30610_02.htm";
				}
				return dragonflute.getObjectId() == dragonflute_id ? "30610_07.htm" : "30610_06.htm";
				
			case Mimyu:
				if ((qs.getQuestItemsCount(Dragon_Bugle_of_Wind) + qs.getQuestItemsCount(Dragon_Bugle_of_Star) + qs.getQuestItemsCount(Dragon_Bugle_of_Twilight)) > 0)
				{
					return "30747_00b.htm";
				}
				else if (dragonflute == null)
				{
					return "noquest";
				}
				
				switch (cond)
				{
					case 1:
						return "30747_01.htm";
						
					case 2:
						if (!HatchlingSummoned(qs, false))
						{
							return "30747_09.htm";
						}
						if (qs.getQuestItemsCount(Fairy_Leaf) == 0)
						{
							qs.playSound(SOUND_FINISH);
							qs.exitCurrentQuest(true);
							return "30747_11.htm";
						}
						return "30747_10.htm";
						
					case 3:
						if (dragonflute.getObjectId() != dragonflute_id)
						{
							return "30747_00a.htm";
						}
						else if (qs.getQuestItemsCount(Fairy_Leaf) > 0)
						{
							qs.playSound(SOUND_FINISH);
							qs.exitCurrentQuest(true);
							return "30747_11.htm";
						}
						else if (!(CheckTree(qs, Fairy_Tree_of_Wind) && CheckTree(qs, Fairy_Tree_of_Star) && CheckTree(qs, Fairy_Tree_of_Twilight) && CheckTree(qs, Fairy_Tree_of_Abyss)))
						{
							qs.playSound(SOUND_FINISH);
							qs.exitCurrentQuest(true);
							return "30747_11.htm";
						}
						else if (qs.getInt("welldone") == 0)
						{
							if (!HatchlingSummoned(qs, false))
							{
								return "30747_09.htm";
							}
							
							qs.set("welldone", "1");
							return "30747_12.htm";
						}
						else if (HatchlingSummoned(qs, false) || (qs.getPlayer().getSummonList() != null))
						{
							return "30747_13a.htm";
						}
						
						dragonflute.setId((Dragon_Bugle_of_Wind + dragonflute.getId()) - Dragonflute_of_Wind);
						dragonflute.setJdbcState(JdbcEntityState.UPDATED);
						dragonflute.update();
						// send packets
						InventoryUpdate iu = new InventoryUpdate();
						iu.addModifiedItem(dragonflute);
						qs.getPlayer().sendPacket(iu);
						qs.playSound(SOUND_FINISH);
						qs.exitCurrentQuest(true);
						return "30747_13.htm";
				}
				break;
		}
		
		return "noquest";
	}
	
	@Override
	public String onAttack(NpcInstance npc, QuestState qs)
	{
		if ((qs.getState() != STARTED) || (qs.getCond() != 2) || !HatchlingSummoned(qs, true) || (qs.getQuestItemsCount(Fairy_Leaf) == 0))
		{
			return null;
		}
		
		final String npcID = String.valueOf(npc.getId());
		final Integer attaked_times = qs.getInt(npcID);
		
		if (CheckTree(qs, npc.getId()))
		{
			return null;
		}
		
		if (attaked_times > Min_Fairy_Tree_Attaks)
		{
			qs.set(npcID, "1000000");
			Functions.npcSay(npc, "Give me the leaf!");
			qs.takeItems(Fairy_Leaf, 1);
			
			if (CheckTree(qs, Fairy_Tree_of_Wind) && CheckTree(qs, Fairy_Tree_of_Star) && CheckTree(qs, Fairy_Tree_of_Twilight) && CheckTree(qs, Fairy_Tree_of_Abyss))
			{
				qs.setCond(3);
				qs.playSound(SOUND_MIDDLE);
			}
			else
			{
				qs.playSound(SOUND_ITEMGET);
			}
		}
		else
		{
			qs.set(npcID, String.valueOf(attaked_times + 1));
		}
		
		return null;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		ThreadPoolManager.getInstance().schedule(new GuardiansSpawner(npc, qs, Rnd.get(15, 20)), 1000);
		return null;
	}
	
	public class GuardiansSpawner extends RunnableImpl
	{
		private SimpleSpawner _spawn = null;
		private String agressor;
		private String agressors_pet = null;
		private List<String> agressors_party = null;
		private int tiks = 0;
		
		public GuardiansSpawner(NpcInstance npc, QuestState st, int _count)
		{
			NpcTemplate template = NpcHolder.getInstance().getTemplate(Soul_of_Tree_Guardian);
			
			if (template == null)
			{
				return;
			}
			
			try
			{
				_spawn = new SimpleSpawner(template);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			
			for (int i = 0; i < _count; i++)
			{
				_spawn.setLoc(Location.findPointToStay(npc, 50, 200));
				_spawn.setHeading(Rnd.get(0, 0xFFFF));
				_spawn.setAmount(1);
				_spawn.doSpawn(true);
				agressor = st.getPlayer().getName();
				
				if (st.getPlayer().getSummonList().getPet() != null)
				{
					agressors_pet = st.getPlayer().getSummonList().getPet().getName();
				}
				
				if (st.getPlayer().getParty() != null)
				{
					agressors_party = new ArrayList<>();
					
					for (Player _member : st.getPlayer().getParty().getPartyMembers())
					{
						if (!_member.equals(st.getPlayer()))
						{
							agressors_party.add(_member.getName());
						}
					}
				}
			}
			
			_spawn.stopRespawn();
			updateAgression();
		}
		
		private void AddAgression(Playable player, int aggro)
		{
			if (player == null)
			{
				return;
			}
			
			for (NpcInstance mob : _spawn.getAllSpawned())
			{
				mob.getAI().notifyEvent(CtrlEvent.EVT_AGGRESSION, player, aggro);
			}
		}
		
		private void updateAgression()
		{
			final Player _player = World.getPlayer(agressor);
			
			if (_player != null)
			{
				if ((agressors_pet != null) && (_player.getSummonList().getPet() != null) && _player.getSummonList().getPet().getName().equals(agressors_pet))
				{
					AddAgression(_player.getSummonList().getPet(), 10);
				}
				
				AddAgression(_player, 2);
			}
			
			if (agressors_party != null)
			{
				for (String _agressor : agressors_party)
				{
					AddAgression(World.getPlayer(_agressor), 1);
				}
			}
		}
		
		@Override
		public void runImpl()
		{
			if (_spawn == null)
			{
				return;
			}
			
			tiks++;
			
			if (tiks < 600)
			{
				updateAgression();
				ThreadPoolManager.getInstance().schedule(this, 1000);
				return;
			}
			
			_spawn.deleteAll();
		}
	}
	
	private static ItemInstance GetDragonflute(QuestState qs)
	{
		List<ItemInstance> Dragonflutes = new ArrayList<>();
		
		for (ItemInstance item : qs.getPlayer().getInventory().getItems())
		{
			if ((item != null) && ((item.getId() == Dragonflute_of_Wind) || (item.getId() == Dragonflute_of_Star) || (item.getId() == Dragonflute_of_Twilight)))
			{
				Dragonflutes.add(item);
			}
		}
		
		if (Dragonflutes.isEmpty())
		{
			return null;
		}
		
		if (Dragonflutes.size() == 1)
		{
			return Dragonflutes.get(0);
		}
		
		if (qs.getState() == CREATED)
		{
			return null;
		}
		
		int dragonflute_id = qs.getInt("dragonflute");
		
		for (ItemInstance item : Dragonflutes)
		{
			if (item.getObjectId() == dragonflute_id)
			{
				return item;
			}
		}
		
		return null;
	}
	
	private static boolean HatchlingSummoned(QuestState qs, boolean CheckObjID)
	{
		final Summon _pet = qs.getPlayer().getSummonList().getPet();
		
		if (_pet == null)
		{
			return false;
		}
		
		if (CheckObjID)
		{
			int dragonflute_id = qs.getInt("dragonflute");
			
			if (dragonflute_id == 0)
			{
				return false;
			}
			
			if (_pet.getControlItemObjId() != dragonflute_id)
			{
				return false;
			}
		}
		
		final ItemInstance dragonflute = GetDragonflute(qs);
		
		if (dragonflute == null)
		{
			return false;
		}
		
		if (PetDataTable.getControlItemId(_pet.getId()) != dragonflute.getId())
		{
			return false;
		}
		
		return true;
	}
	
	private static boolean CheckTree(QuestState qs, int Fairy_Tree_id)
	{
		return qs.getInt(String.valueOf(Fairy_Tree_id)) == 1000000;
	}
	
	@Override
	public void onLoad()
	{
	}
	
	@Override
	public void onReload()
	{
	}
	
	@Override
	public void onShutdown()
	{
	}
}
