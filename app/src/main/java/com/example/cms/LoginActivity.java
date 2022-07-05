package com.example.cms;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.cms.HelperClass.PostRequest;
import com.example.cms.Leeds.Calculator;
import com.example.cms.Leeds.OtherTablesLayout;
import com.example.cms.Leeds.SanctionLetterDisplay;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {

    EditText username, password;
    ImageView loginBack;
    Button login;

    String UsernameHolder,PasswordHolder;
    ProgressDialog progressDialog;

    boolean CheckEditText ;
    String finalResult ;
    HashMap<String,String> hashMap = new HashMap<>();
    URL url;
    String ServerLoginURL = "http://192.168.40.7:8080/cms/login?";
    public static String Username = "username";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username=findViewById(R.id.username);
        password=findViewById(R.id.password);
        login=findViewById(R.id.signin_btn);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Intent intent = new Intent(LoginActivity.this,DashboardActivity.class);
                //startActivity(intent);
                //finish();

                GetCheckEditTextIsEmptyOrNot();

                if(CheckEditText){
                    LoginFunction(UsernameHolder,PasswordHolder);
                }
                else {

                    Toast.makeText(LoginActivity.this, "Please fill all form fields.", Toast.LENGTH_LONG).show();
                }

            }
        });

    }


    public void GetCheckEditTextIsEmptyOrNot(){

        //NameHolder = name.getText().toString();
        UsernameHolder = username.getText().toString().trim();
        PasswordHolder = password.getText().toString().trim();

        if(!validateUsername() | !validatePassword())
        {

            CheckEditText = false;

        }
        else {

            CheckEditText = true ;
        }

    }

    public void LoginFunction(final String username, final String password){

        class LoginFunctionClass extends AsyncTask<String,Void,String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                progressDialog = new ProgressDialog(LoginActivity.this);
                progressDialog.show();
                progressDialog.setContentView(R.layout.progress_layout);
                progressDialog.getWindow().setBackgroundDrawableResource(
                        android.R.color.transparent
                );

            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);
                System.out.println(httpResponseMsg);

                progressDialog.dismiss();

                //Toast.makeText(LoginScreen.this, httpResponseMsg, Toast.LENGTH_LONG).show();

                try {
                    JSONObject jsonObject = new JSONObject(httpResponseMsg);
                    if (jsonObject.getString("status").equals("ok")) {

                        Username = jsonObject.getString("user");

                        Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
                        //intent.putExtra(Username,user);
                        startActivity(intent);
                        finish();

                    }
                    else {

                        Toast.makeText(LoginActivity.this, "Fields not Matched. Please Check Again", Toast.LENGTH_LONG).show();
                    }
                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(String... params) {

                try {
                    url = new URL(ServerLoginURL);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

                hashMap.put("username",params[0]);

                hashMap.put("password",params[1]);

                System.out.printf("user sent");
                System.out.println(hashMap);

                finalResult =PostRequest.postRequest(url,hashMap);

                return finalResult;
            }
        }

        LoginFunctionClass loginFunctionClass = new LoginFunctionClass();
        loginFunctionClass.execute(username,password);
    }

    private boolean validateUsername(){
        String val = username.getText().toString();

        if(val.isEmpty()){
            username.setError("Field cannot be empty");
            return false;
        }else{
            username.setError(null);
            return true;
        }

    }

    private boolean validatePassword(){
        String val = password.getText().toString();

        if(val.isEmpty()){
            password.setError("Field cannot be empty");
            return false;
        }else{
            password.setError(null);
            return true;
        }

    }
}