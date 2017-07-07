package hr.ferit.mdudjak.healthdiary;

import android.util.Log;
import android.widget.TextView;

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

public class HandleTipsXML {

    private String urlString = null;
    private List<String> sAuthors, sTips;
    private XmlPullParserFactory xmlFactoryObject;
    public volatile boolean parsingComplete = true;
    private String sFailedMessage="OK";
    int mNumberOfItems;
    public HandleTipsXML(String url){
        this.urlString = url;
    }

    public List<String> getAuthors(){
        return sAuthors;
    }
    public List<String> getTips(){
        return sTips;
    }
    public int getmNumberOfItems(){return mNumberOfItems;}
    public String getsFailedMessage(){return sFailedMessage;}

    public void parseXMLAndStoreIt(XmlPullParser myParser) {
        int event;
        sAuthors = new ArrayList();
        sTips = new ArrayList();
        String text=null;
        try {
            event = myParser.getEventType();

            while (event != XmlPullParser.END_DOCUMENT) {
                String name=myParser.getName();

                switch (event){
                    case XmlPullParser.START_TAG:
                        break;

                    case XmlPullParser.TEXT:
                        text = myParser.getText();
                        break;

                    case XmlPullParser.END_TAG:

                        if(name.equals("item")){
                            mNumberOfItems++;
                        }

                        if(name.equals("title")){
                            if(mNumberOfItems>0) {
                                sAuthors.add(text);
                            }
                        }

                        else if(name.equals("description")){
                            if(mNumberOfItems>0) {
                                sTips.add(text);
                            }
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

                    conn.setReadTimeout(3000 /* milliseconds */);
                    conn.setConnectTimeout(6000 /* milliseconds */);
                    conn.setRequestMethod("GET");
                    conn.setDoInput(true);

                    // Starts the query
                    conn.connect();
                    InputStream stream = conn.getInputStream();

                    xmlFactoryObject = XmlPullParserFactory.newInstance();
                    XmlPullParser myparser = xmlFactoryObject.newPullParser();

                    myparser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
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