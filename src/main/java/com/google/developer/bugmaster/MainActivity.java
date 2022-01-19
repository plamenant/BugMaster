package com.google.developer.bugmaster;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.developer.bugmaster.data.DatabaseManager;
import com.google.developer.bugmaster.data.Insect;
import com.google.developer.bugmaster.data.InsectRecyclerAdapter;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity implements
        View.OnClickListener, LoaderManager.LoaderCallbacks<Cursor> {

    public static final String ACTION_START_QUIZ = "action_start_quiz";

    private static final int LOADER_ID = 1;
    private static final String SORT_ORDER_KEY = "sort_order_key";

    private String mCurrentOrder;
    private ArrayList<Insect> mInsectsList;
    private InsectRecyclerAdapter mInsectRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);

        RecyclerView insectRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        insectRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mInsectRecyclerAdapter = new InsectRecyclerAdapter(this);
        insectRecyclerView.setAdapter(mInsectRecyclerAdapter);

        mCurrentOrder = (savedInstanceState != null && savedInstanceState.containsKey(SORT_ORDER_KEY)) ?
                savedInstanceState.getString(SORT_ORDER_KEY) : DatabaseManager.ASC_ORDER_BY_NAME;
        Bundle args = new Bundle();
        args.putString(SORT_ORDER_KEY, mCurrentOrder);

        getSupportLoaderManager().initLoader(LOADER_ID, args, this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);
        outState.putString(SORT_ORDER_KEY, mCurrentOrder);
    }

    @Override
    protected void onNewIntent(Intent intent) {

        super.onNewIntent(intent);
        String action = intent.getAction();
        if (action != null && action.equals(ACTION_START_QUIZ)) {
            getIntent().setAction(null);
            startQuizActivity();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_sort:
                changeListOrder();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void changeListOrder() {

        mCurrentOrder = mCurrentOrder.equals(DatabaseManager.ASC_ORDER_BY_NAME) ?
                DatabaseManager.DESC_ORDER_BY_DANGER_LVL : DatabaseManager.ASC_ORDER_BY_NAME;

        Bundle args = new Bundle();
        args.putString(SORT_ORDER_KEY, mCurrentOrder);
        getSupportLoaderManager().restartLoader(LOADER_ID, args, this);
    }

    /* Click events in Floating Action Button */
    @Override
    public void onClick(View v) {

        startQuizActivity();
    }

    private void startQuizActivity() {

        Intent intent = new Intent(this, QuizActivity.class);
        Collections.shuffle(mInsectsList);
        intent.putParcelableArrayListExtra(QuizActivity.EXTRA_INSECTS, mInsectsList);
        intent.putExtra(QuizActivity.EXTRA_ANSWER, mInsectsList.get(0));
        startActivity(intent);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        return new InsectsAsyncTaskLoader(this, args);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        retrieveAllInsects(data);
        mInsectRecyclerAdapter.swapCursor(data);

        String action = getIntent().getAction();
        if (action != null && action.equals(ACTION_START_QUIZ)) {
            getIntent().setAction(null);
            startQuizActivity();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        mInsectRecyclerAdapter.swapCursor(null);
    }

    private void retrieveAllInsects(Cursor data) {

        if (mInsectsList == null) {
            mInsectsList = new ArrayList<>(data.getCount());
            while (data.moveToNext()) {
                Insect insect = new Insect(data);
                mInsectsList.add(insect);
            }
        }
    }

    private static class InsectsAsyncTaskLoader extends AsyncTaskLoader<Cursor> {

        Cursor mCursorData;
        Bundle mArgs;

        InsectsAsyncTaskLoader(Context context, Bundle args) {

            super(context);
            mArgs = args;
        }

        @Override
        protected void onStartLoading() {

            if (mCursorData != null) {
                deliverResult(mCursorData);
            } else {
                //start loading in the background
                forceLoad();
            }
        }

        @Override
        public Cursor loadInBackground() {

            try {
                String sortOrder = mArgs.getString(SORT_ORDER_KEY);
                return DatabaseManager.getInstance(getContext()).queryAllInsects(sortOrder);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        public void deliverResult(Cursor data) {

            //save current cursor data
            mCursorData = data;
            super.deliverResult(data);
        }
    }
}
