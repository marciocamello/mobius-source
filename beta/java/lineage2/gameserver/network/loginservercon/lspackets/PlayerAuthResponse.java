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
package lineage2.gameserver.network.loginservercon.lspackets;

import lineage2.gameserver.Config;
import lineage2.gameserver.dao.AccountBonusDAO;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.actor.instances.player.Bonus;
import lineage2.gameserver.network.GameClient;
import lineage2.gameserver.network.loginservercon.LoginServerCommunication;
import lineage2.gameserver.network.loginservercon.ReceivablePacket;
import lineage2.gameserver.network.loginservercon.SessionKey;
import lineage2.gameserver.network.loginservercon.gspackets.PlayerInGame;
import lineage2.gameserver.network.serverpackets.CharacterSelectionInfo;
import lineage2.gameserver.network.serverpackets.ExLoginVitalityEffectInfo;
import lineage2.gameserver.network.serverpackets.LoginFail;
import lineage2.gameserver.network.serverpackets.ServerClose;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.network.serverpackets.components.SystemMessageId;
import lineage2.gameserver.utils.SecondaryPasswordAuth;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class PlayerAuthResponse extends ReceivablePacket
{
	private String account;
	private boolean authed;
	private int playOkId1;
	private int playOkId2;
	private int loginOkId1;
	private int loginOkId2;
	private double bonus;
	private int bonusExpire;
	private String _2ndPassword;
	private int _2ndWrongAttempts;
	private long _2ndUnbanTime;
	
	/**
	 * Method readImpl.
	 */
	@Override
	public void readImpl()
	{
		account = readS();
		authed = readC() == 1;
		
		if (authed)
		{
			playOkId1 = readD();
			playOkId2 = readD();
			loginOkId1 = readD();
			loginOkId2 = readD();
			bonus = readF();
			bonusExpire = readD();
			_2ndPassword = readS();
			_2ndWrongAttempts = readD();
			_2ndUnbanTime = readQ();
		}
	}
	
	/**
	 * Method runImpl.
	 */
	@Override
	protected void runImpl()
	{
		SessionKey skey = new SessionKey(loginOkId1, loginOkId2, playOkId1, playOkId2);
		GameClient client = LoginServerCommunication.getInstance().removeWaitingClient(account);
		
		if (client == null)
		{
			return;
		}
		
		if (authed && client.getSessionKey().equals(skey))
		{
			client.setAuthed(true);
			client.setState(GameClient.GameClientState.AUTHED);
			
			switch (Config.SERVICES_RATE_TYPE)
			{
				case Bonus.NO_BONUS:
					bonus = 1.;
					bonusExpire = 0;
					break;
				
				case Bonus.BONUS_GLOBAL_ON_GAMESERVER:
					double[] bonuses = AccountBonusDAO.getInstance().select(account);
					bonus = bonuses[0];
					bonusExpire = (int) bonuses[1];
					break;
			}
			
			client.setBonus(bonus);
			client.setBonusExpire(bonusExpire);
			
			if (Config.SECOND_AUTH_ENABLED)
			{
				client.setSecondaryAuth(new SecondaryPasswordAuth(client, _2ndPassword, _2ndWrongAttempts, _2ndUnbanTime));
			}
			
			GameClient oldClient = LoginServerCommunication.getInstance().addAuthedClient(client);
			
			if (oldClient != null)
			{
				oldClient.setAuthed(false);
				Player activeChar = oldClient.getActiveChar();
				
				if (activeChar != null)
				{
					activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.YOU_ARE_LOGGED_IN_TO_TWO_PLACES_IF_YOU_SUSPECT_ACCOUNT_THEFT_WE_RECOMMEND_CHANGING_YOUR_PASSWORD_SCANNING_YOUR_COMPUTER_FOR_VIRUSES_AND_USING_AN_ANTI_VIRUS_SOFTWARE));
					activeChar.logout();
				}
				else
				{
					oldClient.close(ServerClose.STATIC);
				}
			}
			
			sendPacket(new PlayerInGame(client.getLogin()));
			CharacterSelectionInfo csi = new CharacterSelectionInfo(client.getLogin(), client.getSessionKey().playOkID1);
			ExLoginVitalityEffectInfo vl = new ExLoginVitalityEffectInfo(csi.getCharInfo());
			client.sendPacket(csi, vl);
			client.setCharSelection(csi.getCharInfo());
		}
		else
		{
			client.close(new LoginFail(LoginFail.ACCESS_FAILED_TRY_LATER));
		}
	}
}
