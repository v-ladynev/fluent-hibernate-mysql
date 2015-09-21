package com.github.fluent.hibernate.example.mysql.persistent;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 *
 * @author V.Ladynev
 */
@Entity
@Table(name = "users")
public class User {

    private Long pid;

    private String login;

    private Integer age;

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

    @Column(name = "f_age")
    public Integer getAge() {
        return age;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return String.format("login = '%s', age = '%d'", login, age);
    }

}
