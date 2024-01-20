package com.example.rentalu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class signup extends AppCompatActivity {

    private EditText password, confirmPassword, name, email, phoneNo;
    private Button SignupBtn;

    private DBHelper dbHelper;

    private TextView loginText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        dbHelper = new DBHelper(this, "RentalDB", null, 1);

        SignupBtn = findViewById(R.id.btnSignup);
        password = findViewById(R.id.editTextPassword);
        confirmPassword = findViewById(R.id.editTextConfirmPassword);
        name = findViewById(R.id.editTextName);
        email = findViewById(R.id.editTextEmail);
        phoneNo = findViewById(R.id.editTextPhoneNumber);
        loginText = findViewById(R.id.txtLogin);

        loginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(signup.this, login.class);
                startActivity(intent);
            }
        });
        SignupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (password.getText().toString().equals(confirmPassword.getText().toString())) {
                    String s_name = name.getText().toString();
                    String s_pass = password.getText().toString();
                    String s_ph = phoneNo.getText().toString();
                    String s_email = email.getText().toString();

                    // Call the signup function with user details
                    signupUser(s_name, s_pass, s_ph, s_email);
                } else {
                    // Passwords do not match, show an error or handle accordingly
                    // For example, you can display a toast message
                    Toast.makeText(signup.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void signupUser(String username, String password, String phoneNo, String email) {
        // Use DBHelper to add a new user to the database
        dbHelper.addUser(username, password, phoneNo, email);

        // Optionally, you can provide feedback to the user that signup was successful
        // For example, display a toast message or navigate to the login screen
        Toast.makeText(signup.this, "Signup successful", Toast.LENGTH_SHORT).show();
        // You can also navigate to the login screen or perform any other desired action
    }


}
