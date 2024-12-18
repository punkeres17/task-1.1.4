package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {

    public UserDaoJDBCImpl() {
    }

    @Override
    public void createUsersTable() {
        try (final Connection connection = Util.getConnection();
             final Statement stmt = connection.createStatement()
        ) {
            final String CREATE_TABLE = """
                    CREATE TABLE IF NOT EXISTS user (
                    id INT AUTO_INCREMENT PRIMARY KEY, 
                    name VARCHAR(45) NOT NULL, 
                    lastname VARCHAR(45) NOT NULL, 
                    age INT)
                    """;
            stmt.execute(CREATE_TABLE);
        } catch (final SQLException e) {
            System.err.println("There is no such table");
        }

    }

    @Override
    public void dropUsersTable() {
        try (final Connection connection = Util.getConnection();
             final Statement stmt = connection.createStatement()
        ) {
            final String DROP_TABLE = "DROP TABLE IF EXISTS user";
            stmt.execute(DROP_TABLE);
        } catch (final SQLException e) {
            System.err.println("There is no such table");
        }
    }

    @Override
    public void saveUser(final String name, final String lastName, final byte age) {
        try (final Connection connection = Util.getConnection()) {
            try {
                connection.setAutoCommit(false);

                final String INSERT_USER = "INSERT INTO user (name, lastname, age) VALUES (?, ?, ?)";
                final PreparedStatement preparedStmt = connection.prepareStatement(INSERT_USER);
                preparedStmt.setString(1, name);
                preparedStmt.setString(2, lastName);
                preparedStmt.setByte(3, age);
                preparedStmt.executeUpdate();

                connection.commit();
                System.out.println("User with the name - " + name + " has been added to the database");
            } catch (final Exception e) {
                connection.rollback();
                System.err.println("Transaction rolled back");
            }
        } catch (
                final SQLException e) {
            System.err.println("Failed to add data");
        }
    }

    @Override
    public void removeUserById(final long id) {
        try (final Connection connection = Util.getConnection()) {
            try {
                connection.setAutoCommit(false);

                final String DELETE_USER = "DELETE FROM user WHERE id = ?";
                final PreparedStatement preparedStmt = connection.prepareStatement(DELETE_USER);
                preparedStmt.setLong(1, id);
                preparedStmt.executeUpdate();

                connection.commit();
            } catch (final Exception e) {
                connection.rollback();
                System.err.println("Transaction rolled back");
            }
        } catch (final SQLException e) {
            System.err.println("There is no such id");
        }
    }

    @Override
    public List<User> getAllUsers() {
        final List<User> listOfUser = new ArrayList<>();
        final String SELECT_ALL_USERS = "SELECT * FROM user";

        try (final Connection connection = Util.getConnection();
             final Statement stmt = connection.createStatement();
             final ResultSet resultSet = stmt.executeQuery(SELECT_ALL_USERS)
        ) {

            while (resultSet.next()) {
                final String name = resultSet.getString("name");
                final String lastName = resultSet.getString("lastName");
                final byte age = resultSet.getByte("age");

                listOfUser.add(new User(name, lastName, age));
            }
        } catch (final SQLException e) {
            System.err.println("Data could not be retrieved");
        }
        return listOfUser;
    }

    @Override
    public void cleanUsersTable() {
        try (final Connection connection = Util.getConnection();
             final Statement stmt = connection.createStatement()
        ) {
            final String TRUNCATE_TABLE = "TRUNCATE TABLE user";
            stmt.execute(TRUNCATE_TABLE);
        } catch (final SQLException e) {
            System.err.println("Error erasing the table!");
        }
    }
}
