package com.example.friendly.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.friendly.NavigationUtils;
import com.example.friendly.R;
import com.example.friendly.activities.MainActivity;
import com.example.friendly.objects.Hangout;
import com.parse.ParseUser;

import java.util.List;

@Deprecated
public class ProfileAdapter {
    private static final String TAG = "ProfileAdapter";
    private Context mContext;
    private String username;
    private String firstName;
    

    public ProfileAdapter(Context context, ParseUser user) {
        this.mContext = context; // MainActiviity
        this.username = user.getUsername();
        this.firstName = user.getString("firstName");
    }

    class ViewHolder{

        protected TextView tvUsername;
        protected TextView tvFirstName;

        public ViewHolder(@NonNull View itemView) {
            tvUsername = itemView.findViewById(R.id.tvUsername);
            tvFirstName = itemView.findViewById(R.id.tvNameLabel);
        }

        public void bind(Hangout hangout) {
            // Bind the post data to the view elements
            tvUsername.setText(username);
            tvFirstName.setText(firstName);
        }
    }
}
