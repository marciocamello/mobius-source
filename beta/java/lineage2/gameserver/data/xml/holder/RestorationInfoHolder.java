package lineage2.gameserver.data.xml.holder;

import lineage2.commons.data.xml.AbstractHolder;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.tables.SkillTable;
import lineage2.gameserver.templates.skill.restoration.RestorationInfo;
import gnu.trove.map.hash.TIntObjectHashMap;

public final class RestorationInfoHolder extends AbstractHolder
{
	private static final RestorationInfoHolder _instance = new RestorationInfoHolder();
	private final TIntObjectHashMap<RestorationInfo> _restorationInfoList = new TIntObjectHashMap<>();
	
	public static RestorationInfoHolder getInstance()
	{
		return _instance;
	}
	
	public void addRestorationInfo(RestorationInfo info)
	{
		_restorationInfoList.put(SkillTable.getSkillHashCode(info.getSkillId(), info.getSkillLvl()), info);
	}
	
	public RestorationInfo getRestorationInfo(Skill skill)
	{
		return getRestorationInfo(skill.getId(), skill.getLevel());
	}
	
	private RestorationInfo getRestorationInfo(int skillId, int skillLvl)
	{
		return _restorationInfoList.get(SkillTable.getSkillHashCode(skillId, skillLvl));
	}
	
	@Override
	public int size()
	{
		return _restorationInfoList.size();
	}
	
	@Override
	public void clear()
	{
		_restorationInfoList.clear();
	}
}
/*
 * Location: D:\JavaDecompiler\l2Scripts GoD September 2012 - DECOMPILED\gameserver.jar Qualified Name: l2s.gameserver.data.xml.holder.RestorationInfoHolder JD-Core Version: 0.6.2
 */