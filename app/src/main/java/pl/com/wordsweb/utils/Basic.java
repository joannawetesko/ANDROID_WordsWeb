package pl.com.wordsweb.utils;

import android.util.Base64;

import static pl.com.wordsweb.config.Constants.PASSWORD;
import static pl.com.wordsweb.config.Constants.USER;

/**
 * Created by wewe on 09.10.16.
 */

public class Basic {
    public static String getBasic() {
        String credentials = USER + ":" + PASSWORD;
        return "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
    }
}
