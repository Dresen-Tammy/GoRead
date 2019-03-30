package com.dresen.goread.model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Set;

public class Author {
    private Integer id;
    private String firstName;
    private String lastName;
    private Set<Book> books;

    public Author() {
    }

    public Author(Integer id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Set<Book> getBooks() {
        return books;
    }

    public void setBooks(Set<Book> books) {
        this.books = books;
    }


    public static Comparator<Book> bookTitleComparator = new Comparator<Book>() {
        @Override
        public int compare(Book o1, Book o2) {
            String title1 = o1.getTitle().toUpperCase();
            String title2 = o2.getTitle().toUpperCase();

            return title1.compareTo(title2);
        }
    };


}