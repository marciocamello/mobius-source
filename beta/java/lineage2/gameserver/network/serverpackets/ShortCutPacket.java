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
import lineage2.gameserver.model.actor.instances.player.ShortCut;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.skills.TimeStamp;

/**
 * @author VISTALL
 * @date 7:48/29.03.2011
 */
public abstract class ShortCutPacket extends L2GameServerPacket
{
	static ShortcutInfo convert(Player player, ShortCut shortCut)
	{
		ShortcutInfo shortcutInfo = null;
		int page = shortCut.getSlot() + (shortCut.getPage() * 12);
		
		switch (shortCut.getType())
		{
			case ShortCut.TYPE_ITEM:
				int reuseGroup = -1,
				currentReuse = 0,
				reuse = 0,
				augmentationId = 0;
				ItemInstance item = player.getInventory().getItemByObjectId(shortCut.getId());
				
				if (item != null)
				{
					augmentationId = item.getAugmentationId();
					reuseGroup = item.getTemplate().getDisplayReuseGroup();
					
					if (item.getTemplate().getReuseDelay() > 0)
					{
						TimeStamp timeStamp = player.getSharedGroupReuse(item.getTemplate().getReuseGroup());
						
						if (timeStamp != null)
						{
							currentReuse = (int) (timeStamp.getReuseCurrent() / 1000L);
							reuse = (int) (timeStamp.getReuseBasic() / 1000L);
						}
					}
				}
				
				shortcutInfo = new ItemShortcutInfo(shortCut.getType(), page, shortCut.getId(), reuseGroup, currentReuse, reuse, augmentationId, shortCut.getCharacterType());
				break;
			
			case ShortCut.TYPE_SKILL:
				shortcutInfo = new SkillShortcutInfo(shortCut.getType(), page, shortCut.getId(), shortCut.getLevel(), shortCut.getCharacterType());
				break;
			
			default:
				shortcutInfo = new ShortcutInfo(shortCut.getType(), page, shortCut.getId(), shortCut.getCharacterType());
				break;
		}
		
		return shortcutInfo;
	}
	
	private static class ItemShortcutInfo extends ShortcutInfo
	{
		private final int _reuseGroup;
		private final int _currentReuse;
		private final int _basicReuse;
		
		ItemShortcutInfo(int type, int page, int id, int reuseGroup, int currentReuse, int basicReuse, int augmentationId, int characterType)
		{
			super(type, page, id, characterType);
			_reuseGroup = reuseGroup;
			_currentReuse = currentReuse;
			_basicReuse = basicReuse;
		}
		
		@Override
		protected void write0(ShortCutPacket p)
		{
			p.writeD(_id);
			p.writeD(_characterType);
			p.writeD(_reuseGroup);
			p.writeD(_currentReuse);
			p.writeD(_basicReuse);
			// p.writeD(_augmentationId); // DEL FOR TAUTI
			p.writeH(0x00); // TODO[K] - m.b aura? duble cast?
			p.writeH(0x00); // TODO[K] - m.b aura? duble cast?
			p.writeD(0x00); // Tauti
		}
	}
	
	private static class SkillShortcutInfo extends ShortcutInfo
	{
		private final int _level;
		
		SkillShortcutInfo(int type, int page, int id, int level, int characterType)
		{
			super(type, page, id, characterType);
			_level = level;
		}
		
		@Override
		protected void write0(ShortCutPacket p)
		{
			p.writeD(_id);
			p.writeD(_level);
			p.writeD(_id); // L2WT GOD
			p.writeC(0x00);
			p.writeD(_characterType);
		}
	}
	
	protected static class ShortcutInfo
	{
		private final int _type;
		private final int _page;
		protected final int _id;
		protected final int _characterType;
		
		ShortcutInfo(int type, int page, int id, int characterType)
		{
			_type = type;
			_page = page;
			_id = id;
			_characterType = characterType;
		}
		
		protected void write(ShortCutPacket p)
		{
			p.writeD(_type);
			p.writeD(_page);
			write0(p);
		}
		
		protected void write0(ShortCutPacket p)
		{
			p.writeD(_id);
			p.writeD(_characterType);
		}
	}
}
