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
            case 0:
                view = LayoutInflater.from(context).inflate(R.layout.item_poll_card_radio, parent, false);
                break;
            default:
                view = LayoutInflater.from(context).inflate(R.layout.item_poll_card_checkbox, parent, false);
                break;
        }
        return new ViewHolder(view);
    }

    /**
     * Returns card type to be used (either radio or checkbox)
     *
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        if (position < 2) {
            return 0;
        } else {
            return 1;
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
        private Button btnOption0;
        private Button btnOption1;
        private Button btnOption2;
        private Button btnOption3;
        private Button btnOption4;
        private Button btnOption5;
        private Button btnOption6;
        private Button[] optionViews;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvQuestion = itemView.findViewById(R.id.tvQuestion);
            btnOption0 = itemView.findViewById(R.id.btnOption0);
            btnOption1 = itemView.findViewById(R.id.btnOption1);
            btnOption2 = itemView.findViewById(R.id.btnOption2);
            btnOption3 = itemView.findViewById(R.id.btnOption3);
            btnOption4 = itemView.findViewById(R.id.btnOption4);
            btnOption5 = itemView.findViewById(R.id.btnOption5);
            btnOption6 = itemView.findViewById(R.id.btnOption6);
            optionViews = new Button[]{btnOption0, btnOption1, btnOption2, btnOption3, btnOption4, btnOption5, btnOption6};
        }

        /**
         * Set poll question.
         * Set text for Button option or set visibility to GONE
         *
         * @param preference
         */
        public void bind(Preference preference) {
            tvQuestion.setText(preference.getQuestion());
            //
            for (int i = 0; i < optionViews.length; i++) {
                if (i < preference.getOptions().length) {
                    optionViews[i].setText(preference.getOption(i));
                } else {
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
