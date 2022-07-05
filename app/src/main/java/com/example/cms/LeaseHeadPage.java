package com.example.cms;

import static com.example.cms.LoginActivity.Username;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cms.Adapter.LeedsTableAdapter;
import com.example.cms.HelperClass.PostRequest;
import com.example.cms.Leasing.LeasingTableLayout;
import com.example.cms.Leeds.LeedsTablesLayout;
import com.example.cms.Models.LeedsTableModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

public class LeaseHeadPage extends AppCompatActivity {

    LinearLayout inprogress,creditApproval,deviationApproval,pendingVerification,insuranceConfirmation,pendingActivation,documentPending,paymentPending,active,rejected;
    TextView profilenameTv,inprogressTV,creditTV,deviationTV,pendingVeriTv,insuranceTV,pendingActTV,documentPendingTV,paymentPendingTV,activeTV,rejectedTV;
    String profilename,inprogressValue="0",creditValue="0",deviationValue="0",pendingVerifyValue="0",insuranceValue="0",pendingActivationValue="0",documentValue="0",paymentValue="0",activeValue="0",rejectedValue="0";
    ImageView back;
    ProgressDialog progressDialog;

    String loadleaseHeadDataUrl = "http://192.168.40.7:8080/cms/leasing/loadLeaseMenuCards?";
    String ServerLogoutURL = "http://192.168.40.7:8080/cms/logout?";
    URL url;
    String finalResult;
    HashMap<String,String> hashMap = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lease_head_page);

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

        inprogress = findViewById(R.id.progressLeaseLayout);
        creditApproval = findViewById(R.id.creditLeaseLayout);
        deviationApproval = findViewById(R.id.DeviationLeaseLayout);
        pendingVerification = findViewById(R.id.PendingVerificationLeaseLayout);
        insuranceConfirmation = findViewById(R.id.InsuranceConfirmationLeaseLayout);
        pendingActivation = findViewById(R.id.pendingLeaseLayout);
        documentPending = findViewById(R.id.documentPendingLeaseLayout);
        paymentPending = findViewById(R.id.paymentPendingLeaseLayout);
        active = findViewById(R.id.activeLeaseLayout);
        rejected = findViewById(R.id.rejectLeaseLayout);

        inprogressTV = findViewById(R.id.inprogressLeaseCount);
        creditTV = findViewById(R.id.creditLeasecount);
        deviationTV = findViewById(R.id.deviationLeasecount);
        pendingVeriTv = findViewById(R.id.pendingVerificationLeasecount);
        insuranceTV = findViewById(R.id.insuranceConfirmationLeasecount);
        pendingActTV = findViewById(R.id.pendingLeasecount);
        documentPendingTV = findViewById(R.id.documentPendingLeasecount);
        paymentPendingTV = findViewById(R.id.paymentPendingLeasecount);
        activeTV = findViewById(R.id.activeLeasecount);
        rejectedTV = findViewById(R.id.rejectedLeasecount);


        inprogress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LeasingTableLayout.class);
                intent.putExtra("data","in progress");
                startActivity(intent);

            }
        });
        creditApproval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LeasingTableLayout.class);
                intent.putExtra("data","Credit Approval");
                startActivity(intent);

            }
        });
        deviationApproval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LeasingTableLayout.class);
                intent.putExtra("data","Deviation Approval");
                startActivity(intent);

            }
        });
        pendingVerification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LeasingTableLayout.class);
                intent.putExtra("data","Pending Verification");
                startActivity(intent);

            }
        });
        insuranceConfirmation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LeasingTableLayout.class);
                intent.putExtra("data","Insurance Confirmation");
                startActivity(intent);

            }
        });
        pendingActivation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LeasingTableLayout.class);
                intent.putExtra("data","Pending Activation");
                startActivity(intent);

            }
        });
        documentPending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LeasingTableLayout.class);
                intent.putExtra("data","Document Pending");
                startActivity(intent);

            }
        });
        paymentPending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LeasingTableLayout.class);
                intent.putExtra("data","Payment Pending");
                startActivity(intent);

            }
        });
        active.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LeasingTableLayout.class);
                intent.putExtra("data","active");
                startActivity(intent);

            }
        });
        rejected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LeasingTableLayout.class);
                intent.putExtra("data","Rejected");
                startActivity(intent);

            }
        });


    }


    public void GetLeaseDataFunction(){

        class LeaseFunctionClass extends AsyncTask<String,Void,String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                progressDialog = new ProgressDialog(LeaseHeadPage.this);
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
                    //JSONObject jsonObject = new JSONObject(httpResponseMsg);
                    JSONArray jsonArray = new JSONArray(httpResponseMsg);

                        for(int i=0;i<jsonArray.length();i++){
                            if(jsonArray.getJSONObject(i).getString("status").equals("active")){
                                activeValue = jsonArray.getJSONObject(i).getString("cnt");
                            }else if (jsonArray.getJSONObject(i).getString("status").equals("in progress")){
                                inprogressValue = jsonArray.getJSONObject(i).getString("cnt");
                            }else if (jsonArray.getJSONObject(i).getString("status").equals("Credit Approval")){
                                creditValue = jsonArray.getJSONObject(i).getString("cnt");
                            }else if (jsonArray.getJSONObject(i).getString("status").equals("Pending Activation")){
                                pendingActivationValue = jsonArray.getJSONObject(i).getString("cnt");
                            }else if (jsonArray.getJSONObject(i).getString("status").equals("Document Pending")){
                                documentValue = jsonArray.getJSONObject(i).getString("cnt");
                            }else if (jsonArray.getJSONObject(i).getString("status").equals("Insurance Confirmation")){
                                insuranceValue = jsonArray.getJSONObject(i).getString("cnt");
                            }else if (jsonArray.getJSONObject(i).getString("status").equals("Payment Pending")){
                                paymentValue = jsonArray.getJSONObject(i).getString("cnt");
                            }else if (jsonArray.getJSONObject(i).getString("status").equals("Deviation Approval")){
                                deviationValue = jsonArray.getJSONObject(i).getString("cnt");
                            }else if (jsonArray.getJSONObject(i).getString("status").equals("Pending Verification")){
                                pendingVerifyValue = jsonArray.getJSONObject(i).getString("cnt");
                            }else if (jsonArray.getJSONObject(i).getString("status").equals("Rejected")){
                                rejectedValue = jsonArray.getJSONObject(i).getString("cnt");
                            }
                        }

                    System.out.println("active value is ----- "+insuranceValue);

                        inprogressTV.setText(inprogressValue);
                        creditTV.setText(creditValue);
                        deviationTV.setText(deviationValue);
                        pendingVeriTv.setText(pendingVerifyValue);
                        insuranceTV.setText(insuranceValue);
                        pendingActTV.setText(pendingActivationValue);
                        documentPendingTV.setText(documentValue);
                        paymentPendingTV.setText(paymentValue);
                        activeTV.setText(activeValue);
                        rejectedTV.setText(rejectedValue);




                }catch (JSONException e) {
                    e.printStackTrace();
                }



            }

            @Override
            protected String doInBackground(String... params) {

                try {
                    url = new URL(loadleaseHeadDataUrl);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

                finalResult = PostRequest.getData(url);

                return finalResult;
            }
        }

        LeaseFunctionClass leaseFunctionClass = new LeaseFunctionClass();
        leaseFunctionClass.execute();
    }

    public void logout(){
        class LogoutFunctionClass extends AsyncTask<String,Void,String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                progressDialog = new ProgressDialog(LeaseHeadPage.this);
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