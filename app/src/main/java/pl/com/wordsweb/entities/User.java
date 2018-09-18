package pl.com.wordsweb.entities;

/**
 * Created by wewe on 11.04.16.
 */
public class User {

    private String email;

    private String password;
    private String passwordConfirmation;

    public User(String email, String password, String passwordConfirmation) {
        this.email = email;
        this.password = password;
        this.passwordConfirmation = passwordConfirmation;
    }

    public boolean chechPassword() {
        return (password.equals(passwordConfirmation));
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getPasswordConfirmation() {
        return passwordConfirmation;
    }
}
