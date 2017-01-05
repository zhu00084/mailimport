package ossIndex;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class MysqlDatabase extends Database {	

	private final Logger slf4jLogger = LoggerFactory.getLogger(MysqlDatabase.class);
	
	public MysqlDatabase(String dbDriver, String dbLocation, String dbUserName, String dbPasswd) {
		super(dbDriver, dbLocation, dbUserName, dbPasswd);
	}

	@Override
	protected boolean InitializeSession(Connection conn) {
		
		String query = "SET NAMES 'utf8mb4'";

		PreparedStatement preparedStmt;
		try {
			preparedStmt = conn.prepareStatement(query);		
			preparedStmt.execute();
		} catch (SQLException e) {
			slf4jLogger.error(e.toString());
			return false;
		}
		
		return true;
	}

	@Override
	protected Connection GetConnection() {
		Connection conn = null;
		// create a mysql database connection		
		try {
			Class.forName(getDbDriver());
		} catch (ClassNotFoundException e) {
			slf4jLogger.error(e.toString());
			return conn;
		}

		//String connStr = String.format("jdbc:%s?useUnicode=true" + "&user=%s&password=%s",
		//		"mysql://localhost/ossindex", "root", "Shanghai1");		
		String connStr = String.format("jdbc:%s?useUnicode=true" + "&user=%s&password=%s",
				this.getDbLocation(), this.getDbUserName(), this.getDbPasswd());		
				
		try {
			conn = DriverManager.getConnection(connStr);
		} catch (SQLException e) {
			slf4jLogger.error(e.toString());
			return null;
		}

		return conn;
	}

	@Override
	protected boolean Import(Connection conn, ArrayList<BugTraqMail> mails)
	{
		
		// mysql insert statement
		String query = "insert into ossindex_t (MailIndex,MsgID, MailSubject, Sender,  Date, MailContent)"
		+ " values (?, ?, ?, ?, ?, ?)";// create the mysql insert preparedstatement
		
		PreparedStatement preparedStmt;
		
		try {
			preparedStmt = conn.prepareStatement(query);
		} catch (SQLException e1) {
			slf4jLogger.error(e1.toString());
			return false;
		}	
		
		int paramIndex = 0;
		for (int j = 0; j < mails.size(); j++) {
			paramIndex = 1;
			try {
				preparedStmt.setInt(paramIndex++, mails.get(j).getMailIndex());
				preparedStmt.setString(paramIndex++, mails.get(j).getMsgid());
				preparedStmt.setString(paramIndex++, mails.get(j).getSubject());
				preparedStmt.setString(paramIndex++, mails.get(j).getFrom());
				preparedStmt.setTimestamp(paramIndex++, new Timestamp(mails.get(j).getReceivedDate().getTime()));
				preparedStmt.setString(paramIndex++, mails.get(j).getBody());

				preparedStmt.execute();
			
			} catch (SQLException e) {
				slf4jLogger.error(e.toString());
			}
		}
		
		return true;
	}

	@Override
	protected String GetOssInfo(Connection conn, String key) {
		// the mysql insert statement
		String query = String.format("select oss_value from ossimportinfo_t where oss_key = '%s'",key);
		// create the mysql insert preparedstatement
		String value = "";
		Statement Stmt;
		try {
			Stmt = conn.createStatement();

			ResultSet rs = Stmt.executeQuery(query);
			
			if (rs.isBeforeFirst())
			{
				rs.next();
				value = rs.getString(1);		
				
			}

		} catch (SQLException e) {
			slf4jLogger.error(e.toString());
		}
		
		return value;
	}

	@Override
	protected boolean UpdateOssInfo(Connection conn, String key, String value) {
		String query = String.format("UPDATE ossimportinfo_t SET OSS_Value=%s WHERE OSS_Key='%s'",value,key );
		Statement Stmt;
		try {
			Stmt = conn.createStatement();

			Stmt.executeUpdate(query);			

		} catch (SQLException e) {
			slf4jLogger.error(e.toString());
			return false;
		}

		return true;
	}

}
