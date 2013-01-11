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
package lineage2.gameserver.database;

import java.sql.Connection;
import java.sql.SQLException;

import lineage2.commons.dbcp.BasicDataSource;
import lineage2.gameserver.Config;

public class DatabaseFactory extends BasicDataSource
{
	private static DatabaseFactory _instance = new DatabaseFactory();
	
	public static DatabaseFactory getInstance()
	{
		return _instance;
	}
	
	public DatabaseFactory()
	{
		super(Config.DATABASE_DRIVER, Config.DATABASE_URL, Config.DATABASE_LOGIN, Config.DATABASE_PASSWORD, Config.DATABASE_MAX_CONNECTIONS, Config.DATABASE_MAX_CONNECTIONS, Config.DATABASE_MAX_IDLE_TIMEOUT, Config.DATABASE_IDLE_TEST_PERIOD, false);
	}
	
	public DatabaseFactory(String driver, String url, String login, String pass, int maxconn, int maxIdle, int idleTime, int idleTest, boolean prepared)
	{
		super(driver, url, login, pass, maxconn, maxIdle, idleTime, idleTest, prepared);
	}
	
	@Override
	public Connection getConnection() throws SQLException
	{
		return getConnection(null);
	}
}
