package dev.sturmtruppen.bibliovale;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import dev.sturmtruppen.bibliovale.businessLogic.bo.Book;
import dev.sturmtruppen.bibliovale.businessLogic.GlobalConstants;
import dev.sturmtruppen.bibliovale.businessLogic.helpers.AasyncActivity;
import dev.sturmtruppen.bibliovale.businessLogic.helpers.ActivityFlowHelper;
import dev.sturmtruppen.bibliovale.businessLogic.helpers.AsyncHelper;
import dev.sturmtruppen.bibliovale.businessLogic.helpers.AuthorsMap;
import dev.sturmtruppen.bibliovale.businessLogic.helpers.GenresMap;
import dev.sturmtruppen.bibliovale.businessLogic.helpers.JSONHelper;
import dev.sturmtruppen.bibliovale.businessLogic.helpers.PutExtraPair;
import dev.sturmtruppen.bibliovale.presentationLogic.ResultsListAdapter;

public class ResultsActivity extends AasyncActivity {
    private static final String DESERIALIZE_TASK = "DESERIALIZE_TASK";

    private ListView activityBookList;

    List<Book> books = new ArrayList<Book>();
    private ProgressBar progCircle;
    private String jsonBookList;

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
                String bookTitle = ((TextView)view.findViewById(R.id.txtRowTitle)).getText().toString();

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

    @Override
    public List<Object> doBackgroundWork(String taskName) {
        switch (taskName){
            case DESERIALIZE_TASK:{
                return deserializeTaskBackground();
            }
            default: return null;
        }
    }

    @Override
    public void onAsyncCallBack(List<Object> data) {
        String taskName = (String) data.get(0);
        List<Object> tail = data.subList(1, data.size());

        switch (taskName){
            case DESERIALIZE_TASK:{
                deserializeTaskPost(tail);
                break;
            }
            default: break;
        }
    }

    private void showResults(){
        // Recupero lista di libri
        jsonBookList = this.getIntent().getStringExtra(GlobalConstants.BOOKLIST_KEY);
        // Deserializza in Asynctask e mostra sull'activity
        deserializeTaskPre();
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
        List<PutExtraPair> putExtraList = new ArrayList<PutExtraPair>();
        putExtraList.add(new PutExtraPair(GlobalConstants.DETAILS_ACTIVITY_FLAVOUR, GlobalConstants.DETAILS_SHOW_UPDATE));
        putExtraList.add(new PutExtraPair(GlobalConstants.BOOK_KEY, jsonBook));
        ActivityFlowHelper.goToActivity(this, BookDetailActivity.class, putExtraList);
    }

    /**
     * AsyncTaskHelper Framework
     */
    private void deserializeTaskPre(){
        //Background
        AsyncHelper asyncHelper = new AsyncHelper(DESERIALIZE_TASK, this, progCircle);
        asyncHelper.execute();
    }

    private List<Object> deserializeTaskBackground(){
        List<Object> result = new ArrayList<Object>();

        // Conversione JSON in lista di libri
        books = JSONHelper.bookListDeserialize(jsonBookList);

        result.add(books);
        return result;
    }

    private void deserializeTaskPost(List<Object> results){
        List<Book> books = (ArrayList<Book>) results.get(0);

        // Scrivo lista di libri su activity
        final ArrayList <Book> bookList = new ArrayList<Book>();
        for (int i = 0; i < books.size(); ++i) {
            bookList.add(books.get(i));
        }
        activityBookList.setAdapter(new ResultsListAdapter(ResultsActivity.this, bookList));
    }

}
