package com.google.developer.bugmaster.data;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import static com.google.developer.bugmaster.data.InsectContract.InsectEntry;

public final class Insect implements Parcelable {

    private static final String TAG = Insect.class.getSimpleName();

    //Common name
    public final String name;
    //Latin scientific name
    public final String scientificName;
    //Classification order
    public final String classification;
    //Path to image resource
    public final String imageAsset;
    //1-10 scale danger to humans
    public final int dangerLevel;

    /**
     * Create a new Insect from discrete values
     */
    public Insect(String name, String scientificName, String classification, String imageAsset, int dangerLevel) {

        this.name = name;
        this.scientificName = scientificName;
        this.classification = classification;
        this.imageAsset = imageAsset;
        this.dangerLevel = dangerLevel;
    }

    /**
     * Create a new Insect from a database Cursor
     */
    public Insect(Cursor cursor) {

        this.name = cursor.getString(cursor.getColumnIndex(InsectEntry.COLUMN_FRIENDLY_NAME));
        this.scientificName = cursor.getString(cursor.getColumnIndex(InsectEntry.COLUMN_SCIENTIFIC_NAME));
        this.classification = cursor.getString(cursor.getColumnIndex(InsectEntry.COLUMN_CLASSIFICATION));
        this.imageAsset = cursor.getString(cursor.getColumnIndex(InsectEntry.COLUMN_IMAGE_ASSET));
        this.dangerLevel = cursor.getInt(cursor.getColumnIndex(InsectEntry.COLUMN_DANGER_LEVEL));
    }

    /**
     * Create a new Insect from a data Parcel
     */
    protected Insect(Parcel in) {

        this.name = in.readString();
        this.scientificName = in.readString();
        this.classification = in.readString();
        this.imageAsset = in.readString();
        this.dangerLevel = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(name);
        dest.writeString(scientificName);
        dest.writeString(classification);
        dest.writeString(imageAsset);
        dest.writeInt(dangerLevel);
    }

    @Override
    public int describeContents() {

        return 0;
    }

    public static final Creator<Insect> CREATOR = new Creator<Insect>() {

        @Override
        public Insect createFromParcel(Parcel in) {

            return new Insect(in);
        }

        @Override
        public Insect[] newArray(int size) {

            return new Insect[size];
        }
    };
}
