package com.example.cms.Leeds;

import static com.example.cms.LoginActivity.Username;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cms.Adapter.OtherTableAdapter;
import com.example.cms.HelperClass.PostRequest;
import com.example.cms.LoginActivity;
import com.example.cms.Models.OtherTableModel;
import com.example.cms.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class OtherTablesLayout extends AppCompatActivity {

    String EachUserLeedsloadUrl = "http://192.168.40.7:8080/cms/ro_dashboard/total-leasing?";
    String ServerLogoutURL = "http://192.168.40.7:8080/cms/logout?";
    URL url;
    String finalResult;
    HashMap<String,String> hashMap = new HashMap<>();
    ProgressDialog progressDialog;

    public List<Arrays> tabledata = new ArrayList<Arrays>();
    public List<Arrays> statusarray = new ArrayList<Arrays>();

    String passingData,profilename,topic;
    TextView topicTv,profilenameTv,logout;
    ImageView back;

    public static String id,customer,asset,product,financeAmt,rate,tenor,status,userid;
    public static String statusTo,statusDate,statusFrom;

    RecyclerView recyclerView;
    List<OtherTableModel> list = new ArrayList<>();
    OtherTableAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_tables);

        //get profile name
        profilename = Username;
        profilenameTv = findViewById(R.id.profilename);
        profilenameTv.setText("  " + profilename);

        //back button
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //lstStockItems = findViewById(R.id.lstStockItems);
        topicTv = findViewById(R.id.topic);

        passingData = getIntent().getStringExtra("data");
        topic = getIntent().getStringExtra("topic");
        topicTv.setText(topic);

        recyclerView = findViewById(R.id.dataTableRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        //FillList();
        GetDataFunction(passingData);

        //define logout button
        logout = findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
    }

    public void GetDataFunction(String date){

        class ColdLeedsFunctionClass extends AsyncTask<String,Void,String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);
                System.out.println(httpResponseMsg);


                    try {
                        JSONObject jsonObject = new JSONObject(httpResponseMsg);
                        JSONArray array = jsonObject.getJSONArray("rows");

                        tabledata = new ArrayList<Arrays>();

                        for (int i = 0; i < array.length(); i++) {
                            JSONArray jarray = array.getJSONArray(i);

                            id = jarray.getString(1);
                            customer = jarray.getString(2);
                            asset = jarray.getString(3);
                            product = jarray.getString(4);
                            financeAmt = jarray.getString(5);
                            rate = jarray.getString(6);
                            tenor = jarray.getString(7);
                            userid = jarray.getString(0);
                            status = jarray.getString(8);

                            //get status values
                            if (!status.equals("null")) {
                                JSONObject jsonObjectstatus = new JSONObject(status);
                                statusDate = jsonObjectstatus.getString("rec_date");
                                statusTo = jsonObjectstatus.getString("r_to");
                                statusFrom = jsonObjectstatus.getString("r_from");

                                status = statusDate + " " + statusFrom + " Recommended to " + statusTo;

                            }else{
                                status = "null";
                            }

                            OtherTableModel otherTableModel = new OtherTableModel(id, customer, asset, product, financeAmt, rate, tenor, status, userid, topic);
                            list.add(otherTableModel);

                        }

                        adapter = new OtherTableAdapter(OtherTablesLayout.this, list);
                        recyclerView.setAdapter(adapter);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


            }

            @Override
            protected String doInBackground(String... params) {

                try {
                    url = new URL(EachUserLeedsloadUrl);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                hashMap.put("st",params[0]);

                finalResult = PostRequest.postRequest(url,hashMap);

                return finalResult;
            }
        }

        ColdLeedsFunctionClass coldleedsFunctionClass = new ColdLeedsFunctionClass();
        coldleedsFunctionClass.execute(date);
    }

    public void logout(){
        class LogoutFunctionClass extends AsyncTask<String,Void,String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                progressDialog = new ProgressDialog(OtherTablesLayout.this);
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

}