package com.example.friendly.fragments.match;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.friendly.NavigationUtils;
import com.example.friendly.R;
import com.parse.ParseUser;

public class QuickMatchFragment extends Fragment {
    private static final String TAG = "QuickMatchFragment";
    private Button btnQuickHangout;
    private Button btnLongHangout;
    private Activity mActivity;
    public QuickMatchFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_quick_match, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mActivity = getActivity();
    }

}
