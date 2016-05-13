package fr.rascafr.matlabit.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by root on 13/04/16.
 */
public class RegexUtils {

    public static final String REGEX_HEXA_16 = "^([0-9a-fA-F]{4})+$";

    public static boolean regexValidation(String s, String pattern) {
        try {
            Pattern pattrn = Pattern.compile(pattern);
            Matcher matcher = pattrn.matcher(s);
            return matcher.matches();
        } catch (RuntimeException e) {
            return false;
        }
    }
}
