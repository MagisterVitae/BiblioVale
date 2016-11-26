package dev.sturmtruppen.bibliovale;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.concurrent.ExecutionException;

import dev.sturmtruppen.bibliovale.businessLogic.BO.Book;
import dev.sturmtruppen.bibliovale.businessLogic.DataFetchers.GoogleBooksFetcher;


public class BarcodeScanActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btnScan, btnTestBcode, btnSearch;
    private EditText txtBarcode, txtTitle, txtAuthors, txtYear;
    private ImageView imgThumbnail;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_scan);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        //Assegnamento handle oggetti visualizzati in activity
        btnScan = (Button) findViewById(R.id.btnScan);
        btnTestBcode = (Button) findViewById(R.id.btnTestBcode);
        btnSearch = (Button) findViewById(R.id.btnSearch);
        txtBarcode = (EditText) findViewById(R.id.txtBarcode);
        txtTitle = (EditText) findViewById(R.id.txtTitle);
       // txtAuthors = (EditText) findViewById(R.id.lblAuthors);
       // txtYear = (EditText) findViewById(R.id.lblYear);
        imgThumbnail = (ImageView) findViewById(R.id.imgThumbnail);

        btnScan.setOnClickListener(this);
        btnTestBcode.setOnClickListener(this);
        btnSearch.setOnClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "BarcodeScan Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://dev.sturmtruppen.bibliovale/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "BarcodeScan Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://dev.sturmtruppen.bibliovale/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    @Override
    public void onClick(View view) {
        //Individuo quale oggetto è stato cliccato
        switch (view.getId()){
            case R.id.btnScan:
            {
                IntentIntegrator integrator = new IntentIntegrator((Activity)this);
                integrator.setBarcodeImageEnabled(true);
                integrator.setBeepEnabled(true);
                integrator.initiateScan(IntentIntegrator.ONE_D_CODE_TYPES);
                break;
            }
            case R.id.btnTestBcode:
            {
                this.testFetch(txtBarcode.getText().toString());
                break;
            }
            case R.id.btnSearch:
            {
                // definisco l'intenzione
                Intent openResultsActivity = new Intent(BarcodeScanActivity.this, ResultsActivity.class);
                // passo all'attivazione dell'activity Pagina.java
                startActivity(openResultsActivity);
            }
            default:break;
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        //retrieve scan result
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if(scanResult != null) {
            if(scanResult.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                txtBarcode.setText(scanResult.getContents());
                this.testFetch(scanResult.getContents());
            }
        } else {
            // This is important, otherwise the result will not be passed to the fragment
            super.onActivityResult(requestCode, resultCode, intent);
        }

    }

    private void testFetch(String barcode) {
        if(checkConnectivity(this)){
            try {
                Book book = new GoogleBooksFetcher().execute(barcode).get();
                if(book == null)
                    Toast.makeText(this, "Nessun libro trovato con questo barcode!", Toast.LENGTH_LONG).show();
                else {
                    String authors = "";
                    for (String author : book.getAuthors()) {
                        authors = authors + author + ", ";
                    }
                    txtTitle.setText(book.getTitle());
                    txtYear.setText(book.getYear());
                    txtAuthors.setText(authors);
                    imgThumbnail.setImageDrawable(book.getThumbnail());
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        else
            Toast.makeText(this, "Connessione ad Internet non disponibile!", Toast.LENGTH_LONG).show();

    }

    private boolean checkConnectivity(Context context){
        ConnectivityManager connMgr = (ConnectivityManager)getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnected())
            return true;
        return false;
    }
}

