package com.example.cms;

import static com.example.cms.LoginActivity.Username;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cms.Adapter.LeaseTableAdapter;
import com.example.cms.Adapter.LeedsTableAdapter;
import com.example.cms.Adapter.LoanTableAdapter;
import com.example.cms.HelperClass.PostRequest;
import com.example.cms.Leeds.LeedsUpdate;
import com.example.cms.Leeds.OtherTablesLayout;
import com.example.cms.Leeds.LeedsTablesLayout;
import com.example.cms.Models.LeaseTableModel;
import com.example.cms.Models.LeedsTableModel;
import com.example.cms.Models.LoanTableModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DashboardActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    LinearLayout todayLayout,delayLayout,upcomingLayout,leedsLayout,leaseLayout,loanLayout;
    TextView todayTv,delayTv,upcomingTv,leedsTv,leaseTv;
    ProgressDialog progressDialog;
    ImageView refresh;
    String today,delay,upcoming,prospected,converted,coldLeeds,hotLeeds,warmLeeds,prospectLeeds,sumLeeds,inprogress,approval,activityFacility;

    String cm,ytd,npl,cmprecent,ytdprecent,nplprecent;
    TextView profilenameTv,logout,cmTv,ytdTv,nplTv,cmperecentvalue,ytdpercentvalue,nplpercentvalue;
    ProgressBar cmPro, ytdPro, nplPro,progress1,progress2,progress3;

    String HomeURL = "http://cms.fintrex.lk/ro_dashboard/allocated_contracts_count?";
    String PrecentageUrl = "http://cms.fintrex.lk/company?";
    String ServerLogoutURL = "http://cms.fintrex.lk/logout?";
    String profilename,date = "c1";
    URL url;
    String finalResult;
    HashMap<String,String> hashMap = new HashMap<>();


    Dialog leadsDialog,leasingDialog,loanDialog;
    public static String id,nic,name,branch,product,followup,status,userid;
    public static String leasingid,leasingAssest,leasingname,leasingproduct,leasingfollowup,leasingstatus,leasinguserid,leasingaction;
    public static String loanuserId,loanNo,loanproposal,loanCustomer,loanProduct,loanlastAction,loanstatus;
    String searchLeedLoadUrl = "http://cms.fintrex.test/API/searchLeads?";
    String searchLeasingLoadUrl = "http://cms.fintrex.test/API/searchLeasings?";
    String searchLoanLoadUrl = "http://cms.fintrex.test/API/searchLoans?";
    RecyclerView recyclerView,leaseRecycler,loanRecycler;
    LeedsTableAdapter adapter;
    LeaseTableAdapter leaseAdapter;
    LoanTableAdapter loanAdapter;
    String topic="in progress",facilityTypeHolder,leedSerchKeyword,leasingSearchKeyword,loanSearchKeyword;
    ImageView leedSearch,leadSearchClose,leasingSearchClose,loanSearchClose;
    EditText leedSearchEnter;
    Spinner facilityTypeSpanner;
    ArrayAdapter<CharSequence> facilityTypeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dasboard_new);

        GetDataFunction();

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
        leaseLayout = findViewById(R.id.leaseLayout);
        loanLayout = findViewById(R.id.loanLayout);

        //textview identify
        todayTv = findViewById(R.id.todayTask);
        delayTv = findViewById(R.id.delayTask);
        upcomingTv = findViewById(R.id.upcomingTask);
        leedsTv = findViewById(R.id.leeds);
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

        facilityTypeSpanner=findViewById(R.id.facilityType);

        //spinner setting for select facility type
        facilityTypeAdapter=ArrayAdapter.createFromResource(
                getApplicationContext(),
                R.array.facilityType,
                R.layout.spinner_colorbg);
        facilityTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        facilityTypeSpanner.setAdapter(facilityTypeAdapter);
        facilityTypeSpanner.setOnItemSelectedListener(this);


        //leedsSearch bars
        leedSearch = findViewById(R.id.facilitySearch);
        leedSearchEnter = findViewById(R.id.leedSearchEnter);

 /*
        //leasingSearch bars
        leasingSearch = findViewById(R.id.leasingdSearch);
        leasingSearchEnter = findViewById(R.id.leasingdSearchEnter);

        //loanSearch bars
        loanSearch = findViewById(R.id.loanSearch);
        loanSearchEnter = findViewById(R.id.loanSearchEnter);

         */


        //init dialogbox for search lead list
        leadsDialog = new Dialog(DashboardActivity.this);
        leadsDialog.setContentView(R.layout.leed_search_list_dialogbox);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            leadsDialog.getWindow();
        }
        leadsDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        leadsDialog.setCancelable(false);

        //init alert box ok button
        leadSearchClose = leadsDialog.findViewById(R.id.closeDialog);
        //when user click ok btn
        leadSearchClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                leadsDialog.dismiss();
            }
        });



        //init dialogbox for search leasing list
        leasingDialog = new Dialog(DashboardActivity.this);
        leasingDialog.setContentView(R.layout.leasing_search_list_dialogbox);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            leasingDialog.getWindow();
        }
        leasingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        leasingDialog.setCancelable(false);

        //init alert box ok button
        leasingSearchClose = leasingDialog.findViewById(R.id.closeDialog);
        //when user click ok btn
        leasingSearchClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                leasingDialog.dismiss();
            }
        });



        //init dialogbox for search loan list
        loanDialog = new Dialog(DashboardActivity.this);
        loanDialog.setContentView(R.layout.loan_search_list_dialogbox);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            leadsDialog.getWindow();
        }
        loanDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        loanDialog.setCancelable(false);

        //init alert box ok button
        loanSearchClose = loanDialog.findViewById(R.id.closeDialog);
        //when user click ok btn
        loanSearchClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loanDialog.dismiss();
            }
        });



        //leads serach button
        leedSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                System.out.println("facility holder----"+facilityTypeHolder);

                //load data
                if(facilityTypeHolder.equals("1")) {
                    leedSerchKeyword = leedSearchEnter.getText().toString();
                    if (leedSerchKeyword.isEmpty()) {
                        leedSearchEnter.setError("Field cannot be empty");
                    } else {
                        GetLeedsDataFunction(leedSerchKeyword);
                        leadsDialog.show();
                        Window window = leadsDialog.getWindow();
                        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    }
                }else if(facilityTypeHolder.equals("2")){
                    leedSerchKeyword = leedSearchEnter.getText().toString();
                    if (leedSerchKeyword.isEmpty()) {
                        leedSearchEnter.setError("Field cannot be empty");
                    } else {
                        GetLeasingDataFunction(leedSerchKeyword);
                        leasingDialog.show();
                        Window window = leasingDialog.getWindow();
                        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    }
                }else if(facilityTypeHolder.equals("3")){
                    leedSerchKeyword = leedSearchEnter.getText().toString();
                    if (leedSerchKeyword.isEmpty()) {
                        leedSearchEnter.setError("Field cannot be empty");
                    } else {
                        GetLoanDataFunction(leedSerchKeyword);
                        loanDialog.show();
                        Window window = loanDialog.getWindow();
                        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    }
                }


            }
        });


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


