package com.bxy.salestrategies.db;


import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.log4j.Logger;


public class DBHelper {
	private static final Logger logger = Logger.getLogger(DBHelper.class);

	public static Properties dbProperties = new Properties();
	static{
	    try {
	        Class.forName("com.mysql.jdbc.Driver");
	    } catch (ClassNotFoundException e) {
	        System.out.println("Where is your MySQL JDBC Driver?");
	        e.printStackTrace();
	    }
		InputStream inputStream = DBHelper.class.getResourceAsStream("/dbconfig.properties");
		try {
			dbProperties.load(inputStream);
			System.out.println("userName:"+dbProperties.getProperty("db.username"));
		} catch (IOException e) {
			logger.error("failed to load properties files"+e);
			e.printStackTrace();
		}
	}
	public static Connection getConnection() throws SQLException {
	    Connection conn = null;
	    Properties connectionProps = new Properties();
	    
	    connectionProps.put("user", dbProperties.getProperty("db.username"));
	    connectionProps.put("password", dbProperties.getProperty("db.password"));
	    conn = DriverManager.getConnection(
                "jdbc:" + "mysql" + "://" +
                		dbProperties.getProperty("db.host") +
                ":" + 3306 + "/"+dbProperties.getProperty("db.name")+"?useUnicode=true&characterEncoding=utf-8",
                connectionProps);
	
	   logger.debug("Connected to database");
	    return conn;
	}
	
	  public static void closeConnection(Connection connArg) {
		    try {
		      if (connArg != null) {
		        connArg.close();
		        connArg = null;
		      }
		    } catch (SQLException sqle) {
		      printSQLException(sqle);
		    }
		  }
	
	  
	  public static void printSQLException(SQLException ex) {
		    for (Throwable e : ex) {
		      if (e instanceof SQLException) {
		        if (ignoreSQLException(((SQLException)e).getSQLState()) == false) {
		          e.printStackTrace(System.err);
		          System.err.println("SQLState: " + ((SQLException)e).getSQLState());
		          System.err.println("Error Code: " + ((SQLException)e).getErrorCode());
		          System.err.println("Message: " + e.getMessage());
		          Throwable t = ex.getCause();
		          while (t != null) {
		            System.out.println("Cause: " + t);
		            t = t.getCause();
		          }
		        }
		      }
		    }
		  }
	  
	  public static boolean ignoreSQLException(String sqlState) {
		    if (sqlState == null) {
		      System.out.println("The SQL state is not defined!");
		      return false;
		    }
		    // X0Y32: Jar file already exists in schema
		    if (sqlState.equalsIgnoreCase("X0Y32"))
		      return true;
		    // 42Y55: Table already exists in schema
		    if (sqlState.equalsIgnoreCase("42Y55"))
		      return true;
		    return false;
		  }
}
