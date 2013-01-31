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
package handler.items;

import java.util.ArrayList;
import java.util.List;

import lineage2.commons.util.Rnd;
import lineage2.gameserver.cache.Msg;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.scripts.Functions;

import org.apache.commons.lang3.ArrayUtils;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class Extractable extends SimpleItemHandler
{
	/**
	 * Field ITEM_IDS.
	 */
	private static final int[] ITEM_IDS = new int[]
	{
		20069,
		20070,
		20071,
		20072,
		20073,
		20074,
		20210,
		20211,
		20215,
		20216,
		20217,
		20218,
		20219,
		20220,
		20227,
		20228,
		20229,
		20233,
		20234,
		20235,
		20239,
		20240,
		20241,
		20242,
		20243,
		20244,
		20251,
		20254,
		20278,
		20279,
		20041,
		20042,
		20043,
		20044,
		20035,
		20036,
		20037,
		20038,
		20039,
		20040,
		20060,
		20061,
		20326,
		20327,
		20329,
		20330,
		20059,
		20494,
		20493,
		20395,
		13281,
		13282,
		13283,
		13284,
		13285,
		13286,
		13287,
		13288,
		13289,
		13290,
		14267,
		14268,
		14269,
		13280,
		14616,
		20575,
		20804,
		20807,
		20805,
		20808,
		20806,
		20809,
		20842,
		20843,
		20844,
		20845,
		20846,
		20847,
		20848,
		20849,
		20850,
		20851,
		20852,
		20853,
		20854,
		20855,
		20856,
		20857,
		20858,
		20859,
		20860,
		20861,
		20862,
		20863,
		20864,
		20811,
		20812,
		20813,
		20814,
		20815,
		20816,
		20817,
		20810,
		20865,
		20748,
		20749,
		20750,
		20751,
		20752,
		13777,
		13778,
		13779,
		13780,
		13781,
		13782,
		13783,
		13784,
		13785,
		13786,
		14849,
		13988,
		13989,
		13990,
		13991,
		13992,
		14850,
		14713,
		14714,
		14715,
		14716,
		14717,
		14718,
		13270,
		13271,
		13272,
		14231,
		14232,
		21747,
		21748,
		21749,
		17169,
		21169,
		21753,
		21752,
		33465,
		32264,
		32265,
		32266,
		32267,
		32268,
		32269,
		32270,
		32271,
		33477,
		34694,
		34695,
		34696,
		34697,
		34698,
		30281,
		30279,
		30277,
		6007
	};
	
	/**
	 * Method getItemIds.
	 * @return int[]
	 * @see lineage2.gameserver.handler.items.IItemHandler#getItemIds()
	 */
	@Override
	public int[] getItemIds()
	{
		return ITEM_IDS;
	}
	
	/**
	 * Method useItemImpl.
	 * @param player Player
	 * @param item ItemInstance
	 * @param ctrl boolean
	 * @return boolean
	 */
	@Override
	protected boolean useItemImpl(Player player, ItemInstance item, boolean ctrl)
	{
		int itemId = item.getItemId();
		
		if (!canBeExtracted(player, item))
		{
			return false;
		}
		
		if (!useItem(player, item, 1))
		{
			return false;
		}
		
		switch (itemId)
		{
			case 8534:
				use8534(player, ctrl);
				break;
			case 8535:
				use8535(player, ctrl);
				break;
			case 8536:
				use8536(player, ctrl);
				break;
			case 8537:
				use8537(player, ctrl);
				break;
			case 8538:
				use8538(player, ctrl);
				break;
			case 8539:
				use8539(player, ctrl);
				break;
			case 8540:
				use8540(player, ctrl);
				break;
			case 5916:
				use5916(player, ctrl);
				break;
			case 5944:
				use5944(player, ctrl);
				break;
			case 14841:
				use14841(player, ctrl);
				break;
			case 14847:
				use14847(player, ctrl);
				break;
			case 5966:
				use5966(player, ctrl);
				break;
			case 5967:
				use5967(player, ctrl);
				break;
			case 5968:
				use5968(player, ctrl);
				break;
			case 5969:
				use5969(player, ctrl);
				break;
			case 6007:
				use6007(player, ctrl);
				break;
			case 6008:
				use6008(player, ctrl);
				break;
			case 6009:
				use6009(player, ctrl);
				break;
			case 6010:
				use6010(player, ctrl);
				break;
			case 7725:
				use7725(player, ctrl);
				break;
			case 7637:
				use7637(player, ctrl);
				break;
			case 7636:
				use7636(player, ctrl);
				break;
			case 7629:
				use7629(player, ctrl);
				break;
			case 7630:
				use7630(player, ctrl);
				break;
			case 7631:
				use7631(player, ctrl);
				break;
			case 7632:
				use7632(player, ctrl);
				break;
			case 7633:
				use7633(player, ctrl);
				break;
			case 7634:
				use7634(player, ctrl);
				break;
			case 7635:
				use7635(player, ctrl);
				break;
			case 10408:
				use10408(player, ctrl);
				break;
			case 10473:
				use10473(player, ctrl);
				break;
			case 9599:
				use9599(player, ctrl);
				break;
			case 20069:
				use20069(player, ctrl);
				break;
			case 20070:
				use20070(player, ctrl);
				break;
			case 20071:
				use20071(player, ctrl);
				break;
			case 20072:
				use20072(player, ctrl);
				break;
			case 20073:
				use20073(player, ctrl);
				break;
			case 20074:
				use20074(player, ctrl);
				break;
			case 20210:
				use20210(player, ctrl);
				break;
			case 20211:
				use20211(player, ctrl);
				break;
			case 20215:
				use20215(player, ctrl);
				break;
			case 20216:
				use20216(player, ctrl);
				break;
			case 20217:
				use20217(player, ctrl);
				break;
			case 20218:
				use20218(player, ctrl);
				break;
			case 20219:
				use20219(player, ctrl);
				break;
			case 20220:
				use20220(player, ctrl);
				break;
			case 20227:
				use20227(player, ctrl);
				break;
			case 20228:
				use20228(player, ctrl);
				break;
			case 20229:
				use20229(player, ctrl);
				break;
			case 20233:
				use20233(player, ctrl);
				break;
			case 20234:
				use20234(player, ctrl);
				break;
			case 20235:
				use20235(player, ctrl);
				break;
			case 20239:
				use20239(player, ctrl);
				break;
			case 20240:
				use20240(player, ctrl);
				break;
			case 20241:
				use20241(player, ctrl);
				break;
			case 20242:
				use20242(player, ctrl);
				break;
			case 20243:
				use20243(player, ctrl);
				break;
			case 20244:
				use20244(player, ctrl);
				break;
			case 20251:
				use20251(player, ctrl);
				break;
			case 20254:
				use20254(player, ctrl);
				break;
			case 20278:
				use20278(player, ctrl);
				break;
			case 20279:
				use20279(player, ctrl);
				break;
			case 20041:
				use20041(player, ctrl);
				break;
			case 20042:
				use20042(player, ctrl);
				break;
			case 20043:
				use20043(player, ctrl);
				break;
			case 20044:
				use20044(player, ctrl);
				break;
			case 20035:
				use20035(player, ctrl);
				break;
			case 20036:
				use20036(player, ctrl);
				break;
			case 20037:
				use20037(player, ctrl);
				break;
			case 20038:
				use20038(player, ctrl);
				break;
			case 20039:
				use20039(player, ctrl);
				break;
			case 20040:
				use20040(player, ctrl);
				break;
			case 20060:
				use20060(player, ctrl);
				break;
			case 20061:
				use20061(player, ctrl);
				break;
			case 22000:
				use22000(player, ctrl);
				break;
			case 22001:
				use22001(player, ctrl);
				break;
			case 22002:
				use22002(player, ctrl);
				break;
			case 22003:
				use22003(player, ctrl);
				break;
			case 22004:
				use22004(player, ctrl);
				break;
			case 22005:
				use22005(player, ctrl);
				break;
			case 20326:
				use20326(player, ctrl);
				break;
			case 20327:
				use20327(player, ctrl);
				break;
			case 20329:
				use20329(player, ctrl);
				break;
			case 20330:
				use20330(player, ctrl);
				break;
			case 20059:
				use20059(player, ctrl);
				break;
			case 20494:
				use20494(player, ctrl);
				break;
			case 20493:
				use20493(player, ctrl);
				break;
			case 20395:
				use20395(player, ctrl);
				break;
			case 13281:
				use13281(player, ctrl);
				break;
			case 13282:
				use13282(player, ctrl);
				break;
			case 13283:
				use13283(player, ctrl);
				break;
			case 13284:
				use13284(player, ctrl);
				break;
			case 13285:
				use13285(player, ctrl);
				break;
			case 13286:
				use13286(player, ctrl);
				break;
			case 13287:
				use13287(player, ctrl);
				break;
			case 13288:
				use13288(player, ctrl);
				break;
			case 13289:
				use13289(player, ctrl);
				break;
			case 13290:
				use13290(player, ctrl);
				break;
			case 14267:
				use14267(player, ctrl);
				break;
			case 14268:
				use14268(player, ctrl);
				break;
			case 14269:
				use14269(player, ctrl);
				break;
			case 13280:
				use13280(player, ctrl);
				break;
			case 22087:
				use22087(player, ctrl);
				break;
			case 22088:
				use22088(player, ctrl);
				break;
			case 13713:
				use13713(player, ctrl);
				break;
			case 13714:
				use13714(player, ctrl);
				break;
			case 13715:
				use13715(player, ctrl);
				break;
			case 13716:
				use13716(player, ctrl);
				break;
			case 13717:
				use13717(player, ctrl);
				break;
			case 13718:
				use13718(player, ctrl);
				break;
			case 13719:
				use13719(player, ctrl);
				break;
			case 13720:
				use13720(player, ctrl);
				break;
			case 13721:
				use13721(player, ctrl);
				break;
			case 14549:
				use14549(player, ctrl);
				break;
			case 14550:
				use14550(player, ctrl);
				break;
			case 14551:
				use14551(player, ctrl);
				break;
			case 14552:
				use14552(player, ctrl);
				break;
			case 14553:
				use14553(player, ctrl);
				break;
			case 14554:
				use14554(player, ctrl);
				break;
			case 14555:
				use14555(player, ctrl);
				break;
			case 14556:
				use14556(player, ctrl);
				break;
			case 14557:
				use14557(player, ctrl);
				break;
			case 13695:
				use13695(player, ctrl);
				break;
			case 13696:
				use13696(player, ctrl);
				break;
			case 13697:
				use13697(player, ctrl);
				break;
			case 13698:
				use13698(player, ctrl);
				break;
			case 13699:
				use13699(player, ctrl);
				break;
			case 13700:
				use13700(player, ctrl);
				break;
			case 13701:
				use13701(player, ctrl);
				break;
			case 13702:
				use13702(player, ctrl);
				break;
			case 13703:
				use13703(player, ctrl);
				break;
			case 14531:
				use14531(player, ctrl);
				break;
			case 14532:
				use14532(player, ctrl);
				break;
			case 14533:
				use14533(player, ctrl);
				break;
			case 14534:
				use14534(player, ctrl);
				break;
			case 14535:
				use14535(player, ctrl);
				break;
			case 14536:
				use14536(player, ctrl);
				break;
			case 14537:
				use14537(player, ctrl);
				break;
			case 14538:
				use14538(player, ctrl);
				break;
			case 14539:
				use14539(player, ctrl);
				break;
			case 13704:
				use13704(player, ctrl);
				break;
			case 13705:
				use13705(player, ctrl);
				break;
			case 13706:
				use13706(player, ctrl);
				break;
			case 13707:
				use13707(player, ctrl);
				break;
			case 13708:
				use13708(player, ctrl);
				break;
			case 13709:
				use13709(player, ctrl);
				break;
			case 13710:
				use13710(player, ctrl);
				break;
			case 13711:
				use13711(player, ctrl);
				break;
			case 13712:
				use13712(player, ctrl);
				break;
			case 14540:
				use14540(player, ctrl);
				break;
			case 14541:
				use14541(player, ctrl);
				break;
			case 14542:
				use14542(player, ctrl);
				break;
			case 14543:
				use14543(player, ctrl);
				break;
			case 14544:
				use14544(player, ctrl);
				break;
			case 14545:
				use14545(player, ctrl);
				break;
			case 14546:
				use14546(player, ctrl);
				break;
			case 14547:
				use14547(player, ctrl);
				break;
			case 14548:
				use14548(player, ctrl);
				break;
			case 14884:
				use14884(player, ctrl);
				break;
			case 14885:
				use14885(player, ctrl);
				break;
			case 14886:
				use14886(player, ctrl);
				break;
			case 14887:
				use14887(player, ctrl);
				break;
			case 14888:
				use14888(player, ctrl);
				break;
			case 14889:
				use14889(player, ctrl);
				break;
			case 14890:
				use14890(player, ctrl);
				break;
			case 14891:
				use14891(player, ctrl);
				break;
			case 14892:
				use14892(player, ctrl);
				break;
			case 14893:
				use14893(player, ctrl);
				break;
			case 14894:
				use14894(player, ctrl);
				break;
			case 14895:
				use14895(player, ctrl);
				break;
			case 14896:
				use14896(player, ctrl);
				break;
			case 14897:
				use14897(player, ctrl);
				break;
			case 14898:
				use14898(player, ctrl);
				break;
			case 14899:
				use14899(player, ctrl);
				break;
			case 14900:
				use14900(player, ctrl);
				break;
			case 14901:
				use14901(player, ctrl);
				break;
			case 14616:
				use14616(player, ctrl);
				break;
			case 20575:
				use20575(player, ctrl);
				break;
			case 20804:
				use20804(player, ctrl);
				break;
			case 20807:
				use20807(player, ctrl);
				break;
			case 20805:
				use20805(player, ctrl);
				break;
			case 20808:
				use20808(player, ctrl);
				break;
			case 20806:
				use20806(player, ctrl);
				break;
			case 20809:
				use20809(player, ctrl);
				break;
			case 20842:
				use20842(player, ctrl);
				break;
			case 20843:
				use20843(player, ctrl);
				break;
			case 20844:
				use20844(player, ctrl);
				break;
			case 20845:
				use20845(player, ctrl);
				break;
			case 20846:
				use20846(player, ctrl);
				break;
			case 20847:
				use20847(player, ctrl);
				break;
			case 20848:
				use20848(player, ctrl);
				break;
			case 20849:
				use20849(player, ctrl);
				break;
			case 20850:
				use20850(player, ctrl);
				break;
			case 20851:
				use20851(player, ctrl);
				break;
			case 20852:
				use20852(player, ctrl);
				break;
			case 20853:
				use20853(player, ctrl);
				break;
			case 20854:
				use20854(player, ctrl);
				break;
			case 20855:
				use20855(player, ctrl);
				break;
			case 20856:
				use20856(player, ctrl);
				break;
			case 20857:
				use20857(player, ctrl);
				break;
			case 20858:
				use20858(player, ctrl);
				break;
			case 20859:
				use20859(player, ctrl);
				break;
			case 20860:
				use20860(player, ctrl);
				break;
			case 20861:
				use20861(player, ctrl);
				break;
			case 20862:
				use20862(player, ctrl);
				break;
			case 20863:
				use20863(player, ctrl);
				break;
			case 20864:
				use20864(player, ctrl);
				break;
			case 20811:
				use20811(player, ctrl);
				break;
			case 20812:
				use20812(player, ctrl);
				break;
			case 20813:
				use20813(player, ctrl);
				break;
			case 20814:
				use20814(player, ctrl);
				break;
			case 20815:
				use20815(player, ctrl);
				break;
			case 20816:
				use20816(player, ctrl);
				break;
			case 20817:
				use20817(player, ctrl);
				break;
			case 20810:
				use20810(player, ctrl);
				break;
			case 20865:
				use20865(player, ctrl);
				break;
			case 20748:
				use20748(player, ctrl);
				break;
			case 20749:
				use20749(player, ctrl);
				break;
			case 20750:
				use20750(player, ctrl);
				break;
			case 20751:
				use20751(player, ctrl);
				break;
			case 20752:
				use20752(player, ctrl);
				break;
			case 20195:
				use20195(player, ctrl);
				break;
			case 20196:
				use20196(player, ctrl);
				break;
			case 20197:
				use20197(player, ctrl);
				break;
			case 20198:
				use20198(player, ctrl);
				break;
			case 13777:
				use13777(player, ctrl);
				break;
			case 13778:
				use13778(player, ctrl);
				break;
			case 13779:
				use13779(player, ctrl);
				break;
			case 13780:
				use13780(player, ctrl);
				break;
			case 13781:
				use13781(player, ctrl);
				break;
			case 13782:
				use13782(player, ctrl);
				break;
			case 13783:
				use13783(player, ctrl);
				break;
			case 13784:
				use13784(player, ctrl);
				break;
			case 13785:
				use13785(player, ctrl);
				break;
			case 13786:
				use13786(player, ctrl);
				break;
			case 14849:
				use14849(player, ctrl);
				break;
			case 14834:
				use14834(player, ctrl);
				break;
			case 14833:
				use14833(player, ctrl);
				break;
			case 13988:
				use13988(player, ctrl);
				break;
			case 13989:
				use13989(player, ctrl);
				break;
			case 13003:
				use13003(player, ctrl);
				break;
			case 13004:
				use13004(player, ctrl);
				break;
			case 13005:
				use13005(player, ctrl);
				break;
			case 13006:
				use13006(player, ctrl);
				break;
			case 13007:
				use13007(player, ctrl);
				break;
			case 13990:
				use13990(player, ctrl);
				break;
			case 13991:
				use13991(player, ctrl);
				break;
			case 13992:
				use13992(player, ctrl);
				break;
			case 14850:
				use14850(player, ctrl);
				break;
			case 14713:
				use14713(player, ctrl);
				break;
			case 14714:
				use14714(player, ctrl);
				break;
			case 14715:
				use14715(player, ctrl);
				break;
			case 14716:
				use14716(player, ctrl);
				break;
			case 14717:
				use14717(player, ctrl);
				break;
			case 14718:
				use14718(player, ctrl);
				break;
			case 17138:
				use17138(player, ctrl);
				break;
			case 15482:
				use15482(player, ctrl);
				break;
			case 15483:
				use15483(player, ctrl);
				break;
			case 13270:
				use13270(player, ctrl);
				break;
			case 13271:
				use13271(player, ctrl);
				break;
			case 13272:
				use13272(player, ctrl);
				break;
			case 14231:
				use14231(player, ctrl);
				break;
			case 14232:
				use14232(player, ctrl);
				break;
			case 21747:
				use21747(player, ctrl);
				break;
			case 21748:
				use21748(player, ctrl);
				break;
			case 21749:
				use21749(player, ctrl);
				break;
			case 17169:
				use17169(player, ctrl);
				break;
			case 21169:
				use21169(player, ctrl);
				break;
			case 21753:
				use21753(player, ctrl);
				break;
			case 21752:
				use21752(player, ctrl);
				break;
			case 33465:
				use33465(player, ctrl);
				break;
			case 32264:
				use32264(player, ctrl);
				break;
			case 32265:
				use32265(player, ctrl);
				break;
			case 32266:
				use32266(player, ctrl);
				break;
			case 32267:
				use32267(player, ctrl);
				break;
			case 32268:
				use32268(player, ctrl);
				break;
			case 32269:
				use32269(player, ctrl);
				break;
			case 32270:
				use32270(player, ctrl);
				break;
			case 32271:
				use32271(player, ctrl);
				break;
			case 33477:
				use33477(player, ctrl);
				break;
			case 34694:
				use34694(player, ctrl);
				break;
			case 34695:
				use34695(player, ctrl);
				break;
			case 34696:
				use34696(player, ctrl);
				break;
			case 34697:
				use34697(player, ctrl);
				break;
			case 34698:
				use34698(player, ctrl);
				break;
			case 30281:
				use30281(player, ctrl);
				break;
			case 30279:
				use30279(player, ctrl);
				break;
			case 30277:
				use30277(player, ctrl);
				break;
			default:
				return false;
		}
		return true;
	}
	
	// ------ Adventurer's Boxes ------
	
	// Adventurer's Box: C-Grade Accessory (Low Grade)
	private void use8534(Player player, boolean ctrl)
	{
        int[] list = new int[]{853, 916, 884};
        int[] chances = new int[]{17, 17, 17};
        int[] counts = new int[]{1, 1, 1};
		extract_item_r(list, counts, chances, player);
	}
	
	// Adventurer's Box: C-Grade Accessory (Medium Grade)
	private void use8535(Player player, boolean ctrl)
	{
        int[] list = new int[]{854, 917, 885};
        int[] chances = new int[]{17, 17, 17};
        int[] counts = new int[]{1, 1, 1};
		extract_item_r(list, counts, chances, player);
	}
	
	// Adventurer's Box: C-Grade Accessory (High Grade)
	private void use8536(Player player, boolean ctrl)
	{
        int[] list = new int[]{855, 119, 886};
        int[] chances = new int[]{17, 17, 17};
        int[] counts = new int[]{1, 1, 1};
        extract_item_r(list, counts, chances, player);
	}
	
	// Adventurer's Box: B-Grade Accessory (Low Grade)
	private void use8537(Player player, boolean ctrl)
	{
        int[] list = new int[]{856, 918, 887};
        int[] chances = new int[]{17, 17, 17};
        int[] counts = new int[]{1, 1, 1};
        extract_item_r(list, counts, chances, player);
	}
	
	// Adventurer's Box: B-Grade Accessory (High Grade)
	private void use8538(Player player, boolean ctrl)
	{
        int[] list = new int[]{864, 926, 895};
        int[] chances = new int[]{17, 17, 17};
        int[] counts = new int[]{1, 1, 1};
		extract_item_r(list, counts, chances, player);
	}
	
	// Adventurer's Box: Hair Accessory
	private void use8539(Player player, boolean ctrl)
	{
        int[] list = new int[]{8179, 8178, 8177};
        int[] chances = new int[]{10, 20, 30};
        int[] counts = new int[]{1, 1, 1};
		extract_item_r(list, counts, chances, player);
	}
	
	// Adventurer's Box: Cradle of Creation
	private void use8540(Player player, boolean ctrl)
	{
		if (Rnd.chance(30))
		{
			Functions.addItem(player, 8175, 1);
		}
	}
	
	// Quest 370: A Wiseman Sows Seeds
	private void use5916(Player player, boolean ctrl)
	{
        int[] list = new int[]{5917, 5918, 5919, 5920, 736};
        int[] counts = new int[]{1, 1, 1, 1, 1};
		extract_item(list, counts, player);
	}
	
	// Quest 376: Giants Cave Exploration, Part 1
    private void use5944(Player player, boolean ctrl)
    {
        int[] list = {5922, 5923, 5924, 5925, 5926, 5927, 5928, 5929, 5930, 5931, 5932, 5933, 5934, 5935, 5936, 5937, 5938, 5939, 5940, 5941, 5942, 5943};
        int[] counts = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};

		if (ctrl)
		{
			long item_count = 1 + Functions.removeItem(player, 5944, Functions.getItemCount(player, 5944));
			for (int[] res : mass_extract_item(item_count, list, counts, player))
			{
				Functions.addItem(player, res[0], res[1]);
			}
		}
		else
		{
			extract_item(list, counts, player);
		}
	}
	
	// Quest 376: Giants Cave Exploration, Part 1
    private void use14841(Player player, boolean ctrl)
    {
        int[] list = {14836, 14837, 14838, 14839, 14840};
        int[] counts = {1, 1, 1, 1, 1};

		if (ctrl)
		{
			long item_count = 1 + Functions.removeItem(player, 14841, Functions.getItemCount(player, 14841));
			for (int[] res : mass_extract_item(item_count, list, counts, player))
			{
				Functions.addItem(player, res[0], res[1]);
			}
		}
		else
		{
			extract_item(list, counts, player);
		}
	}
	
	// Quest 377: Giants Cave Exploration, Part 2, new
	private void use14847(Player player, boolean ctrl)
	{
        int[] list = {14842, 14843, 14844, 14845, 14846};
        int[] counts = {1, 1, 1, 1, 1};

		if (ctrl)
		{
			long item_count = 1 + Functions.removeItem(player, 14847, Functions.getItemCount(player, 14847));
			for (int[] res : mass_extract_item(item_count, list, counts, player))
			{
				Functions.addItem(player, res[0], res[1]);
			}
		}
		else
		{
			extract_item(list, counts, player);
		}
	}
	
    // Quest 372: Legacy of Insolence
    private void use5966(Player player, boolean ctrl)
    {
        int[] list = new int[]{5970, 5971, 5977, 5978, 5979, 5986, 5993, 5994, 5995, 5997, 5983, 6001};
        int[] counts = new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        extract_item(list, counts, player);
    }

    // Quest 372: Legacy of Insolence
    private void use5967(Player player, boolean ctrl)
    {
        int[] list = new int[]{5970, 5971, 5975, 5976, 5980, 5985, 5993, 5994, 5995, 5997, 5983, 6001};
        int[] counts = new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        extract_item(list, counts, player);
    }

    // Quest 372: Legacy of Insolence
    private void use5968(Player player, boolean ctrl)
    {
        int[] list = new int[]{5973, 5974, 5981, 5984, 5989, 5990, 5991, 5992, 5996, 5998, 5999, 6000, 5988, 5983, 6001};
        int[] counts = new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        extract_item(list, counts, player);
    }

    // Quest 372: Legacy of Insolence
    private void use5969(Player player, boolean ctrl)
    {
        int[] list = new int[]{5970, 5971, 5982, 5987, 5989, 5990, 5991, 5992, 5996, 5998, 5999, 6000, 5972, 6001};
        int[] counts = new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        extract_item(list, counts, player);
    }

    /**
     * Quest 373: Supplier of Reagents, from Hallate's Maid, Reagent Pouch (Gray)
     * 2x Quicksilver (6019) 30%
     * 2x Moonstone Shard (6013) 30%
     * 1x Rotten Bone Piece (6014) 20%
     * 1x Infernium Ore (6016) 20%
     */
    private void use6007(Player player, boolean ctrl)
    {
        int[] list = new int[]{6019, 6013, 6014, 6016};
        int[] counts = new int[]{2, 2, 1, 1};
        int[] chances = new int[]{30, 30, 20, 20};

		if (ctrl)
		{
			long item_count = 1 + Functions.removeItem(player, 6007, Functions.getItemCount(player, 6007));
			for (int[] res : mass_extract_item_r(item_count, list, counts, chances, player))
			{
				Functions.addItem(player, res[0], res[1]);
			}
		}
		else
		{
			extract_item_r(list, counts, chances, player);
		}
	}
	
    /**
     * Quest 373: Supplier of Reagents, from Platinum Tribe Shaman, Reagent Pouch (Yellow)
     * 2x Blood Root (6017) 10%
     * 2x Sulfur (6020) 20%
     * 1x Rotten Bone Piece (6014) 35%
     * 1x Infernium Ore (6016) 35%
     */
    private void use6008(Player player, boolean ctrl)
    {
        int[] list = new int[]{6017, 6020, 6014, 6016};
        int[] counts = new int[]{2, 2, 1, 1};
        int[] chances = new int[]{10, 20, 35, 35};

		if (ctrl)
		{
			long item_count = 1 + Functions.removeItem(player, 6008, Functions.getItemCount(player, 6008));
			for (int[] res : mass_extract_item_r(item_count, list, counts, chances, player))
			{
				Functions.addItem(player, res[0], res[1]);
			}
		}
		else
		{
			extract_item_r(list, counts, chances, player);
		}
	}
	
    /**
     * Quest 373: Supplier of Reagents, from Hames Orc Shaman, Reagent Pouch (Brown)
     * 1x Lava Stone (6012) 20%
     * 2x Volcanic Ash (6018) 20%
     * 2x Quicksilver (6019) 20%
     * 1x Moonstone Shard (6013) 40%
     */
    private void use6009(Player player, boolean ctrl)
    {
        int[] list = new int[]{6012, 6018, 6019, 6013};
        int[] counts = new int[]{1, 2, 2, 1};
        int[] chances = new int[]{20, 20, 20, 40};

		if (ctrl)
		{
			long item_count = 1 + Functions.removeItem(player, 6009, Functions.getItemCount(player, 6009));
			for (int[] res : mass_extract_item_r(item_count, list, counts, chances, player))
			{
				Functions.addItem(player, res[0], res[1]);
			}
		}
		else
		{
			extract_item_r(list, counts, chances, player);
		}
	}
	
    /**
     * Quest 373: Supplier of Reagents, from Platinum Guardian Shaman, Reagent Box
     * 2x Blood Root (6017) 20%
     * 2x Sulfur (6020) 20%
     * 1x Infernium Ore (6016) 35%
     * 2x Demon's Blood (6015) 25%
     */
    private void use6010(Player player, boolean ctrl)
    {
        int[] list = new int[]{6017, 6020, 6016, 6015};
        int[] counts = new int[]{2, 2, 1, 2};
        int[] chances = new int[]{20, 20, 35, 25};

		if (ctrl)
		{
			long item_count = 1 + Functions.removeItem(player, 6010, Functions.getItemCount(player, 6010));
			for (int[] res : mass_extract_item_r(item_count, list, counts, chances, player))
			{
				Functions.addItem(player, res[0], res[1]);
			}
		}
		else
		{
			extract_item_r(list, counts, chances, player);
		}
	}
	
    // Quest 628: Hunt of Golden Ram
    private void use7725(Player player, boolean ctrl)
    {
        int[] list = new int[]{6035, 1060, 735, 1540, 1061, 1539};
        int[] counts = new int[]{1, 1, 1, 1, 1, 1};
        int[] chances = new int[]{7, 39, 7, 3, 12, 32};

		if (ctrl)
		{
			long item_count = 1 + Functions.removeItem(player, 7725, Functions.getItemCount(player, 7725));
			for (int[] res : mass_extract_item_r(item_count, list, counts, chances, player))
			{
				Functions.addItem(player, res[0], res[1]);
			}
		}
		else
		{
			extract_item_r(list, counts, chances, player);
		}
	}
	
    // Quest 628: Hunt of Golden Ram
    private void use7637(Player player, boolean ctrl)
    {
        int[] list = new int[]{4039, 4041, 4043, 4044, 4042, 4040};
        int[] counts = new int[]{4, 1, 4, 4, 2, 2};
        int[] chances = new int[]{20, 10, 20, 20, 15, 15};

		if (ctrl)
		{
			long item_count = 1 + Functions.removeItem(player, 7637, Functions.getItemCount(player, 7637));
			for (int[] res : mass_extract_item_r(item_count, list, counts, chances, player))
			{
				Functions.addItem(player, res[0], res[1]);
			}
		}
		else
		{
			extract_item_r(list, counts, chances, player);
		}
	}
	
    // Quest 628: Hunt of Golden Ram
    private void use7636(Player player, boolean ctrl)
    {
        int[] list = new int[]{1875, 1882, 1880, 1874, 1877, 1881, 1879, 1876};
        int[] counts = new int[]{3, 3, 4, 1, 3, 1, 3, 6};
        int[] chances = new int[]{10, 20, 10, 10, 10, 12, 12, 16};

		if (ctrl)
		{
			long item_count = 1 + Functions.removeItem(player, 7636, Functions.getItemCount(player, 7636));
			for (int[] res : mass_extract_item_r(item_count, list, counts, chances, player))
			{
				Functions.addItem(player, res[0], res[1]);
			}
		}
		else
		{
			extract_item_r(list, counts, chances, player);
		}
	}
	
    // Looted Goods - White Cargo box
    private void use7629(Player player, boolean ctrl)
    {
        int[] list = new int[]{6688, 6689, 6690, 6691, 6693, 6694, 6695, 6696, 6697, 7579, 57};
        int[] counts = new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 330000};
        int[] chances = new int[]{9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 10};
		extract_item_r(list, counts, chances, player);
	}
	
    // Looted Goods - Blue Cargo box #All chances of 8 should be 8.5, must be fixed if possible!!
    private void use7630(Player player, boolean ctrl)
    {
        int[] list = new int[]{6703, 6704, 6705, 6706, 6708, 6709, 6710, 6712, 6713, 6714, 57};
        int[] counts = new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 292000};
        int[] chances = new int[]{8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 20};
        extract_item_r(list, counts, chances, player);
    }

    // Looted Goods - Yellow Cargo box
    private void use7631(Player player, boolean ctrl)
    {
        int[] list = new int[]{6701, 6702, 6707, 6711, 57};
        int[] counts = new int[]{1, 1, 1, 1, 930000};
        int[] chances = new int[]{20, 20, 20, 20, 20};
        extract_item_r(list, counts, chances, player);
    }

    // Looted Goods - Red Filing Cabinet
    private void use7632(Player player, boolean ctrl)
    {
        int[] list;
        list = new int[]{6857, 6859, 6861, 6863, 6867, 6869, 6871, 6875, 6877, 6879, 13100, 57};
        int[] counts = new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 340000};
        int[] chances = new int[]{8, 9, 8, 9, 8, 9, 8, 9, 8, 9, 8, 7};
        extract_item_r(list, counts, chances, player);
    }

    // Looted Goods - Purple Filing Cabinet
    private void use7633(Player player, boolean ctrl)
    {
        int[] list;
        list = new int[]{6853, 6855, 6865, 6873, 57};
        int[] counts = new int[]{1, 1, 1, 1, 850000};
        int[] chances = new int[]{20, 20, 20, 20, 20};
        extract_item_r(list, counts, chances, player);
    }

    // Looted Goods - Brown Pouch
    private void use7634(Player player, boolean ctrl)
    {
        int[] list = new int[]{1874, 1875, 1876, 1877, 1879, 1880, 1881, 1882, 57};
        int[] counts = new int[]{20, 20, 20, 20, 20, 20, 20, 20, 150000};
        int[] chances = new int[]{10, 10, 16, 11, 10, 5, 10, 18, 10};
        extract_item_r(list, counts, chances, player);
    }

    // Looted Goods - Gray Pouch
    private void use7635(Player player, boolean ctrl)
    {
        int[] list = new int[]{4039, 4040, 4041, 4042, 4043, 4044, 57};
        int[] counts = new int[]{4, 4, 4, 4, 4, 4, 160000};
        int[] chances = new int[]{20, 10, 10, 10, 20, 20, 10};
        extract_item_r(list, counts, chances, player);
    }

    // Old Agathion
    private void use10408(Player player, boolean ctrl){
        Functions.addItem(player, 6471, 20);
        Functions.addItem(player, 5094, 40);
        Functions.addItem(player, 9814, 3);
        Functions.addItem(player, 9816, 4);
        Functions.addItem(player, 9817, 4);
        Functions.addItem(player, 9815, 2);
        Functions.addItem(player, 57, 6000000);
    }

    // Magic Armor Set
    private void use10473(Player player, boolean ctrl)
    {
        Functions.addItem(player, 10470, 2); // Shadow Item - Red Crescent
        Functions.addItem(player, 10471, 2); // Shadow Item - Ring of Devotion
        Functions.addItem(player, 10472, 1); // Shadow Item - Necklace of Devotion
    }

    // Ancient Tome of the Demon
    private void use9599(Player player, boolean ctrl)
    {
        int[] list = new int[]{9600, 9601, 9602};
        int[] count_min = new int[]{1, 1, 1};
        int[] count_max = new int[]{2, 2, 1};
        int[] chances = new int[]{4, 10, 1};

		if (ctrl)
		{
			long item_count = 1 + Functions.removeItem(player, 9599, Functions.getItemCount(player, 9599));
			for (int[] res : mass_extract_item_r(item_count, list, count_min, count_max, chances, player))
			{
				Functions.addItem(player, res[0], res[1]);
			}
		}
		else
		{
			extract_item_r(list, count_min, count_max, chances, player);
		}
	}
	
	// Baby Panda Agathion Pack
	private void use20069(Player player, boolean ctrl)
	{
		Functions.addItem(player, 20063, 1);
	}
	
	// Bamboo Panda Agathion Pack
	private void use20070(Player player, boolean ctrl)
	{
		Functions.addItem(player, 20064, 1);
	}
	
	// Sexy Panda Agathion Pack
	private void use20071(Player player, boolean ctrl)
	{
		Functions.addItem(player, 20065, 1);
	}
	
	// Agathion of Baby Panda 15 Day Pack
	private void use20072(Player player, boolean ctrl)
	{
		Functions.addItem(player, 20066, 1);
	}
	
	// Bamboo Panda Agathion 15 Day Pack
	private void use20073(Player player, boolean ctrl)
	{
		Functions.addItem(player, 20067, 1);
	}
	
	// Agathion of Sexy Panda 15 Day Pack
	private void use20074(Player player, boolean ctrl)
	{
		Functions.addItem(player, 20068, 1);
	}
	
	// Charming Valentine Gift Set
	private void use20210(Player player, boolean ctrl)
	{
		Functions.addItem(player, 20212, 1);
	}
	
	// Naughty Valentine Gift Set
	private void use20211(Player player, boolean ctrl)
	{
		Functions.addItem(player, 20213, 1);
	}
	
	// White Maneki Neko Agathion Pack
	private void use20215(Player player, boolean ctrl)
	{
		Functions.addItem(player, 20221, 1);
	}
	
	// Black Maneki Neko Agathion Pack
	private void use20216(Player player, boolean ctrl)
	{
		Functions.addItem(player, 20222, 1);
	}
	
	// Brown Maneki Neko Agathion Pack
	private void use20217(Player player, boolean ctrl)
	{
		Functions.addItem(player, 20223, 1);
	}
	
	// White Maneki Neko Agathion 7-Day Pack
	private void use20218(Player player, boolean ctrl)
	{
		Functions.addItem(player, 20224, 1);
	}
	
	// Black Maneki Neko Agathion 7-Day Pack
	private void use20219(Player player, boolean ctrl)
	{
		Functions.addItem(player, 20225, 1);
	}
	
	// Brown Maneki Neko Agathion 7-Day Pack
	private void use20220(Player player, boolean ctrl)
	{
		Functions.addItem(player, 20226, 1);
	}
	
	// One-Eyed Bat Drove Agathion Pack
	private void use20227(Player player, boolean ctrl)
	{
		Functions.addItem(player, 20230, 1);
	}
	
	// One-Eyed Bat Drove Agathion 7-Day Pack
	private void use20228(Player player, boolean ctrl)
	{
		Functions.addItem(player, 20231, 1);
	}
	
	// One-Eyed Bat Drove Agathion 7-Day Pack
	private void use20229(Player player, boolean ctrl)
	{
		Functions.addItem(player, 20232, 1);
	}
	
	// Pegasus Agathion Pack
	private void use20233(Player player, boolean ctrl)
	{
		Functions.addItem(player, 20236, 1);
	}
	
	// Pegasus Agathion 7-Day Pack
	private void use20234(Player player, boolean ctrl)
	{
		Functions.addItem(player, 20237, 1);
	}
	
	// Pegasus Agathion 7-Day Pack
	private void use20235(Player player, boolean ctrl)
	{
		Functions.addItem(player, 20238, 1);
	}
	
	// Yellow-Robed Tojigong Pack
	private void use20239(Player player, boolean ctrl)
	{
		Functions.addItem(player, 20245, 1);
	}
	
	// Blue-Robed Tojigong Pack
	private void use20240(Player player, boolean ctrl)
	{
		Functions.addItem(player, 20246, 1);
	}
	
	// Green-Robed Tojigong Pack
	private void use20241(Player player, boolean ctrl)
	{
		Functions.addItem(player, 20247, 1);
	}
	
	// Yellow-Robed Tojigong 7-Day Pack
	private void use20242(Player player, boolean ctrl)
	{
		Functions.addItem(player, 20248, 1);
	}
	
	// Blue-Robed Tojigong 7-Day Pack
	private void use20243(Player player, boolean ctrl)
	{
		Functions.addItem(player, 20249, 1);
	}
	
	// Green-Robed Tojigong 7-Day Pack
	private void use20244(Player player, boolean ctrl)
	{
		Functions.addItem(player, 20250, 1);
	}
	
	// Bugbear Agathion Pack
	private void use20251(Player player, boolean ctrl)
	{
		Functions.addItem(player, 20252, 1);
	}
	
	// Agathion of Love Pack (Event)
	private void use20254(Player player, boolean ctrl)
	{
		Functions.addItem(player, 20253, 1);
	}
	
	// Gold Afro Hair Pack
	private void use20278(Player player, boolean ctrl)
	{
		Functions.addItem(player, 20275, 1);
	}
	
	// Pink Afro Hair Pack
	private void use20279(Player player, boolean ctrl)
	{
		Functions.addItem(player, 20276, 1);
	}
	
	// Plaipitak Agathion Pack
	private void use20041(Player player, boolean ctrl)
	{
		Functions.addItem(player, 20012, 1);
	}
	
	// Plaipitak Agathion 30-Day Pack
	private void use20042(Player player, boolean ctrl)
	{
		Functions.addItem(player, 20013, 1);
	}
	
	// Plaipitak Agathion 30-Day Pack
	private void use20043(Player player, boolean ctrl)
	{
		Functions.addItem(player, 20014, 1);
	}
	
	// Plaipitak Agathion 30-Day Pack
	private void use20044(Player player, boolean ctrl)
	{
		Functions.addItem(player, 20015, 1);
	}
	
	// Majo Agathion Pack
	private void use20035(Player player, boolean ctrl)
	{
		Functions.addItem(player, 20006, 1);
	}
	
	// Gold Crown Majo Agathion Pack
	private void use20036(Player player, boolean ctrl)
	{
		Functions.addItem(player, 20007, 1);
	}
	
	// Black Crown Majo Agathion Pack
	private void use20037(Player player, boolean ctrl)
	{
		Functions.addItem(player, 20008, 1);
	}
	
	// Majo Agathion 30-Day Pack
	private void use20038(Player player, boolean ctrl)
	{
		Functions.addItem(player, 20009, 1);
	}
	
	// Gold Crown Majo Agathion 30-Day Pack
	private void use20039(Player player, boolean ctrl)
	{
		Functions.addItem(player, 20010, 1);
	}
	
	// Black Crown Majo Agathion 30-Day Pack
	private void use20040(Player player, boolean ctrl)
	{
		Functions.addItem(player, 20011, 1);
	}
	
	// Kat the Cat Hat Pack
	private void use20060(Player player, boolean ctrl)
	{
		Functions.addItem(player, 20031, 1);
	}
	
	// Skull Hat Pack
	private void use20061(Player player, boolean ctrl)
	{
		Functions.addItem(player, 20032, 1);
	}
	
	// ****** Start Item Mall ******
	// Small fortuna box
    private void use22000(Player player, boolean ctrl)
    {
        int[][] list = new int[][]{{22006, 3}, {22007, 2}, {22008, 1}, {22014, 1}, {22022, 3}, {22023, 3}, {22024, 1}, {8743, 1}, {8744, 1}, {8745, 1}, {8753, 1}, {8754, 1}, {8755, 1}, {22025, 5}};
        double[] chances = new double[]{20.55555, 14.01515, 6.16666, 0.86999, 3.19444, 6.38888, 5.75, 10, 8.33333, 6.94444, 2, 1.6666, 1.38888, 12.77777};
        extractRandomOneItem(player, list, chances);
    }

    // Middle fortuna box
    private void use22001(Player player, boolean ctrl)
    {
        int[][] list = new int[][]{{22007, 3}, {22008, 2}, {22009, 1}, {22014, 1}, {22015, 1}, {22022, 5}, {22023, 5}, {22024, 2}, {8746, 1}, {8747, 1}, {8748, 1}, {8756, 1}, {8757, 1}, {8758, 1}, {22025, 10}};
        double[] chances = new double[]{27.27272, 9, 5, 0.93959, 0.32467, 3.75, 7.5, 5.625, 9.11458, 7.875, 6.5625, 1.82291, 1.575, 1.3125, 12.5};
        extractRandomOneItem(player, list, chances);
    }

    // Large fortuna box
    private void use22002(Player player, boolean ctrl)
    {
        int[][] list = new int[][]{{22008, 2}, {22009, 1}, {22014, 1}, {22015, 1}, {22018, 1}, {22019, 1}, {22022, 10}, {22023, 10}, {22024, 5}, {8749, 1}, {8750, 1}, {8751, 1}, {8759, 1}, {8760, 1}, {8761, 1}, {22025, 20}};
        double[] chances = new double[]{27, 15, 0.78299, 0.27056, 0.00775, 0.0027, 3.75, 7.5, 4.5, 9.75, 8.125, 6.77083, 1.95, 1.625, 1.35416, 12.5};
        extractRandomOneItem(player, list, chances);
    }

    // Small fortuna cube
    private void use22003(Player player, boolean ctrl)
    {
        int[][] list = new int[][]{{22010, 3}, {22011, 2}, {22012, 1}, {22016, 1}, {22022, 3}, {22023, 3}, {22024, 1}, {8743, 1}, {8744, 1}, {8745, 1}, {8753, 1}, {8754, 1}, {8755, 1}, {22025, 5}};
        double[] chances = new double[]{20.22222, 13.78787, 6.06666, 0.69599, 3.47222, 6.94444, 6.25, 9.5, 7.91666, 6.59722, 1.9, 1.58333, 1.31944, 13.88888};
        extractRandomOneItem(player, list, chances);
    }

    // Middle fortuna cube
    private void use22004(Player player, boolean ctrl)
    {
        int[][] list = new int[][]{{22011, 3}, {22012, 2}, {22013, 1}, {22016, 1}, {22017, 1}, {22022, 5}, {22023, 5}, {22024, 2}, {8746, 1}, {8747, 1}, {8748, 1}, {8756, 1}, {8757, 1}, {8758, 1}, {22025, 10}};
        double[] chances = new double[]{26.51515, 8.75, 4.86111, 0.91349, 0.31565, 3.75, 7.5, 5.625, 9.54861, 8.25, 6.875, 1.90972, 1.65, 1.375, 12.5};
        extractRandomOneItem(player, list, chances);
    }

    // Large fortuna cube
    private void use22005(Player player, boolean ctrl)
    {
    	int[][] list = new int[][]{{22012, 2}, {22013, 1}, {22016, 1}, {22017, 1}, {22020, 1}, {22021, 1}, {22022, 10}, {22023, 10}, {22024, 5}, {8749, 1}, {8750, 1}, {8751, 1}, {8759, 1}, {8760, 1}, {8761, 1}, {22025, 20}};
        double[] chances = new double[]{26.25, 14.58333, 0.69599, 0.24049, 0.00638, 0.0022, 3.95833, 7.91666, 4.75, 9.58333, 7.98611, 6.65509, 1.91666, 1.59722, 1.33101, 13.19444};
        extractRandomOneItem(player, list, chances);
    }
	
	// Beast Soulshot Pack
	private void use20326(Player player, boolean ctrl)
	{
		Functions.addItem(player, 20332, 5000);
	}
	
	// Beast Spiritshot Pack
	private void use20327(Player player, boolean ctrl)
	{
		Functions.addItem(player, 20333, 5000);
	}
	
	// Beast Soulshot Large Pack
	private void use20329(Player player, boolean ctrl)
	{
		Functions.addItem(player, 20332, 10000);
	}
	
	// Beast Spiritshot Large Pack
	private void use20330(Player player, boolean ctrl)
	{
		Functions.addItem(player, 20333, 10000);
	}
	
	// Light Purple Maned Horse Bracelet 30-Day Pack
	private void use20059(Player player, boolean ctrl)
	{
		Functions.addItem(player, 20030, 1);
	}
	
	// Steam Beatle Mounting Bracelet 7 Day Pack
	private void use20494(Player player, boolean ctrl)
	{
		Functions.addItem(player, 20449, 1);
	}
	
	// Light Purple Maned Horse Mounting Bracelet 7 Day Pack
	private void use20493(Player player, boolean ctrl)
	{
		Functions.addItem(player, 20448, 1);
	}
	
	// Steam Beatle Mounting Bracelet Pack
	private void use20395(Player player, boolean ctrl)
	{
		Functions.addItem(player, 20396, 1);
	}
	
	// Pumpkin Transformation Stick 7 Day Pack
	private void use13281(Player player, boolean ctrl)
	{
		Functions.addItem(player, 13253, 1);
	}
	
	// Kat the Cat Hat 7-Day Pack
	private void use13282(Player player, boolean ctrl)
	{
		Functions.addItem(player, 13239, 1);
	}
	
	// Feline Queen Hat 7-Day Pack
	private void use13283(Player player, boolean ctrl)
	{
		Functions.addItem(player, 13240, 1);
	}
	
	// Monster Eye Hat 7-Day Pack
	private void use13284(Player player, boolean ctrl)
	{
		Functions.addItem(player, 13241, 1);
	}
	
	// Brown Bear Hat 7-Day Pack
	private void use13285(Player player, boolean ctrl)
	{
		Functions.addItem(player, 13242, 1);
	}
	
	// Fungus Hat 7-Day Pack
	private void use13286(Player player, boolean ctrl)
	{
		Functions.addItem(player, 13243, 1);
	}
	
	// Skull Hat 7-Day Pack
	private void use13287(Player player, boolean ctrl)
	{
		Functions.addItem(player, 13244, 1);
	}
	
	// Ornithomimus Hat 7-Day Pack
	private void use13288(Player player, boolean ctrl)
	{
		Functions.addItem(player, 13245, 1);
	}
	
	// Feline King Hat 7-Day Pack
	private void use13289(Player player, boolean ctrl)
	{
		Functions.addItem(player, 13246, 1);
	}
	
	// Kai the Cat Hat 7-Day Pack
	private void use13290(Player player, boolean ctrl)
	{
		Functions.addItem(player, 13247, 1);
	}
	
	// Sudden Agathion 7 Day Pack
	private void use14267(Player player, boolean ctrl)
	{
		Functions.addItem(player, 14093, 1);
	}
	
	// Shiny Agathion 7 Day Pack
	private void use14268(Player player, boolean ctrl)
	{
		Functions.addItem(player, 14094, 1);
	}
	
	// Sobbing Agathion 7 Day Pack
	private void use14269(Player player, boolean ctrl)
	{
		Functions.addItem(player, 14095, 1);
	}
	
	// Agathion of Love 7-Day Pack
	private void use13280(Player player, boolean ctrl)
	{
		Functions.addItem(player, 20201, 1);
	}
	
	// A Scroll Bundle of Fighter
	private void use22087(Player player, boolean ctrl)
	{
		Functions.addItem(player, 22039, 1);
		Functions.addItem(player, 22040, 1);
		Functions.addItem(player, 22041, 1);
		Functions.addItem(player, 22042, 1);
		Functions.addItem(player, 22043, 1);
		Functions.addItem(player, 22044, 1);
		Functions.addItem(player, 22047, 1);
		Functions.addItem(player, 22048, 1);
	}
	
	// A Scroll Bundle of Mage
	private void use22088(Player player, boolean ctrl)
	{
		Functions.addItem(player, 22045, 1);
		Functions.addItem(player, 22046, 1);
		Functions.addItem(player, 22048, 1);
		Functions.addItem(player, 22049, 1);
		Functions.addItem(player, 22050, 1);
		Functions.addItem(player, 22051, 1);
		Functions.addItem(player, 22052, 1);
		Functions.addItem(player, 22053, 1);
	}
	
	// ****** End Item Mall ******
	
	// ****** Belts ******
	// Gludio Supply Box - Belt: Grade B, C
	private void use13713(Player player, boolean ctrl)
	{
		if (Rnd.chance(50))
		{
			Functions.addItem(player, 13894, 1); // Cloth Belt
		}
		else if (Rnd.chance(50))
		{
			Functions.addItem(player, 13895, 1); // Leather Belt
		}
		else
		{
			player.sendPacket(Msg.THERE_WAS_NOTHING_FOUND_INSIDE_OF_THAT);
		}
	}
	
	// Dion Supply Box - Belt: Grade B, C
	private void use13714(Player player, boolean ctrl)
	{
		if (Rnd.chance(50))
		{
			Functions.addItem(player, 13894, 1); // Cloth Belt
		}
		else if (Rnd.chance(50))
		{
			Functions.addItem(player, 13895, 1); // Leather Belt
		}
		else
		{
			player.sendPacket(Msg.THERE_WAS_NOTHING_FOUND_INSIDE_OF_THAT);
		}
	}
	
	// Giran Supply Box - Belt: Grade B, C
	private void use13715(Player player, boolean ctrl)
	{
		if (Rnd.chance(50))
		{
			Functions.addItem(player, 13894, 1); // Cloth Belt
		}
		else if (Rnd.chance(50))
		{
			Functions.addItem(player, 13895, 1); // Leather Belt
		}
		else
		{
			player.sendPacket(Msg.THERE_WAS_NOTHING_FOUND_INSIDE_OF_THAT);
		}
	}
	
	// Oren Supply Box - Belt: Grade B, C
	private void use13716(Player player, boolean ctrl)
	{
		if (Rnd.chance(50))
		{
			Functions.addItem(player, 13894, 1); // Cloth Belt
		}
		else if (Rnd.chance(50))
		{
			Functions.addItem(player, 13895, 1); // Leather Belt
		}
		else
		{
			player.sendPacket(Msg.THERE_WAS_NOTHING_FOUND_INSIDE_OF_THAT);
		}
	}
	
	// Aden Supply Box - Belt: Grade B, C
	private void use13717(Player player, boolean ctrl)
	{
		if (Rnd.chance(50))
		{
			Functions.addItem(player, 13894, 1); // Cloth Belt
		}
		else if (Rnd.chance(50))
		{
			Functions.addItem(player, 13895, 1); // Leather Belt
		}
		else
		{
			player.sendPacket(Msg.THERE_WAS_NOTHING_FOUND_INSIDE_OF_THAT);
		}
	}
	
	// Innadril Supply Box - Belt: Grade B, C
	private void use13718(Player player, boolean ctrl)
	{
		if (Rnd.chance(50))
		{
			Functions.addItem(player, 13894, 1); // Cloth Belt
		}
		else if (Rnd.chance(50))
		{
			Functions.addItem(player, 13895, 1); // Leather Belt
		}
		else
		{
			player.sendPacket(Msg.THERE_WAS_NOTHING_FOUND_INSIDE_OF_THAT);
		}
	}
	
	// Goddard Supply Box - Belt: Grade B, C
	private void use13719(Player player, boolean ctrl)
	{
		if (Rnd.chance(50))
		{
			Functions.addItem(player, 13894, 1); // Cloth Belt
		}
		else if (Rnd.chance(50))
		{
			Functions.addItem(player, 13895, 1); // Leather Belt
		}
		else
		{
			player.sendPacket(Msg.THERE_WAS_NOTHING_FOUND_INSIDE_OF_THAT);
		}
	}
	
	// Rune Supply Box - Belt: Grade B, C
	private void use13720(Player player, boolean ctrl)
	{
		if (Rnd.chance(50))
		{
			Functions.addItem(player, 13894, 1); // Cloth Belt
		}
		else if (Rnd.chance(50))
		{
			Functions.addItem(player, 13895, 1); // Leather Belt
		}
		else
		{
			player.sendPacket(Msg.THERE_WAS_NOTHING_FOUND_INSIDE_OF_THAT);
		}
	}
	
	// Schuttgart Supply Box - Belt: Grade B, C
	private void use13721(Player player, boolean ctrl)
	{
		if (Rnd.chance(50))
		{
			Functions.addItem(player, 13894, 1); // Cloth Belt
		}
		else if (Rnd.chance(50))
		{
			Functions.addItem(player, 13895, 1); // Leather Belt
		}
		else
		{
			player.sendPacket(Msg.THERE_WAS_NOTHING_FOUND_INSIDE_OF_THAT);
		}
	}
	
	// Gludio Supply Box - Belt: Grade S, A
	private void use14549(Player player, boolean ctrl)
	{
		if (Rnd.chance(50))
		{
			Functions.addItem(player, 13896, 1); // Iron Belt
		}
		else if (Rnd.chance(50))
		{
			Functions.addItem(player, 13897, 1); // Mithril Belt
		}
		else
		{
			player.sendPacket(Msg.THERE_WAS_NOTHING_FOUND_INSIDE_OF_THAT);
		}
	}
	
	// Dion Supply Box - Belt: Grade S, A
	private void use14550(Player player, boolean ctrl)
	{
		if (Rnd.chance(50))
		{
			Functions.addItem(player, 13896, 1); // Iron Belt
		}
		else if (Rnd.chance(50))
		{
			Functions.addItem(player, 13897, 1); // Mithril Belt
		}
		else
		{
			player.sendPacket(Msg.THERE_WAS_NOTHING_FOUND_INSIDE_OF_THAT);
		}
	}
	
	// Giran Supply Box - Belt: Grade S, A
	private void use14551(Player player, boolean ctrl)
	{
		if (Rnd.chance(50))
		{
			Functions.addItem(player, 13896, 1); // Iron Belt
		}
		else if (Rnd.chance(50))
		{
			Functions.addItem(player, 13897, 1); // Mithril Belt
		}
		else
		{
			player.sendPacket(Msg.THERE_WAS_NOTHING_FOUND_INSIDE_OF_THAT);
		}
	}
	
	// Oren Supply Box - Belt: Grade S, A
	private void use14552(Player player, boolean ctrl)
	{
		if (Rnd.chance(50))
		{
			Functions.addItem(player, 13896, 1); // Iron Belt
		}
		else if (Rnd.chance(50))
		{
			Functions.addItem(player, 13897, 1); // Mithril Belt
		}
		else
		{
			player.sendPacket(Msg.THERE_WAS_NOTHING_FOUND_INSIDE_OF_THAT);
		}
	}
	
	// Aden Supply Box - Belt: Grade S, A
	private void use14553(Player player, boolean ctrl)
	{
		if (Rnd.chance(50))
		{
			Functions.addItem(player, 13896, 1); // Iron Belt
		}
		else if (Rnd.chance(50))
		{
			Functions.addItem(player, 13897, 1); // Mithril Belt
		}
		else
		{
			player.sendPacket(Msg.THERE_WAS_NOTHING_FOUND_INSIDE_OF_THAT);
		}
	}
	
	// Innadril Supply Box - Belt: Grade S, A
	private void use14554(Player player, boolean ctrl)
	{
		if (Rnd.chance(50))
		{
			Functions.addItem(player, 13896, 1); // Iron Belt
		}
		else if (Rnd.chance(50))
		{
			Functions.addItem(player, 13897, 1); // Mithril Belt
		}
		else
		{
			player.sendPacket(Msg.THERE_WAS_NOTHING_FOUND_INSIDE_OF_THAT);
		}
	}
	
	// Goddard Supply Box - Belt: Grade S, A
	private void use14555(Player player, boolean ctrl)
	{
		if (Rnd.chance(50))
		{
			Functions.addItem(player, 13896, 1); // Iron Belt
		}
		else if (Rnd.chance(50))
		{
			Functions.addItem(player, 13897, 1); // Mithril Belt
		}
		else
		{
			player.sendPacket(Msg.THERE_WAS_NOTHING_FOUND_INSIDE_OF_THAT);
		}
	}
	
	// Rune Supply Box - Belt: Grade S, A
	private void use14556(Player player, boolean ctrl)
	{
		if (Rnd.chance(50))
		{
			Functions.addItem(player, 13896, 1); // Iron Belt
		}
		else if (Rnd.chance(50))
		{
			Functions.addItem(player, 13897, 1); // Mithril Belt
		}
		else
		{
			player.sendPacket(Msg.THERE_WAS_NOTHING_FOUND_INSIDE_OF_THAT);
		}
	}
	
	// Schuttgart Supply Box - Belt: Grade S, A
	private void use14557(Player player, boolean ctrl)
	{
		if (Rnd.chance(50))
		{
			Functions.addItem(player, 13896, 1); // Iron Belt
		}
		else if (Rnd.chance(50))
		{
			Functions.addItem(player, 13897, 1); // Mithril Belt
		}
		else
		{
			player.sendPacket(Msg.THERE_WAS_NOTHING_FOUND_INSIDE_OF_THAT);
		}
	}
	
	// ****** Magic Pins ******
	// Gludio Supply Box - Magic Pin: Grade B, C
	private void use13695(Player player, boolean ctrl)
	{
		if (Rnd.chance(50))
		{
			Functions.addItem(player, 13898, 1); // Sealed Magic Pin (C-Grade)
		}
		else if (Rnd.chance(50))
		{
			Functions.addItem(player, 13899, 1); // Sealed Magic Pin (B-Grade)
		}
		else
		{
			player.sendPacket(Msg.THERE_WAS_NOTHING_FOUND_INSIDE_OF_THAT);
		}
	}
	
	// Dion Supply Box - Magic Pin: Grade B, C
	private void use13696(Player player, boolean ctrl)
	{
		if (Rnd.chance(50))
		{
			Functions.addItem(player, 13898, 1); // Sealed Magic Pin (C-Grade)
		}
		else if (Rnd.chance(50))
		{
			Functions.addItem(player, 13899, 1); // Sealed Magic Pin (B-Grade)
		}
		else
		{
			player.sendPacket(Msg.THERE_WAS_NOTHING_FOUND_INSIDE_OF_THAT);
		}
	}
	
	// Giran Supply Box - Magic Pin: Grade B, C
	private void use13697(Player player, boolean ctrl)
	{
		if (Rnd.chance(50))
		{
			Functions.addItem(player, 13898, 1); // Sealed Magic Pin (C-Grade)
		}
		else if (Rnd.chance(50))
		{
			Functions.addItem(player, 13899, 1); // Sealed Magic Pin (B-Grade)
		}
		else
		{
			player.sendPacket(Msg.THERE_WAS_NOTHING_FOUND_INSIDE_OF_THAT);
		}
	}
	
	// Oren Supply Box - Magic Pin: Grade B, C
	private void use13698(Player player, boolean ctrl)
	{
		if (Rnd.chance(50))
		{
			Functions.addItem(player, 13898, 1); // Sealed Magic Pin (C-Grade)
		}
		else if (Rnd.chance(50))
		{
			Functions.addItem(player, 13899, 1); // Sealed Magic Pin (B-Grade)
		}
		else
		{
			player.sendPacket(Msg.THERE_WAS_NOTHING_FOUND_INSIDE_OF_THAT);
		}
	}
	
	// Aden Supply Box - Magic Pin: Grade B, C
	private void use13699(Player player, boolean ctrl)
	{
		if (Rnd.chance(50))
		{
			Functions.addItem(player, 13898, 1); // Sealed Magic Pin (C-Grade)
		}
		else if (Rnd.chance(50))
		{
			Functions.addItem(player, 13899, 1); // Sealed Magic Pin (B-Grade)
		}
		else
		{
			player.sendPacket(Msg.THERE_WAS_NOTHING_FOUND_INSIDE_OF_THAT);
		}
	}
	
	// Innadril Supply Box - Magic Pin: Grade B, C
	private void use13700(Player player, boolean ctrl)
	{
		if (Rnd.chance(50))
		{
			Functions.addItem(player, 13898, 1); // Sealed Magic Pin (C-Grade)
		}
		else if (Rnd.chance(50))
		{
			Functions.addItem(player, 13899, 1); // Sealed Magic Pin (B-Grade)
		}
		else
		{
			player.sendPacket(Msg.THERE_WAS_NOTHING_FOUND_INSIDE_OF_THAT);
		}
	}
	
	// Goddard Supply Box - Magic Pin: Grade B, C
	private void use13701(Player player, boolean ctrl)
	{
		if (Rnd.chance(50))
		{
			Functions.addItem(player, 13898, 1); // Sealed Magic Pin (C-Grade)
		}
		else if (Rnd.chance(50))
		{
			Functions.addItem(player, 13899, 1); // Sealed Magic Pin (B-Grade)
		}
		else
		{
			player.sendPacket(Msg.THERE_WAS_NOTHING_FOUND_INSIDE_OF_THAT);
		}
	}
	
	// Rune Supply Box - Magic Pin: Grade B, C
	private void use13702(Player player, boolean ctrl)
	{
		if (Rnd.chance(50))
		{
			Functions.addItem(player, 13898, 1); // Sealed Magic Pin (C-Grade)
		}
		else if (Rnd.chance(50))
		{
			Functions.addItem(player, 13899, 1); // Sealed Magic Pin (B-Grade)
		}
		else
		{
			player.sendPacket(Msg.THERE_WAS_NOTHING_FOUND_INSIDE_OF_THAT);
		}
	}
	
	// Schuttgart Supply Box - Magic Pin: Grade B, C
	private void use13703(Player player, boolean ctrl)
	{
		if (Rnd.chance(50))
		{
			Functions.addItem(player, 13898, 1); // Sealed Magic Pin (C-Grade)
		}
		else if (Rnd.chance(50))
		{
			Functions.addItem(player, 13899, 1); // Sealed Magic Pin (B-Grade)
		}
		else
		{
			player.sendPacket(Msg.THERE_WAS_NOTHING_FOUND_INSIDE_OF_THAT);
		}
	}
	
	// Gludio Supply Box - Magic Pin: Grade S, A
	private void use14531(Player player, boolean ctrl)
	{
		if (Rnd.chance(50))
		{
			Functions.addItem(player, 13900, 1); // Sealed Magic Pin (A-Grade)
		}
		else if (Rnd.chance(50))
		{
			Functions.addItem(player, 13901, 1); // Sealed Magic Pin (S-Grade)
		}
		else
		{
			player.sendPacket(Msg.THERE_WAS_NOTHING_FOUND_INSIDE_OF_THAT);
		}
	}
	
	// Dion Supply Box - Magic Pin: Grade S, A
	private void use14532(Player player, boolean ctrl)
	{
		if (Rnd.chance(50))
		{
			Functions.addItem(player, 13900, 1); // Sealed Magic Pin (A-Grade)
		}
		else if (Rnd.chance(50))
		{
			Functions.addItem(player, 13901, 1); // Sealed Magic Pin (S-Grade)
		}
		else
		{
			player.sendPacket(Msg.THERE_WAS_NOTHING_FOUND_INSIDE_OF_THAT);
		}
	}
	
	// Giran Supply Box - Magic Pin: Grade S, A
	private void use14533(Player player, boolean ctrl)
	{
		if (Rnd.chance(50))
		{
			Functions.addItem(player, 13900, 1); // Sealed Magic Pin (A-Grade)
		}
		else if (Rnd.chance(50))
		{
			Functions.addItem(player, 13901, 1); // Sealed Magic Pin (S-Grade)
		}
		else
		{
			player.sendPacket(Msg.THERE_WAS_NOTHING_FOUND_INSIDE_OF_THAT);
		}
	}
	
	// Oren Supply Box - Magic Pin: Grade S, A
	private void use14534(Player player, boolean ctrl)
	{
		if (Rnd.chance(50))
		{
			Functions.addItem(player, 13900, 1); // Sealed Magic Pin (A-Grade)
		}
		else if (Rnd.chance(50))
		{
			Functions.addItem(player, 13901, 1); // Sealed Magic Pin (S-Grade)
		}
		else
		{
			player.sendPacket(Msg.THERE_WAS_NOTHING_FOUND_INSIDE_OF_THAT);
		}
	}
	
	// Aden Supply Box - Magic Pin: Grade S, A
	private void use14535(Player player, boolean ctrl)
	{
		if (Rnd.chance(50))
		{
			Functions.addItem(player, 13900, 1); // Sealed Magic Pin (A-Grade)
		}
		else if (Rnd.chance(50))
		{
			Functions.addItem(player, 13901, 1); // Sealed Magic Pin (S-Grade)
		}
		else
		{
			player.sendPacket(Msg.THERE_WAS_NOTHING_FOUND_INSIDE_OF_THAT);
		}
	}
	
	// Innadril Supply Box - Magic Pin: Grade S, A
	private void use14536(Player player, boolean ctrl)
	{
		if (Rnd.chance(50))
		{
			Functions.addItem(player, 13900, 1); // Sealed Magic Pin (A-Grade)
		}
		else if (Rnd.chance(50))
		{
			Functions.addItem(player, 13901, 1); // Sealed Magic Pin (S-Grade)
		}
		else
		{
			player.sendPacket(Msg.THERE_WAS_NOTHING_FOUND_INSIDE_OF_THAT);
		}
	}
	
	// Goddard Supply Box - Magic Pin: Grade S, A
	private void use14537(Player player, boolean ctrl)
	{
		if (Rnd.chance(50))
		{
			Functions.addItem(player, 13900, 1); // Sealed Magic Pin (A-Grade)
		}
		else if (Rnd.chance(50))
		{
			Functions.addItem(player, 13901, 1); // Sealed Magic Pin (S-Grade)
		}
		else
		{
			player.sendPacket(Msg.THERE_WAS_NOTHING_FOUND_INSIDE_OF_THAT);
		}
	}
	
	// Rune Supply Box - Magic Pin: Grade S, A
	private void use14538(Player player, boolean ctrl)
	{
		if (Rnd.chance(50))
		{
			Functions.addItem(player, 13900, 1); // Sealed Magic Pin (A-Grade)
		}
		else if (Rnd.chance(50))
		{
			Functions.addItem(player, 13901, 1); // Sealed Magic Pin (S-Grade)
		}
		else
		{
			player.sendPacket(Msg.THERE_WAS_NOTHING_FOUND_INSIDE_OF_THAT);
		}
	}
	
	// Schuttgart Supply Box - Magic Pin: Grade S, A
	private void use14539(Player player, boolean ctrl)
	{
		if (Rnd.chance(50))
		{
			Functions.addItem(player, 13900, 1); // Sealed Magic Pin (A-Grade)
		}
		else if (Rnd.chance(50))
		{
			Functions.addItem(player, 13901, 1); // Sealed Magic Pin (S-Grade)
		}
		else
		{
			player.sendPacket(Msg.THERE_WAS_NOTHING_FOUND_INSIDE_OF_THAT);
		}
	}
	
	// ****** Magic Pouchs ******
	// Gludio Supply Box - Magic Pouch: Grade B, C
	private void use13704(Player player, boolean ctrl)
	{
		if (Rnd.chance(50))
		{
			Functions.addItem(player, 13918, 1); // Sealed Magic Pouch (C-Grade)
		}
		else if (Rnd.chance(50))
		{
			Functions.addItem(player, 13919, 1); // Sealed Magic Pouch (B-Grade)
		}
		else
		{
			player.sendPacket(Msg.THERE_WAS_NOTHING_FOUND_INSIDE_OF_THAT);
		}
	}
	
	// Dion Supply Box - Magic Pouch: Grade B, C
	private void use13705(Player player, boolean ctrl)
	{
		if (Rnd.chance(50))
		{
			Functions.addItem(player, 13918, 1); // Sealed Magic Pouch (C-Grade)
		}
		else if (Rnd.chance(50))
		{
			Functions.addItem(player, 13919, 1); // Sealed Magic Pouch (B-Grade)
		}
		else
		{
			player.sendPacket(Msg.THERE_WAS_NOTHING_FOUND_INSIDE_OF_THAT);
		}
	}
	
	// Giran Supply Box - Magic Pouch: Grade B, C
	private void use13706(Player player, boolean ctrl)
	{
		if (Rnd.chance(50))
		{
			Functions.addItem(player, 13918, 1); // Sealed Magic Pouch (C-Grade)
		}
		else if (Rnd.chance(50))
		{
			Functions.addItem(player, 13919, 1); // Sealed Magic Pouch (B-Grade)
		}
		else
		{
			player.sendPacket(Msg.THERE_WAS_NOTHING_FOUND_INSIDE_OF_THAT);
		}
	}
	
	// Oren Supply Box - Magic Pouch: Grade B, C
	private void use13707(Player player, boolean ctrl)
	{
		if (Rnd.chance(50))
		{
			Functions.addItem(player, 13918, 1); // Sealed Magic Pouch (C-Grade)
		}
		else if (Rnd.chance(50))
		{
			Functions.addItem(player, 13919, 1); // Sealed Magic Pouch (B-Grade)
		}
		else
		{
			player.sendPacket(Msg.THERE_WAS_NOTHING_FOUND_INSIDE_OF_THAT);
		}
	}
	
	// Aden Supply Box - Magic Pouch: Grade B, C
	private void use13708(Player player, boolean ctrl)
	{
		if (Rnd.chance(50))
		{
			Functions.addItem(player, 13918, 1); // Sealed Magic Pouch (C-Grade)
		}
		else if (Rnd.chance(50))
		{
			Functions.addItem(player, 13919, 1); // Sealed Magic Pouch (B-Grade)
		}
		else
		{
			player.sendPacket(Msg.THERE_WAS_NOTHING_FOUND_INSIDE_OF_THAT);
		}
	}
	
	// Innadril Supply Box - Magic Pouch: Grade B, C
	private void use13709(Player player, boolean ctrl)
	{
		if (Rnd.chance(50))
		{
			Functions.addItem(player, 13918, 1); // Sealed Magic Pouch (C-Grade)
		}
		else if (Rnd.chance(50))
		{
			Functions.addItem(player, 13919, 1); // Sealed Magic Pouch (B-Grade)
		}
		else
		{
			player.sendPacket(Msg.THERE_WAS_NOTHING_FOUND_INSIDE_OF_THAT);
		}
	}
	
	// Goddard Supply Box - Magic Pouch: Grade B, C
	private void use13710(Player player, boolean ctrl)
	{
		if (Rnd.chance(50))
		{
			Functions.addItem(player, 13918, 1); // Sealed Magic Pouch (C-Grade)
		}
		else if (Rnd.chance(50))
		{
			Functions.addItem(player, 13919, 1); // Sealed Magic Pouch (B-Grade)
		}
		else
		{
			player.sendPacket(Msg.THERE_WAS_NOTHING_FOUND_INSIDE_OF_THAT);
		}
	}
	
	// Rune Supply Box - Magic Pouch: Grade B, C
	private void use13711(Player player, boolean ctrl)
	{
		if (Rnd.chance(50))
		{
			Functions.addItem(player, 13918, 1); // Sealed Magic Pouch (C-Grade)
		}
		else if (Rnd.chance(50))
		{
			Functions.addItem(player, 13919, 1); // Sealed Magic Pouch (B-Grade)
		}
		else
		{
			player.sendPacket(Msg.THERE_WAS_NOTHING_FOUND_INSIDE_OF_THAT);
		}
	}
	
	// Schuttgart Supply Box - Magic Pouch: Grade B, C
	private void use13712(Player player, boolean ctrl)
	{
		if (Rnd.chance(50))
		{
			Functions.addItem(player, 13918, 1); // Sealed Magic Pouch (C-Grade)
		}
		else if (Rnd.chance(50))
		{
			Functions.addItem(player, 13919, 1); // Sealed Magic Pouch (B-Grade)
		}
		else
		{
			player.sendPacket(Msg.THERE_WAS_NOTHING_FOUND_INSIDE_OF_THAT);
		}
	}
	
	// Gludio Supply Box - Magic Pouch: Grade S, A
	private void use14540(Player player, boolean ctrl)
	{
		if (Rnd.chance(50))
		{
			Functions.addItem(player, 13920, 1); // Sealed Magic Pouch (A-Grade)
		}
		else if (Rnd.chance(50))
		{
			Functions.addItem(player, 13921, 1); // Sealed Magic Pouch (S-Grade)
		}
		else
		{
			player.sendPacket(Msg.THERE_WAS_NOTHING_FOUND_INSIDE_OF_THAT);
		}
	}
	
	// Dion Supply Box - Magic Pouch: Grade S, A
	private void use14541(Player player, boolean ctrl)
	{
		if (Rnd.chance(50))
		{
			Functions.addItem(player, 13920, 1); // Sealed Magic Pouch (A-Grade)
		}
		else if (Rnd.chance(50))
		{
			Functions.addItem(player, 13921, 1); // Sealed Magic Pouch (S-Grade)
		}
		else
		{
			player.sendPacket(Msg.THERE_WAS_NOTHING_FOUND_INSIDE_OF_THAT);
		}
	}
	
	// Giran Supply Box - Magic Pouch: Grade S, A
	private void use14542(Player player, boolean ctrl)
	{
		if (Rnd.chance(50))
		{
			Functions.addItem(player, 13920, 1); // Sealed Magic Pouch (A-Grade)
		}
		else if (Rnd.chance(50))
		{
			Functions.addItem(player, 13921, 1); // Sealed Magic Pouch (S-Grade)
		}
		else
		{
			player.sendPacket(Msg.THERE_WAS_NOTHING_FOUND_INSIDE_OF_THAT);
		}
	}
	
	// Oren Supply Box - Magic Pouch: Grade S, A
	private void use14543(Player player, boolean ctrl)
	{
		if (Rnd.chance(50))
		{
			Functions.addItem(player, 13920, 1); // Sealed Magic Pouch (A-Grade)
		}
		else if (Rnd.chance(50))
		{
			Functions.addItem(player, 13921, 1); // Sealed Magic Pouch (S-Grade)
		}
		else
		{
			player.sendPacket(Msg.THERE_WAS_NOTHING_FOUND_INSIDE_OF_THAT);
		}
	}
	
	// Aden Supply Box - Magic Pouch: Grade S, A
	private void use14544(Player player, boolean ctrl)
	{
		if (Rnd.chance(50))
		{
			Functions.addItem(player, 13920, 1); // Sealed Magic Pouch (A-Grade)
		}
		else if (Rnd.chance(50))
		{
			Functions.addItem(player, 13921, 1); // Sealed Magic Pouch (S-Grade)
		}
		else
		{
			player.sendPacket(Msg.THERE_WAS_NOTHING_FOUND_INSIDE_OF_THAT);
		}
	}
	
	// Innadril Supply Box - Magic Pouch: Grade S, A
	private void use14545(Player player, boolean ctrl)
	{
		if (Rnd.chance(50))
		{
			Functions.addItem(player, 13920, 1); // Sealed Magic Pouch (A-Grade)
		}
		else if (Rnd.chance(50))
		{
			Functions.addItem(player, 13921, 1); // Sealed Magic Pouch (S-Grade)
		}
		else
		{
			player.sendPacket(Msg.THERE_WAS_NOTHING_FOUND_INSIDE_OF_THAT);
		}
	}
	
	// Goddard Supply Box - Magic Pouch: Grade S, A
	private void use14546(Player player, boolean ctrl)
	{
		if (Rnd.chance(50))
		{
			Functions.addItem(player, 13920, 1); // Sealed Magic Pouch (A-Grade)
		}
		else if (Rnd.chance(50))
		{
			Functions.addItem(player, 13921, 1); // Sealed Magic Pouch (S-Grade)
		}
		else
		{
			player.sendPacket(Msg.THERE_WAS_NOTHING_FOUND_INSIDE_OF_THAT);
		}
	}
	
	// Rune Supply Box - Magic Pouch: Grade S, A
	private void use14547(Player player, boolean ctrl)
	{
		if (Rnd.chance(50))
		{
			Functions.addItem(player, 13920, 1); // Sealed Magic Pouch (A-Grade)
		}
		else if (Rnd.chance(50))
		{
			Functions.addItem(player, 13921, 1); // Sealed Magic Pouch (S-Grade)
		}
		else
		{
			player.sendPacket(Msg.THERE_WAS_NOTHING_FOUND_INSIDE_OF_THAT);
		}
	}
	
	// Schuttgart Supply Box - Magic Pouch: Grade S, A
	private void use14548(Player player, boolean ctrl)
	{
		if (Rnd.chance(50))
		{
			Functions.addItem(player, 13920, 1); // Sealed Magic Pouch (A-Grade)
		}
		else if (Rnd.chance(50))
		{
			Functions.addItem(player, 13921, 1); // Sealed Magic Pouch (S-Grade)
		}
		else
		{
			player.sendPacket(Msg.THERE_WAS_NOTHING_FOUND_INSIDE_OF_THAT);
		}
	}
	
	// ****** Magic Rune Clip ******
	// Gludio Supply Box - Magic Rune Clip: Grade S, A
	private void use14884(Player player, boolean ctrl)
	{
		if (Rnd.chance(50))
		{
			Functions.addItem(player, 14902, 1); // Sealed Magic Rune Clip (A-Grade)
		}
		else if (Rnd.chance(50))
		{
			Functions.addItem(player, 14903, 1); // Sealed Magic Rune Clip (S-Grade)
		}
		else
		{
			player.sendPacket(Msg.THERE_WAS_NOTHING_FOUND_INSIDE_OF_THAT);
		}
	}
	
	// Dion Supply Box - Magic Rune Clip: Grade S, A
	private void use14885(Player player, boolean ctrl)
	{
		if (Rnd.chance(50))
		{
			Functions.addItem(player, 14902, 1); // Sealed Magic Rune Clip (A-Grade)
		}
		else if (Rnd.chance(50))
		{
			Functions.addItem(player, 14903, 1); // Sealed Magic Rune Clip (S-Grade)
		}
		else
		{
			player.sendPacket(Msg.THERE_WAS_NOTHING_FOUND_INSIDE_OF_THAT);
		}
	}
	
	// Giran Supply Box - Magic Rune Clip: Grade S, A
	private void use14886(Player player, boolean ctrl)
	{
		if (Rnd.chance(50))
		{
			Functions.addItem(player, 14902, 1); // Sealed Magic Rune Clip (A-Grade)
		}
		else if (Rnd.chance(50))
		{
			Functions.addItem(player, 14903, 1); // Sealed Magic Rune Clip (S-Grade)
		}
		else
		{
			player.sendPacket(Msg.THERE_WAS_NOTHING_FOUND_INSIDE_OF_THAT);
		}
	}
	
	// Oren Supply Box - Magic Rune Clip: Grade S, A
	private void use14887(Player player, boolean ctrl)
	{
		if (Rnd.chance(50))
		{
			Functions.addItem(player, 14902, 1); // Sealed Magic Rune Clip (A-Grade)
		}
		else if (Rnd.chance(50))
		{
			Functions.addItem(player, 14903, 1); // Sealed Magic Rune Clip (S-Grade)
		}
		else
		{
			player.sendPacket(Msg.THERE_WAS_NOTHING_FOUND_INSIDE_OF_THAT);
		}
	}
	
	// Aden Supply Box - Magic Rune Clip: Grade S, A
	private void use14888(Player player, boolean ctrl)
	{
		if (Rnd.chance(50))
		{
			Functions.addItem(player, 14902, 1); // Sealed Magic Rune Clip (A-Grade)
		}
		else if (Rnd.chance(50))
		{
			Functions.addItem(player, 14903, 1); // Sealed Magic Rune Clip (S-Grade)
		}
		else
		{
			player.sendPacket(Msg.THERE_WAS_NOTHING_FOUND_INSIDE_OF_THAT);
		}
	}
	
	// Innadril Supply Box - Magic Rune Clip: Grade S, A
	private void use14889(Player player, boolean ctrl)
	{
		if (Rnd.chance(50))
		{
			Functions.addItem(player, 14902, 1); // Sealed Magic Rune Clip (A-Grade)
		}
		else if (Rnd.chance(50))
		{
			Functions.addItem(player, 14903, 1); // Sealed Magic Rune Clip (S-Grade)
		}
		else
		{
			player.sendPacket(Msg.THERE_WAS_NOTHING_FOUND_INSIDE_OF_THAT);
		}
	}
	
	// Goddard Supply Box - Magic Rune Clip: Grade S, A
	private void use14890(Player player, boolean ctrl)
	{
		if (Rnd.chance(50))
		{
			Functions.addItem(player, 14902, 1); // Sealed Magic Rune Clip (A-Grade)
		}
		else if (Rnd.chance(50))
		{
			Functions.addItem(player, 14903, 1); // Sealed Magic Rune Clip (S-Grade)
		}
		else
		{
			player.sendPacket(Msg.THERE_WAS_NOTHING_FOUND_INSIDE_OF_THAT);
		}
	}
	
	// Rune Supply Box - Magic Rune Clip: Grade S, A
	private void use14891(Player player, boolean ctrl)
	{
		if (Rnd.chance(50))
		{
			Functions.addItem(player, 14902, 1); // Sealed Magic Rune Clip (A-Grade)
		}
		else if (Rnd.chance(50))
		{
			Functions.addItem(player, 14903, 1); // Sealed Magic Rune Clip (S-Grade)
		}
		else
		{
			player.sendPacket(Msg.THERE_WAS_NOTHING_FOUND_INSIDE_OF_THAT);
		}
	}
	
	// Schuttgart Supply Box - Magic Rune Clip: Grade S, A
	private void use14892(Player player, boolean ctrl)
	{
		if (Rnd.chance(50))
		{
			Functions.addItem(player, 14902, 1); // Sealed Magic Rune Clip (A-Grade)
		}
		else if (Rnd.chance(50))
		{
			Functions.addItem(player, 14903, 1); // Sealed Magic Rune Clip (S-Grade)
		}
		else
		{
			player.sendPacket(Msg.THERE_WAS_NOTHING_FOUND_INSIDE_OF_THAT);
		}
	}
	
	// ****** Magic Ornament ******
	// Gludio Supply Box - Magic Ornament: Grade S, A
	private void use14893(Player player, boolean ctrl)
	{
		if (Rnd.chance(20))
		{
			Functions.addItem(player, 14904, 1); // Sealed Magic Ornament (A-Grade)
		}
		else if (Rnd.chance(20))
		{
			Functions.addItem(player, 14905, 1); // Sealed Magic Ornament (S-Grade)
		}
		else
		{
			player.sendPacket(Msg.THERE_WAS_NOTHING_FOUND_INSIDE_OF_THAT);
		}
	}
	
	// Dion Supply Box - Magic Ornament: Grade S, A
	private void use14894(Player player, boolean ctrl)
	{
		if (Rnd.chance(20))
		{
			Functions.addItem(player, 14904, 1); // Sealed Magic Ornament (A-Grade)
		}
		else if (Rnd.chance(20))
		{
			Functions.addItem(player, 14905, 1); // Sealed Magic Ornament (S-Grade)
		}
		else
		{
			player.sendPacket(Msg.THERE_WAS_NOTHING_FOUND_INSIDE_OF_THAT);
		}
	}
	
	// Giran Supply Box - Magic Ornament: Grade S, A
	private void use14895(Player player, boolean ctrl)
	{
		if (Rnd.chance(20))
		{
			Functions.addItem(player, 14904, 1); // Sealed Magic Ornament (A-Grade)
		}
		else if (Rnd.chance(20))
		{
			Functions.addItem(player, 14905, 1); // Sealed Magic Ornament (S-Grade)
		}
		else
		{
			player.sendPacket(Msg.THERE_WAS_NOTHING_FOUND_INSIDE_OF_THAT);
		}
	}
	
	// Oren Supply Box - Magic Ornament: Grade S, A
	private void use14896(Player player, boolean ctrl)
	{
		if (Rnd.chance(20))
		{
			Functions.addItem(player, 14904, 1); // Sealed Magic Ornament (A-Grade)
		}
		else if (Rnd.chance(20))
		{
			Functions.addItem(player, 14905, 1); // Sealed Magic Ornament (S-Grade)
		}
		else
		{
			player.sendPacket(Msg.THERE_WAS_NOTHING_FOUND_INSIDE_OF_THAT);
		}
	}
	
	// Aden Supply Box - Magic Ornament: Grade S, A
	private void use14897(Player player, boolean ctrl)
	{
		if (Rnd.chance(20))
		{
			Functions.addItem(player, 14904, 1); // Sealed Magic Ornament (A-Grade)
		}
		else if (Rnd.chance(20))
		{
			Functions.addItem(player, 14905, 1); // Sealed Magic Ornament (S-Grade)
		}
		else
		{
			player.sendPacket(Msg.THERE_WAS_NOTHING_FOUND_INSIDE_OF_THAT);
		}
	}
	
	// Innadril Supply Box - Magic Ornament: Grade S, A
	private void use14898(Player player, boolean ctrl)
	{
		if (Rnd.chance(20))
		{
			Functions.addItem(player, 14904, 1); // Sealed Magic Ornament (A-Grade)
		}
		else if (Rnd.chance(20))
		{
			Functions.addItem(player, 14905, 1); // Sealed Magic Ornament (S-Grade)
		}
		else
		{
			player.sendPacket(Msg.THERE_WAS_NOTHING_FOUND_INSIDE_OF_THAT);
		}
	}
	
	// Goddard Supply Box - Magic Ornament: Grade S, A
	private void use14899(Player player, boolean ctrl)
	{
		if (Rnd.chance(20))
		{
			Functions.addItem(player, 14904, 1); // Sealed Magic Ornament (A-Grade)
		}
		else if (Rnd.chance(20))
		{
			Functions.addItem(player, 14905, 1); // Sealed Magic Ornament (S-Grade)
		}
		else
		{
			player.sendPacket(Msg.THERE_WAS_NOTHING_FOUND_INSIDE_OF_THAT);
		}
	}
	
	// Rune Supply Box - Magic Ornament: Grade S, A
	private void use14900(Player player, boolean ctrl)
	{
		if (Rnd.chance(20))
		{
			Functions.addItem(player, 14904, 1); // Sealed Magic Ornament (A-Grade)
		}
		else if (Rnd.chance(20))
		{
			Functions.addItem(player, 14905, 1); // Sealed Magic Ornament (S-Grade)
		}
		else
		{
			player.sendPacket(Msg.THERE_WAS_NOTHING_FOUND_INSIDE_OF_THAT);
		}
	}
	
	// Schuttgart Supply Box - Magic Ornament: Grade S, A
	private void use14901(Player player, boolean ctrl)
	{
		if (Rnd.chance(20))
		{
			Functions.addItem(player, 14904, 1); // Sealed Magic Ornament (A-Grade)
		}
		else if (Rnd.chance(20))
		{
			Functions.addItem(player, 14905, 1); // Sealed Magic Ornament (S-Grade)
		}
		else
		{
			player.sendPacket(Msg.THERE_WAS_NOTHING_FOUND_INSIDE_OF_THAT);
		}
	}
	
	// Gift from Santa Claus
	private void use14616(Player player, boolean ctrl)
	{
		// Santa Claus' Weapon Exchange Ticket - 12 Hour Expiration Period
		Functions.addItem(player, 20107, 1);
		
		// Christmas Red Sock
		Functions.addItem(player, 14612, 1);
		
		// Special Christmas Tree
		if (Rnd.chance(30))
		{
			Functions.addItem(player, 5561, 1);
		}
		
		// Christmas Tree
		if (Rnd.chance(50))
		{
			Functions.addItem(player, 5560, 1);
		}
		
		// Agathion Seal Bracelet - Rudolph (????NN‚??N?½?½N‹?? ??N€?µ?/???µN‚)
		if ((Functions.getItemCount(player, 10606) == 0) && Rnd.chance(5))
		{
			Functions.addItem(player, 10606, 1);
		}
		
		// Agathion Seal Bracelet: Rudolph - 30
		if ((Functions.getItemCount(player, 20094) == 0) && Rnd.chance(3))
		{
			Functions.addItem(player, 20094, 1);
		}
		
		// Chest of Experience (Event)
		if (Rnd.chance(30))
		{
			Functions.addItem(player, 20575, 1);
		}
	}
	
	// Chest of Experience (Event)
	private void use20575(Player player, boolean ctrl)
	{
		Functions.addItem(player, 20335, 1); // Rune of Experience: 30% - 5 hour limited time
		Functions.addItem(player, 20341, 1); // Rune of SP 30% - 5 Hour Expiration Period
	}
	
	// Nepal Snow Agathion Pack
	private void use20804(Player player, boolean ctrl)
	{
		Functions.addItem(player, 20782, 1);
	}
	
	// Nepal Snow Agathion 7-Day Pack - Snow's Haste
	private void use20807(Player player, boolean ctrl)
	{
		Functions.addItem(player, 20785, 1);
	}
	
	// Round Ball Snow Agathion Pack
	private void use20805(Player player, boolean ctrl)
	{
		Functions.addItem(player, 20783, 1);
	}
	
	// Round Ball Snow Agathion 7-Day Pack - Snow's Acumen
	private void use20808(Player player, boolean ctrl)
	{
		Functions.addItem(player, 20786, 1);
	}
	
	// Ladder Snow Agathion Pack
	private void use20806(Player player, boolean ctrl)
	{
		Functions.addItem(player, 20784, 1);
	}
	
	// Ladder Snow Agathion 7-Day Pack - Snow's Wind Walk
	private void use20809(Player player, boolean ctrl)
	{
		Functions.addItem(player, 20787, 1);
	}
	
	// Iken Agathion Pack
	private void use20842(Player player, boolean ctrl)
	{
		Functions.addItem(player, 20818, 1);
	}
	
	// Iken Agathion 7-Day Pack Prominent Outsider Adventurer's Ability
	private void use20843(Player player, boolean ctrl)
	{
		Functions.addItem(player, 20819, 1);
	}
	
	// Lana Agathion Pack
	private void use20844(Player player, boolean ctrl)
	{
		Functions.addItem(player, 20820, 1);
	}
	
	// Lana Agathion 7-Day Pack Prominent Outsider Adventurer's Ability
	private void use20845(Player player, boolean ctrl)
	{
		Functions.addItem(player, 20821, 1);
	}
	
	// Gnocian Agathion Pack
	private void use20846(Player player, boolean ctrl)
	{
		Functions.addItem(player, 20822, 1);
	}
	
	// Gnocian Agathion 7-Day Pack Prominent Outsider Adventurer's Ability
	private void use20847(Player player, boolean ctrl)
	{
		Functions.addItem(player, 20823, 1);
	}
	
	// Orodriel Agathion Pack
	private void use20848(Player player, boolean ctrl)
	{
		Functions.addItem(player, 20824, 1);
	}
	
	// Orodriel Agathion 7-Day Pack Prominent Outsider Adventurer's Ability
	private void use20849(Player player, boolean ctrl)
	{
		Functions.addItem(player, 20825, 1);
	}
	
	// Lakinos Agathion Pack
	private void use20850(Player player, boolean ctrl)
	{
		Functions.addItem(player, 20826, 1);
	}
	
	// Lakinos Agathion 7-Day Pack Prominent Outsider Adventurer's Ability
	private void use20851(Player player, boolean ctrl)
	{
		Functions.addItem(player, 20827, 1);
	}
	
	// Mortia Agathion Pack
	private void use20852(Player player, boolean ctrl)
	{
		Functions.addItem(player, 20828, 1);
	}
	
	// Mortia Agathion 7-Day Pack Prominent Outsider Adventurer's Ability
	private void use20853(Player player, boolean ctrl)
	{
		Functions.addItem(player, 20829, 1);
	}
	
	// Hayance Agathion Pack
	private void use20854(Player player, boolean ctrl)
	{
		Functions.addItem(player, 20830, 1);
	}
	
	// Hayance Agathion 7-Day Pack Prominent Outsider Adventurer's Ability
	private void use20855(Player player, boolean ctrl)
	{
		Functions.addItem(player, 20831, 1);
	}
	
	// Meruril Agathion Pack
	private void use20856(Player player, boolean ctrl)
	{
		Functions.addItem(player, 20832, 1);
	}
	
	// Meruril Agathion 7-Day Pack Prominent Outsider Adventurer's Ability
	private void use20857(Player player, boolean ctrl)
	{
		Functions.addItem(player, 20833, 1);
	}
	
	// Taman ze Lapatui Agathion Pack
	private void use20858(Player player, boolean ctrl)
	{
		Functions.addItem(player, 20834, 1);
	}
	
	// Taman ze Lapatui Agathion 7-Day Pack Prominent Outsider Adventurer's Ability
	private void use20859(Player player, boolean ctrl)
	{
		Functions.addItem(player, 20835, 1);
	}
	
	// Kaurin Agathion Pack
	private void use20860(Player player, boolean ctrl)
	{
		Functions.addItem(player, 20836, 1);
	}
	
	// Kaurin Agathion 7-Day Pack Prominent Outsider Adventurer's Ability
	private void use20861(Player player, boolean ctrl)
	{
		Functions.addItem(player, 20837, 1);
	}
	
	// Ahertbein Agathion Pack
	private void use20862(Player player, boolean ctrl)
	{
		Functions.addItem(player, 20838, 1);
	}
	
	// Ahertbein Agathion 7-Day Pack Prominent Outsider Adventurer's Ability
	private void use20863(Player player, boolean ctrl)
	{
		Functions.addItem(player, 20839, 1);
	}
	
	// Naonin Agathion Pack
	private void use20864(Player player, boolean ctrl)
	{
		Functions.addItem(player, 20840, 1);
	}
	
	// Rocket Gun Hat Pack Continuous Fireworks
	private void use20811(Player player, boolean ctrl)
	{
		Functions.addItem(player, 20789, 1);
	}
	
	// Yellow Paper Hat 7-Day Pack Bless the Body
	private void use20812(Player player, boolean ctrl)
	{
		Functions.addItem(player, 20790, 1);
	}
	
	// Pink Paper Mask Set 7-Day Pack Bless the Soul
	private void use20813(Player player, boolean ctrl)
	{
		Functions.addItem(player, 20791, 1);
	}
	
	// Flavorful Cheese Hat Pack
	private void use20814(Player player, boolean ctrl)
	{
		Functions.addItem(player, 20792, 1);
	}
	
	// Sweet Cheese Hat Pack
	private void use20815(Player player, boolean ctrl)
	{
		Functions.addItem(player, 20793, 1);
	}
	
	// Flavorful Cheese Hat 7-Day Pack Scent of Flavorful Cheese
	private void use20816(Player player, boolean ctrl)
	{
		Functions.addItem(player, 20794, 1);
	}
	
	// Sweet Cheese Hat 7-Day Pack Scent of Sweet Cheese
	private void use20817(Player player, boolean ctrl)
	{
		Functions.addItem(player, 20795, 1);
	}
	
	// Flame Box Pack
	private void use20810(Player player, boolean ctrl)
	{
		Functions.addItem(player, 20725, 1);
	}
	
	// Naonin Agathion 7-Day Pack Prominent Outsider Adventurer's Ability
	private void use20865(Player player, boolean ctrl)
	{
		Functions.addItem(player, 20841, 1);
	}
	
	// Shiny Mask of Giant Hercules 7 day Pack
	private void use20748(Player player, boolean ctrl)
	{
		Functions.addItem(player, 20743, 1);
	}
	
	// Shiny Mask of Silent Scream 7 day Pack
	private void use20749(Player player, boolean ctrl)
	{
		Functions.addItem(player, 20744, 1);
	}
	
	// Shiny Spirit of Wrath Mask 7 day Pack
	private void use20750(Player player, boolean ctrl)
	{
		Functions.addItem(player, 20745, 1);
	}
	
	// Shiny Undecaying Corpse Mask 7 Day Pack
	private void use20751(Player player, boolean ctrl)
	{
		Functions.addItem(player, 20746, 1);
	}
	
	// Shiny Planet X235 Alien Mask 7 day Pack
	private void use20752(Player player, boolean ctrl)
	{
		Functions.addItem(player, 20747, 1);
	}
	
	private void use32264(Player player, boolean ctrl)
	{
		Functions.addItem(player, 30275, 5);
		Functions.addItem(player, 33502, 2);
		Functions.addItem(player, 33766, 5);
		Functions.addItem(player, 30310, 1);
	}
	
	private void use32265(Player player, boolean ctrl)
	{
		Functions.addItem(player, 30275, 5);
		Functions.addItem(player, 33502, 2);
		Functions.addItem(player, 33766, 5);
		Functions.addItem(player, 30311, 1);
	}
	
	private void use32266(Player player, boolean ctrl)
	{
		Functions.addItem(player, 30275, 5);
		Functions.addItem(player, 33502, 2);
		Functions.addItem(player, 33766, 5);
		Functions.addItem(player, 30312, 1);
	}
	
	private void use32267(Player player, boolean ctrl)
	{
		Functions.addItem(player, 30275, 5);
		Functions.addItem(player, 33502, 2);
		Functions.addItem(player, 33766, 5);
		Functions.addItem(player, 30313, 1);
	}
	
	private void use32268(Player player, boolean ctrl)
	{
		Functions.addItem(player, 30275, 5);
		Functions.addItem(player, 33502, 2);
		Functions.addItem(player, 33766, 5);
		Functions.addItem(player, 30314, 1);
	}
	
	private void use32269(Player player, boolean ctrl)
	{
		Functions.addItem(player, 30275, 5);
		Functions.addItem(player, 33502, 2);
		Functions.addItem(player, 33766, 5);
		Functions.addItem(player, 30315, 1);
	}
	
	private void use32270(Player player, boolean ctrl)
	{
		Functions.addItem(player, 30275, 5);
		Functions.addItem(player, 33502, 2);
		Functions.addItem(player, 33766, 5);
		Functions.addItem(player, 30316, 1);
	}
	
	private void use32271(Player player, boolean ctrl)
	{
		Functions.addItem(player, 30275, 5);
		Functions.addItem(player, 33502, 2);
		Functions.addItem(player, 33766, 5);
		Functions.addItem(player, 30317, 1);
	}
	
	private void use33477(Player player, boolean ctrl)
	{
		if (Rnd.chance(30))
		{
			Functions.addItem(player, 34698, 1);
		}
		
		if (Rnd.chance(30))
		{
			Functions.addItem(player, 34695, 1);
		}
		
		if (Rnd.chance(30))
		{
			Functions.addItem(player, 34694, 1);
		}
		
		if (Rnd.chance(5))
		{
			Functions.addItem(player, 34697, 1);
		}
		
		if (Rnd.chance(5))
		{
			Functions.addItem(player, 34696, 1);
		}
	}
	
	private void use34694(Player player, boolean ctrl)
	{
		if (Rnd.chance(16.6))
		{
			Functions.addItem(player, 12370, 1);
		}
		
		if (Rnd.chance(16.6))
		{
			Functions.addItem(player, 12365, 1);
		}
		
		if (Rnd.chance(16.6))
		{
			Functions.addItem(player, 12371, 1);
		}
		
		if (Rnd.chance(16.6))
		{
			Functions.addItem(player, 12366, 1);
		}
		
		if (Rnd.chance(16.6))
		{
			Functions.addItem(player, 30382, 1);
		}
		
		if (Rnd.chance(16.6))
		{
			Functions.addItem(player, 30381, 1);
		}
	}
	
	private void use34695(Player player, boolean ctrl)
	{
		if (Rnd.chance(16.6))
		{
			Functions.addItem(player, 22224, 1);
		}
		
		if (Rnd.chance(16.6))
		{
			Functions.addItem(player, 22223, 1);
		}
		
		if (Rnd.chance(16.6))
		{
			Functions.addItem(player, 22222, 1);
		}
		
		if (Rnd.chance(16.6))
		{
			Functions.addItem(player, 22221, 1);
		}
		
		if (Rnd.chance(16.6))
		{
			Functions.addItem(player, 33479, 1);
		}
		
		if (Rnd.chance(16.6))
		{
			Functions.addItem(player, 33478, 1);
		}
	}
	
	private void use34696(Player player, boolean ctrl)
	{
		if (Rnd.chance(16.6))
		{
			Functions.addItem(player, 6570, 1);
		}
		
		if (Rnd.chance(16.6))
		{
			Functions.addItem(player, 6569, 1);
		}
		
		if (Rnd.chance(16.6))
		{
			Functions.addItem(player, 6578, 1);
		}
		
		if (Rnd.chance(16.6))
		{
			Functions.addItem(player, 6577, 1);
		}
		
		if (Rnd.chance(16.6))
		{
			Functions.addItem(player, 19448, 1);
		}
		
		if (Rnd.chance(16.6))
		{
			Functions.addItem(player, 19447, 1);
		}
	}
	
	private void use34697(Player player, boolean ctrl)
	{
		if (Rnd.chance(50))
		{
			Functions.addItem(player, 19444, 1);
		}
		
		if (Rnd.chance(25))
		{
			Functions.addItem(player, 19445, 1);
		}
		
		if (Rnd.chance(25))
		{
			Functions.addItem(player, 19446, 1);
		}
	}
	
	private void use30281(Player player, boolean ctrl)
	{
		Functions.addItem(player, 19442, 10000);
	}
	
	private void use30279(Player player, boolean ctrl)
	{
		Functions.addItem(player, 19441, 10000);
	}
	
	private void use30277(Player player, boolean ctrl)
	{
		Functions.addItem(player, 17754, 10000);
	}
	
	private void use34698(Player player, boolean ctrl)
	{
		if (Rnd.chance(1.1))
		{
			Functions.addItem(player, 9552, 1);
		}
		
		if (Rnd.chance(1.1))
		{
			Functions.addItem(player, 9553, 1);
		}
		
		if (Rnd.chance(1.1))
		{
			Functions.addItem(player, 9554, 1);
		}
		
		if (Rnd.chance(1.1))
		{
			Functions.addItem(player, 9555, 1);
		}
		
		if (Rnd.chance(1.1))
		{
			Functions.addItem(player, 9556, 1);
		}
		
		if (Rnd.chance(1.1))
		{
			Functions.addItem(player, 9557, 1);
		}
		
		if (Rnd.chance(1.1))
		{
			Functions.addItem(player, 33863, 1);
		}
		
		if (Rnd.chance(1.1))
		{
			Functions.addItem(player, 33864, 1);
		}
		
		if (Rnd.chance(1.1))
		{
			Functions.addItem(player, 33865, 1);
		}
		
		if (Rnd.chance(1.1))
		{
			Functions.addItem(player, 33866, 1);
		}
		
		if (Rnd.chance(1.1))
		{
			Functions.addItem(player, 33867, 1);
		}
		
		if (Rnd.chance(1.1))
		{
			Functions.addItem(player, 33868, 1);
		}
		
		if (Rnd.chance(1.1))
		{
			Functions.addItem(player, 33869, 1);
		}
		
		if (Rnd.chance(1.1))
		{
			Functions.addItem(player, 33870, 1);
		}
		
		if (Rnd.chance(1.1))
		{
			Functions.addItem(player, 33871, 1);
		}
		
		if (Rnd.chance(1.1))
		{
			Functions.addItem(player, 33872, 1);
		}
		
		if (Rnd.chance(1.1))
		{
			Functions.addItem(player, 33873, 1);
		}
		
		if (Rnd.chance(1.1))
		{
			Functions.addItem(player, 33874, 1);
		}
	}
	
	// Simple Valentine Cake
	private void use20195(Player player, boolean ctrl)
	{
		// Velvety Valentine Cake
		if (Rnd.chance(20))
		{
			Functions.addItem(player, 20196, 1);
		}
		else
		{
			// Dragon Bomber Transformation Scroll
			if (Rnd.chance(5))
			{
				Functions.addItem(player, 20371, 1);
			}
			
			// Unicorn Transformation Scroll
			if (Rnd.chance(5))
			{
				Functions.addItem(player, 20367, 1);
			}
			
			// Quick Healing Potion
			if (Rnd.chance(10))
			{
				Functions.addItem(player, 1540, 1);
			}
			
			// Greater Healing Potion
			if (Rnd.chance(15))
			{
				Functions.addItem(player, 1539, 1);
			}
		}
	}
	
	// Velvety Valentine Cake
	private void use20196(Player player, boolean ctrl)
	{
		// Delectable Valentine Cake
		if (Rnd.chance(15))
		{
			Functions.addItem(player, 20197, 1);
		}
		else
		{
			// Scroll: Enchant Armor (C)
			if (Rnd.chance(10))
			{
				Functions.addItem(player, 952, 1);
			}
			
			// Scroll: Enchant Armor (B)
			if (Rnd.chance(5))
			{
				Functions.addItem(player, 948, 1);
			}
			
			// Blessed Scroll of Escape
			if (Rnd.chance(10))
			{
				Functions.addItem(player, 1538, 1);
			}
			
			// Blessed Scroll of Resurrection
			if (Rnd.chance(5))
			{
				Functions.addItem(player, 3936, 1);
			}
			
			// Agathion of Love - 3 Day Expiration Period
			if (Rnd.chance(10))
			{
				Functions.addItem(player, 20200, 1);
			}
		}
	}
	
	// Delectable Valentine Cake
	private void use20197(Player player, boolean ctrl)
	{
		// Decadent Valentine Cake
		if (Rnd.chance(10))
		{
			Functions.addItem(player, 20198, 1);
		}
		else
		{
			// Scroll: Enchant Weapon (C)
			if (Rnd.chance(10))
			{
				Functions.addItem(player, 951, 1);
			}
			
			// Scroll: Enchant Weapon (B)
			if (Rnd.chance(5))
			{
				Functions.addItem(player, 947, 1);
			}
			
			// Agathion of Love - 7 Day Expiration Period
			if (Rnd.chance(5))
			{
				Functions.addItem(player, 20201, 1);
			}
		}
	}
	
	// Decadent Valentine Cake
	private void use20198(Player player, boolean ctrl)
	{
		// Scroll: Enchant Weapon (S)
		if (Rnd.chance(5))
		{
			Functions.addItem(player, 959, 1);
		}
		
		// Scroll: Enchant Weapon (A)
		if (Rnd.chance(10))
		{
			Functions.addItem(player, 729, 1);
		}
		
		// Agathion of Love - 15 Day Expiration Period
		if (Rnd.chance(10))
		{
			Functions.addItem(player, 20202, 1);
		}
		
		// Agathion of Love - 30 Day Expiration Period
		if (Rnd.chance(5))
		{
			Functions.addItem(player, 20203, 1);
		}
	}
	
	private static final int[] SOI_books =
	{
		14209, // Forgotten Scroll - Hide
		14212, // Forgotten Scroll - Enlightenment - Wizard
		14213, // Forgotten Scroll - Enlightenment - Healer
		10554, // Forgotten Scroll - Anti-Magic Armor
		14208, // Forgotten Scroll - Final Secret
		10577  // Forgotten Scroll - Excessive Loyalty
	};
	
	// Jewel Ornamented Duel Supplies
	private void use13777(Player player, boolean ctrl)
	{
		int rnd = Rnd.get(100);
		if (rnd <= 65)
		{
			Functions.addItem(player, 9630, 3); // 3 Orichalcum
			Functions.addItem(player, 9629, 3); // 3 Adamantine
			Functions.addItem(player, 9628, 4); // 4 Leonard
			Functions.addItem(player, 8639, 6); // 6 Elixir of CP (S-Grade)
			Functions.addItem(player, 8627, 6); // 6 Elixir of Life (S-Grade)
			Functions.addItem(player, 8633, 6); // 6 Elixir of Mental Strength (S-Grade)
		}
		else if (rnd <= 95)
		{
			Functions.addItem(player, SOI_books[Rnd.get(SOI_books.length)], 1);
		}
		else
		{
			Functions.addItem(player, 14027, 1); // Collection Agathion Summon Bracelet
		}
	}
	
	// Mother-of-Pearl Ornamented Duel Supplies
	private void use13778(Player player, boolean ctrl)
	{
		int rnd = Rnd.get(100);
		if (rnd <= 65)
		{
			Functions.addItem(player, 9630, 2); // 3 Orichalcum
			Functions.addItem(player, 9629, 2); // 3 Adamantine
			Functions.addItem(player, 9628, 3); // 4 Leonard
			Functions.addItem(player, 8639, 5); // 5 Elixir of CP (S-Grade)
			Functions.addItem(player, 8627, 5); // 5 Elixir of Life (S-Grade)
			Functions.addItem(player, 8633, 5); // 5 Elixir of Mental Strength (S-Grade)
		}
		else if (rnd <= 95)
		{
			Functions.addItem(player, SOI_books[Rnd.get(SOI_books.length)], 1);
		}
		else
		{
			Functions.addItem(player, 14027, 1); // Collection Agathion Summon Bracelet
		}
	}
	
	// Gold-Ornamented Duel Supplies
	private void use13779(Player player, boolean ctrl)
	{
		int rnd = Rnd.get(100);
		if (rnd <= 65)
		{
			Functions.addItem(player, 9630, 1); // 1 Orichalcum
			Functions.addItem(player, 9629, 1); // 1 Adamantine
			Functions.addItem(player, 9628, 2); // 2 Leonard
			Functions.addItem(player, 8639, 4); // 4 Elixir of CP (S-Grade)
			Functions.addItem(player, 8627, 4); // 4 Elixir of Life (S-Grade)
			Functions.addItem(player, 8633, 4); // 4 Elixir of Mental Strength (S-Grade)
		}
		else if (rnd <= 95)
		{
			Functions.addItem(player, SOI_books[Rnd.get(SOI_books.length)], 1);
		}
		else
		{
			Functions.addItem(player, 14027, 1); // Collection Agathion Summon Bracelet
		}
	}
	
	// Silver-Ornamented Duel Supplies
	private void use13780(Player player, boolean ctrl)
	{
		Functions.addItem(player, 8639, 4); // 4 Elixir of CP (S-Grade)
		Functions.addItem(player, 8627, 4); // 4 Elixir of Life (S-Grade)
		Functions.addItem(player, 8633, 4); // 4 Elixir of Mental Strength (S-Grade)
	}
	
	// Bronze-Ornamented Duel Supplies
	private void use13781(Player player, boolean ctrl)
	{
		Functions.addItem(player, 8639, 4); // 4 Elixir of CP (S-Grade)
		Functions.addItem(player, 8627, 4); // 4 Elixir of Life (S-Grade)
		Functions.addItem(player, 8633, 4); // 4 Elixir of Mental Strength (S-Grade)
	}
	
	// Non-Ornamented Duel Supplies
	private void use13782(Player player, boolean ctrl)
	{
		Functions.addItem(player, 8639, 3); // 3 Elixir of CP (S-Grade)
		Functions.addItem(player, 8627, 3); // 3 Elixir of Life (S-Grade)
		Functions.addItem(player, 8633, 3); // 3 Elixir of Mental Strength (S-Grade)
	}
	
	// Weak-Looking Duel Supplies
	private void use13783(Player player, boolean ctrl)
	{
		Functions.addItem(player, 8639, 3); // 3 Elixir of CP (S-Grade)
		Functions.addItem(player, 8627, 3); // 3 Elixir of Life (S-Grade)
		Functions.addItem(player, 8633, 3); // 3 Elixir of Mental Strength (S-Grade)
	}
	
	// Sad-Looking Duel Supplies
	private void use13784(Player player, boolean ctrl)
	{
		Functions.addItem(player, 8639, 3); // 3 Elixir of CP (S-Grade)
		Functions.addItem(player, 8627, 3); // 3 Elixir of Life (S-Grade)
		Functions.addItem(player, 8633, 3); // 3 Elixir of Mental Strength (S-Grade)
	}
	
	// Poor-Looking Duel Supplies
	private void use13785(Player player, boolean ctrl)
	{
		Functions.addItem(player, 8639, 2); // 2 Elixir of CP (S-Grade)
		Functions.addItem(player, 8627, 2); // 2 Elixir of Life (S-Grade)
		Functions.addItem(player, 8633, 2); // 2 Elixir of Mental Strength (S-Grade)
	}
	
	// Worthless Duel Supplies
	private void use13786(Player player, boolean ctrl)
	{
		Functions.addItem(player, 8639, 1); // 1 Elixir of CP (S-Grade)
		Functions.addItem(player, 8627, 1); // 1 Elixir of Life (S-Grade)
		Functions.addItem(player, 8633, 1); // 1 Elixir of Mental Strength (S-Grade)
	}
	
    // Kahman's Supply Box
    private void use14849(Player player, boolean ctrl)
    {
        int[] list = new int[]{9625, 9626}; // codex_of_giant_forgetting, codex_of_giant_training
        int[] chances = new int[]{100, 80};
        int[] counts = new int[]{1, 1};
        extract_item_r(list, counts, chances, player);
    }

    // Big Stakato Cocoon
    private void use14834(Player player, boolean ctrl)
    {
        int[][] items = new int[][]{{9575, 1}, // rare_80_s
                {10485, 1}, // rare_82_s
                {10577, 1}, // sb_excessive_loyalty
                {14209, 1}, // sb_hide1
                {14208, 1}, // sb_final_secret1
                {14212, 1}, // sb_enlightenment_wizard1
                {960, 1}, // scrl_of_ench_am_s
                {9625, 1}, // codex_of_giant_forgetting
                {9626, 1}, // codex_of_giant_training
                {959, 1}, // scrl_of_ench_wp_s
                {10373, 1}, // rp_icarus_sowsword_i
                {10374, 1}, // rp_icarus_disperser_i
                {10375, 1}, // rp_icarus_spirits_i
                {10376, 1}, // rp_icarus_heavy_arms_i
                {10377, 1}, // rp_icarus_trident_i
                {10378, 1}, // rp_icarus_chopper_i
                {10379, 1}, // rp_icarus_knuckle_i
                {10380, 1}, // rp_icarus_wand_i
                {10381, 1}}; // rp_icarus_accipiter_i
        double[] chances = new double[]{2.77, 2.31, 3.2, 3.2, 3.2, 3.2, 6.4, 3.2, 2.13, 0.64, 1.54, 1.54, 1.54, 1.54, 1.54, 1.54, 1.54, 1.54, 1.54};
        extractRandomOneItem(player, items, chances);
    }

    // Small Stakato Cocoon
    private void use14833(Player player, boolean ctrl)
    {
        int[][] items = new int[][]{
        		{9575, 1}, // rare_80_s
                {10485, 1}, // rare_82_s
                {10577, 1}, // sb_excessive_loyalty
                {14209, 1}, // sb_hide1
                {14208, 1}, // sb_final_secret1
                {14212, 1}, // sb_enlightenment_wizard1
                {960, 1}, // scrl_of_ench_am_s
                {9625, 1}, // codex_of_giant_forgetting
                {9626, 1}, // codex_of_giant_training
                {959, 1}, // scrl_of_ench_wp_s
                {10373, 1}, // rp_icarus_sowsword_i
                {10374, 1}, // rp_icarus_disperser_i
                {10375, 1}, // rp_icarus_spirits_i
                {10376, 1}, // rp_icarus_heavy_arms_i
                {10377, 1}, // rp_icarus_trident_i
                {10378, 1}, // rp_icarus_chopper_i
                {10379, 1}, // rp_icarus_knuckle_i
                {10380, 1}, // rp_icarus_wand_i
                {10381, 1}}; // rp_icarus_accipiter_i
        double[] chances = new double[]{2.36, 1.96, 2.72, 2.72, 2.72, 2.72, 5.44, 2.72, 1.81, 0.54, 1.31, 1.31, 1.31, 1.31, 1.31, 1.31, 1.31, 1.31, 1.31};
        extractRandomOneItem(player, items, chances);
    }

    private void use13988(Player player, boolean ctrl)
    {
        int[] list = new int[]{9442, 9443, 9444, 9445, 9446, 9447, 9448, 9450, 10252, 10253, 10215, 10216, 10217, 10218, 10219, 10220, 10221, 10222, 10223};
        int[] chances = new int[]{64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 40, 40, 40, 40, 40, 40, 40, 40, 40};
        int[] counts = new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        extract_item_r(list, counts, chances, player);
    }

    private void use13989(Player player, boolean ctrl)
    {
        int[] list = new int[]{9514, 9515, 9516, 9517, 9518, 9519, 9520, 9521, 9522, 9523, 9524, 9525, 9526, 9527, 9528};
        int[] chances = new int[]{50, 63, 70, 75, 75, 50, 63, 70, 75, 75, 50, 63, 70, 75, 75};
        int[] counts = new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        extract_item_r(list, counts, chances, player);
    }

	// Pathfinder's Reward - D-Grade
	private void use13003(Player player, boolean ctrl)
	{
		if (Rnd.chance(3.2))
		{
			Functions.addItem(player, 947, 1); // Scroll: Enchant Weapon B
		}
	}
	
	// Pathfinder's Reward - C-Grade
	private void use13004(Player player, boolean ctrl)
	{
		if (Rnd.chance(1.6111))
		{
			Functions.addItem(player, 729, 1); // Scroll: Enchant Weapon A
		}
	}
	
	// Pathfinder's Reward - B-Grade
	private void use13005(Player player, boolean ctrl)
	{
		if (Rnd.chance(1.14))
		{
			Functions.addItem(player, 959, 1); // Scroll: Enchant Weapon S
		}
	}
	
    // Pathfinder's Reward - A-Grade
    private void use13006(Player player, boolean ctrl)
    {
        int[][] items = new int[][]{{9546, 1}, {9548, 1}, {9550, 1}, {959, 1}, {9442, 1}, {9443, 1}, {9444, 1}, {9445, 1}, {9446, 1}, {9447, 1}, {9448, 1}, {9449, 1}, {9450, 1}, {10252, 1}, {10253, 1}, {15645, 1}, {15646, 1}, {15647, 1}};
        double[] chances = new double[]{19.8, 19.8, 19.8, 1.98, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 1, 1, 1};
        extractRandomOneItem(player, items, chances);
    }

    // Pathfinder's Reward - S-Grade
    private void use13007(Player player, boolean ctrl)
    {
        int[][] items = new int[][]{{9546, 1}, {9548, 1}, {9550, 1}, {959, 1}, {10215, 1}, {10216, 1}, {10217, 1}, {10218, 1}, {10219, 1}, {10220, 1}, {10221, 1}, {10222, 1}, {10223, 1}};
        double[] chances = new double[]{26.4, 26.4, 26.4, 3.84, 0.13, 0.13, 0.13, 0.13, 0.13, 0.13, 0.13, 0.13, 0.13};
        extractRandomOneItem(player, items, chances);
    }
	
	// Pathfinder's Reward - AU Karm
	private void use13270(Player player, boolean ctrl)
	{
		if (Rnd.chance(30))
		{
			Functions.addItem(player, 13236, 1);
		}
	}
	
	// Pathfinder's Reward - AR Karm
	private void use13271(Player player, boolean ctrl)
	{
		if (Rnd.chance(30))
		{
			Functions.addItem(player, 13237, 1);
		}
	}
	
	// Pathfinder's Reward - AE Karm
	private void use13272(Player player, boolean ctrl)
	{
		if (Rnd.chance(30))
		{
			Functions.addItem(player, 13238, 1);
		}
	}
	
    private void use13990(Player player, boolean ctrl)
    {
        int[] list = new int[]{6364, 6365, 6366, 6367, 6368, 6369, 6370, 6371, 6372, 6534, 6579, 7575};
        int[] chances = new int[]{83, 83, 83, 83, 83, 83, 83, 83, 83, 83, 83, 83};
        int[] counts = new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        extract_item_r(list, counts, chances, player);
    }

    private void use13991(Player player, boolean ctrl)
    {
        int[] list = new int[]{6674, 6675, 6676, 6677, 6679, 6680, 6681, 6682, 6683, 6684, 6685, 6686, 6687};
        int[] chances = new int[]{70, 80, 95, 95, 90, 55, 95, 95, 90, 55, 95, 95, 90};
        int[] counts = new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        extract_item_r(list, counts, chances, player);
    }

    private void use13992(Player player, boolean ctrl)
    {
        int[] list = new int[]{6724, 6725, 6726};
        int[] chances = new int[]{25, 32, 42};
        int[] counts = new int[]{1, 1, 1};
		extract_item_r(list, counts, chances, player);
	}
	
	// Droph's Support Items
	private void use14850(Player player, boolean ctrl)
	{
		int rndAA = Rnd.get(80000, 100000);
		Functions.addItem(player, 5575, rndAA); // Ancient Adena
	}
	
	// Greater Elixir Gift Box (No-Grade)
	private void use14713(Player player, boolean ctrl)
	{
		Functions.addItem(player, 14682, 50); // Greater Elixir of Life (No-Grade)
		Functions.addItem(player, 14688, 50); // Greater Elixir of Mental Strength (No-Grade)
		Functions.addItem(player, 14694, 50); // Greater Elixir of CP (No Grade)
	}
	
	// Greater Elixir Gift Box (D-Grade)
	private void use14714(Player player, boolean ctrl)
	{
		Functions.addItem(player, 14683, 50); // Greater Elixir of Life (D-Grade)
		Functions.addItem(player, 14689, 50); // Greater Elixir of Mental Strength (D-Grade)
		Functions.addItem(player, 14695, 50); // Greater Elixir of CP (D Grade)
	}
	
	// Greater Elixir Gift Box (C-Grade)
	private void use14715(Player player, boolean ctrl)
	{
		Functions.addItem(player, 14684, 50); // Greater Elixir of Life (C-Grade)
		Functions.addItem(player, 14690, 50); // Greater Elixir of Mental Strength (C-Grade)
		Functions.addItem(player, 14696, 50); // Greater Elixir of CP (C Grade)
	}
	
	// Greater Elixir Gift Box (B-Grade)
	private void use14716(Player player, boolean ctrl)
	{
		Functions.addItem(player, 14685, 50); // Greater Elixir of Life (B-Grade)
		Functions.addItem(player, 14691, 50); // Greater Elixir of Mental Strength (B-Grade)
		Functions.addItem(player, 14697, 50); // Greater Elixir of CP (B Grade)
	}
	
	// Greater Elixir Gift Box (A-Grade)
	private void use14717(Player player, boolean ctrl)
	{
		Functions.addItem(player, 14686, 50); // Greater Elixir of Life (A-Grade)
		Functions.addItem(player, 14692, 50); // Greater Elixir of Mental Strength (A-Grade)
		Functions.addItem(player, 14698, 50); // Greater Elixir of CP (A Grade)
	}
	
	// Greater Elixir Gift Box (S-Grade)
	private void use14718(Player player, boolean ctrl)
	{
		Functions.addItem(player, 14687, 50); // Greater Elixir of Life (S-Grade)
		Functions.addItem(player, 14693, 50); // Greater Elixir of Mental Strength (S-Grade)
		Functions.addItem(player, 14699, 50); // Greater Elixir of CP (S Grade)
	}
	
	// Freya's Gift
    private void use17138(Player player, boolean ctrl)
    {
        int[][] items = new int[][]{{16026, 1}, {9627, 1}, {17139, 1}, {17140, 1}, {14052, 1}, {6622, 1}, {2134, 2}, {14701, 1}};
        double[] chances = new double[]{0.0001, 0.1417, 1.4172, 1.4172, 2.8345, 18.424, 21.2585, 54.5068};
        extractRandomOneItem(player, items, chances);
    }

    // Beginner Adventurer's Treasure Sack
    private void use21747(Player player, boolean ctrl)
    {
        int group = Rnd.get(7);
        int[] items = new int[0];
        if (group < 4) //Low D-Grade rewards
		{
			items = new int[]{312, 167, 220, 258, 178, 221, 123, 156, 291, 166, 274};
		}
		else if (group >= 4)
		{
			items = new int[]{261, 224, 318, 93, 129, 294, 88, 90, 158, 172, 279, 169};
		}
        Functions.addItem(player, items[Rnd.get(items.length)], 1);
    }

    // Experienced Adventurer's Treasure Sack
    private void use21748(Player player, boolean ctrl)
    {
        int group = Rnd.get(10);
        int[] items = new int[0];
        if (group < 4) //Low C-Grade rewards
		{
			items = new int[]{160, 298, 72, 193, 192, 281, 7887, 226, 2524, 191, 71, 263};
		}
		else if ((group >= 4) && (group < 7)) //Low B-Grade rewards
		{
			items = new int[]{78, 2571, 300, 284, 142, 267, 229, 148, 243, 92, 7892, 91};
		}
		else if ((group >= 7) && (group < 9)) //Low A-Grade rewards
		{
			items = new int[]{98, 5233, 80, 235, 269, 288, 7884, 2504, 150, 7899, 212};
		}
		else if (group == 9) //Low S-Grade rewards
		{
			items = new int[]{6365, 6371, 6364, 6366, 6580, 7575, 6579, 6372, 6370, 6369, 6367};
		}
        Functions.addItem(player, items[Rnd.get(items.length)], 1);
	}
	
	// Giant box
	private void use33465(Player player, boolean ctrl)
	{
		if (Rnd.chance(30))
		{
			if (Rnd.chance(40))
			{
				Functions.addItem(player, 30297, 1); // Codex
			}
			else if (Rnd.chance(20))
			{
				Functions.addItem(player, 30299, 1); // Discipline
			}
			else if (Rnd.chance(10))
			{
				Functions.addItem(player, 30298, 1); // Mastery
			}
			else if (Rnd.chance(30))
			{
				Functions.addItem(player, 30300, 1); // Oblivion
			}
		}
		else
		{
			player.sendPacket(Msg.THERE_WAS_NOTHING_FOUND_INSIDE_OF_THAT);// TODO
		}
	}
	
	// Great Adventurer's Treasure Sack
	private void use21749(Player player, boolean ctrl)
	{
		int group = Rnd.get(9);
		int[] items = new int[0];
		if (group < 5) //Top S-Grade rewards
		{
			items = new int[]{9447, 9384, 9449, 9380, 9448, 9443, 9450, 10253, 9445, 9442, 9446, 10004, 10252, 9376, 9444};
		}
		else if ((group >= 5) && (group < 8)) //S80-Grade rewards
		{
			items = new int[]{10226, 10217, 10224, 10215, 10225, 10223, 10220, 10415, 10216, 10221, 10219, 10218, 10222};
		}
		else if (group == 8) //Low S84-Grade rewards
		{
			items = new int[]{13467, 13462, 13464, 13461, 13465, 13468, 13463, 13470, 13460, 52, 13466, 13459, 13457, 13469, 13458};
		}
		Functions.addItem(player, items[Rnd.get(items.length)], 1);
	}
	
	// Golden Spice Crate
	private void use15482(Player player, boolean ctrl)
	{
		if (Rnd.chance(10))
		{
			Functions.addItem(player, 15474, 40);
			if (Rnd.chance(50))
			{
				Functions.addItem(player, 15476, 5);
			}
			else
			{
				Functions.addItem(player, 15478, 5);
			}
		}
		else
		{
			Functions.addItem(player, 15474, 50);
		}
	}
	
	// Crystal Spice Crate
	private void use15483(Player player, boolean ctrl)
	{
		if (Rnd.chance(10))
		{
			Functions.addItem(player, 15475, 40);
			if (Rnd.chance(50))
			{
				Functions.addItem(player, 15477, 5);
			}
			else
			{
				Functions.addItem(player, 15479, 5);
			}
		}
		else
		{
			Functions.addItem(player, 15475, 50);
		}
	}
	
	// Gold Maned Lion Mounting Bracelet 7 Day Pack
	private void use14231(Player player, boolean ctrl)
	{
		Functions.addItem(player, 14053, 1);
	}
	
	// Steam Beatle Mounting Bracelet 7 Day Pack
	private void use14232(Player player, boolean ctrl)
	{
		Functions.addItem(player, 14054, 1);
	}
	
	// Olympiad Treasure Chest
    private void use17169(Player player, boolean ctrl) {
        int[][] items = new int[][]{{13750, 1}, {13751, 1}, {13754, 1}, {13753, 1}, {13752, 1}, {6622, 1}};
        double[] chances = new double[]{34.7, 12.3, 2.65, 1.2, 1.98, 46.5, 5.4};
        if (Rnd.chance(60))
		{
			extractRandomOneItem(player, items, chances);
		}
        int[] counts = {100, 150, 200, 250, 300, 350};
        Functions.addItem(player, 13722, counts[Rnd.get(counts.length)]);
    }

	// Birthday Present Pack
	private void use21169(Player player, boolean ctrl)
	{
		Functions.addItem(player, 21170, 3);
		Functions.addItem(player, 21595, 1);
		Functions.addItem(player, 13488, 1);
	}
	
	// Pablo's Box
	private void use21753(Player player, boolean ctrl)
	{
		int category = Rnd.get(7);
		switch (category)
		{
			case 0:
				Functions.addItem(player, 21122, 1);
				break;
			case 1:
				Functions.addItem(player, 21118, 1);
				break;
			case 2:
				Functions.addItem(player, 21116, 1);
				break;
			case 3:
				Functions.addItem(player, 21114, 1);
				break;
			case 4:
				Functions.addItem(player, 21112, 1);
				break;
			case 5:
				Functions.addItem(player, 21120, 1);
				break;
			case 6:
				Functions.addItem(player, 21126, 1);
				break;
		}
	}
	
	// Rune Jewelry Box - Talisman
	private void use21752(Player player, boolean ctrl)
	{
		final List<Integer> talismans = new ArrayList<>();
		
		// 9914-9965
		for (int i = 9914; i <= 9965; i++)
		{
			if (i != 9923)
			{
				talismans.add(i);
			}
		}
		// 10416-10424
		for (int i = 10416; i <= 10424; i++)
		{
			talismans.add(i);
		}
		// 10518-10519
		for (int i = 10518; i <= 10519; i++)
		{
			talismans.add(i);
		}
		// 10533-10543
		for (int i = 10533; i <= 10543; i++)
		{
			talismans.add(i);
		}
		
		Functions.addItem(player, talismans.get(Rnd.get(talismans.size())), 1);
	}
	
	/**
	 * Method extract_item.
	 * @param list int[]
	 * @param counts int[]
	 * @param player Player
	 */
	private static void extract_item(int[] list, int[] counts, Player player)
	{
		int index = Rnd.get(list.length);
		int id = list[index];
		int count = counts[index];
		Functions.addItem(player, id, count);
	}
	
	/**
	 * Method mass_extract_item.
	 * @param source_count long
	 * @param list int[]
	 * @param counts int[]
	 * @param player Player
	 * @return List<int[]>
	 */
	private static List<int[]> mass_extract_item(long source_count, int[] list, int[] counts, Player player)
	{
		List<int[]> result = new ArrayList<>((int) Math.min(list.length, source_count));
		
		for (int n = 1; n <= source_count; n++)
		{
			int index = Rnd.get(list.length);
			int item = list[index];
			int count = counts[index];
			
			int[] old = null;
			for (int[] res : result)
			{
				if (res[0] == item)
				{
					old = res;
				}
			}
			
			if (old == null)
			{
				result.add(new int[]{item, count});
			}
			else
			{
				old[1] += count;
			}
		}
		
		return result;
	}
	
	/**
	 * Method extract_item_r.
	 * @param list int[]
	 * @param count_min int[]
	 * @param count_max int[]
	 * @param chances int[]
	 * @param player Player
	 */
	private static void extract_item_r(int[] list, int[] count_min, int[] count_max, int[] chances, Player player)
	{
		int[] counts = count_min;
		for (int i = 0; i < count_min.length; i++)
		{
			counts[i] = Rnd.get(count_min[i], count_max[i]);
		}
		extract_item_r(list, counts, chances, player);
	}
	
	/**
	 * Method extract_item_r.
	 * @param list int[]
	 * @param counts int[]
	 * @param chances int[]
	 * @param player Player
	 */
	private static void extract_item_r(int[] list, int[] counts, int[] chances, Player player)
	{
		int sum = 0;
		
		for (int i = 0; i < list.length; i++)
		{
			sum += chances[i];
		}
		
		int[] table = new int[sum];
		int k = 0;
		
		for (int i = 0; i < list.length; i++)
		{
			for (int j = 0; j < chances[i]; j++)
			{
				table[k] = i;
				k++;
			}
		}
		
		int i = table[Rnd.get(table.length)];
		int item = list[i];
		int count = counts[i];
		
		Functions.addItem(player, item, count);
	}
	
	/**
	 * Method mass_extract_item_r.
	 * @param source_count long
	 * @param list int[]
	 * @param count_min int[]
	 * @param count_max int[]
	 * @param chances int[]
	 * @param player Player
	 * @return List<int[]>
	 */
	private static List<int[]> mass_extract_item_r(long source_count, int[] list, int[] count_min, int[] count_max, int[] chances, Player player)
	{
		int[] counts = count_min;
		for (int i = 0; i < count_min.length; i++)
		{
			counts[i] = Rnd.get(count_min[i], count_max[i]);
		}
		return mass_extract_item_r(source_count, list, counts, chances, player);
	}
	
	/**
	 * Method mass_extract_item_r.
	 * @param source_count long
	 * @param list int[]
	 * @param counts int[]
	 * @param chances int[]
	 * @param player Player
	 * @return List<int[]>
	 */
	private static List<int[]> mass_extract_item_r(long source_count, int[] list, int[] counts, int[] chances, Player player)
	{
		List<int[]> result = new ArrayList<>((int) Math.min(list.length, source_count));
		
		int sum = 0;
		for (int i = 0; i < list.length; i++)
		{
			sum += chances[i];
		}
		
		int[] table = new int[sum];
		int k = 0;
		
		for (int i = 0; i < list.length; i++)
		{
			for (int j = 0; j < chances[i]; j++)
			{
				table[k] = i;
				k++;
			}
		}
		
		for (int n = 1; n <= source_count; n++)
		{
			int i = table[Rnd.get(table.length)];
			int item = list[i];
			int count = counts[i];
			
			int[] old = null;
			for (int[] res : result)
			{
				if (res[0] == item)
				{
					old = res;
				}
			}
			
			if (old == null)
			{
				result.add(new int[]{item, count});
			}
			else
			{
				old[1] += count;
			}
		}
		
		return result;
	}
	
	/**
	 * Method canBeExtracted.
	 * @param player Player
	 * @param item ItemInstance
	 * @return boolean
	 */
	private static boolean canBeExtracted(Player player, ItemInstance item)
	{
		if ((player.getWeightPenalty() >= 3) || (player.getInventory().getSize() > (player.getInventoryLimit() - 10)))
		{
			player.sendPacket(Msg.YOUR_INVENTORY_IS_FULL, new SystemMessage(SystemMessage.S1_CANNOT_BE_USED_DUE_TO_UNSUITABLE_TERMS).addItemName(item.getItemId()));
			return false;
		}
		return true;
	}
	
	/**
	 * Method extractRandomOneItem.
	 * @param player Player
	 * @param items int[][]
	 * @param chances double[]
	 * @return boolean
	 */
	private static boolean extractRandomOneItem(Player player, int[][] items, double[] chances)
	{
		if (items.length != chances.length)
		{
			return false;
		}
		
		double extractChance = 0;
		for (double c : chances)
		{
			extractChance += c;
		}
		
		if (Rnd.chance(extractChance))
		{
			int[] successfulItems = new int[0];
			while (successfulItems.length == 0)
			{
				for (int i = 0; i < items.length; i++)
				{
					if (Rnd.chance(chances[i]))
					{
						successfulItems = ArrayUtils.add(successfulItems, i);
					}
				}
			}
			int[] item = items[successfulItems[Rnd.get(successfulItems.length)]];
			if (item.length < 2)
			{
				return false;
			}
			
			Functions.addItem(player, item[0], item[1]);
		}
		return true;
	}
}
