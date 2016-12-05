package dev.sturmtruppen.bibliovale;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;
import android.content.DialogInterface;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import dev.sturmtruppen.bibliovale.businessLogic.BO.Author;
import dev.sturmtruppen.bibliovale.businessLogic.BO.Book;
import dev.sturmtruppen.bibliovale.businessLogic.BO.Genre;
import dev.sturmtruppen.bibliovale.businessLogic.BiblioValeApi;
import dev.sturmtruppen.bibliovale.businessLogic.DataFetchers.DBApiResponse;
import dev.sturmtruppen.bibliovale.businessLogic.GlobalConstants;
import dev.sturmtruppen.bibliovale.businessLogic.Helpers.ActivityFlowHelper;
import dev.sturmtruppen.bibliovale.businessLogic.Helpers.JSONHelper;

public class BookDetailActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private EditText txtTitle, txtYear, txtIsbn10, txtIsbn13, txtNotes;
    private ImageView imgThumbnail;
    private AutoCompleteTextView acTxtAuthor;
    private Spinner spinGenre, spinStatus;
    private Button btnSave;
    private LinearLayout linLayout;

    private String savedGenre;
    private String savedAuthor;
    private String jsonBook;

    String[] genresArray;
    String[] authorsArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

        //Assegnamento handle oggetti visualizzati in activity
        linLayout = (LinearLayout) findViewById(R.id.detailLinLayout);

        txtTitle = (EditText) findViewById(R.id.txtTitle);
        txtYear = (EditText) findViewById(R.id.txtYear);
        txtIsbn10 = (EditText) findViewById(R.id.txtIsbn10);
        txtIsbn13 = (EditText) findViewById(R.id.txtIsbn13);
        txtNotes = (EditText) findViewById(R.id.txtNotes);
        imgThumbnail = (ImageView) findViewById(R.id.imgThumbnail);
        spinGenre = (Spinner) findViewById(R.id.spinGenre);
        spinStatus = (Spinner) findViewById(R.id.spinStatus);
        acTxtAuthor = (AutoCompleteTextView) findViewById(R.id.acTxtAuthor);
        btnSave = (Button) findViewById(R.id.btnSave);

        //Adapter autocompletamento
        genresArray = fetchGenresArray();
        authorsArray = fetchAuthorsArray();

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
                createNewAuthor(invalidText);
                return acTxtAuthor.getText();
            }
        });

        //Listener
        spinGenre.setOnItemSelectedListener(this);
        btnSave.setOnClickListener(this);
        linLayout.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View view, MotionEvent ev)
            {
                hideKeyboard(view);
                return false;
            }
        });

        //Individuo il flavour e preparo l'activity di conseguenza
        switch (this.getIntent().getStringExtra(GlobalConstants.DETAILS_ACTIVITY_FLAVOUR)){
            case GlobalConstants.DETAILS_SHOW_UPDATE: {
                //Recupero libro da visualizzare
                jsonBook = this.getIntent().getStringExtra(GlobalConstants.BOOK_KEY);
                this.showBookFlavour(jsonBook);
                break;
            }
            case GlobalConstants.DETAILS_CREATE: {
                this.newBookFlavour();
                break;
            }
        }


    }

    @Override
    public void onClick(View v) {
        //Individuo l'oggetto cliccato
        switch (v.getId()) {
            case R.id.acTxtAuthor: {
                this.acTxtAuthor.showDropDown();
                break;
            }
            case R.id.btnSave:{
                switch (this.getIntent().getStringExtra(GlobalConstants.DETAILS_ACTIVITY_FLAVOUR)) {
                    case GlobalConstants.DETAILS_SHOW_UPDATE: {
                        this.updateBook(jsonBook);
                        break;
                    }
                    case GlobalConstants.DETAILS_CREATE: {
                        this.createBook();
                        break;
                    }
                }
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

    private void showBookFlavour(String jsonBook){
        Book book = JSONHelper.bookDeserialize(jsonBook);
        List<Author> authorsList = book.getAuthors();
        Drawable bookCover = null;
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

        bookCover = book.fetchThumbnail();
        if(bookCover == null)
            bookCover = ContextCompat.getDrawable(this, R.drawable.cover_not_found);
        imgThumbnail.setImageDrawable(bookCover);
    }

    private void newBookFlavour(){
        Drawable bookCover = ContextCompat.getDrawable(this, R.drawable.cover_not_found);
        imgThumbnail.setImageDrawable(bookCover);
    }

    private void updateBook(String jsonBook){
        Book modifiedBook = createBookFromActivity(jsonBook);
        DBApiResponse response = JSONHelper.dbApiResponseDeserialize(BiblioValeApi.updateBook(modifiedBook));
        switch (response.getStatusId()){
            case 0: {
                Toast.makeText(this, "Salvataggio completato", Toast.LENGTH_SHORT).show();
                ActivityFlowHelper.goToActivity(this, MainActivity.class); //Torna alla home
                break;
            }
            case 1:{
                new AlertDialog.Builder(this).setTitle("Errore").setMessage("Compilare tutti i campi obbligatori!").setNeutralButton("Chiudi", null).show();
                break;
            }
            case 2:{
                new AlertDialog.Builder(this).setTitle("Errore").setMessage("Autore inesistente!").setNeutralButton("Chiudi", null).show();
                break;
            }
            case 3:{
                new AlertDialog.Builder(this).setTitle("Errore").setMessage("Genere inesistente!").setNeutralButton("Chiudi", null).show();
                break;
            }
            case 4:{
                new AlertDialog.Builder(this).setTitle("Errore").setMessage("Errore durante il salvataggio!").setNeutralButton("Chiudi", null).show();
                break;
            }
        }
    }

    private void createBook(){
        Book newBook = createBookFromActivity("");
        DBApiResponse response = JSONHelper.dbApiResponseDeserialize(BiblioValeApi.createBook(newBook));
        switch (response.getStatusId()){
            case 0: {
                Toast.makeText(this, "Salvataggio completato", Toast.LENGTH_SHORT).show();
                ActivityFlowHelper.goToActivity(this, MainActivity.class); //Torna alla home
                break;
            }
            case 1:{
                new AlertDialog.Builder(this).setTitle("Errore").setMessage("Compilare tutti i campi obbligatori!").setNeutralButton("Chiudi", null).show();
                break;
            }
            case 2:{
                new AlertDialog.Builder(this).setTitle("Errore").setMessage("Autore inesistente!").setNeutralButton("Chiudi", null).show();
                break;
            }
            case 3:{
                new AlertDialog.Builder(this).setTitle("Errore").setMessage("Libro già salvato!").setNeutralButton("Chiudi", null).show();
                break;
            }
            case 4:{
                new AlertDialog.Builder(this).setTitle("Errore").setMessage("Genere inesistente!").setNeutralButton("Chiudi", null).show();
                break;
            }
            case 5:{
                new AlertDialog.Builder(this).setTitle("Errore").setMessage("Errore durante il salvataggio!").setNeutralButton("Chiudi", null).show();
                break;
            }
        }
    }

    private Book createBookFromActivity(String jsonBook){
        //Costruisco oggetto libro con i dati mostrati sull'activity
        Author newAuthor = new Author(acTxtAuthor.getText().toString().split(", ")[1].replace("\n", ""), acTxtAuthor.getText().toString().split(", ")[0].replace("\n", ""));
        List<Author> newAuthors = new ArrayList<Author>();
        newAuthors.add(newAuthor);

        Book modifiedBook = new Book();
        if(!jsonBook.isEmpty())
            modifiedBook.setId(JSONHelper.bookDeserialize(jsonBook).getId());
        modifiedBook.setTitle(txtTitle.getText().toString());
        modifiedBook.setAuthors(newAuthors);
        modifiedBook.setGenre(spinGenre.getSelectedItem().toString());
        modifiedBook.setYear(txtYear.getText().toString());
        modifiedBook.setStatus(spinStatus.getSelectedItem().toString());
        modifiedBook.setIsbn10(txtIsbn10.getText().toString());
        modifiedBook.setIsbn13(txtIsbn13.getText().toString());
        modifiedBook.setNotes(txtNotes.getText().toString());

        return modifiedBook;
    }

    private void createNewAuthor(final CharSequence strNewAuth){
        final String surname = strNewAuth.toString().split(", ")[0].replace("\n", "");
        final String name = strNewAuth.toString().split(", ")[1].replace("\n", "");

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Attenzione")
                .setMessage(String.format("Vuoi creare l'autore con cognome \"%s\" e nome \"%s\"?", surname, name))
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        DBApiResponse response = JSONHelper.dbApiResponseDeserialize(BiblioValeApi.createAuthor(surname, name));
                        switch (response.getStatusId()){
                            case 0: {
                                authorsArray = fetchAuthorsArray();
                                acTxtAuthor.setText(strNewAuth);
                                break;
                            }
                            default: {
                                acTxtAuthor.setText(savedAuthor);
                                break;
                            }
                        }
                    }
                })
                .setNegativeButton("Annulla", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        acTxtAuthor.setText(savedAuthor);
                    }
                })
                .show();
    }

    private void hideKeyboard(View view)
    {
        InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

}
