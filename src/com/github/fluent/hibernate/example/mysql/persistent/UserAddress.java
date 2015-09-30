/**
 * $HeadURL$
 * $Revision$
 * $Date$
 *
 * Copyright (c) Isida-Informatica, Ltd. All Rights Reserved.
 */
package com.github.fluent.hibernate.example.mysql.persistent;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * Ann user address.
 *
 * @author V.Ladynev
 * @version $Id$
 */
@Entity
@Table(name = "user_addresses")
public class UserAddress {

    private Long pid;

    private String street;

    private User user;

    @Id
    @GeneratedValue
    @Column(name = "f_pid")
    public Long getPid() {
        return pid;
    }

    @Column(name = "f_street")
    public String getStreet() {
        return street;
    }

    @OneToOne(mappedBy = "address")
    public User getUser() {
        return user;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return String.format("street = '%s'", street);
    }

    public static UserAddress create(String street, User user) {
        UserAddress result = new UserAddress();
        result.setStreet(street);
        result.setUser(user);
        return result;
    }

}
