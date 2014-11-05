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
package lineage2.gameserver.model.party;

import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

import lineage2.commons.threading.RunnableImpl;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Request;
import lineage2.gameserver.model.Request.L2RequestType;
import lineage2.gameserver.network.serverpackets.ExRegistPartySubstitute;
import lineage2.gameserver.network.serverpackets.ExRegistWaitingSubstituteOk;

/**
 * @author ALF
 * @date 22.08.2012
 */
public class PartySubstituteTask extends RunnableImpl
{
	@Override
	public void runImpl()
	{
		ConcurrentMap<Player, Integer> _wPlayers = PartySubstitute.getInstance().getWaitingPlayer();
		Set<Player> _wPartys = PartySubstitute.getInstance().getWaitingParty();
		Set<Entry<Player, Integer>> sets = _wPlayers.entrySet();
		
		for (Entry<Player, Integer> e : sets)
		{
			Player p = e.getKey();
			
			if (e.getValue() > 4)
			{
				PartySubstitute.getInstance().removePlayerReplace(p);
				p.getParty().getPartyLeader().sendPacket(new ExRegistPartySubstitute(p.getObjectId(), ExRegistPartySubstitute.REGISTER_TIMEOUT));
				continue;
			}
			
			for (Player pp : _wPartys)
			{
				if (PartySubstitute.getInstance().isGoodPlayer(p, pp))
				{
					if (pp.isProcessingRequest())
					{
						continue;
					}
					
					new Request(L2RequestType.SUBSTITUTE, p, pp).setTimeout(10000L);
					pp.sendPacket(new ExRegistWaitingSubstituteOk(p.getParty(), p));
					break;
				}
			}
			
			PartySubstitute.getInstance().updatePlayerToReplace(p, e.getValue() + 1);
		}
	}
}
