package com.example.friendly;

import android.content.Context;
import android.graphics.Color;

import com.example.friendly.objects.Hangout;

public class DisplayUtils {
    /**
     * Get card color based off hangout's createdAt value
     * @param hangout - given Hangout item to retrieve card color for
     * @return Color int for hangout card
     */
    public static int getCardColor(Context context, Hangout hangout){
        long i = hangout.getCreatedAt().getTime();
        String[] colorArray = context.getResources().getStringArray(R.array.colors);
        String randomStr = String.valueOf(colorArray[ Math.floorMod(i, colorArray.length)]);
        return Color.parseColor(randomStr);
    }
}
