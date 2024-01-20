package com.example.rentalu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class addRental extends AppCompatActivity {

    private EditText propertyTypeEditText, bedroomEditText, locationEditText,
            priceEditText, furniTypesEditText, phoneNoEditText, remarksEditText, reporterEditText;

    private Button btnSubmit, btnView;
    private int userID;
    private List<RentalPost> rentalList;
    private RentalAdapter rentalAdapter;

    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_rental);
        Intent intent = getIntent();
        userID = intent.getIntExtra("user_id", -1);

        dbHelper = new DBHelper(this, "RentalDB", null, 1);

        propertyTypeEditText = findViewById(R.id.propertyType);
        bedroomEditText = findViewById(R.id.txtBedroom);
        locationEditText = findViewById(R.id.location);
        priceEditText = findViewById(R.id.price);
        furniTypesEditText = findViewById(R.id.furnitureType);
        phoneNoEditText = findViewById(R.id.phoneNo);
        remarksEditText = findViewById(R.id.remark);
        reporterEditText = findViewById(R.id.reporter);

        rentalAdapter = new RentalAdapter(this, rentalList, userID);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addRental();
            }
        });

        btnView = findViewById(R.id.btnView);
        btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(addRental.this,viewPosts.class);
                startActivity(i);
                finish();
            }
        });

    }

    private void addRental() {
        String propertyType = propertyTypeEditText.getText().toString().trim();
        String bedroom = bedroomEditText.getText().toString().trim();
        String location = locationEditText.getText().toString().trim();
        String price = priceEditText.getText().toString().trim();
        String furniTypes = furniTypesEditText.getText().toString().trim();
        String phoneNo = phoneNoEditText.getText().toString().trim();
        String remarks = remarksEditText.getText().toString().trim();
        String reporter = reporterEditText.getText().toString().trim();

        if (propertyType.isEmpty() || bedroom.isEmpty() || location.isEmpty() || price.isEmpty() || reporter.isEmpty()) {
            Toast.makeText(this, "Please fill in all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        dbHelper.addRental(userID, propertyType, bedroom, location, price, furniTypes, phoneNo, remarks, reporter);
        clearInputFields();
        rentalList = dbHelper.getAllRentalPosts();
        rentalAdapter.updateData(rentalList);

        Toast.makeText(this, "Rental added successfully", Toast.LENGTH_SHORT).show();
    }

    private void clearInputFields() {
        propertyTypeEditText.setText("");
        bedroomEditText.setText("");
        locationEditText.setText("");
        priceEditText.setText("");
        furniTypesEditText.setText("");
        phoneNoEditText.setText("");
        remarksEditText.setText("");
        reporterEditText.setText("");
    }
}
