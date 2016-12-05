package dev.sturmtruppen.bibliovale.businessLogic.Helpers;

import android.os.Bundle;

/**
 * Created by Matteo on 03/12/2016.
 */

public class PutExtraPair<T> {
    private String key;
    private T value;

    public PutExtraPair(){super();}

    public PutExtraPair(String key, T value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }
}
