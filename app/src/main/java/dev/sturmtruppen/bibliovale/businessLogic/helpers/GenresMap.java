package dev.sturmtruppen.bibliovale.businessLogic.helpers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dev.sturmtruppen.bibliovale.businessLogic.bo.Genre;
import dev.sturmtruppen.bibliovale.dataLayer.BiblioValeApi;

/**
 * Created by Matteo on 18/04/2017.
 */

public class GenresMap {
    Map<String,Integer> map;
    List<Genre> genresList;

    public GenresMap(){
        this.map = fetchMap();
    }

    private Map<String,Integer> fetchMap() {
        Map<String,Integer> genresMap = new HashMap<>();
        genresList = JSONHelper.genresListDeserialize(BiblioValeApi.getAllGenres(true));
        for (int i=0; i<genresList.size(); i++){
            genresMap.put(genresList.get(i).getName(), genresList.get(i).getId());
        }
        return genresMap;
    }

    public Map<String,Integer> getMap() {
        return map;
    }

    public List<Genre> getGenresList(){
        return this.genresList;
    }

    public void setMap(Map<String,Integer> map) {
        this.map = map;
    }
}
