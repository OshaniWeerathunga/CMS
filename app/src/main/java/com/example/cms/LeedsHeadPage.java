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
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cms.HelperClass.PostRequest;
import com.example.cms.Leeds.LeedsTablesLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LeedsHeadPage extends AppCompatActivity {

    LinearLayout hotleeds,warmleeds,coldleeds,prospectleeds,lvItem;
    TextView leedsTopic,hotcountTv,warmcountTv,coldcountTv,prospectcountTv;
    String hot,warm,cold,prospect;
    ProgressDialog progressDialog;
    ImageView back;

    String LeaseDataURL = "http://192.168.40.7:8080/cms/ro_dashboard/allocated_contracts_count?";
    String PrecentageUrl = "http://192.168.40.7:8080/cms/company?";
    String loadUrl = "http://192.168.40.7:8080/cms/ro_dashboard/allocated_contracts_data?";
    String ServerLogoutURL = "http://192.168.40.7:8080/cms/logout?";
    URL url;
    String finalResult;
    HashMap<String,String> hashMap = new HashMap<>();

    String cm,ytd,npl,cmprecent,ytdprecent,nplprecent;
    TextView profilenameTv,logout,cmTv,ytdTv,nplTv,cmperecentvalue,ytdpercentvalue,nplpercentvalue;
    ProgressBar cmPro, ytdPro, nplPro;


    public List<Arrays> hotleedsdata = new ArrayList<Arrays>();
    String profilename,hotpara = "3",coldpara = "1",warmpara = "2",prospectpara = "4";
    String id,nic,name,mobile,product,followup,address,userid;
    List<Map<String, String>> prolist = new ArrayList<Map<String, String>>();
    String[] from = {"Id", "Nic", "Name", "Mobile", "Product", "FollowUp", "Address"};
    int[] views = {R.id.txtId, R.id.txtNic, R.id.txtName,R.id.txtMobile, R.id.txtProduct, R.id.txtFollowup, R.id.txtAddress};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leeds_head_page);

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

        lvItem = findViewById(R.id.lvTemplate);

        hotleeds = findViewById(R.id.hotleedsLayout);
        warmleeds = findViewById(R.id.warmleedsLayout);
        coldleeds = findViewById(R.id.coldleedsLayout);
        prospectleeds = findViewById(R.id.prospectleedsLayout);

        hotcountTv = findViewById(R.id.hotleedscount);
        warmcountTv = findViewById(R.id.warmleedscount);
        coldcountTv = findViewById(R.id.coldleedscount);
        prospectcountTv = findViewById(R.id.prospectleedscount);

        //summary textview identify
        cmTv = findViewById(R.id.cmValue);
        ytdTv = findViewById(R.id.ytdValue);
        nplTv = findViewById(R.id.nplValue);

        //percentage identify
        cmperecentvalue = findViewById(R.id.cmPrecentage);
        ytdpercentvalue = findViewById(R.id.ytdPrecentage);
        nplpercentvalue = findViewById(R.id.nplPrecentage);

        //progressbar identify
        cmPro = findViewById(R.id.cmprogressBar);
        ytdPro = findViewById(R.id.ytdprogressBar);
        nplPro = findViewById(R.id.nplprogressBar);

        GetLeaseDataFunction();

        hotleeds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LeedsTablesLayout.class);
                intent.putExtra("data",hotpara);
                intent.putExtra("leedsName","Hot Leeds");
                startActivity(intent);
            }
        });

        coldleeds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LeedsTablesLayout.class);
                intent.putExtra("data",coldpara);
                intent.putExtra("leedsName","Cold Leeds");
                startActivity(intent);

            }
        });

        warmleeds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LeedsTablesLayout.class);
                intent.putExtra("data",warmpara);
                intent.putExtra("leedsName","Warm Leeds");
                startActivity(intent);
            }
        });

        prospectleeds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LeedsTablesLayout.class);
                intent.putExtra("data",prospectpara);
                intent.putExtra("leedsName","Prospecting Leeds");
                startActivity(intent);
            }
        });

        //define logout button
        logout = findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

    }



    public void GetLeaseDataFunction(){

        class LeaseFunctionClass extends AsyncTask<String,Void,String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                progressDialog = new ProgressDialog(LeedsHeadPage.this);
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


                try {
                    JSONObject jsonObject = new JSONObject(httpResponseMsg);
                    if(jsonObject!=null) {

                        hot = jsonObject.getString("hot");
                        cold = jsonObject.getString("cold");
                        warm = jsonObject.getString("warm");
                        prospect = jsonObject.getString("prospecting");

                        hotcountTv.setText(hot);
                        warmcountTv.setText(warm);
                        coldcountTv.setText(cold);
                        prospectcountTv.setText(prospect);
                    }
                    else{
                        Toast.makeText(LeedsHeadPage.this,"Cannot Load Data.Please Check your connection", Toast.LENGTH_LONG).show();
                    }

                }catch (JSONException e) {
                    e.printStackTrace();

                }
            }

            @Override
            protected String doInBackground(String... params) {

                try {
                    url = new URL(LeaseDataURL);
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