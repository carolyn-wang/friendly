package com.example.friendly;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HangoutsAdapter extends RecyclerView.Adapter<HangoutsAdapter.ViewHolder> {

    protected Context context;
    protected List<Hangout> hangouts;
    private static final String TAG = "HangoutsAdapter";

    public HangoutsAdapter(Context context, List<Hangout> hangouts) {
        this.context = context;
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
        View view = LayoutInflater.from(context).inflate(R.layout.item_hangout_card, parent, false);
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

        protected TextView hangoutUser1;
        protected TextView hangoutUser2;
        protected TextView hangoutDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            hangoutUser1 = itemView.findViewById(R.id.hangoutUser1);
            hangoutUser2 = itemView.findViewById(R.id.hangoutUser2);
            hangoutDate = itemView.findViewById(R.id.hangoutDate);
//                currentUser = ParseUser.getCurrentUser();
        }

        public void bind(Hangout hangout) {
            // Bind the post data to the view elements
            hangoutUser1.setText(hangout.getUser1().getUsername());
            if(hangout.getUser2() != null){
                hangoutUser2.setText(hangout.getUser2().getUsername());
            }
            hangoutDate.setText(hangout.getDate().toString());
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
