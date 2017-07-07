package hr.ferit.mdudjak.healthdiary;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;

public class SymptomsHistory extends AppCompatActivity{

    ListView lvSymtpomsList;
    SymptomsAdapter mSymptomsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_symptoms_history);
        this.setUpUI();
    }

    private void setUpUI() {
        this.lvSymtpomsList = (ListView) this.findViewById(R.id.lvSymptomsHistory);
        ArrayList<Symptom> symptoms = DBHelper.getInstance(this).getAllSymptoms();
        Collections.reverse(symptoms);
        mSymptomsAdapter = new SymptomsAdapter(symptoms,this);
        this.lvSymtpomsList.setAdapter(mSymptomsAdapter);
        this.lvSymtpomsList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(SymptomsHistory.this);
                dialogBuilder.setMessage("Do you want to delete symptom log?");
                dialogBuilder.setCancelable(true);

                dialogBuilder.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                DBHelper.getInstance(getApplicationContext()).deleteSymptom((Symptom) mSymptomsAdapter.getItem(position));
                                mSymptomsAdapter.deleteAt(position);
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

}




