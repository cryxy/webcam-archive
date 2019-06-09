package de.cryxy.homeauto.surveillance.application;

/**
 * 
 * @author fabian
 *
 */
public class ApplicationServer {

	// private String configFile;
	//
	// private static ApplicationServer server;
	//
	//
	// public static void main(String[] args) throws Exception {
	// String configFile = System.getProperty(Config.CONFIG_FILE_PROPERTY,
	// Config.DEFAULT_CONFIG_FILE_NAME);
	// server = new ApplicationServer(configFile);
	// server.start();
	// }
	//
	// public ApplicationServer(String configFile) {
	// this.configFile = configFile;
	// }
	//
	// public void start() throws Exception {
	//
	// // read properties file
	// Reader reader = new FileReader(configFile);
	// Properties props = new Properties();
	// props.load(reader);
	//
	// new Swarm().fraction(new DatasourcesFraction().jdbcDriver("com.mysql", (d) ->
	// {
	// d.driverClassName("com.mysql.jdbc.Driver");
	// d.xaDatasourceClass("com.mysql.jdbc.jdbc2.optional.MysqlXADataSource");
	// d.driverModuleName("com.mysql");
	// }).dataSource("ArchiveDS", (ds) -> {
	// ds.driverName("com.mysql");
	// // jdbc url
	// final String JDBC_URL = "javax.persistence.jdbc.url";
	// ds.connectionUrl(props.getProperty(JDBC_URL));
	// // jdbc user
	// final String JDBC_USER = "javax.persistence.jdbc.user";
	// ds.userName(props.getProperty(JDBC_USER));
	// // jdbc password
	// final String JDBC_PASSWD = "javax.persistence.jdbc.password";
	// ds.password(props.getProperty(JDBC_PASSWD));
	// })).start().deploy();
	// }

}
