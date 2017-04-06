package com.karbaros.userregistrationusingsqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by shanu on 04-Apr-17.
 */

public class DBAdapter extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "userData.db";
    private static final String TABLE_NAME = "login_table";

    private static final String COL_1 = "_id";
    private static final String COL_2 = "_name";
    private static final String COL_3 = "_email";
    private static final String COL_4 = "_password";
    private static final String COL_5 = "_number";
    private static final String COL_6 = "_status";
    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" + COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_2 +
            " TEXT, " + COL_3 + " TEXT, " + COL_4 + " TEXT, " + COL_5 + " INTEGER, " + COL_6 + " TEXT)";

    private static final String DRP_TABLE = "DROP TABLE IF EXIST " + TABLE_NAME;

    public DBAdapter(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //sqLiteDatabase.execSQL(DRP_TABLE);
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(DRP_TABLE);
        onCreate(sqLiteDatabase);
    }

    public boolean insertData(UserDetail userDetail) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, userDetail.getName());
        contentValues.put(COL_3, userDetail.getEmail());
        contentValues.put(COL_4, userDetail.getPassword());
        contentValues.put(COL_5, userDetail.getNumber());
        contentValues.put(COL_6, userDetail.getStatus());
        return ((sqLiteDatabase.insert(TABLE_NAME, null, contentValues)) == -1 ? false : true);
    }

    public boolean checkUser(String email) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor mCursor = sqLiteDatabase.rawQuery("SELECT " + COL_1 + " FROM " + TABLE_NAME + " WHERE " + COL_3 + " = ?", new String[]{email});
        int dataCount = mCursor.getCount();
        mCursor.close();
        sqLiteDatabase.close();
        if (dataCount > 0)
            return true;
        else
            return false;
    }

    public int loginCheck(String email, String password) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor mCursor = sqLiteDatabase.rawQuery("SELECT " + COL_1 + " FROM " + TABLE_NAME + " WHERE " + COL_3 + " = ? AND " + COL_4 + " = ?", new String[]{email, password});
        int dataCount = mCursor.getCount();
        mCursor.moveToFirst();
        String id = mCursor.getString(0);
        if (dataCount > 0) {

            return Integer.parseInt(mCursor.getString(0));
        } else
            return -1;

    }

    public boolean updateData(String id, String status) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1, id);
        contentValues.put(COL_6, status);

        sqLiteDatabase.update(TABLE_NAME, contentValues, COL_1 + " = ?", new String[]{id});
        return true;

    }

    public Cursor checkActiveUser() {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor result = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COL_6 + " = ?", new String[]{"ACTIVE"});
        Log.i("user",""+result.getCount());
        return result;

    }

    public String getLatestUser () {
        //SELECT ROWID from SQL_LITE_SEQUENCE order by ROWID DESC limit 1
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT "+COL_3+" FROM " + TABLE_NAME + " ORDER BY "+COL_1+" DESC limit 1", null);
        cursor.moveToFirst();
        Log.i("user",cursor.getString(0));

        return cursor.getString(0);

    }

}
