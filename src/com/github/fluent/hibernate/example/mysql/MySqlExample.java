package com.github.fluent.hibernate.example.mysql;

import org.jboss.logging.Logger;

import com.github.fluent.hibernate.H;
import com.github.fluent.hibernate.example.mysql.persistent.User;
import com.github.fluent.hibernate.example.mysql.persistent.UserAddress;
import com.github.fluent.hibernate.factory.HibernateSessionFactory;

/**
 *
 * @author V.Ladynev
 */
public class MySqlExample {

    private static final String USER_A_STREET = "street A";

    private static final String USER_LOGIN_A = "loginA";

    private static final Logger LOG = Logger.getLogger(MySqlExample.class);

    public static void main(String[] args) {
        try {
            confgiureForMySql();
            // confgiureForH2();
            new MySqlExample().doSomeDatabaseStuff();
        } finally {
            HibernateSessionFactory.closeSessionFactory();
        }
    }

    private static void confgiureForMySql() {
        HibernateSessionFactory.Builder.configureFromDefaultHibernateCfgXml()
                .createSessionFactory();
    }

    private static void confgiureForH2() {
        HibernateSessionFactory.Builder.configureFromDefaultHibernateCfgXml()
        .addHibernatePropertiesFromClassPathResource("hibernate-h2.properties")
                .createSessionFactory();
    }

    private void doSomeDatabaseStuff() {
        deleteAllUsers();
        insertUsers();
        countUsers();
        findUserA();
        doSomeUserAddressStuff();
    }

    private void doSomeUserAddressStuff() {
        getUserByStreet();
        getStreetByUser();
    }

    private void getUserByStreet() {
        User user = H.<User> request(User.class).innerJoin("address")
                .eq("address.street", USER_A_STREET).first();

        LOG.info(String.format("User %s address: %s", user, user.getAddress()));
    }

    private void getStreetByUser() {
        final String userLogin = USER_LOGIN_A;
        UserAddress address = H.<UserAddress> request(UserAddress.class).innerJoin("user")
                .eq("user.login", userLogin).first();
        LOG.info(String.format("UserAddress: %s", address));
    }

    private void findUserA() {
        User user = findUserByLogin(USER_LOGIN_A);
        LOG.info("User: " + user);
    }

    private User findUserByLogin(String login) {
        return H.<User> request(User.class).eq(User.LOGIN, login).first();
    }

    private void deleteAllUsers() {
        H.update("delete from User").execute();
    }

    private void insertUsers() {
        H.saveOrUpdate(userA());
        H.saveOrUpdate(userB());
    }

    private void countUsers() {
        int count = H.<Long> request(User.class).count();
        LOG.info("Users count: " + count);
    }

    public static User userA() {
        return addAddress(User.create(USER_LOGIN_A, "A user", 20), USER_A_STREET);
    }

    public static User userB() {
        return addAddress(User.create("loginB", "B user", 30), "street B");
    }

    public static User addAddress(User toUser, String street) {
        toUser.setAddress(UserAddress.create(street, toUser));
        return toUser;
    }
}
