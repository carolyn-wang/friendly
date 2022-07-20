package com.example.friendly.fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.friendly.NavigationUtils;
import com.example.friendly.R;
import com.example.friendly.activities.MainActivity;

import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AvailabilityFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AvailabilityFragment extends Fragment {

    private static Context mContext;
    private Button btnM;
    private Button btnTu;
    private Button btnW;
    private Button btnTh;
    private Button btnF;
    private Button btnSa;
    private Button btnSu;
    private int pagerIndex = 0;
    private int nextIndex = 1;
    private static final int NUM_PAGES = 7;
    private ViewPager mPager;
    private PagerAdapter pagerAdapter;
    private Button saveButton;

    private final static int activeButtonColor = Color.BLACK;
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

        mPager = (ViewPager) view.findViewById(R.id.pagerAvailability);
        mPager.setPageTransformer(true, new ZoomOutPageTransformer());

        pagerAdapter = new ScreenSlidePagerAdapter(getParentFragmentManager());
        mPager.setAdapter(pagerAdapter);

        List<Button> buttons = Arrays.asList(btnM, btnTu, btnW, btnTh, btnF, btnSa, btnSu);

        buttons.get(pagerIndex).setBackgroundColor(activeButtonColor);
        mPager.setCurrentItem(pagerIndex);

        for (Button btn : buttons) {
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    buttons.get(pagerIndex).setBackgroundColor(inactiveButtonColor);
                    pagerIndex = buttons.indexOf(btn);
                    btn.setBackgroundColor(activeButtonColor);
                    mPager.setCurrentItem(pagerIndex);
                }
            });
        }

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavigationUtils.displayFragmentProfile(getParentFragmentManager());
            }
        });
    }


    /**
     * A simple pager adapter that represents 5 ScreenSlidePageFragment objects, in
     * sequence.
     */
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return new AvailabilityList(getContext(), position);
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }


    private class ZoomOutPageTransformer implements ViewPager.PageTransformer {
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