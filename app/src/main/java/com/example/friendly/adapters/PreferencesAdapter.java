package com.example.friendly.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.Group;
import androidx.recyclerview.widget.RecyclerView;

import com.example.friendly.activities.PreferencesActivity;
import com.example.friendly.objects.Preference;
import com.example.friendly.R;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.Arrays;
import java.util.List;

public class PreferencesAdapter extends RecyclerView.Adapter<PreferencesAdapter.ViewHolder> {
    protected Context mContext;
    protected List<Preference> preferences;
    private static final String TAG = "PreferencesAdapter";

    private static final String KEY_PREFERENCE0 = "yearPreference";
    private static final String KEY_PREFERENCE1 = "similarityPreference";
    private static final String KEY_PREFERENCE2 = "activitiesPreference";

    public PreferencesAdapter(Context context, List<Preference> preferences) {
        this.mContext = context;
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
                view = LayoutInflater.from(mContext).inflate(R.layout.item_poll_card_radio, parent, false);
                break;
            default:
                view = LayoutInflater.from(mContext).inflate(R.layout.item_poll_card_checkbox, parent, false);
                break;
        }
        return new ViewHolder(view);
    }

    /**
     * Returns card type to be used (either radio (0) or checkbox (1))
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
        private View[] optionViews;
        RadioGroup rgOptions;
        LinearLayout lvOptions;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvQuestion = itemView.findViewById(R.id.tvQuestion);
//            btnOption0 = itemView.findViewById(R.id.btnOption0);
//            btnOption1 = itemView.findViewById(R.id.btnOption1);
//            btnOption2 = itemView.findViewById(R.id.btnOption2);
//            btnOption3 = itemView.findViewById(R.id.btnOption3);
//            btnOption4 = itemView.findViewById(R.id.btnOption4);
//            btnOption5 = itemView.findViewById(R.id.btnOption5);
//            btnOption6 = itemView.findViewById(R.id.btnOption6);
            rgOptions = (RadioGroup) itemView.findViewById(R.id.rgOptions);
            lvOptions = (LinearLayout) itemView.findViewById(R.id.lvOptions);
//            optionViews = new View[]{btnOption0, btnOption1, btnOption2, btnOption3, btnOption4, btnOption5, btnOption6};
        }

        /**
         * Set poll question.
         * Set text for Button option or set visibility to GONE
         *
         * @param preference
         */
        public void bind(Preference preference) {
            tvQuestion.setText(preference.getQuestion());

            // show previous preferences if not null
            int position = getAdapterPosition();
            List<String> allPreferenceKeys = ((PreferencesActivity) mContext).getAllPreferenceKeys();
            int preferenceIndex = ParseUser.getCurrentUser().getInt(allPreferenceKeys.get(position));

            if (getItemViewType() == 0) {
                // Dynamically set preference option texts
                for (int i = 0; i < preference.getOptions().length; i++) {
                    RadioButton btnOption = new RadioButton(mContext);
                    btnOption.setText(preference.getOption(i));
                    rgOptions.addView(btnOption);
                }

                // Set previous preferences as checked
                RadioButton option = (RadioButton) rgOptions.getChildAt(preferenceIndex);
                option.setChecked(true);

                // On Click Listener
                rgOptions.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        // checkedId is the RadioButton selected
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            Preference clickedPreference = preferences.get(position);
                            String preferenceKey = clickedPreference.getParseKey();
                            RadioButton rb = (RadioButton) itemView.findViewById(checkedId);
                            savePreference(ParseUser.getCurrentUser(), preferenceKey, Arrays.asList(preference.getOptions()).indexOf(rb.getText()));
                        }

                    }
                });
            } else {
                for (int i = 0; i < preference.getOptions().length; i++) {
                    CheckBox btnOption = new CheckBox(mContext);
                    btnOption.setText(preference.getOption(i));
                    lvOptions.addView(btnOption);
                }

            }

        }
    }

    private static void savePreference(ParseUser currentUser, String preferenceKey, int index) {
        currentUser.put(preferenceKey, index);
        currentUser.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                Log.i(TAG, "saved preference");
            }
        });
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
