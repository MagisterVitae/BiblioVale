package dev.sturmtruppen.bibliovale;

import android.graphics.Color;
import android.graphics.Typeface;
import android.preference.PreferenceGroup;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Pair;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import org.apache.commons.lang3.text.WordUtils;

import dev.sturmtruppen.bibliovale.businessLogic.BiblioValeApi;
import dev.sturmtruppen.bibliovale.businessLogic.GlobalConstants;
import dev.sturmtruppen.bibliovale.businessLogic.Helpers.JSONHelper;

public class StatsActivity extends AppCompatActivity {
    private TableLayout layStatsTable;
    private ProgressBar progCircle;

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
            // add the TableRow to the TableLayout
            layStatsTable.addView(row, new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));
        }
    }
}
