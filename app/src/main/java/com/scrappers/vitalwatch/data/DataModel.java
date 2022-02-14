package com.scrappers.vitalwatch.data;

/**
 * Represents an abstract layer to the data model.
 * Holds the user data.
 * @author pavl_g.
 */
public abstract class DataModel {
    private String username;
    private String userPassword;
    private String userAccount;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount;
    }
}
