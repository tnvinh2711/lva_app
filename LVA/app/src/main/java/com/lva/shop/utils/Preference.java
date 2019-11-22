package com.lva.shop.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Preference {
    /**
     * The name of SharedPreferences
     */
    private static final String PREFERENCE_NAME = "eam_preference";

    /**
     * Get the instance of SharedPreferences
     *
     * @param context
     * @return
     */
    public static SharedPreferences getSharedPreferences(Context context) {
        if(context!=null){
            return context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        }else{
            return null;
        }
    }

    /**
     * Save Single String preference
     *
     * @param context
     * @param key
     * @param value
     * @return
     */
    public static boolean save(Context context, String key, String value) {
        try {
            SharedPreferences sharedPreferences = getSharedPreferences(context);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(key, value);
            return editor.commit();
        }catch (Exception e){
            return false;
        }
    }

    /**
     * Get string value through key
     *
     * @param context
     * @param key
     * @return
     */
    public static String getString(Context context, String key) {
        try {
            SharedPreferences sharedPreferences = getSharedPreferences(context);
            return sharedPreferences.getString(key,null);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static String getString(Context context, String key , String strDefault) {
        try {
            SharedPreferences sharedPreferences = getSharedPreferences(context);
            return sharedPreferences.getString(key, strDefault);
        }catch (Exception e){
            e.printStackTrace();
            return strDefault;
        }
    }

    /**
     * Save map String preference
     *
     * @param context
     * @param valuesMap
     * @return
     */
    public static boolean save(Context context, HashMap<String, String> valuesMap) {
        try {
            SharedPreferences sharedPreferences = getSharedPreferences(context);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            String value = "";
            for (String key : valuesMap.keySet()) {
                value = valuesMap.get(key);
                editor.putString(key, value);
            }
            return editor.commit();
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Save single boolean preference
     *
     * @param context
     * @param key
     * @param value
     * @return
     */
    public static boolean save(Context context, String key, boolean value) {
        try {
            SharedPreferences sharedPreferences = getSharedPreferences(context);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(key, value);
            return editor.commit();
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Get boolean value through key
     *
     * @param context
     * @param key
     * @param defFlag
     * @return
     */
    public static boolean getBoolean(Context context, String key, boolean defaultValue) {
        try {
            SharedPreferences sharedPreferences = getSharedPreferences(context);
            return sharedPreferences.getBoolean(key, defaultValue);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Clean the SharedPreferences
     *
     * @param context
     */
    public static void clear(Context context) {
        try {
            SharedPreferences sharedPreferences = getSharedPreferences(context);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.commit();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * remove the value by key
     *
     * @param context
     * @param key
     */
    public static void remove(Context context, String key) {
        try {
            SharedPreferences sharedPreferences = getSharedPreferences(context);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove(key);
            editor.commit();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Save int value
     *
     * @param context
     * @param key
     * @param value
     * @return
     */
    public static boolean save(Context context, String key, int value) {
        try {
            SharedPreferences sharedPreferences = getSharedPreferences(context);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt(key, value);
            return editor.commit();
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Save long value
     *
     * @param context
     * @param key
     * @param value
     * @return
     */
    public static boolean save(Context context, String key, long value) {
        try {
            SharedPreferences sharedPreferences = getSharedPreferences(context);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putLong(key, value);
            return editor.commit();
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Get int value
     *
     * @param context
     * @param key
     * @param defaultValue
     * @return
     */
    public static int getInt(Context context, String key, int defaultValue) {
        try {
            SharedPreferences sharedPreferences = getSharedPreferences(context);
            return sharedPreferences.getInt(key, defaultValue);
        }catch (Exception e){
            e.printStackTrace();
            return 0;
        }
    }


    /**
     * Get long value
     *
     * @param context
     * @param key
     * @param defaultValue
     * @return
     */
    public static long getLong(Context context, String key, long defaultValue) {
        try {
            SharedPreferences sharedPreferences = getSharedPreferences(context);
            return sharedPreferences.getLong(key, defaultValue);
        }catch (Exception e){
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * Save float value
     *
     * @param context
     * @param key
     * @param value
     * @return
     */
    public static boolean save(Context context, String key, float value) {
        try {
            SharedPreferences sharedPreferences = getSharedPreferences(context);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putFloat(key, value);
            return editor.commit();
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Get float value
     *
     * @param context
     * @param key
     * @param defaultValue
     * @return
     */
    public static float getFloat(Context context, String key, float defaultValue) {
        try {
            SharedPreferences sharedPreferences = getSharedPreferences(context);
            return sharedPreferences.getFloat(key, defaultValue);
        }catch (Exception e){
            e.printStackTrace();
            return 0;
        }
    }

    public static boolean save(Context context, String key, Set<String> value) {
        try {
            SharedPreferences sharedPreferences = getSharedPreferences(context);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putStringSet(key, value);
            return editor.commit();
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public static Set<String> getStringSet(Context context, String key, Set<String> defaultValue) {
        try {
            SharedPreferences sharedPreferences = getSharedPreferences(context);
            return sharedPreferences.getStringSet(key, defaultValue);
        }catch (Exception e){
            e.printStackTrace();
            return new HashSet<String>();
        }
    }
}
