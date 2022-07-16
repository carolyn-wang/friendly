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

    public static String getEmojiByPlace(Place place) {
        String category = place.getCategory();
        if (category == null) {
            return getEmojiByUnicode(0x1F4CD);
        } else {
            String emoji;
            switch (category) {
                case TYPE_BAR:
                    emoji = getEmojiByUnicode(0x1F37A);
                    break;
                case TYPE_NOODLE:
                    emoji = getEmojiByUnicode(0x1F35C);
                    break;
                case TYPE_TACO:
                    emoji = getEmojiByUnicode(0x1F32E);
                    break;
                case TYPE_MOVIE:
                    emoji = getEmojiByUnicode(0x1F3AC);
                    break;
                case TYPE_PARK:
                    emoji = getEmojiByUnicode(0x1F333);
                    break;
                default:
                    emoji = getEmojiByUnicode(0x1F4CD);
                    break;
            }
            return emoji;
        }
    }

    public static String getEmojiByUnicode(int unicode) {
        return new String(Character.toChars(unicode));
    }
}
