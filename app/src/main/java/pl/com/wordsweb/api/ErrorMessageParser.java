package pl.com.wordsweb.api;

import pl.com.wordsweb.config.Constants;

/**
 * Created by wewe on 22.12.16.
 */

public class ErrorMessageParser {

    public static String getErrorMessage(int code) {
        switch (code) {
            case 503:
                return Constants.SERVICE_UNAVAILABLE;
            case 500:
                return Constants.SERVICE_UNAVAILABLE;
            case 404:
                return Constants.NOT_FOUND;
            case 401:
                return Constants.NO_AUTH;
            default:
                return Constants.UNKNOWN_ERROR;
        }

    }
}
