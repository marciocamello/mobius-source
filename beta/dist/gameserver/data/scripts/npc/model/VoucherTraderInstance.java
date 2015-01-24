package npc.model;

import lineage2.gameserver.data.xml.holder.MultiSellHolder;
import lineage2.gameserver.enums.ClassLevel;
import lineage2.gameserver.instancemanager.ReflectionManager;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.templates.npc.NpcTemplate;

/**
 * @author iqman
 */
public final class VoucherTraderInstance extends NpcInstance
{
	private static final int Trader = 33388;
	private static final int SignOfAllegiance = 17739;
	private static final int SignOfPledge = 17740;
	private static final int SignOfSincerity = 17741;
	private static final int SignOfWill = 17742;
	private static final int SealOfAllegiance = 17743;
	private static final int SealOfPledge = 17744;
	private static final int SealOfSincerity = 17745;
	private static final int SealOfWill = 17746;
	
	public VoucherTraderInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}
	
	@Override
	public void onMenuSelect(Player player, int ask, int reply)
	{
		switch (reply)
		{
			case 0:
				int sign = -1;
				int seal = -1;
				int exp = -1;
				
				switch (getId())
				{
					case 33385:
						sign = SignOfAllegiance;
						seal = SealOfAllegiance;
						exp = 60000000;
						break;
					
					case 33386:
						sign = SignOfPledge;
						seal = SealOfPledge;
						exp = 66000000;
						break;
					
					case 33387:
						sign = SignOfSincerity;
						seal = SealOfSincerity;
						exp = 68000000;
						break;
					
					case 33388:
						sign = SignOfWill;
						seal = SealOfWill;
						exp = 76000000;
						break;
				}
				
				if (player.getInventory().getCountOf(sign) == 0)
				{
					showChatWindow(player, "default/voucher_no_item.htm");
					return;
				}
				if ((player.getVar("" + getId() + "") != null) && (Long.parseLong(player.getVar("" + getId() + "")) > System.currentTimeMillis()))
				{
					showChatWindow(player, "default/voucher_time_left.htm");
					return;
				}
				
				player.setVar("" + getId() + "", String.valueOf(System.currentTimeMillis() + 86400000L), -1);
				Functions.removeItem(player, sign, 1);
				Functions.addItem(player, seal, 20);
				player.addExpAndSp(exp, 0);
				showChatWindow(player, "default/" + getId() + "-list.htm");
				return;
				
			case 1:
				switch (getId())
				{
					case 33385:
						MultiSellHolder.getInstance().SeparateAndSend(720, player, 0, Trader);
						break;
					
					case 33386:
						MultiSellHolder.getInstance().SeparateAndSend(723, player, 0, Trader);
						break;
					
					case 33387:
						MultiSellHolder.getInstance().SeparateAndSend(722, player, 0, Trader);
						break;
					
					case 33388:
						MultiSellHolder.getInstance().SeparateAndSend(721, player, 0, Trader);
						break;
				}
				return;
				
			case 2:
				showChatWindow(player, "default/voucher_info.htm");
				return;
				
			case 3:
				Reflection instance = player.getReflection();
				if (instance != null)
				{
					if (instance.getReturnLoc() != null)
					{
						player.teleToLocation(instance.getReturnLoc(), ReflectionManager.DEFAULT);
					}
					else
					{
						player.setReflection(ReflectionManager.DEFAULT);
					}
				}
				return;
				
			case 4:
				showChatWindow(player, "default/voucher_talisman_select.htm");
				return;
				
			case 11:
				showChatWindow(player, "default/voucher_talisman_select_class.htm");
				return;
				
			case 12:
				if (!player.getClassId().isOfLevel(ClassLevel.Fourth))
				{
					showChatWindow(player, "default/voucher_talisman_no_class.htm");
					return;
				}
				int npcOffset = (getId() - 33385) * 8;
				
				switch (player.getClassId().getId())
				{
					case 148:
					case 149:
					case 150:
					case 151:
						MultiSellHolder.getInstance().SeparateAndSend(735 + npcOffset, player, 0, Trader);
						break;
					
					case 152:
					case 153:
					case 154:
					case 155:
					case 156:
					case 157:
						MultiSellHolder.getInstance().SeparateAndSend(736 + npcOffset, player, 0, Trader);
						break;
					
					case 158:
					case 159:
					case 160:
					case 161:
						MultiSellHolder.getInstance().SeparateAndSend(737 + npcOffset, player, 0, Trader);
						break;
					
					case 162:
					case 163:
					case 164:
					case 165:
						MultiSellHolder.getInstance().SeparateAndSend(738 + npcOffset, player, 0, Trader);
						break;
					
					case 166:
					case 167:
					case 168:
					case 169:
					case 170:
						MultiSellHolder.getInstance().SeparateAndSend(741 + npcOffset, player, 0, Trader);
						break;
					
					case 171:
					case 172:
					case 173:
					case 174:
					case 175:
						MultiSellHolder.getInstance().SeparateAndSend(740 + npcOffset, player, 0, Trader);
						break;
					
					case 176:
					case 177:
					case 178:
						MultiSellHolder.getInstance().SeparateAndSend(739 + npcOffset, player, 0, Trader);
						break;
					
					case 179:
					case 180:
					case 181:
						MultiSellHolder.getInstance().SeparateAndSend(742 + npcOffset, player, 0, Trader);
						break;
				}
		}
	}
}