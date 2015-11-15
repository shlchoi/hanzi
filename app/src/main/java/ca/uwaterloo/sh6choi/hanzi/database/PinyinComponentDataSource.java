package ca.uwaterloo.sh6choi.hanzi.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

import ca.uwaterloo.sh6choi.hanzi.model.PinyinComponent;

/**
 * Created by Samson on 2015-10-23.
 */
public class PinyinComponentDataSource {

    private SQLiteDatabase mDatabase;
    private HanziSQLiteOpenHelper mHelper;
    private String[] mColumns = { HanziSQLiteOpenHelper.COLUMN_PINYIN_NUMBER,
            HanziSQLiteOpenHelper.COLUMN_ZHUYIN,
            HanziSQLiteOpenHelper.COLUMN_NO_INTIAL_FORM,
            HanziSQLiteOpenHelper.COLUMN_ALTERNATE_FORM,
            HanziSQLiteOpenHelper.COLUMN_TYPE};

    public PinyinComponentDataSource(Context context) {
        mHelper = new HanziSQLiteOpenHelper(context);
    }

    public void open() throws SQLException {
        mDatabase = mHelper.getWritableDatabase();
    }

    public void close() {
        mHelper.close();
    }

    public void queryPinyin(final DatabaseRequestCallback<List<PinyinComponent>> callback) {
        new AsyncTask<Void, Void, List<PinyinComponent>>() {
            @Override
            protected List<PinyinComponent> doInBackground(Void... params) {
                List<PinyinComponent> pinyinComponents = new ArrayList<>();

                Cursor cursor = mDatabase.query(HanziSQLiteOpenHelper.TABLE_PINYIN, mColumns, null, null, null, null, null);

                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    pinyinComponents.add(cursorToCharacter(cursor));
                    cursor.moveToNext();
                }

                cursor.close();

                //Collections.sort(pinyinComponents, new PinyinComponent.CharacterComparator());
                return pinyinComponents;
            }

            @Override
            protected void onPostExecute(List<PinyinComponent> pinyinComponentList) {
                callback.processResults(pinyinComponentList);
            }

        }.execute();
    }

    public void update(final PinyinComponent pinyinComponent, final DatabaseRequestCallback<Void> callback ) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(HanziSQLiteOpenHelper.COLUMN_PINYIN_NUMBER, pinyinComponent.getPinyin());
                contentValues.put(HanziSQLiteOpenHelper.COLUMN_ZHUYIN, pinyinComponent.getZhuyin());
                if (pinyinComponent.getNoIntialForm() != null) {
                    contentValues.put(HanziSQLiteOpenHelper.COLUMN_NO_INTIAL_FORM, pinyinComponent.getNoIntialForm());
                }

                if (pinyinComponent.getAlternateForm() != null) {
                    contentValues.put(HanziSQLiteOpenHelper.COLUMN_ALTERNATE_FORM, pinyinComponent.getAlternateForm());
                }

                contentValues.put(HanziSQLiteOpenHelper.COLUMN_TYPE, pinyinComponent.getType());

                mDatabase.insertWithOnConflict(HanziSQLiteOpenHelper.TABLE_PINYIN, null, contentValues,
                        SQLiteDatabase.CONFLICT_REPLACE);

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

    private PinyinComponent cursorToCharacter(Cursor cursor) {
        PinyinComponent character = new PinyinComponent(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4));
        return character;
    }
}
