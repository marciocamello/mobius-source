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
import java.util.Collections;
import java.util.List;

import lineage2.commons.util.Rnd;
import lineage2.gameserver.Config;
import lineage2.gameserver.data.xml.holder.NpcHolder;
import lineage2.gameserver.data.xml.holder.SoulCrystalHolder;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.instances.MonsterInstance;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.network.serverpackets.components.SystemMessageId;
import lineage2.gameserver.scripts.ScriptFile;
import lineage2.gameserver.templates.SoulCrystal;
import lineage2.gameserver.templates.npc.AbsorbInfo;
import lineage2.gameserver.templates.npc.NpcTemplate;

public class Q00350_EnhanceYourWeapon extends Quest implements ScriptFile
{
	// Npcs
	private static final int Jurek = 30115;
	private static final int Gideon = 30194;
	private static final int Winonin = 30856;
	// Items
	private static final int RED_SOUL_CRYSTAL0_ID = 4629;
	private static final int GREEN_SOUL_CRYSTAL0_ID = 4640;
	private static final int BLUE_SOUL_CRYSTAL0_ID = 4651;
	
	public Q00350_EnhanceYourWeapon()
	{
		super(false);
		addStartNpc(Jurek);
		addStartNpc(Gideon);
		addStartNpc(Winonin);
		
		for (NpcTemplate template : NpcHolder.getInstance().getAll())
		{
			if ((template != null) && !template.getAbsorbInfo().isEmpty())
			{
				addKillId(template.getId());
			}
		}
	}
	
	@Override
	public String onEvent(String event, QuestState qs, NpcInstance npc)
	{
		switch (event)
		{
			case "30115-04.htm":
			case "30194-04.htm":
				qs.setCond(1);
				qs.setState(STARTED);
				qs.playSound(SOUND_ACCEPT);
				break;
			
			case "30115-09.htm":
			case "30194-09.htm":
				qs.giveItems(RED_SOUL_CRYSTAL0_ID, 1);
				break;
			
			case "30115-10.htm":
			case "30194-10.htm":
				qs.giveItems(GREEN_SOUL_CRYSTAL0_ID, 1);
				break;
			
			case "30115-11.htm":
			case "30194-11.htm":
				qs.giveItems(BLUE_SOUL_CRYSTAL0_ID, 1);
				break;
			
			case "exit.htm":
				qs.exitCurrentQuest(true);
				break;
		}
		
		return event;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState qs)
	{
		String htmltext = "noquest";
		final int id = qs.getState();
		final int npcId = npc.getId();
		
		if ((qs.getQuestItemsCount(RED_SOUL_CRYSTAL0_ID) == 0) && (qs.getQuestItemsCount(GREEN_SOUL_CRYSTAL0_ID) == 0) && (qs.getQuestItemsCount(BLUE_SOUL_CRYSTAL0_ID) == 0))
		{
			if (id == CREATED)
			{
				htmltext = npcId + "-01.htm";
			}
			else
			{
				htmltext = npcId + "-21.htm";
			}
		}
		else
		{
			if (id == CREATED)
			{
				qs.setCond(1);
				qs.setState(STARTED);
			}
			
			htmltext = npcId + "-03.htm";
		}
		
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		final Player player = qs.getPlayer();
		
		if ((player == null) || !npc.isMonster())
		{
			return null;
		}
		
		List<PlayerResult> list;
		
		if (player.getParty() == null)
		{
			list = new ArrayList<>(1);
			list.add(new PlayerResult(player));
		}
		else
		{
			list = new ArrayList<>(player.getParty().getMemberCount());
			list.add(new PlayerResult(player));
			
			for (Player m : player.getParty().getPartyMembers())
			{
				if ((m != player) && m.isInRange(npc.getLoc(), Config.ALT_PARTY_DISTRIBUTION_RANGE))
				{
					list.add(new PlayerResult(m));
				}
			}
		}
		
		for (AbsorbInfo info : npc.getTemplate().getAbsorbInfo())
		{
			calcAbsorb(list, (MonsterInstance) npc, info);
		}
		
		for (PlayerResult r : list)
		{
			r.send();
		}
		
		return null;
	}
	
