package hr.ferit.mdudjak.healthdiary;

/**
 * Created by Mario on 7.6.2017..
 */

public class Reminder {
    private String mTitle;
    private String mDescription;
    private String mDateTime;
    private int mID;
    private int mRepeatingTime;

    public Reminder(String title, String description, String dateTime, int repeating,int ID){
        this.mTitle=title;
        this.mDescription=description;
        this.mDateTime=dateTime;
        this.mID=ID;
        this.mRepeatingTime=repeating;
    }
    public Reminder(String title, String description, String dateTime, int repeating){
        this.mTitle=title;
        this.mDescription=description;
        this.mDateTime=dateTime;
        this.mRepeatingTime=repeating;
    }
    public String getTitle() { return mTitle; }
    public String getDescription() { return mDescription; }
    public String getDateTime(){return mDateTime;}
    public int getID(){return  mID;}
    public int getRepeatingTime(){ return mRepeatingTime;}

}
