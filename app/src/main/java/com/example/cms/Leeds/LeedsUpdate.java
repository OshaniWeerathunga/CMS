package com.example.cms.Leeds;

import static com.example.cms.LoginActivity.Username;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ClipData;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cms.Adapter.LeedsTableAdapter;
import com.example.cms.HelperClass.MultipartRequest;
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
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class LeedsUpdate extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    String id,topic,msg;
    TextView leedsId,profilenameTv,logout,alertmsg;
    Dialog dialog;
    Button update,close;
    String profilename,titleHolder,genderHolder,mkofficerHolder,branchHolder,vbranchHolder,channelHolder,productHolder,stageHolder;
    String spinnertitleHolder="1",spinnergenderHolder="1",spinnermkofficerHolder="4",spinnerbranchHolder="8",spinnervbranchHolder="8",spinnerchannelHolder="1",spinnerproductHolder="1",spinnerstageHolder="3";
    String leedsIdHolder,nicHolder,fullnameHolder,initialsHolder,dobHolder,emailHolder,mobileHolder,paddressHolder1,paddressHolder2,pcityHolder,followDateHolder,followActionHolder;
    EditText nic,fullname,initials,dob,email,mobile,paddress1,paddress2,pcity,followupdate,followupaction;
    Spinner title,gender,mkofficer,branch,vbranch,channel,product,stage;
    int titlevalaue,gendervalue,mkofficervalue,branchvalue,vbranchvalue,channelvalue,productvalue,stagevalue;

    ArrayAdapter<CharSequence> stageadapter,titleadapter,genderadapter,mkofficeradapter,branchadapter,vbranchadapter,channeladapter,productadapter;


    String EachUserLeedsloadUrl = "http://192.168.40.7:8080/cms/lead/loadData?";
    String UploadUrl = "http://192.168.40.7:8080/cms/lead/update?";
    String ServerLogoutURL = "http://192.168.40.7:8080/cms/logout?";
    String LoadMarketOfficersURL = "http://192.168.40.7:8080/cms/lead/mkofficer?";
    URL url;
    JSONObject data,datamain;
    String finalResult;
    HashMap<String,String> hashMap = new HashMap<>();
    HashMap<String,JSONObject> hashMap2 = new HashMap<>();
    ImageView back;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.leeds_update_details);

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

        id = getIntent().getStringExtra("id");
        topic = getIntent().getStringExtra("topic");
        leedsIdHolder = getIntent().getStringExtra("leedsId");

        //update button initialize
        update=findViewById(R.id.update);

        //set update button invisible for converted task
        if (topic.equals("Converted Tasks")){
            update.setVisibility(View.INVISIBLE);
        }

        //call each user further details form
        GetEachUserLeedsDataFunction(id);

        //load market officers
        LoadMarketOfficers();

        //spinner sets
        title = findViewById(R.id.title);
        gender = findViewById(R.id.gender);
        mkofficer = findViewById(R.id.mkofficer);
        branch = findViewById(R.id.branch);
        vbranch = findViewById(R.id.virtualbranch);
        channel = findViewById(R.id.channel);
        product = findViewById(R.id.product);
        stage = findViewById(R.id.stage);

        //edit Text boxes
        leedsId = findViewById(R.id.leedId);
        nic = findViewById(R.id.nic);
        fullname = findViewById(R.id.fullname);
        initials = findViewById(R.id.nameInitials);
        dob = findViewById(R.id.dob);
        email = findViewById(R.id.email);
        mobile = findViewById(R.id.mobile);
        paddress1 = findViewById(R.id.paddress1);
        paddress2 = findViewById(R.id.paddress2);
        pcity = findViewById(R.id.pcity);
        followupdate = findViewById(R.id.followupdate);
        followupaction = findViewById(R.id.followupaction);

        //spinner setting for title
        titleadapter = ArrayAdapter.createFromResource(
                getApplicationContext(),
                R.array.titleSpinner,
                R.layout.spinner_colorbg);
        titleadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        title.setAdapter(titleadapter);
        title.setOnItemSelectedListener(this);

        //spinner setting for gender
        genderadapter=ArrayAdapter.createFromResource(
                getApplicationContext(),
                R.array.genderSpinner,
                R.layout.spinner_colorbg);
        genderadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gender.setAdapter(genderadapter);
        gender.setOnItemSelectedListener(this);

        //spinner setting for mkofficer
        mkofficeradapter=ArrayAdapter.createFromResource(
                getApplicationContext(),
                R.array.mkofficerSpinner,
                R.layout.spinner_colorbg);
        mkofficeradapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mkofficer.setAdapter(mkofficeradapter);
        mkofficer.setOnItemSelectedListener(this);


        //spinner setting for branch
        branchadapter=ArrayAdapter.createFromResource(
                getApplicationContext(),
                R.array.branchSpinner,
                R.layout.spinner_colorbg);
        branchadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        branch.setAdapter(branchadapter);
        branch.setOnItemSelectedListener(this);

        //spinner setting for vbranch
        vbranchadapter=ArrayAdapter.createFromResource(
                getApplicationContext(),
                R.array.vbranchSpinner,
                R.layout.spinner_colorbg);
        vbranchadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        vbranch.setAdapter(vbranchadapter);
        vbranch.setOnItemSelectedListener(this);

        //spinner setting for channel
        channeladapter=ArrayAdapter.createFromResource(
                getApplicationContext(),
                R.array.channelSpinner,
                R.layout.spinner_colorbg);
        channeladapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        channel.setAdapter(channeladapter);
        channel.setOnItemSelectedListener(this);

        //spinner setting for product
        productadapter=ArrayAdapter.createFromResource(
                getApplicationContext(),
                R.array.productSpinner,
                R.layout.spinner_colorbg);
        productadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        product.setAdapter(productadapter);
        product.setOnItemSelectedListener(this);

        //spinner setting for stage
        stageadapter=ArrayAdapter.createFromResource(
                getApplicationContext(),
                R.array.stageSpinner,
                R.layout.spinner_colorbg);
        stageadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        stage.setAdapter(stageadapter);
        stage.setOnItemSelectedListener(this);


        //init dialogbox for successfully update lead
        dialog = new Dialog(LeedsUpdate.this);
        dialog.setContentView(R.layout.alert_update_);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            dialog.getWindow();
        }
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);

        //init alert box ok button
        Button ok = dialog.findViewById(R.id.okbtn);
        //when user click ok btn
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                dialog.dismiss();
            }
        });


        //create date picker for followdate
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        followupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        LeedsUpdate.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month = month+1;
                        String fdate = year +"-"+month+"-"+dayOfMonth;
                        followupdate.setText(fdate);
                    }
                },year,month,day);
                datePickerDialog.show();

            }
        });

        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        LeedsUpdate.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month = month+1;
                        String dobdate = year +"-"+month+"-"+dayOfMonth;
                        dob.setText(dobdate);
                    }
                },year,month,day);
                datePickerDialog.show();

            }
        });



        //update details button click
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateData();

            }
        });

        //close button
        close = findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
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





    public void GetEachUserLeedsDataFunction(String uid){

        class EachUserLeedsFunctionClass extends AsyncTask<String,Void,String> {

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


                        nicHolder = jsonObject.optString("nic");
                        titleHolder = jsonObject.optString("title_txt");
                        fullnameHolder = jsonObject.optString("fname");
                        initialsHolder = jsonObject.optString("short_name");
                        genderHolder = jsonObject.optString("gender_txt");
                        dobHolder = jsonObject.optString("dob");
                        emailHolder = jsonObject.optString("email");
                        mobileHolder = jsonObject.optString("mobile");
                        mkofficerHolder = jsonObject.optString("mkofficer_txt");
                        branchHolder = jsonObject.optString("branch_txt");
                        vbranchHolder = jsonObject.optString("vbranch_txt");

                        paddressHolder1 = jsonObject.optString("p_address_l");
                        paddressHolder2 = jsonObject.optString("p_address_2");
                        pcityHolder = jsonObject.optString("pcity_txt");

                        channelHolder = jsonObject.optString("channel_txt");
                        productHolder = jsonObject.optString("product_txt");
                        stageHolder = jsonObject.optString("stage_txt");
                        followDateHolder = jsonObject.optString("followup");
                        followActionHolder = jsonObject.optString("followup_action");

                        //set values for edit text
                        leedsId.setText(leedsIdHolder);
                        nic.setText(nicHolder);
                        fullname.setText(fullnameHolder);
                        initials.setText(initialsHolder);
                        dob.setText(dobHolder);
                        email.setText(emailHolder);
                        mobile.setText(mobileHolder);
                        paddress1.setText(paddressHolder1);
                        paddress2.setText(paddressHolder2);
                        pcity.setText(pcityHolder);
                        followupdate.setText(followDateHolder);
                        followupaction.setText(followActionHolder);

                       //set values for spinners
                         titlevalaue = titleadapter.getPosition(titleHolder);
                         gendervalue = genderadapter.getPosition(genderHolder);
                         mkofficervalue = mkofficeradapter.getPosition(mkofficerHolder);
                         branchvalue = branchadapter.getPosition(branchHolder);
                         vbranchvalue = vbranchadapter.getPosition(vbranchHolder);
                         channelvalue = channeladapter.getPosition(channelHolder);
                         productvalue = productadapter.getPosition(productHolder);
                         stagevalue = stageadapter.getPosition(stageHolder);

                    System.out.println(titlevalaue+gendervalue+channelvalue);

                        title.setSelection(titlevalaue);
                        gender.setSelection(gendervalue);
                        mkofficer.setSelection(mkofficervalue);
                        branch.setSelection(branchvalue);
                        vbranch.setSelection(vbranchvalue);
                        channel.setSelection(channelvalue);
                        product.setSelection(productvalue);
                        stage.setSelection(stagevalue);


                }catch (JSONException e) {
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
                hashMap.put("id",params[0]);

                finalResult = PostRequest.postRequest(url,hashMap);

                return finalResult;
            }
        }

        EachUserLeedsFunctionClass eachUserLeedsFunctionClass = new EachUserLeedsFunctionClass();
        eachUserLeedsFunctionClass.execute(uid);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        if (parent.getId() == R.id.title) {
            String dropdownInterest = parent.getItemAtPosition(position).toString();
            switch (dropdownInterest) {
                case "Mr":
                    spinnertitleHolder = "1";
                    break;
                case "Mrs":
                    spinnertitleHolder = "11";
                    break;
                case "Miss":
                    spinnertitleHolder = "3";
                    break;
                case "Master":
                    spinnertitleHolder = "8";
                    break;
                case "Prof":
                    spinnertitleHolder = "9";
                    break;
                case "Dr":
                    spinnertitleHolder = "18";
                    break;

            }
        }

        else if (parent.getId() == R.id.gender) {
            String dropdownTerm = parent.getItemAtPosition(position).toString();
            switch (dropdownTerm) {
                case "Male":
                    spinnergenderHolder = "1";
                    break;
                case "Female":
                    spinnergenderHolder = "2";
                    break;

            }
        }

        else if (parent.getId() == R.id.mkofficer) {
            String dropdownTerm = parent.getItemAtPosition(position).toString();
            switch (dropdownTerm) {

                case "Poorna Prathiba":
                    spinnermkofficerHolder = "4";
                    break;
                case "Dinesh Wickramasinghe":
                    spinnermkofficerHolder = "12";
                    break;
                case "Sirimal Priyantha":
                    spinnermkofficerHolder = "48";
                    break;
                case "Rangana Sampath Kumara":
                    spinnermkofficerHolder = "50";
                    break;
                case "Sagara Saman  Kumara":
                    spinnermkofficerHolder = "51";
                    break;
                case "Thilina Lakmal":
                    spinnermkofficerHolder = "53";
                    break;
                case "Steve Perera":
                    spinnermkofficerHolder = "55";
                    break;
                case "Sushan Sugandhika Somasiri":
                    spinnermkofficerHolder = "264";
                    break;
                case "Cherin Jayasekara":
                    spinnermkofficerHolder = "268";
                    break;
                case "Ashan Saranga":
                    spinnermkofficerHolder = "270";
                    break;
                case "Sumudu Manoj Rukshan":
                    spinnermkofficerHolder = "271";
                    break;

                case "Gehan Ranishika Fernando":
                    spinnermkofficerHolder = "274";
                    break;
                case "Chamli Udayakumara":
                    spinnermkofficerHolder = "275";
                    break;
                case "Sanjaya Perera":
                    spinnermkofficerHolder = "276";
                    break;
                case "Ishara Udayangana":
                    spinnermkofficerHolder = "277";
                    break;
                case "Ravin Dilshan":
                    spinnermkofficerHolder = "279";
                    break;
                case "Sudheera Chathuranga":
                    spinnermkofficerHolder = "280";
                    break;
                case "Priyashad Madhusanka":
                    spinnermkofficerHolder = "284";
                    break;
                case "Lalith Kumara Siriwardana":
                    spinnermkofficerHolder = "285";
                    break;
                case "Indika Senarath Bandara":
                    spinnermkofficerHolder = "286";
                    break;
                case "Keshawa De Soysa":
                    spinnermkofficerHolder = "288";
                    break;
                case "Vinod Manula":
                    spinnermkofficerHolder = "289";
                    break;
                case "Himal Ruvinda":
                    spinnermkofficerHolder = "290";
                    break;
                case "Sanjaya Lakmal":
                    spinnermkofficerHolder = "296";
                    break;
                case "Harsha Chamara besil":
                    spinnermkofficerHolder = "297";
                    break;
                case "Sasitha Sarannga":
                    spinnermkofficerHolder = "300";
                    break;
                case "Anuruddha Perera":
                    spinnermkofficerHolder = "302";
                    break;
                case "Chanaka dhananjaya Weediyawaththa":
                    spinnermkofficerHolder = "305";
                    break;
                case "Sathsara Lakmal Kumara":
                    spinnermkofficerHolder = "309";
                    break;
                case "Akila Chanaka":
                    spinnermkofficerHolder = "311";
                    break;
                case "Amila Dhanushka":
                    spinnermkofficerHolder = "319";
                    break;
                case "Anusha Perera":
                    spinnermkofficerHolder = "320";
                    break;
                case "Asintha Kumara":
                    spinnermkofficerHolder = "322";
                    break;
                case "Kusal Dewapriya":
                    spinnermkofficerHolder = "323";
                    break;
                case "Tharooka Deshan":
                    spinnermkofficerHolder = "326";
                    break;
                case "Jayan Harshan":
                    spinnermkofficerHolder = "328";
                    break;
                case "Isuru Gihan":
                    spinnermkofficerHolder = "329";
                    break;
                case "Thushara Wickramasingha":
                    spinnermkofficerHolder = "330";
                    break;
                case "Aruna Ravindra Rathnayaka":
                    spinnermkofficerHolder = "332";
                    break;
                case "Sachinda Randimal":
                    spinnermkofficerHolder = "334";
                    break;
                case "Deeptha Chiranthaka":
                    spinnermkofficerHolder = "335";
                    break;
                case "Hishan Shamika":
                    spinnermkofficerHolder = "336";
                    break;
                case "Pradeep Nishantha Perera":
                    spinnermkofficerHolder = "337";
                    break;
                case "Lahiru Madusanka Pradeep":
                    spinnermkofficerHolder = "338";
                    break;
                case "Shihan Vidushaka":
                    spinnermkofficerHolder = "340";
                    break;
                case "Isuru Malinda":
                    spinnermkofficerHolder = "341";
                    break;
                case "Roshan Maduwantha":
                    spinnermkofficerHolder = "343";
                    break;
                case "Manjula Wickramasinghage":
                    spinnermkofficerHolder = "344";
                    break;
                case "janith":
                    spinnermkofficerHolder = "367";
                    break;
                case "Kolitha Sudamma Senarath":
                    spinnermkofficerHolder = "373";
                    break;
                case "Rajith Lasitha Bandara":
                    spinnermkofficerHolder = "374";
                    break;
                case "Rajitha Nuwan Kaluararchchi":
                    spinnermkofficerHolder = "378";
                    break;
                case "Lasan Madhubhasha Karunadhipathi":
                    spinnermkofficerHolder = "385";
                    break;
                case "Primal Sandika":
                    spinnermkofficerHolder = "386";
                    break;
                case "Viraj Chathuranga":
                    spinnermkofficerHolder = "389";
                    break;
                case "Rajitha Prasanna Weerasiri":
                    spinnermkofficerHolder = "394";
                    break;
                case "Kasun Hansana Kumara":
                    spinnermkofficerHolder = "395";
                    break;
                case "Manoj Rukshan":
                    spinnermkofficerHolder = "398";
                    break;
                case "Hemantha Adikari":
                    spinnermkofficerHolder = "400";
                    break;
                case "Rangana Nuwan Priyadharshana":
                    spinnermkofficerHolder = "401";
                    break;
                case "Chandika Jayanjan":
                    spinnermkofficerHolder = "418";
                    break;
                case "Manoj Wijethunga":
                    spinnermkofficerHolder = "421";
                    break;
                case "Chamara Sandaruwan":
                    spinnermkofficerHolder = "422";
                    break;
                case "Indra Kumar":
                    spinnermkofficerHolder = "423";
                    break;

            }
        }

        else if (parent.getId() == R.id.branch) {
            String dropdownTerm = parent.getItemAtPosition(position).toString();
            switch (dropdownTerm) {
                case "Head Office":
                    spinnerbranchHolder = "8";
                    break;
                case "City Office":
                    spinnerbranchHolder = "1";
                    break;
                case "Kiribathgoda":
                    spinnerbranchHolder = "14";
                    break;
                case "Kuliyapitiya":
                    spinnerbranchHolder = "2";
                    break;
                case "Kegalle":
                    spinnerbranchHolder = "7";
                    break;
                case "Kalutara":
                    spinnerbranchHolder = "11";
                    break;
                case "Matara":
                    spinnerbranchHolder = "3";
                    break;
                case "Galle":
                    spinnerbranchHolder = "16";
                    break;
                case "Gampaha":
                    spinnerbranchHolder = "13";
                    break;
                case "Negombo":
                    spinnerbranchHolder = "15";
                    break;
                case "Kurunegala":
                    spinnerbranchHolder = "9";
                    break;
                case "Metro":
                    spinnerbranchHolder = "10";
                    break;
                case "Dambulla":
                    spinnerbranchHolder = "12";
                    break;
                case "Malabe":
                    spinnerbranchHolder = "17";
                    break;
                case "Maharagama":
                    spinnerbranchHolder = "18";
                    break;

            }
        }

        else if (parent.getId() == R.id.virtualbranch) {
            String dropdownTerm = parent.getItemAtPosition(position).toString();
            switch (dropdownTerm) {
                case "Head Office":
                    spinnervbranchHolder = "8";
                    break;
                case "City Office":
                    spinnervbranchHolder = "1";
                    break;
                case "Kiribathgoda":
                    spinnervbranchHolder = "14";
                    break;
                case "Kuliyapitiya":
                    spinnervbranchHolder = "2";
                    break;
                case "Kegalle":
                    spinnervbranchHolder = "7";
                    break;
                case "Kalutara":
                    spinnervbranchHolder = "11";
                    break;
                case "Matara":
                    spinnervbranchHolder = "3";
                    break;
                case "Galle":
                    spinnervbranchHolder = "16";
                    break;
                case "Gampaha":
                    spinnervbranchHolder = "13";
                    break;
                case "Negombo":
                    spinnervbranchHolder = "15";
                    break;
                case "Kurunegala":
                    spinnervbranchHolder = "9";
                    break;
                case "Metro":
                    spinnervbranchHolder = "10";
                    break;
                case "Dambulla":
                    spinnervbranchHolder = "12";
                    break;
                case "Malabe":
                    spinnervbranchHolder = "17";
                    break;
                case "Maharagama":
                    spinnervbranchHolder = "18";
                    break;

            }
        }

        else if (parent.getId() == R.id.channel) {
            String dropdownTerm = parent.getItemAtPosition(position).toString();
            switch (dropdownTerm) {
                case "Pharmaceutical Industry":
                    spinnerchannelHolder = "1";
                    break;
                case "Insurance Agent":
                    spinnerchannelHolder = "4";
                    break;
                case "Garage Agent":
                    spinnerchannelHolder = "2";
                    break;
                case "Vehicle Dealer":
                    spinnerchannelHolder = "3";
                    break;
                case "Existing Clients":
                    spinnerchannelHolder = "5";
                    break;

            }
        }

        else if (parent.getId() == R.id.product) {
            String dropdownTerm = parent.getItemAtPosition(position).toString();
            switch (dropdownTerm) {
                case "Leasing":
                    spinnerproductHolder = "62";
                    break;
                case "Vehicle Loan":
                    spinnerproductHolder = "71";
                    break;
                case "Finance Lease - 4W":
                    spinnerproductHolder = "79";
                    break;
                case "Finance Lease - 3W":
                    spinnerproductHolder = "80";
                    break;
                case "Finance Lease - 2W":
                    spinnerproductHolder = "83";
                    break;

            }
        }

        else if (parent.getId() == R.id.stage) {
            String dropdownTerm = parent.getItemAtPosition(position).toString();
            switch (dropdownTerm) {
                case "Hot":
                    spinnerstageHolder = "3";
                    break;
                case "Cold":
                    spinnerstageHolder = "1";
                    break;
                case "Warm":
                    spinnerstageHolder = "2";
                    break;
                case "Prospecting":
                    spinnerstageHolder = "4";
                    break;

            }
        }


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }



    public void UpdateData(){

        class UpdateLeedsFunctionClass extends AsyncTask<String,Void,String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);
                System.out.println("httpResponse : " + httpResponseMsg);

                try {
                    JSONObject jsonObject = new JSONObject(httpResponseMsg);
                    if (jsonObject.getString("status").equals("success")) {

                        msg = jsonObject.getString("msg");

                        dialog.show();
                        TextView alertmsg  = dialog.findViewById(R.id.updatemsg);
                        alertmsg.setText(msg);


                    } else {

                        Toast.makeText(getApplicationContext(), "Lead not updated", Toast.LENGTH_LONG).show();
                    }
                }catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "Please try again.", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }


            }

            @Override
            protected String doInBackground(String... params) {

                data = new JSONObject();
                try {
                    data.put("uid",id);
                    data.put("nic",nic.getText().toString());
                    data.put("title",spinnertitleHolder);
                    data.put("fname",fullname.getText().toString());
                    data.put("short_name",initials.getText().toString());
                    data.put("gender",spinnergenderHolder);
                    data.put("dob",dob.getText().toString());
                    data.put("mobile",mobile.getText().toString());
                    data.put("email",email.getText().toString());
                    data.put("mkofficer",spinnermkofficerHolder);
                    data.put("branch",spinnerbranchHolder);
                    data.put("vbranch",spinnervbranchHolder);
                    data.put("p_address_l",paddress1.getText().toString());
                    data.put("p_address_2",paddress2.getText().toString());
                    data.put("p_city","");
                    data.put("channel",spinnerchannelHolder);
                    data.put("product",spinnerproductHolder);
                    data.put("stage",spinnerstageHolder);
                    data.put("followup",followupdate.getText().toString());
                    data.put("followup_action",followupaction.getText().toString());

                    data.put("telephone","");
                    data.put("c_address_l","");
                    data.put("c_address_2","");
                    data.put("c_city","");
                    data.put("cal_product","");
                    data.put("cal_leaseAmount","");
                    data.put("cal_tenor","");
                    data.put("cal_interest","");
                    data.put("cal_repaymentType","");
                    data.put("amortized","");
                    data.put("structured_payment","");

                    System.out.println("sending data via request---"+data);

                    MultipartRequest multipart = new MultipartRequest(UploadUrl);
                    multipart.addFormField("data", data.toString());
                    String response = multipart.finish();

                    System.out.println("response inside try block---"+response);
                    return response;


                } catch (Exception e) {
                    e.printStackTrace();
                }
                return  null;

            }
        }

        UpdateLeedsFunctionClass updateLeedsFunctionClass = new UpdateLeedsFunctionClass();
        updateLeedsFunctionClass.execute();
    }



    public void LoadMarketOfficers(){
        class LogoutFunctionClass extends AsyncTask<String,Void,String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);
                System.out.println(httpResponseMsg);




            }

            @Override
            protected String doInBackground(String... params) {

                try {
                    url = new URL(LoadMarketOfficersURL);
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