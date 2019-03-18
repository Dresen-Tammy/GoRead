package com.dresen.goread.model;

public class Book {
    private Integer id;
    private String title;
    private String description;
    private String firstName;
    private String lastName;

    public Book() {
    }

    public Book(Integer id, String title, String description, String firstName, String lastName) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.firstName = firstName;
        this.lastName = lastName;
    }
    public Book(String title, String description, String firstName, String lastName) {
        this.title = title;
        this.description = description;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
