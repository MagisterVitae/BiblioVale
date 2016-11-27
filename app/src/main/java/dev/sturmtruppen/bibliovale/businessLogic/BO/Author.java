package dev.sturmtruppen.bibliovale.businessLogic.BO;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Matteo on 26/11/2016.
 */

public class Author {
    private String originalJson;
    private int id;
    private String name;
    private String surname;

    public Author(){super();}
    public Author(JSONObject _jsonBook) {
        try {
            this.originalJson = _jsonBook.toString();
            this.id = _jsonBook.getInt("id");
            this.name = _jsonBook.getString("name");
            this.surname = _jsonBook.getString("surname");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }
}
