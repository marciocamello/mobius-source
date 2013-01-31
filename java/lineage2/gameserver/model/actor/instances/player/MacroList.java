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
package lineage2.gameserver.model.actor.instances.player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import lineage2.commons.dbutils.DbUtils;
import lineage2.gameserver.database.DatabaseFactory;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.actor.instances.player.Macro.L2MacroCmd;
import lineage2.gameserver.network.serverpackets.SendMacroList;
import lineage2.gameserver.utils.Strings;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class MacroList
{
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(MacroList.class);
	/**
	 * Field player.
	 */
	private final Player player;
	/**
	 * Field _macroses.
	 */
	private final Map<Integer, Macro> _macroses = new HashMap<>();
	/**
	 * Field _revision.
	 */
	private int _revision;
	/**
	 * Field _macroId.
	 */
	private int _macroId;
	
	/**
	 * Constructor for MacroList.
	 * @param player Player
	 */
	public MacroList(Player player)
	{
		this.player = player;
		_revision = 1;
		_macroId = 1000;
	}
	
	/**
	 * Method getRevision.
	 * @return int
	 */
	public int getRevision()
	{
		return _revision;
	}
	
	/**
	 * Method getAllMacroses.
	 * @return Macro[]
	 */
	public Macro[] getAllMacroses()
	{
		return _macroses.values().toArray(new Macro[_macroses.size()]);
	}
	
	/**
	 * Method getMacro.
	 * @param id int
	 * @return Macro
	 */
	public Macro getMacro(int id)
	{
		return _macroses.get(id - 1);
	}
	
	/**
	 * Method registerMacro.
	 * @param macro Macro
	 */
	public void registerMacro(Macro macro)
	{
		if (macro.id == 0)
		{
			macro.id = _macroId++;
			while (_macroses.get(macro.id) != null)
			{
				macro.id = _macroId++;
			}
			_macroses.put(macro.id, macro);
			registerMacroInDb(macro);
		}
		else
		{
			Macro old = _macroses.put(macro.id, macro);
			if (old != null)
			{
				deleteMacroFromDb(old);
			}
			registerMacroInDb(macro);
		}
		sendUpdate();
	}
	
	/**
	 * Method deleteMacro.
	 * @param id int
	 */
	public void deleteMacro(int id)
	{
		Macro toRemove = _macroses.get(id);
		if (toRemove != null)
		{
			deleteMacroFromDb(toRemove);
		}
		_macroses.remove(id);
		sendUpdate();
	}
	
	/**
	 * Method sendUpdate.
	 */
	public void sendUpdate()
	{
		_revision++;
		Macro[] all = getAllMacroses();
		if (all.length == 0)
		{
			player.sendPacket(new SendMacroList(_revision, all.length, null));
		}
		else
		{
			for (Macro m : all)
			{
				player.sendPacket(new SendMacroList(_revision, all.length, m));
			}
		}
	}
	
	/**
	 * Method registerMacroInDb.
	 * @param macro Macro
	 */
	private void registerMacroInDb(Macro macro)
	{
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("REPLACE INTO character_macroses (char_obj_id,id,icon,name,descr,acronym,commands) values(?,?,?,?,?,?,?)");
			statement.setInt(1, player.getObjectId());
			statement.setInt(2, macro.id);
			statement.setInt(3, macro.icon);
			statement.setString(4, macro.name);
			statement.setString(5, macro.descr);
			statement.setString(6, macro.acronym);
			StringBuilder sb = new StringBuilder();
			for (L2MacroCmd cmd : macro.commands)
			{
				sb.append(cmd.type).append(',');
				sb.append(cmd.d1).append(',');
				sb.append(cmd.d2);
				if ((cmd.cmd != null) && (cmd.cmd.length() > 0))
				{
					sb.append(',').append(cmd.cmd);
				}
				sb.append(';');
			}
			statement.setString(7, sb.toString());
			statement.execute();
		}
		catch (Exception e)
		{
			_log.error("could not store macro: " + macro.toString(), e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
	}
	
	/**
	 * Method deleteMacroFromDb.
	 * @param macro Macro
	 */
	private void deleteMacroFromDb(Macro macro)
	{
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("DELETE FROM character_macroses WHERE char_obj_id=? AND id=?");
			statement.setInt(1, player.getObjectId());
			statement.setInt(2, macro.id);
			statement.execute();
		}
		catch (Exception e)
		{
			_log.error("could not delete macro:", e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
	}
	
	/**
	 * Method restore.
	 */
	public void restore()
	{
		_macroses.clear();
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rset = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("SELECT char_obj_id, id, icon, name, descr, acronym, commands FROM character_macroses WHERE char_obj_id=?");
			statement.setInt(1, player.getObjectId());
			rset = statement.executeQuery();
			while (rset.next())
			{
				int id = rset.getInt("id");
				int icon = rset.getInt("icon");
				String name = Strings.stripSlashes(rset.getString("name"));
				String descr = Strings.stripSlashes(rset.getString("descr"));
				String acronym = Strings.stripSlashes(rset.getString("acronym"));
				List<L2MacroCmd> commands = new ArrayList<>();
				StringTokenizer st1 = new StringTokenizer(rset.getString("commands"), ";");
				while (st1.hasMoreTokens())
				{
					StringTokenizer st = new StringTokenizer(st1.nextToken(), ",");
					int type = Integer.parseInt(st.nextToken());
					int d1 = Integer.parseInt(st.nextToken());
					int d2 = Integer.parseInt(st.nextToken());
					String cmd = "";
					if (st.hasMoreTokens())
					{
						cmd = st.nextToken();
					}
					L2MacroCmd mcmd = new L2MacroCmd(commands.size(), type, d1, d2, cmd);
					commands.add(mcmd);
				}
				Macro m = new Macro(id, icon, name, descr, acronym, commands.toArray(new L2MacroCmd[commands.size()]));
				_macroses.put(m.id, m);
			}
		}
		catch (Exception e)
		{
			_log.error("could not restore shortcuts:", e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rset);
		}
	}
}
