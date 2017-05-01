package dev.sturmtruppen.bibliovale;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import dev.sturmtruppen.bibliovale.businessLogic.GlobalConstants;
import dev.sturmtruppen.bibliovale.businessLogic.bo.Author;
import dev.sturmtruppen.bibliovale.businessLogic.bo.Book;
import dev.sturmtruppen.bibliovale.businessLogic.helpers.AasyncActivity;
import dev.sturmtruppen.bibliovale.businessLogic.helpers.AsyncHelper;
import dev.sturmtruppen.bibliovale.businessLogic.helpers.AuthorsMap;
import dev.sturmtruppen.bibliovale.businessLogic.helpers.GenresMap;
import dev.sturmtruppen.bibliovale.businessLogic.helpers.JSONHelper;
import dev.sturmtruppen.bibliovale.dataLayer.BiblioValeApi;
import dev.sturmtruppen.bibliovale.businessLogic.helpers.ActivityFlowHelper;
import dev.sturmtruppen.bibliovale.businessLogic.helpers.HttpConnectionHelper;
import dev.sturmtruppen.bibliovale.businessLogic.helpers.PutExtraPair;

public class SearchActivity extends AasyncActivity implements View.OnClickListener{
    private static final String LOAD_BOOKS_TASK = "LOAD_BOOKS_TASK";
    private static final String SEARCH_BOOKS_TASK = "SEARCH_BOOKS_TASK";

    private Button btnSearch;
    private AutoCompleteTextView acSearchAuthor, acSearchTitle;
    private RelativeLayout relLayout;
    private ProgressBar progCircle;

    String[] authorsArray;
    String[] titlesArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        //Assegnamento handle oggetti visualizzati in activity
        relLayout = (RelativeLayout) findViewById(R.id.searchRelLayout);

        btnSearch = (Button) findViewById(R.id.btnSearch);
        acSearchAuthor = (AutoCompleteTextView) findViewById(R.id.acSearchAuthor);
        acSearchTitle = (AutoCompleteTextView) findViewById(R.id.acSearchTitle);
        progCircle = (ProgressBar) findViewById(R.id.searchProgCir);

