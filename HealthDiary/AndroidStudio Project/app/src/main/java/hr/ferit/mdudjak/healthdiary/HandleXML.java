package hr.ferit.mdudjak.healthdiary;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mario on 29.5.2017..
 */

public class HandleXML {

    private String urlString = null;
    List links,titles,descriptions,pubdates,images;
    private XmlPullParserFactory xmlFactoryObject;
    //This class is used to create implementations of XML Pull Parser defined in XMPULL V1 API. Usef for creating new Instance of XmlPullParser class.
    private String sFailedMessage="OK";
    public volatile boolean parsingComplete = true;

    public HandleXML(String url){
        this.urlString = url;
    }

    public List getLinks(){
        return links;
    }
    public List getTitles(){
        return titles;
    }
    public List getDescriptions(){
        return descriptions;
    }
    public List getPubDates(){
        return pubdates;
    }
    public List getImages(){
        return images;
    }
    public String getsFailedMessage(){return sFailedMessage;}

    public void parseXMLAndStoreIt(XmlPullParser myParser) {
        int event;
        String text=null;
        links = new ArrayList();
        descriptions = new ArrayList();
        titles = new ArrayList();
        pubdates = new ArrayList();
        images = new ArrayList();

        try {
            event = myParser.getEventType();

            while (event != XmlPullParser.END_DOCUMENT) {
                //No more events are available
                String name=myParser.getName();
                //When namespace processing is disabled, the raw name is returned
                switch (event){
                    case XmlPullParser.START_TAG:
                        //An XML start tag was read.
                        break;

                    case XmlPullParser.TEXT:
                        //Text content was read
                        text = myParser.getText();
                        break;

                    case XmlPullParser.END_TAG:
                        //An end tag was read
                        if(name.equals("title")){
                            titles.add(text);
                        }

                        else if(name.equals("link")){
                            links.add(text);
                        }
                        else if(name.equals("description")) {

                            String[] descriptionText = text.split("<p>|</p>");
                            String[] strings = text.split("\"");
                            for (String string: strings) {

                                String[] newString = string.split("/");
                                for (String a: newString) {
                                    if (a.equals("images")){  //in your question pix or other words
                                        images.add(string);
                                    }
                                }
                            }
                            int i=0;
                            for (String string: descriptionText) {
                                if(i++==1)
                                descriptions.add(string);
                            }

                        }

                        else if(name.equals("pubDate")){
                            pubdates.add(text);
                        }

                        break;
                }

                event = myParser.next();
            }

           parsingComplete = false;
        }

        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void fetchXML(){
        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {

                try {
                    URL url = new URL(urlString);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    //For HTTP Requests
                    conn.setReadTimeout(1000 /* milliseconds */);
                    conn.setConnectTimeout(1000 /* milliseconds */);
                    conn.setRequestMethod("GET");
                    conn.setDoInput(true);

                    // Starts the query
                    conn.connect();
                    InputStream stream = conn.getInputStream();

                    xmlFactoryObject = XmlPullParserFactory.newInstance();
                    XmlPullParser myparser = xmlFactoryObject.newPullParser();

                    myparser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                    //This feature determines whether the parser processes namespaces. (xmlns)
                    myparser.setInput(stream, null);

                    parseXMLAndStoreIt(myparser);
                    stream.close();
                }

                catch (Exception e) {
                    sFailedMessage= String.valueOf(R.string.connectionTimeoutMessage);
                    parsingComplete=false;
                }
            }
        });
        thread.start();
    }
}
