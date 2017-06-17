package com.example.arturrinkis.universitystudentrating.Utilities;

import com.example.arturrinkis.universitystudentrating.DTO.Rating;

import java.util.Calendar;
import java.util.Comparator;

public class RatingMonthComparator implements Comparator<Rating> {
    @Override
    public int compare(Rating rating1, Rating rating2) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(rating1.getDate());
        Integer month1 = calendar.get(Calendar.MONTH);
        calendar.setTime(rating2.getDate());
        Integer month2 = calendar.get(Calendar.MONTH);

        return month1.compareTo(month2);
    }
}