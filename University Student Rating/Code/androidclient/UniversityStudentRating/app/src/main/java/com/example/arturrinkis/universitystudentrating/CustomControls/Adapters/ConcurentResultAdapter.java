package com.example.arturrinkis.universitystudentrating.CustomControls.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.arturrinkis.universitystudentrating.DTO.ConcurentResult;
import com.example.arturrinkis.universitystudentrating.R;

import java.util.ArrayList;

public class ConcurentResultAdapter extends BaseAdapter {
    Context context;
    LayoutInflater lInflater;
    ArrayList<ConcurentResult> results;

    public ConcurentResultAdapter(Context context, ArrayList<ConcurentResult> results) {
        this.context = context;
        this.results = results;
        lInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return results.size();
    }

    @Override
    public Object getItem(int position) {
        return results.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.concurent_item, parent, false);
        }

        ConcurentResult result = getConcurentResult(position);

        ((TextView)view.findViewById(R.id.user_name)).setText(result.getUserProfile().getFirstName() + " " + result.getUserProfile().getLastName());
        ((TextView)view.findViewById(R.id.university_name)).setText(result.getUserProfile().getUniversity().getName());

        ((TextView)view.findViewById(R.id.faculty_name)).setText(result.getUserProfile().getFaculty().getName());
        ((TextView)view.findViewById(R.id.course_name)).setText(String.valueOf(result.getUserProfile().getCourse().getNumber()) + " course");

        ((TextView)view.findViewById(R.id.status_name)).setText(result.getUserProfile().getStatus().getName());
        ((ImageView)view.findViewById(R.id.photo_image)).setImageBitmap(result.getUserProfile().getPhotoPathBitmap());

        ((TextView)view.findViewById(R.id.rating_value_tv)).setText(String.valueOf(result.getRatingValue()));

        return view;
    }

    ConcurentResult getConcurentResult(int position) {
        return ((ConcurentResult) getItem(position));
    }
}