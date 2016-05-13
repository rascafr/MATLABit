package fr.rascafr.matlabit.utils;

import org.json.JSONObject;

/**
 * Created by root on 13/04/16.
 */
public class APIResponse {

    private int status;
    private String errorMsg;
    private JSONObject jsonData;

    private final static int ERR_NETWORK = 0;
    private final static String ERR_NETWORK_MSG = "Connexion impossible : erreur réseau";
    public final static int HTTP_OK = 200;
    public final static int CODE_OK = 1;

    public APIResponse () {
        this.status = ERR_NETWORK;
        this.errorMsg = ERR_NETWORK_MSG;
        this.jsonData = new JSONObject();
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public void setJsonData(JSONObject jsonData) {
        this.jsonData = jsonData;
    }

    public boolean isValid () {
        return status == CODE_OK || status == HTTP_OK;
    }

    @Override
    public String toString() {
        return "APIResponse{" +
                "status=" + status +
                ", errorMsg='" + errorMsg + '\'' +
                ", jsonData=" + jsonData +
                '}';
    }

    public JSONObject getJsonData() {
        return jsonData;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public int getStatus() {
        return status;
    }
}
