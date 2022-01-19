package com.google.developer.bugmaster.data;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.developer.bugmaster.InsectDetailsActivity;
import com.google.developer.bugmaster.R;
import com.google.developer.bugmaster.views.DangerLevelView;

/**
 * RecyclerView adapter extended with project-specific required methods.
 */

public class InsectRecyclerAdapter extends
        RecyclerView.Adapter<InsectRecyclerAdapter.InsectHolder> {

    /* ViewHolder for each insect item */
    public class InsectHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final TextView friendlyNameTv;
        final TextView scientificNameTv;
        final DangerLevelView dangerLevelView;

        private Insect mInsect;

        InsectHolder(View itemView) {

            super(itemView);

            friendlyNameTv = (TextView) itemView.findViewById(R.id.insect_list_row_friendly_name);
            scientificNameTv = (TextView) itemView.findViewById(R.id.insect_list_row_scientific_name);
            dangerLevelView = (DangerLevelView) itemView.findViewById(R.id.insect_list_rew_danger_level);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            Intent intent = new Intent(mContext, InsectDetailsActivity.class);
            intent.putExtra(InsectDetailsActivity.EXTRA_INSECT, mInsect);
            mContext.startActivity(intent);
        }

        void update(Insect insect) {

            mInsect = insect;
            friendlyNameTv.setText(insect.name);
            scientificNameTv.setText(insect.scientificName);
            dangerLevelView.setDangerLevel(insect.dangerLevel);
        }
    }

    private Context mContext;
    private Cursor mCursor;

    public InsectRecyclerAdapter(Context context) {

        mContext = context;
    }

    @Override
    public InsectHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(mContext);
        View insectView = inflater.inflate(R.layout.insect_list_row, parent, false);
        return new InsectHolder(insectView);
    }

    @Override
    public void onBindViewHolder(InsectHolder holder, int position) {

        try {
            Insect item = getItem(position);
            if (item != null) {
                holder.update(item);
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {

        return mCursor == null ? 0 : mCursor.getCount();
    }

    /**
     * Return the {@link Insect} represented by this item in the adapter.
     *
     * @param position Adapter item position.
     * @return A new {@link Insect} filled with this position's attributes
     * @throws IllegalArgumentException if position is out of the adapter's bounds.
     */
    public Insect getItem(int position) {

        if (position < 0 || position >= getItemCount()) {
            throw new IllegalArgumentException("Item position is out of adapter's range");
        } else if (mCursor.moveToPosition(position)) {
            return new Insect(mCursor);
        }
        return null;
    }

    public void swapCursor(Cursor cursor) {

        mCursor = cursor;
        notifyDataSetChanged();
    }
}
