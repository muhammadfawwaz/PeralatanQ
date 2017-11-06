package com.example.muhammadafifaf.peralatanq.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.muhammadafifaf.peralatanq.data.PeralatanContract.PeralatanEntry;

/**
 * Created by Muhammad Afif AF on 04/11/2017.
 */

public class PeralatanOpenHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "PeralatanDb";
    public static final String SQL_DELETE_ENTRIES = "DROP TABLE alat";

    public PeralatanOpenHelper(Context context) {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String SQL_CREATE_PERALATAN_TABLE = "CREATE TABLE " + PeralatanEntry.TABLE_NAME + "(" +
                PeralatanEntry._id + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                PeralatanEntry.BARANG_COLUMN + " TEXT NOT NULL, " +
                PeralatanEntry.WARNA_COLUMN + " TEXT, " +
                PeralatanEntry.JUMLAH_COLUMN + " INTEGER NOT NULL DEFAULT 0);";

        sqLiteDatabase.execSQL(SQL_CREATE_PERALATAN_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int newV, int old) {
        sqLiteDatabase.execSQL(SQL_DELETE_ENTRIES);
        onCreate(sqLiteDatabase);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db,oldVersion,newVersion);
    }
}
