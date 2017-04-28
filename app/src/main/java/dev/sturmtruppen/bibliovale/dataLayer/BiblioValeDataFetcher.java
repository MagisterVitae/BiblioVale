package dev.sturmtruppen.bibliovale.dataLayer;

import android.os.AsyncTask;

import dev.sturmtruppen.bibliovale.businessLogic.helpers.HttpConnectionHelper;

/**
 * Created by Matteo on 27/08/2016.
 */
public class BiblioValeDataFetcher extends AsyncTask<String, String, String> {

    @Override
    protected String doInBackground(String... params) {
        String json = "";
        String urlString = params[0];

        json = HttpConnectionHelper.getJsonData(urlString);

        return json;
    }

    public String getDataSync(String... params){
        String json = "";
        String urlString = params[0];

        json = HttpConnectionHelper.getJsonData(urlString);

        return json;
    }
}
