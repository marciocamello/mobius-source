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
package events.SummerMeleons;

import java.util.List;
import java.util.concurrent.ScheduledFuture;

import lineage2.commons.threading.RunnableImpl;
import lineage2.commons.util.Rnd;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.ai.Fighter;
import lineage2.gameserver.data.xml.holder.NpcHolder;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.SimpleSpawner;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.reward.RewardData;
import lineage2.gameserver.model.reward.RewardItem;
import lineage2.gameserver.network.serverpackets.MagicSkillUse;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.utils.Log;
import npc.model.MeleonInstance;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class MeleonAI extends Fighter
{
	/**
	 * @author Mobius
	 */
	private final class PolimorphTask extends RunnableImpl
	{
		/**
		 * 
		 */
		public PolimorphTask()
		{
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			final MeleonInstance actor = getActor();
			
			if (actor == null)
			{
				return;
			}
			
			SimpleSpawner spawn = null;
			
			try
			{
				spawn = new SimpleSpawner(NpcHolder.getInstance().getTemplate(_npcId));
				spawn.setLoc(actor.getLoc());
				final NpcInstance npc = spawn.doSpawn(true);
				npc.setAI(new MeleonAI(npc));
				((MeleonInstance) npc).setSpawner(actor.getSpawner());
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			
			_timeToUnspawn = Long.MAX_VALUE;
			actor.deleteMe();
		}
	}
	
	private static final RewardData[] _dropList = new RewardData[]
	{
		new RewardData(1539, 1, 5, 15000),
		new RewardData(1374, 1, 3, 15000),
		new RewardData(4411, 1, 1, 5000),
		new RewardData(4412, 1, 1, 5000),
		new RewardData(4413, 1, 1, 5000),
		new RewardData(4414, 1, 1, 5000),
		new RewardData(4415, 1, 1, 5000),
		new RewardData(4416, 1, 1, 5000),
		new RewardData(4417, 1, 1, 5000),
		new RewardData(5010, 1, 1, 5000),
		new RewardData(1458, 10, 30, 13846),
		new RewardData(1459, 10, 30, 3000),
		new RewardData(1460, 10, 30, 1000),
		new RewardData(1461, 10, 30, 600),
		new RewardData(1462, 10, 30, 360),
		new RewardData(4161, 1, 1, 5000),
		new RewardData(4182, 1, 1, 5000),
		new RewardData(4174, 1, 1, 5000),
		new RewardData(4166, 1, 1, 5000),
		new RewardData(8660, 1, 1, 1000),
		new RewardData(8661, 1, 1, 1000),
		new RewardData(4393, 1, 1, 300),
		new RewardData(7836, 1, 1, 200),
		new RewardData(5590, 1, 1, 200),
		new RewardData(7058, 1, 1, 50),
		new RewardData(8350, 1, 1, 50),
		new RewardData(5133, 1, 1, 50),
		new RewardData(5817, 1, 1, 50),
		new RewardData(9140, 1, 1, 30),
		new RewardData(9177, 1, 1, 100),
		new RewardData(9178, 1, 1, 100),
		new RewardData(9179, 1, 1, 100),
		new RewardData(9180, 1, 1, 100),
		new RewardData(9181, 1, 1, 100),
		new RewardData(9182, 1, 1, 100),
		new RewardData(9183, 1, 1, 100),
		new RewardData(9184, 1, 1, 100),
		new RewardData(9185, 1, 1, 100),
		new RewardData(9186, 1, 1, 100),
		new RewardData(9187, 1, 1, 100),
		new RewardData(9188, 1, 1, 100),
		new RewardData(9189, 1, 1, 100),
		new RewardData(9190, 1, 1, 100),
		new RewardData(9191, 1, 1, 100),
		new RewardData(9192, 1, 1, 100),
		new RewardData(9193, 1, 1, 100),
		new RewardData(9194, 1, 1, 100),
		new RewardData(9195, 1, 1, 100),
		new RewardData(9196, 1, 1, 100),
		new RewardData(9197, 1, 1, 100),
		new RewardData(9198, 1, 1, 100),
		new RewardData(9199, 1, 1, 100),
		new RewardData(9200, 1, 1, 100),
		new RewardData(9201, 1, 1, 100),
		new RewardData(9202, 1, 1, 100),
		new RewardData(9203, 1, 1, 100),
		new RewardData(9204, 1, 1, 100),
		new RewardData(9146, 1, 3, 5000),
		new RewardData(9147, 1, 3, 5000),
		new RewardData(9148, 1, 3, 5000),
		new RewardData(9149, 1, 3, 5000),
		new RewardData(9150, 1, 3, 5000),
		new RewardData(9151, 1, 3, 5000),
		new RewardData(9152, 1, 3, 5000),
		new RewardData(9153, 1, 3, 5000),
		new RewardData(9154, 1, 3, 5000),
		new RewardData(9155, 1, 3, 5000),
		new RewardData(9156, 1, 3, 2000),
		new RewardData(9157, 1, 3, 1000),
		new RewardData(955, 1, 1, 400),
		new RewardData(956, 1, 1, 2000),
		new RewardData(951, 1, 1, 300),
		new RewardData(952, 1, 1, 1500),
		new RewardData(947, 1, 1, 200),
		new RewardData(948, 1, 1, 1000),
		new RewardData(729, 1, 1, 100),
		new RewardData(730, 1, 1, 500),
		new RewardData(959, 1, 1, 50),
		new RewardData(960, 1, 1, 300)
	};
	private final static int Young_Watermelon = 13271;
	private final static int Rain_Watermelon = 13273;
	private final static int Defective_Watermelon = 13272;
	private final static int Young_Honey_Watermelon = 13275;
	private final static int Rain_Honey_Watermelon = 13277;
	private final static int Defective_Honey_Watermelon = 13276;
	private final static int Large_Rain_Watermelon = 13274;
	private final static int Large_Rain_Honey_Watermelon = 13278;
	private final static int Squash_Level_up = 4513;
	private final static int Squash_Poisoned = 4514;
	private static final String[] textOnSpawn = new String[]
	{
		"scripts.events.SummerMeleons.MeleonAI.textOnSpawn.0",
		"scripts.events.SummerMeleons.MeleonAI.textOnSpawn.1",
		"scripts.events.SummerMeleons.MeleonAI.textOnSpawn.2"
	};
	private static final String[] textOnAttack = new String[]
	{
		"Who is biting me? Ouch! Oops! Hey you, now I'm going to ask you!",
		"Ha ha ha, I grew all the envy, look!",
		"You do muff? Past the fruit can not!",
		"That's what you calculate your beats? Look better targeting teachers ...",
		"Do not waste your time, I'm immortal!",
		"Ha! True pleasant sound?",
		"As long as you attack me grow, and grow up, you'll be up to two times!",
		"You hit or tickle? Can not make it ... Pathetic attempts!",
		"Only musical weapon opens watermelon. Your blunt weapon is not an assistant!"
	};
	private static final String[] textTooFast = new String[]
	{
		"That's what blows! That's technique!",
		"Hey you! Your skills are deplorable, my grandmother fights better! Ha Ha Ha!",
		"Lets hit once more, and again!",
		"I'm your house Shatal pipe!",
		"Hey, and Semyon there? A five adena? And call? Hahaha!",
		"What obscenity! Come without these jokes!",
		"Show imagination, come back, what are you topcheshsya!",
		"Wake up as you leave, you're quite dull and boring ..."
	};
	private static final String[] textSuccess0 = new String[]
	{
		"Watermelon grows well if his drink carefully, you know this secret, is not it?",
		"That's what I nectar, and there is always some slop!",
		"I see, I perceive, this is China, oh my God, I am a Chinese watermelon!",
		"Let's pour more between the first and second pereryvchik small!",
		"Refueling on the fly!"
	};
	private static final String[] textFail0 = new String[]
	{
		"You deaf? I need nectar, and not something that you pour you!",
		"What you're a loser and look like cheerfully! I need nectar Leu quality, otherwise get shish!",
		"And again, fail, how long can? You want me to laugh?"
	};
	private static final String[] textSuccess1 = new String[]
	{
		"Now sing! Arbuuuuuu-uh-uh!",
		"That's so good, so very good, do not stop!",
		"I grow quickly, have time to rebound? Ha!",
		"You're a master of his craft! More, please!"
	};
	private static final String[] textFail1 = new String[]
	{
		"Strike while the iron on the spot! Otherwise, you are no gingerbread.",
		"Muddler! Ignoramus! Boobies! Loser! Again you fed me slop!",
		"Let's have actively spud, watered well, what kind of pathetic attempts?",
		"You want me to so he died? Come to grow right!"
	};
	private static final String[] textSuccess2 = new String[]
	{
		"Here! Whew! Come on, and soon I'll love you forever!",
		"At this rate, I will be the emperor of watermelons!",
		"Very good, I put you on the Ladder of agriculture, you wisely me grow!"
	};
	private static final String[] textFail2 = new String[]
	{
		"And you do local? Watermelon eyes You see? It is a failure!",
		"I'll give you a sign Loser of the Year, just as bad loser can cope with such a simple thing!",
		"Well, Feed me, huh? Normally only, not here this dubious nectar ...",
		"And you're not a terrorist event? Can you hunger morish me? What do you want?"
	};
	private static final String[] textSuccess3 = new String[]
	{
		"Life is getting better, do not be sorry lei!",
		"You taught this mom do you have a great work!",
		"Why do you have growth? There be? I will be very juicy watermelon!"
	};
	private static final String[] textFail3 = new String[]
	{
		"Is that Vodicka of sewage? You know what nectar?",
		"Gods, save me from this sad sack, he's all the spoils!"
	};
	private static final String[] textSuccess4 = new String[]
	{
		"That's a charge! Have you slipped into nectar? There's just 40 degrees! Ahahaha I get drunk!",
		"You're risking not grow watermelon, and the whole rocket! Adds, come again!"
	};
	private static final String[] textFail4 = new String[]
	{
		"Oh, how I want to drink ... Nectar, please ...",
		"Lei nectar here and see what happens!"
	};
	int _npcId;
	private int _nectar;
	private int _tryCount;
	private long _lastNectarUse;
	long _timeToUnspawn;
	private ScheduledFuture<?> _polimorphTask;
	private static final int NECTAR_REUSE = 3000;
	
	/**
	 * Constructor for MeleonAI.
	 * @param actor NpcInstance
	 */
	MeleonAI(NpcInstance actor)
	{
		super(actor);
		_npcId = getActor().getNpcId();
		Functions.npcSayCustomMessage(getActor(), textOnSpawn[Rnd.get(textOnSpawn.length)]);
		_timeToUnspawn = System.currentTimeMillis() + 120000;
	}
	
	/**
	 * Method thinkActive.
	 * @return boolean
	 */
	@Override
	protected boolean thinkActive()
	{
		if (System.currentTimeMillis() > _timeToUnspawn)
		{
			_timeToUnspawn = Long.MAX_VALUE;
			
			if (_polimorphTask != null)
			{
				_polimorphTask.cancel(false);
				_polimorphTask = null;
			}
			
			final MeleonInstance actor = getActor();
			actor.deleteMe();
		}
		
		return false;
	}
	
	/**
	 * Method onEvtSeeSpell.
	 * @param skill Skill
	 * @param caster Creature
	 */
	@Override
	protected void onEvtSeeSpell(Skill skill, Creature caster)
	{
		final MeleonInstance actor = getActor();
		
		if ((actor == null) || (skill.getId() != 2005))
		{
			return;
		}
		
		if ((actor.getNpcId() != Young_Watermelon) && (actor.getNpcId() != Young_Honey_Watermelon))
		{
			return;
		}
		
		switch (_tryCount)
		{
			case 0:
				_tryCount++;
				_lastNectarUse = System.currentTimeMillis();
				
				if (Rnd.chance(50))
				{
					_nectar++;
					Functions.npcSay(actor, textSuccess0[Rnd.get(textSuccess0.length)]);
					actor.broadcastPacket(new MagicSkillUse(actor, actor, Squash_Level_up, 1, NECTAR_REUSE, 0));
				}
				else
				{
					Functions.npcSay(actor, textFail0[Rnd.get(textFail0.length)]);
					actor.broadcastPacket(new MagicSkillUse(actor, actor, Squash_Poisoned, 1, NECTAR_REUSE, 0));
				}
				
				break;
			
			case 1:
				if ((System.currentTimeMillis() - _lastNectarUse) < NECTAR_REUSE)
				{
					Functions.npcSay(actor, textTooFast[Rnd.get(textTooFast.length)]);
					return;
				}
				
				_tryCount++;
				_lastNectarUse = System.currentTimeMillis();
				
				if (Rnd.chance(50))
				{
					_nectar++;
					Functions.npcSay(actor, textSuccess1[Rnd.get(textSuccess1.length)]);
					actor.broadcastPacket(new MagicSkillUse(actor, actor, Squash_Level_up, 1, NECTAR_REUSE, 0));
				}
				else
				{
					Functions.npcSay(actor, textFail1[Rnd.get(textFail1.length)]);
					actor.broadcastPacket(new MagicSkillUse(actor, actor, Squash_Poisoned, 1, NECTAR_REUSE, 0));
				}
				
				break;
			
			case 2:
				if ((System.currentTimeMillis() - _lastNectarUse) < NECTAR_REUSE)
				{
					Functions.npcSay(actor, textTooFast[Rnd.get(textTooFast.length)]);
					return;
				}
				
				_tryCount++;
				_lastNectarUse = System.currentTimeMillis();
				
				if (Rnd.chance(50))
				{
					_nectar++;
					Functions.npcSay(actor, textSuccess2[Rnd.get(textSuccess2.length)]);
					actor.broadcastPacket(new MagicSkillUse(actor, actor, Squash_Level_up, 1, NECTAR_REUSE, 0));
				}
				else
				{
					Functions.npcSay(actor, textFail2[Rnd.get(textFail2.length)]);
					actor.broadcastPacket(new MagicSkillUse(actor, actor, Squash_Poisoned, 1, NECTAR_REUSE, 0));
				}
				
				break;
			
			case 3:
				if ((System.currentTimeMillis() - _lastNectarUse) < NECTAR_REUSE)
				{
					Functions.npcSay(actor, textTooFast[Rnd.get(textTooFast.length)]);
					return;
				}
				
				_tryCount++;
				_lastNectarUse = System.currentTimeMillis();
				
				if (Rnd.chance(50))
				{
					_nectar++;
					Functions.npcSay(actor, textSuccess3[Rnd.get(textSuccess3.length)]);
					actor.broadcastPacket(new MagicSkillUse(actor, actor, Squash_Level_up, 1, NECTAR_REUSE, 0));
				}
				else
				{
					Functions.npcSay(actor, textFail3[Rnd.get(textFail3.length)]);
					actor.broadcastPacket(new MagicSkillUse(actor, actor, Squash_Poisoned, 1, NECTAR_REUSE, 0));
				}
				
				break;
			
			case 4:
				if ((System.currentTimeMillis() - _lastNectarUse) < NECTAR_REUSE)
				{
					Functions.npcSay(actor, textTooFast[Rnd.get(textTooFast.length)]);
					return;
				}
				
				_tryCount++;
				_lastNectarUse = System.currentTimeMillis();
				
				if (Rnd.chance(50))
				{
					_nectar++;
					Functions.npcSay(actor, textSuccess4[Rnd.get(textSuccess4.length)]);
					actor.broadcastPacket(new MagicSkillUse(actor, actor, Squash_Level_up, 1, NECTAR_REUSE, 0));
				}
				else
				{
					Functions.npcSay(actor, textFail4[Rnd.get(textFail4.length)]);
					actor.broadcastPacket(new MagicSkillUse(actor, actor, Squash_Poisoned, 1, NECTAR_REUSE, 0));
				}
				
				if (_npcId == Young_Watermelon)
				{
					if (_nectar < 3)
					{
						_npcId = Defective_Watermelon;
					}
					else if (_nectar == 5)
					{
						_npcId = Large_Rain_Watermelon;
					}
					else
					{
						_npcId = Rain_Watermelon;
					}
				}
				else if (_npcId == Young_Honey_Watermelon)
				{
					if (_nectar < 3)
					{
						_npcId = Defective_Honey_Watermelon;
					}
					else if (_nectar == 5)
					{
						_npcId = Large_Rain_Honey_Watermelon;
					}
					else
					{
						_npcId = Rain_Honey_Watermelon;
					}
				}
				
				_polimorphTask = ThreadPoolManager.getInstance().schedule(new PolimorphTask(), NECTAR_REUSE);
				break;
		}
	}
	
	/**
	 * Method onEvtAttacked.
	 * @param attacker Creature
	 * @param damage int
	 */
	@Override
	protected void onEvtAttacked(Creature attacker, int damage)
	{
		final MeleonInstance actor = getActor();
		
		if ((actor != null) && Rnd.chance(5))
		{
			Functions.npcSay(actor, textOnAttack[Rnd.get(textOnAttack.length)]);
		}
	}
	
	/**
	 * Method onEvtDead.
	 * @param killer Creature
	 */
	@Override
	protected void onEvtDead(Creature killer)
	{
		_tryCount = -1;
		final MeleonInstance actor = getActor();
		
		if (actor == null)
		{
			return;
		}
		
		double dropMod = 1.5;
		
		switch (_npcId)
		{
			case Defective_Watermelon:
				dropMod *= 1;
				Functions.npcSay(actor, "Watermelon is open!");
				Functions.npcSay(actor, "Oho-ho Yes then crumbs, try better!");
				break;
			
			case Rain_Watermelon:
				dropMod *= 2;
				Functions.npcSay(actor, "Watermelon is open!");
				Functions.npcSay(actor, "Ay-ay-ay! Good catch!");
				break;
			
			case Large_Rain_Watermelon:
				dropMod *= 4;
				Functions.npcSay(actor, "Watermelon is open!");
				Functions.npcSay(actor, "Wow! What treasures!");
				break;
			
			case Defective_Honey_Watermelon:
				dropMod *= 12.5;
				Functions.npcSay(actor, "Watermelon is open!");
				Functions.npcSay(actor, "Spent a lot, and fished out a little!");
				break;
			
			case Rain_Honey_Watermelon:
				dropMod *= 25;
				Functions.npcSay(actor, "Watermelon is open!");
				Functions.npcSay(actor, "Boom-boom-boom! Nice catch!");
				break;
			
			case Large_Rain_Honey_Watermelon:
				dropMod *= 50;
				Functions.npcSay(actor, "Watermelon is open!");
				Functions.npcSay(actor, "Fanfare! You have opened a giant watermelon! Untold riches on earth! Catch them!");
				break;
			
			default:
				dropMod *= 0;
				Functions.npcSay(actor, "I will not give anything to you, if I die like this ...");
				Functions.npcSay(actor, "Этот позор навеки покроет твое им�?...");
				break;
		}
		
		super.onEvtDead(actor);
		
		if (dropMod > 0)
		{
			if (_polimorphTask != null)
			{
				_polimorphTask.cancel(false);
				_polimorphTask = null;
				Log.add("SummerMeleons :: Player " + actor.getSpawner().getName() + " tried to use cheat (SquashAI clone): killed " + actor + " after polymorfing started", "illegal-actions");
				return;
			}
			
			for (RewardData d : _dropList)
			{
				List<RewardItem> itd = d.roll(null, dropMod);
				
				for (RewardItem i : itd)
				{
					actor.dropItem(actor.getSpawner(), i.itemId, i.count);
				}
			}
		}
	}
	
	/**
	 * Method randomAnimation.
	 * @return boolean
	 */
	@Override
	protected boolean randomAnimation()
	{
		return false;
	}
	
	/**
	 * Method randomWalk.
	 * @return boolean
	 */
	@Override
	protected boolean randomWalk()
	{
		return false;
	}
	
	/**
	 * Method getActor.
	 * @return MeleonInstance
	 */
	@Override
	public MeleonInstance getActor()
	{
		return (MeleonInstance) super.getActor();
	}
}
