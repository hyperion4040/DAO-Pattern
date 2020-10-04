package com.akozlowski;

import com.akozlowski.intrastructure.Database;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class App {

    public static final String CANNOT_OPEN_THE_DATABASE_CONNECTION = "Cannot open the database connection";
    public static final String CANNOT_CLOSE_THE_DATABASE_CONNECTION = "Cannot close the database connection";
    public static final String CONNECTION_SUCCESSFUL = "Connection successful";
    private static final Logger logger = Logger.getAnonymousLogger();

    public static void main(String[] args) {
        final Database instance = Database.getInstance();
        try {
            instance.connect();
        } catch (SQLException e) {
            logger.log(Level.WARNING, CANNOT_OPEN_THE_DATABASE_CONNECTION);
        }

        logger.log(Level.INFO, CONNECTION_SUCCESSFUL);

        try {
            instance.close();
        } catch (SQLException e) {
            logger.log(Level.WARNING, CANNOT_CLOSE_THE_DATABASE_CONNECTION);
        }
    }
}
