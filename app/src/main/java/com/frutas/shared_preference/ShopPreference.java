package com.frutas.shared_preference;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

public class ShopPreference {
    public static final String SHOP_PREFERENCE = "frutas";
    public static final String SHOP_ID = "shop_id";

    private final SharedPreferences sharedPreferences;

    public ShopPreference(Context context){
        sharedPreferences = context.getSharedPreferences(SHOP_PREFERENCE, Context.MODE_PRIVATE);
    }

    public void writeData(String field, String data){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(field, data);
        editor.apply();
    }

    public String readData(String field){
        if (sharedPreferences.contains(field))
            return sharedPreferences.getString(field, null);
        return null;
    }


}
