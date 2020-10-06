package com.akozlowski.domain;

import com.akozlowski.intrastructure.Database;
import com.akozlowski.intrastructure.Profile;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.SetEnvironmentVariable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SetEnvironmentVariable(key = "env", value = "test")
class UserDaoImplTest {


    public static final String SELECT_ID_NAME_FROM_USER_ORDER_BY_ID_DESC = "select id, name from user order by id desc";
    public static final String SELECT_COUNT_ID_AS_ID_FROM_USER = "select max(id) as id from user";
    private static final int NUM_TEST_USERS = 4;
    public static final String SELECT_ID_NAME_FROM_USER_WHERE_ID_AND_ID = "select id,name from user where id >= ? and id <= ?";
    private Connection connection;
    private List<User> users;

    private List<User> loadUsers() throws IOException {

        return Files
                .lines(Paths.get("src/test/resources/greetexpectations.txt"))
                .map(line -> line.split("[^A-Za-z]"))
                .map(Arrays::asList)
                .flatMap(list -> list.stream())
                .filter(word -> word.length() > 2 && word.length() < 15)
                .map(User::new)
                .limit(NUM_TEST_USERS)
                .collect(Collectors.toList());
    }

    @BeforeEach
    void setUp() throws SQLException, IOException {

        users = loadUsers();

        final Properties properties = Profile.getProperties("db");
        final Database dbConnection = Database.getInstance();
        dbConnection.connect(properties);
        connection = dbConnection.getConnection();
        connection.setAutoCommit(false);
    }

    @AfterEach
    void tearDown() throws SQLException {
        Database.getInstance().close();
    }

    @Test
    void testFindAndUpdate() throws SQLException {
        final User user = users.get(0);

        final UserDaoImpl userDao = new UserDaoImpl();
        userDao.save(user);

        final int maxUserId = getMaxUserId();

        user.setId(maxUserId);

        Optional<User> retrievedUserOptional = userDao.findById(maxUserId);

        assertTrue(retrievedUserOptional.isPresent(),"No user retrieved");

        User retrievedUser = retrievedUserOptional.get();

        assertEquals(user, retrievedUser, "Retrieved user doesn't match the saved user");

        user.setName("xyz");

        userDao.update(user);

        retrievedUserOptional = userDao.findById(maxUserId);

        assertTrue(retrievedUserOptional.isPresent(),"No user retrieved");

        retrievedUser = retrievedUserOptional.get();

        assertEquals(user, retrievedUser, "Retrieved user doesn't match the updated user");

        System.out.println(retrievedUser);

    }

    @Test
    @SetEnvironmentVariable(key = "env", value = "test")
    void testSaveMultipleUsers() throws SQLException {
        final UserDaoImpl userDao = new UserDaoImpl();
        users.forEach(userDao::save);
        final int maxUserId = getMaxUserId();

        for (int i = 0; i < users.size(); i++) {
            final int id = (maxUserId - users.size()) + i + 1;
            users.get(i).setId(id);
        }

        final List<User> retrievedUsers = getUsersInIdRange((maxUserId - users.size()) + 1, maxUserId);

        assertEquals(NUM_TEST_USERS,retrievedUsers.size(),"different number of users" );

        System.out.println(maxUserId);

        assertEquals(users,retrievedUsers,"Retrieved users don't match saved users");
    }

    @Test
    @SetEnvironmentVariable(key = "env", value = "test")
    void testDeleteMultipleUsers() throws SQLException {
        final UserDaoImpl userDao = new UserDaoImpl();
        users.forEach(userDao::save);

        final int maxUserId = getMaxUserId();

        for (int i = 0; i < users.size(); i++) {
            final int id = (maxUserId - users.size()) + i + 1;
            users.get(i).setId(id);
        }

        final int deleteUserId = NUM_TEST_USERS/2;
        final User deleteUser = users.get(deleteUserId);

        final List<User> retrievedUsers = getUsersInIdRange((maxUserId - users.size()) + 1, maxUserId);

        assertEquals(NUM_TEST_USERS,retrievedUsers.size(),"different number of users" );

        System.out.println(maxUserId);

        assertEquals(users,retrievedUsers,"Retrieved users don't match saved users");
    }

    @Test
    void testGetAll() throws SQLException {
        final UserDaoImpl userDao = new UserDaoImpl();
        users.forEach(userDao::save);
        final int maxUserId = getMaxUserId();

        for (int i = 0; i < users.size(); i++) {
            final int id = (maxUserId - users.size()) + i + 1;
            users.get(i).setId(id);
        }

        List<User> retrievedUsers = userDao.getAll();
        retrievedUsers = retrievedUsers.subList(retrievedUsers.size() - users.size(), retrievedUsers.size());

        assertEquals(NUM_TEST_USERS,retrievedUsers.size(),"different number of users" );

        System.out.println(maxUserId);

        assertEquals(users,retrievedUsers,"Retrieved users don't match saved users");
    }

    private List<User> getUsersInIdRange(final int minId, final int maxId) throws SQLException {

        final ArrayList<User> retrievedUsers = new ArrayList<>();
        final PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ID_NAME_FROM_USER_WHERE_ID_AND_ID);
        preparedStatement.setInt(1,minId);
        preparedStatement.setInt(2,maxId);

        final ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            final int id = resultSet.getInt("id");
            final String name = resultSet.getString("name");

            retrievedUsers.add( new User(id, name));
        }

        preparedStatement.close();

        return retrievedUsers;
    }

    private int getMaxUserId() throws SQLException {

        final Statement statement = connection.createStatement();
        final ResultSet resultSet = statement.executeQuery(SELECT_COUNT_ID_AS_ID_FROM_USER);

        resultSet.next();
        final int id = resultSet.getInt("id");

        statement.close();
        return id;
    }

    @Test
    void testSave() throws SQLException {

        final String jupiter = "Jupiter";
        final User user = new User(jupiter);
        final UserDaoImpl userDao = new UserDaoImpl();

        userDao.save(user);
        final Statement statement = connection.createStatement();

        final ResultSet resultSet = statement.executeQuery(SELECT_ID_NAME_FROM_USER_ORDER_BY_ID_DESC);

        final boolean result = resultSet.next();
        assertTrue(result, "Cannot retrieve the record");

        final String name = resultSet.getString("name");
        assertEquals(jupiter, name, "The inserted record is wrong");
        statement.close();
    }
}
