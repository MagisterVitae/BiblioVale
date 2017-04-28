package dev.sturmtruppen.bibliovale.dataLayer.bookRepositories;

import dev.sturmtruppen.bibliovale.businessLogic.bo.Book;

/**
 * Created by Matteo on 24/04/2017.
 */

public interface IBookRepositoryFetcher {

    Book getBookSync(String[] params);

    String getJsonBookSync(String[] params);

    Book searchByIsbn(String isbn);

    String jsonSearchByIsbn(String isbn);

    Book searchByTitleAndAuthor(String title, String autSurname, String autName);

    String jsonSearchByTitleAndAuthor(String title, String autSurname, String autName);

    Book bookSearcher(String completeUrl);

    String jsonBookSearcher(String completeUrl);
}
