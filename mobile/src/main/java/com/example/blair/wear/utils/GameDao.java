package com.example.blair.wear.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by chris on 16/11/12.
 */
public class GameDao {

    private static final String SHARE_NAME = "game";
    private SharedPreferences sharedPreferences;
    private static final String PHONE_WIN_NUMBER = "PHONE_WIN_NUMBER";
    private static final String PHONE_TIE_NUMBER = "PHONE_TIE_NUMBER";
    private static final String PHONE_LOSE_NUMBER = "PHONE_LOSE_NUMBER";

    private static GameDao gameDao;
    public static GameDao getInstance(Context context){
        if(gameDao == null){
            gameDao = new GameDao(context);
        }
        return gameDao;
    }

    public GameDao(Context context){
        sharedPreferences = context.getSharedPreferences(SHARE_NAME,Context.MODE_PRIVATE);
    }

    public void addWin(){
        int number = getPhoneWinNumber();
        number++;
        sharedPreferences.edit().putInt(PHONE_WIN_NUMBER, number).commit();
    }
    public void addTie(){
        int number = getPhoneTieNumber();
        number++;
        sharedPreferences.edit().putInt(PHONE_TIE_NUMBER, number).commit();
    }
    public void addLose(){
        int number = getPhoneLoseNumber();
        number++;
        sharedPreferences.edit().putInt(PHONE_LOSE_NUMBER, number).commit();
    }

    public int getPhoneWinNumber(){
        return sharedPreferences.getInt(PHONE_WIN_NUMBER, 0);
    }

    public int getPhoneTieNumber(){
        return sharedPreferences.getInt(PHONE_TIE_NUMBER, 0);
    }

    public int getPhoneLoseNumber(){
        return sharedPreferences.getInt(PHONE_LOSE_NUMBER, 0);
    }
}
