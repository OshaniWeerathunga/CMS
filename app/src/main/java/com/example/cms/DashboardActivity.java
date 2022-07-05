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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cms.HelperClass.PostRequest;
import com.example.cms.Leeds.OtherTablesLayout;
import com.example.cms.Leeds.LeedsTablesLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

public class DashboardActivity extends AppCompatActivity {

    LinearLayout todayLayout,delayLayout,upcomingLayout,leedsLayout,inprogressLayout,pendingLayout,activeFacilityLayout,leaseLayout;
    TextView todayTv,delayTv,upcomingTv,leedsTv,inprogressTv,pendingTv,rejectedTv,leaseTv;
    ProgressDialog progressDialog;
    ImageView refresh;
    String today,delay,upcoming,prospected,converted,coldLeeds,hotLeeds,warmLeeds,prospectLeeds,sumLeeds,inprogress,approval,activityFacility;

    String cm,ytd,npl,cmprecent,ytdprecent,nplprecent;
    TextView profilenameTv,logout,cmTv,ytdTv,nplTv,cmperecentvalue,ytdpercentvalue,nplpercentvalue;
    ProgressBar cmPro, ytdPro, nplPro,progress1,progress2,progress3;

    String HomeURL = "http://192.168.40.7:8080/cms/ro_dashboard/allocated_contracts_count?";
    String PrecentageUrl = "http://192.168.40.7:8080/cms/company?";
    String ServerLogoutURL = "http://192.168.40.7:8080/cms/logout?";
    String profilename,date = "c1";
    URL url;
    String finalResult;
    HashMap<String,String> hashMap = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        GetDataFunction();
        //GetPrecentageDataFunction(date);

        //refresh
        refresh = findViewById(R.id.refresh);

        //get profile name
        profilename = Username;
        profilenameTv = findViewById(R.id.profilename);
        profilenameTv.setText("  " + profilename);

        //layout identify
        todayLayout = findViewById(R.id.todatTaskLayout);
        delayLayout = findViewById(R.id.delayLayout);
        upcomingLayout = findViewById(R.id.upcomingLayout);
        leedsLayout = findViewById(R.id.leedsLayout);
        inprogressLayout = findViewById(R.id.inprogressLayout);
        pendingLayout = findViewById(R.id.pendingLayout);
        activeFacilityLayout = findViewById(R.id.activeFacilityLayout);
        leaseLayout = findViewById(R.id.leaseLayout);

        //textview identify
        todayTv = findViewById(R.id.todayTask);
        delayTv = findViewById(R.id.delayTask);
        upcomingTv = findViewById(R.id.upcomingTask);
        leedsTv = findViewById(R.id.leeds);
        inprogressTv = findViewById(R.id.inprogress);
        rejectedTv = findViewById(R.id.active);
        pendingTv = findViewById(R.id.pendings);
        leaseTv = findViewById(R.id.lease);

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
        progress1 = findViewById(R.id.progressBar1);
        progress2 = findViewById(R.id.progressBar2);
        progress3 = findViewById(R.id.progressBar3);

        leedsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LeedsHeadPage.class);
                startActivity(intent);
            }
        });

        todayLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LeedsTablesLayout.class);
                intent.putExtra("data","folowup_today");
                intent.putExtra("leedsName","Today Tasks");
                startActivity(intent);
            }
        });

        delayLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LeedsTablesLayout.class);
                intent.putExtra("data","folowup_delay");
                intent.putExtra("leedsName","Delay Tasks");
                startActivity(intent);
            }
        });

        upcomingLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LeedsTablesLayout.class);
                intent.putExtra("data","folowup_upcoming");
                intent.putExtra("leedsName","Upcoming Tasks");
                startActivity(intent);
            }
        });

        leaseLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LeaseHeadPage.class);
                startActivity(intent);
            }
        });

        inprogressLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), OtherTablesLayout.class);
                intent.putExtra("data","in progress");
                intent.putExtra("topic","Inprogress Activity");
                startActivity(intent);
            }
        });

        pendingLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), OtherTablesLayout.class);
                intent.putExtra("data","pending");
                intent.putExtra("topic","Pending Approval");
                startActivity(intent);
            }
        });

        activeFacilityLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), OtherTablesLayout.class);
                intent.putExtra("data","active");
                intent.putExtra("topic","Active Facility ");
                startActivity(intent);
            }
        });

        //refresh button
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetDataFunction();
                GetPrecentageDataFunction(date);
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

    
    
    
    public void GetDataFunction(){

        class LoginFunctionClass extends AsyncTask<String,Void,String> {

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
                    if(jsonObject!=null) {
                        today = jsonObject.getString("flp");
                        delay = jsonObject.getString("delay_task");
                        upcoming = jsonObject.getString("upcoming_task");

                        hotLeeds = jsonObject.getString("hot");
                        coldLeeds = jsonObject.getString("cold");
                        warmLeeds = jsonObject.getString("warm");
                        prospectLeeds = jsonObject.getString("prospecting");

                        prospected = jsonObject.getString("convrt_p");
                        //converted = jsonObject.getString("converted");
                        inprogress = jsonObject.getString("inp");
                        approval = jsonObject.getString("pcnt");
                        activityFacility = jsonObject.getString("cnt");

                        //get sum of leeds
                        //int sum = Integer.parseInt(hotLeeds)+Integer.parseInt(coldLeeds)+Integer.parseInt(warmLeeds)+Integer.parseInt(prospectLeeds);
                        //sumLeeds = String.valueOf(sum);

                        todayTv.setText(today);
                        delayTv.setText(delay);
                        upcomingTv.setText(upcoming);
                        //leedsTv.setText(sumLeeds);
                        inprogressTv.setText(inprogress);
                        pendingTv.setText(approval);
                        //convertedTv.setText(converted);
                        rejectedTv.setText(activityFacility);
                    }
                    else{
                        Toast.makeText(DashboardActivity.this,"Cannot Load Data.Please Check your connection", Toast.LENGTH_LONG).show();
                    }


                }catch (JSONException e) {
                    e.printStackTrace();

                }


            }

            @Override
            protected String doInBackground(String... params) {

                try {
                    url = new URL(HomeURL);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

                finalResult = PostRequest.getData(url);

                return finalResult;
            }
        }

        LoginFunctionClass loginFunctionClass = new LoginFunctionClass();
        loginFunctionClass.execute();
    }

    public void GetPrecentageDataFunction(String date){

        class LoginFunctionClass extends AsyncTask<String,Void,String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);
                System.out.println(httpResponseMsg);

                progress1.setVisibility(View.INVISIBLE);
                progress2.setVisibility(View.INVISIBLE);
                progress3.setVisibility(View.INVISIBLE);
                cmPro.setVisibility(View.VISIBLE);
                ytdPro.setVisibility(View.VISIBLE);
                nplPro.setVisibility(View.VISIBLE);

                /*
                try {
                    JSONObject jsonObject = new JSONObject(httpResponseMsg);
                    if(jsonObject!=null) {
                        String cmb = jsonObject.getString("cmb");
                        String cmt = jsonObject.getString("cmt");
                        String ytdb = jsonObject.getString("ytdb");
                        String ytdt = jsonObject.getString("ytdt");
                        String nplexp = jsonObject.getString("npl_exp");
                        String exp = jsonObject.getString("exp");

                        cm = cmb+"/"+cmt;
                        ytd = ytdb+"/"+ytdt;
                        npl = nplexp+"/"+exp;

                        cmprecent = jsonObject.getString("cmr");
                        ytdprecent = jsonObject.getString("ytdr");
                        nplprecent = jsonObject.getString("nplr");

                        cmTv.setText(cm);
                        ytdTv.setText(ytd);
                        nplTv.setText(npl);
                        cmperecentvalue.setText(cmprecent+"%");
                        ytdpercentvalue.setText(ytdprecent+"%");
                        nplpercentvalue.setText(nplprecent+"%");

                        double cmprovalue = Double.parseDouble(cmprecent);
                        double ytdprovalue = Double.parseDouble(ytdprecent);
                        double nplprovalue = Double.parseDouble(nplprecent);
                        int intcm = (int) cmprovalue;
                        int intytd = (int) ytdprovalue;
                        int intnpl = (int) nplprovalue;

                        cmPro.setProgress(intcm);
                        ytdPro.setProgress(intytd);
                        nplPro.setProgress(intnpl);

                    }
                    else{
                        Toast.makeText(DashboardActivity.this,"Cannot Load Data.Please Check your connection", Toast.LENGTH_LONG).show();
                    }


                }catch (JSONException e) {
                    e.printStackTrace();
                }

                 */


            }

            @Override
            protected String doInBackground(String... params) {

                try {
                    url = new URL(PrecentageUrl);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                hashMap.put("action",params[0]);

                finalResult = PostRequest.postRequest(url,hashMap);

                return finalResult;
            }
        }

        LoginFunctionClass loginFunctionClass = new LoginFunctionClass();
        loginFunctionClass.execute(date);
    }


    public void logout(){
        class LogoutFunctionClass extends AsyncTask<String,Void,String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                progressDialog = new ProgressDialog(DashboardActivity.this);
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
        Toast.makeText(DashboardActivity.this,"Please SignOut...", Toast.LENGTH_LONG).show();
    }

}