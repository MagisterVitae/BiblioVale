package dev.sturmtruppen.bibliovale;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private Button btnSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Assegnamento handle oggetti visualizzati in activity
        btnSearch = (Button) findViewById(R.id.btnSearch);

        btnSearch.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        //Individuo l'oggetto cliccato
        switch (v.getId()) {
            case R.id.btnSearch: {
                // definisco l'intenzione
                Intent openSearchActivity = new Intent(MainActivity.this, SearchActivity.class);
                // passo all'attivazione dell'activity
                startActivity(openSearchActivity);
            }
            default:
                break;
        }
    }
}
