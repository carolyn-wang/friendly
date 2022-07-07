package com.example.friendly.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.friendly.objects.Preference;
import com.example.friendly.R;

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
        View view;
        switch (viewType) {
            case 0: view = LayoutInflater.from(context).inflate(R.layout.item_poll_card_radio, parent, false);
                break;
            case 2: view = LayoutInflater.from(context).inflate(R.layout.item_poll_card_checkbox, parent, false);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + viewType);
        }
        return new ViewHolder(view);
    }
    @Override
    public int getItemViewType(int position) {
        // Just as an example, return 0 or 2 depending on position
        // Note that unlike in ListView adapters, types don't have to be contiguous
        if (position < 2){
            return 0;
        }else{
            return 2;
        }
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
        private Button cbOption0;
        private Button cbOption1;
        private Button cbOption2;
        private Button cbOption3;
        private Button cbOption4;
        private Button cbOption5;
        private Button cbOption6;
        private Button[] optionViews;
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
            optionViews = new Button[]{cbOption0, cbOption1, cbOption2, cbOption3, cbOption4, cbOption5, cbOption6};
//            rvOptions = itemView.findViewById(R.id.rvOptions);
//                currentUser = ParseUser.getCurrentUser();
        }

        /**
         * Set poll question.
         *  Set text for Button option or set visibility to GONE
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
