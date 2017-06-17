package com.example.arturrinkis.universitystudentrating;


import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class InstuctionFragment extends Fragment {
    private String INSTRUCTION_FRAGMENT_TAG = "INSTRUCTION_FRAGMENT_TAG";
    private View view = null;


    public InstuctionFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
        view =  inflater.inflate(R.layout.fragment_instuction, container, false);

        initMainHeaderTextView();
        hideMainContent();
        showLoadingAnimation();

        initControls();

        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void initControls() {
        try {
            hideLoadingAnimation();
            showMainContent();
        } catch (NullPointerException e) {
            Log.e("asynch task", "Activity has been destroyed");
        }
    }

    private void initMainHeaderTextView(){
        TextView main_header_tv = (TextView)getActivity().findViewById(R.id.main_header_tv);
        main_header_tv.setText("Instruction");
    }


    private void showLoadingAnimation() {
        RelativeLayout loading_layout = (RelativeLayout) getActivity().findViewById(R.id.loading_layout);
        loading_layout.setVisibility(View.VISIBLE);
    }

    private void hideMainContent() {
        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.content_layout);
        linearLayout.setVisibility(View.GONE);
    }

    private void hideLoadingAnimation() {
        RelativeLayout loading_layout = (RelativeLayout) getActivity().findViewById(R.id.loading_layout);
        loading_layout.setVisibility(View.GONE);
    }

    private void showMainContent(){
        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.content_layout);
        linearLayout.setVisibility(View.VISIBLE);
    }

}
