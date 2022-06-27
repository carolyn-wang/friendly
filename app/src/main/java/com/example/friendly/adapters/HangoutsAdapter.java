package com.example.friendly.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.friendly.objects.Hangout;
import com.example.friendly.NavigationUtils;
import com.example.friendly.R;
import com.example.friendly.activities.MainActivity;

import java.text.SimpleDateFormat;
import java.util.List;

public class HangoutsAdapter extends RecyclerView.Adapter<HangoutsAdapter.ViewHolder> {

    private Context mContext;
    protected List<Hangout> hangouts;
    private static final String TAG = "HangoutsAdapter";

    public HangoutsAdapter(Context context, List<Hangout> hangouts) {
        this.mContext = context; // MainActiviity
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

        private View cdHangout;
        private TextView tvHangoutUser1;
        private TextView tvHangoutUser2;
        private TextView tvHangoutDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvHangoutUser1 = itemView.findViewById(R.id.tvHangoutUser1);
            tvHangoutUser2 = itemView.findViewById(R.id.tvHangoutUser2);
            tvHangoutDate = itemView.findViewById(R.id.tvHangoutDate);
//                currentUser = ParseUser.getCurrentUser();
            cdHangout = itemView.findViewById(R.id.cdHangout);
        }

        public void bind(Hangout hangout) {
            // Bind the post data to the view elements
            tvHangoutUser1.setText(hangout.getUser1().getUsername());
            if(hangout.getUser2() != null){
                tvHangoutUser2.setText(hangout.getUser2().getUsername());
            }
            String formattedDate = new SimpleDateFormat("MM/dd/yyyy, hh a").format(hangout.getDate());
            tvHangoutDate.setText(formattedDate);

            // click listener to open DetailFragment for hangout
            cdHangout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Hangout hangout = hangouts.get(position);
                        Log.i(TAG, hangout.getUser1().getUsername());
                        NavigationUtils.displayFragmentHangoutDetail(hangout, ((MainActivity)mContext).getSupportFragmentManager() );
//                        NavigationUtils.displayFragmentHangoutDetail(hangout);
                    }
                }
            });
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