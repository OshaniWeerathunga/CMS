package com.example.cms.Leeds;

import static com.example.cms.LoginActivity.Username;

import android.app.ProgressDialog;
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

import com.example.cms.Adapter.LeedsTableAdapter;
import com.example.cms.HelperClass.PostRequest;
import com.example.cms.LoginActivity;
import com.example.cms.Models.LeedsTableModel;
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

public class LeedsTablesLayout extends AppCompatActivity {


    String loadUrl = "http://cms.fintrex.lk/ro_dashboard/allocated_contracts_data?";
    String ServerLogoutURL = "http://cms.fintrex.lk/logout?";
    URL url;
    String finalResult;
    HashMap<String,String> hashMap = new HashMap<>();
    ProgressDialog progressDialog;

    public List<Arrays> hotleedsdata = new ArrayList<Arrays>();

    String passingData,profilename,topic;
    TextView leedsTopic,profilenameTv,logout,newlead;

    public static String id,nic,name,mobile,product,followup,address,userid;

    RecyclerView recyclerView;
    List<LeedsTableModel> list = new ArrayList<>();
    LeedsTableAdapter adapter;
    ImageView back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leeds_table_layout);

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
        topic = getIntent().getStringExtra("leedsName");
        leedsTopic.setText(topic);

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

        //new lead
        newlead = findViewById(R.id.newlead);
        newlead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), NewLeadLayout.class);
                intent.putExtra("topic", "New Lead");
                startActivity(intent);

            }
        });

    }

    public void GetLeedsDataFunction(String date){

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

                    //hotleedsdata = new ArrayList<Arrays>();

                    for(int i=0; i<array.length();i++){
                        JSONArray jarray = array.getJSONArray(i);

                        id = jarray.getString(0);
                        nic = jarray.getString(1);
                        name = jarray.getString(2);
                        mobile = jarray.getString(3);
                        product = jarray.getString(5);
                        followup = jarray.getString(6);
                        address = jarray.getString(7);
                        userid = jarray.getString(8);

                        LeedsTableModel leedsTableModel = new LeedsTableModel(id,nic,name,mobile,product,followup,address,userid,topic);
                        list.add(leedsTableModel);

                    }
                    adapter = new LeedsTableAdapter(LeedsTablesLayout.this,list);
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
                hashMap.put("d",params[0]);

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

                progressDialog = new ProgressDialog(LeedsTablesLayout.this);
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
