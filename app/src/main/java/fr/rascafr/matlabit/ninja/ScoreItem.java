package fr.rascafr.matlabit.ninja;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by root on 14/04/16.
 */
public class ScoreItem {

    private String login, os;
    private int score, rank;
    private boolean isHeader;
    private boolean isLegend;

    public ScoreItem (JSONObject obj) throws JSONException {
        this.login = obj.getString("login");
        this.score = obj.getInt("score");
        this.os = obj.getString("os");
        this.rank = obj.getInt("order");
    }

    public ScoreItem(String login, boolean isHeader) {
        this.login = login;
        this.isHeader = isHeader;
        this.isLegend = !isHeader;
    }

    public String getLogin() {
        return login;
    }

    public int getScore() {
        return score;
    }

    public String getOs() {
        return os;
    }

    public int getRank() {
        return rank;
    }

    public boolean isHeader() {
        return isHeader;
    }

    public boolean isLegend() {
        return isLegend;
    }
}
