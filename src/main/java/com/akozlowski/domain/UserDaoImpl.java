package com.akozlowski.domain;

import com.akozlowski.intrastructure.Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDaoImpl implements UserDao {

    public static final String INSERT_INTO_USER_NAME_VALUES = "insert into user (name) values (?)";
    public static final String SELECT_ID_NAME_FROM_USER_ORDER_BY_ID = "select id, name from user order by id";
    public static final String DELETE_FROM_MYDB_USER_WHERE_ID = "delete from user where id=?";
    public static final String SELECT_ID_NAME_FROM_MYDB_USER_WHERE_ID = "select id,name from user where id = (?)";

    @Override
    public void save(final User user) {
        final Connection connection = Database.getInstance().getConnection();

        try {
            final PreparedStatement statement = connection.prepareStatement(INSERT_INTO_USER_NAME_VALUES);

            statement.setString(1, user.getName());
            statement.executeUpdate();
            statement.close();

        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public Optional<User> findById(final int id) {
        final Connection connection = Database.getInstance().getConnection();

        try {
            final PreparedStatement statement = connection.prepareStatement(SELECT_ID_NAME_FROM_MYDB_USER_WHERE_ID);

            statement.setString(1, String.valueOf(id));

            final ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                final String name = resultSet.getString("name");
                final User user = new User(id, name);

                return Optional.of(user);
            }
            statement.close();

        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return Optional.empty();
    }

    @Override
    public void update(final User user) {
        final Connection connection = Database.getInstance().getConnection();

        try {
            final PreparedStatement statement = connection.prepareStatement("update user set name=? where id=?");
            statement.setString(1, user.getName());
            statement.setInt(2,user.getId());
            statement.executeUpdate();
            statement.close();

        } catch (SQLException e) {
            throw new DaoException(e);
        }

    }

    @Override
    public void delete(final User user) {
        final Connection connection = Database.getInstance().getConnection();

        try {
            PreparedStatement statement = connection.prepareStatement(DELETE_FROM_MYDB_USER_WHERE_ID);
            statement.setInt(1,user.getId());
            statement.executeUpdate();
            statement.close();

        } catch (SQLException e) {
            throw new DaoException(e);
        }

    }

    @Override
    public List<User> getAll() {
        final Connection connection = Database.getInstance().getConnection();
        final List<User> users = new ArrayList<>();

        try {
            final Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery(SELECT_ID_NAME_FROM_USER_ORDER_BY_ID);

            while (resultSet.next()) {
                final int id = resultSet.getInt("id");
                final String name = resultSet.getString("name");
                users.add(new User(id, name));
            }
            statement.close();

        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return users;
    }
}
