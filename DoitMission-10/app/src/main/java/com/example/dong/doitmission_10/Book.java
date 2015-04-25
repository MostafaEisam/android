package com.example.dong.doitmission_10;

/**
 * Created by Dong on 2015-04-01.
 */
public class Book {
    private String title;
    private String company;
    private String date;
    private String author;

    public Book(String title, String company, String date, String author){
        this.title = title;
        this.company = company;
        this.date = date;
        this.author = author;
    }

    public String getTitle(){
        return title;
    }

    public String getCompany(){
        return company;
    }

    public String getDate(){
        return date;
    }

    public String getAuthor(){
        return author;
    }
}
