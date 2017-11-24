package kr.gaion.ceh.web.io;

import java.io.File;

/**
 * 
 * @author hoang
 *
 */
public class WebServerDir {
	/**
	 * web server root directory
	 */
	public String rootPath;
	
	/**
	 * to get working directory of CEH-Web server
	 * @return
	 */
	public String getWorkingDir(){
		rootPath = System.getProperty("catalina.home");
		StringBuilder pathBuilder = new StringBuilder(rootPath);
		pathBuilder.append(File.separator);
		pathBuilder.append("webapps");
		pathBuilder.append(File.separator);
		pathBuilder.append("ceh");
		pathBuilder.append(File.separator);
		
		return pathBuilder.toString();
	}
}
