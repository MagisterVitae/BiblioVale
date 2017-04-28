package dev.sturmtruppen.bibliovale.dataLayer.bookRepositories;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import dev.sturmtruppen.bibliovale.businessLogic.bo.Book;
import dev.sturmtruppen.bibliovale.businessLogic.OPACUtils;

/**
 * Created by Matteo on 24/04/2017.
 */

public class OPACFetcher implements IBookRepositoryFetcher{

    private static final String baseUrl = "http://opac.sbn.it/opacmobilegw/search.json?";

    public OPACFetcher() {
    }

    /*
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
    */

    public Book getBookSync(String[] params){
        String isbn13 = params[0];
        String isbn10 = params[1];
        String title = params[2];
        String autSurname = params[3];
        String autName = params[4];
        Book book;

        book = this.searchByIsbn(isbn13);
        if(book==null)
            book = this.searchByIsbn(isbn10);
        if(book==null)
            book = this.searchByTitleAndAuthor(title, autSurname, autName);
        return book;
    }

    public String getJsonBookSync(String[] params){
        String isbn13 = params[0];
        String isbn10 = params[1];
        String title = params[2];
        String autSurname = params[3];
        String autName = params[4];
        String jsonBook = "";

        jsonBook = this.jsonSearchByIsbn(isbn13);
        if(jsonBook.isEmpty())
            jsonBook = this.jsonSearchByIsbn(isbn10);
        if(jsonBook.isEmpty())
            jsonBook = this.jsonSearchByTitleAndAuthor(title, autSurname, autName);
        return jsonBook;
    }

    public Book searchByIsbn(String isbn){
        String completeUrl = baseUrl +
                "isbn=" + isbn;
        if(!isbn.isEmpty())
            return bookSearcher(completeUrl);
        else
            return null;
    }

    public String jsonSearchByIsbn(String isbn){
        String completeUrl = baseUrl +
                "isbn:" + isbn;
        if(!isbn.isEmpty())
            return jsonBookSearcher(completeUrl);
        else
            return null;
    }

    public Book searchByTitleAndAuthor(String title, String autSurname, String autName) {
        String completeUrl = baseUrl +
                "any=" +
                title.replace(" ", "%20") +
                autSurname.replace(" ", "%20") + "%20" + autName.replace(" ", "%20");
        if(!title.isEmpty() && !autSurname.isEmpty() && !autName.isEmpty())
            return bookSearcher(completeUrl);
        else
            return null;
    }

    public String jsonSearchByTitleAndAuthor(String title, String autSurname, String autName) {
        String completeUrl = baseUrl +
                "any=" +
                title.replace(" ", "%20") +
                autSurname.replace(" ", "%20") + "%20" + autName.replace(" ", "%20");
        if(!title.isEmpty() && !autSurname.isEmpty() && !autName.isEmpty())
            return jsonBookSearcher(completeUrl);
        else
            return "";
    }

    public Book bookSearcher(String completeUrl){
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

            Book book = OPACUtils.getBook(json);

            if (book == null)
                return null;

            /*
            is = (InputStream) new URL(book.getThumbnailUrl()).getContent();
            Drawable thumbnail = Drawable.createFromStream(is, book.getTitle());
            book.setThumbnail(thumbnail);
            */

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

    public String jsonBookSearcher(String completeUrl){
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

            return json;

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
        return "";
    }
}
