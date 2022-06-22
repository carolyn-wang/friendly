package com.example.friendly.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.recyclerview.widget.RecyclerView;

import com.example.friendly.R;

public class PollOptionsAdapter extends RecyclerView.Adapter<PollOptionsAdapter.ViewHolder> {
    private String[] options;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final CheckBox cbPollOption;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View
            cbPollOption = (CheckBox) view.findViewById(R.id.cbPollOption);
        }

        public CheckBox getCheckBox() {
            return cbPollOption;
        }
    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used
     *                by RecyclerView.
     */
    public PollOptionsAdapter(String[] dataSet) {
        options = dataSet;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_poll_row, viewGroup, false);
        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        viewHolder.getCheckBox().setText(options[position]);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return options.length;
    }
}

