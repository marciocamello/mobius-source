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
package lineage2.commons.dbcp;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.apache.commons.dbcp.ConnectionFactory;
import org.apache.commons.dbcp.DriverManagerConnectionFactory;
import org.apache.commons.dbcp.PoolableConnectionFactory;
import org.apache.commons.dbcp.PoolingDataSource;
import org.apache.commons.pool.ObjectPool;
import org.apache.commons.pool.impl.GenericKeyedObjectPool;
import org.apache.commons.pool.impl.GenericKeyedObjectPoolFactory;
import org.apache.commons.pool.impl.GenericObjectPool;

public class BasicDataSource implements DataSource
{
	private final PoolingDataSource _source;
	private final ObjectPool<?> _connectionPool;
	
	public BasicDataSource(String driver, String connectURI, String uname, String passwd, int maxActive, int maxIdle, int idleTimeOut, int idleTestPeriod, boolean poolPreparedStatements)
	{
		GenericObjectPool<?> connectionPool = new GenericObjectPool<>(null);
		connectionPool.setMaxActive(maxActive);
		connectionPool.setMaxIdle(maxIdle);
		connectionPool.setMinIdle(1);
		connectionPool.setMaxWait(-1L);
		connectionPool.setWhenExhaustedAction(GenericObjectPool.WHEN_EXHAUSTED_GROW);
		connectionPool.setTestOnBorrow(false);
		connectionPool.setTestWhileIdle(true);
		connectionPool.setTimeBetweenEvictionRunsMillis(idleTestPeriod * 1000L);
		connectionPool.setNumTestsPerEvictionRun(maxActive);
		connectionPool.setMinEvictableIdleTimeMillis(idleTimeOut * 1000L);
		GenericKeyedObjectPoolFactory<?, ?> statementPoolFactory = null;
		if (poolPreparedStatements)
		{
			statementPoolFactory = new GenericKeyedObjectPoolFactory<>(null, -1, GenericObjectPool.WHEN_EXHAUSTED_FAIL, 0L, 1, GenericKeyedObjectPool.DEFAULT_MAX_TOTAL);
		}
		Properties connectionProperties = new Properties();
		connectionProperties.put("user", uname);
		connectionProperties.put("password", passwd);
		ConnectionFactory connectionFactory = new DriverManagerConnectionFactory(connectURI, connectionProperties);
		@SuppressWarnings("unused")
		PoolableConnectionFactory poolableConnectionFactory = new PoolableConnectionFactory(connectionFactory, connectionPool, statementPoolFactory, "SELECT 1", false, true);
		PoolingDataSource dataSource = new PoolingDataSource(connectionPool);
		_connectionPool = connectionPool;
		_source = dataSource;
	}
	
	public Connection getConnection(Connection con) throws SQLException
	{
		return (con == null) || con.isClosed() ? _source.getConnection() : con;
	}
	
	public int getBusyConnectionCount()
	{
		return _connectionPool.getNumActive();
	}
	
	public int getIdleConnectionCount()
	{
		return _connectionPool.getNumIdle();
	}
	
	public void shutdown() throws Exception
	{
		_connectionPool.close();
	}
	
	@Override
	public PrintWriter getLogWriter()
	{
		return _source.getLogWriter();
	}
	
	@Override
	public void setLogWriter(PrintWriter out)
	{
		_source.setLogWriter(out);
	}
	
	@Override
	public void setLoginTimeout(int seconds)
	{
		throw new UnsupportedOperationException();
	}
	
	@Override
	public int getLoginTimeout()
	{
		throw new UnsupportedOperationException();
	}
	
	@Override
	public Logger getParentLogger()
	{
		throw new UnsupportedOperationException();
	}
	
	@Override
	public <T> T unwrap(Class<T> iface)
	{
		throw new UnsupportedOperationException();
	}
	
	@Override
	public boolean isWrapperFor(Class<?> iface)
	{
		return false;
	}
	
	@Override
	public Connection getConnection() throws SQLException
	{
		return _source.getConnection();
	}
	
	@Override
	public Connection getConnection(String username, String password)
	{
		throw new UnsupportedOperationException();
	}
}
