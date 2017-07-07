package hr.ferit.mdudjak.healthdiary;

/**
 * Created by Mario on 13.5.2017..
 */

public class BodyLog {
    int mHeartRate, mUpperPressure, mLowerPressure, mID;
    float mWeight, mBloodSugar;
    String mDate;
    public BodyLog(float weight, int heartRate, float bloodSugar, int upperPressure, int lowerPressure, String date){
        this.mWeight=weight;
        this.mHeartRate=heartRate;
        this.mBloodSugar=bloodSugar;
        this.mUpperPressure=upperPressure;
        this.mLowerPressure=lowerPressure;
        this.mDate=date;
    }
    public BodyLog(float weight, int heartRate, float bloodSugar, int upperPressure, int lowerPressure,String date,int ID){
        this.mWeight=weight;
        this.mHeartRate=heartRate;
        this.mBloodSugar=bloodSugar;
        this.mUpperPressure=upperPressure;
        this.mLowerPressure=lowerPressure;
        this.mDate=date;
        this.mID=ID;
    }
    public Float getWeight(){return mWeight;}
    public int getHeartRate(){return mHeartRate;}
    public Float getBloodSugar(){return mBloodSugar;}
    public int getUpperPressure(){ return mUpperPressure;}
    public int getLowerPressure(){return mLowerPressure;}
    public String getDate(){return mDate;}
    public int getID(){ return mID;}
}
