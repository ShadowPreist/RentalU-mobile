package com.example.rentalu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class update_rental extends AppCompatActivity {
    private Button editBtn, deleteBtn;
    private DBHelper dbHelper;
    private List<RentalPost> rentalList;
    private RentalAdapter rentalAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_rental);

        dbHelper = new DBHelper(this, "RentalDB", null, 1);
        // Retrieve data from the Intent
        editBtn = findViewById(R.id.edit_btn);
        deleteBtn = findViewById(R.id.delete_btn);

        Intent intent = getIntent();
        int refId = intent.getIntExtra("ref_id", -1);
        int userId = intent.getIntExtra("user_id", -1);
        String propertyType = intent.getStringExtra("property_type");
        String bedroom = intent.getStringExtra("bedroom");
        String location = intent.getStringExtra("location");
        String price = intent.getStringExtra("price");
        String furniType = intent.getStringExtra("furniture_type");
        String contact = intent.getStringExtra("contact");
        String remarks = intent.getStringExtra("remarks");
        String reporter = intent.getStringExtra("reporter");
        rentalAdapter = new RentalAdapter(this, rentalList, userId);

        EditText referenceIdEditText = findViewById(R.id.reference_id);
        referenceIdEditText.setText(Integer.toString(refId));



        EditText userIdEditText = findViewById(R.id.user_id);
        userIdEditText.setText(Integer.toString(userId));

        EditText propertyTypeEditText = findViewById(R.id.property_type);
        propertyTypeEditText.setText(propertyType);

        EditText bedroomEditText = findViewById(R.id.bedroom);
        bedroomEditText.setText(bedroom);

        EditText locationEditText = findViewById(R.id.location);
        locationEditText.setText(location);

        EditText priceEditText = findViewById(R.id.price);
        priceEditText.setText(price);

        EditText furniTypeEditText = findViewById(R.id.furniture_type);
        furniTypeEditText.setText(furniType);

        EditText contactEditText = findViewById(R.id.contact);
        contactEditText.setText(contact);

        EditText remarksEditText = findViewById(R.id.remarks);
        remarksEditText.setText(remarks);

        EditText reporterEditText = findViewById(R.id.reporter);
        reporterEditText.setText(reporter);

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String updatedPropertyType = propertyTypeEditText.getText().toString();
                String updatedBedroom = bedroomEditText.getText().toString();

                LocalDate currentDate = LocalDate.now();
                String updatedDate = currentDate.toString();

                String updatedLocation = locationEditText.getText().toString();
                String updatedPrice = priceEditText.getText().toString();
                String updatedFurniType = furniTypeEditText.getText().toString();
                String updatedContact = contactEditText.getText().toString();
                String updatedRemarks = remarksEditText.getText().toString();
                String updatedReporter = reporterEditText.getText().toString();

                int rowsUpdated = dbHelper.updateRental(refId, userId, updatedPropertyType, updatedBedroom, updatedDate, updatedLocation, updatedPrice, updatedFurniType, updatedContact, updatedRemarks, updatedReporter);
                if (rowsUpdated > 0) {

                    Toast.makeText(update_rental.this, "Rental post updated successfully", Toast.LENGTH_SHORT).show();


                    rentalList = dbHelper.getAllRentalPosts();
                    rentalAdapter.updateData(rentalList);
                    // Use a Handler to delay finishing the activity
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // Finish the activity after a delay
                            Intent resultIntent = new Intent(update_rental.this, viewPosts.class);
                            startActivity(resultIntent);
                            finish();
                        }
                    }, 1000);
                } else {

                    Toast.makeText(update_rental.this, "Failed to update rental post", Toast.LENGTH_SHORT).show();
                }
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int rowsDeleted = dbHelper.deleteRental(refId);
                if(rowsDeleted > 0) {
                    Toast.makeText(update_rental.this, "Rental post deleted successfully", Toast.LENGTH_SHORT).show();

                    rentalList = dbHelper.getAllRentalPosts();
                    rentalAdapter.updateData(rentalList);
                    // Use a Handler to delay finishing the activity
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent resultIntent = new Intent(update_rental.this, viewPosts.class);
                            startActivity(resultIntent);
                            finish();
                        }
                    }, 1000);
                }
            }
        });

    }
}
