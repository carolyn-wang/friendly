package com.example.friendly.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.friendly.activities.PreferencesActivity;
import com.example.friendly.objects.Preference;
import com.example.friendly.R;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;

public class PreferencesAdapter extends RecyclerView.Adapter<PreferencesAdapter.ViewHolder> {
    private Context mContext;
    private List<Preference> preferences;
    private static final String TAG = "PreferencesAdapter";

    public PreferencesAdapter(Context context, List<Preference> preferences) {
        this.mContext = context;
        this.preferences = preferences;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == 0) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_poll_card_radio, parent, false);
        } else {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_poll_card_checkbox, parent, false);
        }
        return new ViewHolder(view);
    }

    /**
     * Returns card type to be used
     *
     * @param position - current adapter position
     * @return int to indicate card type: radio (0) or checkbox (1)
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
        private RadioGroup rgOptions;
        private LinearLayout lvOptions;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvQuestion = itemView.findViewById(R.id.tvQuestion);
            rgOptions = itemView.findViewById(R.id.rgOptions);
            lvOptions = itemView.findViewById(R.id.lvOptions);
        }

        /**
         * Set poll question.
         * Set text for Button option or set visibility to GONE
         *
         * @param preference - current Preference object with question and options data to bind to card
         */
        public void bind(Preference preference) {
            tvQuestion.setText(preference.getQuestion());

            int cardPosition = getAdapterPosition();
            List<String> allPreferenceKeys = ((PreferencesActivity) mContext).getAllPreferenceKeys();

            if (getItemViewType() == 0) {
                int preferenceIndex = ParseUser.getCurrentUser().getInt(allPreferenceKeys.get(cardPosition));

                // Dynamically set preference options and options text
                for (int i = 0; i < preference.getOptions().length; i++) {
                    RadioButton btnOption = new RadioButton(mContext);
                    btnOption.setText(preference.getOption(i));
                    rgOptions.addView(btnOption);
                }

                // Set previous preferences as checked if not null
                if (preferenceIndex != -1) {
                    RadioButton option = (RadioButton) rgOptions.getChildAt(preferenceIndex);
                    option.setChecked(true);
                }

                // On Click Listener
                rgOptions.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        // checkedId is the RadioButton selected
                        int checkedRadioPosition = getAdapterPosition();
                        if (checkedRadioPosition != RecyclerView.NO_POSITION) {
                            Preference clickedPreference = preferences.get(checkedRadioPosition);
                            String preferenceKey = clickedPreference.getParseKey();
                            saveRadioPreference(preferenceKey, group.indexOfChild(itemView.findViewById(checkedId)));
                        }
                    }
                });
            } else {
                String preferenceKey = preference.getParseKey();
                JSONArray previousPreferences = ParseUser.getCurrentUser().getJSONArray(preferenceKey);

                for (int i = 0; i < preference.getOptions().length; i++) {
                    CheckBox btnOption = new CheckBox(mContext);
                    btnOption.setText(preference.getOption(i));
                    lvOptions.addView(btnOption);
                    if (previousPreferences != null) {
                        try {
                            btnOption.setChecked(previousPreferences.getBoolean(i));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }


                    btnOption.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            int buttonIndex = lvOptions.indexOfChild(buttonView) - 1;
                            saveCheckboxPreference(preferenceKey, buttonIndex, isChecked, previousPreferences);
                        }
                    });
                }
            }
        }
    }


    private static void saveRadioPreference(String preferenceKey, int index) {
        ParseUser.getCurrentUser().put(preferenceKey, index);
        ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                Log.d(TAG, "saved radio preference at index " + index);
            }
        });
    }

    private static void saveCheckboxPreference(String preferenceKey, int index, boolean bool, JSONArray previousPreferences) {
        try {
            previousPreferences = previousPreferences.put(index, bool);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ParseUser.getCurrentUser().put(preferenceKey, previousPreferences);
        ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                Log.d(TAG, "saved checkbox preference at index " + index);
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
