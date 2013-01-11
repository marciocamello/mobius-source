/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package lineage2.gameserver.model.instances;

import lineage2.gameserver.dao.CharacterDAO;
import lineage2.gameserver.templates.npc.NpcTemplate;

public class StatueInstance extends NpcInstance
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final int _recordId;
	private final int _socialId;
	private final int _socialFrame;
	private final int _sex;
	private final int _hairStyle;
	private final int _hairColor;
	private final int _face;
	private int _necklace = 0;
	private int _head = 0;
	private int _classId = 0;
	private int _rHand = 0;
	private int _lHand = 0;
	private int _gloves = 0;
	private int _chest = 0;
	private int _pants = 0;
	private int _boots = 0;
	private int _cloak = 0;
	private int _hair1 = 0;
	private int _hair2 = 0;
	private int _race = 0;
	
	public StatueInstance(int objectId, NpcTemplate template, int playerObjId, int loc[], int items[], int appearance[])
	{
		super(objectId, template);
		_recordId = loc[4];
		_socialId = 0;
		_socialFrame = 0;
		_necklace = items[0];
		_head = items[1];
		_rHand = items[2];
		_lHand = items[3];
		_gloves = items[4];
		_chest = items[5];
		_pants = items[6];
		_boots = items[7];
		_cloak = items[8];
		_hair1 = items[9];
		_hair2 = items[10];
		setName(CharacterDAO.getInstance().getNameByObjectId(playerObjId));
		_classId = appearance[0];
		_race = appearance[1];
		_sex = appearance[2];
		_hairStyle = appearance[3];
		_hairColor = appearance[4];
		_face = appearance[5];
		setIsInvul(true);
		setXYZ(loc[0], loc[1], loc[2]);
		setHeading(loc[3]);
		spawnMe();
	}
	
	public int getRecordId()
	{
		return _recordId;
	}
	
	public int getSocialId()
	{
		return _socialId;
	}
	
	public int getSocialFrame()
	{
		return _socialFrame;
	}
	
	public int getClassId()
	{
		return _classId;
	}
	
	public int getRace()
	{
		return _race;
	}
	
	public int getSex()
	{
		return _sex;
	}
	
	public int getHairStyle()
	{
		return _hairStyle;
	}
	
	public int getHairColor()
	{
		return _hairColor;
	}
	
	public int getFace()
	{
		return _face;
	}
	
	public int getNecklace()
	{
		return _necklace;
	}
	
	public int getHead()
	{
		return _head;
	}
	
	public int getRHand()
	{
		return _rHand;
	}
	
	public int getLHand()
	{
		return _lHand;
	}
	
	public int getGloves()
	{
		return _gloves;
	}
	
	public int getChest()
	{
		return _chest;
	}
	
	public int getPants()
	{
		return _pants;
	}
	
	public int getBoots()
	{
		return _boots;
	}
	
	public int getCloak()
	{
		return _cloak;
	}
	
	public int getHair1()
	{
		return _hair1;
	}
	
	public int getHair2()
	{
		return _hair2;
	}
}
