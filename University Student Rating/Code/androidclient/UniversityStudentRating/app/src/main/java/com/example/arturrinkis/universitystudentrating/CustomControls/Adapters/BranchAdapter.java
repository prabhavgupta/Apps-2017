package com.example.arturrinkis.universitystudentrating.CustomControls.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.arturrinkis.universitystudentrating.DTO.DisciplineBranch;
import com.example.arturrinkis.universitystudentrating.R;

import java.util.ArrayList;

public class BranchAdapter extends BaseAdapter {
    Context context;
    LayoutInflater lInflater;
    ArrayList<DisciplineBranch> branches;

    public BranchAdapter(Context context, ArrayList<DisciplineBranch> branches) {
        this.context = context;
        this.branches = branches;
        lInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return branches.size();
    }

    @Override
    public Object getItem(int position) {
        return branches.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.branch_item, parent, false);
        }

        DisciplineBranch disciplineBranch = getDisciplineBranch(position);

        ((TextView) view.findViewById(R.id.branch_name)).setText(disciplineBranch.getName());
        ((TextView) view.findViewById(R.id.branch_description)).setText(disciplineBranch.getName());
        ((ImageView) view.findViewById(R.id.ivImage)).setImageBitmap(disciplineBranch.getIconBitmap());

        return view;
    }

    DisciplineBranch getDisciplineBranch(int position) {
        return ((DisciplineBranch) getItem(position));
    }
}
