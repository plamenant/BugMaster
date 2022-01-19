package com.google.developer.bugmaster.data;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.developer.bugmaster.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static com.google.developer.bugmaster.data.InsectContract.InsectEntry;

/**
 * Database helper class to facilitate creating and updating
 * the database from the chosen schema.
 */
public class BugsDbHelper extends SQLiteOpenHelper {

    private static final String TAG = BugsDbHelper.class.getSimpleName();

    private static final String DATABASE_NAME = "insects.db";
    private static final int DATABASE_VERSION = 4;

    private static final String SQL_CREATE_INSECT_TABLE = "CREATE TABLE " +
            InsectEntry.TABLE_NAME + " (" +
            InsectEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            InsectEntry.COLUMN_FRIENDLY_NAME + " TEXT NOT NULL, " +
            InsectEntry.COLUMN_SCIENTIFIC_NAME + " TEXT NOT NULL, " +
            InsectEntry.COLUMN_CLASSIFICATION + " TEXT NOT NULL, " +
            InsectEntry.COLUMN_IMAGE_ASSET + " TEXT NOT NULL, " +
            InsectEntry.COLUMN_DANGER_LEVEL + " INTEGER NOT NULL);";

    private static final String SQL_DROP_TABLE = "DROP TABLE IF EXISTS " + InsectEntry.TABLE_NAME;

    private static final String JSON_PROPERTY_INSECTS = "insects";
    private static final String JSON_PROPERTY_FRIENDLY_NAME = "friendlyName";
    private static final String JSON_PROPERTY_SCIENTIFIC_NAME = "scientificName";
    private static final String JSON_PROPERTY_CLASSIFICATION = "classification";
    private static final String JSON_PROPERTY_IMAGE_ASSET = "imageAsset";
    private static final String JSON_PROPERTY_DANGER_LEVEL = "dangerLevel";

    //Used to read data from res/ and assets/
    private Resources mResources;

    public BugsDbHelper(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        mResources = context.getResources();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        try {
            db.execSQL(SQL_CREATE_INSECT_TABLE);
            db.beginTransaction();
            db.delete(InsectEntry.TABLE_NAME, null, null);
            readInsectsFromResources(db);
            db.setTransactionSuccessful();
            Log.d(TAG, "Database created and filled with data.");
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL(SQL_DROP_TABLE);
        onCreate(db);
        Log.d(TAG, "Database upgraded from version = " + oldVersion + " to version = " + newVersion);
    }

    /**
     * Streams the JSON data from insect.json, parses it, and inserts it into the
     * provided {@link SQLiteDatabase}.
     *
     * @param db Database where objects should be inserted.
     * @throws IOException
     * @throws JSONException
     */
    private void readInsectsFromResources(SQLiteDatabase db) throws IOException, JSONException {

        StringBuilder builder = new StringBuilder();
        InputStream in = mResources.openRawResource(R.raw.insects);
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));

        String line;
        while ((line = reader.readLine()) != null) {
            builder.append(line);
        }

        //Parse resource into key/values
        final String rawJson = builder.toString();
        JSONObject jsonObject = new JSONObject(rawJson);
        JSONArray jsonArray = jsonObject.getJSONArray(JSON_PROPERTY_INSECTS);

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject insectObject = jsonArray.getJSONObject(i);
            ContentValues insectValues = new ContentValues();

            insectValues.put(InsectEntry.COLUMN_FRIENDLY_NAME, insectObject.getString(JSON_PROPERTY_FRIENDLY_NAME));
            insectValues.put(InsectEntry.COLUMN_SCIENTIFIC_NAME, insectObject.getString(JSON_PROPERTY_SCIENTIFIC_NAME));
            insectValues.put(InsectEntry.COLUMN_CLASSIFICATION, insectObject.getString(JSON_PROPERTY_CLASSIFICATION));
            insectValues.put(InsectEntry.COLUMN_IMAGE_ASSET, insectObject.getString(JSON_PROPERTY_IMAGE_ASSET));
            insectValues.put(InsectEntry.COLUMN_DANGER_LEVEL, insectObject.getString(JSON_PROPERTY_DANGER_LEVEL));

            db.insert(InsectEntry.TABLE_NAME, null, insectValues);
        }
    }
}
