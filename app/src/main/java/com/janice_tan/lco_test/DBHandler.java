package com.janice_tan.lco_test;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

/**
 * Created by janice on 15/10/16.
 */

public class DBHandler extends SQLiteOpenHelper {

    private static final String TAG = DBHandler.class.getSimpleName();

    private static final String DATABASE_NAME = "lco";

    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_PLACES = "places";
    private static final String TABLE_CATEGORIES = "categories";

    private static final String KEY_PLACE_ID = "id";
    public static final String KEY_PLACE_NAME = "name";
    public static final String KEY_PLACE_RATING = "rating";
    private static final String KEY_PLACE_IMAGE_PATH = "image_path";
    public static final String KEY_PLACE_UPDATED = "updated_at";
    private static final String KEY_PLACE_CATEGORY_ID = "category_id";

    private static final String KEY_CATEGORY_ID = "id";
    private static final String KEY_CATEGORY_NAME = "name";

    public static final String ORDER_BY_ASC = "ASC";
    public static final String ORDER_BY_DESC = "DESC";

    public static final int CATEGORY_ALL = -1;

    public DBHandler(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String CREATE_CATEGORIES_TABLE = "CREATE TABLE " + TABLE_CATEGORIES + " (" +
                KEY_CATEGORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                KEY_CATEGORY_NAME + " TEXT NOT NULL)";
        sqLiteDatabase.execSQL(CREATE_CATEGORIES_TABLE);

        String CREATE_PLACES_TABLE = "CREATE TABLE " + TABLE_PLACES + " (" +
                KEY_PLACE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                KEY_PLACE_NAME + " TEXT NOT NULL, " +
                KEY_PLACE_RATING + " INTEGER, " +
                KEY_PLACE_IMAGE_PATH + " TEXT, " +
                KEY_PLACE_UPDATED + " LONG, " +
                KEY_PLACE_CATEGORY_ID + " INTEGER NOT NULL, " +
                "FOREIGN KEY(" + KEY_PLACE_CATEGORY_ID + ") REFERENCES " + TABLE_CATEGORIES + "(" + KEY_CATEGORY_ID + "))";
        sqLiteDatabase.execSQL(CREATE_PLACES_TABLE);

        // insert the test data for Categories:
        ArrayList categoryList = new ArrayList();

        Category category_travel = new Category("Travel");
        categoryList.add(category_travel);
        long travelCatId = insertTestDataCategories(sqLiteDatabase, category_travel);

        Category category_food = new Category("Food");
        categoryList.add(category_food);
        long foodCatId = insertTestDataCategories(sqLiteDatabase, category_food);

        Random rand;
        int MAX_RATING = 6;

        // insert the test data for Places:

        //Place(String name, int rating, String imagePath, int category_id)
        Place place;
        String[] travelPlaces = {"Sunset on Virgin Gorda", "Perms, Ollantaytambo", "Rapids in Yoho", "Sunflowers", "Belfry of Bruges",
                "Pitching Tents", "Peruvian Dance", "Aveiro", "Island of Saona", "Valletta"};
        int index = 1;
        for (int i = 0; i < travelPlaces.length; i ++) {
            rand = new Random();
            place = new Place(travelPlaces[i], rand.nextInt(MAX_RATING), "travel_" + index++ + ".jpeg", (int) travelCatId);
            insertTestDataPlaces(sqLiteDatabase, place);
        }

        String[] foodPlaces = {"Salad", "Spaghetti Bolognese", "Ice Berry Drink", "Berry Dessert", "Fried Chicken & Tots",
                "Mexican Food", "Mac & Cheese", "Pancakes", "Eggs", "Charcoal Burger"};
        index = 1;
        for (int i = 0; i < foodPlaces.length; i ++) {
            rand = new Random();
            place = new Place(foodPlaces[i], rand.nextInt(MAX_RATING), "food_" + index++ + ".jpeg", (int) foodCatId);
            insertTestDataPlaces(sqLiteDatabase, place);
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_PLACES);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIES);

