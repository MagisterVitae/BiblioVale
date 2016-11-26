package dev.sturmtruppen.bibliovale.businessLogic.Helpers;

import android.graphics.drawable.Drawable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import dev.sturmtruppen.bibliovale.businessLogic.BO.Book;

/**
 * Created by Matteo on 25/07/2016.
 */
public final class HttpConnectionHelper {

    private static HttpURLConnection conn = null;
    private static BufferedReader br = null;


    //public HttpConnectionHelper(){};

    public static String getJsonData(String urlString){
        String jsonData = "";

        try{
            URL url = new URL(urlString);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            conn.connect();

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }

            // Fetch JSON string
            br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));

            String output;
            while ((output = br.readLine()) != null) {
                jsonData = jsonData + output;
            }
        } catch (MalformedURLException e){
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }

        return jsonData;
    }

}
