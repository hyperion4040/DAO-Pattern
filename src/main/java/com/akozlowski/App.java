package com.akozlowski;

import com.akozlowski.domain.User;
import com.akozlowski.domain.UserDao;
import com.akozlowski.domain.UserDaoImpl;
import com.akozlowski.intrastructure.Database;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
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

        final UserDao userDao = new UserDaoImpl();

        List<User> users = userDao.getAll();
        users.forEach(user -> logger.log(Level.INFO, String.valueOf(user)));

        final Optional<User> user = userDao.findById(1);
        if (user.isPresent()) {
            logger.log(Level.INFO, "User is found: {0}",user.get());
        } else {
            logger.log(Level.INFO, "User does not exist");
        }

        final Optional<User> userToDelete = userDao.findById(1);
        userToDelete.ifPresent(userDao::delete);

        try {
            instance.close();
        } catch (SQLException e) {
            logger.log(Level.WARNING, CANNOT_CLOSE_THE_DATABASE_CONNECTION);
        }
    }
}
