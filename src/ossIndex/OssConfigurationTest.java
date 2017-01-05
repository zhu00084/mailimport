package ossIndex;

import static org.junit.Assert.*;

import java.util.Properties;

import org.junit.Test;

public class OssConfigurationTest {

	@Test
	public void testSaveProperties() {
		OssConfiguration oc = new OssConfiguration();
		oc.saveProperties("OssIndex.config");
	}
	
	@Test
	public void testReadProp() {
		OssConfiguration oc = new OssConfiguration();
		Properties pt = oc.readProp("OssIndex.config");
		assertNotNull(pt);
		assertEquals("twoducks.ca", pt.getProperty("host"));
		assertEquals(993, Integer.parseInt(pt.getProperty("port")));
		assertEquals("enable", pt.getProperty("ssl"));
		assertEquals("debugtraq", pt.getProperty("mailbox"));
		assertEquals("d8gh\\#putr", pt.getProperty("password"));

	}
}

