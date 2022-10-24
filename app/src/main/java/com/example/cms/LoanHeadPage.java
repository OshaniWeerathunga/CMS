package com.example.cms;

import static android.widget.Toast.LENGTH_LONG;
import static com.example.cms.LoginActivity.Username;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cms.HelperClass.PostRequest;
import com.example.cms.Leasing.LeasingTableLayout;
import com.example.cms.Loan.LoanTableLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

public class LoanHeadPage extends AppCompatActivity {

    LinearLayout inprogress,pendingActivation,active,rejected;
    TextView profilenameTv,inprogressTV,pendingActTV,activeTV,rejectedTV;
    String profilename,inprogressValue="0",pendingActivationValue="0",activeValue="0",rejectedValue="0";
    ImageView back;
    ProgressDialog progressDialog;

    String loadloanHeadDataUrl = "http://cms.fintrex.lk/loan/cards";
    String ServerLogoutURL = "http://cms.fintrex.lk/logout?";
    URL url;
    String finalResult;
    HashMap<String,String> hashMap = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan_head_page);

        //get profile name
        profilename = Username;
        profilenameTv = findViewById(R.id.profilename);
        profilenameTv.setText("  " + profilename);

        //back button
        back = findViewById(R.id.backlease);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        GetLeaseDataFunction();

        inprogress = findViewById(R.id.progressLoanLayout);
        pendingActivation = findViewById(R.id.pendingLoanLayout);
        active = findViewById(R.id.activeLoanLayout);
        rejected = findViewById(R.id.rejectLoanLayout);

        inprogressTV = findViewById(R.id.inprogressLoanCount);
        pendingActTV = findViewById(R.id.pendingLoancount);
        activeTV = findViewById(R.id.activeLoancount);
        rejectedTV = findViewById(R.id.rejectedLoancount);


        inprogress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoanTableLayout.class);
                intent.putExtra("data","in progress");
                startActivity(intent);

            }
        });

        pendingActivation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoanTableLayout.class);
                intent.putExtra("data","pending activation");
                startActivity(intent);

            }
        });

        active.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoanTableLayout.class);
                intent.putExtra("data","active");
                startActivity(intent);

            }
        });

        rejected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoanTableLayout.class);
                intent.putExtra("data","rejected");
                startActivity(intent);

            }
        });


    }


    public void GetLeaseDataFunction(){

        class LoanFunctionClass extends AsyncTask<String,Void,String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                progressDialog = new ProgressDialog(LoanHeadPage.this);
                progressDialog.show();
                progressDialog.setContentView(R.layout.progress_layout);
                progressDialog.getWindow().setBackgroundDrawableResource(
                        android.R.color.transparent
                );
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);

                progressDialog.dismiss();

                System.out.println(httpResponseMsg);


                /*
                try {
                    JSONObject jsonObject = new JSONObject(httpResponseMsg);
                    if(jsonObject!=null) {


                        inprogressValue = jsonObject.getString("cnt");
                        //creditValue = jsonObject.getString("cold");
                        //deviationValue = jsonObject.getString("warm");
                        //pendingValue = jsonObject.getString("prospecting");
                        //activeValue = jsonObject.getString("prospecting");
                        //rejectedValue = jsonObject.getString("prospecting");


                    }
                    else{
                        Toast.makeText(LeaseHeadPage.this,"Cannot Load Data.Please Check your connection", Toast.LENGTH_LONG).show();
                    }

                }catch (JSONException e) {
                    e.printStackTrace();

                }

                 */

                try {
                    JSONObject jsonObject = new JSONObject(httpResponseMsg);
                    JSONArray jsonArray = new JSONArray(jsonObject.getString("data"));

                    for(int i=0;i<jsonArray.length();i++){
                        if(jsonArray.getJSONObject(i).getString("status").equals("active")){
                            activeValue = jsonArray.getJSONObject(i).getString("val");
                        }else if (jsonArray.getJSONObject(i).getString("status").equals("in progress")){
                            inprogressValue = jsonArray.getJSONObject(i).getString("val");
                        }else if (jsonArray.getJSONObject(i).getString("status").equals("pending activation")){
                            pendingActivationValue = jsonArray.getJSONObject(i).getString("val");
                        }else if (jsonArray.getJSONObject(i).getString("status").equals("rejected")){
                            rejectedValue = jsonArray.getJSONObject(i).getString("val");
                        }
                    }

                    inprogressTV.setText(inprogressValue);
                    pendingActTV.setText(pendingActivationValue);
                    activeTV.setText(activeValue);
                    rejectedTV.setText(rejectedValue);




                }catch (JSONException e) {
                    e.printStackTrace();
                }





            }

            @Override
            protected String doInBackground(String... params) {

                try {
                    url = new URL(loadloanHeadDataUrl);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

                finalResult = PostRequest.getData(url);

                return finalResult;
            }
        }

        LoanFunctionClass loanFunctionClass = new LoanFunctionClass();
        loanFunctionClass.execute();
    }

    public void logout(){
        class LogoutFunctionClass extends AsyncTask<String,Void,String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                progressDialog = new ProgressDialog(LoanHeadPage.this);
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

                if(httpResponseMsg.equals("ok") || (httpResponseMsg.equals("auth_fail"))){

                    Toast.makeText(getApplicationContext(), "Successfully Logout", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                    finish();

                }else {
                    Toast.makeText(getApplicationContext(), "Please Signout again...", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            protected String doInBackground(String... params) {

                try {
                    url = new URL(ServerLogoutURL);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

                finalResult = PostRequest.logout(url);

                return finalResult;
            }
        }

        LogoutFunctionClass logoutFunctionClass = new LogoutFunctionClass();
        logoutFunctionClass.execute();

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
        startActivity(intent);
        finish();
    }
}