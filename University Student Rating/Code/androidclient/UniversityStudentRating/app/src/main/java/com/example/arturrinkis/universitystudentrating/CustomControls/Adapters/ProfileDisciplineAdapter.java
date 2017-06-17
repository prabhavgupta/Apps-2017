package com.example.arturrinkis.universitystudentrating.CustomControls.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.arturrinkis.universitystudentrating.DTO.Discipline;
import com.example.arturrinkis.universitystudentrating.DTO.ProfessorDiscipline;
import com.example.arturrinkis.universitystudentrating.R;

import java.util.ArrayList;

public class ProfileDisciplineAdapter  extends BaseAdapter {
    Context context;
    LayoutInflater lInflater;
    ArrayList<Discipline> disciplines;
    ArrayList<ProfessorDiscipline> professorDisciplines;
    public ArrayList<Boolean> checkedDisciplines = new ArrayList<Boolean>();

    public ProfileDisciplineAdapter(Context context, ArrayList<Discipline> disciplines, ArrayList<ProfessorDiscipline> professorDisciplines) {
        this.context = context;
        this.disciplines = disciplines;
        this.professorDisciplines = professorDisciplines;
        for(int i = 0; i < disciplines.size(); ++i){
            checkedDisciplines.add(getCheckedState(disciplines.get(i)));
        }
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
            view = lInflater.inflate(R.layout.professor_discipline_item, parent, false);
        }

        final Discipline discipline = getDiscipline(position);

        boolean isChecked = getCheckedState(discipline);
        ((CheckBox)view.findViewById(R.id.check_professor)).setChecked(isChecked);
        ((CheckBox)view.findViewById(R.id.check_professor)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int index = disciplines.indexOf(discipline);
                checkedDisciplines.set(index, !checkedDisciplines.get(index));
            }
        });

        ((TextView)view.findViewById(R.id.professor_name)).setText(discipline.getName());
        ((TextView)view.findViewById(R.id.discipline_name)).setText(discipline.getName());
        ((ImageView)view.findViewById(R.id.professor_photo_image)).setImageBitmap(discipline.getIconBitmap());

        return view;
    }

    Discipline getDiscipline(int position) {
        return ((Discipline) getItem(position));
    }

    private boolean getCheckedState(Discipline discipline){
        for(int i = 0; i < professorDisciplines.size(); ++i){
            if(professorDisciplines.get(i).getDisciplineId() == discipline.getId()){
                return true;
            }
        }
        return false;
    }
}