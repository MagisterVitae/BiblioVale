package dev.sturmtruppen.bibliovale.businessLogic;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import dev.sturmtruppen.bibliovale.businessLogic.bo.Author;
import dev.sturmtruppen.bibliovale.businessLogic.bo.Book;

/**
 * Created by Matteo on 24/04/2017.
 */

public final class OPACUtils {
    private static final String baseUrl = "http://opac.sbn.it/opacmobilegw/search.json?";


    public static Book getBook(String json){

        Book book = new Book();

        try {
            JsonNode rootNode = new ObjectMapper().readTree(json);
            JsonNode volumeInfoNode = rootNode.path("briefRecords").path(0);
            JsonNode authorsNode = volumeInfoNode;
            JsonNode thumbnailNode = volumeInfoNode;
            JsonNode isbnNode = volumeInfoNode;

            String title, year, thumbnailUrl, isbn;
            Author author;
            List<Author> authorList = new ArrayList<Author>();

            int totItems = rootNode.path("numFound").asInt();
            //se non trovo libri ritorno null
            if(totItems < 1)
                return null;

            try {
                String t = volumeInfoNode.path("titolo").asText();
                title = t.split(" / ")[0];
            }catch (Exception e){
                title = volumeInfoNode.path("titolo").asText();
            }
            try {
                String y = volumeInfoNode.path("pubblicazione").asText();
                year = y.split(", ")[1];
            }catch (Exception e){
                year = volumeInfoNode.path("pubblicazione").asText();
            }
            try {
                String a = authorsNode.path("autorePrincipale").asText();
                String aut = a.split(" <")[0];
                author = new Author(aut.split(", ")[1], aut.split(", ")[0]);
            }catch (Exception e){
                String a = null;
                try{
                    a = authorsNode.path("autorePrincipale").asText();
                    author = new Author(a.split(", ")[1], a.split(", ")[0]);
                }catch (Exception e2){
                    author = new Author("TROVATO", "NON");
                }
            }
            thumbnailUrl = thumbnailNode.path("copertina").asText();
            isbn = isbnNode.path("isbn").asText().replace("-","");
            authorList.add(author);

            book.setTitle(title);
            book.setYear(year);
            book.setThumbnailUrl(thumbnailUrl);
            book.setTotalItems(totItems);
            book.setIsbn13(isbn);
            book.setIsbn10("");
            book.setAuthors(authorList);

        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return book;
    }
}
