package com.example.muhammadafifaf.peralatanq;

import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.muhammadafifaf.peralatanq.data.PeralatanContract;
import com.example.muhammadafifaf.peralatanq.data.PeralatanOpenHelper;

public class EditorActivity extends AppCompatActivity implements android.app.LoaderManager.LoaderCallbacks<Cursor>{
    PeralatanOpenHelper peralatanOpenHelper;
    EditText barangView;
    EditText warnaView;
    EditText jumlahView;
    private static final int PERALATAN_LOADER = 0;
    Uri currentUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        barangView = (EditText) findViewById(R.id.edit_barang);
        warnaView = (EditText) findViewById(R.id.edit_warna);
        jumlahView = (EditText) findViewById(R.id.edit_jumlah);

        peralatanOpenHelper = new PeralatanOpenHelper(this);
        Intent intent = getIntent();
        currentUri = intent.getData();
        if(currentUri == null) {
            setTitle("add barang");
            invalidateOptionsMenu();
        }
        else {
            setTitle("edit barang");
        }


        getLoaderManager().initLoader(PERALATAN_LOADER,null,this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.editor_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_delete:
                deletePet();
                finish();
                return true;
            case R.id.action_save:
                insertPeralatan();
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    void insertPeralatan() {
        ContentValues cv = new ContentValues();

        if(currentUri == PeralatanContract.PeralatanEntry.CONTENT_URI) {
            Log.v("if","insert");
            cv.put(PeralatanContract.PeralatanEntry.BARANG_COLUMN,barangView.getText().toString().trim());
            cv.put(PeralatanContract.PeralatanEntry.WARNA_COLUMN,warnaView.getText().toString().trim());
            cv.put(PeralatanContract.PeralatanEntry.JUMLAH_COLUMN,jumlahView.getText().toString().trim());

            Uri newUri = getContentResolver().insert(PeralatanContract.PeralatanEntry.CONTENT_URI,cv);
            if (newUri == null) {
                // If the new content URI is null, then there was an error with insertion.
                Toast.makeText(this, "Gagal Bos",
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the insertion was successful and we can display a toast.
                Toast.makeText(this, "Sukses Bos",
                        Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Log.v("else", String.valueOf(currentUri));
            cv.put(PeralatanContract.PeralatanEntry.BARANG_COLUMN,barangView.getText().toString().trim());
            cv.put(PeralatanContract.PeralatanEntry.WARNA_COLUMN,warnaView.getText().toString().trim());
            cv.put(PeralatanContract.PeralatanEntry.JUMLAH_COLUMN,Integer.parseInt(jumlahView.getText().toString().trim()));
            Integer newUri = getContentResolver().update(currentUri,cv,null,null);
            if (newUri == null) {
                // If the new content URI is null, then there was an error with insertion.
                Toast.makeText(this, "Gagal Bos",
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the insertion was successful and we can display a toast.
                Toast.makeText(this, "Sukses Bos",
                        Toast.LENGTH_SHORT).show();
            }
        }


    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

        if(currentUri == null) {
            currentUri = PeralatanContract.PeralatanEntry.CONTENT_URI;
        }

        String[] projection = {PeralatanContract.PeralatanEntry._id, PeralatanContract.PeralatanEntry.BARANG_COLUMN, PeralatanContract.PeralatanEntry.WARNA_COLUMN, PeralatanContract.PeralatanEntry.JUMLAH_COLUMN};
        return new CursorLoader(this,currentUri,projection,null,null,null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor.moveToFirst()) {
            // Find the columns of pet attributes that we're interested in
            int barangColumnIndex = cursor.getColumnIndex(PeralatanContract.PeralatanEntry.BARANG_COLUMN);
            int warnaColumnIndex = cursor.getColumnIndex(PeralatanContract.PeralatanEntry.WARNA_COLUMN);
            int jumlahColumnIndex = cursor.getColumnIndex(PeralatanContract.PeralatanEntry.JUMLAH_COLUMN);

            // Extract out the value from the Cursor for the given column index
            String barang = cursor.getString(barangColumnIndex);
            String warna = cursor.getString(warnaColumnIndex);
            String jumlah = cursor.getString(jumlahColumnIndex);

            // Update the views on the screen with the values from the database
            if(currentUri == PeralatanContract.PeralatanEntry.CONTENT_URI) {
                barang = "";
                warna = "";
                jumlah ="";
            }
            barangView.setText(barang);
            warnaView.setText(warna);
            jumlahView.setText(jumlah);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        // If this is a new pet, hide the "Delete" menu item.
        if (currentUri == PeralatanContract.PeralatanEntry.CONTENT_URI) {
            MenuItem menuItem = menu.findItem(R.id.action_delete);
            menuItem.setVisible(false);
        }
        return true;
    }

    private void deletePet() {
        int rowsDeleted = getContentResolver().delete(currentUri, null, null);
        if (rowsDeleted == 0) {
            // If no rows were deleted, then there was an error with the delete.
            Toast.makeText(this, "Delete Gagal",
                    Toast.LENGTH_SHORT).show();
        } else {
            // Otherwise, the delete was successful and we can display a toast.
            Toast.makeText(this, "Delete Sukses",
                    Toast.LENGTH_SHORT).show();
        }
        getContentResolver().notifyChange(currentUri,null);
    }
}
