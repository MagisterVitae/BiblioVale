package dev.sturmtruppen.bibliovale;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import dev.sturmtruppen.bibliovale.businessLogic.GlobalConstants;
import dev.sturmtruppen.bibliovale.businessLogic.Helpers.ActivityFlowHelper;
import dev.sturmtruppen.bibliovale.businessLogic.Helpers.PutExtraPair;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private Button btnSearch, btnNewBook, btnOpenConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Assegnamento handle oggetti visualizzati in activity
        btnSearch = (Button) findViewById(R.id.btnSearch);
        btnNewBook = (Button) findViewById(R.id.btnNewBook);
        btnOpenConfig = (Button) findViewById(R.id.btnOpenConfig);

        //Listener
        btnSearch.setOnClickListener(this);
        btnNewBook.setOnClickListener(this);
        btnOpenConfig.setOnClickListener(this);

        //Imposto variabili globali dalle sharedPreferences
        this.setGlobalVars();
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
            default:
                break;
        }
    }

    private void setGlobalVars(){
        // Leggiamo le Preferences
        SharedPreferences prefs = getSharedPreferences(GlobalConstants.CONFIG_PREFS, Context.MODE_PRIVATE);
        // Leggiamo l'informazione associata alla proprietà TEXT_DATA
        String cfgURL = prefs.getString(GlobalConstants.CONFIG_URL, "");
        if (cfgURL.isEmpty())
            new AlertDialog.Builder(this).setTitle("Errore").setMessage("Configurare URL BiblioVale!").setNeutralButton("Chiudi", null).show();
        // Lo imposto nella variabile globale
        GlobalConstants.webSiteUrl = cfgURL;
    }

    private void btnCreateNewBook(){
        List<PutExtraPair> putExtraList = new ArrayList<PutExtraPair>();
        putExtraList.add(new PutExtraPair(GlobalConstants.DETAILS_ACTIVITY_FLAVOUR, GlobalConstants.DETAILS_CREATE));
        ActivityFlowHelper.goToActivity(this, BookDetailActivity.class, putExtraList);
    }

}
