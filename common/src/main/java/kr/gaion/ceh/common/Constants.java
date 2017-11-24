package kr.gaion.ceh.common;

/**
 * CEH-project constants
 * 
 * @author hoang
 *
 */
public class Constants {

	// Web-project
	public static final String LOGIN_PAGE = "login";
	public static final String TEST_PAGE = "test";
	public static final String FPGROWTH_HOMEPAGE = "fpGrowth_homepage";
	public static final String SVM_CLASSIFICATION_HOMEPAGE = "classification_svm_homepage";
	public static final String SVC_CLASSIFICATION_HOMEPAGE = "classification_svc_homepage";
	public static final String RFC_CLASSIFICATION_HOMEPAGE = "classification_rfc_homepage";
	public static final String MLP_CLASSIFICATION_HOMEPAGE = "classification_mlp_homepage";
	public static final String NB_CLASSIFICATION_HOMEPAGE = "classification_nb_homepage";
	public static final String LR_CLASSIFICATION_HOMEPAGE = "classification_lr_homepage";
	public static final String CLUSTERING_HOMEPAGE = "clustering_homepage";
	public static final String FEATURES_SELECTION_CHISQ_HOMEPAGE = "features_selection_ChiSqSelector";
	public static final String HOME_PAGE = "homePage";
	public static final String UPLOAD_PAGE = "page-upload";
	public static final String DASHBOAD = "dashboard";
	public static final String ADMIN_USR = "thehn@gaion.kr";
	public static final String ADMIN_PWD = "$2a$10$xYYPUpUSjFXVqYg0QeXXcenCdk1hKeOEb3eD7muXkYaKpRNz/pDTS";

	// path to configuration file
	public static final String SYS_ENV_CEH_HOME = "CEH_HOME";
	public static final String CONF_DIR = "conf";
	public static final String DATA_DIR = "data";
	public static final String MODEL_DIR = "model";
	public static final String APP_CONFIG_FILE = "webApp.conf";
	public static final String REST_APP_CONFIG_FILE = "restApp.conf";
	public static final String SPARK_CONFIG_FILE = "spark.conf";

	// Application-JavaSpark properties (file configuration <key, value>)
	public static final String PROPTOCOL = "http";
	public static final String CONF_SPARK_HOST = "spark.server.host";
	public static final String CONF_SPARK_PORT = "spark.server.port";
	public static final String CONF_ES_HOST = "es.master.host";
	public static final String CONF_ES_TRANSPORT_PORT = "es.transport.port";
	public static final String CONF_ES_CLUSTER_NAME = "es.cluster.name";
	public static final String CONF_FPG_DATA_MATCHING_TABLE = "fp-growth.data.matching";
	public static final String CONF_DATA_LOOKUP_DEFINITION = "data.lookup.definition";
	public static final String CONF_FPG_DATA_SEPARATOR = "fp-growth.table.matching.separator";
	public static final String CONF_DATA_UNLABELED_SEPARATOR = "data.unlabeled.separator";
	public static final String CONF_REST_PORT = "rest.port"; 
	public static final String CONF_MAX_RESULTS = "result.preview.max_count";

	// Constants for Spark Rest API only
	public static final String SPA_ES_LABELED_INDEX = "labeled_index";
	public static final String SPA_ES_LABELED_TYPE = "_labeled_field";
	public static final String SPA_ES_DATA_FORMAT = "format";
	public static final String SPA_ES_DATA_FORMAT_TYPE = "_data_format";
	public static final String SPA_ES_DATA_FORMAT_SPARSE = "SPARSE";
	public static final String SPA_ES_DATA_FORMAT_DENSE = "DENSE";
	public static final int SPA_DEFAULT_PORT = 6886;

	// API constants
	public static final String API_SEARCH = "es-search";
	public static final String API_UPLOAD = "file-upload";
	public static final String API_ES_INDEX = "index-data";
	public static final String API_GET_MODELS = "get-models";
	public static final String API_DEL_MODEL = "delete-model";
	public static final String API_GET_MODEL_INFO = "model-info";
	public static final String API_GET_SCHEDULED_JOBS = "list-schedules";
	public static final String API_SCHEDULES = "schedule";
	public static final String API_RESCHEDULES = "reschedule";
	public static final String SCHEDULE_ACTION_GET_INFO = "get-info";
	public static final String SCHEDULE_ACTION_DELETE_JOB = "delete-job";
	public static final String SCHEDULE_ACTION_PAUSE_JOB = "pause-job";
	public static final String SCHEDULE_ACTION_RESUME_JOB = "resume-job";
	public static final String SCHEDULE_ACTION_RESCHEDULE_JOB = "reschedule-job";

	// K-means clustering
	// public static final String SPA_KMEANS_NUMCLUSTERS = "numClusters";
	// public static final String SPA_KMEANS_NUMITERATIONS = "numIterations";
	// public static final String SPA_KMEANS_MODEL_NAME = "modelNameKmeans";

	// Response elements
	public static final String FALSE_NEGATIVE = "numFalseNegatives";
	public static final String FALSE_POSITIVE = "numFalsePositives";
	public static final String NEGATIVE = "numNegatives";
	public static final String POSITIVE = "numPositives";
	public static final String TRUE_NEGATIVE = "numTrueNegatives";
	public static final String TRUE_POSITIVE = "numTruePositives";

	// about coordinate of chart
	public static final int XAXIS_PART = 0;
	public static final int YAXIS_PART = 1;
	public static final String KEY_XAXIS = "_1$mcD$sp";
	public static final String KEY_YAXIS = "_2$mcD$sp";

	// libraries
	public static final String SPA_JAR_ELASTICSEARCH = "elasticsearch-spark-20_2.11-5.3.0.jar";

	// extension
	public static final String CSV_EXTENSION = ".csv";
	public static final String CSV_SEPARATOR = ",";

	// Rest-API configuration key file
	public static final String CONF_FEATURE_DELIMITER = "feature.delimiter";

	// others
	public static final String MODEL_INDEX = "model";
}
