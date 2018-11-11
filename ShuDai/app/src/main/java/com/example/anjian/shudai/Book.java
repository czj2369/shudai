package com.example.anjian.shudai;

import android.app.Activity;

/**
 * Created by Administrator on 2018/10/24.
 */

public class Book {
    private String bookName;
    private String author;
    private String imagehref;
    private Activity activity;
    private String bookContinue;
    private int select;

    public void setSelect(int select) {
        this.select = select;
    }

    public int getSelect() {
        return select;
    }

    public void setBookContinue(String bookContinue) {
        this.bookContinue = bookContinue;
    }

    public String getBookContinue() {
        return bookContinue;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public String getImagehref() {
        return imagehref;
    }

    public void setImagehref(String imagehref) {
        this.imagehref = imagehref;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public Book(Activity activity,String name,String author,String href){
        this.activity = activity;
        bookName = name;
        this.author = author;
        imagehref = href;
    }
}
