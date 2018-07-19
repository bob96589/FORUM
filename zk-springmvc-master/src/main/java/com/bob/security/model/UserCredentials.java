package com.bob.security.model;

import javax.persistence.Entity;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * API model for the user credentials.
 *
 * @author cassiomolin
 */
@Entity
@XmlRootElement
public class UserCredentials {

    private String username;
    private String password;

    public UserCredentials() {

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}