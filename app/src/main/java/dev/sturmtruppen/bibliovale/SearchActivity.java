package dev.sturmtruppen.bibliovale;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import dev.sturmtruppen.bibliovale.businessLogic.GlobalConstants;
import dev.sturmtruppen.bibliovale.businessLogic.BO.Book;
import dev.sturmtruppen.bibliovale.businessLogic.BiblioValeApi;
import dev.sturmtruppen.bibliovale.businessLogic.Helpers.ActivityFlowHelper;
import dev.sturmtruppen.bibliovale.businessLogic.Helpers.PutExtraPair;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btnSearch;
    private EditText txtAuthName, txtAuthSurname, txtTitle;
    private RelativeLayout relLayout;
    private ProgressBar progCircle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        //Assegnamento handle oggetti visualizzati in activity
        relLayout = (RelativeLayout) findViewById(R.id.searchRelLayout);

        btnSearch = (Button) findViewById(R.id.btnSearch);
        txtAuthName = (EditText) findViewById(R.id.txtAuthName);
        txtAuthSurname = (EditText) findViewById(R.id.txtAuthSurname);
        txtTitle = (EditText) findViewById(R.id.txtTitle);
        progCircle = (ProgressBar) findViewById(R.id.searchProgCir);

        //Listener
        btnSearch.setOnClickListener(this);
        relLayout.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View view, MotionEvent ev)
            {
                hideKeyboard(view);
                return false;
            }
        });

        //Proprietà
        progCircle.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        //Individuo l'oggetto cliccato
        switch (v.getId()) {
            case R.id.btnSearch: {
                //Mostro progress circle
                progCircle.setVisibility(View.VISIBLE);
                this.btnSearchLogic();
                //Nascondo progress circle
                progCircle.setVisibility(View.GONE);
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

        List<PutExtraPair> putExtraList = new ArrayList<PutExtraPair>();
        putExtraList.add(new PutExtraPair(GlobalConstants.BOOKLIST_KEY, jsonBookList));
        ActivityFlowHelper.goToActivity(this, ResultsActivity.class, putExtraList);
    }

    private void hideKeyboard(View view)
    {
        InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

}
