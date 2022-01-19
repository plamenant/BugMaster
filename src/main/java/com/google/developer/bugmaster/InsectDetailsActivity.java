package com.google.developer.bugmaster;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatRatingBar;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.developer.bugmaster.data.Insect;

import java.io.IOException;
import java.io.InputStream;

public class InsectDetailsActivity extends AppCompatActivity {

    public static final String EXTRA_INSECT = "extra_insect";

    private Insect mInsect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insect_details);

        Intent intent = getIntent();
        if (intent.getExtras() != null && intent.hasExtra(EXTRA_INSECT)) {
            mInsect = intent.getParcelableExtra(EXTRA_INSECT);
        } else {
            finish();
        }

        ImageView insectIv = (ImageView) findViewById(R.id.activity_insect_details_image);
        TextView commonNameTv = (TextView) findViewById(R.id.activity_insect_details_common_name);
        TextView scientificNameTv = (TextView) findViewById(R.id.activity_insect_details_scientific_name);
        TextView classificationTv = (TextView) findViewById(R.id.activity_insect_details_classification);
        TextView dangerLevelTv = (TextView) findViewById(R.id.activity_insect_details_danger_level);
        TextView dangerLevelDescriptionTv = (TextView) findViewById(R.id.activity_insect_details_danger_level_description);
        AppCompatRatingBar insectRatingBar = (AppCompatRatingBar) findViewById(R.id.activity_insect_details_rating_bar);

        setInsectImage(insectIv);
        commonNameTv.setText(mInsect.name);
        scientificNameTv.setText(mInsect.scientificName);
        classificationTv.setText(String.format(getString(R.string.classification), mInsect.classification));
        dangerLevelTv.setText(R.string.danger_level);
        dangerLevelDescriptionTv.setText(R.string.danger_level_description);
        insectRatingBar.setRating(mInsect.dangerLevel);
    }

    private void setInsectImage(ImageView insectIv) {

        Drawable insectDrawable = getInsectDrawable();
        if (insectDrawable != null) {
            insectIv.setImageDrawable(insectDrawable);
            insectIv.setMinimumHeight(getResources().getDimensionPixelSize(R.dimen.activity_insect_details_image_min_height));
        }
    }

    @Nullable
    private Drawable getInsectDrawable() {

        InputStream stream = null;
        Drawable insectDrawable = null;
        try {
            stream = getAssets().open(mInsect.imageAsset);
            insectDrawable = Drawable.createFromStream(stream, null);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return insectDrawable;
    }
}
