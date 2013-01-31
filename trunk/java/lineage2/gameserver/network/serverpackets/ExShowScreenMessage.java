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

import lineage2.gameserver.network.serverpackets.components.NpcString;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ExShowScreenMessage extends NpcStringContainer
{
	/**
	 * @author Mobius
	 */
	public static enum ScreenMessageAlign
	{
		/**
		 * Field TOP_LEFT.
		 */
		TOP_LEFT,
		/**
		 * Field TOP_CENTER.
		 */
		TOP_CENTER,
		/**
		 * Field TOP_RIGHT.
		 */
		TOP_RIGHT,
		/**
		 * Field MIDDLE_LEFT.
		 */
		MIDDLE_LEFT,
		/**
		 * Field MIDDLE_CENTER.
		 */
		MIDDLE_CENTER,
		/**
		 * Field MIDDLE_RIGHT.
		 */
		MIDDLE_RIGHT,
		/**
		 * Field BOTTOM_CENTER.
		 */
		BOTTOM_CENTER,
		/**
		 * Field BOTTOM_RIGHT.
		 */
		BOTTOM_RIGHT,
	}
	
	/**
	 * Field SYSMSG_TYPE. (value is 0)
	 */
	public static final int SYSMSG_TYPE = 0;
	/**
	 * Field STRING_TYPE. (value is 1)
	 */
	public static final int STRING_TYPE = 1;
	/**
	 * Field _sysMessageId. Field _type.
	 */
	private final int _type, _sysMessageId;
	/**
	 * Field _effect. Field _big_font.
	 */
	private final boolean _big_font, _effect;
	/**
	 * Field _text_align.
	 */
	private final ScreenMessageAlign _text_align;
	/**
	 * Field _time.
	 */
	private final int _time;
	/**
	 * Field _unk.
	 */
	private final int _unk;
	
	/**
	 * Constructor for ExShowScreenMessage.
	 * @param text String
	 * @param time int
	 * @param text_align ScreenMessageAlign
	 * @param big_font boolean
	 */
	public ExShowScreenMessage(String text, int time, ScreenMessageAlign text_align, boolean big_font)
	{
		this(text, time, text_align, big_font, 1, -1, false);
	}
	
	/**
	 * Constructor for ExShowScreenMessage.
	 * @param text String
	 * @param time int
	 * @param text_align ScreenMessageAlign
	 * @param big_font boolean
	 * @param type int
	 * @param messageId int
	 * @param showEffect boolean
	 */
	public ExShowScreenMessage(String text, int time, ScreenMessageAlign text_align, boolean big_font, int type, int messageId, boolean showEffect)
	{
		super(NpcString.NONE, text);
		_type = type;
		_sysMessageId = messageId;
		_time = time;
		_text_align = text_align;
		_big_font = big_font;
		_effect = showEffect;
		_unk = 0;
	}
	
	/**
	 * Constructor for ExShowScreenMessage.
	 * @param t NpcString
	 * @param time int
	 * @param text_align ScreenMessageAlign
	 * @param params String[]
	 */
	public ExShowScreenMessage(NpcString t, int time, ScreenMessageAlign text_align, String... params)
	{
		this(t, time, text_align, true, STRING_TYPE, -1, false, 0, params);
	}
	
	/**
	 * Constructor for ExShowScreenMessage.
	 * @param npcString NpcString
	 * @param time int
	 * @param text_align ScreenMessageAlign
	 * @param big_font boolean
	 * @param params String[]
	 */
	public ExShowScreenMessage(NpcString npcString, int time, ScreenMessageAlign text_align, boolean big_font, String... params)
	{
		this(npcString, time, text_align, big_font, STRING_TYPE, -1, false, 0, params);
	}
	
	/**
	 * Constructor for ExShowScreenMessage.
	 * @param npcString NpcString
	 * @param time int
	 * @param text_align ScreenMessageAlign
	 * @param big_font boolean
	 * @param showEffect boolean
	 * @param params String[]
	 */
	public ExShowScreenMessage(NpcString npcString, int time, ScreenMessageAlign text_align, boolean big_font, boolean showEffect, String... params)
	{
		this(npcString, time, text_align, big_font, STRING_TYPE, -1, showEffect, 0, params);
	}
	
	/**
	 * Constructor for ExShowScreenMessage.
	 * @param npcString NpcString
	 * @param time int
	 * @param text_align ScreenMessageAlign
	 * @param big_font boolean
	 * @param type int
	 * @param systemMsg int
	 * @param showEffect boolean
	 * @param params String[]
	 */
	public ExShowScreenMessage(NpcString npcString, int time, ScreenMessageAlign text_align, boolean big_font, int type, int systemMsg, boolean showEffect, String... params)
	{
		this(npcString, time, text_align, big_font, type, systemMsg, showEffect, 0, params);
	}
	
	/**
	 * Constructor for ExShowScreenMessage.
	 * @param npcString NpcString
	 * @param time int
	 * @param text_align ScreenMessageAlign
	 * @param big_font boolean
	 * @param type int
	 * @param systemMsg int
	 * @param showEffect boolean
	 * @param unk int
	 * @param params String[]
	 */
	public ExShowScreenMessage(NpcString npcString, int time, ScreenMessageAlign text_align, boolean big_font, int type, int systemMsg, boolean showEffect, int unk, String... params)
	{
		super(npcString, params);
		_type = type;
		_sysMessageId = systemMsg;
		_time = time;
		_text_align = text_align;
		_big_font = big_font;
		_effect = showEffect;
		_unk = unk;
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected final void writeImpl()
	{
		writeEx(0x39);
		writeD(_type);
		writeD(_sysMessageId);
		writeD(_text_align.ordinal() + 1);
		writeD(0x00);
		writeD(_big_font ? 0 : 1);
		writeD(0x00);
		writeD(_unk);
		writeD(_effect ? 1 : 0);
		writeD(_time);
		writeD(0x01);
		writeElements();
	}
}
