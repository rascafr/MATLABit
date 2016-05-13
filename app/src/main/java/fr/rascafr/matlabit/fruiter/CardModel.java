/**
 * AndTinder v0.1 for Android
 *
 * @Author: Enrique López Mañas <eenriquelopez@gmail.com>
 * http://www.lopez-manas.com
 *
 * TAndTinder is a native library for Android that provide a
 * Tinder card like effect. A card can be constructed using an
 * image and displayed with animation effects, dismiss-to-like
 * and dismiss-to-unlike, and use different sorting mechanisms.
 *
 * AndTinder is compatible with API Level 13 and upwards
 *
 * @copyright: Enrique López Mañas
 * @license: Apache License 2.0
 */

package fr.rascafr.matlabit.fruiter;

import org.json.JSONException;
import org.json.JSONObject;

public class CardModel {

	private String   title, imgUri, login;

	public CardModel(JSONObject obj) throws JSONException {
		this.title = obj.getString("name");
		this.login = obj.getString("login");
		this.imgUri = obj.getString("img");
	}

	@Override
	public String toString() {
		return "CardModel{" +
				"title='" + title + '\'' +
				", imgUri='" + imgUri + '\'' +
				", login='" + login + '\'' +
				'}';
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getImgUri() {
		return imgUri;
	}

	public String getLogin() {
		return login;
	}
}
