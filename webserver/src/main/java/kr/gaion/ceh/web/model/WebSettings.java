package kr.gaion.ceh.web.model;

import kr.gaion.ceh.common.config.ConfigBase;

public class WebSettings extends ConfigBase {

	/*
	 * Class constants
	 */
	public static final String SPARK_HOST = "sparkHost";
	public static final String SPARK_PORT = "sparkPort";
	public static final String ES_HOST = "esHost";
	public static final String ES_TRANSPORT_PORT = "esTransportPort";
	public static final String MATCHING_TABLE_PATH = "matchingTablePath";
	public static final String DATA_SEPARATOR = "dataSeparator";
	/*
	 * Class properties
	 */
	protected String sparkHost;
	protected int sparkPort;
	protected String esHost;
	protected int esTransportPort;
	protected String esClusterName;
	protected String matchingTablePath;
	protected String dataSeparator;

	/*
	 * Setters and Getters
	 */
	public String getSparkHost() {
		return sparkHost;
	}

	public void setSparkHost(String sparkHost) {
		this.sparkHost = sparkHost;
	}

	public int getSparkPort() {
		return sparkPort;
	}

	public void setSparkPort(int sparkPort) {
		this.sparkPort = sparkPort;
	}

	public String getMatchingTablePath() {
		return matchingTablePath;
	}

	public void setMatchingTablePath(String matchingTablePath) {
		this.matchingTablePath = matchingTablePath;
	}

	public String getDataSeparator() {
		return dataSeparator;
	}

	public void setDataSeparator(String dataSeparator) {
		this.dataSeparator = dataSeparator;
	}

	public String getEsHost() {
		return esHost;
	}

	public void setEsHost(String esHost) {
		this.esHost = esHost;
	}

	public int getEsTransportPort() {
		return esTransportPort;
	}

	public void setEsTransportPort(int esTransportPort) {
		this.esTransportPort = esTransportPort;
	}

	public String getEsClusterName() {
		return esClusterName;
	}

	public void setEsClusterName(String esClusterName) {
		this.esClusterName = esClusterName;
	}

}
