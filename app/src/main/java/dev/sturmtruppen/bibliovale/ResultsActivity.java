package dev.sturmtruppen.bibliovale;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import dev.sturmtruppen.bibliovale.businessLogic.BO.Book;
import dev.sturmtruppen.bibliovale.businessLogic.GlobalConstants;
import dev.sturmtruppen.bibliovale.businessLogic.Helpers.ActivityFlowHelper;
import dev.sturmtruppen.bibliovale.businessLogic.Helpers.JSONHelper;
import dev.sturmtruppen.bibliovale.businessLogic.Helpers.PutExtraPair;

public class ResultsActivity extends AppCompatActivity {
    private ListView activityBookList;

    List<Book> books = new ArrayList<Book>();
    private ProgressBar progCircle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        //Assegnamento titolo
        String title = this.getIntent().getStringExtra(GlobalConstants.ACTIVITY_RESULTS_TITLE);
        if(title!= null && title!="")
            this.setTitle(title);

        //Assegnamento handle oggetti visualizzati in activity
        activityBookList = (ListView) findViewById(R.id.lstBooks);
        progCircle = (ProgressBar) findViewById(R.id.resultsProgCir);

        activityBookList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String bookTitle = ((TextView)view).getText().toString();

                String jsonBook = getSelectedBook(bookTitle).getOriginalJson();

                openDetailsActivity(jsonBook);
            }
        });

        //Proprietà
        progCircle.setVisibility(View.GONE);

        //Visualizza risultati
        this.showResults();
    }

    @Override
    public void onResume(){
        super.onResume();
        progCircle.setVisibility(View.GONE);
    }

    private void showResults(){
        // Recupero lista di libri
        String jsonBookList = this.getIntent().getStringExtra(GlobalConstants.BOOKLIST_KEY);
        // Conversione JSON in lista di libri
        books = JSONHelper.bookListDeserialize(jsonBookList);

        // Scrivo lista di libri su activity
        final ArrayList <String> listp = new ArrayList<String>();
        for (int i = 0; i < books.size(); ++i) {
            listp.add(books.get(i).getTitle());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listp);
        activityBookList.setAdapter(adapter);
    }

    @Nullable
    private Book getSelectedBook(String title){

        for (Book currBook : books){
            if (currBook.getTitle() == title)
                return currBook;
        }

        return null;
    }

    private void openDetailsActivity(String jsonBook){
        //Mostro progress circle
        progCircle.setVisibility(View.VISIBLE);

        List<PutExtraPair> putExtraList = new ArrayList<PutExtraPair>();
        putExtraList.add(new PutExtraPair(GlobalConstants.DETAILS_ACTIVITY_FLAVOUR, GlobalConstants.DETAILS_SHOW_UPDATE));
        putExtraList.add(new PutExtraPair(GlobalConstants.BOOK_KEY, jsonBook));
        ActivityFlowHelper.goToActivity(this, BookDetailActivity.class, putExtraList);
    }

}
