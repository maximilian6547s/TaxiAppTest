package com.example.taxiapptest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class DriverSignInActivity extends AppCompatActivity {

    private static final String TAG = "DriverSignInActivity";
    private TextInputLayout textInputEmail;
    private TextInputLayout textInputName;
    private TextInputLayout textInputPassword;
    private TextInputLayout textInputPasswordConfirm;

    private TextView toggleLoginSingUpTextView;
    private Button signUpButton;
    private boolean isLoginModeActive;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_sign_in);
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            startActivity(new Intent(this,DriverMapsActivity.class));
        }

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        textInputEmail = findViewById(R.id.textInputEmail);
        textInputName = findViewById(R.id.textInputName);
        textInputPassword = findViewById(R.id.textInputPassword);
        textInputPasswordConfirm = findViewById(R.id.textInputPasswordConfirm);

        toggleLoginSingUpTextView = findViewById(R.id.toggleLoginSingUpTextView);
        signUpButton = findViewById(R.id.signUpButton);

    }

    private boolean validEmail() {
        String emailInput = textInputEmail.getEditText().getText().toString().trim();

        if (emailInput.isEmpty()) {
            textInputEmail.setError("Please input your email");
            return false;
        } else {
            textInputEmail.setError("");
            return true;
        }
    }

    private boolean validName() {
        String nameInput = textInputName.getEditText().getText().toString().trim();

        if (nameInput.isEmpty()) {
            textInputName.setError("Please input your name");
            return false;
        } else if (nameInput.length() > 15) {
            textInputName.setError("Name lenght have to be less than 15");
            return false;
        } else {
            textInputName.setError("");
            return true;
        }
    }

    private boolean validPassword() {
        String passwordInput = textInputPassword.getEditText().getText().toString().trim();

        if (passwordInput.isEmpty()) {
            textInputPassword.setError("Please input your password");
            return false;
        } else if (passwordInput.length() < 7) {
            textInputPassword.setError("Name lenght have to be more than 7");
            return false;
        } else {
            textInputPassword.setError("");
            return true;
        }
    }

    private boolean validConfirmPassword() {
        String passwordInput = textInputPassword.getEditText().getText().toString().trim();
        String confirmPasswordInput = textInputPasswordConfirm.getEditText().getText().toString().trim();

       if (!passwordInput.equals(confirmPasswordInput)) {
            textInputPasswordConfirm.setError("Passwords have to match");
            return false;
        } else {
            textInputPassword.setError("");
            return true;
        }
    }

    public void loginSignUpUser(View view) {
        if (!validEmail() | !validName() | !validPassword()) {
            return;
        }
        String email = textInputEmail.getEditText().getText().toString().trim();
        String password = textInputPassword.getEditText().getText().toString().trim();
        if (isLoginModeActive) {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                FirebaseUser user = mAuth.getCurrentUser();
                                Log.d(TAG, "signInWithEmail:success");
                                startActivity(new Intent(DriverSignInActivity.this, DriverMapsActivity.class));
                                //updateUI(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(DriverSignInActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                //updateUI(null);
                                // ...
                            }

                            // ...
                        }
                    });

        } else {
            if (!validEmail() | !validName() | !validPassword() | !validConfirmPassword()) {
                return;
            }
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                FirebaseUser user = mAuth.getCurrentUser();
                                Log.d(TAG, "createUserWithEmail:success");
                                startActivity(new Intent(DriverSignInActivity.this, DriverMapsActivity.class));
                                //updateUI(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(DriverSignInActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                //updateUI(null);
                            }

                            // ...
                        }
                    });

        }
    }





    public void toggleLoginSignUp(View view) {
        if (isLoginModeActive) {
            isLoginModeActive = false;
            signUpButton.setText("Sign Up");
            toggleLoginSingUpTextView.setText("Or, log in");
            textInputPasswordConfirm.setVisibility(View.VISIBLE);
        } else {
            isLoginModeActive = true;
            signUpButton.setText("Log in");
            toggleLoginSingUpTextView.setText("Or, sign up");
            textInputPasswordConfirm.setVisibility(View.GONE);
        }
    }
}
