package dev.sturmtruppen.bibliovale.businessLogic.helpers;

import android.content.Context;
import android.content.Intent;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Matteo on 03/12/2016.
 */

public final class ActivityFlowHelper {

    public static void goToActivity(Context context, Class destinationClass){
        // definisco l'intent
        Intent nextActivity = new Intent(context, destinationClass);
        // passo all'attivazione dell'activity
        context.startActivity(nextActivity);
    }

    public static void goToActivity(Context context, Class destinationClass, List<PutExtraPair> putExtraList){
        // definisco l'intent
        Intent nextActivity = new Intent(context, destinationClass);
        // aggiungo i parametri putExtra
        if(putExtraList != null) {
            for (PutExtraPair pair : putExtraList) {
                nextActivity.putExtra(pair.getKey(), (Serializable) pair.getValue());
            }
        }
        // passo all'attivazione dell'activity
        context.startActivity(nextActivity);
    }

    public static void goToActivity(Context context, Class destinationClass, List<PutExtraPair> putExtraList, List<Integer> flags){
        // definisco l'intent
        Intent nextActivity = new Intent(context, destinationClass);
        // aggiungo i parametri putExtra
        if(putExtraList != null) {
            for (PutExtraPair pair : putExtraList) {
                nextActivity.putExtra(pair.getKey(), (Serializable) pair.getValue());
            }
        }
        // aggiungo i flag
        if(flags != null) {
            for (Integer flag : flags) {
                nextActivity.addFlags(flag);
            }
        }
        // passo all'attivazione dell'activity
        context.startActivity(nextActivity);
    }

}
