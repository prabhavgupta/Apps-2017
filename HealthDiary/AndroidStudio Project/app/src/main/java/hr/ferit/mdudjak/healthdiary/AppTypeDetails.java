package hr.ferit.mdudjak.healthdiary;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Mario on 8.6.2017..
 */

public class AppTypeDetails {

    private SharedPreferences sh;
    private AppTypeDetails() {

    }

    private AppTypeDetails(Context mContext) {
        sh = PreferenceManager.getDefaultSharedPreferences(mContext);
    }

    private static AppTypeDetails instance = null;

    public synchronized static AppTypeDetails getInstance(Context mContext) {
        if (instance == null) {
            instance = new AppTypeDetails(mContext);
        }
        return instance;
    }


    public boolean getToggleStatus(int position) {
        Log.e("GET", String.valueOf(position));
        return sh.getBoolean("ToggleButton"+position, true);
    }

    public void setToggleStatus(Boolean toggle,int position) {
        Log.e("SET", String.valueOf(position));
        sh.edit().putBoolean("ToggleButton"+position, toggle).commit();
    }



    public void clear() {
        sh.edit().clear().commit();
    }

}