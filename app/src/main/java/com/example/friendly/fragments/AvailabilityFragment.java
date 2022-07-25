package com.example.friendly.fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.friendly.utils.NavigationUtils;
import com.example.friendly.R;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AvailabilityFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AvailabilityFragment extends Fragment {

    private static final String TAG = "AvailabilityFragment";
    private static Context mContext;
    private Button btnM;
    private Button btnTu;
    private Button btnW;
    private Button btnTh;
    private Button btnF;
    private Button btnSa;
    private Button btnSu;
    private int pagerIndex = 0;
    private static final int NUM_PAGES = 7;
    private ViewPager2 mPager;
    private ScreenSlidePagerAdapter pagerAdapter;
    private Button saveButton;
    private static final String KEY_AVAILABILITY_PREFERENCE = "availabilityPreference";

    private final static int activeButtonColor = R.color.blue;
    private final static int inactiveButtonColor = Color.WHITE;


    public AvailabilityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_availability, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mContext = getContext();
        btnM = view.findViewById(R.id.btnM);
        btnTu = view.findViewById(R.id.btnTu);
        btnW = view.findViewById(R.id.btnW);
        btnTh = view.findViewById(R.id.btnTh);
        btnF = view.findViewById(R.id.btnF);
        btnSa = view.findViewById(R.id.btnSa);
        btnSu = view.findViewById(R.id.btnSu);
        saveButton = view.findViewById(R.id.saveButton);

        mPager = (ViewPager2) view.findViewById(R.id.pagerAvailability);
        mPager.setPageTransformer(new ZoomOutPageTransformer());

        pagerAdapter = new ScreenSlidePagerAdapter(getParentFragmentManager());
        mPager.setAdapter(pagerAdapter);

        List<Button> buttons = Arrays.asList(btnM, btnTu, btnW, btnTh, btnF, btnSa, btnSu);

        buttons.get(pagerIndex).setBackgroundColor(getResources().getColor(activeButtonColor));
        mPager.setCurrentItem(pagerIndex);

        for (Button btn : buttons) {
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    buttons.get(pagerIndex).setBackgroundColor(inactiveButtonColor);
                    btn.setBackgroundColor(getResources().getColor(activeButtonColor));
                    updateAvailabilityPreferences(pagerIndex, v); // save changes from previous page
                    mPager.setCurrentItem(buttons.indexOf(btn));
                    pagerIndex = buttons.indexOf(btn);
                }
            });
        }

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateAvailabilityPreferences(pagerIndex, v);
                NavigationUtils.displayFragmentProfile(getParentFragmentManager());
            }
        });
    }

    /**
     * Updates availability preferences Boolean List and saves new list to database.
     * Called upon switching fragment in Pageviewer.
     * @param pageIndex Index of page to update
     */
    private void updateAvailabilityPreferences(int pageIndex, View textview) {
        List<Boolean> userAvailabilityPreference = ParseUser.getCurrentUser().getList(KEY_AVAILABILITY_PREFERENCE);
        int availability_options_len = getResources().getStringArray(R.array.time_options_array).length;
        ListView listView = (ListView)(this.getChildFragmentManager().findFragmentByTag("f" + mPager.getCurrentItem()).getView());
        for (int row = 0; row < availability_options_len; row++) {
                if (listView != null) {
                    TextView textView = (TextView) (listView).getChildAt(row);
                    if (textView != null) {
                        userAvailabilityPreference.set((pageIndex * availability_options_len ) + row, textView.isActivated());
                    }
                }
        }
        ParseUser.getCurrentUser().put(KEY_AVAILABILITY_PREFERENCE, userAvailabilityPreference);
        ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
            }
        });
    }


    /**
     * A simple pager adapter that represents 7 ScreenSlidePageFragment objects, in
     * sequence.
     */
    private class ScreenSlidePagerAdapter extends FragmentStateAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(AvailabilityFragment.this);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return new AvailabilityList(getContext(), position);
        }

        @Override
        public int getItemCount() {
            return NUM_PAGES;
        }
    }


    private class ZoomOutPageTransformer implements ViewPager2.PageTransformer {
        private static final float MIN_SCALE = 1.0f;
        private static final float MIN_ALPHA = 0.6f;

        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();
            int pageHeight = view.getHeight();

            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.setAlpha(0f);

            } else if (position <= 1) { // [-1,1]
                // Modify the default slide transition to shrink the page as well
                float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
                float vertMargin = pageHeight * (1 - scaleFactor) / 2;
                float horzMargin = pageWidth * (1 - scaleFactor) / 2;
                if (position < 0) {
                    view.setTranslationX(horzMargin - vertMargin / 2);
                } else {
                    view.setTranslationX(-horzMargin + vertMargin / 2);
                }

                // Scale the page down (between MIN_SCALE and 1)
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);

                // Fade the page relative to its size.
                view.setAlpha(MIN_ALPHA +
                        (scaleFactor - MIN_SCALE) /
                                (1 - MIN_SCALE) * (1 - MIN_ALPHA));

            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                view.setAlpha(0f);
            }
        }
    }


}