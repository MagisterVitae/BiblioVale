package dev.sturmtruppen.bibliovale.businessLogic;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import dev.sturmtruppen.bibliovale.businessLogic.bo.Author;
import dev.sturmtruppen.bibliovale.businessLogic.bo.Book;

public final class GoogleBooksUtils{

    private static final String googleKey = "AIzaSyAsz4PRSi2OIlRiu_uXZ-xW--PxEgC1X9E";
    private static final String baseUrl = "https://www.googleapis.com/books/v1/volumes?q=isbn:";


    public static Book getBook(String json){

        Book book = new Book();

        try {
            JsonNode rootNode = new ObjectMapper().readTree(json);
            JsonNode volumeInfoNode = rootNode.path("items").path(0).path("volumeInfo");
            JsonNode authorsNode = volumeInfoNode.path("authors");
            JsonNode thumbnailNode = volumeInfoNode.path("imageLinks").path("thumbnail");
            JsonNode isbnNode = volumeInfoNode.path("industryIdentifiers");

            int totItems = rootNode.path("totalItems").asInt();
            String title = volumeInfoNode.path("title").asText();
            String year = volumeInfoNode.path("publishedDate").asText();
            String thumbnailUrl = thumbnailNode.asText();

            //se non trovo libri ritorno null
            if(totItems < 1)
                return null;

            book.setTitle(title);
            book.setYear(year);
            book.setThumbnailUrl(thumbnailUrl);
            book.setTotalItems(totItems);
            if (authorsNode.isArray()) {
                JsonNode autNode = authorsNode.get(0);
                Author author;
                try{
                    String name = autNode.asText().split(" ")[0];
                    String surname = autNode.asText().substring(name.length() +1 );
                    author = new Author(name, surname);
                }catch (Exception e){
                    //Impossibile splittare l'autore in nome e cognome
                    author = new Author(autNode.asText(), autNode.asText());
                }
                List<Author> authorList = new ArrayList<Author>();
                authorList.add(author);
                book.setAuthors(authorList);
                /*
                for (final JsonNode objNode : authorsNode) {
                    //book.setAuthor(objNode.asText()); //// TODO: 27/11/2016 CORREGGERE CON OGGETTO AUTHORS
                }*/
            }
            if (isbnNode.isArray()){
                for (final JsonNode objNode : isbnNode){
                    if(objNode.path("type").asText().compareToIgnoreCase("ISBN_10") == 0)
                        book.setIsbn10(objNode.path("identifier").asText());
                    if(objNode.path("type").asText().compareToIgnoreCase("ISBN_13") == 0)
                        book.setIsbn13(objNode.path("identifier").asText());
                }
            }
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