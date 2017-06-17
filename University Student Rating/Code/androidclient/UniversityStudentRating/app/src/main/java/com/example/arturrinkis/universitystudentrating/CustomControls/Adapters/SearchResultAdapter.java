package com.example.arturrinkis.universitystudentrating.CustomControls.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.arturrinkis.universitystudentrating.DTO.UserProfile;
import com.example.arturrinkis.universitystudentrating.R;

import java.util.ArrayList;

public class SearchResultAdapter  extends BaseAdapter {
    Context context;
    LayoutInflater lInflater;
    ArrayList<UserProfile> userProfiles;

    public SearchResultAdapter(Context context, ArrayList<UserProfile> userProfiles) {
        this.context = context;
        this.userProfiles = userProfiles;
        lInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return userProfiles.size();
    }

    @Override
    public Object getItem(int position) {
        return userProfiles.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.search_item, parent, false);
        }

        UserProfile userProfile = getUserProfile(position);

        ((TextView)view.findViewById(R.id.user_name)).setText(userProfile.getFirstName() + " " + userProfile.getLastName());
        ((TextView)view.findViewById(R.id.university_name)).setText(userProfile.getUniversity().getName());
        if(userProfile.getStatus().getId() == 1){
            ((TextView)view.findViewById(R.id.faculty_name)).setText(userProfile.getFaculty().getName());
            ((TextView)view.findViewById(R.id.course_name)).setText(String.valueOf(userProfile.getCourse().getNumber()) + " course");
            ((TextView)view.findViewById(R.id.faculty_name)).setVisibility(View.VISIBLE);
            ((TextView)view.findViewById(R.id.course_name)).setVisibility(View.VISIBLE);
        }
        else{
            ((TextView)view.findViewById(R.id.faculty_name)).setVisibility(View.GONE);
            ((TextView)view.findViewById(R.id.course_name)).setVisibility(View.GONE);
        }
        ((TextView)view.findViewById(R.id.status_name)).setText(userProfile.getStatus().getName());
        ((ImageView)view.findViewById(R.id.photo_image)).setImageBitmap(userProfile.getPhotoPathBitmap());

        return view;
    }

    UserProfile getUserProfile(int position) {
        return ((UserProfile) getItem(position));
    }
}