        //Adapter autocompletamento
        authorsArray = fetchAuthorsArray();
        ArrayAdapter<String> authorsListAdapter= new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line, authorsArray);
        acSearchAuthor.setAdapter(authorsListAdapter);
        acSearchAuthor.setValidator(new AutoCompleteTextView.Validator() {
            @Override
            public boolean isValid(CharSequence text) {
                Arrays.sort(authorsArray);
                if (Arrays.binarySearch(authorsArray, text.toString()) > 0) {
                    loadBooksTaskPre();
                    return true;
                }
                return false;
            }

            @Override
            public CharSequence fixText(CharSequence invalidText) {
                showToast("Autore inesistente", Toast.LENGTH_SHORT);
                return null;
            }
        });

        loadBooksTaskPre();
        ArrayAdapter<String> titlesListAdapter= new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line, titlesArray);
        acSearchTitle.setAdapter(titlesListAdapter);
        acSearchTitle.setValidator(new AutoCompleteTextView.Validator() {
            @Override
            public boolean isValid(CharSequence text) {
                Arrays.sort(titlesArray);
                if (Arrays.binarySearch(titlesArray, text.toString()) > 0) {
                    return true;
                }
                return false;
            }

            @Override
            public CharSequence fixText(CharSequence invalidText) {
                //showToast("Libro inesistente", Toast.LENGTH_SHORT);
                return null;
            }
        });

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

        //Verifico connettività
        this.checkConnectivity();
    }

    @Override
    public void onClick(View v) {
        //Individuo l'oggetto cliccato
        switch (v.getId()) {
            case R.id.btnSearch: {
                this.btnSearchLogic();
            }
            case R.id.acSearchAuthor: {
                this.acSearchAuthor.showDropDown();
                break;
            }
            case R.id.acSearchTitle: {
                if(acSearchAuthor.getText().toString().isEmpty()) {
                    titlesArray = new String[]{""};
                    ArrayAdapter<String> titlesListAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, titlesArray);
                    acSearchTitle.setAdapter(titlesListAdapter);
                }
                this.acSearchTitle.showDropDown();
                break;
            }
            default:
                break;
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        progCircle.setVisibility(View.GONE);
    }

    @Override
    public List<Object> doBackgroundWork(String taskName) {
        switch (taskName){
            case LOAD_BOOKS_TASK: {
                return loadBooksTaskBackground();
            }
            case SEARCH_BOOKS_TASK: {
                return searchBooksTaskBackground();
            }
            default:
                return null;
        }
    }

    @Override
    public void onAsyncCallBack(List<Object> data) {
        String taskName = (String) data.get(0);
        List<Object> tail = data.subList(1, data.size());

        switch (taskName){
            case LOAD_BOOKS_TASK:{
                loadBooksTaskPost(tail);
                break;
            }
            case SEARCH_BOOKS_TASK:{
                searchBooksTaskPost(tail);
                break;
            }
            default: break;
        }
    }

    private void checkConnectivity() {
        if(!HttpConnectionHelper.checkConnectivity(this)){
            Toast.makeText(this, "Attivare connessione ad internet", Toast.LENGTH_LONG).show();
            this.btnSearch.setEnabled(false);
        }
    }

    private void btnSearchLogic(){
        searchBooksTaskPre();
    }

    private void hideKeyboard(View view)
    {
        InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        acSearchTitle.dismissDropDown();
        acSearchAuthor.dismissDropDown();
    }

    public void showToast(String msg, int len){
        Toast.makeText(this, msg, len).show();
    }


    private String[] fetchAuthorsArray() {
        List<Author> authorsList = new ArrayList<Author>();
        authorsList = GlobalConstants.authorsMap.getAuthorsList();
        if(authorsList.size() < 1)
            return new String[]{""};
        String[] authorsArray = new String[authorsList.size()];
        for (int i=0; i<authorsList.size(); i++)
            authorsArray[i] = authorsList.get(i).getSurname() + ", " + authorsList.get(i).getName();

        return authorsArray;
    }


    /**
     * AsyncTaskHelper framework
     */
    private void searchBooksTaskPre(){
        //Pre-execute
        acSearchTitle.dismissDropDown();
        acSearchAuthor.dismissDropDown();
        //Background
        AsyncHelper asyncHelper = new AsyncHelper(SEARCH_BOOKS_TASK, this, progCircle);
        asyncHelper.execute();
    }

    private List<Object> searchBooksTaskBackground(){
        List<Object> result = new ArrayList<Object>();

        Author author = null;
        try {
            author = new Author(acSearchAuthor.getText().toString().split(", ")[1].replace("\n", ""), acSearchAuthor.getText().toString().split(", ")[0].replace("\n", ""));
        }catch (Exception e){
            author = new Author("", "");
        }
        //Mostro progress circle
        progCircle.setVisibility(View.VISIBLE);
        //Recupero lista libri
        String jsonBookList = BiblioValeApi.getBookList(author.getSurname(),
                                                        author.getName(),
                                                        acSearchTitle.getText().toString(),
                                                        true);
        result.add(jsonBookList);

        return result;
    }

    private void searchBooksTaskPost(List<Object> results){
        String jsonBookList = (String) results.get(0);

        acSearchTitle.dismissDropDown();
        acSearchAuthor.dismissDropDown();
        if(jsonBookList.length()<3 ) { //Nessun libro trovato
            showToast("Nessun libro trovato", Toast.LENGTH_SHORT);
            return;
        }
        List<PutExtraPair> putExtraList = new ArrayList<PutExtraPair>();
        putExtraList.add(new PutExtraPair(GlobalConstants.BOOKLIST_KEY, jsonBookList));
        ActivityFlowHelper.goToActivity(this, ResultsActivity.class, putExtraList);
    }

    private void loadBooksTaskPre(){
        //Pre-execute
        titlesArray = new String[]{""};
        ArrayAdapter<String> titlesListAdapter= new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line, titlesArray);
        acSearchTitle.setAdapter(titlesListAdapter);
        //Background
        AsyncHelper asyncHelper = new AsyncHelper(LOAD_BOOKS_TASK, this, progCircle);
        asyncHelper.execute();
    }

    private List<Object> loadBooksTaskBackground(){
        List<Object> result = new ArrayList<Object>();

        Author author = null;
        try {
            author = new Author(acSearchAuthor.getText().toString().split(", ")[1].replace("\n", ""), acSearchAuthor.getText().toString().split(", ")[0].replace("\n", ""));
        }catch (Exception e){
            result.add(new String[]{""});
            return result;
        }
        List<Book> books = new ArrayList<>();
        books = JSONHelper.bookListDeserialize(BiblioValeApi.getBookList(author.getSurname(), author.getName(), "", true));
        if(books.size() > 0){
            titlesArray = new String[books.size()];
            for (int i=0; i<titlesArray.length; i++){
                titlesArray[i] = books.get(i).getTitle();
            }
            result.add(titlesArray);
            return result;
        }
        return result;
    }

    private void loadBooksTaskPost(List<Object> results){
        titlesArray = (String[]) results.get(0);
        ArrayAdapter<String> titlesListAdapter= new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line, titlesArray);
        acSearchTitle.setAdapter(titlesListAdapter);
    }


}
