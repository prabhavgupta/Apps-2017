package hr.ferit.mdudjak.healthdiary;

/**
 * Created by Student on 11.5.2017..
 */

public class Symptom {
    String mArea, mDescription,mDate;
    int mIntensity, mID;

    public Symptom(String area, String description, int intensity,String date){
        this.mArea=area;
        this.mDescription=description;
        this.mIntensity=intensity;
        this.mDate=date;


    }
    public Symptom(String area, String description, int intensity,String date,int ID){
        this.mArea=area;
        this.mDescription=description;
        this.mIntensity=intensity;
        this.mID=ID;
        this.mDate=date;

    }
    public String getArea(){return mArea;}
    public String getDescription(){return mDescription;}
    public int getIntensity(){return mIntensity;}
    public int getID(){return mID;}
    public String getDate(){return mDate;}
}
