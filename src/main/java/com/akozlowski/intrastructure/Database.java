package com.akozlowski.intrastructure;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    private static final Database db = new Database();
//    private static final String URL = "jdbc:mysql://localhost:3306/mydb?serverTimezone=UTC";
    private static final String URL = "jdbc:mysql://localhost:3306/mysql";
    private Connection connection;

    private Database() {
    }

    public static Database getInstance() {
        return db;
    }

    public void connect() throws SQLException {
        connection = DriverManager.getConnection(URL, "root", "Buster");
    }

    public void close() throws SQLException {
        connection.close();
    }
}
