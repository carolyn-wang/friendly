package com.example.friendly;

import android.content.Context;
import android.graphics.Color;

import com.example.friendly.objects.Hangout;
import com.example.friendly.objects.Place;

public class DisplayUtils {
    private static final String TYPE_BAR = "bar";
    private static final String TYPE_NOODLE = "noodle";
    private static final String TYPE_TACO = "taco";
    private static final String TYPE_MOVIE = "movie";
    private static final String TYPE_PARK = "park";


    /**
     * Get card color based off hangout's createdAt value
     *
     * @param hangout - given Hangout item to retrieve card color for
     * @return Color int for hangout card
     */
    public static int getCardColor(Context context, Hangout hangout) {
        long i = hangout.getCreatedAt().getTime();
        String[] colorArray = context.getResources().getStringArray(R.array.colors);
        String randomStr = String.valueOf(colorArray[Math.floorMod(i, colorArray.length)]);
        return Color.parseColor(randomStr);
    }

    public static String getEmojiByPlace(Context mContext, Place place) {
        String category = place.getCategory();
        int emojiUnicode;
        if (category == null) {
            emojiUnicode = R.integer.pin_emoji;
        } else {
            switch (category) {
                case TYPE_BAR:
                    emojiUnicode = R.integer.bar_emoji;
                    break;
                case TYPE_NOODLE:
                    emojiUnicode = R.integer.noodle_emoji;
                    break;
                case TYPE_TACO:
                    emojiUnicode = R.integer.taco_emoji;
                    break;
                case TYPE_MOVIE:
                    emojiUnicode = R.integer.movie_emoji;
                    break;
                case TYPE_PARK:
                    emojiUnicode = R.integer.park_emoji;
                    break;
                default:
                    emojiUnicode = R.integer.pin_emoji;
                    break;
            }
        }
        return getEmojiByUnicode(mContext.getResources().getInteger(emojiUnicode));
    }

    public static String getEmojiByUnicode(int unicode) {
        return new String(Character.toChars(unicode));
    }
}
