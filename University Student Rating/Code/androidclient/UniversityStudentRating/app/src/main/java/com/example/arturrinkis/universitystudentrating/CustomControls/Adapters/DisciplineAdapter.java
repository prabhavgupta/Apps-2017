package com.example.arturrinkis.universitystudentrating.CustomControls.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.arturrinkis.universitystudentrating.DTO.Discipline;
import com.example.arturrinkis.universitystudentrating.R;

import java.util.ArrayList;

public class DisciplineAdapter extends BaseAdapter {
    Context context;
    LayoutInflater lInflater;
    ArrayList<Discipline> disciplines;

    public DisciplineAdapter(Context context, ArrayList<Discipline> disciplines) {
        this.context = context;
        this.disciplines = disciplines;
        lInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return disciplines.size();
    }

    @Override
    public Object getItem(int position) {
        return disciplines.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.discipline_item, parent, false);
        }

        Discipline discipline = getDiscipline(position);

        ((TextView) view.findViewById(R.id.discipline_name)).setText(discipline.getName());
        ((TextView) view.findViewById(R.id.discipline_description)).setText(discipline.getName());
        ((ImageView) view.findViewById(R.id.discipline_image)).setImageBitmap(discipline.getIconBitmap());

        return view;
    }

    Discipline getDiscipline(int position) {
        return ((Discipline) getItem(position));
    }
}