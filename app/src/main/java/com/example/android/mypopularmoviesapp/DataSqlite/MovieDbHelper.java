package com.example.android.mypopularmoviesapp.DataSqlite;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Tonyukuk on 14.03.2018.
 */

public class MovieDbHelper extends SQLiteOpenHelper {

    public static final String SQL_CREATE_MOVIE_FAVOURITE_TABLE = " CREATE TABLE IF NOT EXISTS " +
            MovieDbContract.TABLE_NAME + " (" +
            MovieDbContract._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            MovieDbContract.COLUMB_MOVIE_ID + " INTEGER UNIQUE, " +
            MovieDbContract.COLUMB_TITLE + " TEXT NOT NULL, " +
            MovieDbContract.COLUMB_POSTER_IMAGE + " TEXT NOT NULL, " +
            MovieDbContract.COLUMB_BACKDROP_IMAGE + " TEXT NOT NULL, " +
            MovieDbContract.COLUMB_AVERAGE_RATING + " TEXT NOT NULL, " +
            MovieDbContract.COLUMB_OVERVIEW + " TEXT NOT NULL, " +
            MovieDbContract.COLUMB_RELEASE_DATE + " TEXT NOT NULL" +
            " );";
    static final String DATABASE_NAME = "favourite";
    private static final int DATABASE_VERSION = 1;


    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        try {
            sqLiteDatabase.execSQL(SQL_CREATE_MOVIE_FAVOURITE_TABLE);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieDbContract.TABLE_NAME);
        onCreate(sqLiteDatabase);

    }
}
