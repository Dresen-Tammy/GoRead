package com.dresen.goread.model;

import android.icu.text.UnicodeSet;

import java.util.Comparator;

public class Book {
    private Integer id;
    private String title;
    private String description;
    private Author author;

    public Book() {
    }

    public Book(Integer id, String title, String description, Author author) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.author = author;
    }
    public Book(String title, String description, Author author) {
        this.title = title;
        this.description = description;
        this.author = author;
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

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }


    public static Comparator<Book> bookTitleComparator = new Comparator<Book>() {
        @Override
        public int compare(Book o1, Book o2) {
            String title1 = o1.getTitle().toUpperCase();
            String title2 = o2.getTitle().toUpperCase();

            return title1.compareTo(title2);
        }};

    public static Comparator<Book> bookAuthorComparator = new Comparator<Book>() {
        @Override
        public int compare(Book o1, Book o2) {
            String author1 = o1.getAuthor().getLastName().toUpperCase()+o1.getAuthor().getFirstName().toUpperCase();
            String author2 = o2.getAuthor().getLastName().toUpperCase()+o2.getAuthor().getFirstName().toUpperCase();

            return author1.compareTo(author2);
        }};

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", author=" + author +
                '}';
    }
}
