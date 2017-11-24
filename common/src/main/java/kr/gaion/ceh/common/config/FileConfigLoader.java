package kr.gaion.ceh.common.config;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import kr.gaion.ceh.common.interfaces.IConfigurable;

/***
 * To load setting from configuration file This is applied singleton
 * design-pattern
 * 
 * @author hoang
 *
 */
public class FileConfigLoader extends ConfigBase {
	private Properties prop;
	// private static FileConfigLoader instance = null;
	private String path;

	/**
	 * To load all setting
	 * 
	 * @throws IOException
	 */
	public FileConfigLoader(String path) throws IOException {
		InputStream input = null;
		prop = new Properties();
		this.path = path;
		input = new FileInputStream(this.path);
		prop.load(input);
	}

	/**
	 * To update configuration file This function uses to update setting from
	 * web page
	 * 
	 * @param key
	 * @param value
	 */
	public void updateProperty(String key, String value) {
		prop.setProperty(key, value);
	}

	/**
	 * @see IConfigurable.IConfiguration#getSetting(java.lang.String) To get
	 *      setting value by key
	 * @param key
	 */
	@SuppressWarnings("unchecked")
	public String getSetting(String key) {
		return prop.getProperty(key);
	}

	/**
	 * This function uses for sending properties from web-server to spark-server
	 * 
	 * @param out
	 *            The output stream to write
	 * @throws IOException
	 */
	public void sendProperties(OutputStream out) throws IOException {
		prop.store(out, null);
	}

	/**
	 * to get current Properties
	 * 
	 * @return
	 */
	public Properties getProperties() {
		return prop;
	}

	/**
	 * to save all changes to configuration file
	 * 
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public void saveProperties() throws FileNotFoundException, IOException {
		// User updated information
		String userName = System.getProperty("user.name");
		String osName = System.getProperty("os.name");
		String osArch = System.getProperty("os.arch");
		String osVer = System.getProperty("os.version");
		prop.store(new FileOutputStream(path),
				String.format(("Updated by user: %s\nOS information: name: %s, architectue: %s, version: %s"), userName,
						osName, osArch, osVer));
	}

}
