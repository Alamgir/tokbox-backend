package com.cboxgames.idonia.backend.commons;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;
import javax.servlet.ServletContext;

import com.cboxgames.stack.system.services.logging.CBoxLoggerSyslog;

public class DBSQL {

	/** Is this persistent SQL store initialized. */
	private boolean initialized;
	
	/** Connection pool to pull connections from when required. */
	private DataSource connection_pool;
	private ServletContext servlet_context;
	
	public DBSQL(DataSource connection_pool, ServletContext servlet_context) throws SQLException {
		this.connection_pool = connection_pool;
		this.servlet_context = servlet_context;
		initialized = false;
	}

    public DataSource getDataSource() {
        return connection_pool;
    }
	
    public ServletContext getServletContext() {
        return servlet_context;
    }
    
	protected void setInitialized(boolean initialized) {
		this.initialized = initialized;
	}
	
	/**
	 * Is this store initialized?
	 * 
	 * @return ...
	 */
	public boolean isInitialized() {
		return initialized;
	}
	
	/**
	 * Get a connection to use for a query.
	 * 
	 * @return
	 * @throws SQLException
	 */
	protected Connection getConnection() throws SQLException {
		Connection connection = connection_pool.getConnection();
		
		if (connection == null)
			throw new SQLException("Null connection returned by pool!");
		
		return connection;
	}
	
	/**
	 * Check if the result is null, auto increment otherwise.
	 * 
	 * @param result the ResultSet to examine.
	 * 
	 * @return weather there is a result in result.
	 * 
	 * @throws SQLException
	 */
	protected boolean checkResult(ResultSet result) throws SQLException { return result != null && result.next(); }
	
	/**
	 * Release the provided connection.
	 * 
	 * @param conn
	 */
	public void closeConnection(Connection conn) {
		if (conn != null) {
			try {
				if (!conn.isClosed())
					conn.close();
			} catch (SQLException e) {
				CBoxLoggerSyslog.logException(e, "StorePersistentSQL.closeConn");
			} finally {
				if (conn != null) {
					try {
						conn.close();
					} catch (SQLException e) {
						CBoxLoggerSyslog.logException(e, "StorePersistentSQL.closeConn");
					}
				}
			}
		}
	}

    public void rollbackConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.rollback();
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void setAutoCommitTrue(Connection conn) {
        if (conn != null) {
            try {
                conn.setAutoCommit(true);
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}