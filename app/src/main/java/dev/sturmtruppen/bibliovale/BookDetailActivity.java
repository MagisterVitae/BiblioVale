package dev.sturmtruppen.bibliovale;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import dev.sturmtruppen.bibliovale.businessLogic.BO.Book;
import dev.sturmtruppen.bibliovale.businessLogic.BO.Genre;
import dev.sturmtruppen.bibliovale.businessLogic.BiblioValeApi;
import dev.sturmtruppen.bibliovale.businessLogic.GlobalConstants;
import dev.sturmtruppen.bibliovale.businessLogic.Helpers.JSONHelper;

public class BookDetailActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText txtTitle, txtAuthor, txtYear, txtIsbn10, txtIsbn13, txtStatus, txtNotes;
    private ImageView imgThumbnail;
    private AutoCompleteTextView acTxtGenre;

    private String savedGenre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

        //Assegnamento handle oggetti visualizzati in activity
        txtTitle = (EditText) findViewById(R.id.txtTitle);
        txtAuthor = (EditText) findViewById(R.id.txtAuthor);
        txtYear = (EditText) findViewById(R.id.txtYear);
        txtIsbn10 = (EditText) findViewById(R.id.txtIsbn10);
        txtIsbn13 = (EditText) findViewById(R.id.txtIsbn13);
        txtStatus = (EditText) findViewById(R.id.txtStatus);
        txtNotes = (EditText) findViewById(R.id.txtNotes);
        imgThumbnail = (ImageView) findViewById(R.id.imgThumbnail);
        acTxtGenre = (AutoCompleteTextView) findViewById(R.id.acTxtGenre);

        //Adapter autocompletamento
        final String[] genresArray = fetchGenresArray();
        ArrayAdapter<String> genreListAdapter= new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_dropdown_item_1line, genresArray);
        acTxtGenre.setAdapter(genreListAdapter);
        acTxtGenre.setValidator(new AutoCompleteTextView.Validator() {
            @Override
            public boolean isValid(CharSequence text) {
                Arrays.sort(genresArray);
                if (Arrays.binarySearch(genresArray, text.toString()) > 0) {
                    return true;
                }
                return false;
            }

            @Override
            public CharSequence fixText(CharSequence invalidText) {
                Toast.makeText(getApplicationContext(), "'" + invalidText + "' non è un genere ammesso", Toast.LENGTH_SHORT).show();
                return savedGenre;
            }
        });

        //Listener
        acTxtGenre.setOnClickListener(this);

        //Recupero libro da visualizzare
        String jsonBook = this.getIntent().getStringExtra(GlobalConstants.BOOK_KEY);
        this.showBook(jsonBook);

    }

    @Override
    public void onClick(View v) {
        //Individuo l'oggetto cliccato
        switch (v.getId()) {
            case R.id.acTxtGenre: {
                this.acTxtGenre.showDropDown();
            }
            default:
                break;
        }
    }

    private String[] fetchGenresArray() {
        List<Genre> genresList = new ArrayList<Genre>();

        String jsonGenres = BiblioValeApi.getAllGenres();
        if(TextUtils.isEmpty(jsonGenres))
            return new String[]{""};
        genresList = JSONHelper.getGenresList(jsonGenres);
        String[] genresArray = new String[genresList.size()];
        for (int i=0; i<genresList.size(); i++)
            genresArray[i] = genresList.get(i).getName();

        return genresArray;
    }

    private void showBook(String jsonBook){
        Book book = JSONHelper.getBook(jsonBook);
        List<String> authorsList = book.getAuthors();
        String authors = "";

        for (String aut : authorsList){
            authors += aut + "\n";
        }

        txtTitle.setText(book.getTitle());
        txtAuthor.setText(authors);
        txtYear.setText(book.getYear());
        txtStatus.setText(book.getStatus());
        txtIsbn10.setText(book.getIsbn10());
        txtIsbn13.setText(book.getIsbn13());
        txtNotes.setText(book.getNotes());

        acTxtGenre.setText(book.getGenre().getName());
        this.savedGenre = book.getGenre().getName();

        imgThumbnail.setImageDrawable(book.fetchThumbnail());
    }

}
