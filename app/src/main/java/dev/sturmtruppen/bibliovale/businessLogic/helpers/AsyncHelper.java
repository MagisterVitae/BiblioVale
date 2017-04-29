package dev.sturmtruppen.bibliovale.businessLogic.helpers;

import android.app.Activity;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;

import java.util.List;

import dev.sturmtruppen.bibliovale.businessLogic.bo.Book;

/**
 * Created by Matteo on 29/04/2017.
 */

public class AsyncHelper extends AsyncTask<Void, Void, List<Object>>{

    private String taskname;
    private AasyncActivity activity;
    private ProgressBar progressBar;

    public AsyncHelper(String taskname, AasyncActivity activity, ProgressBar progressBar) {
        this.taskname = taskname;
        this.activity = activity;
        this.progressBar = progressBar;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected List<Object> doInBackground(Void... params) {
        List<Object> result = activity.doBackgroundWork(taskname);
        return result;
    }

    @Override
    protected void onPostExecute(List<Object> result) {
        super.onPostExecute(result);
        progressBar.setVisibility(View.GONE);
        result.add(0, taskname); //Primo elemento taskname: per differenziare logiche in funzione del task
        activity.onAsyncCallBack(result);
    }


}
