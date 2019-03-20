package com.dresen.goread.model;

public class User {
    private Integer id;
    private String uname;
    private String pword;
    private String session;

    public User() {
    }

    public User(Integer id, String uname, String password, String session) {
        this.id = id;
        this.uname = uname;
        this.pword = password;
        this.session = session;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getPword() {
        return pword;
    }

    public void setPword(String pword) {
        this.pword = pword;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }
}
