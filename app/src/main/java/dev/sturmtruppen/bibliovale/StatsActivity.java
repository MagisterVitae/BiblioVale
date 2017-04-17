package dev.sturmtruppen.bibliovale;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceGroup;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Pair;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;
import org.apache.commons.lang3.text.WordUtils;

import dev.sturmtruppen.bibliovale.businessLogic.BiblioValeApi;
import dev.sturmtruppen.bibliovale.businessLogic.GlobalConstants;
import dev.sturmtruppen.bibliovale.businessLogic.Helpers.ActivityFlowHelper;
import dev.sturmtruppen.bibliovale.businessLogic.Helpers.JSONHelper;
import dev.sturmtruppen.bibliovale.businessLogic.Helpers.PutExtraPair;

import static android.R.id.progress;

public class StatsActivity extends AppCompatActivity implements View.OnClickListener{
    private TableLayout layStatsTable;
    private ProgressBar progCircle;
    private TableRow selectedRow;

    private Handler handler;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        //Assegnamento handle oggetti visualizzati in activity
        layStatsTable = (TableLayout) findViewById(R.id.layStatsTable);
        progCircle = (ProgressBar) findViewById(R.id.statsProgCir);

        //Proprietà
        progCircle.setVisibility(View.GONE);

        //Visualizza risultati
        this.showResults();
    }

    @Override
    public void onClick(View v) {
        //Individuo l'oggetto cliccato
        switch (v.getId()) {
            default:
                TableRow row = (TableRow) v;
                TextView keyTextView = (TextView) row.getChildAt(0);
                String status = keyTextView.getText().toString();
                if (status.compareToIgnoreCase("TOTALE")!=0){
                    selectedRow = row;
                    v.setBackgroundResource(R.color.colorPrimaryDark);
                    statusListLogic(status);
                }
                break;
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        progCircle.setVisibility(View.GONE);
        if(selectedRow != null)
            selectedRow.setBackgroundColor(Color.TRANSPARENT);
    }

    private void statusListLogic(String status) {
        //Mostro progress circle
        progCircle.setVisibility(View.VISIBLE);

        String jsonBookList = BiblioValeApi.getBooksByStatus(status);

        List<PutExtraPair> putExtraList = new ArrayList<PutExtraPair>();
        putExtraList.add(new PutExtraPair(GlobalConstants.BOOKLIST_KEY, jsonBookList));
        putExtraList.add(new PutExtraPair(GlobalConstants.ACTIVITY_RESULTS_TITLE, status));
        ActivityFlowHelper.goToActivity(StatsActivity.this, ResultsActivity.class, putExtraList);
    }

    private void showResults(){
        //Recupero elenco statistiche
        String jsonStats = BiblioValeApi.getStats();
        List<Pair<String,String>> statList = JSONHelper.pairListDeserialize(jsonStats);

        //Scrivo statistiche su activity
        for(int i=0;i<statList.size();i++){
            // create a new TableRow
            TableRow row = new TableRow(this);
            row.setHorizontalGravity(Gravity.CENTER_HORIZONTAL);
            row.setPadding(0,15,0,15);
            // create a new TextView for showing xml data
            TextView textKey = (TextView) getLayoutInflater().inflate(R.layout.table_key_style, null);
            TextView textValue = (TextView) getLayoutInflater().inflate(R.layout.table_value_style, null);
            // set the text to "text xx"
            textKey.setText(WordUtils.capitalize(statList.get(i).first));
            textValue.setText(statList.get(i).second);
            // last row style
            if(i==statList.size()-1){
                // add separation line
                View line = new View(this);
                line.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, 1));
                line.setBackgroundColor(Color.rgb(51, 51, 51));
                layStatsTable.addView(line);

                textValue.setTypeface(textValue.getTypeface(), Typeface.BOLD);
                textKey.setTextScaleX(1.3f);
                textValue.setTextScaleX(1.3f);
            }
            // add the TextView  to the new TableRow
            row.addView(textKey);
            row.addView(textValue);
            // set OnClickListener
            row.setOnClickListener(StatsActivity.this);
            // add the TableRow to the TableLayout
            layStatsTable.addView(row, new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));
        }
    }


}
