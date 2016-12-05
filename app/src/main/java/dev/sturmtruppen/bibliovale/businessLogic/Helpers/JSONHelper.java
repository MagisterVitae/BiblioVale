package dev.sturmtruppen.bibliovale.businessLogic.Helpers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import dev.sturmtruppen.bibliovale.businessLogic.BO.Author;
import dev.sturmtruppen.bibliovale.businessLogic.BO.Book;
import dev.sturmtruppen.bibliovale.businessLogic.BO.Genre;
import dev.sturmtruppen.bibliovale.businessLogic.DataFetchers.DBApiResponse;

/**
 * Created by Matteo on 27/08/2016.
 */
public final class JSONHelper {

    public static List<Book> bookListDeserialize(String jsonList){
        List<Book> books = new ArrayList<Book>();

        try {
            JSONArray jsonBooks;
            jsonBooks = new JSONArray(jsonList);
            if (jsonBooks != null){
                for (int i=0; i<jsonBooks.length(); i++){
                    JSONObject jsonBook = jsonBooks.getJSONObject(i);
                    Book book = new Book(jsonBook);
                    books.add(book);
                }
            }
        } catch (JSONException e){
            e.printStackTrace();
        }

        return books;
    }

    public static Book bookDeserialize(String strBook){
        JSONObject jsonBook = null;
        try {
            jsonBook = new JSONObject(strBook);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Book book = new Book(jsonBook);

        return book;
    }

    public static List<Genre> genresListDeserialize(String jsonList){
        List<Genre> genres = new ArrayList<Genre>();

        try {
            JSONArray jsonGenres;
            jsonGenres = new JSONArray(jsonList);
            if (jsonGenres != null){
                for (int i=0; i<jsonGenres.length(); i++){
                    JSONObject jsonGenre = jsonGenres.getJSONObject(i);
                    Genre genre = new Genre(jsonGenre);
                    genres.add(genre);
                }
            }
        } catch (JSONException e){
            e.printStackTrace();
        }

        return genres;
    }

    public static List<Author> authorsListDeserialize(String jsonList){
        List<Author> authors = new ArrayList<Author>();

        try {
            JSONArray jsonAuthors;
            jsonAuthors = new JSONArray(jsonList);
            if (jsonAuthors != null){
                for (int i=0; i<jsonAuthors.length(); i++){
                    JSONObject jsonAuthor = jsonAuthors.getJSONObject(i);
                    Author author = new Author(jsonAuthor);
                    authors.add(author);
                }
            }
        } catch (JSONException e){
            e.printStackTrace();
        }

        return authors;
    }

    public static DBApiResponse dbApiResponseDeserialize(String strResponse){
       JSONObject jsonResponse = null;
        try {
            jsonResponse = new JSONObject(strResponse);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        DBApiResponse response = new DBApiResponse(jsonResponse);
        return response;
    }
}
