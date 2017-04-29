package dev.sturmtruppen.bibliovale.businessLogic.helpers;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;

import java.util.List;

/**
 * Created by Matteo on 29/04/2017.
 */

public abstract class AasyncActivity extends AppCompatActivity {
    /**
     * Metodo contenente la logica da eseguire in background
     */
    public abstract List<Object> doBackgroundWork(String taskName);

    /**
     * Metodo di callback richiamato al termine del task asincrono
     * @param data risultato del task, da castare opportunamente
     */
    public abstract void onAsyncCallBack(List<Object> data);
}
