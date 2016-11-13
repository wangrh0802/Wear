package com.example.blair.wear.utils;

import com.example.blair.wear.R;

/**
 * Created by chris on 16/11/12.
 */
public class Util {

    public static final String ROCK = "rock";
    public static final String PAPER = "paper";
    public static final String SCISSORS = "scissors";

    public static final int WIN = 10;
    public static final int TIE = 11;
    public static final int LOSE = 12;

    public static int getPhoneResult(String phoneChoice, String wearChoice){

        if(phoneChoice.equals(ROCK)){

            if(wearChoice.equals(ROCK))
                return TIE;
            if(wearChoice.equals(PAPER))
                return LOSE;
            if(wearChoice.equals(SCISSORS))
                return WIN;

        }else if(phoneChoice.equals(PAPER)){

            if(wearChoice.equals(ROCK))
                return WIN;
            if(wearChoice.equals(PAPER))
                return TIE;
            if(wearChoice.equals(SCISSORS))
                return LOSE;

        }if(phoneChoice.equals(SCISSORS)){

            if(wearChoice.equals(ROCK))
                return LOSE;
            if(wearChoice.equals(PAPER))
                return WIN;
            if(wearChoice.equals(SCISSORS))
                return TIE;

        }
        return -1;
    }

    public static int getImageIdByName(String name){

        if(name.equals(ROCK))
            return R.mipmap.rock;
        else if(name.equals(PAPER))
            return R.mipmap.paper;
        else
            return R.mipmap.scissors;
    }

}
