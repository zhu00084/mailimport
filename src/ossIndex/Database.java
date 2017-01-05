package ossIndex;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Database implements IDatabase {
	
	private static final String NEXT_MAIL_INDEX = "NextMailIndex";

	private final Logger slf4jLogger = LoggerFactory.getLogger(Database.class);
	
	private String DbDriver, DbLocation, DbUserName, DbPasswd;	
	
	public Database(){}
	
	public Database(String dbDriver, String dbLocation, String dbUserName, String dbPasswd){
		setDbDriver(dbDriver);	
		setDbLocation(dbLocation);
		setDbUserName(dbUserName);
		setDbPasswd(dbPasswd);
	}
	
	@Override
	public boolean ImportMail(ArrayList<BugTraqMail> mails) {
		
		try {
			
			Connection conn = GetConnection();
			
			if (conn == null)
			{
				slf4jLogger.info("Could not create connection to database");
				return false;
			}
			
			if(!InitializeSession(conn)) return false;
			
			if(!Import(conn,mails)) return false;
				
			conn.close();
			
		} catch (Exception e) {
			slf4jLogger.info(e.toString());
		}
		
		return true;	
	
	}

	@Override
	public int GetNextMailIndex() throws OssNoConnectionException {
		Connection conn = GetConnection();

		if (conn == null) {
			slf4jLogger.info("Could not create connection to database");
			throw new OssNoConnectionException("Could not create connection to database.");
		}
		
		String value = GetOssInfo(conn, NEXT_MAIL_INDEX);
		
		int retValue = 0;
		if (value != null && !value.isEmpty())
		{
			try
			{
				retValue = Integer.parseInt(value);				
			}
			catch (NumberFormatException e)
			{
				slf4jLogger.info("Next Mail Index value {} is invalid", value);
				retValue = 0;				
			}
				
		}
		

		try {
			conn.close();
		} catch (SQLException e) {
			slf4jLogger.info("Failed to close connection.");
		}

		return retValue;

	}
	
	public boolean UpdateNextMailIndex(int nextMailIndex) throws OssNoConnectionException
	{
		Connection conn = GetConnection();

		if (conn == null) {
			slf4jLogger.info("Could not create connection to database");
			throw new OssNoConnectionException("Could not create connection to database.");
		}
		
		boolean success = UpdateOssInfo(conn, NEXT_MAIL_INDEX, Integer.toString(nextMailIndex));
		

		try {
			conn.close();
		} catch (SQLException e) {
			slf4jLogger.info("Failed to close connection.");
		}	
		return success;
	}
	
	public String getDbDriver() {
		return DbDriver;
	}

	public void setDbDriver(String dbDriver) {
		DbDriver = dbDriver;
	}
	
	public String getDbLocation(){
		return DbLocation;
	}
	
	public void setDbLocation(String dbLocation){
		DbLocation = dbLocation;
	}
	
	public String getDbUserName(){
		return DbUserName;
	}
	
	public void setDbUserName(String dbUserName){
		DbUserName = dbUserName;
	}

	public String getDbPasswd(){
		return DbPasswd;	
	}
	
	public void setDbPasswd(String dbPasswd){
		DbPasswd = dbPasswd;
	}
	
	//get database connection -- mysql, oracle or sql server
	protected abstract Connection GetConnection();
	//run sql statements to initialize the current connection session
	protected abstract boolean InitializeSession(Connection conn);
	//import email operation
	protected abstract boolean Import(Connection conn, ArrayList<BugTraqMail> mails);
	//import email operation
	protected abstract String GetOssInfo(Connection conn, String key);
	//import email operation
	protected abstract boolean UpdateOssInfo(Connection conn, String key, String value);
	
	
}
