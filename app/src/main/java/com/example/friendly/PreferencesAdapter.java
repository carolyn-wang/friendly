package com.example.friendly;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PreferencesAdapter extends RecyclerView.Adapter<PreferencesAdapter.ViewHolder> {

    protected Context context;
    protected List<Preference> preferences;
    private static final String TAG = "PreferencesAdapter";

    public PreferencesAdapter(Context context, List<Preference> preferences) {
        this.context = context;
        this.preferences = preferences;
    }

    /**
     * Passes LayoutInflater a “blueprint” of the view (reference to XML layout file)
     * Wraps view in a ViewHolder for easy access
     *
     * @param parent
     * @param viewType
     * @return
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Preference preference = preferences.get(position);
        holder.bind(preference);
    }

    @Override
    public int getItemCount() {
        return preferences.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        protected TextView tvQuestion;
//            protected RecyclerView rvOptions;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Log.i(TAG, "viewholder");
            tvQuestion = itemView.findViewById(R.id.tvQuestion);
//                rvOptions = itemView.findViewById(R.id.rvOptions);
//                currentUser = ParseUser.getCurrentUser();
        }

        public void bind(Preference preference) {
            // Bind the post data to the view elements
            tvQuestion.setText("hi");
//            tvQuestion.setText(preference.getQuestion());
//                PollOptionsAdapter adapter = new PollOptionsAdapter(preference.getOptions());
//                rvOptions.setAdapter(adapter);
//                adapter.notifyDataSetChanged();
        }
    }

    // Clean all elements of the recycler
    public void clear() {
        preferences.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Preference> list) {
        preferences.addAll(list);
        notifyDataSetChanged();
    }
}
