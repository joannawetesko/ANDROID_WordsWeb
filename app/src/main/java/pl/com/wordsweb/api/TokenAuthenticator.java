package pl.com.wordsweb.api;


import android.content.Context;
import android.util.Log;

import java.io.IOException;

import okhttp3.Authenticator;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;
import pl.com.wordsweb.config.AppSettings;
import pl.com.wordsweb.entities.Token;
import pl.com.wordsweb.event.OnMustLogoutEvent;
import pl.com.wordsweb.storage.SharedPreferencesProvider;
import pl.com.wordsweb.utils.Basic;
import retrofit2.Call;
import retrofit2.Callback;

import static pl.com.wordsweb.config.AppSettings.bus;

/**
 * Created by wewe on 16.10.16.
 */

public class TokenAuthenticator implements Authenticator {
    private Context context;
    private int mCounter = 0;
    private boolean needLogout = false;
    public TokenAuthenticator(Context context) {
        this.context = context;
        this.mCounter = 0;
    }

    @Override
    public Request authenticate(Route route, Response response) throws IOException {
        needLogout = false;
        if (mCounter++ > 5) {
            // in case to much request
            Log.d("OKHTTP", "Fail more then 2 times");
            return null;
        }
        else {
            // everything is ok, it will try 2 times - in case problem with too long response
            final SharedPreferencesProvider sharedPreferencesProvider = new SharedPreferencesProvider(context);
            Token token = sharedPreferencesProvider.getAcessToken();
            Log.d("HEADERS: ", " " + response.headers());
            Call<Token> call = AppSettings.userApi.getRefreshToken(Basic.getBasic(), token.getRefresh_token(), "refresh_token");
            call.enqueue(new Callback<Token>() {
                @Override
                public void onResponse(Call<Token> call, retrofit2.Response<Token> response) {
                    if (response.isSuccessful()) {
                        Token newToken = response.body();
                        sharedPreferencesProvider.setNewRefreshToken(newToken);
                        mCounter = 0;
                        Log.d("REFRESH TOKEN", " New refreshed token cached");
                        Log.d("RESPONSE CODE", " " + response.code());

                    }
                    if (mCounter > 3) {

                        Log.d("Need to relogin,", "" + response.code());
                        bus.post(new OnMustLogoutEvent());
                        needLogout = true;


                    }
                }

                @Override
                public void onFailure(Call<Token> call, Throwable t) {
                    Log.d("CallBack", " Throwable is " + t);
                    Log.d("CallBack", " Throwable is " + t.getMessage());
                    Log.d("CallBack", " Throwable is " + t.getLocalizedMessage());
                }
            });
            if (needLogout) return null;
            return response.request().newBuilder()
                    .header("Authorization", sharedPreferencesProvider.getAcessToken().getToken())
                    .header("Connection", "close")
                    .build();
        }
    }

}
