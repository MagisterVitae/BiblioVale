package dev.sturmtruppen.bibliovale.businessLogic;

import dev.sturmtruppen.bibliovale.businessLogic.helpers.AuthorsMap;
import dev.sturmtruppen.bibliovale.businessLogic.helpers.GenresMap;

/**
 * Created by Matteo on 27/08/2016.
 */
public class GlobalConstants {
    //Costanti chiave per passaggio dati tra Activity
    public static final String BOOKLIST_KEY = "BOOKLIST";
    public static final String BOOK_KEY = "BOOK";
    public static final String DETAILS_ACTIVITY_FLAVOUR = "DETAILS_ACTIVITY_FLAVOUR";
    public static final String DETAILS_SHOW_UPDATE = "DETAILS_SHOW_UPDATE";
    public static final String DETAILS_CREATE = "DETAILS_CREATE";
    public static final String CONFIG_PREFS = "BIBLIOVALE_PREFERENCES";
    public static final String CONFIG_URL = "BIBLIOVALE_URL";
    public static final String ACTIVITY_RESULTS_TITLE = "ACTIVITY_RESULTS_TITLE";

    public static String webSiteUrl = "";
    public static GenresMap genresMap = null;
    public static AuthorsMap authorsMap = null;

}


