package com.example.arturrinkis.universitystudentrating.DTO;

import android.graphics.Bitmap;

public class DisciplineBranch {
    private int id;
    private String name;
    private String iconPath;

    Bitmap iconBitmap;

    public DisciplineBranch(int id, String name, String iconPath) {
        this.id = id;
        this.name = name;
        this.iconPath = iconPath;
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
