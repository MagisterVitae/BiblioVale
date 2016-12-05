package dev.sturmtruppen.bibliovale.businessLogic.DataFetchers;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Matteo on 02/12/2016.
 */

public class DBApiResponse {
    private int statusId;
    private String statusDescription;

    public DBApiResponse(){super();}
    public DBApiResponse(int _statusId, String _statusDescription){
        this.statusId = _statusId;
        this.statusDescription = _statusDescription;
    }
    public DBApiResponse(JSONObject _jsonResponse){
        try {
            this.statusId = _jsonResponse.getInt("status_id");
            this.statusDescription = _jsonResponse.getString("status_desc");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    public String getStatusDescription() {
        return statusDescription;
    }

    public void setStatusDescription(String statusDescription) {
        this.statusDescription = statusDescription;
    }
}
