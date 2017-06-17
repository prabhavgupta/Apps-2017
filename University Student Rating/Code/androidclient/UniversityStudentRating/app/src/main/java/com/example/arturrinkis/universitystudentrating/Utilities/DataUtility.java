package com.example.arturrinkis.universitystudentrating.Utilities;

import android.graphics.Bitmap;

import com.example.arturrinkis.universitystudentrating.DTO.Rating;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Locale;

public class DataUtility {
    public ArrayList<RatingMonth> getRatingByMonthes(ArrayList<Rating> ratings, StudentRatingType ratingType){
        ArrayList<RatingMonth> ratingByMonthes = new ArrayList<>();
        Collections.sort(ratings, new RatingMonthComparator());
        Calendar calendar = Calendar.getInstance();

        for(int i = 0; i < ratings.size(); ){
            float value = ratings.get(i).getPoints();
            int count = 1;
            calendar.setTime(ratings.get(i).getDate());
            int month = calendar.get(Calendar.MONTH);
            int year = calendar.get(Calendar.YEAR);
            String monthText = getMonthForInt(month);

            for(int j = i+1; j < ratings.size(); ++j){
                calendar.setTime(ratings.get(j).getDate());
                int month2 = calendar.get(Calendar.MONTH);
                if(month2 == month){
                    value += ratings.get(j).getPoints();
                    ++count;
                }
                else{
                    break;
                }
            }

            value = ratingType == StudentRatingType.Average ? value/count : value;
            RatingMonth ratingMonth = new RatingMonth(value, monthText);
            ratingByMonthes.add(ratingMonth);
            i += count;
        }

        return ratingByMonthes;
    }

    public Bitmap cropBitmap(Bitmap srcBmp){
        Bitmap dstBmp;
        if (srcBmp.getWidth() >= srcBmp.getHeight()){

            dstBmp = Bitmap.createBitmap(
                    srcBmp,
                    srcBmp.getWidth()/2 - srcBmp.getHeight()/2,
                    0,
                    srcBmp.getHeight(),
                    srcBmp.getHeight()
            );

        }else{

            dstBmp = Bitmap.createBitmap(
                    srcBmp,
                    0,
                    srcBmp.getHeight()/2 - srcBmp.getWidth()/2,
                    srcBmp.getWidth(),
                    srcBmp.getWidth()
            );
        }
        return Bitmap.createScaledBitmap(dstBmp, 200, 200, false);
    }

    public String getMonthForInt(int num) {
        String month = "wrong";
        DateFormatSymbols dfs = new DateFormatSymbols(Locale.US);
        String[] months = dfs.getMonths();
        if (num >= 0 && num <= 11 ) {
            month = months[num];
        }
        return month;
    }
}