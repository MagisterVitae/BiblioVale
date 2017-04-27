package dev.sturmtruppen.bibliovale.businessLogic.BO;


import android.graphics.drawable.Drawable;
import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import dev.sturmtruppen.bibliovale.businessLogic.BiblioValeApi;
import dev.sturmtruppen.bibliovale.businessLogic.DataFetchers.BookRepositoryDispatcher;
import dev.sturmtruppen.bibliovale.businessLogic.GlobalConstants;
import dev.sturmtruppen.bibliovale.businessLogic.Helpers.JSONHelper;

public class Book{

    private int id;
    private String title;
    private List<Author> authors = new ArrayList<Author>();
    private String year;
    private String thumbnailUrl;
    private String isbn10;
    private String isbn13;
    private String status;
    private Drawable thumbnail;
    private int totalItems;
    private String originalJson;
    private Genre genre;
    private String notes;


    public Book(){
        super();
    }
    public Book(JSONObject _jsonBook){
        try {
            this.originalJson = _jsonBook.toString();
            this.id = _jsonBook.getInt("id");
            this.title = _jsonBook.getString("title");
            this.year = _jsonBook.getString("year");
            this.isbn10 = _jsonBook.getString("isbn_10");
            this.isbn13 = _jsonBook.getString("isbn_13");
            this.status = _jsonBook.getString("status");
            this.notes = _jsonBook.getString("notes");

            this.setGenre(_jsonBook.getString("genre"));

            // AUTORE SINGOLO
            String name = _jsonBook.getString("name");
            String surname = _jsonBook.getString("surname");
            this.setAuthors(this.fetchAuthors(name, surname));
        } catch (JSONException e){
            e.printStackTrace();
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(List<Author> authors) {
        this.authors = authors;
    }

    /*public void setAuthor(String author) {
        this.authors.add(author);
    }*/

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String stato) {
        this.status = stato;
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

    public String getOriginalJson() {
        return originalJson;
    }

    public String toString(){
        String out = "";
        out = out + "ID: " + this.id + "\n";
        out = out + "Title: " + this.title + "\n";
        out = out + "Authors: ";
        for(Author author : this.authors){
            out = out + author.getName() + ", " + author.getSurname();
        }
        out = out + "\n";
        out = out + "Year: " + this.year + "\n";
        out = out + "Thumbnail: " + this.thumbnailUrl + "\n";
        out = out + "ISBN_10: " + this.isbn10 + "\n";
        out = out + "ISBN_13: " + this.isbn13 + "\n";
        return out;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(String _genreName) {
        //Recupero l'ID associato al genere e costruisco l'oggetto
        Genre genre = new Genre();
        //Leggo da hashmap, altrimenti chiamata a webapi
        if (GlobalConstants.genresMap != null)
            genre.setId(GlobalConstants.genresMap.getMap().get(_genreName));
        else
            genre.setId(BiblioValeApi.getGenreID(_genreName));

        if(genre.getId()<0)
            genre.setName("Not Found");
        else
            genre.setName(_genreName);

        this.genre = genre;
    }


    public Drawable fetchThumbnail(){
        Drawable thumbnail = null;

        try {
            thumbnail = new BookRepositoryDispatcher().fetchThumbnail(this);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        if (thumbnail == null)
            return null;
        return thumbnail;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String jsonSerialize(){
        return JSONHelper.bookSerialize(this);
    }

    private List<Author> fetchAuthors(String name, String surname) {
        List<Author> authorsList = new ArrayList<Author>();

        Author author = new Author(name, surname);
        authorsList.add(author);
        /* TODO: gestione più autori per libro
        String jsonAuthors = BiblioValeApi.getAuthors(name, surname);
        if(TextUtils.isEmpty(jsonAuthors))
            return authorsList;
        authorsList = JSONHelper.authorsListDeserialize(jsonAuthors);
        */
        return authorsList;
    }
}
