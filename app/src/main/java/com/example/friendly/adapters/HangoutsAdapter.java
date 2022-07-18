package com.example.friendly.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.friendly.DisplayUtils;
import com.example.friendly.HangoutQuery;
import com.example.friendly.objects.Hangout;
import com.example.friendly.NavigationUtils;
import com.example.friendly.R;
import com.example.friendly.activities.MainActivity;
import com.google.android.material.timepicker.TimeFormat;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Random;

public class HangoutsAdapter extends RecyclerView.Adapter<HangoutsAdapter.ViewHolder> {

    private Context mContext;
    protected List<Hangout> hangouts;
    private static final String TAG = "HangoutsAdapter";

    public HangoutsAdapter(Context context, List<Hangout> hangouts) {
        this.mContext = context; // MainActivity
        this.hangouts = hangouts;
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
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_hangout_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Hangout hangout = hangouts.get(position);
        holder.bind(hangout);
    }

    @Override
    public int getItemCount() {
        return hangouts.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private CardView cdHangout;
        private TextView tvHangoutUser1;
        private TextView tvHangoutUser2;
        private TextView tvHangoutDate;
        private TextView tvHangoutLocation;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvHangoutUser1 = itemView.findViewById(R.id.tvHangoutUser1);
            tvHangoutUser2 = itemView.findViewById(R.id.tvHangoutUser2);
            tvHangoutDate = itemView.findViewById(R.id.tvHangoutDate);
            tvHangoutLocation = itemView.findViewById(R.id.tvHangoutLocation);
            cdHangout = itemView.findViewById(R.id.cdHangout);
        }

        public void bind(Hangout hangout) {
            tvHangoutUser1.setText(hangout.getUser1().getUsername());
            if (hangout.getUser2() != null) {
                tvHangoutUser2.setText(hangout.getUser2().getUsername());
                // click listener to open DetailFragment for hangout, only if no User 2 (is quick match)
                cdHangout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = getAdapterPosition();
                        Hangout hangout = hangouts.get(position);
                        NavigationUtils.displayFragmentHangoutDetail(hangout, ((MainActivity) mContext).getSupportFragmentManager());

                    }
                });
            } else { // click listener for if RV is showing upcoming quick hangouts
                cdHangout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            Hangout hangout = hangouts.get(position);
                            NavigationUtils.displayFragmentQuickMatchDetail(mContext, v, hangout, ((MainActivity) mContext).getSupportFragmentManager());
                        }
                    }
                });
                tvHangoutUser2.setText("");
            }
            String formattedDate = SimpleDateFormat.getDateTimeInstance().format(hangout.getDate());
            tvHangoutDate.setText(formattedDate);
            if (hangout.getLocation() != null) {
                tvHangoutLocation.setText(DisplayUtils.getEmojiByPlace(mContext, hangout.getLocation()) + " " + hangout.getLocationName());
            }else{
                tvHangoutLocation.setText("");
            }
            // TODO: move into child classes
            cdHangout.setCardBackgroundColor(DisplayUtils.getCardColor(mContext, hangout));
        }
    }

    // Clean all elements of the recycler
    public void clear() {
        hangouts.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Hangout> list) {
        hangouts.addAll(list);
        notifyDataSetChanged();
    }
}
