package dev.sturmtruppen.bibliovale;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import dev.sturmtruppen.bibliovale.businessLogic.BiblioValeApi;
import dev.sturmtruppen.bibliovale.businessLogic.GlobalConstants;
import dev.sturmtruppen.bibliovale.businessLogic.Helpers.ActivityFlowHelper;
import dev.sturmtruppen.bibliovale.businessLogic.Helpers.AuthorsMap;
import dev.sturmtruppen.bibliovale.businessLogic.Helpers.GenresMap;
import dev.sturmtruppen.bibliovale.businessLogic.Helpers.HttpConnectionHelper;
import dev.sturmtruppen.bibliovale.businessLogic.Helpers.PutExtraPair;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btnSearch, btnNewBook, btnOpenConfig, btnWishList, btnStats;
    private ProgressBar progCircle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Assegnamento handle oggetti visualizzati in activity
        btnSearch = (Button) findViewById(R.id.btnSearch);
        btnNewBook = (Button) findViewById(R.id.btnNewBook);
        btnOpenConfig = (Button) findViewById(R.id.btnOpenConfig);
        btnWishList = (Button) findViewById(R.id.btnWishList);
        btnStats = (Button) findViewById(R.id.btnStats);
        progCircle = (ProgressBar) findViewById(R.id.mainProgCir);

        //Listener
        btnSearch.setOnClickListener(this);
        btnNewBook.setOnClickListener(this);
        btnOpenConfig.setOnClickListener(this);
        btnWishList.setOnClickListener(this);
        btnStats.setOnClickListener(this);

        //Imposto variabili globali dalle sharedPreferences
        this.setGlobalVars();

        //Verifico connettività
        if(this.checkConnectivity()){
            //Caricamento dati con task asincrono
            LoadDataTask loadDataTask = new LoadDataTask();
            loadDataTask.execute();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        progCircle.setVisibility(View.GONE);
    }

    private boolean checkConnectivity() {
        if (!HttpConnectionHelper.checkConnectivity(this)) {
            Toast.makeText(this, "Attivare connessione ad internet", Toast.LENGTH_LONG).show();
            btnNewBook.setEnabled(false);
            btnSearch.setEnabled(false);
            btnStats.setEnabled(false);
            btnWishList.setEnabled(false);
            return false;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        //Individuo l'oggetto cliccato
        switch (v.getId()) {
            case R.id.btnSearch: {
                ActivityFlowHelper.goToActivity(this, SearchActivity.class);
                break;
            }
            case R.id.btnNewBook: {
                btnCreateNewBook();
                break;
            }
            case R.id.btnOpenConfig: {
                ActivityFlowHelper.goToActivity(this, ConfigurationActivity.class);
                break;
            }
            case R.id.btnWishList: {
                btnWishListLogic();
                break;
            }
            case R.id.btnStats: {
                ActivityFlowHelper.goToActivity(this, StatsActivity.class);
                break;
            }
            default:
                break;
        }
    }

    private void setGlobalVars() {
        // Leggiamo le Preferences
        SharedPreferences prefs = getSharedPreferences(GlobalConstants.CONFIG_PREFS, Context.MODE_PRIVATE);
        // Leggiamo l'informazione associata alla proprietà TEXT_DATA
        String cfgURL = prefs.getString(GlobalConstants.CONFIG_URL, "");
        if (cfgURL.isEmpty())
            new AlertDialog.Builder(this).setTitle("Errore").setMessage("Configurare URL BiblioVale!").setNeutralButton("Chiudi", null).show();
        // Lo imposto nella variabile globale
        GlobalConstants.webSiteUrl = cfgURL;
    }

    private void btnCreateNewBook() {
        List<PutExtraPair> putExtraList = new ArrayList<PutExtraPair>();
        putExtraList.add(new PutExtraPair(GlobalConstants.DETAILS_ACTIVITY_FLAVOUR, GlobalConstants.DETAILS_CREATE));
        ActivityFlowHelper.goToActivity(this, BookDetailActivity.class, putExtraList);
    }

    private void btnWishListLogic() {
        //Recupero lista libri
        String jsonBookList = BiblioValeApi.getWishList();

        List<PutExtraPair> putExtraList = new ArrayList<PutExtraPair>();
        putExtraList.add(new PutExtraPair(GlobalConstants.BOOKLIST_KEY, jsonBookList));
        ActivityFlowHelper.goToActivity(this, ResultsActivity.class, putExtraList);
    }

    private class LoadDataTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            //Creo hashmap globale dei generi dei libri
            GlobalConstants.genresMap = new GenresMap();
            //Creo lista globale degli autori
            GlobalConstants.authorsMap = new AuthorsMap();
            return true;
        }

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            //progCircle.setVisibility(View.VISIBLE);
            btnNewBook.setEnabled(false);
            btnSearch.setEnabled(false);
            btnStats.setEnabled(false);
            btnWishList.setEnabled(false);
        }

        @Override
        protected void onProgressUpdate(Void[] values) {

        };

        @Override
        protected void onPostExecute(final Boolean result) {
            super.onPostExecute(result);
            //progCircle.setVisibility(View.GONE);
            btnNewBook.setEnabled(true);
            btnSearch.setEnabled(true);
            btnStats.setEnabled(true);
            btnWishList.setEnabled(true);
        }
    }

}
