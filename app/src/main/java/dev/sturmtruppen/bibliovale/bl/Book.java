package dev.sturmtruppen.bibliovale.bl;


import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.List;

public class Book {

    private String title;
    private List<String> authors = new ArrayList<String>();
    private String year;
    private String thumbnailUrl;
    private String isbn10;
    private String isbn13;
    private Drawable thumbnail;
    private int totalItems;


    public Book(){
        super();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getAuthors() {
        return authors;
    }

    public void setAuthors(List<String> authors) {
        this.authors = authors;
    }

    public void setAuthor(String author) {
        this.authors.add(author);
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getIsbn10() {
        return isbn10;
    }

    public void setIsbn10(String isbn10) {
        this.isbn10 = isbn10;
    }

    public String getIsbn13() {
        return isbn13;
    }

    public void setIsbn13(String isbn13) {
        this.isbn13 = isbn13;
    }

    public Drawable getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(Drawable thumbnail) {
        this.thumbnail = thumbnail;
    }

    public int getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(int totalItems) {
        this.totalItems = totalItems;
    }

    public String toString(){
        String out = "";
        out = out + "Title: " + this.title + "\n";
        out = out + "Authors: ";
        for(final String author : this.authors){
            out = out + author + ", ";
        }
        out = out + "\n";
        out = out + "Year: " + this.year + "\n";
        out = out + "Thumbnail: " + this.thumbnailUrl + "\n";
        out = out + "ISBN_10: " + this.isbn10 + "\n";
        out = out + "ISBN_13: " + this.isbn13 + "\n";
        return out;
    }
}
