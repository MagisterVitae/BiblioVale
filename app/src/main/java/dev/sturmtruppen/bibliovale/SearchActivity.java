package dev.sturmtruppen.bibliovale;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import java.util.List;

import dev.sturmtruppen.bibliovale.businessLogic.GlobalConstants;
import dev.sturmtruppen.bibliovale.businessLogic.BO.Book;
import dev.sturmtruppen.bibliovale.businessLogic.BiblioValeApi;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btnSearch;
    private EditText txtAuthName, txtAuthSurname, txtTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        //Assegnamento handle oggetti visualizzati in activity
        btnSearch = (Button) findViewById(R.id.btnSearch);
        txtAuthName = (EditText) findViewById(R.id.txtAuthName);
        txtAuthSurname = (EditText) findViewById(R.id.txtAuthSurname);
        txtTitle = (EditText) findViewById(R.id.txtTitle);

        btnSearch.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        //Individuo l'oggetto cliccato
        switch (v.getId()) {
            case R.id.btnSearch: {
                this.btnSearchLogic();
            }
            default:
                break;
        }
    }

    private void btnSearchLogic(){
        //Recupero lista libri
        String jsonBookList = BiblioValeApi.getBookList(txtAuthSurname.getText().toString(),
                                                        txtAuthName.getText().toString(),
                                                        txtTitle.getText().toString());
        // definisco l'intent
        Intent openResultsActivity = new Intent(SearchActivity.this, ResultsActivity.class);
        openResultsActivity.putExtra(GlobalConstants.BOOKLIST_KEY, jsonBookList);
        // passo all'attivazione dell'activity
        startActivity(openResultsActivity);
    }
}
