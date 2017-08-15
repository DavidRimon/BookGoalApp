package com.example.david.bookgoalapp;

import java.util.ArrayList;

import static com.example.david.bookgoalapp.BookGoalMySQLiteDBDiffinition.BookGoalTableDiffinition.POS_TYPES.AMOD;
import static com.example.david.bookgoalapp.BookGoalMySQLiteDBDiffinition.BookGoalTableDiffinition.POS_TYPES.DAF;

/**
 * Created by David on 24-Jun-17.
 */

public  abstract class BookGoalMySQLiteDBDiffinition {
    public static final String DATABASE_NAME = "book_goal_db";
    public static final int DATABASE_VERSION = 10;
    //the table name
    public static final String TABLE_BOOKGOAL_NAME = "bookGoal_table";
    //the tables diffnition
    public static abstract class BookGoalTableDiffinition { //SQL cullumn name
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_START_POS = "startingPosition";
        public static final String COLUMN_END_POS = "endingPosition";
        public static final String COLUMN_CUR_POS = "currentPosition";
        public static final String COLUMN_POS_TYPE = "positionType";
        public static final String COLUMN_RATE = "rate";
        public static final String COLUMN_START_DATE = "StartingDate";
        public static final String DATE_FORMAT = "dd-MM-yy";
        public static final String COLUMN_COLOR = "color";
        public static final String COLUMN_AT_TIME = "everyDayAt";
        public static final String COLUMN_ENABLED = "enabled";
        public static final String COLUMN_NOTE = "note";
        public static enum POS_TYPES {
            AMOD("עמוד"),//using this will make start and end refer to amod
            DAF("דף"),
            PEREK("פרק"),
            MISHNA("משנה"),
            PARASHA("פרשה"),
            HALACHA("הלכה"),
            PAGE("page");

            private final String text;
            private POS_TYPES(String text){
                this.text = text;
            }
            public String toString() {
                return text;
            }
        }
        public static final int[] COLORS_ARRAY = {R.color.holo_blue_bright,
                R.color.holo_blue_light,
                R.color.holo_blue_dark,
                R.color.holo_green_light,
                R.color.holo_green_dark,
                R.color.holo_orange_light,
                R.color.holo_orange_dark,
                R.color.holo_red_light,
                R.color.holo_red_dark,
                R.color.holo_purple};
        public static POS_TYPES POS_TYPESvalueOf(String str) throws Exception {

            for (POS_TYPES p:POS_TYPES.values())
                if(p.toString() == str)
                    return p;
            //TODO:: extract this string
            throw new Exception("Invalid value for pos types");
        }
    }
    //command to create the SQL table
    public static final String CREATE_TABLE_BOOKGOAL_CMD =
            "CREATE TABLE "+ BookGoalMySQLiteDBDiffinition.TABLE_BOOKGOAL_NAME +
                    "(" +
                    BookGoalTableDiffinition.COLUMN_ID 			+ " INTEGER PRIMARY KEY AUTOINCREMENT not null, " +
                    BookGoalTableDiffinition.COLUMN_NAME 		+ " varchar(60) unique not null ," +
                    BookGoalTableDiffinition.COLUMN_START_POS	+ " int 		  ," +
                    BookGoalTableDiffinition.COLUMN_END_POS 	+ " int 		  ," +
                    BookGoalTableDiffinition.COLUMN_CUR_POS 	+ " int 		  ," +
                    BookGoalTableDiffinition.COLUMN_POS_TYPE 	+ " varchar(15)   ," +
                    BookGoalTableDiffinition.COLUMN_RATE 		+ " int default 1 ," +
                    BookGoalTableDiffinition.COLUMN_START_DATE 	+ " varchar(15)   ," +
                    BookGoalTableDiffinition.COLUMN_COLOR 		+ " int 		  ," +
                    BookGoalTableDiffinition.COLUMN_AT_TIME 	+ " varchar(10) default ''," +
                    BookGoalTableDiffinition.COLUMN_ENABLED     + " tinyint(1)  default 1 ," +
                    BookGoalTableDiffinition.COLUMN_NOTE        + " varchar(100)   " +
                    ");" +
                    "insert into " + BookGoalMySQLiteDBDiffinition.TABLE_BOOKGOAL_NAME +
                    "(" + BookGoalTableDiffinition.COLUMN_ID + "," + BookGoalTableDiffinition.COLUMN_NAME + ")" +
                    "values (1000,'temp');";

}