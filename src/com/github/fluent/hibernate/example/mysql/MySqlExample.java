package com.github.fluent.hibernate.example.mysql;

import com.github.fluent.hibernate.H;
import com.github.fluent.hibernate.cfg.Fluent;
import com.github.fluent.hibernate.cfg.scanner.EntityScanner;
import com.github.fluent.hibernate.example.mysql.persistent.User;
import com.github.fluent.hibernate.example.mysql.persistent.UserAddress;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.jboss.logging.Logger;

import java.util.List;

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
            // confgiureUsingHibernate4();
            // confgiureUsingHibernate5();
            new MySqlExample().doSomeDatabaseStuff();
        } finally {
            Fluent.factory().close();
        }
    }

    private static void confgiureForMySql() {
        Fluent.factory().dontUseHibernateCfgXml().useNamingStrategy()
                .scanPackages("com.github.fluent.hibernate.example.mysql.persistent").build();

        // Fluent.factory().useNamingStrategy().build();
    }

    private static void confgiureForH2() {
        Fluent.factory().dontUseHibernateCfgXml()
                .hibernatePropertiesFromClassPathResource("hibernate-h2.properties")
                .scanPackages("com.github.fluent.hibernate.example.mysql.persistent")
                .useNamingStrategy().build();
    }

    private static void confgiureUsingHibernate4() {
        Configuration configuration = new Configuration().configure();

        StandardServiceRegistryBuilder registryBuilder = new StandardServiceRegistryBuilder();
        // registryBuilder.loadProperties("hibernate-h2.properties");
        SessionFactory sessionFactory = configuration.buildSessionFactory(
                registryBuilder.applySettings(configuration.getProperties()).build());

        Fluent.configureFromExistingSessionFactory(sessionFactory);
    }

    private static void confgiureUsingHibernate5() {
        /*
        Configuration configuration = new Configuration();
        
        EntityScanner.scanPackages("com.github.fluent.hibernate.example.mysql.persistent")
                .addTo(configuration);
        
        SessionFactory sessionFactory = configuration.buildSessionFactory();
        */

        List<Class<?>> classes = EntityScanner
                .scanPackages("com.github.fluent.hibernate.example.mysql.persistent").result();

        MetadataSources metadataSources = new MetadataSources();
        for (Class<?> annotatedClass : classes) {
            metadataSources.addAnnotatedClass(annotatedClass);
        }

        SessionFactory sessionFactory = metadataSources.buildMetadata().buildSessionFactory();

        Fluent.configureFromExistingSessionFactory(sessionFactory);
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
