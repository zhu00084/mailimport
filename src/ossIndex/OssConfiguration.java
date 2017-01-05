package ossIndex;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OssConfiguration {

	private final Logger slf4jLogger = LoggerFactory.getLogger(OssConfiguration.class);
	
	public void saveProperties(String configFile) {
		Properties prop = new Properties();
		OutputStream output = null;

		try {

			output = new FileOutputStream(configFile);

			// set the properties value
			prop.setProperty("host", "twoducks.ca");
			prop.setProperty("port", "993");
			prop.setProperty("ssl", "true");
			prop.setProperty("mailbox", "debugtraq");
			prop.setProperty("password", "d8gh#putr");
			prop.setProperty("dbdriver", "com.mysql.jdbc.Driver");
			prop.setProperty("dblocation", "mysql://localhost/ossindex");
			prop.setProperty("dbusername", "root");
			prop.setProperty("dbpasswd", "Shanghai1");

			// save properties to project root folder
			prop.store(output, null);

		} catch (IOException io) {
			io.printStackTrace();
		} finally {
			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public Properties readProp(String configFile) {
		Properties prop = new Properties();
		InputStream input = null;

		try {

			input = new FileInputStream(configFile);
			prop.load(input);

		} catch (IOException ex) {
			slf4jLogger.error(ex.toString());
			prop = null;
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					slf4jLogger.error(e.toString());
				}
			}
		}

		return prop;

	}
}
