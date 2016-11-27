package dev.sturmtruppen.bibliovale;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import dev.sturmtruppen.bibliovale.businessLogic.BO.Author;
import dev.sturmtruppen.bibliovale.businessLogic.BO.Book;
import dev.sturmtruppen.bibliovale.businessLogic.BO.Genre;
import dev.sturmtruppen.bibliovale.businessLogic.BiblioValeApi;
import dev.sturmtruppen.bibliovale.businessLogic.GlobalConstants;
import dev.sturmtruppen.bibliovale.businessLogic.Helpers.JSONHelper;

public class BookDetailActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private EditText txtTitle, txtYear, txtIsbn10, txtIsbn13, txtNotes;
    private ImageView imgThumbnail;
    private AutoCompleteTextView acTxtAuthor;
    private Spinner spinGenre, spinStatus;

    private String savedGenre;
    private String savedAuthor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

        //Assegnamento handle oggetti visualizzati in activity
        txtTitle = (EditText) findViewById(R.id.txtTitle);
        txtYear = (EditText) findViewById(R.id.txtYear);
        txtIsbn10 = (EditText) findViewById(R.id.txtIsbn10);
        txtIsbn13 = (EditText) findViewById(R.id.txtIsbn13);
        txtNotes = (EditText) findViewById(R.id.txtNotes);
        imgThumbnail = (ImageView) findViewById(R.id.imgThumbnail);
        spinGenre = (Spinner) findViewById(R.id.spinGenre);
        spinStatus = (Spinner) findViewById(R.id.spinStatus);
        acTxtAuthor = (AutoCompleteTextView) findViewById(R.id.acTxtAuthor);

        //Adapter autocompletamento
        final String[] genresArray = fetchGenresArray();
        final String[] authorsArray = fetchAuthorsArray();

        ArrayAdapter<String> genreListAdapter= new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, genresArray);
        genreListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<CharSequence> statusListAdapter= ArrayAdapter.createFromResource(this,
                R.array.status, android.R.layout.simple_spinner_item);
        statusListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<String> authorsListAdapter= new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line, authorsArray);

        spinGenre.setAdapter(genreListAdapter);
        spinStatus.setAdapter(statusListAdapter);
        acTxtAuthor.setAdapter(authorsListAdapter);
        acTxtAuthor.setValidator(new AutoCompleteTextView.Validator() {
            @Override
            public boolean isValid(CharSequence text) {
                Arrays.sort(authorsArray);
                if (Arrays.binarySearch(authorsArray, text.toString()) > 0) {
                    return true;
                }
                return false;
            }

            @Override
            public CharSequence fixText(CharSequence invalidText) {
                Toast.makeText(getApplicationContext(), "'" + invalidText + "' non è un autore ammesso", Toast.LENGTH_SHORT).show();
                return savedAuthor;
            }
        });


        //Listener
        acTxtAuthor.setOnClickListener(this);
        spinGenre.setOnItemSelectedListener(this);

        //Recupero libro da visualizzare
        String jsonBook = this.getIntent().getStringExtra(GlobalConstants.BOOK_KEY);
        this.showBook(jsonBook);

    }

    @Override
    public void onClick(View v) {
        //Individuo l'oggetto cliccato
        switch (v.getId()) {
            case R.id.acTxtAuthor: {
                this.acTxtAuthor.showDropDown();
            }
            default:
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (view.getId()){
            case R.id.spinGenre:{
                //this.spinGenre.sett parent.getItemAtPosition(position).toString();
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private String[] fetchGenresArray() {
        List<Genre> genresList = new ArrayList<Genre>();

        String jsonGenres = BiblioValeApi.getAllGenres();
        if(TextUtils.isEmpty(jsonGenres))
            return new String[]{""};
        genresList = JSONHelper.genresListDeserialize(jsonGenres);
        String[] genresArray = new String[genresList.size()];
        for (int i=0; i<genresList.size(); i++)
            genresArray[i] = genresList.get(i).getName();

        return genresArray;
    }

    private String[] fetchAuthorsArray() {
        List<Author> authorsList = new ArrayList<Author>();

        String jsonAuthors = BiblioValeApi.getAllAuthors();
        if(TextUtils.isEmpty(jsonAuthors))
            return new String[]{""};
        authorsList = JSONHelper.authorsListDeserialize(jsonAuthors);
        String[] authorsArray = new String[authorsList.size()];
        for (int i=0; i<authorsList.size(); i++)
            authorsArray[i] = authorsList.get(i).getSurname() + ", " + authorsList.get(i).getName();

        return authorsArray;
    }

    private void setGenreSpinnerText(String text){
        for(int i= 0; i < spinGenre.getAdapter().getCount(); i++)
        {
            if(spinGenre.getAdapter().getItem(i).toString().contains(text))
            {
                spinGenre.setSelection(i);
                return;
            }
        }
    }

    private void setStatusSpinnerText(String text){
        for(int i= 0; i < spinStatus.getAdapter().getCount(); i++)
        {
            if(spinStatus.getAdapter().getItem(i).toString().contains(text))
            {
                spinStatus.setSelection(i);
                return;
            }
        }
    }

    private void showBook(String jsonBook){
        Book book = JSONHelper.bookDeserialize(jsonBook);
        List<Author> authorsList = book.getAuthors();
        String authors = "";

        //AUTORE SINGOLO
        for (Author aut : authorsList){
            authors += aut.getSurname() + ", " + aut.getName() + "\n";
        }

        txtTitle.setText(book.getTitle());
        acTxtAuthor.setText(authors);
        txtYear.setText(book.getYear());
        txtIsbn10.setText(book.getIsbn10());
        txtIsbn13.setText(book.getIsbn13());
        txtNotes.setText(book.getNotes());

        this.setGenreSpinnerText(book.getGenre().getName());
        this.setStatusSpinnerText(book.getStatus());
        this.savedGenre = book.getGenre().getName();
        this.savedAuthor = authors;

        imgThumbnail.setImageDrawable(book.fetchThumbnail());
    }


}
