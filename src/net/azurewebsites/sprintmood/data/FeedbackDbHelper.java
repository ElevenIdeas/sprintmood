/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.azurewebsites.sprintmood.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import net.azurewebsites.sprintmood.data.FeedbackContract.FeedbackSubmitted;
import net.azurewebsites.sprintmood.data.FeedbackContract.FeedbackCache;

// from udc_sunshine_lesson4a : https://github.com/udacity/Sunshine-Version-2/blob/lesson_4_starter_code/data/WeatherDbHelper.java

/**
 * Manages a local database for feedback data.
 */
public class FeedbackDbHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "feedback.db";

    public FeedbackDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_FEEDBACK_CACHE_TABLE = "CREATE TABLE " + FeedbackCache.TABLE_NAME + " (" +
                // Why AutoIncrement here, and not above?
                // Unique keys will be auto-generated in either case.  But for weather
                // forecasting, it's reasonable to assume the user will want information
                // for a certain date and all dates *following*, so the forecast data
                // should be sorted accordingly.
                FeedbackCache._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +

                // the ID of the location entry associated with this weather data
                FeedbackCache.COLUMN_FEEDBACK_ID + " INTEGER NOT NULL, " +
                FeedbackCache.COLUMN_APPLIES_TO_TIME + " INTEGER NOT NULL, " +
                FeedbackCache.COLUMN_SPRINTUSER_ID + " INTEGER NOT NULL," +

                FeedbackCache.COLUMN_SCORE + " INT NOT NULL, " +
                FeedbackCache.COLUMN_COMMENT + " TEXT NOT NULL, " +
                FeedbackCache.COLUMN_IS_PRIVATE + " INT NOT NULL, " +

                // To assure the application have just one weather entry per day
                // per location, it's created a UNIQUE constraint with REPLACE strategy
                " UNIQUE (" + FeedbackCache.COLUMN_FEEDBACK_ID + ") ON CONFLICT REPLACE);";

        sqLiteDatabase.execSQL(SQL_CREATE_FEEDBACK_CACHE_TABLE);

        final String SQL_CREATE_FEEDBACK_SUBMITTED_TABLE = "CREATE TABLE " + FeedbackSubmitted.TABLE_NAME + " (" +
                // Why AutoIncrement here, and not above?
                // Unique keys will be auto-generated in either case.  But for weather
                // forecasting, it's reasonable to assume the user will want information
                // for a certain date and all dates *following*, so the forecast data
                // should be sorted accordingly.
                FeedbackSubmitted._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +

                // the ID of the location entry associated with this weather data
                FeedbackSubmitted.COLUMN_APPLIES_TO_TIME + " INTEGER NOT NULL, " +
				FeedbackSubmitted.COLUMN_SCORE + " INT NOT NULL, " +
				FeedbackSubmitted.COLUMN_COMMENT + " TEXT NOT NULL, " +
				FeedbackSubmitted.COLUMN_IS_PRIVATE + " INT NOT NULL);";

        sqLiteDatabase.execSQL(SQL_CREATE_FEEDBACK_SUBMITTED_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        // Note that this only fires if you change the version number for your database.
        // It does NOT depend on the version number for your application.
        // If you want to update the schema without wiping data, commenting out the next 2 lines
        // should be your top priority before modifying this method.
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FeedbackSubmitted.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FeedbackCache.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}