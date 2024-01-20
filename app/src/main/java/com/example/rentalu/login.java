package com.example.rentalu;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class login extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private Button loginButton;
    private TextView signUpLink;

    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        dbHelper = new DBHelper(this, "RentalDB", null, 1);

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        signUpLink = findViewById(R.id.txtSignup);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get entered email and password
                String enteredEmail = emailEditText.getText().toString().trim();
                String enteredPassword = passwordEditText.getText().toString().trim();

                // Validate and perform login using DBHelper
                performLogin(enteredEmail, enteredPassword);
            }
        });

        signUpLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the click event to navigate to the signup page
                navigateToSignUp();
            }
        });
    }

    private void performLogin(String email, String password) {
        // Check if the entered email and password match with any user in the database
        int userId = dbHelper.getUserId(email, password); // Assume you have a method to get user_id

        if (userId != -1) {
            navigateToView(userId);
        } else {

            Toast.makeText(login.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
        }
    }

    private void navigateToView(int userId) {
        // Create an Intent to start the MainActivity or your home screen
        Intent intent = new Intent(login.this, viewPosts.class);
        intent.putExtra("user_id", userId);
        startActivity(intent);


        finish();
    }

    private void navigateToSignUp() {
        // Create an Intent to start the SignUpActivity
        Intent intent = new Intent(login.this, signup.class);
        startActivity(intent);
    }
}
