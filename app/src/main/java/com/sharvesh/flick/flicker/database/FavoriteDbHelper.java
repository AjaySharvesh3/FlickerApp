package com.sharvesh.flick.flicker.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.sharvesh.flick.flicker.model.Movies;

import java.util.ArrayList;
import java.util.List;

public class FavoriteDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "favorite.db";
    public static final int DATABASE_VERSION = 1;
    public static final String LOG_TAG = "FAVORITES";

    SQLiteOpenHelper sqLiteOpenHelper;
    SQLiteDatabase sqLiteDatabase;

    public FavoriteDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void openDataBase() {
        Log.i(LOG_TAG, "Database Opened");
        sqLiteDatabase = sqLiteOpenHelper.getWritableDatabase();
    }

    public void closeDataBase() {
        Log.i(LOG_TAG, "Database Closed");
        sqLiteOpenHelper.close();
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_FAVORITE_TABLE = "CREATE TABLE " + FavoriteContract.FavoriteEntryData.TABLE_NAME + " (" +
                FavoriteContract.FavoriteEntryData._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                FavoriteContract.FavoriteEntryData.COLUMN_MOVIE_ID + " INTEGER, " +
                FavoriteContract.FavoriteEntryData.COLUMN_TITLE + " TEXT NOT NULL, " +
                FavoriteContract.FavoriteEntryData.COLUMN_USER_RATING + " REAL NOT NULL, " +
                FavoriteContract.FavoriteEntryData.COLUMN_POSTER_PATH + " TEXT NOT NULL, " +
                FavoriteContract.FavoriteEntryData.COLUMN_PLOT_SYNOPSIS + " TEXT NOT NULL" +
                "); ";
        db.execSQL(SQL_CREATE_FAVORITE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FavoriteContract.FavoriteEntryData.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public void addToFavorite(Movies movies) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FavoriteContract.FavoriteEntryData.COLUMN_MOVIE_ID, movies.getId());
        values.put(FavoriteContract.FavoriteEntryData.COLUMN_TITLE, movies.getOriginalTitle());
        values.put(FavoriteContract.FavoriteEntryData.COLUMN_PLOT_SYNOPSIS, movies.getOverview());
        values.put(FavoriteContract.FavoriteEntryData.COLUMN_POSTER_PATH, movies.getPosterPath());
        values.put(FavoriteContract.FavoriteEntryData.COLUMN_USER_RATING, movies.getVoteAverage());

        database.insert(FavoriteContract.FavoriteEntryData.TABLE_NAME, null, values);
        database.close();
    }

    public void deleteFromFavorite(int id) {
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete(FavoriteContract.FavoriteEntryData.TABLE_NAME,
                FavoriteContract.FavoriteEntryData.COLUMN_MOVIE_ID + "=" + id, null);
    }

    public List<Movies> getAllFavoriteMovieList() {
        String [] listOfColumns = {
                FavoriteContract.FavoriteEntryData._ID,
                FavoriteContract.FavoriteEntryData.COLUMN_MOVIE_ID,
                FavoriteContract.FavoriteEntryData.COLUMN_TITLE,
                FavoriteContract.FavoriteEntryData.COLUMN_USER_RATING,
                FavoriteContract.FavoriteEntryData.COLUMN_POSTER_PATH,
                FavoriteContract.FavoriteEntryData.COLUMN_PLOT_SYNOPSIS
        };
        String sortOrder = FavoriteContract.FavoriteEntryData._ID + " ASC";
        List<Movies> moviesList = new ArrayList<>();
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.query(FavoriteContract.FavoriteEntryData.TABLE_NAME,
                listOfColumns,
                null,
                null,
                null,
                null,
                sortOrder);
        if (cursor.moveToFirst()) {
            do {
                Movies movies = new Movies();
                movies.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(FavoriteContract.FavoriteEntryData.COLUMN_MOVIE_ID))));
                movies.setOriginalTitle(cursor.getString(cursor.getColumnIndex(FavoriteContract.FavoriteEntryData.COLUMN_TITLE)));
                movies.setVoteAverage(Double.parseDouble(cursor.getString(cursor.getColumnIndex(FavoriteContract.FavoriteEntryData.COLUMN_USER_RATING))));
                movies.setPosterPath(cursor.getString(cursor.getColumnIndex(FavoriteContract.FavoriteEntryData.COLUMN_POSTER_PATH)));
                movies.setOverview(cursor.getString(cursor.getColumnIndex(FavoriteContract.FavoriteEntryData.COLUMN_PLOT_SYNOPSIS)));

                moviesList.add(movies);
            }   while (cursor.moveToNext());
        }
        cursor.close();
        database.close();

        return moviesList;
    }
}
