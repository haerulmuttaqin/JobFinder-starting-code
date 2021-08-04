package id.haerulmuttaqin.jobfinder;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;

import androidx.appcompat.app.AppCompatDelegate;

import org.ocpsoft.prettytime.PrettyTime;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.HttpException;

public final class Utils {
    public static String getStringPreference(Context context, String key) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPrefs.getString(key, null);
    }

    public static int getIntegerPreference(Context context, int key) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPrefs.getInt(String.valueOf(key), 0);
    }

    public static long getLongPreference(Context context, String key) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPrefs.getLong(key, 0);
    }

    public static int getIntegerPreference(Context context, String key) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPrefs.getInt(key, 0);
    }

    public static boolean getBooleanPreference(Context context, String key) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPrefs.getBoolean(key, false);
    }

    public static void putPreference(Context context, String key, String value) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString(key, value);
        editor.apply();
        editor.commit();
    }

    public static void putPreference(Context context, String key, float value) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putFloat(key, value);
        editor.apply();
        editor.commit();
    }

    public static void putPreference(Context context, String key, long value) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putLong(key, value);
        editor.apply();
        editor.commit();
    }

    public static void putPreference(Context context, String key, boolean value) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putBoolean(key, value);
        editor.apply();
        editor.commit();
    }

    public static void putPreference(Context context, String key, int value) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putInt(key, value);
        editor.apply();
        editor.commit();
    }

    public static void setupTheme(boolean isDarkMode) {
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        }
        return false;
    }

    public static String dateToTimeFormat(String oldStringDate) {
        PrettyTime p = new PrettyTime(new Locale("en-US"));
        String isTime = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
                    Locale.ENGLISH);
            Date date = sdf.parse(oldStringDate);
            isTime = p.format(date);
        } catch (ParseException e) {
            SimpleDateFormat sdf2 = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy",
                    Locale.ENGLISH);
            Date date = null;
            try {
                date = sdf2.parse(oldStringDate);
            } catch (ParseException parseException) {
                parseException.printStackTrace();
            }
            isTime = p.format(date);
            e.printStackTrace();
        }

        return isTime;
    }

    public static String dateFormatter(String Date) {
        if (Date != null) {
//            SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
            SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy",Locale.US);
            java.util.Date value = null;
            try {
                value = formatter.parse(Date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return dateFormatter.format(value);
        }
        return "-";
    }

    public static String errorMessageHandler(Call call, Throwable t) {
        if (t instanceof SocketTimeoutException)
        {
            return "Connection timeout, Please try again!";
        }
        else if (t instanceof HttpException) {
            ResponseBody body = ((HttpException) t).response().errorBody();
            try {
                return body.string();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if (t instanceof IOException)
        {
            return "Request timeout, Please try again!";
        }
        else
        {
            //Call was cancelled by user
            if(call.isCanceled())
            {
                return "Call was cancelled forcefully, Please try again!";
            }
            else
            {
                return "Network Problem, Please try again!";
            }
        }
        return "Network Problem, Please try again!";
    }
}