	private void calcAbsorb(List<PlayerResult> players, MonsterInstance npc, AbsorbInfo info)
	{
		int memberSize = 0;
		List<PlayerResult> targets;
		
		switch (info.getAbsorbType())
		{
			case LAST_HIT:
				targets = Collections.singletonList(players.get(0));
				break;
			
			case PARTY_ALL:
				targets = players;
				break;
			
			case PARTY_RANDOM:
				memberSize = players.size();
				
				if (memberSize == 1)
				{
					targets = Collections.singletonList(players.get(0));
				}
				else
				{
					int size = Rnd.get(memberSize);
					targets = new ArrayList<>(size);
					List<PlayerResult> temp = new ArrayList<>(players);
					Collections.shuffle(temp);
					
					for (int i = 0; i < size; i++)
					{
						targets.add(temp.get(i));
					}
				}
				break;
			
			case PARTY_ONE:
				memberSize = players.size();
				
				if (memberSize == 1)
				{
					targets = Collections.singletonList(players.get(0));
				}
				else
				{
					int rnd = Rnd.get(memberSize);
					targets = Collections.singletonList(players.get(rnd));
				}
				break;
			
			default:
				return;
		}
		
		for (PlayerResult target : targets)
		{
			if ((target == null) || (target.getMessage() == SystemMessage.getSystemMessage(SystemMessageId.THE_SOUL_CRYSTAL_SUCCEEDED_IN_ABSORBING_A_SOUL)))
			{
				continue;
			}
			
			final Player targetPlayer = target.getPlayer();
			
			if (info.isSkill() && !npc.isAbsorbed(targetPlayer))
			{
				continue;
			}
			
			if (targetPlayer.getQuestState(Q00350_EnhanceYourWeapon.class) == null)
			{
				continue;
			}
			
			boolean resonation = false;
			SoulCrystal soulCrystal = null;
			final ItemInstance[] items = targetPlayer.getInventory().getItems();
			
			for (ItemInstance item : items)
			{
				SoulCrystal crystal = SoulCrystalHolder.getInstance().getCrystal(item.getId());
				
				if (crystal == null)
				{
					continue;
				}
				
				target.setMessage(SystemMessage.getSystemMessage(SystemMessageId.THE_SOUL_CRYSTAL_WAS_NOT_ABLE_TO_ABSORB_THE_SOUL));
				
				if (soulCrystal != null)
				{
					target.setMessage(SystemMessage.getSystemMessage(SystemMessageId.THE_SOUL_CRYSTAL_CAUSED_RESONATION_AND_FAILED_AT_ABSORBING_A_SOUL));
					break;
				}
				
				soulCrystal = crystal;
			}
			
			if (resonation || (soulCrystal == null))
			{
				continue;
			}
			
			if (!info.canAbsorb(soulCrystal.getLevel() + 1))
			{
				target.setMessage(SystemMessage.getSystemMessage(SystemMessageId.THE_SOUL_CRYSTAL_IS_REFUSING_TO_ABSORB_THE_SOUL));
				continue;
			}
			
			int nextItemId = 0;
			
			if ((info.getCursedChance() > 0) && (soulCrystal.getCursedNextItemId() > 0))
			{
				nextItemId = Rnd.chance(info.getCursedChance()) ? soulCrystal.getCursedNextItemId() : 0;
			}
			
			if (nextItemId == 0)
			{
				nextItemId = Rnd.chance(info.getChance()) ? soulCrystal.getNextItemId() : 0;
			}
			
			if (nextItemId == 0)
			{
				target.setMessage(SystemMessage.getSystemMessage(SystemMessageId.THE_SOUL_CRYSTAL_WAS_NOT_ABLE_TO_ABSORB_THE_SOUL));
				continue;
			}
			
			if (targetPlayer.consumeItem(soulCrystal.getId(), 1))
			{
				targetPlayer.getInventory().addItem(nextItemId, 1);
				targetPlayer.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.YOU_HAVE_EARNED_S1).addItemName(nextItemId));
				target.setMessage(SystemMessage.getSystemMessage(SystemMessageId.THE_SOUL_CRYSTAL_SUCCEEDED_IN_ABSORBING_A_SOUL));
			}
			else
			{
				target.setMessage(SystemMessage.getSystemMessage(SystemMessageId.THE_SOUL_CRYSTAL_WAS_NOT_ABLE_TO_ABSORB_THE_SOUL));
			}
		}
	}
	
	private static class PlayerResult
	{
		private final Player _player;
		private SystemMessage _message;
		
		public PlayerResult(Player player)
		{
			_player = player;
		}
		
		public Player getPlayer()
		{
			return _player;
		}
		
		public SystemMessage getMessage()
		{
			return _message;
		}
		
		public void setMessage(SystemMessage message)
		{
			_message = message;
		}
		
		public void send()
		{
			if (_message != null)
			{
				_player.sendPacket(_message);
			}
		}
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
