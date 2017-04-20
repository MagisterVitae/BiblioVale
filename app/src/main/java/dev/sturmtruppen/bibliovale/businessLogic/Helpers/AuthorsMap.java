package dev.sturmtruppen.bibliovale.businessLogic.Helpers;

import java.util.List;

import dev.sturmtruppen.bibliovale.businessLogic.BO.Author;
import dev.sturmtruppen.bibliovale.businessLogic.BiblioValeApi;

/**
 * Created by Matteo on 20/04/2017.
 */

public class AuthorsMap {
    List<Author> authorsList;

    public AuthorsMap(){
        this.authorsList = fetchAuthors();
    }

    private List<Author> fetchAuthors() {
         return JSONHelper.authorsListDeserialize(BiblioValeApi.getAllAuthors(true));
    }

    public List<Author> getAuthorsList() {
        return authorsList;
    }

    public void addAuthor(Author author){
        authorsList.add(author);
    }
}
