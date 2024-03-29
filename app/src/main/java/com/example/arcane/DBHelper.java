package com.example.arcane;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

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
    private static final String FK_USER_NAME        = "USER_NAME";
    // foreign key : USERNAME ( from user table)

    Context context;

    int placement = 0;

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


        // food table with foreign key
        String createTable3 = "CREATE TABLE " + FOOD_TABLE + "(\n" +
                FOOD_ID + "  INTEGER PRIMARY KEY AUTOINCREMENT, \n" +
                FOOD_CATEGORY + "  TEXT, \n" +
                FOOD_NAME + "  TEXT, \n" +
                FOOD_DESCRIPTION + "  TEXT, \n" +
                FK_USER_NAME + "  TEXT, \n" +
                "  FOREIGN KEY(USER_NAME) REFERENCES USER(USER_NAME)\n" +
                ");";

        db.execSQL(createTable3);

        // Leaderboard Table
        String createLeaderboardTable = "CREATE TABLE " + "LEADERBOARD_TABLE" + "(\n" +
                "LEADERBOARD_ID" + "  INTEGER PRIMARY KEY AUTOINCREMENT, \n" +
                "POINT" + "  NUMBER, \n" +
                FK_USER_NAME + "  TEXT, \n" +
                "  FOREIGN KEY(USER_NAME) REFERENCES USER(USER_NAME)\n" +
                ");";

        db.execSQL(createLeaderboardTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(" drop table if exists " + USER_TABLE);
        db.execSQL(" drop table if exists " + FOOD_TABLE);
        db.execSQL(" drop table if exists " + "LEADERBOARD_TABLE");
    }

    // Insert point into leaderboard table
    public void insertXP(String username, int point){
        db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();

        cv.put("USER_NAME", username);
        cv.put("POINT", point);

        db.insert("LEADERBOARD_TABLE", null, cv);
    }

    // View leaderboard
    public String highestXP(){
        db = this.getReadableDatabase();

        String[] columns = new String[]{"LEADERBOARD_ID", "POINT", FK_USER_NAME};

        Cursor cursor = db.query("LEADERBOARD_TABLE", columns, null, null, null, null, "POINT");

        int id, point, username;

        id = cursor.getColumnIndex("LEADERBOARD_ID");
        point = cursor.getColumnIndex("POINT");
        username = cursor.getColumnIndex(FK_USER_NAME);

        String result = "";

        int maxScore = 0, second = 0;
        String maxPeople = "";

        for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){

            if(maxScore < Integer.parseInt(cursor.getString(point))) {
                maxScore = Integer.parseInt(cursor.getString(point));
                result = "Current Highest Point : " + maxScore + " XP \n";
            }

        }
        db.close();
        return result;
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
    public Boolean checkBoth(String username, String password){
        db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("select * from USER where USER_NAME = ? and USER_PASSWORD = ?", new String[] {username,password});

        if(cursor.getCount() > 0)
            return true;
        else
            return false;
    }

    // add new food information into the food table
    public void addFood(String foodCategory, String foodName, String foodDescription, String userName){
        db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();

        cv.put(FOOD_CATEGORY,foodCategory);
        cv.put(FOOD_NAME, foodName);
        cv.put(FOOD_DESCRIPTION, foodDescription);
        cv.put(FK_USER_NAME, userName);

        db.insert(FOOD_TABLE, null, cv);
    }

    // view food list
    public String viewFoodList(){
        db = this.getReadableDatabase();

        String[] columns = new String[]{FOOD_ID, FOOD_CATEGORY, FOOD_NAME, FOOD_DESCRIPTION, FK_USER_NAME};

        Cursor cursor = db.query(FOOD_TABLE, columns, null, null, null, null, null);

        int indexfoodID, indexfoodCategory, indexfoodName, indexfoodDescription, indexUserName;

        indexfoodID = cursor.getColumnIndex(FOOD_ID);
        indexfoodCategory = cursor.getColumnIndex(FOOD_CATEGORY);
        indexfoodName = cursor.getColumnIndex(FOOD_NAME);
        indexfoodDescription = cursor.getColumnIndex(FOOD_DESCRIPTION);
        indexUserName = cursor.getColumnIndex(FK_USER_NAME);

        String result = "";

        for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){
            result +=
                    "Food ID\t: " + cursor.getString(indexfoodID) + "\n" +
                            "Food Category\t\t\t\t: " + cursor.getString(indexfoodCategory) + "\n" +
                            "Food Name\t\t\t\t\t\t\t: " + cursor.getString(indexfoodName) + "\n" +
                            "Food Description\t: " + cursor.getString(indexfoodDescription) + "\n\n";
                            //"User Name \t: " + cursor.getString(indexUserName) + "\n\n";
        }
            db.close();
            return result;
    }

    // delete food information based on food id
    public void deleteFood(long l){

        db = this.getWritableDatabase();

        db.delete(FOOD_TABLE, FOOD_ID + " = " + l, null);
    }

    // update food information
    // can't edit username via this function
    public void updateFood(long l, String foodCategory, String foodName, String foodDescription){
        db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(FOOD_CATEGORY,foodCategory);
        cv.put(FOOD_NAME, foodName);
        cv.put(FOOD_DESCRIPTION, foodDescription);

        db.update(FOOD_TABLE, cv, FOOD_ID + " = " + l, null);
        db.close();
    }

    // search for specific food based on three get functions
    public String getFoodCategory(long l1){
        db = this.getReadableDatabase();

        String[] columns = new String[] {FOOD_ID, FOOD_CATEGORY, FOOD_NAME, FOOD_DESCRIPTION};

        Cursor cursor = db.query(FOOD_TABLE, columns, FOOD_ID + " = " + l1, null, null, null, null);

        if(cursor != null){
            cursor.moveToFirst();
            String foodCategory = cursor.getString(1);
            return foodCategory;
        }
        Toast.makeText(context, "food category not found", Toast.LENGTH_SHORT).show();
        return null;
    }

    public String getFoodName(long l1){
        db = this.getReadableDatabase();

        String[] columns = new String[] {FOOD_ID, FOOD_CATEGORY, FOOD_NAME, FOOD_DESCRIPTION};

        Cursor cursor = db.query(FOOD_TABLE, columns, FOOD_ID + " = " + l1, null, null, null, null);

        if(cursor != null){
            cursor.moveToFirst();
            String foodName = cursor.getString(2);
            return foodName;
        }
        Toast.makeText(context, "food name not found", Toast.LENGTH_SHORT).show();
        return null;
    }

    public String getFoodDescription(long l1){
        db = this.getReadableDatabase();

        String[] columns = new String[] {FOOD_ID, FOOD_CATEGORY, FOOD_NAME, FOOD_DESCRIPTION};

        Cursor cursor = db.query(FOOD_TABLE, columns, FOOD_ID + " = " + l1, null, null, null, null);

        if(cursor != null){
            cursor.moveToFirst();
            String foodDescription = cursor.getString(3);
            return foodDescription;
        }
        Toast.makeText(context, "food description not found", Toast.LENGTH_SHORT).show();
        return null;
    }

    // Determine food category for image classification
    public String classifyFoodCategory(String foodName){
        String category = null, a;

        a = foodName.toLowerCase();

        if(a.equals("apple") || a.equals("banana") || a.equals("cabbage"))
            category = "fruits";
        else if(a.equals("cabbage"))
            category = "vegetables";
        else if(a.equals("milk"))
            category = "dairy";
        else if(a.equals("bread") || a.equals("rice"))
            category = "grains";
        else
            category = "food category unknown :(";

        return category;
    }
}
