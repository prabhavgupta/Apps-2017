package com.example.arturrinkis.universitystudentrating.DTO;

import android.graphics.Bitmap;

public class Discipline {
    private int id;
    private String name;
    private String iconPath;
    private DisciplineBranch disciplineBranch;

    Bitmap iconBitmap;

    public Discipline(int id, String name, String iconPath, DisciplineBranch disciplineBranch) {
        this.id = id;
        this.name = name;
        this.iconPath = iconPath;
        this.disciplineBranch = disciplineBranch;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIconPath() {
        return iconPath;
    }

    public void setIconPath(String iconPath) {
        this.iconPath = iconPath;
    }

    public DisciplineBranch getDisciplineBranch() {
        return disciplineBranch;
    }

    public void setDisciplineBranch(DisciplineBranch disciplineBranch) {
        this.disciplineBranch = disciplineBranch;
    }

    public Bitmap getIconBitmap() {
        return iconBitmap;
    }

    public void setIconBitmap(Bitmap iconBitmap) {
        this.iconBitmap = iconBitmap;
    }

    @Override
    public String toString(){
        return name;
    }
}