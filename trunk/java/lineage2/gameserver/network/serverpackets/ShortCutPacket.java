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
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.actor.instances.player.ShortCut;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.skills.TimeStamp;

public abstract class ShortCutPacket extends L2GameServerPacket
{
	public static ShortcutInfo convert(Player player, ShortCut shortCut)
	{
		ShortcutInfo shortcutInfo;
		int page = shortCut.getSlot() + (shortCut.getPage() * 12);
		int reuseGroup = -1;
		switch (shortCut.getType())
		{
			case ShortCut.TYPE_ITEM:
				int currentReuse = 0,
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
				Skill skill = player.getKnownSkill(shortCut.getId());
				if (skill != null)
				{
					reuseGroup = skill.getReuseGroupId();
				}
				shortcutInfo = new SkillShortcutInfo(shortCut.getType(), page, shortCut.getId(), shortCut.getLevel(), reuseGroup, shortCut.getCharacterType());
				break;
			default:
				shortcutInfo = new ShortcutInfo(shortCut.getType(), page, shortCut.getId(), shortCut.getCharacterType());
				break;
		}
		return shortcutInfo;
	}
	
	protected static class ItemShortcutInfo extends ShortcutInfo
	{
		private final int _reuseGroup;
		private final int _currentReuse;
		private final int _basicReuse;
		private final int _augmentationId;
		
		public ItemShortcutInfo(int type, int page, int id, int reuseGroup, int currentReuse, int basicReuse, int augmentationId, int characterType)
		{
			super(type, page, id, characterType);
			_reuseGroup = reuseGroup;
			_currentReuse = currentReuse;
			_basicReuse = basicReuse;
			_augmentationId = augmentationId;
		}
		
		@Override
		protected void write0(ShortCutPacket p)
		{
			p.writeD(_id);
			p.writeD(_characterType);
			p.writeD(_reuseGroup);
			p.writeD(_currentReuse);
			p.writeD(_basicReuse);
			p.writeD(_augmentationId);
			if (p.getClient().getActiveChar().isTautiClient())
			{
				p.writeD(0x00);
			}
		}
	}
	
	protected static class SkillShortcutInfo extends ShortcutInfo
	{
		private final int _level;
		private final int reuseGroup;
		
		public SkillShortcutInfo(int type, int page, int id, int level, int reuseGroup, int characterType)
		{
			super(type, page, id, characterType);
			_level = level;
			this.reuseGroup = reuseGroup;
		}
		
		public int getLevel()
		{
			return _level;
		}
		
		@Override
		protected void write0(ShortCutPacket p)
		{
			p.writeD(_id);
			p.writeD(_level);
			p.writeD(reuseGroup);
			p.writeC(0x00);
			p.writeD(_characterType);
		}
	}
	
	protected static class ShortcutInfo
	{
		protected final int _type;
		protected final int _page;
		protected final int _id;
		protected final int _characterType;
		
		public ShortcutInfo(int type, int page, int id, int characterType)
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
