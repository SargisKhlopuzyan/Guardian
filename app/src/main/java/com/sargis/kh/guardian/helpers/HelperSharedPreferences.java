package com.sargis.kh.guardian.helpers;

import android.content.SharedPreferences;

import com.sargis.kh.guardian.GuardianApplication;
import com.sargis.kh.guardian.utils.Constants;

public class HelperSharedPreferences {

    public static String getLastWebPublicationDate() {
        SharedPreferences pref = GuardianApplication.getContext().getSharedPreferences(Constants.SharedPreferences.NAME,0); // 0 - for private mode
        return pref.getString(Constants.SharedPreferences.LAST_WEB_PUBLICATION_DATE, null);
    }

    public static boolean setLastWebPublicationDateIncreasedByOneSecond(String lastWebPublicationDate) {
        String increasedLastWebPublicationDate = HelperDateTime.increaseDateByOneSecond(lastWebPublicationDate);
        SharedPreferences pref = GuardianApplication.getContext().getSharedPreferences(Constants.SharedPreferences.NAME,0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(Constants.SharedPreferences.LAST_WEB_PUBLICATION_DATE, increasedLastWebPublicationDate);
        return editor.commit();
    }
}
