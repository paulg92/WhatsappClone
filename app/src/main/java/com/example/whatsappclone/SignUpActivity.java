package com.example.whatsappclone;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edtEmail, edtUsername, edtPassword;
    private Button mBtnLogin, mBtnSignUp;

    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    //"(?=.*[0-9])" +         //at least 1 digit
                    //"(?=.*[a-z])" +         //at least 1 lower case letter
                    //"(?=.*[A-Z])" +         //at least 1 upper case letter
                    "(?=.*[a-zA-Z])" +      //any letter
                    "(?=.*[@#$%^&+=])" +    //at least 1 special character
                    "(?=\\S+$)" +           //no white spaces
                    ".{4,}" +               //at least 4 characters
                    "$");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtEmail = findViewById(R.id.edtEmailSignUpActivity);
        edtUsername = findViewById(R.id.edtUsernameSignupActivity);
        edtPassword = findViewById(R.id.edtPasswordSignUpActivity);

        mBtnSignUp = findViewById(R.id.btnSignUpSgpActivity);
        mBtnLogin = findViewById(R.id.btnLoginSignupActivity);

        mBtnSignUp.setOnClickListener(this);
        mBtnLogin.setOnClickListener(this);

        if (ParseUser.getCurrentUser() != null) {
            ParseUser.getCurrentUser();
            ParseUser.logOut();
            //transitionToWhatsAppActivity();
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btnSignUpSgpActivity:
                if (!validateEmail() | !validateUsername() | !validatePassword()) {
                    return;
                } else {
                    final ParseUser appUser = new ParseUser();
                    appUser.setEmail(edtEmail.getText().toString());
                    appUser.setUsername(edtUsername.getText().toString());
                    appUser.setPassword(edtPassword.getText().toString());

                    final ProgressDialog progressDialog = new ProgressDialog(SignUpActivity.this);
                    progressDialog.setMessage("Signin in " + edtUsername.getText().toString());
                    progressDialog.show();

                    appUser.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                FancyToast.makeText(SignUpActivity.this, edtUsername.
                                                getText().toString() + " has been saved successfully!",
                                        FancyToast.LENGTH_LONG, FancyToast.SUCCESS, true).show();
                                transitionToWhatsAppActivity();
                                finish();
                            } else {
                                FancyToast.makeText(SignUpActivity.this, e.getMessage(),
                                        FancyToast.LENGTH_LONG, FancyToast.ERROR, true).show();
                            }
                            progressDialog.dismiss();
                        }
                    });
                }
                break;
            case R.id.btnLoginSignupActivity:
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }

    private void transitionToWhatsAppActivity() {
        Intent intent = new Intent(SignUpActivity.this, WhatsAppUsersActivity.class);
        startActivity(intent);
    }

    private boolean validateEmail() {
        String emailInput = edtEmail.getText().toString().trim();

        if (emailInput.isEmpty()) {
            edtEmail.setError("Field can't be empty");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            edtEmail.setError("Please enter a valid email address");
            return false;
        } else {
            edtEmail.setError(null);
            return true;
        }
    }

    private boolean validateUsername() {
        String usernameInput = edtUsername.getText().toString().trim();

        if (usernameInput.isEmpty()) {
            edtUsername.setError("Field can't be empty");
            return false;
        } else if (usernameInput.length() > 15) {
            edtUsername.setError("Username too long");
            return false;
        } else {
            edtUsername.setError(null);
            return true;
        }
    }

    private boolean validatePassword() {
        String passwordInput = edtPassword.getText().toString().trim();

        if (passwordInput.isEmpty()) {
            edtPassword.setError("Field can't be empty");
            return false;
        } else if (!PASSWORD_PATTERN.matcher(passwordInput).matches()) {
            edtPassword.setError("Password too weak please enter minimum 4 " +
                    "letters and a special character");
            return false;
        } else {
            edtPassword.setError(null);
            return true;
        }
    }

}
