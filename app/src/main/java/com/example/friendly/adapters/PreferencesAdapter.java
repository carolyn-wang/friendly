package com.example.friendly.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.friendly.activities.PreferencesActivity;
import com.example.friendly.objects.Preference;
import com.example.friendly.R;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PreferencesAdapter extends RecyclerView.Adapter<PreferencesAdapter.ViewHolder> {
    protected Context mContext;
    protected List<Preference> preferences;
    private static final String TAG = "PreferencesAdapter";
    private static final ParseUser currentUser = ParseUser.getCurrentUser();

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
        RadioGroup rgOptions;
        LinearLayout lvOptions;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvQuestion = itemView.findViewById(R.id.tvQuestion);
            rgOptions = (RadioGroup) itemView.findViewById(R.id.rgOptions);
            lvOptions = (LinearLayout) itemView.findViewById(R.id.lvOptions);
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
            int cardPosition = getAdapterPosition();
            List<String> allPreferenceKeys = ((PreferencesActivity) mContext).getAllPreferenceKeys();
            int preferenceIndex = currentUser.getInt(allPreferenceKeys.get(cardPosition));

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
                JSONArray previousPreferences = currentUser.getJSONArray(preferenceKey);

                for (int i = 0; i < preference.getOptions().length; i++) {
                    CheckBox btnOption = new CheckBox(mContext);
                    btnOption.setText(preference.getOption(i));
                    lvOptions.addView(btnOption);
                    try {
                        btnOption.setChecked(previousPreferences.getBoolean(i));
                    } catch (JSONException e) {
                        e.printStackTrace();
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
        currentUser.put(preferenceKey, index);
        currentUser.saveInBackground(new SaveCallback() {
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
        currentUser.put(preferenceKey, previousPreferences);
        currentUser.saveInBackground(new SaveCallback() {
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
