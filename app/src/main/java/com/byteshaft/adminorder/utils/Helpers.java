package com.byteshaft.adminorder.utils;

import android.content.Context;
import android.content.ContextWrapper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by husnain on 10/21/15.
 */
public class Helpers extends ContextWrapper {

    public Helpers(Context base) {
        super(base);
    }

    public static String getTimeStampForDatabase() {
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        simpleDateFormat.setTimeZone(TimeZone.getDefault());
        return simpleDateFormat.format(calendar.getTime());
    }
}
