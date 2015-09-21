package com.github.fluent.hibernate.example.mysql;

import java.util.List;

import org.jboss.logging.Logger;

import com.github.fluent.hibernate.H;
import com.github.fluent.hibernate.HibernateSessionFactory;
import com.github.fluent.hibernate.example.mysql.persistent.User;

public class MySqlExample {

    private static final User USER_A = createUser("loginA", 20);

    private static final User USER_B = createUser("loginB", 30);

    private static final Logger LOG = Logger.getLogger(MySqlExample.class);

    public static void main(String[] args) {
        HibernateSessionFactory.createSessionFactory("hibernate.cfg.xml");
        try {
            new MySqlExample().doSomeDatabaseStuff();
        } finally {
            HibernateSessionFactory.closeSessionFactory();
        }
    }

    private void doSomeDatabaseStuff() {
        deleteAllUsers();
        insertUsers();
        User user = findUser(USER_A.getLogin());
        LOG.info("User: " + user);
    }

    private User findUser(String login) {
        return H.<User> request(User.class).eq("login", login).first();
    }

    private void deleteAllUsers() {
        List<User> users = H.<User> request(User.class).list();
        for (User user : users) {
            H.delete(user);
        }
    }

    private void insertUsers() {
        H.saveOrUpdate(USER_A);
        H.saveOrUpdate(USER_B);
    }

    private static User createUser(String login, int age) {
        User result = new User();
        result.setLogin(login);
        result.setAge(age);
        return result;
    }

}
