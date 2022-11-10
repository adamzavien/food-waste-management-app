package com.example.arcane;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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
    private static final String FK_USER_EMAIL       = "USER_EMAIL";
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

        /*
        String createFoodTable = "CREATE TABLE " + FOOD_TABLE + "(\n" +
                FOOD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, \n" +
                FOOD_CATEGORY + "  TEXT, \n" +
                FOOD_NAME + "  TEXT, \n" +
                FOOD_DESCRIPTION + "  TEXT, \n" +
                USERNAME2 + "  TEXT);";

        db.execSQL(createFoodTable);
        */


        // food table with foreign key

        String createTable3 = "CREATE TABLE " + FOOD_TABLE + "(\n" +
                FOOD_ID + "  INTEGER PRIMARY KEY AUTOINCREMENT, \n" +
                FOOD_CATEGORY + "  TEXT, \n" +
                FOOD_NAME + "  TEXT, \n" +
                FOOD_DESCRIPTION + "  TEXT, \n" +
                FK_USER_EMAIL + "  TEXT, \n" +
                "  FOREIGN KEY(USER_EMAIL) REFERENCES USER(USER_EMAIL)\n" +
                ");";

        db.execSQL(createTable3);
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
    public void addFood(String foodCategory, String foodName, String foodDescription, String userEmail){
        db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();

        cv.put(FOOD_CATEGORY,foodCategory);
        cv.put(FOOD_NAME, foodName);
        cv.put(FOOD_DESCRIPTION, foodDescription);
        cv.put(FK_USER_EMAIL, userEmail);

        db.insert(FOOD_TABLE, null, cv);
    }

    // view food list
    // might put user email as parameter
    public String viewFoodList(){
        db = this.getReadableDatabase();

        String[] columns = new String[]{FOOD_ID, FOOD_CATEGORY, FOOD_NAME, FOOD_DESCRIPTION, USER_EMAIL};

        Cursor cursor = db.query(FOOD_TABLE, columns, null, null, null, null, null);

        int indexfoodID, indexfoodCategory, indexfoodName, indexfoodDescription, indexUserEmail;

        indexfoodID = cursor.getColumnIndex(FOOD_ID);
        indexfoodCategory = cursor.getColumnIndex(FOOD_CATEGORY);
        indexfoodName = cursor.getColumnIndex(FOOD_NAME);
        indexfoodDescription = cursor.getColumnIndex(FOOD_DESCRIPTION);
        indexUserEmail = cursor.getColumnIndex(USER_EMAIL);

        String result = "";

        for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){
            result +=
                    "Food ID\t: " + cursor.getString(indexfoodID) + "\n" +
                            "Food Category\t\t\t: " + cursor.getString(indexfoodCategory) + "\n" +
                            "Food Name\t\t\t: " + cursor.getString(indexfoodName) + "\n" +
                            "Food Description\t\t\t: " + cursor.getString(indexfoodDescription) + "\n" +
                            "User Email\t\t\t: " + cursor.getString(indexUserEmail) + "\n\n";
        }
            db.close();
            return result;
    }

}
