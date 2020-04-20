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
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class PassengerSignInActivity extends AppCompatActivity {

    private static final String TAG = "PassengerSignInActivity";

    private TextInputLayout textInputEmailPass;
    private TextInputLayout textInputNamePass;
    private TextInputLayout textInputPasswordPass;
    private TextInputLayout textInputPasswordConfirmPass;

    private TextView toggleLoginSingUpTextViewPass;
    private Button signUpButtonPass;
    private boolean isLoginModeActive;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_sign_in);
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            startActivity(new Intent(this,PassengerMapsActivity.class));
        }

        mAuth = FirebaseAuth.getInstance();
        textInputEmailPass = findViewById(R.id.textInputEmailPass);
        textInputNamePass = findViewById(R.id.textInputNamePass);
        textInputPasswordPass = findViewById(R.id.textInputPasswordPass);
        textInputPasswordConfirmPass = findViewById(R.id.textInputPasswordConfirmPass);

        toggleLoginSingUpTextViewPass = findViewById(R.id.toggleLoginSingUpTextViewPass);
        signUpButtonPass = findViewById(R.id.signUpButtonPass);

    }

    private boolean validEmail() {
        String emailInput = textInputEmailPass.getEditText().getText().toString().trim();

        if (emailInput.isEmpty()) {
            textInputEmailPass.setError("Please input your email");
            return false;
        } else {
            textInputEmailPass.setError("");
            return true;
        }
    }

    private boolean validName() {
        String nameInput = textInputNamePass.getEditText().getText().toString().trim();

        if (nameInput.isEmpty()) {
            textInputNamePass.setError("Please input your name");
            return false;
        } else if (nameInput.length() > 15) {
            textInputNamePass.setError("Name lenght have to be less than 15");
            return false;
        } else {
            textInputNamePass.setError("");
            return true;
        }
    }

    private boolean validPassword() {
        String passwordInput = textInputPasswordPass.getEditText().getText().toString().trim();
        String confirmPasswordInput = textInputPasswordConfirmPass.getEditText().getText().toString().trim();

        if (passwordInput.isEmpty()) {
            textInputPasswordPass.setError("Please input your password");
            return false;
        } else if (passwordInput.length() < 7) {
            textInputPasswordPass.setError("Name lenght have to be more than 7");
            return false;
        } else {
            textInputPasswordPass.setError("");
            return true;
        }
    }

    private boolean validConfirmPassword() {
        String passwordInput = textInputPasswordPass.getEditText().getText().toString().trim();
        String confirmPasswordInput = textInputPasswordConfirmPass.getEditText().getText().toString().trim();

        if (!passwordInput.equals(confirmPasswordInput)) {
            textInputPasswordConfirmPass.setError("Passwords have to match");
            return false;
        } else {
            textInputPasswordPass.setError("");
            return true;
        }
    }

    public void loginSignUpUser(View view) {
        if (!validEmail() | !validName() | !validPassword()) {
            return;
        }
        String email = textInputEmailPass.getEditText().getText().toString().trim();
        String password = textInputPasswordPass.getEditText().getText().toString().trim();
        if (isLoginModeActive) {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                FirebaseUser user = mAuth.getCurrentUser();
                                Log.d(TAG, "signInWithEmail:success");
                                startActivity(new Intent(PassengerSignInActivity.this, PassengerMapsActivity.class));
                                //updateUI(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(PassengerSignInActivity.this, "Authentication failed.",
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
                                startActivity(new Intent(PassengerSignInActivity.this, PassengerMapsActivity.class));
                                //updateUI(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(PassengerSignInActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                //updateUI(null);
                            }

                            // ...
                        }
                    });

        }
    }

    public void toggleLoginSignUp(View view) {
        if(isLoginModeActive) {
            isLoginModeActive = false;
            signUpButtonPass.setText("Sign Up");
            toggleLoginSingUpTextViewPass.setText("Or, log in");
            textInputPasswordConfirmPass.setVisibility(View.VISIBLE);
        } else {
            isLoginModeActive = true;
            signUpButtonPass.setText("Log in");
            toggleLoginSingUpTextViewPass.setText("Or, sign up");
            textInputPasswordConfirmPass.setVisibility(View.GONE);
        }
    }
}
