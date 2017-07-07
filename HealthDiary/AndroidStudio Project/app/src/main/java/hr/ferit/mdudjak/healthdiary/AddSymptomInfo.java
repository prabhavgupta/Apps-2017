package hr.ferit.mdudjak.healthdiary;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;

public class AddSymptomInfo extends AppCompatActivity implements View.OnClickListener,AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener{
    ListView listView;
    ImageButton bAddInfo;
    ImageButton ibSearchInfo;
    EditText etAddInfo;
    EditText etSearchInfo;
    ArrayList<String> areas;
    ArrayList<String> descriptions;
    ArrayAdapter<String> areaItemsAdapter;
    ArrayAdapter<String> descriptionsItemsAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent startingIntent = this.getIntent();
        if(startingIntent.hasExtra(SymptomLogActivity.KEY_REQUEST_AREA)) {
            setContentView(R.layout.activity_add_symptom_area);
            setUpAreaInputUI();
        }
        if(startingIntent.hasExtra(SymptomLogActivity.KEY_REQUEST_DESCRIPTION)){
            setContentView(R.layout.activity_add_symptom_description);
            setUpDescriptionInputUI();
        }
    }

    private void setUpAreaInputUI() {
        this.listView= (ListView) this.findViewById(R.id.areaList);
        this.etAddInfo = (EditText) this.findViewById(R.id.etAddArea);
        this.etSearchInfo= (EditText) this.findViewById(R.id.etSearchArea);
        areas =new ArrayList<String>();
        this.areas = DBHelper.getInstance(this).getAllAreas();
        areaItemsAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,areas);
        this.listView.setAdapter(areaItemsAdapter);
        this.listView.setOnItemClickListener(this);
        this.listView.setOnItemLongClickListener(this);
        this.bAddInfo= (ImageButton) this.findViewById(R.id.ibAddArea);
        this.bAddInfo.setOnClickListener(this);
        this.ibSearchInfo = (ImageButton) this.findViewById(R.id.ibSearchArea);
        this.ibSearchInfo.setOnClickListener(this);
    }
    private void setUpDescriptionInputUI() {
        this.listView= (ListView) this.findViewById(R.id.descriptionList);
        this.etAddInfo= (EditText) this.findViewById(R.id.etAddDescription);
        this.etSearchInfo= (EditText) this.findViewById(R.id.etSearchDescription);
        descriptions = new ArrayList<String>();
        this.descriptions = DBHelper.getInstance(this).getAllDescriptions();
        descriptionsItemsAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,descriptions);
        this.listView.setAdapter(descriptionsItemsAdapter);
        this.listView.setOnItemClickListener(this);
        this.listView.setOnItemLongClickListener(this);
        this.bAddInfo= (ImageButton) this.findViewById(R.id.ibAddDescription);
        this.bAddInfo.setOnClickListener(this);
        this.ibSearchInfo = (ImageButton) this.findViewById(R.id.ibSearchDescription);
        this.ibSearchInfo.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        InputMethodManager imm = (InputMethodManager) getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
        int i;
        switch (v.getId()) {
            case (R.id.ibAddArea):
                String area = etAddInfo.getText().toString();
                areas.add(area);
                DBHelper.getInstance(getApplicationContext()).insertArea(area);
                areaItemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, areas);
                this.listView.setAdapter(areaItemsAdapter);
                //Maknuti tipkovnicu
                break;

            case (R.id.ibAddDescription):
                String description = etAddInfo.getText().toString();
                descriptions.add(description);
                DBHelper.getInstance(getApplicationContext()).insertDescription(description);
                descriptionsItemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, descriptions);
                this.listView.setAdapter(descriptionsItemsAdapter);
                //Maknuti tipkovnicu
                break;

            case (R.id.ibSearchArea):
                String searchArea = this.etSearchInfo.getText().toString();
                if(!(searchArea.equals(""))) {
                ArrayList<String> areasBySearchTerm = new ArrayList<String>();
                for (i = 0; i < areas.size(); i++) {
                    if (areas.get(i).equals(searchArea)) areasBySearchTerm.add(areas.get(i));
                }
                ArrayAdapter<String> areaFilteredItemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, areasBySearchTerm);
                this.listView.setAdapter(areaFilteredItemsAdapter);
                }
                else{
                    ArrayAdapter<String> areaFilteredItemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, areas);
                    this.listView.setAdapter(areaFilteredItemsAdapter);
                }
                break;
            case (R.id.ibSearchDescription):
                String searchDescription = this.etSearchInfo.getText().toString();
                if(!(searchDescription.equals(""))) {
                    ArrayList<String> descriptionsBySearchTerm = new ArrayList<String>();
                    for (i = 0; i < descriptions.size(); i++) {
                        if (descriptions.get(i).equals(searchDescription))
                            descriptionsBySearchTerm.add(descriptions.get(i));
                    }
                    ArrayAdapter<String> descriptionFilteredItemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, descriptionsBySearchTerm);
                    this.listView.setAdapter(descriptionFilteredItemsAdapter);
                }
                else{
                    ArrayAdapter<String> descriptionFilteredItemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, descriptions);
                    this.listView.setAdapter(descriptionFilteredItemsAdapter);
                }
                break;
        }


    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        ArrayAdapter<String> adapter = (ArrayAdapter<String>) parent.getAdapter();
        final String result = adapter.getItem(position).toString();
        final Intent resultIntent = new Intent();
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setCancelable(true);
        dialogBuilder.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });


        switch (parent.getId()) {
            case(R.id.areaList):
                dialogBuilder.setMessage(R.string.AreaLogDialogMessage);
                dialogBuilder.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                resultIntent.putExtra(SymptomLogActivity.AREA_RESULT, result);
                                AddSymptomInfo.this.setResult(RESULT_OK, resultIntent);
                                AddSymptomInfo.this.finish();
                                dialog.cancel();
                            }
                        });
                break;
            case(R.id.descriptionList):
                dialogBuilder.setMessage(R.string.DescriptionLogDialogMessage);
                dialogBuilder.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                resultIntent.putExtra(SymptomLogActivity.DESCRIPTION_RESULT, result);
                                AddSymptomInfo.this.setResult(RESULT_OK, resultIntent);
                                AddSymptomInfo.this.finish();
                                dialog.cancel();
                            }
                        });
                break;
        }
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        switch(parent.getId()){
            case(R.id.areaList):
                android.support.v7.app.AlertDialog.Builder dialogBuilder = new android.support.v7.app.AlertDialog.Builder(AddSymptomInfo.this);
                dialogBuilder.setMessage("Do you want to delete selected area?");
                dialogBuilder.setCancelable(true);

                dialogBuilder.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                DBHelper.getInstance(getApplicationContext()).deleteArea((String) areaItemsAdapter.getItem(position));
                                areaItemsAdapter.remove(areaItemsAdapter.getItem(position));
                                areaItemsAdapter.notifyDataSetChanged();
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
                android.support.v7.app.AlertDialog alertDialog = dialogBuilder.create();
                alertDialog.show();
                break;

            case(R.id.descriptionList):
                android.support.v7.app.AlertDialog.Builder dialogBuilder2 = new android.support.v7.app.AlertDialog.Builder(AddSymptomInfo.this);
                dialogBuilder2.setMessage("Do you want to delete selected description?");
                dialogBuilder2.setCancelable(true);
                dialogBuilder2.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                DBHelper.getInstance(getApplicationContext()).deleteDescription((String) descriptionsItemsAdapter.getItem(position));
                                descriptionsItemsAdapter.remove(descriptionsItemsAdapter.getItem(position));
                                descriptionsItemsAdapter.notifyDataSetChanged();
                                dialog.cancel();
                            }
                        });

                dialogBuilder2.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                android.support.v7.app.AlertDialog alertDialog2 = dialogBuilder2.create();
                alertDialog2.show();
                break;
        }
        return true;
    }
}
