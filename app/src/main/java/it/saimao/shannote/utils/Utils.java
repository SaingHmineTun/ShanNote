package it.saimao.shannote.utils;

import android.content.Context;
import android.util.TypedValue;

import androidx.core.content.ContextCompat;

import java.util.List;

public class Utils {

    public static final List<Integer> COLORS = List.of(com.google.android.material.R.attr.colorSurface, com.google.android.material.R.attr.colorPrimaryContainer, com.google.android.material.R.attr.colorSecondaryContainer, com.google.android.material.R.attr.colorTertiaryContainer, com.google.android.material.R.attr.colorErrorContainer);
    public static final List<Integer> STROKE_COLORS = List.of(com.google.android.material.R.attr.colorOnSurface, com.google.android.material.R.attr.colorOnPrimaryContainer, com.google.android.material.R.attr.colorOnSecondaryContainer, com.google.android.material.R.attr.colorOnTertiaryContainer, com.google.android.material.R.attr.colorOnErrorContainer);

    public static int getColorFromTheme(Context context, int attributeId) {
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(attributeId, typedValue, true);
        return ContextCompat.getColor(context, typedValue.resourceId);
    }
}
