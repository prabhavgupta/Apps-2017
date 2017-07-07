package hr.ferit.mdudjak.healthdiary;

/**
 * Created by Mario on 29.5.2017..
 */

public class News {
    private String mLink;
    private String mDescription;
    private String mTitle;
    private String mPubDate;
    private String mImage;

    public News(String title, String description, String link, String pubDate,String image){
        this.mTitle=title;
        this.mDescription=description;
        this.mLink=link;
        this.mPubDate= pubDate;
        this.mImage = image;
    }

    public String getTitle() { return mTitle; }
    public String getLink() { return mLink; }
    public String getImage() { return mImage;}
    public String getDescription() { return mDescription; }
    public String getPubDate() { return mPubDate; }

}
