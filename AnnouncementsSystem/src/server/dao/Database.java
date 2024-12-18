package server.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Database {
    private static Connection conn = null;

    public static Connection connect() {
        if(conn == null) {
            try {
                Properties props = loadProperties();
                String url = props.getProperty("dburl");
                conn = DriverManager.getConnection(url, props);
            } catch (SQLException e) {

                System.err.println("ERRO ao conectar ao banco de dados: " + e.getLocalizedMessage());
            }
        }

        return conn;
    }

    public static void disconnect() {
        if(conn != null) {
            try {
                conn.close();
                conn = null;
            } catch (SQLException e) {
                System.err.println("ERRO ao dessconectar do banco de dados: " + e.getLocalizedMessage());
            }
        }
    }

    private static Properties loadProperties() {
        Properties props = new Properties();
        props.setProperty("user", "root");
        props.setProperty("password", "");
        props.setProperty("dburl", "jdbc:mysql://localhost:3306/announcements_system");
        props.setProperty("allowPublicKeyRetrieval", "true");
        props.setProperty("useSSL", "false");

        return props;
    }
}
