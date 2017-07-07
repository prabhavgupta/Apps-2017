package hr.ferit.mdudjak.healthdiary;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BodyLogsHistory extends AppCompatActivity implements AdapterView.OnItemClickListener {

    ListView lvBodyLogsList;
    BodyLogsAdapter mBodyLogsAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_body_logs_history);
        this.setUpUI();
    }

    private void setUpUI() {
        this.lvBodyLogsList = (ListView) this.findViewById(R.id.lvBodyLogsHistory);
        ArrayList<BodyLog> bodyLogs = DBHelper.getInstance(this).getAllBodyLogs();
        Collections.reverse(bodyLogs);
        this.mBodyLogsAdapter = new BodyLogsAdapter(bodyLogs,getApplicationContext());
        this.lvBodyLogsList.setAdapter(mBodyLogsAdapter);
        this.lvBodyLogsList.setOnItemClickListener(this);
        this.lvBodyLogsList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(BodyLogsHistory.this);
                dialogBuilder.setMessage("Do you want to delete body info log?");
                dialogBuilder.setCancelable(true);

                dialogBuilder.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                DBHelper.getInstance(getApplicationContext()).deleteBodyLog((BodyLog) mBodyLogsAdapter.getItem(position));
                                mBodyLogsAdapter.deleteAt(position);
                                dialog.cancel();
                            }
                        });

                dialogBuilder.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alertDialog = dialogBuilder.create();
                alertDialog.show();
                return true;
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        BodyLog bodyLog = (BodyLog) mBodyLogsAdapter.getItem(position);

        String pictureURL = null;
        ArrayList<CameraLog> cameraLogs = DBHelper.getInstance(this).getAllCameraLogs();
        for (int i = 0; i < cameraLogs.size(); i++) {
            if (cameraLogs.get(i).getPictureDate().equals(bodyLog.getDate())) {
                pictureURL = cameraLogs.get(i).getPictureURL();
                break;
            }
        }
        if (pictureURL!=null) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(pictureURL));
            List<ResolveInfo> resolvedIntentActivities = getApplicationContext().getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);

            for (ResolveInfo resolvedIntentInfo : resolvedIntentActivities) {
                String packageName = resolvedIntentInfo.activityInfo.packageName;

                getApplicationContext().grantUriPermission(packageName, Uri.parse(pictureURL), Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }
            startActivity(intent);
        }
    }
}
