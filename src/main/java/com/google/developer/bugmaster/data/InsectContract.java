package com.google.developer.bugmaster.data;

import android.provider.BaseColumns;

/**
 * Created by stoyan.yanev on 6.1.2018 г..
 */

public class InsectContract {

    private InsectContract() {

    }

    public static final class InsectEntry implements BaseColumns {

        public static final String TABLE_NAME = "insects";

        public static final String COLUMN_FRIENDLY_NAME = "friendly_name";
        public static final String COLUMN_SCIENTIFIC_NAME = "scientific_name";
        public static final String COLUMN_CLASSIFICATION = "classification";
        public static final String COLUMN_IMAGE_ASSET = "image_asset";
        public static final String COLUMN_DANGER_LEVEL = "danger_level";
    }
}
