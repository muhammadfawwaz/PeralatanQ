package com.example.muhammadafifaf.peralatanq.data;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.muhammadafifaf.peralatanq.R;

/**
 * Created by Muhammad Afif AF on 05/11/2017.
 */

public class PeralatanCursorAdapter extends CursorAdapter {

    /**
     * Constructs a new {@link PeralatanCursorAdapter}.
     *
     * @param context The context
     * @param c       The cursor from which to get the data.
     */
    public PeralatanCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }

    /**
     * Makes a new blank list item view. No data is set (or bound) to the views yet.
     *
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already
     *                moved to the correct position.
     * @param parent  The parent to which the new view is attached to
     * @return the newly created list item view.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // TODO: Fill out this method and return the list item view (instead of null)
        return LayoutInflater.from(context).inflate(R.layout.list_data,parent,false);
    }

    /**
     * This method binds the pet data (in the current row pointed to by cursor) to the given
     * list item layout. For example, the name for the current pet can be set on the name TextView
     * in the list item layout.
     *
     * @param view    Existing view, returned earlier by newView() method
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already moved to the
     *                correct row.
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // TODO: Fill out this method
        TextView barangView = (TextView) view.findViewById(R.id.barang);
        TextView warnaView = (TextView) view.findViewById(R.id.warna);
        TextView jumlahView = (TextView) view.findViewById(R.id.jumlah);

        int barangIndex = cursor.getColumnIndex(PeralatanContract.PeralatanEntry.BARANG_COLUMN);
        int warnaIndex = cursor.getColumnIndex(PeralatanContract.PeralatanEntry.WARNA_COLUMN);
        int jumlahIndex = cursor.getColumnIndex(PeralatanContract.PeralatanEntry.JUMLAH_COLUMN);

        String barang = cursor.getString(barangIndex);
        String warna = cursor.getString(warnaIndex);
        String jumlah = cursor.getString(jumlahIndex);

        barangView.setText(barang);
        warnaView.setText(warna);
        jumlahView.setText(jumlah);
    }
}
