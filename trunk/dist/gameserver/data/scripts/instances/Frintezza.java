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
package instances;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledFuture;

import lineage2.commons.threading.RunnableImpl;
import lineage2.commons.util.Rnd;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.ai.CtrlEvent;
import lineage2.gameserver.listener.actor.OnCurrentHpDamageListener;
import lineage2.gameserver.listener.actor.OnDeathListener;
import lineage2.gameserver.listener.zone.OnZoneEnterLeaveListener;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.Zone;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.Earthquake;
import lineage2.gameserver.network.serverpackets.ExShowScreenMessage;
import lineage2.gameserver.network.serverpackets.ExShowScreenMessage.ScreenMessageAlign;
import lineage2.gameserver.network.serverpackets.MagicSkillCanceled;
import lineage2.gameserver.network.serverpackets.MagicSkillUse;
import lineage2.gameserver.network.serverpackets.SocialAction;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.network.serverpackets.components.NpcString;
import lineage2.gameserver.tables.SkillTable;
import lineage2.gameserver.utils.Location;

import org.apache.commons.lang3.ArrayUtils;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class Frintezza extends Reflection
{
	/**
	 * Field HallAlarmDevice. (value is 18328)
	 */
	private static final int HallAlarmDevice = 18328;
	/**
	 * Field DarkChoirPlayer. (value is 18339)
	 */
	private static final int DarkChoirPlayer = 18339;
	/**
	 * Field _weakScarletId. (value is 29046)
	 */
	private static final int _weakScarletId = 29046;
	/**
	 * Field _strongScarletId. (value is 29047)
	 */
	private static final int _strongScarletId = 29047;
	/**
	 * Field TeleportCube. (value is 29061)
	 */
	private static final int TeleportCube = 29061;
	/**
	 * Field _frintezzasSwordId. (value is 7903)
	 */
	private static final int _frintezzasSwordId = 7903;
	/**
	 * Field DewdropItem. (value is 8556)
	 */
	private static final int DewdropItem = 8556;
	/**
	 * Field hallADoors.
	 */
	static final int[] hallADoors =
	{
		17130051,
		17130052,
		17130053,
		17130054,
		17130055,
		17130056,
		17130057,
		17130058
	};
	/**
	 * Field corridorADoors.
	 */
	static final int[] corridorADoors =
	{
		17130042,
		17130043
	};
	/**
	 * Field hallBDoors.
	 */
	static final int[] hallBDoors =
	{
		17130061,
		17130062,
		17130063,
		17130064,
		17130065,
		17130066,
		17130067,
		17130068,
		17130069,
		17130070
	};
	/**
	 * Field corridorBDoors.
	 */
	static final int[] corridorBDoors =
	{
		17130045,
		17130046
	};
	/**
	 * Field blockANpcs.
	 */
	static final int[] blockANpcs =
	{
		18329,
		18330,
		18331,
		18333
	};
	/**
	 * Field blockBNpcs.
	 */
	static final int[] blockBNpcs =
	{
		18334,
		18335,
		18336,
		18337,
		18338
	};
	/**
	 * Field _intervalOfFrintezzaSongs.
	 */
	static int _intervalOfFrintezzaSongs = 30000;
	
	/**
	 * @author Mobius
	 */
	public static class NpcLocation extends Location
	{
		/**
	 * 
	 */
		private static final long serialVersionUID = 1L;
		/**
		 * Field npcId.
		 */
		public int npcId;
		
		/**
		 * Constructor for NpcLocation.
		 */
		public NpcLocation()
		{
		}
		
		/**
		 * Constructor for NpcLocation.
		 * @param x int
		 * @param y int
		 * @param z int
		 * @param heading int
		 * @param npcId int
		 */
		public NpcLocation(int x, int y, int z, int heading, int npcId)
		{
			super(x, y, z, heading);
			this.npcId = npcId;
		}
	}
	
	/**
	 * Field frintezzaSpawn.
	 */
	static NpcLocation frintezzaSpawn = new NpcLocation(-87784, -155090, -9080, 16048, 29045);
	/**
	 * Field scarletSpawnWeak.
	 */
	static NpcLocation scarletSpawnWeak = new NpcLocation(-87784, -153288, -9176, 16384, 29046);
	/**
	 * Field portraitSpawns.
	 */
	static NpcLocation[] portraitSpawns =
	{
		new NpcLocation(-86136, -153960, -9168, 35048, 29048),
		new NpcLocation(-86184, -152456, -9168, 28205, 29049),
		new NpcLocation(-89368, -152456, -9168, 64817, 29048),
		new NpcLocation(-89416, -153976, -9168, 57730, 29049)
	};
	/**
	 * Field demonSpawns.
	 */
	static NpcLocation[] demonSpawns =
	{
		new NpcLocation(-86136, -153960, -9168, 35048, 29050),
		new NpcLocation(-86184, -152456, -9168, 28205, 29051),
		new NpcLocation(-89368, -152456, -9168, 64817, 29051),
		new NpcLocation(-89416, -153976, -9168, 57730, 29050)
	};
	/**
	 * Field _frintezzaDummy.
	 */
	NpcInstance _frintezzaDummy;
	/**
	 * Field frintezza.
	 */
	NpcInstance frintezza;
	/**
	 * Field weakScarlet.
	 */
	NpcInstance weakScarlet;
	/**
	 * Field strongScarlet.
	 */
	NpcInstance strongScarlet;
	/**
	 * Field portraits.
	 */
	final NpcInstance[] portraits = new NpcInstance[4];
	/**
	 * Field demons.
	 */
	final NpcInstance[] demons = new NpcInstance[4];
	/**
	 * Field _scarletMorph.
	 */
	int _scarletMorph = 0;
	/**
	 * Field battleStartDelay.
	 */
	private static final long battleStartDelay = 5 * 60000L;
	/**
	 * Field _deathListener.
	 */
	final DeathListener _deathListener = new DeathListener();
	/**
	 * Field _currentHpListener.
	 */
	final CurrentHpListener _currentHpListener = new CurrentHpListener();
	/**
	 * Field _zoneListener.
	 */
	private final ZoneListener _zoneListener = new ZoneListener();
	/**
	 * Field musicTask.
	 */
	ScheduledFuture<?> musicTask;
	
	/**
	 * Method onCreate.
	 */
	@Override
	protected void onCreate()
	{
		super.onCreate();
		getZone("[Frintezza]").addListener(_zoneListener);
		for (NpcInstance n : getNpcs())
		{
			n.addListener(_deathListener);
		}
		blockUnblockNpcs(true, blockANpcs);
	}
	
	/**
	 * @author Mobius
	 */
	private class FrintezzaStart extends RunnableImpl
	{
		/**
		 * Constructor for FrintezzaStart.
		 */
		public FrintezzaStart()
		{
			// TODO Auto-generated constructor stub
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			ThreadPoolManager.getInstance().schedule(new Spawn(1), 1000);
		}
	}
	
	/**
	 * @author Mobius
	 */
	private class Spawn extends RunnableImpl
	{
		/**
		 * Field _taskId.
		 */
		private int _taskId = 0;
		
		/**
		 * Constructor for Spawn.
		 * @param taskId int
		 */
		public Spawn(int taskId)
		{
			_taskId = taskId;
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			try
			{
				switch (_taskId)
				{
					case 1:
						_frintezzaDummy = spawn(new NpcLocation(-87784, -155096, -9080, 16048, 29059));
						ThreadPoolManager.getInstance().schedule(new Spawn(2), 1000);
						break;
					case 2:
						closeDoor(corridorBDoors[1]);
						frintezza = spawn(frintezzaSpawn);
						showSocialActionMovie(frintezza, 500, 90, 0, 6500, 8000, 0);
						for (int i = 0; i < 4; i++)
						{
							portraits[i] = spawn(portraitSpawns[i]);
							portraits[i].startImmobilized();
							demons[i] = spawn(demonSpawns[i]);
						}
						blockAll(true);
						ThreadPoolManager.getInstance().schedule(new Spawn(3), 6500);
						break;
					case 3:
						showSocialActionMovie(_frintezzaDummy, 1800, 90, 8, 6500, 7000, 0);
						ThreadPoolManager.getInstance().schedule(new Spawn(4), 900);
						break;
					case 4:
						showSocialActionMovie(_frintezzaDummy, 140, 90, 10, 2500, 4500, 0);
						ThreadPoolManager.getInstance().schedule(new Spawn(5), 4000);
						break;
					case 5:
						showSocialActionMovie(frintezza, 40, 75, -10, 0, 1000, 0);
						showSocialActionMovie(frintezza, 40, 75, -10, 0, 12000, 0);
						ThreadPoolManager.getInstance().schedule(new Spawn(6), 1350);
						break;
					case 6:
						frintezza.broadcastPacket(new SocialAction(frintezza.getObjectId(), 2));
						ThreadPoolManager.getInstance().schedule(new Spawn(7), 7000);
						break;
					case 7:
						_frintezzaDummy.deleteMe();
						_frintezzaDummy = null;
						ThreadPoolManager.getInstance().schedule(new Spawn(8), 1000);
						break;
					case 8:
						showSocialActionMovie(demons[0], 140, 0, 3, 22000, 3000, 1);
						ThreadPoolManager.getInstance().schedule(new Spawn(9), 2800);
						break;
					case 9:
						showSocialActionMovie(demons[1], 140, 0, 3, 22000, 3000, 1);
						ThreadPoolManager.getInstance().schedule(new Spawn(10), 2800);
						break;
					case 10:
						showSocialActionMovie(demons[2], 140, 180, 3, 22000, 3000, 1);
						ThreadPoolManager.getInstance().schedule(new Spawn(11), 2800);
						break;
					case 11:
						showSocialActionMovie(demons[3], 140, 180, 3, 22000, 3000, 1);
						ThreadPoolManager.getInstance().schedule(new Spawn(12), 3000);
						break;
					case 12:
						showSocialActionMovie(frintezza, 240, 90, 0, 0, 1000, 0);
						showSocialActionMovie(frintezza, 240, 90, 25, 5500, 10000, 3);
						ThreadPoolManager.getInstance().schedule(new Spawn(13), 3000);
						break;
					case 13:
						showSocialActionMovie(frintezza, 100, 195, 35, 0, 10000, 0);
						ThreadPoolManager.getInstance().schedule(new Spawn(14), 700);
						break;
					case 14:
						showSocialActionMovie(frintezza, 100, 195, 35, 0, 10000, 0);
						ThreadPoolManager.getInstance().schedule(new Spawn(15), 1300);
						break;
					case 15:
						showSocialActionMovie(frintezza, 120, 180, 45, 1500, 10000, 0);
						frintezza.broadcastPacket(new MagicSkillUse(frintezza, frintezza, 5006, 1, 34000, 0));
						ThreadPoolManager.getInstance().schedule(new Spawn(16), 1500);
						break;
					case 16:
						showSocialActionMovie(frintezza, 520, 135, 45, 8000, 10000, 0);
						ThreadPoolManager.getInstance().schedule(new Spawn(17), 7500);
						break;
					case 17:
						showSocialActionMovie(frintezza, 1500, 110, 25, 10000, 13000, 0);
						ThreadPoolManager.getInstance().schedule(new Spawn(18), 9500);
						break;
					case 18:
						weakScarlet = spawn(scarletSpawnWeak);
						block(weakScarlet, true);
						weakScarlet.addListener(_currentHpListener);
						weakScarlet.broadcastPacket(new MagicSkillUse(weakScarlet, weakScarlet, 5016, 1, 3000, 0));
						Earthquake eq = new Earthquake(weakScarlet.getLoc(), 50, 6);
						for (Player pc : getPlayers())
						{
							pc.broadcastPacket(eq);
						}
						showSocialActionMovie(weakScarlet, 1000, 160, 20, 6000, 6000, 0);
						ThreadPoolManager.getInstance().schedule(new Spawn(19), 5500);
						break;
					case 19:
						showSocialActionMovie(weakScarlet, 800, 160, 5, 1000, 10000, 2);
						ThreadPoolManager.getInstance().schedule(new Spawn(20), 2100);
						break;
					case 20:
						showSocialActionMovie(weakScarlet, 300, 60, 8, 0, 10000, 0);
						ThreadPoolManager.getInstance().schedule(new Spawn(21), 2000);
						break;
					case 21:
						showSocialActionMovie(weakScarlet, 1000, 90, 10, 3000, 5000, 0);
						ThreadPoolManager.getInstance().schedule(new Spawn(22), 3000);
						break;
					case 22:
						for (Player pc : getPlayers())
						{
							pc.leaveMovieMode();
						}
						ThreadPoolManager.getInstance().schedule(new Spawn(23), 2000);
						break;
					case 23:
						blockAll(false);
						spawn(new NpcLocation(-87904, -141296, -9168, 0, TeleportCube));
						_scarletMorph = 1;
						musicTask = ThreadPoolManager.getInstance().schedule(new Music(), 5000);
						break;
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * @author Mobius
	 */
	private class Music extends RunnableImpl
	{
		/**
		 * Constructor for Music.
		 */
		public Music()
		{
			// TODO Auto-generated constructor stub
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			if (frintezza == null)
			{
				return;
			}
			int song = Math.max(1, Math.min(4, getSong()));
			NpcString song_name;
			switch (song)
			{
				case 1:
					song_name = NpcString.REQUIEM_OF_HATRED;
					break;
				case 2:
					song_name = NpcString.FRENETIC_TOCCATA;
					break;
				case 3:
					song_name = NpcString.FUGUE_OF_JUBILATION;
					break;
				case 4:
					song_name = NpcString.MOURNFUL_CHORALE_PRELUDE;
					break;
				default:
					return;
			}
			if (!frintezza.isBlocked())
			{
				frintezza.broadcastPacket(new ExShowScreenMessage(song_name, 3000, ScreenMessageAlign.TOP_CENTER, true, 1, -1, true));
				frintezza.broadcastPacket(new MagicSkillUse(frintezza, frintezza, 5007, song, _intervalOfFrintezzaSongs, 0));
				ThreadPoolManager.getInstance().schedule(new SongEffectLaunched(getSongTargets(song), song, 10000), 10000);
			}
			musicTask = ThreadPoolManager.getInstance().schedule(new Music(), _intervalOfFrintezzaSongs + Rnd.get(10000));
		}
		
		/**
		 * Method getSongTargets.
		 * @param songId int
		 * @return List<Creature>
		 */
		private List<Creature> getSongTargets(int songId)
		{
			List<Creature> targets = new ArrayList<>();
			if (songId < 4)
			{
				if ((weakScarlet != null) && !weakScarlet.isDead())
				{
					targets.add(weakScarlet);
				}
				if ((strongScarlet != null) && !strongScarlet.isDead())
				{
					targets.add(strongScarlet);
				}
				for (int i = 0; i < 4; i++)
				{
					if ((portraits[i] != null) && !portraits[i].isDead())
					{
						targets.add(portraits[i]);
					}
					if ((demons[i] != null) && !demons[i].isDead())
					{
						targets.add(demons[i]);
					}
				}
			}
			else
			{
				for (Player pc : getPlayers())
				{
					if (!pc.isDead())
					{
						targets.add(pc);
					}
				}
			}
			return targets;
		}
		
		/**
		 * Method getSong.
		 * @return int
		 */
		private int getSong()
		{
			if (minionsNeedHeal())
			{
				return 1;
			}
			return Rnd.get(2, 4);
		}
		
		/**
		 * Method minionsNeedHeal.
		 * @return boolean
		 */
		private boolean minionsNeedHeal()
		{
			if (!Rnd.chance(40))
			{
				return false;
			}
			if ((weakScarlet != null) && !weakScarlet.isAlikeDead() && (weakScarlet.getCurrentHp() < ((weakScarlet.getMaxHp() * 2) / 3)))
			{
				return true;
			}
			if ((strongScarlet != null) && !strongScarlet.isAlikeDead() && (strongScarlet.getCurrentHp() < ((strongScarlet.getMaxHp() * 2) / 3)))
			{
				return true;
			}
			for (int i = 0; i < 4; i++)
			{
				if ((portraits[i] != null) && !portraits[i].isDead() && (portraits[i].getCurrentHp() < (portraits[i].getMaxHp() / 3)))
				{
					return true;
				}
				if ((demons[i] != null) && !demons[i].isDead() && (demons[i].getCurrentHp() < (demons[i].getMaxHp() / 3)))
				{
					return true;
				}
			}
			return false;
		}
	}
	
	/**
	 * @author Mobius
	 */
	private class SongEffectLaunched extends RunnableImpl
	{
		/**
		 * Field _targets.
		 */
		private final List<Creature> _targets;
		/**
		 * Field _currentTime.
		 */
		/**
		 * Field _song.
		 */
		private final int _song, _currentTime;
		
		/**
		 * Constructor for SongEffectLaunched.
		 * @param targets List<Creature>
		 * @param song int
		 * @param currentTimeOfSong int
		 */
		public SongEffectLaunched(List<Creature> targets, int song, int currentTimeOfSong)
		{
			_targets = targets;
			_song = song;
			_currentTime = currentTimeOfSong;
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			if (frintezza == null)
			{
				return;
			}
			if (_currentTime > _intervalOfFrintezzaSongs)
			{
				return;
			}
			SongEffectLaunched songLaunched = new SongEffectLaunched(_targets, _song, _currentTime + (_intervalOfFrintezzaSongs / 10));
			ThreadPoolManager.getInstance().schedule(songLaunched, _intervalOfFrintezzaSongs / 10);
			frintezza.callSkill(SkillTable.getInstance().getInfo(5008, _song), _targets, false);
		}
	}
	
	/**
	 * Method spawn.
	 * @param loc NpcLocation
	 * @return NpcInstance
	 */
	NpcInstance spawn(NpcLocation loc)
	{
		return addSpawnWithoutRespawn(loc.npcId, loc, 0);
	}
	
	/**
	 * Method showSocialActionMovie.
	 * @param target NpcInstance
	 * @param dist int
	 * @param yaw int
	 * @param pitch int
	 * @param time int
	 * @param duration int
	 * @param socialAction int
	 */
	void showSocialActionMovie(NpcInstance target, int dist, int yaw, int pitch, int time, int duration, int socialAction)
	{
		if (target == null)
		{
			return;
		}
		for (Player pc : getPlayers())
		{
			if (pc.getDistance(target) <= 2550)
			{
				pc.enterMovieMode();
				pc.specialCamera(target, dist, yaw, pitch, time, duration);
			}
			else
			{
				pc.leaveMovieMode();
			}
		}
		if ((socialAction > 0) && (socialAction < 5))
		{
			target.broadcastPacket(new SocialAction(target.getObjectId(), socialAction));
		}
	}
	
	/**
	 * Method blockAll.
	 * @param flag boolean
	 */
	void blockAll(boolean flag)
	{
		block(frintezza, flag);
		block(weakScarlet, flag);
		block(strongScarlet, flag);
		for (int i = 0; i < 4; i++)
		{
			block(portraits[i], flag);
			block(demons[i], flag);
		}
	}
	
	/**
	 * Method block.
	 * @param npc NpcInstance
	 * @param flag boolean
	 */
	void block(NpcInstance npc, boolean flag)
	{
		if ((npc == null) || npc.isDead())
		{
			return;
		}
		if (flag)
		{
			npc.abortAttack(true, false);
			npc.abortCast(true, true);
			npc.setTarget(null);
			if (npc.isMoving)
			{
				npc.stopMove();
			}
			npc.block();
		}
		else
		{
			npc.unblock();
		}
		npc.setIsInvul(flag);
	}
	
	/**
	 * @author Mobius
	 */
	private class SecondMorph extends RunnableImpl
	{
		/**
		 * Field _taskId.
		 */
		private int _taskId = 0;
		
		/**
		 * Constructor for SecondMorph.
		 * @param taskId int
		 */
		public SecondMorph(int taskId)
		{
			_taskId = taskId;
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			try
			{
				switch (_taskId)
				{
					case 1:
						int angle = Math.abs((weakScarlet.getHeading() < 32768 ? 180 : 540) - (int) (weakScarlet.getHeading() / 182.044444444));
						for (Player pc : getPlayers())
						{
							pc.enterMovieMode();
						}
						blockAll(true);
						showSocialActionMovie(weakScarlet, 500, angle, 5, 500, 15000, 0);
						ThreadPoolManager.getInstance().schedule(new SecondMorph(2), 2000);
						break;
					case 2:
						weakScarlet.broadcastPacket(new SocialAction(weakScarlet.getObjectId(), 1));
						weakScarlet.setCurrentHp((weakScarlet.getMaxHp() * 3) / 4, false);
						weakScarlet.setRHandId(_frintezzasSwordId);
						weakScarlet.broadcastCharInfo();
						ThreadPoolManager.getInstance().schedule(new SecondMorph(3), 5500);
						break;
					case 3:
						weakScarlet.broadcastPacket(new SocialAction(weakScarlet.getObjectId(), 4));
						blockAll(false);
						Skill skill = SkillTable.getInstance().getInfo(5017, 1);
						skill.getEffects(weakScarlet, weakScarlet, false, false);
						for (Player pc : getPlayers())
						{
							pc.leaveMovieMode();
						}
						break;
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * @author Mobius
	 */
	private class ThirdMorph extends RunnableImpl
	{
		/**
		 * Field _taskId.
		 */
		private int _taskId = 0;
		/**
		 * Field _angle.
		 */
		private int _angle = 0;
		
		/**
		 * Constructor for ThirdMorph.
		 * @param taskId int
		 */
		public ThirdMorph(int taskId)
		{
			_taskId = taskId;
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			try
			{
				switch (_taskId)
				{
					case 1:
						_angle = Math.abs((weakScarlet.getHeading() < 32768 ? 180 : 540) - (int) (weakScarlet.getHeading() / 182.044444444));
						for (Player pc : getPlayers())
						{
							pc.enterMovieMode();
						}
						blockAll(true);
						frintezza.broadcastPacket(new MagicSkillCanceled(frintezza.getObjectId()));
						frintezza.broadcastPacket(new SocialAction(frintezza.getObjectId(), 4));
						ThreadPoolManager.getInstance().schedule(new ThirdMorph(2), 100);
						break;
					case 2:
						showSocialActionMovie(frintezza, 250, 120, 15, 0, 1000, 0);
						showSocialActionMovie(frintezza, 250, 120, 15, 0, 10000, 0);
						ThreadPoolManager.getInstance().schedule(new ThirdMorph(3), 6500);
						break;
					case 3:
						frintezza.broadcastPacket(new MagicSkillUse(frintezza, frintezza, 5006, 1, 34000, 0));
						showSocialActionMovie(frintezza, 500, 70, 15, 3000, 10000, 0);
						ThreadPoolManager.getInstance().schedule(new ThirdMorph(4), 3000);
						break;
					case 4:
						showSocialActionMovie(frintezza, 2500, 90, 12, 6000, 10000, 0);
						ThreadPoolManager.getInstance().schedule(new ThirdMorph(5), 3000);
						break;
					case 5:
						showSocialActionMovie(weakScarlet, 250, _angle, 12, 0, 1000, 0);
						showSocialActionMovie(weakScarlet, 250, _angle, 12, 0, 10000, 0);
						ThreadPoolManager.getInstance().schedule(new ThirdMorph(6), 500);
						break;
					case 6:
						weakScarlet.doDie(weakScarlet);
						showSocialActionMovie(weakScarlet, 450, _angle, 14, 8000, 8000, 0);
						ThreadPoolManager.getInstance().schedule(new ThirdMorph(7), 6250);
						break;
					case 7:
						NpcLocation loc = new NpcLocation();
						loc.set(weakScarlet.getLoc());
						loc.npcId = _strongScarletId;
						weakScarlet.deleteMe();
						weakScarlet = null;
						strongScarlet = spawn(loc);
						strongScarlet.addListener(_deathListener);
						block(strongScarlet, true);
						showSocialActionMovie(strongScarlet, 450, _angle, 12, 500, 14000, 2);
						ThreadPoolManager.getInstance().schedule(new ThirdMorph(9), 5000);
						break;
					case 9:
						blockAll(false);
						for (Player pc : getPlayers())
						{
							pc.leaveMovieMode();
						}
						Skill skill = SkillTable.getInstance().getInfo(5017, 1);
						skill.getEffects(strongScarlet, strongScarlet, false, false);
						break;
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * @author Mobius
	 */
	private class Die extends RunnableImpl
	{
		/**
		 * Field _taskId.
		 */
		private int _taskId = 0;
		
		/**
		 * Constructor for Die.
		 * @param taskId int
		 */
		public Die(int taskId)
		{
			_taskId = taskId;
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			try
			{
				switch (_taskId)
				{
					case 1:
						blockAll(true);
						int _angle = Math.abs((strongScarlet.getHeading() < 32768 ? 180 : 540) - (int) (strongScarlet.getHeading() / 182.044444444));
						showSocialActionMovie(strongScarlet, 300, _angle - 180, 5, 0, 7000, 0);
						showSocialActionMovie(strongScarlet, 200, _angle, 85, 4000, 10000, 0);
						ThreadPoolManager.getInstance().schedule(new Die(2), 7500);
						break;
					case 2:
						showSocialActionMovie(frintezza, 100, 120, 5, 0, 7000, 0);
						showSocialActionMovie(frintezza, 100, 90, 5, 5000, 15000, 0);
						ThreadPoolManager.getInstance().schedule(new Die(3), 6000);
						break;
					case 3:
						showSocialActionMovie(frintezza, 900, 90, 25, 7000, 10000, 0);
						frintezza.doDie(frintezza);
						frintezza = null;
						ThreadPoolManager.getInstance().schedule(new Die(4), 7000);
						break;
					case 4:
						for (Player pc : getPlayers())
						{
							pc.leaveMovieMode();
						}
						cleanUp();
						break;
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Method cleanUp.
	 */
	void cleanUp()
	{
		startCollapseTimer(15 * 60 * 1000L);
		for (Player p : getPlayers())
		{
			p.sendPacket(new SystemMessage(SystemMessage.THIS_DUNGEON_WILL_EXPIRE_IN_S1_MINUTES).addNumber(15));
		}
		for (NpcInstance n : getNpcs())
		{
			n.deleteMe();
		}
	}
	
	/**
	 * Method blockUnblockNpcs.
	 * @param block boolean
	 * @param npcArray int[]
	 */
	void blockUnblockNpcs(boolean block, int[] npcArray)
	{
		for (NpcInstance n : getNpcs())
		{
			if (ArrayUtils.contains(npcArray, n.getNpcId()))
			{
				if (block)
				{
					n.block();
					n.setIsInvul(true);
				}
				else
				{
					n.unblock();
					n.setIsInvul(false);
				}
			}
		}
	}
	
	/**
	 * @author Mobius
	 */
	public class CurrentHpListener implements OnCurrentHpDamageListener
	{
		/**
		 * Method onCurrentHpDamage.
		 * @param actor Creature
		 * @param damage double
		 * @param attacker Creature
		 * @param skill Skill
		 * @see lineage2.gameserver.listener.actor.OnCurrentHpDamageListener#onCurrentHpDamage(Creature, double, Creature, Skill)
		 */
		@Override
		public void onCurrentHpDamage(Creature actor, double damage, Creature attacker, Skill skill)
		{
			if (actor.isDead() || (actor != weakScarlet))
			{
				return;
			}
			double newHp = actor.getCurrentHp() - damage;
			double maxHp = actor.getMaxHp();
			switch (_scarletMorph)
			{
				case 1:
					if (newHp < (0.75 * maxHp))
					{
						_scarletMorph = 2;
						ThreadPoolManager.getInstance().schedule(new SecondMorph(1), 1100);
					}
					break;
				case 2:
					if (newHp < (0.1 * maxHp))
					{
						_scarletMorph = 3;
						ThreadPoolManager.getInstance().schedule(new ThirdMorph(1), 2000);
					}
					break;
			}
		}
	}
	
	/**
	 * @author Mobius
	 */
	private class DeathListener implements OnDeathListener
	{
		/**
		 * Constructor for DeathListener.
		 */
		public DeathListener()
		{
			// TODO Auto-generated constructor stub
		}
		
		/**
		 * Method onDeath.
		 * @param self Creature
		 * @param killer Creature
		 * @see lineage2.gameserver.listener.actor.OnDeathListener#onDeath(Creature, Creature)
		 */
		@Override
		public void onDeath(Creature self, Creature killer)
		{
			if (self.isNpc())
			{
				if (self.getNpcId() == HallAlarmDevice)
				{
					for (int hallADoor : hallADoors)
					{
						openDoor(hallADoor);
					}
					blockUnblockNpcs(false, blockANpcs);
					for (NpcInstance n : getNpcs())
					{
						if (ArrayUtils.contains(blockANpcs, n.getNpcId()))
						{
							n.getAI().notifyEvent(CtrlEvent.EVT_AGGRESSION, getPlayers().get(Rnd.get(getPlayers().size())), 200);
						}
					}
				}
				else if (ArrayUtils.contains(blockANpcs, self.getNpcId()))
				{
					for (NpcInstance n : getNpcs())
					{
						if (ArrayUtils.contains(blockANpcs, n.getNpcId()) && !n.isDead())
						{
							return;
						}
					}
					for (int corridorADoor : corridorADoors)
					{
						openDoor(corridorADoor);
					}
					blockUnblockNpcs(true, blockBNpcs);
				}
				else if (self.getNpcId() == DarkChoirPlayer)
				{
					for (NpcInstance n : getNpcs())
					{
						if ((n.getNpcId() == DarkChoirPlayer) && !n.isDead())
						{
							return;
						}
					}
					for (int hallBDoor : hallBDoors)
					{
						openDoor(hallBDoor);
					}
					blockUnblockNpcs(false, blockBNpcs);
				}
				else if (ArrayUtils.contains(blockBNpcs, self.getNpcId()))
				{
					if (Rnd.chance(10))
					{
						((NpcInstance) self).dropItem(killer.getPlayer(), DewdropItem, 1);
					}
					for (NpcInstance n : getNpcs())
					{
						if ((ArrayUtils.contains(blockBNpcs, n.getNpcId()) || ArrayUtils.contains(blockANpcs, n.getNpcId())) && !n.isDead())
						{
							return;
						}
					}
					for (int corridorBDoor : corridorBDoors)
					{
						openDoor(corridorBDoor);
					}
					ThreadPoolManager.getInstance().schedule(new FrintezzaStart(), battleStartDelay);
				}
				else if (self.getNpcId() == _weakScarletId)
				{
					self.decayMe();
					return;
				}
				else if (self.getNpcId() == _strongScarletId)
				{
					ThreadPoolManager.getInstance().schedule(new Die(1), 10);
					setReenterTime(System.currentTimeMillis());
				}
			}
		}
	}
	
	/**
	 * @author Mobius
	 */
	public class ZoneListener implements OnZoneEnterLeaveListener
	{
		/**
		 * Method onZoneEnter.
		 * @param zone Zone
		 * @param cha Creature
		 * @see lineage2.gameserver.listener.zone.OnZoneEnterLeaveListener#onZoneEnter(Zone, Creature)
		 */
		@Override
		public void onZoneEnter(Zone zone, Creature cha)
		{
		}
		
		/**
		 * Method onZoneLeave.
		 * @param zone Zone
		 * @param cha Creature
		 * @see lineage2.gameserver.listener.zone.OnZoneEnterLeaveListener#onZoneLeave(Zone, Creature)
		 */
		@Override
		public void onZoneLeave(Zone zone, Creature cha)
		{
			if (cha.isNpc() && ((cha.getNpcId() == _weakScarletId) || (cha.getNpcId() == _strongScarletId)))
			{
				cha.teleToLocation(new Location(-87784, -153304, -9176));
				((NpcInstance) cha).getAggroList().clear(true);
				cha.setCurrentHpMp(cha.getMaxHp(), cha.getMaxMp());
				cha.broadcastCharInfo();
			}
		}
	}
	
	/**
	 * Method onCollapse.
	 */
	@Override
	protected void onCollapse()
	{
		super.onCollapse();
		if (musicTask != null)
		{
			musicTask.cancel(true);
		}
	}
}
