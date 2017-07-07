package hr.ferit.mdudjak.healthdiary;

/**
 * Created by Student on 16.5.2017..
 */

public class CameraLog {
    int mID;
    String mPictureUrl, mPictureDate;
    public CameraLog(String url, String date){
        this.mPictureUrl=url;
        this.mPictureDate=date;
    }
    public CameraLog(int ID, String url, String date){
        this.mID = ID;
        this.mPictureUrl=url;
        this.mPictureDate=date;
    }
    public String getPictureURL(){return mPictureUrl;}
    public String getPictureDate(){return mPictureDate;}
    public int getID(){return mID;}
}
