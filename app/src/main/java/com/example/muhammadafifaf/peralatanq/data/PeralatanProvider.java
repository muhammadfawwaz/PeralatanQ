package com.example.muhammadafifaf.peralatanq.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by Muhammad Afif AF on 05/11/2017.
 */

public class PeralatanProvider extends ContentProvider {
    private PeralatanOpenHelper peralatanOpenHelper;
    private static final int PERALATAN = 100;
    private static final int PERALATAN_ID = 101;
    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(PeralatanContract.CONTENT_AUTHORITY, PeralatanContract.PATH_ALAT, PERALATAN);
        uriMatcher.addURI(PeralatanContract.CONTENT_AUTHORITY, PeralatanContract.PATH_ALAT + "/#", PERALATAN_ID);
    }

    @Override
    public boolean onCreate() {
        peralatanOpenHelper = new PeralatanOpenHelper(getContext());
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase sqLiteDatabase = peralatanOpenHelper.getReadableDatabase();
        Cursor cursor;

        int match = uriMatcher.match(uri);
        switch (match) {
            case PERALATAN:
                cursor = sqLiteDatabase.query(PeralatanContract.PeralatanEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case PERALATAN_ID:
                selection = PeralatanContract.PeralatanEntry._id + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = sqLiteDatabase.query(PeralatanContract.PeralatanEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Error Bos di uri " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(),uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = uriMatcher.match(uri);
        switch (match) {
            case PERALATAN:
                return PeralatanContract.PeralatanEntry.CONTENT_LIST_TYPE;
            case PERALATAN_ID:
                return PeralatanContract.PeralatanEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final int match = uriMatcher.match(uri);
        switch (match) {
            case PERALATAN:
                return insertBarang(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    public Uri insertBarang(Uri uri, ContentValues values) {
        SQLiteDatabase sqLiteDatabase = peralatanOpenHelper.getWritableDatabase();
        String barang = values.getAsString(PeralatanContract.PeralatanEntry.BARANG_COLUMN);
        if (barang == null) {
            throw new IllegalArgumentException("Pet requires a barang");
        }

        String warna = values.getAsString(PeralatanContract.PeralatanEntry.WARNA_COLUMN);
        if (warna == null) {
            throw new IllegalArgumentException("Pet requires a warna");
        }

        Integer jumlah = values.getAsInteger(PeralatanContract.PeralatanEntry.JUMLAH_COLUMN);
        if (jumlah == null && jumlah < 0) {
            throw new IllegalArgumentException("Pet requires a jumlah");
        }
        long row = sqLiteDatabase.insert(PeralatanContract.PeralatanEntry.TABLE_NAME, null, values);
        getContext().getContentResolver().notifyChange(uri,null);
        return ContentUris.withAppendedId(uri, row);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        SQLiteDatabase sqLiteDatabase = peralatanOpenHelper.getWritableDatabase();
        final int match = uriMatcher.match(uri);

        switch(match) {
            case PERALATAN:
                return sqLiteDatabase.delete(PeralatanContract.PeralatanEntry.TABLE_NAME,s,strings);
            case PERALATAN_ID:
                s = PeralatanContract.PeralatanEntry._id + "=?";
                strings = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return sqLiteDatabase.delete(PeralatanContract.PeralatanEntry.TABLE_NAME, s, strings);
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String selection, @Nullable String[] selectionArgs) {
        final int match = uriMatcher.match(uri);
        switch (match) {
            case PERALATAN:
                return updateBarang(uri, contentValues, selection, selectionArgs);
            case PERALATAN_ID:
                // For the PET_ID code, extract out the ID from the URI,
                // so we know which row to update. Selection will be "_id=?" and selection
                // arguments will be a String array containing the actual ID.
                selection = PeralatanContract.PeralatanEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateBarang(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    private int updateBarang(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String selection, @Nullable String[] selectionArgs) {
        if(contentValues.containsKey(PeralatanContract.PeralatanEntry.BARANG_COLUMN)) {
            String barang = contentValues.getAsString(PeralatanContract.PeralatanEntry.BARANG_COLUMN);
            if (barang == null) {
                throw new IllegalArgumentException("Pet requires a barang");
            }
        }

        if(contentValues.containsKey(PeralatanContract.PeralatanEntry.WARNA_COLUMN)) {
            String warna = contentValues.getAsString(PeralatanContract.PeralatanEntry.WARNA_COLUMN);
            if (warna == null) {
                throw new IllegalArgumentException("Pet requires a warna");
            }
        }

        if(contentValues.containsKey(PeralatanContract.PeralatanEntry.JUMLAH_COLUMN)) {
            Integer jumlah = contentValues.getAsInteger(PeralatanContract.PeralatanEntry.JUMLAH_COLUMN);
            if (jumlah == null && jumlah < 0) {
                throw new IllegalArgumentException("Pet requires a jumlah");
            }
        }

        if (contentValues.size() == 0) {
            return 0;
        }

        SQLiteDatabase sqLiteDatabase = peralatanOpenHelper.getWritableDatabase();
        getContext().getContentResolver().notifyChange(uri,null);
        return sqLiteDatabase.update(PeralatanContract.PeralatanEntry.TABLE_NAME,contentValues,selection,selectionArgs);
    }
}
