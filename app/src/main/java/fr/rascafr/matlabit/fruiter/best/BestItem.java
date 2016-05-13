package fr.rascafr.matlabit.fruiter.best;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by root on 23/04/16.
 */
public class BestItem {

    private String name, imgUrl;
    private int howMany;

    public BestItem (JSONObject obj) throws JSONException {
        this.name = obj.getString("name");
        this.imgUrl = obj.getString("img");
        this.howMany = obj.getInt("nbr");
    }

    public String getName() {
        return name;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public int getHowMany() {
        return howMany;
    }
}
