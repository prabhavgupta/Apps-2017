package com.example.arturrinkis.universitystudentrating.CustomControls.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.arturrinkis.universitystudentrating.DTO.ProfessorDiscipline;
import com.example.arturrinkis.universitystudentrating.DTO.StudentProfessorDiscipline;
import com.example.arturrinkis.universitystudentrating.DTO.UserProfile;
import com.example.arturrinkis.universitystudentrating.R;

import java.util.ArrayList;

public class ProfessorDisciplineAdapter extends BaseAdapter {
    Context context;
    LayoutInflater lInflater;
    ArrayList<ProfessorDiscipline> professorDisciplines;
    ArrayList<StudentProfessorDiscipline> studentProfessorDisciplines;
    public ArrayList<Boolean> checkedProfessorDisciplines = new ArrayList<Boolean>();

    public ProfessorDisciplineAdapter(Context context, ArrayList<ProfessorDiscipline> professorDiscipline, ArrayList<StudentProfessorDiscipline> studentProfessorDiscipline) {
        this.context = context;
        this.professorDisciplines = professorDiscipline;
        this.studentProfessorDisciplines = studentProfessorDiscipline;
        for(int i = 0; i < professorDisciplines.size(); ++i){
            checkedProfessorDisciplines.add(getCheckedState(professorDisciplines.get(i)));
        }
        lInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return professorDisciplines.size();
    }

    @Override
    public Object getItem(int position) {
        return professorDisciplines.get(position);
    }

    public UserProfile getUserProfile(int position) {
        return professorDisciplines.get(position).getUserProfile();
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

        final ProfessorDiscipline professorDiscipline = getProfessorDiscipline(position);

        boolean isChecked = getCheckedState(professorDiscipline);
        ((CheckBox)view.findViewById(R.id.check_professor)).setChecked(isChecked);
        ((CheckBox)view.findViewById(R.id.check_professor)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int index = professorDisciplines.indexOf(professorDiscipline);
                checkedProfessorDisciplines.set(index, !checkedProfessorDisciplines.get(index));
            }
        });

        ((TextView)view.findViewById(R.id.professor_name)).setText(professorDiscipline.getUserProfile().getFirstName() + " " + professorDiscipline.getUserProfile().getLastName());
        ((TextView)view.findViewById(R.id.discipline_name)).setText(professorDiscipline.getDiscipline().getName());
        ((ImageView)view.findViewById(R.id.professor_photo_image)).setImageBitmap(professorDiscipline.getUserProfile().getPhotoPathBitmap());

        return view;
    }

    ProfessorDiscipline getProfessorDiscipline(int position) {
        return ((ProfessorDiscipline) getItem(position));
    }

    private boolean getCheckedState(ProfessorDiscipline professorDiscipline){
        for(int i = 0; i < studentProfessorDisciplines.size(); ++i){
            if(studentProfessorDisciplines.get(i).getProfessorDisciplineId() == professorDiscipline.getId()){
                return true;
            }
        }
        return false;
    }
}