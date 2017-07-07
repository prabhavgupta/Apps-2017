package hr.ferit.mdudjak.healthdiary;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, AdapterView.OnItemClickListener {

    private static final String finalUrl = "http://www.healthline.com/rss/health-news";
    private static final String tipsUrl = "http://feeds.feedburner.com/quotationspage/qotd";
    ListView lvNews;
    List<String> links,descriptions,titles,pubDates,images;
    private HandleXML obj;
    private HandleTipsXML tipsObj;
    NewsAdapter newsAdapter;
    TextView txConnectivity,txConnectionTimeout,txNumberOfSymptomLogs, txInfoData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setUpUI();
        this.setUpListView();
        this.setUpFloatingButton();
    }

    @Override
    protected void onResume(){
        super.onResume();
        this.setUpUI();
    }

    private void setUpFloatingButton() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if(Utils.connectivity(getApplicationContext()))
        {
            tipsObj = new HandleTipsXML(tipsUrl);
            tipsObj.fetchXML();
            while(tipsObj.parsingComplete);
            if(tipsObj.getsFailedMessage().equals("OK")) {

                final int mNumberOfItems = tipsObj.getmNumberOfItems();
                final List<String> tips = tipsObj.getTips();
                final List<String> authors = tipsObj.getAuthors();
                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Random r = new Random();
                        int mRandomNumber = r.nextInt(mNumberOfItems - 1);
                        Snackbar snackbar = Snackbar.make(view, tips.get(mRandomNumber) + "\n" + authors.get(mRandomNumber), Snackbar.LENGTH_LONG)
                                .setAction("Action", null);
                        final View snackbarView = snackbar.getView();
                        final TextView tv = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
                        tv.setText(tips.get(mRandomNumber) + "\n" + authors.get(mRandomNumber));
                        tv.setHeight(250);
                        snackbar.show();
                    }
                });
            }
            else{
                Toast.makeText(getApplicationContext(),R.string.connectionTimeoutMessageForTips, Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            txConnectivity.setVisibility(View.VISIBLE);
            txConnectivity.setText(R.string.noConnectivityWarning);
            Toast.makeText(getApplicationContext(), "Unable to read status feed.", Toast.LENGTH_SHORT).show();
        }
    }

    private void setUpUI() {
        this.txNumberOfSymptomLogs= (TextView) this.findViewById(R.id.txNumberOfSymptomLogs);
        this.txInfoData= (TextView) this.findViewById(R.id.txInfoPodaci);
        this.txConnectivity = (TextView) this.findViewById(R.id.txConnectivity);
        this.txConnectionTimeout= (TextView) this.findViewById(R.id.txConnectionTimeout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar); //Set a Toolbar to act as the ActionBar for this Activity window.
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        //DrawerLayout acts as a top-level container for window content that allows for interactive "drawer" views to be pulled out from one or both vertical edges of the window.
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        //to tie together the functionality of DrawerLayout and the framework ActionBar to implement the recommended design for navigation drawers
        drawer.setDrawerListener(toggle);
        //ActionBarDrawerToggle can be used directly as a DrawerLayout.DrawerListener
        toggle.syncState();
        //Synchronize the state of the drawer indicator/affordance with the linked DrawerLayout.
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        StringBuilder stringBuilder = new StringBuilder();
        Calendar calendar =Calendar.getInstance();
        String day= String.valueOf(calendar.get(Calendar.DATE));
        String month =String.valueOf(calendar.get(Calendar.MONTH));
        String year =String.valueOf(calendar.get(Calendar.YEAR));
        stringBuilder.append(day+".").append(month+".").append(year+".").append("\n");
        switch (calendar.get(Calendar.DAY_OF_WEEK)){
            case 1:  stringBuilder.append("Sunday");
                break;
            case 2:  stringBuilder.append("Monday");
                break;
            case 3:  stringBuilder.append("Tuesday");
                break;
            case 4:  stringBuilder.append("Wednesday");
                break;
            case 5:  stringBuilder.append("Thursday");
                break;
            case 6:  stringBuilder.append("Friday");
                break;
            case 7:  stringBuilder.append("Saturday");
                break;
        }
        this.txInfoData.setText(String.valueOf(stringBuilder));
        List<Symptom> symptoms = DBHelper.getInstance(this).getAllSymptoms();
        int i=0,br=0;
        StringBuilder stringBuilder1 = new StringBuilder();
        stringBuilder1.append(calendar.get(Calendar.DATE) + ".").append(calendar.get(Calendar.MONTH) + ".").append(calendar.get(Calendar.YEAR) + ".");
        String dateNow = String.valueOf(stringBuilder1);
        for(i=0;i<symptoms.size();i++){
            String date = symptoms.get(i).getDate();
            String lines[] = date.split("\\r?\\n");
            if(lines[1].equals(dateNow)){
                br++;
            }
        }
        this.txNumberOfSymptomLogs.setText(String.valueOf(br));
    }


    public void setUpListView(){
        this.lvNews = (ListView) this.findViewById(R.id.lvHealthNews);
        if(Utils.connectivity(getApplicationContext()))
        {
        obj = new HandleXML(finalUrl);
        obj.fetchXML();

        while(obj.parsingComplete);
        if(obj.getsFailedMessage().equals("OK")) {
        links=obj.getLinks();
        descriptions=obj.getDescriptions();
        titles=obj.getTitles();
        pubDates=obj.getPubDates();
        images = obj.getImages();
        int i=0;
        List<News> news = new ArrayList<>();
        for(i=2;i<titles.size();i++){
            try {
                if (titles.get(i) != null && descriptions.get(i) != null && links.get(i) != null && pubDates.get(i - 2) != null && images.get(i) != null)
                    news.add(new News(titles.get(i), descriptions.get(i - 2), links.get(i), pubDates.get(i - 2), images.get(i - 2)));
            }
            catch(Exception e){
                    Log.e("Error", String.valueOf(i));
                }
        }
        this.newsAdapter = new NewsAdapter(news);
        this.lvNews.setAdapter(this.newsAdapter);
        this.lvNews.setOnItemClickListener(this);
        }
        else{
            txConnectionTimeout.setVisibility(View.VISIBLE);
            txConnectionTimeout.setText(R.string.connectionTimeoutMessage);
        }
        }
        else
        {
            txConnectivity.setVisibility(View.VISIBLE);
            txConnectivity.setText(R.string.noConnectivityWarning);
            Toast.makeText(getApplicationContext(), "Unable to read status feed.", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        NewsAdapter adapter = (NewsAdapter) parent.getAdapter();
        News element = (News) adapter.getItem(position);
        Uri uri = Uri.parse(element.getLink());
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }



    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent intent;
        if (id == R.id.nav_symptomLog) {
            intent = new Intent(this, SymptomLogActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_bodyLog) {
            intent = new Intent(this, BodyLogActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_statistics) {
            intent = new Intent(this,BodyStatistics.class);
            startActivity(intent);
        } else if (id == R.id.nav_symptomHistory) {
            intent = new Intent(this, SymptomsHistory.class);
            startActivity(intent);
        } else if (id == R.id.nav_bodyHistory) {
            intent = new Intent(this, BodyLogsHistory.class);
            startActivity(intent);
        } else if (id == R.id.nav_HealthInstitutions) {
            intent = new Intent(this, HealthInstitutions.class);
            startActivity(intent);

        } else if (id == R.id.nav_TherapyReminder) {
            intent = new Intent(this,TherapyReminder.class);
            startActivity(intent);

        } else if (id == R.id.nav_CameraLog) {
            intent = new Intent(this, CameraLogsActivity.class);
            startActivity(intent);

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        //Close the specified drawer by animating it out of view.
        //Push object to x-axis position at the start of its container, not changing its size.
        return true;
    }
}
