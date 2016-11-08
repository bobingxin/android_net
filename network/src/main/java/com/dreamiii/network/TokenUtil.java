package com.dreamiii.network;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by bbx on 2016/11/7.
 */

public class TokenUtil {
    private static final String TOKEN = "token";

    public static void setToken(Context context,String token){
        SharedPreferences sp = context.getSharedPreferences(TOKEN,Context.MODE_PRIVATE);
        sp.edit().putString(TOKEN,token).apply();
    }

    public static String getToken(Context context){
        return context.getSharedPreferences(TOKEN,Context.MODE_PRIVATE).getString(TOKEN,null);
    }
}
