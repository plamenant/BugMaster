package com.google.developer.bugmaster.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import static com.google.developer.bugmaster.data.InsectContract.InsectEntry;

/**
 * Singleton that controls access to the SQLiteDatabase instance
 * for this application.
 */
public class DatabaseManager {

    public static final String ASC_ORDER_BY_NAME = InsectEntry.COLUMN_FRIENDLY_NAME + " ASC";
    public static final String DESC_ORDER_BY_DANGER_LVL = InsectEntry.COLUMN_DANGER_LEVEL + " DESC";

    private static final String SQL_QUERY_BY_ID = "SELECT * FROM " +
            InsectEntry.TABLE_NAME + " WHERE " +
            InsectEntry._ID + " = ?";

    private static DatabaseManager sInstance;

    public static synchronized DatabaseManager getInstance(Context context) {

        if (sInstance == null) {
            sInstance = new DatabaseManager(context.getApplicationContext());
        }

        return sInstance;
    }

    private BugsDbHelper mBugsDbHelper;

    private DatabaseManager(Context context) {

        mBugsDbHelper = new BugsDbHelper(context);
    }

    /**
     * Return a {@link Cursor} that contains every insect in the database.
     *
     * @param sortOrder Optional sort order string for the query, can be null
     * @return {@link Cursor} containing all insect results.
     */
    public Cursor queryAllInsects(String sortOrder) {

        SQLiteDatabase readableDatabase = mBugsDbHelper.getReadableDatabase();
        return readableDatabase.query(InsectEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                sortOrder);
    }

    /**
     * Return a {@link Cursor} that contains a single insect for the given unique id.
     *
     * @param id Unique identifier for the insect record.
     * @return {@link Cursor} containing the insect result.
     */
    public Cursor queryInsectsById(int id) {

        SQLiteDatabase readableDatabase = mBugsDbHelper.getReadableDatabase();
        String[] args = new String[]{String.valueOf(id)};
        return readableDatabase.rawQuery(SQL_QUERY_BY_ID, args);
    }
}
