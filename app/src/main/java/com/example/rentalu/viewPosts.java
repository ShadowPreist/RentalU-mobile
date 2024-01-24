package com.example.rentalu;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.material.navigation.NavigationView;
import java.util.ArrayList;
import java.util.List;

public class viewPosts extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private NavigationView navigationView;
    private ArrayList<RentalPost> rentalList;
    private int loggedInUserId;
    private RentalAdapter rentalAdapter;
    private DBHelper dbHelper;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        dbHelper = new DBHelper(this, "RentalDB", null, 1);


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_posts);

        SharedPreferences preferences = getSharedPreferences("user_data", MODE_PRIVATE);


        Intent intent = getIntent();
        loggedInUserId = preferences.getInt("user_id", -1);


        rentalList = getAllRentalPosts();
        ToggleButton toggleButton = findViewById(R.id.toggleButton);
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // ToggleButton is checked (My Posts)
                    // Update the RecyclerView to show only posts created by the current user
                    ArrayList<RentalPost> myPosts = dbHelper.getPostsByUserId(loggedInUserId);
                    rentalAdapter.updateData(myPosts);
                } else {
                    // ToggleButton is unchecked (All Posts)
                    // Update the RecyclerView to show all posts
                    ArrayList<RentalPost> allPosts = dbHelper.getAllRentalPosts();
                    rentalAdapter.updateData(allPosts);
                }
            }
        });

        drawerLayout = findViewById(R.id.drawer_layout);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        navigationView = findViewById(R.id.navigation_view);

        setNavigationHeader();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                // Handle navigation item clicks here
                int itemId = item.getItemId();

                if (itemId == R.id.menu_add_post) {
                    // Handle add post click
                    Intent intent = new Intent(viewPosts.this, addRental.class);
                    intent.putExtra("user_id", loggedInUserId);
                    startActivity(intent);
                } else if (itemId == R.id.menu_logout) {
                    // Handle log out click
                    SharedPreferences preferences = getSharedPreferences("user_data", login.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.clear();
                    editor.apply();
                    startActivity(new Intent(viewPosts.this, login.class));
                }

                // Close the drawer when an item is selected
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }

        });


        // Initialize and set up the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));



        // Assuming getAllRentalPosts() returns a list of RentalPost objects
        ArrayList<RentalPost> rentalPosts = getAllRentalPosts();

        // Initialize and set the adapter with the rental data
         rentalAdapter = new RentalAdapter(this, rentalPosts, loggedInUserId);
         dbHelper.setAdapter(rentalAdapter);
         recyclerView.setAdapter(rentalAdapter);



    }

    // This method simulates retrieving rental data from your DBHelper
    private ArrayList<RentalPost> getAllRentalPosts() {
        DBHelper dbHelper = new DBHelper(this, "RentalDB", null, 1);

        ArrayList<RentalPost> rentalPosts = dbHelper.getAllRentalPosts();

        dbHelper.close();

        return rentalPosts;
    }

    private void setNavigationHeader() {
        // Set up the navigation header
        View headerView = navigationView.getHeaderView(0);
        SharedPreferences preferences = getSharedPreferences("user_data", login.MODE_PRIVATE);

        TextView userNameTextView = headerView.findViewById(R.id.user_name);
        TextView userEmailTextView = headerView.findViewById(R.id.user_email);

        // Retrieve user data using DBHelper
        DBHelper dbHelper = new DBHelper(this, "RentalDB", null, 1);
        UserModel loggedInUser = dbHelper.getUserById(loggedInUserId);

        // Display the user's name and email
        if (loggedInUser != null) {
            userNameTextView.setText(loggedInUser.getUser_name());
            userEmailTextView.setText(loggedInUser.getEmail());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle hamburger icon click to open/close the drawer
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
