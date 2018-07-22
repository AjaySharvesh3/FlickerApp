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

    private static final String DATABASE_NAME = "favorite.db";

    private static final int DATABASE_VERSION = 1;

    public static final String LOGTAG = "FAVORITE";

    SQLiteOpenHelper dbhandler;
    SQLiteDatabase db;

    public FavoriteDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void open() {
        Log.i(LOGTAG, "Database Opened");
        db = dbhandler.getWritableDatabase();
    }

    public void close() {
        Log.i(LOGTAG, "Database Closed");
        dbhandler.close();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_FAVORITE_TABLE = "CREATE TABLE " + FavoriteContract.FavoriteEntryData.TABLE_NAME + " (" +
                FavoriteContract.FavoriteEntryData._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                FavoriteContract.FavoriteEntryData.COLUMN_MOVIE_ID + " INTEGER, " +
                FavoriteContract.FavoriteEntryData.COLUMN_TITLE + " TEXT NOT NULL, " +
                FavoriteContract.FavoriteEntryData.COLUMN_USER_RATING + " REAL NOT NULL, " +
                FavoriteContract.FavoriteEntryData.COLUMN_POSTER_PATH + " TEXT NOT NULL, " +
                FavoriteContract.FavoriteEntryData.COLUMN_PLOT_SYNOPSIS + " TEXT NOT NULL" +
                "); ";

        sqLiteDatabase.execSQL(SQL_CREATE_FAVORITE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FavoriteContract.FavoriteEntryData.TABLE_NAME);
        onCreate(sqLiteDatabase);

    }

    public void addFavorite(Movies movie) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(FavoriteContract.FavoriteEntryData.COLUMN_MOVIE_ID, movie.getId());
        values.put(FavoriteContract.FavoriteEntryData.COLUMN_TITLE, movie.getOriginalTitle());
        values.put(FavoriteContract.FavoriteEntryData.COLUMN_USER_RATING, movie.getVoteAverage());
        values.put(FavoriteContract.FavoriteEntryData.COLUMN_POSTER_PATH, movie.getPosterPath());
        values.put(FavoriteContract.FavoriteEntryData.COLUMN_PLOT_SYNOPSIS, movie.getOverview());

        db.insert(FavoriteContract.FavoriteEntryData.TABLE_NAME, null, values);
        db.close();
    }

    public void deleteFavorite(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(FavoriteContract.FavoriteEntryData.TABLE_NAME, FavoriteContract.FavoriteEntryData.COLUMN_MOVIE_ID + "=" + id, null);
    }

    public List<Movies> getAllFavorite() {
        String[] columns = {
                FavoriteContract.FavoriteEntryData._ID,
                FavoriteContract.FavoriteEntryData.COLUMN_MOVIE_ID,
                FavoriteContract.FavoriteEntryData.COLUMN_TITLE,
                FavoriteContract.FavoriteEntryData.COLUMN_USER_RATING,
                FavoriteContract.FavoriteEntryData.COLUMN_POSTER_PATH,
                FavoriteContract.FavoriteEntryData.COLUMN_PLOT_SYNOPSIS

        };
        String sortOrder =
                FavoriteContract.FavoriteEntryData._ID + " ASC";
        List<Movies> favoriteList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(FavoriteContract.FavoriteEntryData.TABLE_NAME,
                columns,
                null,
                null,
                null,
                null,
                sortOrder);

        if (cursor.moveToFirst()) {
            do {
                Movies movie = new Movies();
                movie.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(FavoriteContract.FavoriteEntryData.COLUMN_MOVIE_ID))));
                movie.setOriginalTitle(cursor.getString(cursor.getColumnIndex(FavoriteContract.FavoriteEntryData.COLUMN_TITLE)));
                movie.setVoteAverage(Double.parseDouble(cursor.getString(cursor.getColumnIndex(FavoriteContract.FavoriteEntryData.COLUMN_USER_RATING))));
                movie.setPosterPath(cursor.getString(cursor.getColumnIndex(FavoriteContract.FavoriteEntryData.COLUMN_POSTER_PATH)));
                movie.setOverview(cursor.getString(cursor.getColumnIndex(FavoriteContract.FavoriteEntryData.COLUMN_PLOT_SYNOPSIS)));

                favoriteList.add(movie);

            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return favoriteList;
    }
}
