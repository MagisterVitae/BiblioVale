package dev.sturmtruppen.bibliovale.businessLogic.DataFetchers;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import dev.sturmtruppen.bibliovale.businessLogic.Helpers.HttpConnectionHelper;

/**
 * Created by Matteo on 27/08/2016.
 */
public class BiblioValeBookFetcher extends AsyncTask<String, String, String> {

    @Override
    protected String doInBackground(String... params) {
        String json = "";
        String urlString = params[0];

        json = HttpConnectionHelper.getJsonData(urlString);

        return json;
    }
}
