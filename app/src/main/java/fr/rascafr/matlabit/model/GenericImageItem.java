package fr.rascafr.matlabit.model;

/**
 * Created by root on 18/04/16.
 */
public class GenericImageItem extends GenericItem {

    private String imgUrl;
    private String details;

    public GenericImageItem(String imgUrl, String name, String details) {
        super(name);
        this.imgUrl = imgUrl;
        this.details = details;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public String getDetails() {
        return details;
    }
}
