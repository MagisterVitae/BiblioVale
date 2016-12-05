package dev.sturmtruppen.bibliovale.businessLogic.DataFetchers;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.widget.ProgressBar;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import dev.sturmtruppen.bibliovale.businessLogic.BO.Book;
import dev.sturmtruppen.bibliovale.businessLogic.GoogleBooksUtils;

/**
 * Created by sturmtruppen on 30/04/16.
 */
public class GoogleBooksFetcher extends AsyncTask<String, String, Book> {

    private static final String googleKey = "AIzaSyAsz4PRSi2OIlRiu_uXZ-xW--PxEgC1X9E";
    private static final String baseUrl = "https://www.googleapis.com/books/v1/volumes?q=";

    @Override
    protected Book doInBackground(String... strings) {
        String isbn13 = strings[0];
        String isbn10 = strings[1];
        String title = strings[2];
        String autSurname = strings[3];
        String autName = strings[4];
        Book book;

        book = this.searchByIsbn(isbn13);
        if(book==null)
            book = this.searchByIsbn(isbn10);
        if(book==null)
            book = this.searchByTitleAndAuthor(title, autSurname, autName);
        return book;
    }


    @Override
    protected void onPostExecute(Book result){
        super.onPostExecute(result);
    }

    private Book searchByIsbn(String isbn){
        String completeUrl = baseUrl +
                "isbn:" + isbn +
                "&key=" + googleKey;
        if(!isbn.isEmpty())
            return bookSearcher(completeUrl);
        else
            return null;
    }

    private Book searchByTitleAndAuthor(String title, String autSurname, String autName) {
        String completeUrl = baseUrl +
                "title:" + title.replace(" ", "%20") +
                "&author:" + autSurname.replace(" ", "%20") + "%20" + autName.replace(" ", "%20") +
                "&key=" + googleKey;
        if(!title.isEmpty() && !autSurname.isEmpty() && !autName.isEmpty())
            return bookSearcher(completeUrl);
        else
            return null;
    }

    private Book bookSearcher(String completeUrl){
        String json = "";
        HttpURLConnection conn = null;
        BufferedReader br = null;
        InputStream is = null;
        try {
            URL url = new URL(completeUrl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            conn.connect();

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }

            br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));

            String output;
            while ((output = br.readLine()) != null) {
                //System.out.println(output);
                json = json + output;
            }

            Book book = GoogleBooksUtils.getBook(json);

            if (book == null)
                return null;

            is = (InputStream) new URL(book.getThumbnailUrl()).getContent();
            Drawable thumbnail = Drawable.createFromStream(is, book.getTitle());
            book.setThumbnail(thumbnail);

            return book;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if(is != null)
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            if(conn != null)
                conn.disconnect();
            if(br != null)
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
        return null;
    }


}
