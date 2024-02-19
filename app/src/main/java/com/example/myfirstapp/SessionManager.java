package com.example.myfirstapp;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {

    private static final String PREF_NAME = "MyAppPreferences";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_USER_NAME = "userName";
    private static final String KEY_USER_EMAIL = "email";
    private static final String KEY_NAME = "name";
    private static final String KEY_PHONE ="phone";
    private static final String KEY_BIRTH_DATE="birthDate";
    private static final String KEY_PROFESSION="profession";


    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Context context;

    public SessionManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void setLoggedIn(boolean isLoggedIn) {
        editor.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn);
        editor.apply();
    }

    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false);
    }
    public void saveUserInfo(String userName, String email, String name, String birthDate, String profession, String phone) {
        editor.putString(KEY_USER_NAME, userName);
        editor.putString(KEY_USER_EMAIL, email);
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_BIRTH_DATE,birthDate);
        editor.putString(KEY_PHONE,phone);
        editor.putString(KEY_PROFESSION,profession);
        editor.apply();
    }

    public String getUserName() {
        return sharedPreferences.getString(KEY_USER_NAME, "");
    }

    public String getUserEmail() {
        return sharedPreferences.getString(KEY_USER_EMAIL, "");
    }
    public String getUName(){
        return sharedPreferences.getString(KEY_NAME, "");
    }

    public String getBirthDate(){
        return sharedPreferences.getString(KEY_BIRTH_DATE, "");
    }

    public String getProfession(){
        return sharedPreferences.getString(KEY_PROFESSION, "");
    }

    public String getPhone(){
        return sharedPreferences.getString(KEY_PHONE, "");
    }

}
