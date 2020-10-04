package com.akozlowski.intrastructure;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Database {
    private static final Database db = new Database();
    private Connection connection;

    private Database() {
    }

    public Connection getConnection() {
        return connection;
    }

    public static Database getInstance() {
        return db;
    }

    public void connect(final Properties properties) throws SQLException {
        final String server = properties.getProperty("server");
        final String port = properties.getProperty("port");
        final String database = properties.getProperty("database");
        final String user = properties.getProperty("user");
        final String password = properties.getProperty("password");

        final String url = String.format("jdbc:mysql://%s:%s/%s", server, port, database);

        connection = DriverManager.getConnection(url, user,password);
    }

    public void close() throws SQLException {
        connection.close();
    }
}