/*
        //leasing serach
        leasingSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //load data
                leasingSearchKeyword = leasingSearchEnter.getText().toString();
                System.out.println(leasingSearchKeyword);
                if(leasingSearchKeyword.isEmpty()){
                    leasingSearchEnter.setError("Field cannot be empty");
                }else{
                    GetLeasingDataFunction(leasingSearchKeyword);
                    leasingDialog.show();
                    Window window = leasingDialog.getWindow();
                    window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                }

            }
        });

 */
        leaseLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LeaseHeadPage.class);
                startActivity(intent);
            }
        });

/*
        //loan serach
        loanSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //load data
                loanSearchKeyword = loanSearchEnter.getText().toString();
                System.out.println(loanSearchKeyword);
                if(loanSearchKeyword.isEmpty()){
                    loanSearchEnter.setError("Field cannot be empty");
                }else{
                    GetLoanDataFunction(loanSearchKeyword);
                    loanDialog.show();
                    Window window = loanDialog.getWindow();
                    window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                }

            }
        });

 */
        loanLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoanHeadPage.class);
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


    //leads data serach
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


                List<LeedsTableModel> list = new ArrayList<>();
                try {
                    JSONObject jsonObject = new JSONObject(httpResponseMsg);
                    JSONArray array = jsonObject.getJSONArray("rows");

                    //hotleedsdata = new ArrayList<Arrays>();

                    for(int i=0; i<array.length();i++){
                        JSONArray jarray = array.getJSONArray(i);

                        userid = jarray.getString(0);
                        id = jarray.getString(1);
                        nic = jarray.getString(2);
                        name = jarray.getString(3);
                        branch = jarray.getString(4);
                        product = jarray.getString(5);
                        followup = jarray.getString(6);
                        status = jarray.getString(7);


                        LeedsTableModel leedsTableModel = new LeedsTableModel(id,nic,name,branch,product,followup,status,userid,topic);
                        list.add(leedsTableModel);

                    }
                    adapter = new LeedsTableAdapter(DashboardActivity.this,list);
                    recyclerView = leadsDialog.findViewById(R.id.leedSearchRecycler);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    recyclerView.setAdapter(adapter);


                }catch (JSONException e) {
                    e.printStackTrace();
                }



            }

            @Override
            protected String doInBackground(String... params) {

                try {
                    url = new URL(searchLeedLoadUrl);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                hashMap.put("val",params[0]);
                System.out.println(hashMap);

                finalResult = PostRequest.postRequest(url,hashMap);

                return finalResult;
            }
        }

        ColdLeedsFunctionClass coldleedsFunctionClass = new ColdLeedsFunctionClass();
        coldleedsFunctionClass.execute(date);
    }

    //leasing data search
    public void GetLeasingDataFunction(String date){

        class ColdLeedsFunctionClass extends AsyncTask<String,Void,String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);
                System.out.println(httpResponseMsg);


                List<LeaseTableModel> list = new ArrayList<>();
                try {
                    JSONObject jsonObject = new JSONObject(httpResponseMsg);
                    JSONArray array = jsonObject.getJSONArray("rows");

                    //hotleedsdata = new ArrayList<Arrays>();

                    for(int i=0; i<array.length();i++){
                        JSONArray jarray = array.getJSONArray(i);

                        leasinguserid = jarray.getString(0);
                        leasingid = jarray.getString(1);
                        leasingname = jarray.getString(2);
                        leasingproduct = jarray.getString(3);
                        leasingstatus = jarray.getString(4);
                        leasingfollowup = jarray.getString(5);
                        leasingAssest = "";


                        LeaseTableModel leaseTableModel = new LeaseTableModel(leasinguserid,leasingid,leasingname,leasingproduct,leasingAssest,leasingfollowup,leasingstatus);
                        list.add(leaseTableModel);

                    }
                    leaseAdapter = new LeaseTableAdapter(DashboardActivity.this,list);
                    leaseRecycler = leasingDialog.findViewById(R.id.leasingSearchRecycler);
                    leaseRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    leaseRecycler.setAdapter(leaseAdapter);


                }catch (JSONException e) {
                    e.printStackTrace();
                }





            }

            @Override
            protected String doInBackground(String... params) {

                try {
                    url = new URL(searchLeasingLoadUrl);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                hashMap.put("val",params[0]);
                System.out.println(hashMap);

                finalResult = PostRequest.postRequest(url,hashMap);

                return finalResult;
            }
        }

        ColdLeedsFunctionClass coldleedsFunctionClass = new ColdLeedsFunctionClass();
        coldleedsFunctionClass.execute(date);
    }


    //loan data search
    public void GetLoanDataFunction(String date){

        class ColdLeedsFunctionClass extends AsyncTask<String,Void,String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);
                System.out.println(httpResponseMsg);


                List<LoanTableModel> list = new ArrayList<>();
                try {
                    JSONObject jsonObject = new JSONObject(httpResponseMsg);
                    JSONArray array = jsonObject.getJSONArray("rows");

                    for(int i=0; i<array.length();i++){
                        JSONArray jarray = array.getJSONArray(i);

                        loanuserId = jarray.getString(0);
                        loanNo = jarray.getString(1);
                        loanProduct = jarray.getString(2);
                        loanCustomer = jarray.getString(3);
                        loanProduct = jarray.getString(4);
                        loanlastAction = jarray.getString(5);
                        loanstatus = jarray.getString(6);


                        LoanTableModel loanTableModel = new LoanTableModel(loanuserId,loanNo,loanproposal,loanCustomer,loanProduct,loanlastAction,loanstatus);
                        list.add(loanTableModel);

                    }
                    loanAdapter = new LoanTableAdapter(DashboardActivity.this,list);
                    loanRecycler = loanDialog.findViewById(R.id.loanSearchRecycler);
                    loanRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    loanRecycler.setAdapter(loanAdapter);


                }catch (JSONException e) {
                    e.printStackTrace();
                }




            }

            @Override
            protected String doInBackground(String... params) {

                try {
                    url = new URL(searchLoanLoadUrl);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                hashMap.put("val",params[0]);
                System.out.println(hashMap);

                finalResult = PostRequest.postRequest(url,hashMap);

                return finalResult;
            }
        }

        ColdLeedsFunctionClass coldleedsFunctionClass = new ColdLeedsFunctionClass();
        coldleedsFunctionClass.execute(date);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getId() == R.id.facilityType) {
            String dropdownInterest = parent.getItemAtPosition(position).toString();
            switch (dropdownInterest) {
                case "Leads":
                    facilityTypeHolder = "1";
                    break;
                case "Leasing":
                    facilityTypeHolder = "2";
                    break;
                case "Loans":
                    facilityTypeHolder = "3";
                    break;
            }
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


}