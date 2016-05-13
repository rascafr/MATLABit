package fr.rascafr.matlabit.fruiter.list;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by root on 23/04/16.
 */
public class ListItem {

    private String name, imgUrl, login;
    private int others;
    private boolean mutual, isHeader, isEmpty, isYours;

    public ListItem (JSONObject obj, boolean isYours) throws JSONException {
        this.name = obj.getString("name");
        this.login = obj.getString("login");
        this.imgUrl = obj.getString("img");
        this.others = obj.getInt("others");
        this.mutual = obj.getBoolean("mutual");
        this.isYours = isYours;
        this.isHeader = false;
    }

    public ListItem (String name) {
        this.name = name;
        this.isHeader = true;
    }

    public ListItem (String name, boolean isEmpty) {
        this.name = name;
        this.isHeader = false;
        this.isEmpty = isEmpty;
    }

    public String getLogin() {
        return login;
    }

    public boolean isYours() {
        return isYours;
    }

    public void setHeader(boolean header) {
        isHeader = header;
    }

    public boolean isHeader() {
        return isHeader;
    }

    public boolean isEmpty() {
        return isEmpty;
    }

    public String getName() {
        return name;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public int getOthers() {
        return others;
    }

    public boolean isMutual() {
        return mutual;
    }
}
