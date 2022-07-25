package com.example.friendly.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.friendly.utils.DisplayUtils;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.example.friendly.fragments.MapFragment;

import com.example.friendly.objects.Hangout;
import com.example.friendly.utils.NavigationUtils;
import com.example.friendly.R;
import com.example.friendly.activities.MainActivity;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class HangoutsAdapter extends RecyclerView.Adapter<HangoutsAdapter.ViewHolder> {

    private final Context mContext;
    protected List<Hangout> hangouts;
    private static final String TAG = "HangoutsAdapter";
    private static final String KEY_USER_PHONE = "phone";
    private static SimpleDateFormat dateTimeFormat;

    public HangoutsAdapter(Context context, List<Hangout> hangouts) {
        this.mContext = context;
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

        private final CardView cdHangout;
        private final TextView tvHangoutUser1;
        private final TextView tvHangoutUser2;
        private final TextView tvHangoutDate;
        private final TextView tvHangoutLocation;
        private final ImageButton ibMessage;
        private final ImageButton ibMap;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvHangoutUser1 = itemView.findViewById(R.id.tvHangoutUser1);
            tvHangoutUser2 = itemView.findViewById(R.id.tvHangoutUser2);
            tvHangoutDate = itemView.findViewById(R.id.tvHangoutDate);
            tvHangoutLocation = itemView.findViewById(R.id.tvHangoutLocation);
            cdHangout = itemView.findViewById(R.id.cdHangout);
            ibMessage = itemView.findViewById(R.id.ibMessage);
            ibMap = itemView.findViewById(R.id.ibMap);
            dateTimeFormat = new SimpleDateFormat(mContext.getString(R.string.dateTimeFormat), Locale.US);
        }

        public void bind(Hangout hangout) {
            if (hangout.getUser1() != null) {
                    tvHangoutUser1.setText(hangout.getUser1Name());
            }
            if (hangout.getUser2() != null) {
                tvHangoutUser2.setText(hangout.getUser2Name());
                // click listener to open DetailFragment for hangout, only if no User 2 (is quick match)
                cdHangout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = getAdapterPosition();
                        Hangout hangout = hangouts.get(position);
                        NavigationUtils.displayFragmentHangoutDetail(mContext, v, hangout, ((MainActivity) mContext).getSupportFragmentManager());
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
            String formattedDate = dateTimeFormat.format(hangout.getDate());
            tvHangoutDate.setText(formattedDate);
            if (hangout.getPlace() != null) {
                tvHangoutLocation.setText(DisplayUtils.getEmojiByPlace(mContext, hangout.getPlace()) + " " + hangout.getLocationName());
            }
            cdHangout.setCardBackgroundColor(DisplayUtils.getCardColor(mContext, hangout));

            if (hangout.getUser2() != null && hangout.getUser2().get(KEY_USER_PHONE) != null) {
                ibMessage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        NavigationUtils.openMessagesIntent(mContext, hangout.getUser2().getString(KEY_USER_PHONE));
                    }
                });
                ibMessage.setVisibility(View.VISIBLE);
            }
            else{
                ibMessage.setVisibility(View.INVISIBLE);
            }

            if (hangout.getUser2() != null && hangout.getPlace() != null) {
                ibMap.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FragmentManager fragmentManager = ((MainActivity) mContext).getSupportFragmentManager();
                        FragmentTransaction ft = fragmentManager.beginTransaction();
                        MapFragment mapFragment = MapFragment.newInstance(hangout.getUser2(), hangout.getPlace());
                        ft.replace(R.id.flContainer, mapFragment)
                                .addToBackStack(null)
                                .commit();
                    }
                });
                ibMap.setVisibility(View.VISIBLE);
            }
            else{
                ibMap.setVisibility(View.INVISIBLE);
            }
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
