package pl.com.wordsweb.ui.fragments;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import pl.com.wordsweb.R;
import pl.com.wordsweb.config.AppSettings;
import pl.com.wordsweb.entities.Token;
import pl.com.wordsweb.entities.User;
import pl.com.wordsweb.storage.SharedPreferencesProvider;
import pl.com.wordsweb.ui.utils.PhraseWebAlertDialog;
import pl.com.wordsweb.utils.Basic;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by wewe on 08.04.16.
 */
public class RegisterFragment extends Fragment {

    TextView emailTextView;
    TextView passwordTextView;
    TextView passwordConfirmationTextView;
    private ProgressBar progressBar;
    public RegisterFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_register, container, false);
        final TextInputLayout passwordInputLayout = (TextInputLayout) rootView.findViewById(R.id.password_il);
        final TextInputLayout repeatPasswordInputLayout = (TextInputLayout) rootView.findViewById(R.id.repeat_password_il);
        emailTextView = (TextView) rootView.findViewById(R.id.email);
        passwordTextView = (TextView) rootView.findViewById(R.id.password);
        passwordConfirmationTextView = (TextView) rootView.findViewById(R.id.repeat_password);
        final SharedPreferencesProvider sharedPreferencesProvider = new SharedPreferencesProvider(getContext());
        progressBar = (ProgressBar) rootView.findViewById(R.id.login_progress);

        Button joinButton = (Button) rootView.findViewById(R.id.join_button);
        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final User user = new User(emailTextView.getText().toString(),

                        passwordTextView.getText().toString(),
                        passwordConfirmationTextView.getText().toString());
                if (user.chechPassword()) {
                    progressBar.setVisibility(View.VISIBLE);
                    Call<Token> call = AppSettings.userApi.generateAccesTokenRegister(Basic.getBasic(), "client_credentials");
                    call.enqueue(new Callback<Token>() {
                        @Override
                        public void onResponse(Call<Token> call, Response<Token> response) {
                            if (response.isSuccessful()) {
                                final Token registerToken = response.body();
                                sharedPreferencesProvider.setNewRegisterToken(registerToken);
                                registerUser(user, registerToken);
                            }
                        }

                        @Override
                        public void onFailure(Call<Token> call, Throwable t) {
                            progressBar.setVisibility(View.GONE);
                        }
                    });
                } else {
                    passwordInputLayout.setError(getString(R.string.error_different_password));
                    repeatPasswordInputLayout.setError(getString(R.string.error_different_password));
                }



            }


        });
        passwordTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (passwordTextView.getText().length() > 0)
                    passwordInputLayout.setError(null);
                repeatPasswordInputLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        passwordConfirmationTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (passwordConfirmationTextView.getText().length() > 0)
                    passwordInputLayout.setError(null);
                repeatPasswordInputLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        return rootView;
    }

    public void registerUser(User user, Token token) {
        Call<okhttp3.ResponseBody> call = AppSettings.userApi.registerUser(user, token.getToken());
        call.enqueue(new Callback<okhttp3.ResponseBody>() {
            @Override
            public void onResponse(Call<okhttp3.ResponseBody> call, Response<okhttp3.ResponseBody> response) {
                Log.d("RESPONSE CODE", " " + response.code());
                progressBar.setVisibility(View.GONE);
                if (response.code() == 201) {
                    Log.d("RESPONSE: ", "Check e-mail");
                    PhraseWebAlertDialog.showNeedConfirmEmail(getActivity());
                    clearTextFields();

                }
                if (response.code() == 409) {
                    Log.d("RESPONSE: ", "user exist");
                    Toast toast = Toast.makeText(getContext(), R.string.user_exist, Toast.LENGTH_LONG);
                    toast.show();
                }
            }

            @Override
            public void onFailure(Call<okhttp3.ResponseBody> call, Throwable t) {

            }
        });
    }

    private void clearTextFields() {
        passwordTextView.setText("");
        passwordConfirmationTextView.setText("");
        emailTextView.setText("");
    }

}
