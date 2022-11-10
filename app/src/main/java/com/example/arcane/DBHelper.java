package com.example.arcane;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {

    SQLiteDatabase db;

    private static final String DATABASE_NAME = "arcane.db";

    // user table
    private static final String USER_TABLE       = "USER";
    private static final String USER_NAME           = "USER_NAME"; // primary key
    private static final String USER_EMAIL          = "USER_EMAIL";
    private static final String USER_PASSWORD       = "USER_PASSWORD";

    // food table
    private static final String FOOD_TABLE          = "FOOD";
    private static final String FOOD_ID             = "FOOD_ID"; // primary key & auto increment
    private static final String FOOD_CATEGORY       = "FOOD_CATEGORY";
    private static final String FOOD_NAME           = "FOOD_NAME";
    private static final String FOOD_DESCRIPTION    = "FOOD_DESCRIPTION";
    private static final String USERNAME2           = "USER_NAME2";
    //private static final String FK_USERNAME         = "USER_NAME";
    // foreign key : USER_EMAIL ( from user table)

    Context context;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createUserTable = " CREATE TABLE " + USER_TABLE + " ( "
                + USER_NAME + " TEXT PRIMARY KEY, "
                + USER_EMAIL + " TEXT, "
                + USER_PASSWORD + " TEXT); ";

        db.execSQL(createUserTable);

        String createFoodTable = "CREATE TABLE " + FOOD_TABLE + "(\n" +
                FOOD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, \n" +
                FOOD_CATEGORY + "  TEXT, \n" +
                FOOD_NAME + "  TEXT, \n" +
                FOOD_DESCRIPTION + "  TEXT);";

        db.execSQL(createFoodTable);

        // food table with foreign key
        {/*
        String createTable3 = "CREATE TABLE " + FOOD_TABLE + "(\n" +
                FOOD_ID + "  INTEGER PRIMARY KEY AUTOINCREMENT, \n" +
                FOOD_CATEGORY + "  TEXT, \n" +
                FOOD_NAME + "  TEXT, \n" +
                FOOD_DESCRIPTION + "  TEXT, \n" +
                FK_USERNAME + "  TEXT, \n" +
                "  FOREIGN KEY(USER_NAME) REFERENCES USER(USER_NAME)\n" +
                ");";

        db.execSQL(createTable3);
        */}
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(" drop table if exists " + USER_TABLE);
        db.execSQL(" drop table if exists " + FOOD_TABLE);
    }

    // add new user into the user table
    public void addUser(String username, String email, String password){
        db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();

        cv.put(USER_NAME, username);
        cv.put(USER_EMAIL, email);
        cv.put(USER_PASSWORD, password);

        db.insert(USER_TABLE, null, cv);
    }

    // check user credential such as user email & password
    public Boolean checkBoth(String email, String password){
        db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("select * from USER where USER_EMAIL = ? and USER_PASSWORD = ?", new String[] {email,password});

        if(cursor.getCount() > 0)
            return true;
        else
            return false;
    }

    // add new food information into the food table
    public void addFood(String foodCategory, String foodName, String foodDescription){
        db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();

        cv.put(FOOD_CATEGORY,foodCategory);
        cv.put(FOOD_NAME, foodName);
        cv.put(FOOD_DESCRIPTION, foodDescription);

        db.insert(FOOD_TABLE, null, cv);
    }
}
