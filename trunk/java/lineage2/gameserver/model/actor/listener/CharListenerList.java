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
package lineage2.gameserver.model.actor.listener;

import lineage2.commons.listener.Listener;
import lineage2.commons.listener.ListenerList;
import lineage2.gameserver.ai.CtrlEvent;
import lineage2.gameserver.ai.CtrlIntention;
import lineage2.gameserver.listener.actor.OnAttackHitListener;
import lineage2.gameserver.listener.actor.OnAttackListener;
import lineage2.gameserver.listener.actor.OnCurrentHpDamageListener;
import lineage2.gameserver.listener.actor.OnDeathListener;
import lineage2.gameserver.listener.actor.OnKillListener;
import lineage2.gameserver.listener.actor.OnMagicHitListener;
import lineage2.gameserver.listener.actor.OnMagicUseListener;
import lineage2.gameserver.listener.actor.ai.OnAiEventListener;
import lineage2.gameserver.listener.actor.ai.OnAiIntentionListener;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Skill;

public class CharListenerList extends ListenerList<Creature>
{
	final static ListenerList<Creature> global = new ListenerList<>();
	protected final Creature actor;
	
	public CharListenerList(Creature actor)
	{
		this.actor = actor;
	}
	
	public Creature getActor()
	{
		return actor;
	}
	
	public static boolean addGlobal(Listener<Creature> listener)
	{
		return global.add(listener);
	}
	
	public static boolean removeGlobal(Listener<Creature> listener)
	{
		return global.remove(listener);
	}
	
	public void onAiIntention(CtrlIntention intention, Object arg0, Object arg1)
	{
		if (!getListeners().isEmpty())
		{
			for (Listener<Creature> listener : getListeners())
			{
				if (OnAiIntentionListener.class.isInstance(listener))
				{
					((OnAiIntentionListener) listener).onAiIntention(getActor(), intention, arg0, arg1);
				}
			}
		}
	}
	
	public void onAiEvent(CtrlEvent evt, Object[] args)
	{
		if (!getListeners().isEmpty())
		{
			for (Listener<Creature> listener : getListeners())
			{
				if (OnAiEventListener.class.isInstance(listener))
				{
					((OnAiEventListener) listener).onAiEvent(getActor(), evt, args);
				}
			}
		}
	}
	
	public void onAttack(Creature target)
	{
		if (!global.getListeners().isEmpty())
		{
			for (Listener<Creature> listener : global.getListeners())
			{
				if (OnAttackListener.class.isInstance(listener))
				{
					((OnAttackListener) listener).onAttack(getActor(), target);
				}
			}
		}
		if (!getListeners().isEmpty())
		{
			for (Listener<Creature> listener : getListeners())
			{
				if (OnAttackListener.class.isInstance(listener))
				{
					((OnAttackListener) listener).onAttack(getActor(), target);
				}
			}
		}
	}
	
	public void onAttackHit(Creature attacker)
	{
		if (!global.getListeners().isEmpty())
		{
			for (Listener<Creature> listener : global.getListeners())
			{
				if (OnAttackHitListener.class.isInstance(listener))
				{
					((OnAttackHitListener) listener).onAttackHit(getActor(), attacker);
				}
			}
		}
		if (!getListeners().isEmpty())
		{
			for (Listener<Creature> listener : getListeners())
			{
				if (OnAttackHitListener.class.isInstance(listener))
				{
					((OnAttackHitListener) listener).onAttackHit(getActor(), attacker);
				}
			}
		}
	}
	
	public void onMagicUse(Skill skill, Creature target, boolean alt)
	{
		if (!global.getListeners().isEmpty())
		{
			for (Listener<Creature> listener : global.getListeners())
			{
				if (OnMagicUseListener.class.isInstance(listener))
				{
					((OnMagicUseListener) listener).onMagicUse(getActor(), skill, target, alt);
				}
			}
		}
		if (!getListeners().isEmpty())
		{
			for (Listener<Creature> listener : getListeners())
			{
				if (OnMagicUseListener.class.isInstance(listener))
				{
					((OnMagicUseListener) listener).onMagicUse(getActor(), skill, target, alt);
				}
			}
		}
	}
	
	public void onMagicHit(Skill skill, Creature caster)
	{
		if (!global.getListeners().isEmpty())
		{
			for (Listener<Creature> listener : global.getListeners())
			{
				if (OnMagicHitListener.class.isInstance(listener))
				{
					((OnMagicHitListener) listener).onMagicHit(getActor(), skill, caster);
				}
			}
		}
		if (!getListeners().isEmpty())
		{
			for (Listener<Creature> listener : getListeners())
			{
				if (OnMagicHitListener.class.isInstance(listener))
				{
					((OnMagicHitListener) listener).onMagicHit(getActor(), skill, caster);
				}
			}
		}
	}
	
	public void onDeath(Creature killer)
	{
		if (!global.getListeners().isEmpty())
		{
			for (Listener<Creature> listener : global.getListeners())
			{
				if (OnDeathListener.class.isInstance(listener))
				{
					((OnDeathListener) listener).onDeath(getActor(), killer);
				}
			}
		}
		if (!getListeners().isEmpty())
		{
			for (Listener<Creature> listener : getListeners())
			{
				if (OnDeathListener.class.isInstance(listener))
				{
					((OnDeathListener) listener).onDeath(getActor(), killer);
				}
			}
		}
	}
	
	public void onKill(Creature victim)
	{
		if (!global.getListeners().isEmpty())
		{
			for (Listener<Creature> listener : global.getListeners())
			{
				if (OnKillListener.class.isInstance(listener) && !((OnKillListener) listener).ignorePetOrSummon())
				{
					((OnKillListener) listener).onKill(getActor(), victim);
				}
			}
		}
		if (!getListeners().isEmpty())
		{
			for (Listener<Creature> listener : getListeners())
			{
				if (OnKillListener.class.isInstance(listener) && !((OnKillListener) listener).ignorePetOrSummon())
				{
					((OnKillListener) listener).onKill(getActor(), victim);
				}
			}
		}
	}
	
	public void onKillIgnorePetOrSummon(Creature victim)
	{
		if (!global.getListeners().isEmpty())
		{
			for (Listener<Creature> listener : global.getListeners())
			{
				if (OnKillListener.class.isInstance(listener) && ((OnKillListener) listener).ignorePetOrSummon())
				{
					((OnKillListener) listener).onKill(getActor(), victim);
				}
			}
		}
		if (!getListeners().isEmpty())
		{
			for (Listener<Creature> listener : getListeners())
			{
				if (OnKillListener.class.isInstance(listener) && ((OnKillListener) listener).ignorePetOrSummon())
				{
					((OnKillListener) listener).onKill(getActor(), victim);
				}
			}
		}
	}
	
	public void onCurrentHpDamage(double damage, Creature attacker, Skill skill)
	{
		if (!global.getListeners().isEmpty())
		{
			for (Listener<Creature> listener : global.getListeners())
			{
				if (OnCurrentHpDamageListener.class.isInstance(listener))
				{
					((OnCurrentHpDamageListener) listener).onCurrentHpDamage(getActor(), damage, attacker, skill);
				}
			}
		}
		if (!getListeners().isEmpty())
		{
			for (Listener<Creature> listener : getListeners())
			{
				if (OnCurrentHpDamageListener.class.isInstance(listener))
				{
					((OnCurrentHpDamageListener) listener).onCurrentHpDamage(getActor(), damage, attacker, skill);
				}
			}
		}
	}
}
