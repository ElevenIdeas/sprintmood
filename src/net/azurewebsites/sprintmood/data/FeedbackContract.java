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

import android.provider.BaseColumns;
import android.text.format.Time;

// from: udc_sunshine_l4a: https://github.com/udacity/Sunshine-Version-2/blob/lesson_4_starter_code/data/WeatherContract.java

/**
 * Defines table and column names for the feedback database.
 */
public class FeedbackContract {

    // To make it easy to query for the exact date, we normalize all dates that go into
    // the database to the start of the the Julian day at UTC.
    public static long normalizeDate(long startDate) {
        // normalize the start date to the beginning of the (UTC) day
        Time time = new Time();
        time.setToNow();
        int julianDay = Time.getJulianDay(startDate, time.gmtoff);
        return time.setJulianDay(julianDay);
    }

    /*
        Inner class that defines the table contents of the feedback table (from user)
     */
    public static final class FeedbackSubmitted implements BaseColumns {
        public static final String TABLE_NAME = "feedback_submitted";

        public static final String COLUMN_APPLIES_TO_TIME = "applies_to_time";
        public static final String COLUMN_SCORE = "score";
        public static final String COLUMN_IS_PRIVATE = "is_private";
        public static final String COLUMN_COMMENT = "comment";

    }

    /* Inner class that defines the table contents of the feedback_cache table (from server) */
    public static final class FeedbackCache implements BaseColumns {

        public static final String TABLE_NAME = "feedback_cache";

        // Date, stored as long in milliseconds since the epoch
        public static final String COLUMN_APPLIES_TO_TIME = "applies_to_time";
        // Weather id as returned by API, to identify the icon to be used
        public static final String COLUMN_FEEDBACK_ID = "feedback_id";

        // Short description and long description of the weather, as provided by API.
        // e.g "clear" vs "sky is clear".
        public static final String COLUMN_SPRINTUSER_ID = "sprintuser_id";

        // Min and max temperatures for the day (stored as floats)
        public static final String COLUMN_SCORE = "score";
        public static final String COLUMN_IS_PRIVATE = "is_private";

        // Humidity is stored as a float representing percentage
        public static final String COLUMN_COMMENT = "comment";

   }
}