package com.example.arturrinkis.universitystudentrating;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.arturrinkis.universitystudentrating.DTO.AuthUser;
import com.example.arturrinkis.universitystudentrating.DTO.HttpResponse;
import com.example.arturrinkis.universitystudentrating.DTO.LoginModel;
import com.example.arturrinkis.universitystudentrating.ServerServiceAPI.Interfaces.IServerAPIManager;
import com.example.arturrinkis.universitystudentrating.ServerServiceAPI.ServerAPIManager;

public class LoginActivity extends AppCompatActivity {
    IServerAPIManager serverAPIManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);serverAPIManager = new ServerAPIManager(this.getApplicationContext());
        setContentView(R.layout.activity_login);
        initControls();
    }

    private void initControls(){
        initButtons();
    }

    private void initButtons(){
        initLoginButton();
    }

    private void initLoginButton(){
        final Button loginButton = (Button)findViewById(R.id.login_btn);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginButton.setText("PLEASE WAIT...");
                LoginModel model = new LoginModel();
                EditText userName_et = (EditText)findViewById(R.id.username_et);
                EditText password_et = (EditText)findViewById(R.id.password_et);

                model.setUserName(userName_et.getText().toString());
                model.setPassword(password_et.getText().toString());

                new GetLoginAsyncTask().execute(model);
            }
        });
    }

    private void handleLoginResult(HttpResponse httpResponse) {
        final Button loginButton = (Button) findViewById(R.id.login_btn);
        if (httpResponse == null) {
            loginButton.setText("LOGIN");
            Toast.makeText(this, "Server is not responding", Toast.LENGTH_LONG).show();
        } else if (httpResponse.getCode() == 200) {
            new GetAuthUserAsyncTask().execute();
        } else {
            loginButton.setText("LOGIN");
            TextView error_messages_tv = (TextView) findViewById(R.id.error_messages_tv);
            if (httpResponse.getMessage() != "") {
                error_messages_tv.setText("");
                for (String text : httpResponse.getMessage().split("\n")) {
                    error_messages_tv.append("* " + text + "\n");
                }
                error_messages_tv.setVisibility(View.VISIBLE);
            } else {
                error_messages_tv.setText("");
                error_messages_tv.setVisibility(View.GONE);
            }
        }
    }

    private void endAuthenticate(AuthUser authUser) {
        Intent intent = new Intent(this, authUser.getUserProfile().getStatusId() == 1 ? StudentActivity.class : ProfessorActivity.class);
        startActivity(intent);
        finish();
    }

    //
    //
    //database async tasks

    class GetAuthUserAsyncTask extends AsyncTask<Void, Void, AuthUser> {
        @Override
        protected AuthUser doInBackground(Void... params) {
            return serverAPIManager.getUserService().getAuthUser(false);
        }
        protected void onPostExecute(AuthUser authUser){
            endAuthenticate(authUser);
        }
    }

    class GetLoginAsyncTask extends AsyncTask<LoginModel, Void, HttpResponse> {
        @Override
        protected HttpResponse doInBackground(LoginModel... params) {
            return serverAPIManager.getAuthServiceAPI().logIn(params[0]);
        }
        protected void onPostExecute(HttpResponse HttpResponse) {
            handleLoginResult(HttpResponse);
        }
    }
}
