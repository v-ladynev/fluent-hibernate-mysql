package com.github.fluent.hibernate.example.mysql.persistent;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

/**
 * A user.
 *
 * @author V.Ladynev
 */
@Entity
// @Table(name = "users")
public class User {

    public static final String LOGIN = "login";

    private Long pid;

    private String login;

    private String name;

    private Integer age;

    private UserAddress address;

    @Id
    @GeneratedValue
    @Column(name = "f_pid")
    public Long getPid() {
        return pid;
    }

    @Column(name = "f_login")
    public String getLogin() {
        return login;
    }

    @Column(name = "f_name")
    public String getName() {
        return name;
    }

    @Column(name = "f_age")
    public Integer getAge() {
        return age;
    }

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "f_user_address_pid")
    public UserAddress getAddress() {
        return address;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public void setAddress(UserAddress address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return String.format("login = '%s', name = '%s', age = '%d'", login, name, age);
    }

    public static User create(String login, String name, int age) {
        User result = new User();
        result.setLogin(login);
        result.setName(name);
        result.setAge(age);
        return result;
    }

}
