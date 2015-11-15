package ca.uwaterloo.sh6choi.hanzi.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Samson on 2015-10-23.
 */
public class HanziSQLiteOpenHelper extends SQLiteOpenHelper{
    private static final String TAG = HanziSQLiteOpenHelper.class.getCanonicalName();

    public static final String TABLE_PINYIN = "pinyin";
    public static final String COLUMN_PINYIN = "pinyin";
    public static final String COLUMN_ZHUYIN = "zhuyin";
    public static final String COLUMN_NO_INTIAL_FORM = "no_intial_form";
    public static final String COLUMN_ALTERNATE_FORM = "alternate_form";
    public static final String COLUMN_TYPE = "type";

    public static final String TABLE_NUMBERS = "numbers";
    public static final String COLUMN_NUMBER = "number";
    public static final String COLUMN_TRADITIONAL = "traditional";
    public static final String COLUMN_SIMPLIFIED = "simplified";
    public static final String COLUMN_PINYIN_NUMBER = "pinyin";

    private static final String DATABASE_NAME = "Hanzi.db";
    private static final int DATABASE_VERSION = 3;

    private static final String TABLE_PINYIN_CREATE = "CREATE TABLE " + TABLE_PINYIN + "(" +
            COLUMN_PINYIN + " TEXT NOT NULL PRIMARY KEY, " +
            COLUMN_ZHUYIN + " TEXT, " +
            COLUMN_NO_INTIAL_FORM + " TEXT, " +
            COLUMN_ALTERNATE_FORM + " TEXT, " +
            COLUMN_TYPE + " TEXT NOT NULL);";

    private static final String TABLE_NUMBERS_CREATE = "CREATE TABLE " + TABLE_NUMBERS + " (" +
            COLUMN_NUMBER + " INTEGER NOT NULL PRIMARY KEY, " +
            COLUMN_TRADITIONAL + " TEXT, " +
            COLUMN_SIMPLIFIED + " TEXT, " +
            COLUMN_PINYIN_NUMBER + ");";


    public HanziSQLiteOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_PINYIN_CREATE);
        db.execSQL(TABLE_NUMBERS_CREATE);
        Log.d(TAG, "Database Created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PINYIN);
        onCreate(db);
    }
}
