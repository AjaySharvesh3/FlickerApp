package com.sharvesh.flick.flicker.database;

import android.provider.BaseColumns;

public class FavoriteContract {

    public static final class FavoriteEntryData implements BaseColumns {

        public static final String TABLE_NAME = "favorite";
        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_USER_RATING = "user_rating";
        public static final String COLUMN_POSTER_PATH = "poster_path";
        public static final String COLUMN_PLOT_SYNOPSIS = "overview";

    }

}
