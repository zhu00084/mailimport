package ossIndex;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Properties;

public class OssMailImport {

	private static Logger slf4jLogger = LoggerFactory.getLogger(OssMailImport.class);

	public class SortByDate implements Comparator<BugTraqMail> {
		@Override
		public int compare(BugTraqMail mail1, BugTraqMail mail2) {

			return mail1.getReceivedDate().compareTo(mail2.getReceivedDate());
		}

	}

	public static void main(String[] args) {

		LogImportStartMessage(slf4jLogger);

		// read from config file
		OssConfiguration oc = new OssConfiguration();
		Properties pt = oc.readProp("OssIndex.config");

		if (pt == null) {
			slf4jLogger.info("Could not read application configurations");
			slf4jLogger.info("No mail imported.");
			System.exit(0);
		}

		MysqlDatabase db = new MysqlDatabase(pt.getProperty("dbdriver"), pt.getProperty("dblocation"),
				pt.getProperty("dbusername"), pt.getProperty("dbpasswd"));

		int nextMailIndex = 0;

		try {
			nextMailIndex = db.GetNextMailIndex();
		} catch (OssNoConnectionException e) {
			slf4jLogger.info("Could not retrieve NextMailIndex from database.");
			slf4jLogger.info("No mail imported.");
			System.exit(0);
		}

		if (nextMailIndex <= 0)
		{
			slf4jLogger.info("Invalid Mail Index. Please ensure NextMailIndex (>0) exists in ossimportinfo_t table.");
			slf4jLogger.info("Stop Mail Import.");
			LogImportEndMessage(slf4jLogger);
			System.exit(0);
		}
		
		

		// initialize mail client wrapper
		OssMailClient ossMailClient = new OssMailClient(pt.getProperty("host"),
				Integer.parseInt(pt.getProperty("port")), pt.getProperty("ssl"),
				pt.getProperty("mailbox"), pt.getProperty("password"));

		 ArrayList<BugTraqMail> btm =  ossMailClient.getMail(nextMailIndex);

		// btm.sort((new OssMailImport()).new SortByDate());
		// OssSerializationHelper.serialize(btm);
		// OssSerializationHelper.deSerialize("ossmail.ser");
		// ArrayList<BugTraqMail> btm =
		// OssSerializationHelper.deSerialize("ossmail.ser");

		 boolean success = db.ImportMail(btm);
		 if (!success)
		 {
			 slf4jLogger.info("Failed importing emails to database");
		 }
		
		try {
			db.UpdateNextMailIndex(ossMailClient.getNextMailIndex());
		} catch (OssNoConnectionException e) {
			slf4jLogger.info("Failed updateing NextMailIndex in OssImportInfo table");
		}

		LogImportEndMessage(slf4jLogger);

	}
	
	public static void LogImportEndMessage(Logger logger)
	{
		logger.info("----------End Mail Import----------");		
	}
	
	public static void LogImportStartMessage(Logger logger)
	{
		logger.info("**********Start Mail Import**********");		
	}
	

}
