package dev.sturmtruppen.bibliovale;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
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
import com.google.zxing.common.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import dev.sturmtruppen.bibliovale.businessLogic.BO.Author;
import dev.sturmtruppen.bibliovale.businessLogic.BO.Book;
import dev.sturmtruppen.bibliovale.businessLogic.DataFetchers.BookRepositoryDispatcher;
import dev.sturmtruppen.bibliovale.businessLogic.DataFetchers.GoogleBooksFetcher;
import dev.sturmtruppen.bibliovale.businessLogic.GlobalConstants;
import dev.sturmtruppen.bibliovale.businessLogic.Helpers.ActivityFlowHelper;
import dev.sturmtruppen.bibliovale.businessLogic.Helpers.AuthorsMap;
import dev.sturmtruppen.bibliovale.businessLogic.Helpers.GenresMap;
import dev.sturmtruppen.bibliovale.businessLogic.Helpers.PutExtraPair;


public class BarcodeScanActivity extends AppCompatActivity implements View.OnClickListener{


    private Button btnScan, btnTestBcode, btnSearch;
    private EditText txtBarcode, txtTitle, txtAuthors, txtYear;
    private ImageView imgThumbnail;

    private String barcode;
    private Book book;
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

        //BACKGROUND
        this.scanBarcode();
        //if(barcode != null && !barcode.isEmpty())
            //book = this.fetchBook();

    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
       // client.connect();
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
        //this.scanBarcode();
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
                scanBarcode();
                break;
            }
            case R.id.btnTestBcode:
            {
               // this.testFetch(txtBarcode.getText().toString());
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

    private void scanBarcode(){
        /*
        IntentIntegrator integrator = new IntentIntegrator((Activity)this);
        integrator.setBarcodeImageEnabled(true);
        integrator.setBeepEnabled(true);
        integrator.initiateScan(IntentIntegrator.ONE_D_CODE_TYPES);
        */
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        /*
        //retrieve scan result
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if(scanResult != null) {
            if(scanResult.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                //txtBarcode.setText(scanResult.getContents());
                //this.testFetch(scanResult.getContents());
                this.book = this.fetchBook(scanResult.getContents());
                if(book.getGenre() == null)
                    book.setGenre("Romanzo");
                if(book.getStatus() == null || book.getStatus().isEmpty())
                    book.setStatus("Non Letto");
                if(book.getNotes() == null || book.getNotes().isEmpty())
                    book.setNotes("");
                String jsonBook = this.book.jsonSerialize();
                /*
                List<PutExtraPair> putExtraList = new ArrayList<PutExtraPair>();
                putExtraList.add(new PutExtraPair(GlobalConstants.DETAILS_ACTIVITY_FLAVOUR, GlobalConstants.DETAILS_SHOW_UPDATE));
                putExtraList.add(new PutExtraPair(GlobalConstants.BOOK_KEY, jsonBook));
                ActivityFlowHelper.goToActivity(this, BookDetailActivity.class, putExtraList);
                *
                Intent returnIntent = new Intent();
                returnIntent.putExtra(GlobalConstants.BOOK_KEY, jsonBook);
                setResult(Activity.RESULT_OK,returnIntent);
                finish();
            }
        } else {
            // This is important, otherwise the result will not be passed to the fragment
            super.onActivityResult(requestCode, resultCode, intent);
        }
*/
    }

    private boolean checkConnectivity(Context context){
        ConnectivityManager connMgr = (ConnectivityManager)getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnected())
            return true;
        return false;
    }

    private Book fetchBook(String barcode){
        Book book = null;
        /*
        try {
            String[] params = {barcode, barcode, "", "", ""};
            book = new BookRepositoryDispatcher().execute(params).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        */
        return book;

    }

    /*
    private void testFetch(String barcode) {
        if(checkConnectivity(this)){
            try {

                Book book = new GoogleBooksFetcher().execute(barcode).get();
                if(book == null)
                    Toast.makeText(this, "Nessun libro trovato con questo barcode!", Toast.LENGTH_LONG).show();
                else {
                    String authors = "";
                    for (Author author : book.getAuthors()) {
                        authors += author.getName() + ", " + author.getSurname() + "; ";
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
    */

    /*
    private class FetchBookTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String[] googlePars = {params[0], params[0], "", "" , ""};
            String
        }

        @Override
        protected void onPreExecute(){
        }

        @Override
        protected void onProgressUpdate(Void[] values) {
        }

        @Override
        protected void onPostExecute(final String result) {
        }
    }
    */
}

