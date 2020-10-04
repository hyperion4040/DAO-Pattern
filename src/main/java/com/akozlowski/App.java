package com.akozlowski;

import com.akozlowski.domain.User;
import com.akozlowski.domain.UserDao;
import com.akozlowski.domain.UserDaoImpl;
import com.akozlowski.intrastructure.Database;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class App {

    public static final String CANNOT_OPEN_THE_DATABASE_CONNECTION = "Cannot open the database connection";
    public static final String CANNOT_CLOSE_THE_DATABASE_CONNECTION = "Cannot close the database connection";
    public static final String CONNECTION_SUCCESSFUL = "Connection successful";
    public static final String USER_IS_FOUND = "User is found: {0}";
    public static final String USER_DOES_NOT_EXIST = "User does not exist";
    private static final Logger logger = Logger.getAnonymousLogger();

    public static void main(String[] args) {

        Properties properties = new Properties();
        try {
            properties.load(App.class.getResourceAsStream("/config/db.properties"));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        final Database instance = Database.getInstance();
        try {
            instance.connect(properties);
        } catch (SQLException e) {
            logger.log(Level.WARNING, CANNOT_OPEN_THE_DATABASE_CONNECTION);
            return;
        }

        logger.log(Level.INFO, CONNECTION_SUCCESSFUL);

        final UserDao userDao = new UserDaoImpl();

        List<User> users = userDao.getAll();
        users.forEach(user -> logger.log(Level.INFO, String.valueOf(user)));

        final Optional<User> user = userDao.findById(2);
        if (user.isPresent()) {
            final User userToUpdate = user.get();
            logger.log(Level.INFO, USER_IS_FOUND, userToUpdate);
            userToUpdate.setName("Snoopy");
            userDao.update(userToUpdate);
        } else {
            logger.log(Level.INFO, USER_DOES_NOT_EXIST);
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
