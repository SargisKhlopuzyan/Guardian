package com.sargis.kh.guardian.helpers;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class HelperDateTime {

    public static String increaseDateByOneSecond(String date) {
        String pattern = "yyyy-MM-dd'T'HH:mm:ss";
        return parseDate(date, pattern, pattern);
    }

    private static String parseDate(String date, String currentPattern, String outputPattern) {

        SimpleDateFormat sdf = new SimpleDateFormat(currentPattern, Locale.getDefault());
        try {
            Date dataToIncrease = sdf.parse(date);
            Date increasedDate = increaseDate(dataToIncrease);

            sdf.applyPattern(outputPattern);
            return sdf.format(increasedDate);
        } catch (Exception e) {
            return "";
        }
    }

    private static Date increaseDate(Date origDate) {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(origDate);
        calendar.add(Calendar.SECOND, +1);
        Date newDate = calendar.getTime();
        calendar.setTimeZone(TimeZone.getTimeZone("GMT"));
        return newDate;
    }

}
