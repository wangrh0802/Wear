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

    public static int getWearResult(String wearChoice, String phoneChoice){

        if(wearChoice.equals(ROCK)){

            if(phoneChoice.equals(ROCK))
                return TIE;
            if(phoneChoice.equals(PAPER))
                return LOSE;
            if(phoneChoice.equals(SCISSORS))
                return WIN;

        }else if(wearChoice.equals(PAPER)){

            if(phoneChoice.equals(ROCK))
                return WIN;
            if(phoneChoice.equals(PAPER))
                return TIE;
            if(phoneChoice.equals(SCISSORS))
                return LOSE;

        }if(wearChoice.equals(SCISSORS)){

            if(phoneChoice.equals(ROCK))
                return LOSE;
            if(phoneChoice.equals(PAPER))
                return WIN;
            if(phoneChoice.equals(SCISSORS))
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
