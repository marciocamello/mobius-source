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
package lineage2.gameserver.network.serverpackets;

import lineage2.gameserver.model.Player;

/**
 * @author Smo
 */
public class ExResponseBeautyRegistResetPacket extends L2GameServerPacket
{
	private final int _type;
	private final int _result;
	private final int _hairStyle;
	private final int _hairColor;
	private final int _face;
	private final long _adena;
	private final long _coins;
	
	public ExResponseBeautyRegistResetPacket(Player player, int type, int result)
	{
		_type = type;
		_result = result;
		_hairStyle = ((player.getNewHairStyle() > 0) ? player.getNewHairStyle() : player.getHairStyle());
		_hairColor = ((player.getNewHairColor() > 0) ? player.getNewHairColor() : player.getHairColor());
		_face = ((player.getNewFace() > 0) ? player.getNewFace() : player.getFace());
		_adena = player.getAdena();
		_coins = player.getBeautyShopCoin();
	}
	
	@Override
	protected void writeImpl()
	{
		writeEx(0x140);
		writeQ(_adena);
		writeQ(_coins);
		writeD(_type);
		writeD(_result);
		writeD(_hairStyle);
		writeD(_face);
		writeD(_hairColor);
	}
}