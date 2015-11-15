package ca.uwaterloo.sh6choi.hanzi.database;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ca.uwaterloo.sh6choi.hanzi.model.ChineseNumber;

/**
 * Created by Samson on 2015-11-01.
 */
public class NumberDataSource {

    private SQLiteDatabase mDatabase;
    private HanziSQLiteOpenHelper mHelper;
    private String[] mColumns = { HanziSQLiteOpenHelper.COLUMN_NUMBER,
            HanziSQLiteOpenHelper.COLUMN_TRADITIONAL,
            HanziSQLiteOpenHelper.COLUMN_SIMPLIFIED,
            HanziSQLiteOpenHelper.COLUMN_PINYIN_NUMBER};

    public NumberDataSource(Context context) {
        mHelper = new HanziSQLiteOpenHelper(context);
    }

    public void open() throws SQLException {
        mDatabase = mHelper.getWritableDatabase();
    }

    public void close() {
        mHelper.close();
    }

    public void queryNumbers(final DatabaseRequestCallback<List<ChineseNumber>> callback) {
        new AsyncTask<Void, Void, List<ChineseNumber>>() {
            @Override
            protected List<ChineseNumber> doInBackground(Void... params) {
                List<ChineseNumber> ChineseNumbers = new ArrayList<>();

                Cursor cursor = mDatabase.query(HanziSQLiteOpenHelper.TABLE_NUMBERS, mColumns, null, null, null, null, null);

                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    ChineseNumbers.add(cursorToNumber(cursor));
                    cursor.moveToNext();
                }

                cursor.close();

                Collections.sort(ChineseNumbers, new ChineseNumber.NumberComparator());
                return ChineseNumbers;
            }

            @Override
            protected void onPostExecute(List<ChineseNumber> hangulCharacterList) {
                callback.processResults(hangulCharacterList);
            }

        }.execute();
    }

    public void queryNumber(final int number, final DatabaseRequestCallback<List<ChineseNumber>> callback) {
        new AsyncTask<Void, Void, List<ChineseNumber>>() {
            @Override
            protected List<ChineseNumber> doInBackground(Void... params) {
                List<ChineseNumber> chineseNumbers = new ArrayList<>();

                Cursor cursor = mDatabase.query(HanziSQLiteOpenHelper.TABLE_NUMBERS, mColumns, "WHERE " + HanziSQLiteOpenHelper.TABLE_NUMBERS + "." + HanziSQLiteOpenHelper.COLUMN_NUMBER + " =?", new String[]{Integer.toString(number)}, null, null, null);

                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    chineseNumbers.add(cursorToNumber(cursor));
                    cursor.moveToNext();
                }

                cursor.close();

                Collections.sort(chineseNumbers, new ChineseNumber.NumberComparator());
                return chineseNumbers;
            }

            @Override
            protected void onPostExecute(List<ChineseNumber> hangulCharacterList) {
                callback.processResults(hangulCharacterList);
            }

        }.execute();
    }

    public void update(final List<ChineseNumber> ChineseNumbers, final DatabaseRequestCallback<Void> callback ) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {

                String sql = "INSERT OR REPLACE INTO " + HanziSQLiteOpenHelper.TABLE_NUMBERS + "(" +
                        HanziSQLiteOpenHelper.COLUMN_NUMBER + ", " +
                        HanziSQLiteOpenHelper.COLUMN_TRADITIONAL + ", " +
                        HanziSQLiteOpenHelper.COLUMN_SIMPLIFIED + ", " +
                        HanziSQLiteOpenHelper.COLUMN_PINYIN_NUMBER + ") VALUES ";

                StringBuilder builder = new StringBuilder(sql);
                for (int i = 0; i < ChineseNumbers.size(); i ++) {
                    ChineseNumber ChineseNumber = ChineseNumbers.get(i);
                    String format = "(\"%1$s\", \"%2$s\", \"%3$s\", \"%4$s\")";

                    builder.append(String.format(format, ChineseNumber.getNumber(), ChineseNumber.getTraditional(), ChineseNumber.getSimplified(), ChineseNumber.getPinyin()));
                    if (i < ChineseNumbers.size() - 1) {
                        builder.append(", ");
                    }
                }
                builder.append(";");
                SQLiteStatement statement = mDatabase.compileStatement(builder.toString());
                statement.executeInsert();
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                if (callback != null) {
                    callback.processResults(aVoid);
                }
            }
        }.execute();
    }

    private ChineseNumber cursorToNumber(Cursor cursor) {
        ChineseNumber ChineseNumber = new ChineseNumber(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3));
        return ChineseNumber;
    }
}
