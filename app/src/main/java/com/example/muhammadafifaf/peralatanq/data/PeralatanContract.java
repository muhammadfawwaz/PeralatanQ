package com.example.muhammadafifaf.peralatanq.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Muhammad Afif AF on 04/11/2017.
 */

public final class PeralatanContract {
    public static final String CONTENT_AUTHORITY = "com.example.android.peralatan";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_ALAT = "alat";

    public  static final class PeralatanEntry implements BaseColumns {
        /**
         * The MIME type of the {@link #CONTENT_URI} for a list of pets.
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ALAT;

        /**
         * The MIME type of the {@link #CONTENT_URI} for a single pet.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ALAT;
        public final static String TABLE_NAME = "alat";

        public final static String _id = BaseColumns._ID;
        public final static String BARANG_COLUMN = "barang";
        public final static String WARNA_COLUMN = "warna";
        public final static String JUMLAH_COLUMN = "jumlah";

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI,PATH_ALAT);
    }
}
