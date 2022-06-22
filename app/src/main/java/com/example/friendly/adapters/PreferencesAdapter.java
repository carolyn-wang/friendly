package com.example.friendly.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.friendly.objects.Preference;
import com.example.friendly.R;

import java.util.ArrayList;
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
        View view = LayoutInflater.from(context).inflate(R.layout.item_poll_card, parent, false);
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

        private TextView tvQuestion;
        private CheckBox cbOption0;
        private CheckBox cbOption1;
        private CheckBox cbOption2;
        private CheckBox cbOption3;
        private CheckBox cbOption4;
        private CheckBox cbOption5;
        private CheckBox cbOption6;
        private CheckBox[] optionViews;
//            protected RecyclerView rvOptions;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Log.i(TAG, "viewholder");
            tvQuestion = itemView.findViewById(R.id.tvQuestion);
            cbOption0 = itemView.findViewById(R.id.cbOption0);
            cbOption1 = itemView.findViewById(R.id.cbOption1);
            cbOption2 = itemView.findViewById(R.id.cbOption2);
            cbOption3 = itemView.findViewById(R.id.cbOption3);
            cbOption4 = itemView.findViewById(R.id.cbOption4);
            cbOption5 = itemView.findViewById(R.id.cbOption5);
            cbOption6 = itemView.findViewById(R.id.cbOption6);
            optionViews = new CheckBox[]{cbOption0, cbOption1, cbOption2, cbOption3, cbOption4, cbOption5, cbOption6};
//            rvOptions = itemView.findViewById(R.id.rvOptions);
//                currentUser = ParseUser.getCurrentUser();
        }

        /**
         * Set poll question.
         *  Set text for checkbox option or set visibility to GONE
         * @param preference
         */
        public void bind(Preference preference) {
            tvQuestion.setText(preference.getQuestion());
            //
            for (int i=0; i < optionViews.length; i++){
                if (i < preference.getOptions().length){
                    optionViews[i].setText(preference.getOption(i));
                }
                else{
                    optionViews[i].setVisibility(View.GONE);
                }
            }
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
