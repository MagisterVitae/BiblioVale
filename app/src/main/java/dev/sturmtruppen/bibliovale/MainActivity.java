package dev.sturmtruppen.bibliovale;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import dev.sturmtruppen.bibliovale.businessLogic.helpers.AasyncActivity;
import dev.sturmtruppen.bibliovale.businessLogic.helpers.AsyncHelper;
import dev.sturmtruppen.bibliovale.businessLogic.GlobalConstants;
import dev.sturmtruppen.bibliovale.businessLogic.helpers.ActivityFlowHelper;
import dev.sturmtruppen.bibliovale.businessLogic.helpers.AuthorsMap;
import dev.sturmtruppen.bibliovale.businessLogic.helpers.GenresMap;
import dev.sturmtruppen.bibliovale.businessLogic.helpers.HttpConnectionHelper;
import dev.sturmtruppen.bibliovale.businessLogic.helpers.PutExtraPair;
import dev.sturmtruppen.bibliovale.dataLayer.BiblioValeApi;

public class MainActivity extends AasyncActivity implements View.OnClickListener {
    private static final String LOAD_DATA_TASK = "LOAD_DATA_TASK";
    private static final String WISHLIST_TASK = "WISHLIST_TASK";

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
            loadDataTaskPre();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        progCircle.setVisibility(View.GONE);
    }


    @Override
    public void onBackPressed()
    {
        this.finishAffinity();
        this.finish();
        this.moveTaskToBack(true);
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
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

    @Override
    public List<Object> doBackgroundWork(String taskName) {
        switch (taskName){
            case LOAD_DATA_TASK:{
                return loadDataTaskBackground();
            }
            case WISHLIST_TASK:{
                return wishListTaskBackground();
            }
            default: return null;
        }
    }

    @Override
    public void onAsyncCallBack(List<Object> data) {
        String taskName = (String) data.get(0);
        List<Object> tail = data.subList(1, data.size());

        switch (taskName){
            case LOAD_DATA_TASK:{
                loadDataTaskPost(tail);
                break;
            }
            case WISHLIST_TASK:{
                wishListTaskPost(tail);
                break;
            }
            default: break;
        }
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
        wishListTaskPre();
    }

    /**
     * AsyncTaskHelper framework
     */
    private void loadDataTaskPre(){
        //Pre-execute
        btnNewBook.setEnabled(false);
        btnSearch.setEnabled(false);
        btnStats.setEnabled(false);
        btnWishList.setEnabled(false);
        //Background
        AsyncHelper asyncHelper = new AsyncHelper(LOAD_DATA_TASK, this, progCircle);
        asyncHelper.execute();
    }

    private List<Object> loadDataTaskBackground(){
        List<Object> result = new ArrayList<Object>();

        //Creo hashmap globale dei generi dei libri
        GlobalConstants.genresMap = new GenresMap();
        //Creo lista globale degli autori
        GlobalConstants.authorsMap = new AuthorsMap();

        result.add(true);
        return result;
    }

    private void loadDataTaskPost(List<Object> results){
        Boolean res = (Boolean) results.get(0);

        btnNewBook.setEnabled(true);
        btnSearch.setEnabled(true);
        btnStats.setEnabled(true);
        btnWishList.setEnabled(true);
    }

    private void wishListTaskPre(){
        //Background
        AsyncHelper asyncHelper = new AsyncHelper(WISHLIST_TASK, this, progCircle);
        asyncHelper.execute();
    }

    private List<Object> wishListTaskBackground(){
        List<Object> result = new ArrayList<Object>();

        String jsonBookList = BiblioValeApi.getWishList(true);
        result.add(jsonBookList);

        return result;
    }

    private void wishListTaskPost(List<Object> results){
        String jsonBookList = (String) results.get(0);
        List<PutExtraPair> putExtraList = new ArrayList<PutExtraPair>();
        putExtraList.add(new PutExtraPair(GlobalConstants.BOOKLIST_KEY, jsonBookList));
        ActivityFlowHelper.goToActivity(this, ResultsActivity.class, putExtraList);
    }

}
