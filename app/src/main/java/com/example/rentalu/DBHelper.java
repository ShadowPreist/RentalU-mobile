package com.example.rentalu;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {


    private RentalAdapter adapter;

    public void setAdapter(RentalAdapter adapter) {
        this.adapter = adapter;
    }
    private static String DBName = "RentalDB";
    //user information
    private static String USER_TABLE = "user_table";
    private static String USER_ID = "user_id";
    private static String USER_NAME = "username";
    private static String USER_EMAIL = "email";
    private static String PASSWORD = "password";
    private static String PH_NO = "phone_no";

    //rental information
    private static String PROPERTY_TABLE = "property_table";
    private static String REF_ID = "ref_id";
    private static String PROPERTY_TYPE = "property_type";
    private static String BEDROOM = "bedroom";
    private static String DATE_ADDED = "date_added";
    private static String LOCATION = "location";
    private static String PRICE = "price";
    private static String PHONE_NO = "phoneNo";
    private static String FURNI_TYPES = "furniTypes";
    private static String REMARKS = "remarks";
    private static String REPORTER = "reporter";

    public DBHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DBName, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String user_table_create = "CREATE TABLE " + USER_TABLE + "(" +
                USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                USER_NAME + " TEXT," +
                USER_EMAIL + " TEXT," +
                PASSWORD + " TEXT," +
                PH_NO + " TEXT)";


        String property_table_create = "CREATE TABLE " + PROPERTY_TABLE + "(" +
                REF_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                USER_ID + " INTEGER," +
                PROPERTY_TYPE + " TEXT," +
                BEDROOM + " TEXT," +
                DATE_ADDED + " TEXT," +
                LOCATION + " TEXT," +
                PRICE + " TEXT," +
                FURNI_TYPES + " TEXT," +
                PHONE_NO + " TEXT," +
                REMARKS + " TEXT," +
                REPORTER + " TEXT," +
                "FOREIGN KEY (" + USER_ID + ") REFERENCES " + USER_TABLE + "(" + USER_ID + "))";


        db.execSQL(user_table_create);
        db.execSQL(property_table_create);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + PROPERTY_TABLE);

    }

    public void addUser(String username, String password, String ph_no, String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(USER_NAME, username);
        cv.put(USER_EMAIL,email);
        cv.put(PASSWORD, password);
        cv.put(PH_NO, ph_no);

        db.insert(USER_TABLE,null,cv);
        db.close();
    }

    public UserModel getUser(String email, String password) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + USER_ID + ", " + USER_NAME + " FROM " + USER_TABLE + " WHERE " + USER_EMAIL + " = ? AND " + PASSWORD + " = ?", new String[]{email, password});

        UserModel user = null;

        if (cursor != null && cursor.moveToFirst()) {
            int userId = cursor.getInt(0);
            String userName = cursor.getString(1);

            user = new UserModel(userId, userName, email, password);

            cursor.close();
        }

        db.close();

        return user;
    }

    public boolean validateUser(String email, String password) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + USER_TABLE + " WHERE " + USER_EMAIL + " = ? AND " + PASSWORD + " = ?", new String[]{email, password});

        boolean isValid = false;

        if (cursor != null) {
            isValid = cursor.getCount() > 0;
            cursor.close();
        }

        db.close();

        return isValid;
    }

    public void addRental(int user_id, String property_type, String bedroom, String location, String price, String furniTypes, String phoneNo, String remarks, String reporter) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        LocalDate currentDate = LocalDate.now();
        cv.put(USER_ID, user_id); // Set the user_id in the ContentValues
        cv.put(PROPERTY_TYPE, property_type);
        cv.put(BEDROOM, bedroom);
        cv.put(DATE_ADDED, currentDate.toString());
        cv.put(LOCATION, location);
        cv.put(PRICE, price);
        cv.put(FURNI_TYPES, furniTypes);
        cv.put(PHONE_NO, phoneNo);
        cv.put(REMARKS, remarks);
        cv.put(REPORTER, reporter);

        db.insert(PROPERTY_TABLE, null, cv);
        db.close();
    }

    public int updateRental(int ref_id, int user_id, String property_type, String bedroom, String date_added,String location, String price, String furniTypes, String phoneNo, String remarks, String reporter) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(REF_ID,ref_id);
        cv.put(USER_ID, user_id);
        cv.put(PROPERTY_TYPE,property_type);
        cv.put(BEDROOM,bedroom);
        cv.put(DATE_ADDED,date_added);
        cv.put(LOCATION,location);
        cv.put(PRICE,price);
        cv.put(FURNI_TYPES,furniTypes);
        cv.put(PHONE_NO,phoneNo);
        cv.put(REMARKS,remarks);
        cv.put(REPORTER,reporter);

        int row_updated = db.update(PROPERTY_TABLE, cv, REF_ID + "=" + ref_id, null);
        db.close();


        return row_updated;

    }

    public int deleteRental(int ref_id) {
        SQLiteDatabase db = this.getWritableDatabase();

        String whereClause = REF_ID + "=" + ref_id;

        int rowsDeleted = db.delete(PROPERTY_TABLE, whereClause, null);
        db.close();
        return rowsDeleted;
    }

    public ArrayList<RentalPost> getAllRentalPosts() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + PROPERTY_TABLE, null);
        ArrayList<RentalPost> rentalPosts = new ArrayList<>();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                RentalPost rentalPost = new RentalPost();
                rentalPost.setRefId(cursor.getInt(0));
                rentalPost.setUserId(cursor.getInt(1));
                rentalPost.setPropertyType(cursor.getString(2));
                rentalPost.setBedroom(cursor.getString(3));
                rentalPost.setDateAdded(cursor.getString(4));
                rentalPost.setLocation(cursor.getString(5));
                rentalPost.setPrice(cursor.getString(6));
                rentalPost.setFurniTypes(cursor.getString(7));
                rentalPost.setPhoneNo(cursor.getString(8));
                rentalPost.setRemarks(cursor.getString(9));
                rentalPost.setReporter(cursor.getString(10));

                rentalPosts.add(rentalPost);
            } while (cursor.moveToNext());

            cursor.close();
        }

        db.close();
        return rentalPosts;
    }

    public int getUserId(String email, String password) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + USER_ID + " FROM " + USER_TABLE + " WHERE " + USER_EMAIL + " = ? AND " + PASSWORD + " = ?", new String[]{email, password});

        int userId = -1;

        if (cursor != null && cursor.moveToFirst()) {
            userId = cursor.getInt(0);
            cursor.close();
        }

        db.close();

        return userId;
    }

    public UserModel getUserById(int userId) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + USER_NAME + ", " + USER_EMAIL + " FROM " + USER_TABLE + " WHERE " + USER_ID + " = ?", new String[]{String.valueOf(userId)});

        UserModel user = null;

        if (cursor != null && cursor.moveToFirst()) {
            String userName = cursor.getString(0);
            String userEmail = cursor.getString(1);

            user = new UserModel(userId, userName, userEmail, ""); // Pass an empty string for password

            cursor.close();
        }

        db.close();

        return user;
    }

    public RentalPost getSpecificRentalPost(int refId) {
        SQLiteDatabase db = getReadableDatabase();
        String[] projection = {
                REF_ID,
                USER_ID,
                PROPERTY_TYPE,
                BEDROOM,
                DATE_ADDED,
                LOCATION,
                PRICE,
                FURNI_TYPES,
                PHONE_NO,
                REMARKS,
                REPORTER
        };

        String selection = REF_ID + " = ?";
        String[] selectionArgs = {String.valueOf(refId)};

        Cursor cursor = db.query(
                PROPERTY_TABLE,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        RentalPost rentalPost = null;
        if (cursor != null && cursor.moveToFirst()) {
            rentalPost = new RentalPost();
            rentalPost.setRefId(cursor.getInt(0));
            rentalPost.setUserId(cursor.getInt(1));
            rentalPost.setPropertyType(cursor.getString(2));
            rentalPost.setBedroom(cursor.getString(3));
            rentalPost.setDateAdded(cursor.getString(4));
            rentalPost.setLocation(cursor.getString(5));
            rentalPost.setPrice(cursor.getString(6));
            rentalPost.setFurniTypes(cursor.getString(7));
            rentalPost.setPhoneNo(cursor.getString(8));
            rentalPost.setRemarks(cursor.getString(9));
            rentalPost.setReporter(cursor.getString(10));

            cursor.close();
        }

        return rentalPost;
    }

    private void notifyDataChanged() {
        if (adapter != null) {
            adapter.updateData(getAllRentalPosts());
        }
    }



}
