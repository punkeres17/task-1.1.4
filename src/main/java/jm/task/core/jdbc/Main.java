package jm.task.core.jdbc;

import jm.task.core.jdbc.dao.UserDao;
import jm.task.core.jdbc.dao.UserDaoJDBCImpl;
import jm.task.core.jdbc.service.UserService;
import jm.task.core.jdbc.service.UserServiceImpl;
import jm.task.core.jdbc.util.Util;

public class Main {

    public static void main(final String[] args) {

        Util.getSessionFactory();

        final UserService userServiceHibernate = new UserServiceImpl();
        userServiceHibernate.createUsersTable();
        userServiceHibernate.saveUser("Roman", "Smirnov", (byte) 28);
        userServiceHibernate.saveUser("Liza", "Smirnova", (byte) 27);
        userServiceHibernate.saveUser("Pavel", "Nevmovenko", (byte) 30);
        userServiceHibernate.saveUser("Nikita", "Kologriviy", (byte) 5);

        userServiceHibernate.getAllUsers().forEach(System.out::println);
        userServiceHibernate.cleanUsersTable();
        userServiceHibernate.dropUsersTable();


    }
}
