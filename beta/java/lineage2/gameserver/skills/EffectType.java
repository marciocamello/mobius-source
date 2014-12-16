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
package lineage2.gameserver.skills;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import lineage2.gameserver.model.Effect;
import lineage2.gameserver.skills.effects.EffectAddSkills;
import lineage2.gameserver.skills.effects.EffectAgathionRes;
import lineage2.gameserver.skills.effects.EffectAggression;
import lineage2.gameserver.skills.effects.EffectBetray;
import lineage2.gameserver.skills.effects.EffectBlessNoblesse;
import lineage2.gameserver.skills.effects.EffectBlockStat;
import lineage2.gameserver.skills.effects.EffectBlockTarget;
import lineage2.gameserver.skills.effects.EffectBluff;
import lineage2.gameserver.skills.effects.EffectBuff;
import lineage2.gameserver.skills.effects.EffectCPDamPercent;
import lineage2.gameserver.skills.effects.EffectCallSkills;
import lineage2.gameserver.skills.effects.EffectCannotTarget;
import lineage2.gameserver.skills.effects.EffectCharge;
import lineage2.gameserver.skills.effects.EffectChargesOverTime;
import lineage2.gameserver.skills.effects.EffectCharmOfCourage;
import lineage2.gameserver.skills.effects.EffectCombatPointHealOverTime;
import lineage2.gameserver.skills.effects.EffectConsumeSoulsOverTime;
import lineage2.gameserver.skills.effects.EffectCubic;
import lineage2.gameserver.skills.effects.EffectCurseOfLifeFlow;
import lineage2.gameserver.skills.effects.EffectDamOverTime;
import lineage2.gameserver.skills.effects.EffectDamOverTimeLethal;
import lineage2.gameserver.skills.effects.EffectDamageHealMpToEffector;
import lineage2.gameserver.skills.effects.EffectDamageHealToEffector;
import lineage2.gameserver.skills.effects.EffectDamageHealToEffectorAndPets;
import lineage2.gameserver.skills.effects.EffectDeathImmunity;
import lineage2.gameserver.skills.effects.EffectDebuffImmunity;
import lineage2.gameserver.skills.effects.EffectDestroySummon;
import lineage2.gameserver.skills.effects.EffectDisarm;
import lineage2.gameserver.skills.effects.EffectDiscord;
import lineage2.gameserver.skills.effects.EffectDispelEffects;
import lineage2.gameserver.skills.effects.EffectDispelOnHIt;
import lineage2.gameserver.skills.effects.EffectEnervation;
import lineage2.gameserver.skills.effects.EffectFakeDeath;
import lineage2.gameserver.skills.effects.EffectFear;
import lineage2.gameserver.skills.effects.EffectGiantForceAura;
import lineage2.gameserver.skills.effects.EffectGrow;
import lineage2.gameserver.skills.effects.EffectHPDamPercent;
import lineage2.gameserver.skills.effects.EffectHate;
import lineage2.gameserver.skills.effects.EffectHeal;
import lineage2.gameserver.skills.effects.EffectHealAndDamage;
import lineage2.gameserver.skills.effects.EffectHealBlock;
import lineage2.gameserver.skills.effects.EffectHealCPPercent;
import lineage2.gameserver.skills.effects.EffectHealHPCP;
import lineage2.gameserver.skills.effects.EffectHealOverTime;
import lineage2.gameserver.skills.effects.EffectHealPercent;
import lineage2.gameserver.skills.effects.EffectHellBinding;
import lineage2.gameserver.skills.effects.EffectHourglass;
import lineage2.gameserver.skills.effects.EffectHpToOne;
import lineage2.gameserver.skills.effects.EffectImmobilize;
import lineage2.gameserver.skills.effects.EffectIncreaseChargesOverTime;
import lineage2.gameserver.skills.effects.EffectInterrupt;
import lineage2.gameserver.skills.effects.EffectInvisible;
import lineage2.gameserver.skills.effects.EffectInvulnerable;
import lineage2.gameserver.skills.effects.EffectKnockBack;
import lineage2.gameserver.skills.effects.EffectKnockDown;
import lineage2.gameserver.skills.effects.EffectLDManaDamOverTime;
import lineage2.gameserver.skills.effects.EffectLockInventory;
import lineage2.gameserver.skills.effects.EffectMDamOverTime;
import lineage2.gameserver.skills.effects.EffectMPDamPercent;
import lineage2.gameserver.skills.effects.EffectManaDamOverTime;
import lineage2.gameserver.skills.effects.EffectManaHeal;
import lineage2.gameserver.skills.effects.EffectManaHealOverTime;
import lineage2.gameserver.skills.effects.EffectManaHealPercent;
import lineage2.gameserver.skills.effects.EffectMeditation;
import lineage2.gameserver.skills.effects.EffectMute;
import lineage2.gameserver.skills.effects.EffectMuteAll;
import lineage2.gameserver.skills.effects.EffectMuteAttack;
import lineage2.gameserver.skills.effects.EffectMutePhisycal;
import lineage2.gameserver.skills.effects.EffectNegateEffects;
import lineage2.gameserver.skills.effects.EffectNegateMusic;
import lineage2.gameserver.skills.effects.EffectParalyze;
import lineage2.gameserver.skills.effects.EffectPetrification;
import lineage2.gameserver.skills.effects.EffectRandomHate;
import lineage2.gameserver.skills.effects.EffectRelax;
import lineage2.gameserver.skills.effects.EffectRemoveTarget;
import lineage2.gameserver.skills.effects.EffectRestoration;
import lineage2.gameserver.skills.effects.EffectRestorationRandom;
import lineage2.gameserver.skills.effects.EffectRoot;
import lineage2.gameserver.skills.effects.EffectSalvation;
import lineage2.gameserver.skills.effects.EffectSantaHuntBlessing;
import lineage2.gameserver.skills.effects.EffectServitorShare;
import lineage2.gameserver.skills.effects.EffectShadowStep;
import lineage2.gameserver.skills.effects.EffectSilentMove;
import lineage2.gameserver.skills.effects.EffectSleep;
import lineage2.gameserver.skills.effects.EffectStun;
import lineage2.gameserver.skills.effects.EffectSummonSkill;
import lineage2.gameserver.skills.effects.EffectSymbol;
import lineage2.gameserver.skills.effects.EffectTalismanOfPower;
import lineage2.gameserver.skills.effects.EffectTargetToMe;
import lineage2.gameserver.skills.effects.EffectTargetToOwner;
import lineage2.gameserver.skills.effects.EffectTemplate;
import lineage2.gameserver.skills.effects.EffectTransformation;
import lineage2.gameserver.skills.effects.EffectTurner;
import lineage2.gameserver.skills.effects.EffectUnAggro;
import lineage2.gameserver.stats.Env;
import lineage2.gameserver.stats.Stats;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public enum EffectType
{
	AddSkills(EffectAddSkills.class, null, false),
	AgathionResurrect(EffectAgathionRes.class, null, true),
	Aggression(EffectAggression.class, null, true),
	Betray(EffectBetray.class, null, Stats.MENTAL_RESIST, Stats.MENTAL_POWER, true),
	BlessNoblesse(EffectBlessNoblesse.class, null, true),
	BlockStat(EffectBlockStat.class, null, true),
	BlockTarget(EffectBlockTarget.class, null, true),
	Buff(EffectBuff.class, null, false),
	Bluff(EffectBluff.class, AbnormalEffect.NULL, Stats.MENTAL_RESIST, Stats.MENTAL_POWER, true),
	DebuffImmunity(EffectDebuffImmunity.class, null, true),
	DispelEffects(EffectDispelEffects.class, null, Stats.CANCEL_RESIST, Stats.CANCEL_POWER, true),
	CallSkills(EffectCallSkills.class, null, false),
	CombatPointHealOverTime(EffectCombatPointHealOverTime.class, null, true),
	ConsumeSoulsOverTime(EffectConsumeSoulsOverTime.class, null, true),
	Charge(EffectCharge.class, null, false),
	CharmOfCourage(EffectCharmOfCourage.class, null, true),
	CPDamPercent(EffectCPDamPercent.class, null, true),
	Cubic(EffectCubic.class, null, true),
	DamageHealMpToEffector(EffectDamageHealMpToEffector.class, null, false),
	DamageHealToEffector(EffectDamageHealToEffector.class, null, false),
	DamageHealToEffectorAndPets(EffectDamageHealToEffectorAndPets.class, null, false),
	DamOverTime(EffectDamOverTime.class, null, false),
	DamOverTimeLethal(EffectDamOverTimeLethal.class, null, false),
	DeathImmunity(EffectDeathImmunity.class, null, false),
	DestroySummon(EffectDestroySummon.class, null, Stats.MENTAL_RESIST, Stats.MENTAL_POWER, true),
	Disarm(EffectDisarm.class, null, Stats.DISARM_RESIST, Stats.DISARM_POWER, true),
	Discord(EffectDiscord.class, AbnormalEffect.WIND, Stats.MENTAL_RESIST, Stats.MENTAL_POWER, true),
	Enervation(EffectEnervation.class, null, Stats.MENTAL_RESIST, Stats.MENTAL_POWER, false),
	FakeDeath(EffectFakeDeath.class, null, true),
	Fear(EffectFear.class, AbnormalEffect.FEAR, Stats.MENTAL_RESIST, Stats.MENTAL_POWER, true),
	Grow(EffectGrow.class, AbnormalEffect.GROW, false),
	GiantForceAura(EffectGiantForceAura.class, null, false),
	Hate(EffectHate.class, null, false),
	Heal(EffectHeal.class, null, false),
	HealAndDamage(EffectHealAndDamage.class, null, false),
	HealBlock(EffectHealBlock.class, null, true),
	HealCPPercent(EffectHealCPPercent.class, null, true),
	HealHPCP(EffectHealHPCP.class, null, true),
	HealOverTime(EffectHealOverTime.class, null, false),
	HealPercent(EffectHealPercent.class, null, false),
	HellBinding(EffectHellBinding.class, AbnormalEffect.S_HELLBINDING, true),
	HPDamPercent(EffectHPDamPercent.class, null, true),
	HpToOne(EffectHpToOne.class, null, true),
	IncreaseChargesOverTime(EffectIncreaseChargesOverTime.class, null, true),
	IgnoreSkill(EffectBuff.class, null, false),
	Immobilize(EffectImmobilize.class, null, true),
	Interrupt(EffectInterrupt.class, null, true),
	Invulnerable(EffectInvulnerable.class, AbnormalEffect.INVULNERABLE, false),
	Invisible(EffectInvisible.class, AbnormalEffect.STEALTH, false),
	LockInventory(EffectLockInventory.class, null, false),
	CurseOfLifeFlow(EffectCurseOfLifeFlow.class, null, true),
	LDManaDamOverTime(EffectLDManaDamOverTime.class, null, true),
	ManaDamOverTime(EffectManaDamOverTime.class, null, true),
	ManaHeal(EffectManaHeal.class, null, false),
	ManaHealOverTime(EffectManaHealOverTime.class, null, false),
	ManaHealPercent(EffectManaHealPercent.class, null, false),
	MDamOverTime(EffectMDamOverTime.class, null, false),
	Meditation(EffectMeditation.class, null, false),
	MPDamPercent(EffectMPDamPercent.class, null, true),
	Mute(EffectMute.class, AbnormalEffect.MUTED, Stats.MENTAL_RESIST, Stats.MENTAL_POWER, true),
	MuteAll(EffectMuteAll.class, AbnormalEffect.MUTED, Stats.MENTAL_RESIST, Stats.MENTAL_POWER, true),
	MuteAttack(EffectMuteAttack.class, AbnormalEffect.MUTED, Stats.MENTAL_RESIST, Stats.MENTAL_POWER, true),
	MutePhisycal(EffectMutePhisycal.class, AbnormalEffect.MUTED, Stats.MENTAL_RESIST, Stats.MENTAL_POWER, true),
	NegateEffects(EffectNegateEffects.class, null, false),
	NegateMusic(EffectNegateMusic.class, null, false),
	Paralyze(EffectParalyze.class, AbnormalEffect.HOLD_1, Stats.PARALYZE_RESIST, Stats.PARALYZE_POWER, true),
	Petrification(EffectPetrification.class, AbnormalEffect.HOLD_2, Stats.PARALYZE_RESIST, Stats.PARALYZE_POWER, true),
	RandomHate(EffectRandomHate.class, null, true),
	Relax(EffectRelax.class, null, true),
	CannotTarget(EffectCannotTarget.class, null, true),
	RemoveTarget(EffectRemoveTarget.class, null, true),
	RestorationRandom(EffectRestorationRandom.class, null, true),
	Restoration(EffectRestoration.class, null, true),
	Root(EffectRoot.class, AbnormalEffect.ROOT, Stats.ROOT_RESIST, Stats.ROOT_POWER, true),
	Hourglass(EffectHourglass.class, null, true),
	Salvation(EffectSalvation.class, null, true),
	SantaHuntBlessingSocks(EffectSantaHuntBlessing.class, AbnormalEffect.U_SANTA_SOCKS_AVE, true),
	SantaHuntBlessingTree(EffectSantaHuntBlessing.class, AbnormalEffect.U_SANTA_TREE_AVE, true),
	SantaHuntBlessingSnowman(EffectSantaHuntBlessing.class, AbnormalEffect.U_SANTA_SNOWMAN_AVE, true),
	ServitorShare(EffectServitorShare.class, null, true),
	SilentMove(EffectSilentMove.class, AbnormalEffect.STEALTH, true),
	Sleep(EffectSleep.class, AbnormalEffect.SLEEP, Stats.SLEEP_RESIST, Stats.SLEEP_POWER, true),
	Stun(EffectStun.class, AbnormalEffect.STUN, Stats.STUN_RESIST, Stats.STUN_POWER, true),
	DispelOnHit(EffectDispelOnHIt.class, null, true),
	SummonSkill(EffectSummonSkill.class, null, true),
	Symbol(EffectSymbol.class, null, false),
	Transformation(EffectTransformation.class, null, Stats.MUTATE_RESIST, Stats.MUTATE_POWER, true),
	UnAggro(EffectUnAggro.class, null, true),
	Vitality(EffectBuff.class, AbnormalEffect.VITALITY, true),
	TalismanOfPower(EffectTalismanOfPower.class, null, false),
	TargetToMe(EffectTargetToMe.class, null, Stats.PULL_RESIST, Stats.PULL_POWER, true),
	TargetToOwner(EffectTargetToOwner.class, null, true),
	TransferDam(EffectBuff.class, null, false),
	Turner(EffectTurner.class, null, false),
	Poison(EffectDamOverTime.class, null, Stats.POISON_RESIST, Stats.POISON_POWER, false),
	PoisonLethal(EffectDamOverTimeLethal.class, null, Stats.POISON_RESIST, Stats.POISON_POWER, false),
	Bleed(EffectDamOverTime.class, AbnormalEffect.BLEEDING, Stats.BLEED_RESIST, Stats.BLEED_POWER, false),
	Debuff(EffectBuff.class, null, false),
	WatcherGaze(EffectBuff.class, null, false),
	KnockDown(EffectKnockDown.class, AbnormalEffect.S_51, Stats.KNOCKDOWN_RESIST, Stats.KNOCKDOWN_POWER, true),
	KnockBack(EffectKnockBack.class, null, Stats.KNOCKBACK_RESIST, Stats.KNOCKBACK_POWER, true),
	ShadowStep(EffectShadowStep.class, null, true),
	ChargesOverTime(EffectChargesOverTime.class, null, true),
	Mentoring(EffectBuff.class, null, false),
	AbsorbDamageToEffector(EffectBuff.class, null, false),
	AbsorbDamageToMp(EffectBuff.class, null, false),
	AbsorbDamageToSummon(EffectLDManaDamOverTime.class, null, true);
	private final Constructor<? extends Effect> _constructor;
	private final AbnormalEffect _abnormal;
	private final Stats _resistType;
	private final Stats _attributeType;
	private final boolean _isRaidImmune;
	
	/**
	 * Constructor for EffectType.
	 * @param clazz Class<? extends Effect>
	 * @param abnormal AbnormalEffect
	 * @param isRaidImmune boolean
	 */
	private EffectType(Class<? extends Effect> clazz, AbnormalEffect abnormal, boolean isRaidImmune)
	{
		this(clazz, abnormal, null, null, isRaidImmune);
	}
	
	/**
	 * Constructor for EffectType.
	 * @param clazz Class<? extends Effect>
	 * @param abnormal AbnormalEffect
	 * @param resistType Stats
	 * @param attributeType Stats
	 * @param isRaidImmune boolean
	 */
	private EffectType(Class<? extends Effect> clazz, AbnormalEffect abnormal, Stats resistType, Stats attributeType, boolean isRaidImmune)
	{
		try
		{
			_constructor = clazz.getConstructor(Env.class, EffectTemplate.class);
		}
		catch (NoSuchMethodException e)
		{
			throw new Error(e);
		}
		
		_abnormal = abnormal;
		_resistType = resistType;
		_attributeType = attributeType;
		_isRaidImmune = isRaidImmune;
	}
	
	/**
	 * Method getAbnormal.
	 * @return AbnormalEffect
	 */
	public AbnormalEffect getAbnormal()
	{
		return _abnormal;
	}
	
	/**
	 * Method getResistType.
	 * @return Stats
	 */
	public Stats getResistType()
	{
		return _resistType;
	}
	
	/**
	 * Method getAttributeType.
	 * @return Stats
	 */
	public Stats getAttributeType()
	{
		return _attributeType;
	}
	
	/**
	 * Method isRaidImmune.
	 * @return boolean
	 */
	public boolean isRaidImmune()
	{
		return _isRaidImmune;
	}
	
	/**
	 * Method makeEffect.
	 * @param env Env
	 * @param template EffectTemplate
	 * @return Effect
	 * @throws IllegalArgumentException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public Effect makeEffect(Env env, EffectTemplate template) throws IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException
	{
		return _constructor.newInstance(env, template);
	}
}
