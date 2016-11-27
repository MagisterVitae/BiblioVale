package dev.sturmtruppen.bibliovale.businessLogic;

import android.net.Uri;
import android.text.TextUtils;

import java.util.concurrent.ExecutionException;

import dev.sturmtruppen.bibliovale.businessLogic.DataFetchers.BiblioValeDataFetcher;

/**
 * Created by Matteo on 25/07/2016.
 */
public class BiblioValeApi {

    private static final String URL = "http://192.168.1.127/bibliovale/BiblioValeApi.php?"; //TEST, da convertire in configurazione
    // Elenco API REST PHP esposte
    private enum F_NAMES {getBook, getAuthors, getIsbn, createBook, createAuthor, getGenreID, getStatusID, getAllGenres, getAllAuthors}

    public static String getBookList(String _surname, String _name, String _title){
        String jsonBookList = "";

        // Preparazione url per chiamata REST
        String urlString = Uri.parse(URL).buildUpon()
                .appendQueryParameter("fName", F_NAMES.getBook.name())
                .appendQueryParameter("surname", _surname.isEmpty() ? "" : _surname)
                .appendQueryParameter("name", _name.isEmpty() ? "" : _name)
                .appendQueryParameter("title", _title.isEmpty() ? "" : _title)
                .build().toString();

        // Fetch book list
        try {
            jsonBookList = new BiblioValeDataFetcher().execute(urlString).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return jsonBookList;
    }

    public static String getAllGenres(){
        String jsonGenresList = "";

        // Preparazione url per chiamata REST
        String urlString = Uri.parse(URL).buildUpon()
                .appendQueryParameter("fName", F_NAMES.getAllGenres.name())
                .build().toString();

        // Fetch lista completa generi
        try {
            jsonGenresList = new BiblioValeDataFetcher().execute(urlString).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return jsonGenresList;
    }

    public static int getGenreID(String _genreName){
        String genreID = "";

        // Preparazione url per chiamata REST
        String urlString = Uri.parse(URL).buildUpon()
                .appendQueryParameter("fName", F_NAMES.getGenreID.name())
                .appendQueryParameter("genName", _genreName.isEmpty() ? "" : _genreName)
                .build().toString();

        // Fetch lista completa generi
        try {
            genreID = new BiblioValeDataFetcher().execute(urlString).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        if (TextUtils.isEmpty(genreID))
            return -1;
        else {
            return Integer.parseInt(genreID);
        }
    }

    public static String getAllAuthors(){
        String jsonAuthorsList = "";

        // Preparazione url per chiamata REST
        String urlString = Uri.parse(URL).buildUpon()
                .appendQueryParameter("fName", F_NAMES.getAllAuthors.name())
                .build().toString();

        // Fetch lista completa generi
        try {
            jsonAuthorsList = new BiblioValeDataFetcher().execute(urlString).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return jsonAuthorsList;
    }

    public static String getAuthors(String _name, String _surname){
        String jsonAuthorsList = "";

        // Preparazione url per chiamata REST
        String urlString = Uri.parse(URL).buildUpon()
                .appendQueryParameter("fName", F_NAMES.getAuthors.name())
                .appendQueryParameter("name", _name.isEmpty() ? "" : _name)
                .appendQueryParameter("surname", _surname.isEmpty() ? "" : _surname)
                .build().toString();

        // Fetch lista completa generi
        try {
            jsonAuthorsList = new BiblioValeDataFetcher().execute(urlString).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return jsonAuthorsList;
    }
}
