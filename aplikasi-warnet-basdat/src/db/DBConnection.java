package db;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {
    private static String server = "LAPTOP-KAVAKS1D";
    private static String database = "ApkWarnet";
    private static String user = "sa";
    private static String password = "dina001";
    private static String url = "jdbc:sqlserver://" + server + ":1004;databaseName=" + database + ";encrypt=false";


    public static Connection getConnection() throws Exception {
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        return DriverManager.getConnection(url, user, password);
    }
}