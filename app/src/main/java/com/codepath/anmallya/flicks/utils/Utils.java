package com.codepath.anmallya.flicks.utils;

import java.math.BigDecimal;

/**
 * Created by anmallya on 10/16/2016.
 */
public class Utils {

    public static float round(float d, int decimalPlace) {
        return BigDecimal.valueOf(d).setScale(decimalPlace, BigDecimal.ROUND_HALF_UP).floatValue();
    }
}
