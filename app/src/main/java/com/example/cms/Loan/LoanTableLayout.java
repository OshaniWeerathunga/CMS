package com.example.cms.Loan;

import static com.example.cms.LoginActivity.Username;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cms.Adapter.LeaseTableAdapter;
import com.example.cms.Adapter.LoanTableAdapter;
import com.example.cms.HelperClass.PostRequest;
import com.example.cms.LoginActivity;
import com.example.cms.Models.LeaseTableModel;
import com.example.cms.Models.LoanTableModel;
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

public class LoanTableLayout extends AppCompatActivity {

    String loadUrl = "http://cms.fintrex.lk/API/Loan";
    String ServerLogoutURL = "http://cms.fintrex.lk/logout?";
    URL url;
    String finalResult;
    HashMap<String,String> hashMap = new HashMap<>();
    HashMap<String,JSONObject> jsonhashMap = new HashMap<>();

    public List<Arrays> hotleedsdata = new ArrayList<Arrays>();

    String passingData,profilename,topic;
    TextView leedsTopic,profilenameTv,logout,newlead;

    public static String userid,applicationNo,customer,product,asset,action,status,proposal,lastaction;

    RecyclerView recyclerView;
    List<LoanTableModel> list = new ArrayList<>();
    LoanTableAdapter adapter;
    ImageView back;
    JSONObject data;
    JSONArray array1,array2,array3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan_table_layout);

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
        leedsTopic = findViewById(R.id.leedstopic);

        passingData = getIntent().getStringExtra("data");
        leedsTopic.setText(passingData);


        recyclerView = findViewById(R.id.coldrecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        //FillList();
        GetLeedsDataFunction(passingData);

        //define logout button
        logout = findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

    }

    //load data into tables
    public void GetLeedsDataFunction(String date){

        class ColdLeedsFunctionClass extends AsyncTask<String,Void,String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);
                System.out.println("loan table data ----- "+httpResponseMsg);


                try {
                    JSONObject jsonObject = new JSONObject(httpResponseMsg);
                    JSONArray array = jsonObject.getJSONArray("rows");

                    //hotleedsdata = new ArrayList<Arrays>();

                    for(int i=0; i<array.length();i++){
                        JSONArray jarray = array.getJSONArray(i);

                        userid = jarray.getString(0);
                        applicationNo = jarray.getString(1);
                        proposal = jarray.getString(2);
                        customer = jarray.getString(3);
                        product = jarray.getString(4);
                        lastaction = jarray.getString(5);
                        status = jarray.getString(6);

                        LoanTableModel loanTableModel = new LoanTableModel(userid,applicationNo,proposal,customer,product,lastaction,status);
                        list.add(loanTableModel);

                    }
                    adapter = new LoanTableAdapter(LoanTableLayout.this,list);
                    recyclerView.setAdapter(adapter);


                }catch (JSONException e) {
                    e.printStackTrace();
                }




            }

            @Override
            protected String doInBackground(String... params) {

                try {
                    url = new URL(loadUrl);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                hashMap.put("status",params[0]);
                System.out.println(hashMap);

                finalResult = PostRequest.postRequest(url,hashMap);

                return finalResult;
            }
        }

        ColdLeedsFunctionClass coldleedsFunctionClass = new ColdLeedsFunctionClass();
        coldleedsFunctionClass.execute(date);
    }



    //logout
    public void logout(){
        class LogoutFunctionClass extends AsyncTask<String,Void,String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

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