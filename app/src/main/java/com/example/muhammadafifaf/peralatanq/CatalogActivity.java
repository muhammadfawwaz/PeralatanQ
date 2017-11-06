package com.example.muhammadafifaf.peralatanq;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.muhammadafifaf.peralatanq.data.PeralatanContract.PeralatanEntry;

import com.example.muhammadafifaf.peralatanq.data.PeralatanCursorAdapter;
import com.example.muhammadafifaf.peralatanq.data.PeralatanOpenHelper;

import java.util.List;

public class CatalogActivity extends AppCompatActivity implements android.app.LoaderManager.LoaderCallbacks<Cursor> {
    PeralatanOpenHelper peralatanOpenHelper;
    private static final int PERALATAN_LOADER = 0;
    PeralatanCursorAdapter peralatanCursorAdapter;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);
        Log.v("onCreate","hmm");

        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.float_button);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CatalogActivity.this,EditorActivity.class);
                startActivity(intent);
            }
        });
        peralatanOpenHelper = new PeralatanOpenHelper(this);
        peralatanCursorAdapter = new PeralatanCursorAdapter(this, null);
        listView = (ListView) findViewById(R.id.list_view);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                Intent intent = new Intent(CatalogActivity.this,EditorActivity.class);
                Uri newUri = ContentUris.withAppendedId(PeralatanEntry.CONTENT_URI,id);
                intent.setData(newUri);
                startActivity(intent);
            }
        });

        getLoaderManager().initLoader(PERALATAN_LOADER,null,this);
        listView.setAdapter(peralatanCursorAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.catalog_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_insert_dummy_data:
                inputPeralatan();
                return true;

            case R.id.action_delete_all_entries:
                deleteAll();
                return true;
        }
        finish();
        return super.onOptionsItemSelected(item);
    }

    void inputPeralatan() {
        ContentValues cv = new ContentValues();
        cv.put(PeralatanEntry.BARANG_COLUMN,"Kulkas");
        cv.put(PeralatanEntry.WARNA_COLUMN,"Biru");
        cv.put(PeralatanEntry.JUMLAH_COLUMN,2);

        Uri newUri = getContentResolver().insert(PeralatanEntry.CONTENT_URI,cv);
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

    @Override
    public android.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.v("onCreateLoader","hmm");
        String[] projection = {PeralatanEntry._id,PeralatanEntry.BARANG_COLUMN,PeralatanEntry.WARNA_COLUMN,PeralatanEntry.JUMLAH_COLUMN};
        listView.setAdapter(peralatanCursorAdapter);
        return new android.content.CursorLoader(this,PeralatanEntry.CONTENT_URI,projection,null,null,null);
    }

    @Override
    public void onLoadFinished(android.content.Loader<Cursor> loader, Cursor cursor) {
        Log.v("onLoadFinished","hmm");
        peralatanCursorAdapter.swapCursor(cursor);
        listView.setAdapter(peralatanCursorAdapter);
    }

    @Override
    public void onLoaderReset(android.content.Loader<Cursor> loader) {
        Log.v("onLoaderReset","hmm");
        peralatanCursorAdapter.swapCursor(null);
    }

    public void deleteAll() {
        int rowsDeleted = getContentResolver().delete(PeralatanEntry.CONTENT_URI, null, null);
        getContentResolver().notifyChange(PeralatanEntry.CONTENT_URI,null);
    }
}
