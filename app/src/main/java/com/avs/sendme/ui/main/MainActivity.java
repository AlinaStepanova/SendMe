package com.avs.sendme.ui.main;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.avs.sendme.R;
import com.avs.sendme.provider.SendMeContractTest;
import com.avs.sendme.ui.preference.FollowingPreferenceActivity;
import com.avs.sendme.provider.SendMeContract;
import com.avs.sendme.provider.SendMeProvider;

import static com.avs.sendme.provider.SendMeContractTest.COLUMN_CONNECTED_KEY;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static String LOG_TAG = MainActivity.class.getSimpleName();
    private static final int LOADER_ID_MESSAGES = 0;
    private static final int LOADER_TEST = 1;

    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    SendMeAdapter adapter;

    static final String[] MESSAGES_PROJECTION = {
            SendMeContract.COLUMN_AUTHOR,
            SendMeContract.COLUMN_MESSAGE,
            SendMeContract.COLUMN_DATE,
            SendMeContract.COLUMN_AUTHOR_KEY,
            SendMeContract.COLUMN_ID
    };

    static final String[] TEST_PROJECTION = {
            SendMeContractTest.COLUMN_TITLE,
            SendMeContractTest.COLUMN_CONNECTED_KEY
    };

    static final int COL_NUM_AUTHOR = 0;
    static final int COL_NUM_MESSAGE = 1;
    static final int COL_NUM_DATE = 2;
    static final int COL_NUM_AUTHOR_KEY = 3;
    static final int COL_ID_KEY = 4;

    static final int COL_TEST_TITLE = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.squawks_recycler_view);

        // Use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // Use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // Add dividers
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                recyclerView.getContext(),
                layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        // Specify an adapter
        adapter = new SendMeAdapter();
        recyclerView.setAdapter(adapter);

        // Start the loader
        LoaderManager.getInstance(this).initLoader(LOADER_ID_MESSAGES, null, this);
        LoaderManager.getInstance(this).initLoader(LOADER_TEST, null, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_following_preferences) {
            // Opens the following activity when the menu icon is pressed
            Intent startFollowingActivity = new Intent(this, FollowingPreferenceActivity.class);
            startActivity(startFollowingActivity);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * Loader callbacks
     */

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader loader = new CursorLoader(this);
        switch (id) {
            case LOADER_ID_MESSAGES: {
                String selection = SendMeContract.createSelectionForCurrentFollowers(
                        PreferenceManager.getDefaultSharedPreferences(this));
                Log.d(LOG_TAG, "Selection is " + selection);
                loader = new CursorLoader(this, SendMeProvider.SendMeMessages.CONTENT_URI,
                        MESSAGES_PROJECTION, selection, null, SendMeContract.COLUMN_DATE + " DESC");
                break;
            }
            case LOADER_TEST: {
                loader = new CursorLoader(this, SendMeProvider.SendMeTest.CONTENT_URI,
                        TEST_PROJECTION, null, null, SendMeContractTest.COLUMN_ID + " DESC");
                break;
            }
        }
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        switch (loader.getId()) {
            case LOADER_ID_MESSAGES: {
                adapter.swapCursor(data);
                break;
            }
            case LOADER_TEST: {
                data.moveToFirst();
                Log.d(LOG_TAG, data.getString(COL_TEST_TITLE));
                break;
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }
}
