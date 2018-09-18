package pl.com.wordsweb.storage;

import android.content.Context;
import android.content.SharedPreferences;

import pl.com.wordsweb.entities.Token;

import static pl.com.wordsweb.config.Constants.REGISTER_TOKEN_PREFERENCES;
import static pl.com.wordsweb.config.Constants.TOKEN;
import static pl.com.wordsweb.config.Constants.TOKEN_PREFERENCES;
import static pl.com.wordsweb.config.Constants.TOKEN_REFRESH;
import static pl.com.wordsweb.config.Constants.TOKEN_TYPE;

/**
 * Created by wewe on 27.04.16.
 */
public class SharedPreferencesProvider {

    private SharedPreferences accessTokenPreferences;
    private SharedPreferences registerTokenPreferences;
    private Token token = new Token();


    public SharedPreferencesProvider(Context context){
        accessTokenPreferences = context.getSharedPreferences(TOKEN_PREFERENCES, Context.MODE_PRIVATE);
        registerTokenPreferences = context.getSharedPreferences(REGISTER_TOKEN_PREFERENCES, Context.MODE_PRIVATE);
    }


    public void setNewRefreshToken(Token token) {
        this.token = token;
        setToken(accessTokenPreferences);
    }

    public void setNewRegisterToken(Token token) {
        this.token = token;
        setToken(registerTokenPreferences);
    }

    private void setToken(SharedPreferences preferences) {
        preferences.edit().putString(TOKEN, this.token.getAccess_token()).apply();
        preferences.edit().putString(TOKEN_TYPE, this.token.getToken_type()).apply();
        preferences.edit().putString(TOKEN_REFRESH, this.token.getRefresh_token()).apply();

    }

    public Token getAcessToken() {
        return getToken(accessTokenPreferences);
    }

    public Token getRegisterToken() {
        return getToken(registerTokenPreferences);
    }


    private Token getToken(SharedPreferences preferences) {
        this.token.setAccess_token(preferences.getString(TOKEN, ""));
        this.token.setToken_type(preferences.getString(TOKEN_TYPE, ""));
        this.token.setRefresh_token(preferences.getString(TOKEN_REFRESH, ""));
        return token;
    }

    public void removeToken(){
        accessTokenPreferences.edit().clear().apply();
    }

    public boolean checkToken(){
        if (accessTokenPreferences.getString(TOKEN, "").isEmpty())
            return false;
        else
            return true;
    }


}
