package com.example.arturrinkis.universitystudentrating;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.arturrinkis.universitystudentrating.DTO.AuthUser;
import com.example.arturrinkis.universitystudentrating.ServerServiceAPI.Interfaces.IServerAPIManager;
import com.example.arturrinkis.universitystudentrating.ServerServiceAPI.ServerAPIManager;
import com.example.arturrinkis.universitystudentrating.CustomControls.SquareImageButton;

public class WelcomeActivity extends AppCompatActivity {
    IServerAPIManager serverAPIManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        serverAPIManager = new ServerAPIManager(this.getApplicationContext());
        new GetAuthStateAsyncTask().execute();
    }

    private void initControls(){
        initButtons();
    }

    //
    //
    //init buttons

    private void initButtons(){
        initLoginBtn();
        initSignupBtn();
        initCloseappBtn();
        initAsGuestBtn();
    }
    private void initLoginBtn(){
        SquareImageButton loginBtn = (SquareImageButton)findViewById(R.id.login_btn);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
    }
    private void initSignupBtn(){
        SquareImageButton signupBtn = (SquareImageButton)findViewById(R.id.signup_btn);
        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), SignUpActivity.class);
                startActivity(intent);
            }
        });
    }
    private void initCloseappBtn(){
        SquareImageButton closeappBtn = (SquareImageButton)findViewById(R.id.closeapp_btn);
        closeappBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        });
    }
    private void initAsGuestBtn(){
        SquareImageButton asguest_btn = (SquareImageButton)findViewById(R.id.asguest_btn);
        asguest_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), GuestActivity.class);
                startActivity(intent);
            }
        });
    }

    //
    //
    //database async task

    private void endAuthenticate(AuthUser authUser){
        if(authUser == null){
            setContentView(R.layout.activity_welcome);
            initControls();
        }
        else{
            Intent intent = new Intent(this, authUser.getUserProfile().getStatusId() == 1 ? StudentActivity.class : ProfessorActivity.class);
            startActivity(intent);
            finish();
        }
    }
    class GetAuthStateAsyncTask extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... params) {
            return serverAPIManager.getAuthServiceAPI().isAuthenticated();
        }
        protected void onPostExecute(Boolean isAuthenticated){
            new GetAuthUserAsyncTask().execute();
        }
    }

    class GetAuthUserAsyncTask extends AsyncTask<Void, Void, AuthUser> {
        @Override
        protected AuthUser doInBackground(Void... params) {
            return serverAPIManager.getUserService().getAuthUser(false);
        }
        protected void onPostExecute(AuthUser authUser){
            endAuthenticate(authUser);
        }
    }
}