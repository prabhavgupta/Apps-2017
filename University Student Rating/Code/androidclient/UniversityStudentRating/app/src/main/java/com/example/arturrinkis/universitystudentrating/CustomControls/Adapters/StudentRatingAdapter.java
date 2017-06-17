package com.example.arturrinkis.universitystudentrating.CustomControls.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.arturrinkis.universitystudentrating.DTO.IndividualRating;
import com.example.arturrinkis.universitystudentrating.R;
import com.example.arturrinkis.universitystudentrating.Utilities.StudentRatingType;

import java.util.ArrayList;

public class StudentRatingAdapter extends BaseAdapter {
    Context context;
    LayoutInflater lInflater;
    ArrayList<IndividualRating> individualRatings;
    StudentRatingType studentRatingType;

    public StudentRatingAdapter(Context context, ArrayList<IndividualRating> individualRatings, StudentRatingType studentRatingType) {
        this.context = context;
        this.individualRatings = individualRatings;
        lInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.studentRatingType = studentRatingType;
    }

    @Override
    public int getCount() {
        return individualRatings.size();
    }

    @Override
    public Object getItem(int position) {
        return individualRatings.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.student_rating_item, parent, false);
        }

        IndividualRating individualRating = getIndividualRating(position);

        ((TextView)view.findViewById(R.id.student_name)).setText(individualRating.getUserProfile().getFirstName() + " " + individualRating.getUserProfile().getLastName());
        ((TextView)view.findViewById(R.id.discipline_name)).setText(individualRating.getDiscipline().getName());
        if(studentRatingType == StudentRatingType.Average) {
            ((TextView) view.findViewById(R.id.rating_tv)).setText(String.valueOf(individualRating.getAverageClassRating()));
        }
        else if(studentRatingType == StudentRatingType.Overall){
            ((TextView) view.findViewById(R.id.rating_tv)).setText(String.valueOf(individualRating.getTotalClassRating()));
        }
        else if(studentRatingType == StudentRatingType.Olympiads){
            ((TextView) view.findViewById(R.id.rating_tv)).setText(String.valueOf(individualRating.getTotalOlympiadsRating()));
        }

        ((ImageView)view.findViewById(R.id.photo_image)).setImageBitmap(individualRating.getUserProfile().getPhotoPathBitmap());

        return view;
    }

    IndividualRating getIndividualRating(int position) {
        return ((IndividualRating) getItem(position));
    }
}