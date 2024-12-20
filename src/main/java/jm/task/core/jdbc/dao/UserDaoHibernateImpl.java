package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

@Slf4j
public class UserDaoHibernateImpl implements UserDao {
    public UserDaoHibernateImpl() {

    }

    @Override
    public void createUsersTable() {
        try (final Session session = Util.getSessionFactory().openSession()) {

            final Transaction transaction = session.beginTransaction();

            final String CREATE_TABLE = """
                    CREATE TABLE IF NOT EXISTS user (
                    id INT AUTO_INCREMENT PRIMARY KEY, 
                    name VARCHAR(45) NOT NULL, 
                    lastname VARCHAR(45) NOT NULL, 
                    age INT)
                    """;
            session.createSQLQuery(CREATE_TABLE).executeUpdate();
            transaction.commit();
        } catch (final Exception e) {
            log.error("Error with creating table - 'user'", e);
            throw new HibernateException(e);
        }
    }


    @Override
    public void dropUsersTable() {
        try (final Session session = Util.getSessionFactory().openSession()) {

            final Transaction transaction = session.beginTransaction();

            final String DROP_TABLE = "DROP TABLE IF EXISTS user";
            session.createSQLQuery(DROP_TABLE).executeUpdate();
            transaction.commit();
        } catch (final Exception e) {
            log.error("Error with dropping the table - 'user'", e);
            throw new HibernateException(e);
        }
    }

    @Override
    public void saveUser(final String name, final String lastName, final byte age) {
        Transaction transaction = null;
        try (final Session session = Util.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            final User user = new User(name, lastName, age);
            session.save(user);
            transaction.commit();
        } catch (final Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            log.error("Error with adding data to the table - 'user'", e);
            throw new HibernateException(e);
        }
    }


    @Override
    public void removeUserById(final long id) {
        Transaction transaction = null;
        try (final Session session = Util.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            final User user = session.get(User.class, id);
            if (user != null) {
                session.delete(user);
                transaction.commit();
            }
        } catch (final Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            log.error("Error with deleting a user by index in the table - 'user'", e);
            throw new HibernateException(e);
        }
    }

    @Override
    public List<User> getAllUsers() {
        try (final Session session = Util.getSessionFactory().openSession()) {
            return session.createQuery("from User", User.class).list();
        } catch (final Exception e) {
            log.error("Error with getting all users'", e);
            throw new HibernateException(e);
        }
    }

    @Override
    public void cleanUsersTable() {
        try (final Session session = Util.getSessionFactory().openSession()) {
            final Transaction transaction = session.beginTransaction();
            final String TRUNCATE_TABLE = "TRUNCATE TABLE user";
            session.createSQLQuery(TRUNCATE_TABLE).executeUpdate();
            transaction.commit();
        } catch (final Exception e) {
            log.error("Error with deleting data from the table - 'user'", e);
            throw new HibernateException(e);
        } finally {
            Util.closeSessionFactory();
        }
    }
}

