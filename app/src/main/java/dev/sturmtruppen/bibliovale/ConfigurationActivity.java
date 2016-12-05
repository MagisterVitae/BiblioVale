package dev.sturmtruppen.bibliovale;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import dev.sturmtruppen.bibliovale.businessLogic.GlobalConstants;
import dev.sturmtruppen.bibliovale.businessLogic.Helpers.ActivityFlowHelper;

public class ConfigurationActivity extends AppCompatActivity implements View.OnClickListener{
    EditText txtConfigURL;
    Button btnConfigSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuration);

        txtConfigURL = (EditText) findViewById(R.id.txtConfigURL);
        btnConfigSave = (Button) findViewById(R.id.btnConfigSave);

        btnConfigSave.setOnClickListener(this);

        this.fillActivity();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnConfigSave: {
                savePreferencesData();
                break;
            }
        }
    }

    private void fillActivity(){
        txtConfigURL.setText(GlobalConstants.webSiteUrl);
    }

    public void savePreferencesData() {
        // Otteniamo il riferimento alle Preferences
        SharedPreferences prefs = getSharedPreferences(GlobalConstants.CONFIG_PREFS, Context.MODE_PRIVATE);
        // Otteniamo il corrispondente Editor
        SharedPreferences.Editor editor = prefs.edit();
        // Modifichiamo i valori con quelli inseriti nell'Activity
        if(txtConfigURL.getText() != null){
            editor.putString(GlobalConstants.CONFIG_URL, txtConfigURL.getText().toString());
            editor.commit();
        }

        updatePreferencesData();

        Toast.makeText(this, "Configurazioni salvate", Toast.LENGTH_SHORT).show();

        ActivityFlowHelper.goToActivity(this, MainActivity.class);
    }

    private void updatePreferencesData(){
        // Leggiamo le Preferences
        SharedPreferences prefs = getSharedPreferences(GlobalConstants.CONFIG_PREFS, Context.MODE_PRIVATE);
        // Leggiamo l'informazione associata alla proprietà TEXT_DATA
        String cfgURL = prefs.getString(GlobalConstants.CONFIG_URL, "No Preferences!");
        // Lo impostiamo alla TextView
        txtConfigURL.setText(cfgURL);
        // Lo imposto nella variabile globale
        GlobalConstants.webSiteUrl = cfgURL;
    }


}
