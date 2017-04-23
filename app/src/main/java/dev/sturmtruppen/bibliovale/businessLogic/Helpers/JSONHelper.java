package dev.sturmtruppen.bibliovale.businessLogic.Helpers;

import android.util.Pair;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
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

    public static String bookSerialize(Book book){
        JSONObject jsonBook = new JSONObject();

        try {
            jsonBook.put("id", book.getId());
            jsonBook.put("title", book.getTitle());
            jsonBook.put("surname", book.getAuthors().get(0).getSurname());
            jsonBook.put("name", book.getAuthors().get(0).getName());
            jsonBook.put("year", book.getYear());
            jsonBook.put("genre", book.getGenre().getName());
            jsonBook.put("isbn_10", book.getIsbn10());
            jsonBook.put("isbn_13", book.getIsbn13());
            jsonBook.put("status", book.getStatus());
            jsonBook.put("notes", book.getNotes());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonBook.toString();
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

    // Deserializzazione di generiche coppie chiave-valore
    public static List<Pair<String, String>> pairListDeserialize(String stringPairs){
        List<Pair<String,String>> pairList = new ArrayList<>();
        try {
            JSONArray jsonPairs;
            jsonPairs = new JSONArray(stringPairs);
            if (jsonPairs != null){
                for (int i=0; i<jsonPairs.length(); i++){
                    JSONObject jsonPair = jsonPairs.getJSONObject(i);
                    int j = 0;
                    String key = "", value = "";
                    for(Iterator<String> iter = jsonPair.keys(); iter.hasNext();) {
                        String jsonKey = iter.next();
                        String jsonValue = "";
                        try {
                            jsonValue = (String) jsonPair.get(jsonKey);
                        } catch (JSONException e) {
                            // Something went wrong!
                        }
                        if(j==0)
                            key = jsonValue;
                        else
                            value = jsonValue;
                        j++;
                    }
                    Pair<String, String> pair = new Pair<>(key, value);
                    pairList.add(pair);
                }
            }
        } catch (JSONException e){
            e.printStackTrace();
        }
        return pairList;
    }
}
