package pl.com.wordsweb.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;

import pl.com.wordsweb.R;
import pl.com.wordsweb.api.ErrorMessageParser;
import pl.com.wordsweb.config.AppSettings;
import pl.com.wordsweb.entities.Token;
import pl.com.wordsweb.storage.SharedPreferencesProvider;
import pl.com.wordsweb.ui.activities.MainActivity;
import pl.com.wordsweb.utils.Basic;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static pl.com.wordsweb.config.Constants.FOUR_ZERO_ONE;
import static pl.com.wordsweb.config.Constants.NO_INTERNET_CONNECTION;
/**
 * Created by wewe on 08.04.16.
 */

public  class LoginFragment extends Fragment {


    public LoginFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_login, container, false);

        final EditText usernameEditText = (EditText) rootView.findViewById(R.id.username);
        final EditText passwordEditText = (EditText) rootView.findViewById(R.id.password);

        Button loginButton = (Button) rootView.findViewById(R.id.sign_in_button);
        loginButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                getToken(usernameEditText.getText().toString(), passwordEditText.getText().toString(), rootView);
            }


        });

        return rootView;
    }


    public void getToken(String username, String password, final View rootView) {

        Call<Token> call = AppSettings.userApi.generateAccesTokenLogin(username, password, "password", Basic.getBasic());
        call.enqueue(new Callback<Token>() {
            @Override
            public void onResponse(Call<Token> call, Response<Token> response) {
                Log.d("CallBack", " code is " + response.code());
                if (response.isSuccessful()){
                    Log.d("CallBack", " response is " + new Gson().toJson(response.body()));
                    Token token = response.body();
                    SharedPreferencesProvider preferences = new SharedPreferencesProvider(getActivity());
                    preferences.setNewRefreshToken(token);
                    Log.d("ACCESS_TOKEN_IS: ", token.getAccess_token() + " " + token.getToken_type());
                    startNewActivity();
                    getActivity().finish();
                }
                else{
                    Log.d("CallBack", " response is " + response.code());
                    showErrorSnackbar(response.code(), rootView);

                }
            }

            @Override
            public void onFailure(Call<Token> call, Throwable t) {
                Snackbar.make(rootView, NO_INTERNET_CONNECTION, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

            }
        });

    }

    public void startNewActivity() {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);

    }


    public void showErrorSnackbar(int code, View rootView){
        if (code == 401){
            Snackbar.make(rootView, FOUR_ZERO_ONE, Snackbar.LENGTH_LONG)
                    .show();
        } else {
            Snackbar.make(rootView, ErrorMessageParser.getErrorMessage(code), Snackbar.LENGTH_LONG).show();
        }

    }




}