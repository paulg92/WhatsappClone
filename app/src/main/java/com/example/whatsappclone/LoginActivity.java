package com.example.whatsappclone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.shashank.sony.fancytoastlib.FancyToast;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edtEmail, edtPassword;
    private Button btnLogin, btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtEmail = findViewById(R.id.edtUsernameLoginActivity);
        edtPassword = findViewById(R.id.edtPasswordLoginActivity);

        btnSignUp = findViewById(R.id.btnSignupLoginActivty);
        btnLogin = findViewById(R.id.btnLoginActivityLogin);

        btnSignUp.setOnClickListener(this);
        btnLogin.setOnClickListener(this);

        if (ParseUser.getCurrentUser() != null) {
            ParseUser.getCurrentUser();
            ParseUser.logOut();
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSignupLoginActivty:
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
                break;
            case R.id.btnLoginActivityLogin:
                ParseUser.logInInBackground(edtEmail.getText().toString(), edtPassword.getText().toString(), new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {
                        if (e == null) {
                            FancyToast.makeText(LoginActivity.this, ParseUser.
                                            getCurrentUser().getUsername() + " is logged in",
                                    Toast.LENGTH_SHORT, FancyToast.SUCCESS, true).show();
                            transitionToWhatsAppActivity();
                            finish();
                        } else {
                            FancyToast.makeText(LoginActivity.this, e.getMessage(),
                                    Toast.LENGTH_SHORT, FancyToast.ERROR, true).show();
                        }
                    }
                });
                break;
        }
    }

    private void transitionToWhatsAppActivity() {
        Intent intent = new Intent(LoginActivity.this, WhatsAppUsersActivity.class);
        startActivity(intent);
    }
}
