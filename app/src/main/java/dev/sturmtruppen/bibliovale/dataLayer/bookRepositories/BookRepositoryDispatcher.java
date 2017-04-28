package dev.sturmtruppen.bibliovale.dataLayer.bookRepositories;

import android.graphics.drawable.Drawable;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import dev.sturmtruppen.bibliovale.businessLogic.bo.Book;

/**
 * Created by Matteo on 26/04/2017.
 */

public class BookRepositoryDispatcher implements IBookRepositoryFetcher{

    List<IBookRepositoryFetcher> subscribedBookRepos = new ArrayList<IBookRepositoryFetcher>();
    List<IBookRepositoryFetcher> subscribedThumbnailRepos = new ArrayList<IBookRepositoryFetcher>();

    public BookRepositoryDispatcher() {
        // Repository utilizzati in ordine di inserimento in lista
        subscribedBookRepos.add(new GoogleBooksFetcher());
        subscribedBookRepos.add(new OPACFetcher());

        subscribedThumbnailRepos.add(new GoogleBooksFetcher());
    }

    /**
     * Ricerca cross-repository ordinatamente per
     *  - ISBN13
     *  - ISBN10
     *  - Autore; Titolo
     */
    @Override
    public Book getBookSync(String[] params) {
        String isbn13 = params[0];
        String isbn10 = params[1];
        String title = params[2];
        String autSurname = params[3];
        String autName = params[4];
        Book result = null;

        // ISBN13
        for (IBookRepositoryFetcher repo : subscribedBookRepos) {
            result = repo.searchByIsbn(isbn13);
            if (result != null)
                return result;
        }

        // ISBN10
        for (IBookRepositoryFetcher repo : subscribedBookRepos) {
            result = repo.searchByIsbn(isbn10);
            if (result != null)
                return result;
        }

        // Autore - Titolo
        for (IBookRepositoryFetcher repo : subscribedBookRepos) {
            result = repo.searchByTitleAndAuthor(title, autSurname, autName);
            if (result != null)
                return result;
        }

        return result;
    }

    /**
     * Ricerca copertina cross-repository
     * @param book
     * @return
     */
    public Drawable fetchThumbnail(Book book){
        Drawable thumbnail = null;
        InputStream is;

        //Download da URL associata al libro
        try {
            is = (InputStream) new URL(book.getThumbnailUrl()).getContent();
            thumbnail = Drawable.createFromStream(is, book.getTitle());
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(thumbnail != null)
            return thumbnail;

        //Download da repository registrati - ISBN13
        for (IBookRepositoryFetcher repo : subscribedThumbnailRepos) {
            Book result = repo.searchByIsbn(book.getIsbn13());
            if (result != null){
                try {
                    is = (InputStream) new URL(result.getThumbnailUrl()).getContent();
                    thumbnail = Drawable.createFromStream(is, result.getTitle());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(thumbnail != null)
                    return thumbnail;
            }
        }

        //Download da repository registrati - ISBN10
        for (IBookRepositoryFetcher repo : subscribedThumbnailRepos) {
            Book result = repo.searchByIsbn(book.getIsbn10());
            if (result != null){
                try {
                    is = (InputStream) new URL(result.getThumbnailUrl()).getContent();
                    thumbnail = Drawable.createFromStream(is, result.getTitle());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(thumbnail != null)
                    return thumbnail;
            }
        }

        //Download da repository registrati - Autore; Titolo
        for (IBookRepositoryFetcher repo : subscribedThumbnailRepos) {
            Book result = repo.searchByTitleAndAuthor(book.getTitle(), book.getAuthors().get(0).getSurname(), book.getAuthors().get(0).getName());
            if (result != null){
                try {
                    is = (InputStream) new URL(result.getThumbnailUrl()).getContent();
                    thumbnail = Drawable.createFromStream(is, result.getTitle());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(thumbnail != null)
                    return thumbnail;
            }
        }

        return thumbnail;
    }

    @Override
    public String getJsonBookSync(String[] params) {
        String result = null;
        for (IBookRepositoryFetcher repo :
                subscribedBookRepos) {
            result = repo.getJsonBookSync(params);
            if (result != null)
                break;
        }
        return result;
    }

    @Override
    public Book searchByIsbn(String isbn) {
        Book result = null;
        for (IBookRepositoryFetcher repo : subscribedBookRepos) {
            result = repo.searchByIsbn(isbn);
            if (result != null)
                break;
        }
        return result;
    }

    @Override
    public String jsonSearchByIsbn(String isbn) {
        String result = null;
        for (IBookRepositoryFetcher repo : subscribedBookRepos) {
            result = repo.jsonSearchByIsbn(isbn);
            if (result != null)
                break;
        }
        return result;
    }

    @Override
    public Book searchByTitleAndAuthor(String title, String autSurname, String autName) {
        Book result = null;
        for (IBookRepositoryFetcher repo : subscribedBookRepos) {
            result = repo.searchByTitleAndAuthor(title, autSurname, autName);
            if (result != null)
                break;
        }
        return result;
    }

    @Override
    public String jsonSearchByTitleAndAuthor(String title, String autSurname, String autName) {
        String result = null;
        for (IBookRepositoryFetcher repo : subscribedBookRepos) {
            result = repo.jsonSearchByTitleAndAuthor(title, autSurname, autName);
            if (result != null)
                break;
        }
        return result;
    }

    @Override
    public Book bookSearcher(String completeUrl) {
        Book result = null;
        for (IBookRepositoryFetcher repo : subscribedBookRepos) {
            result = repo.bookSearcher(completeUrl);
            if (result != null)
                break;
        }
        return result;
    }

    @Override
    public String jsonBookSearcher(String completeUrl) {
        String result = null;
        for (IBookRepositoryFetcher repo : subscribedBookRepos) {
            result = repo.jsonBookSearcher(completeUrl);
            if (result != null)
                break;
        }
        return result;
    }
}
