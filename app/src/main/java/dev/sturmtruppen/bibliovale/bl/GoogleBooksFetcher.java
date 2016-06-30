package dev.sturmtruppen.bibliovale.bl;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by sturmtruppen on 30/04/16.
 */
public class GoogleBooksFetcher extends AsyncTask<String, String, Book> {

    private static final String googleKey = "AIzaSyAsz4PRSi2OIlRiu_uXZ-xW--PxEgC1X9E";
    private static final String baseUrl = "https://www.googleapis.com/books/v1/volumes?q=isbn:";

    @Override
    protected Book doInBackground(String... strings) {
        String json = "";
        String isbn = strings[0];
        HttpURLConnection conn = null;
        BufferedReader br = null;
        InputStream is = null;

        try {
            String completeUrl = baseUrl + isbn + "&key=" + googleKey;
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

    @Override
    protected void onPostExecute(Book result){
        super.onPostExecute(result);
    }
}