        onCreate(sqLiteDatabase);
    }

    private long insertTestDataCategories(SQLiteDatabase sqLiteDatabase, Category category) {

        ContentValues values = new ContentValues();
        values.put(KEY_CATEGORY_NAME, category.getName());

        return sqLiteDatabase.insert(TABLE_CATEGORIES, null, values);
    }

    private long insertTestDataPlaces(SQLiteDatabase sqLiteDatabase, Place place) {

        Random rnd = new Random();
        long offset = Timestamp.valueOf("2015-01-01 00:00:00").getTime();
        long end = Timestamp.valueOf("2016-09-30 00:00:00").getTime();
        long diff = end - offset + 1;
        long date = offset + (long)(Math.random() * diff);

        ContentValues values = new ContentValues();
        values.put(KEY_PLACE_NAME, place.getName());
        values.put(KEY_PLACE_RATING, place.getRating());
        values.put(KEY_PLACE_IMAGE_PATH, place.getImagePath());
        values.put(KEY_PLACE_UPDATED, date);
        values.put(KEY_PLACE_CATEGORY_ID, place.getCategory_id());

        //Log.d(TAG, place.getName() + ". " + "(" + place.getRating() + ") " + place.getImagePath() + " (" + new Date(date) + ")");

        return sqLiteDatabase.insert(TABLE_PLACES, null, values);
    }

    private HashMap getCategories() {
        String selectQuery = "SELECT * FROM " + TABLE_CATEGORIES;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        HashMap categories = new HashMap();
        Category category;
        if (cursor != null) {
            while (cursor.moveToNext()) {
                category = new Category(cursor.getInt(cursor.getColumnIndex(KEY_CATEGORY_ID)), cursor.getString(cursor.getColumnIndex(KEY_CATEGORY_NAME)));
                categories.put(category.getId(), category);
            }
        }

        if (cursor != null)
            cursor.close();
        if (db != null && db.isOpen())
            db.close();

        return categories;
    }


    public ArrayList<Place> getAllPlaces(String orderBy, String direction, int categoryId) {

        HashMap categories = getCategories();

        //getAllPlaces(KEY_PLACE_NAME, ORDER_BY_ASC);
        String selectQuery = "SELECT * FROM " + TABLE_PLACES;
        if (categoryId > 0)
            selectQuery += " WHERE " + KEY_PLACE_CATEGORY_ID + " = " + categoryId;

        selectQuery += " ORDER BY " + orderBy + " " + direction;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        //Log.d(TAG, selectQuery);

        ArrayList<Place> placesList = getPlaces(cursor, categories);

        if (cursor != null)
            cursor.close();
        if (db != null && db.isOpen())
            db.close();

        return placesList;
    }

    private ArrayList<Place> getPlaces(Cursor cursor, HashMap categories) {

        ArrayList<Place> placesList = new ArrayList<Place>();

        String name, imagePath;
        int id, rating, catId;
        Place place;
        long longDate;

        if (cursor != null) {
            while (cursor.moveToNext()) {
                id = cursor.getInt(cursor.getColumnIndex(KEY_PLACE_ID));
                name = cursor.getString(cursor.getColumnIndex(KEY_PLACE_NAME));
                imagePath = cursor.getString(cursor.getColumnIndex(KEY_PLACE_IMAGE_PATH));
                rating = cursor.getInt(cursor.getColumnIndex(KEY_PLACE_RATING));
                longDate = cursor.getLong(cursor.getColumnIndex(KEY_PLACE_UPDATED));
                catId = cursor.getInt(cursor.getColumnIndex(KEY_PLACE_CATEGORY_ID));

                //Place(int id, String name, int rating, String imagePath, Date updatedAt, int category_id)
                place = new Place(id, name, rating, imagePath, new Date(longDate), catId);
                place.setCategory((Category) categories.get(catId));
                placesList.add(place);

                //Log.d(TAG, id + ": " + name + ". " + "(" + rating + ") " + imagePath + " (" + new Date(longDate) + ")");
            }
        }

        return placesList;
    }


    public ArrayList<Place> findPlaces(String keyword) {

        HashMap categories = getCategories();

        //String[] keywords = keyword.split(" ");

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_PLACES, null, KEY_PLACE_NAME + " LIKE '%" + keyword + "%'", null, null, null, null);

        ArrayList<Place> placesList = getPlaces(cursor, categories);

        if (cursor != null)
            cursor.close();
        if (db != null && db.isOpen())
            db.close();

        return placesList;

    }


    public ArrayList<Category> getAllCategories() {

        ArrayList<Category> categoryList = new ArrayList<Category>();

        String selectQuery = "SELECT * FROM " + TABLE_CATEGORIES + " ORDER BY " + KEY_CATEGORY_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        Category category;
        if (cursor != null) {
            while (cursor.moveToNext()) {
                category = new Category(cursor.getInt(cursor.getColumnIndex(KEY_CATEGORY_ID)), cursor.getString(cursor.getColumnIndex(KEY_CATEGORY_NAME)));
                categoryList.add(category);
            }
        }

        if (cursor != null)
            cursor.close();
        if (db != null && db.isOpen())
            db.close();

        return categoryList;
    }
}
