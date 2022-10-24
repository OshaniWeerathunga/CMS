package com.example.cms.Leeds;

import static com.example.cms.LoginActivity.Username;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cms.HelperClass.MultipartRequest;
import com.example.cms.HelperClass.PostRequest;
import com.example.cms.LeedsHeadPage;
import com.example.cms.LoginActivity;
import com.example.cms.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Pattern;

public class NewLeadLayout extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    String topic,msg;
    ImageView back;
    Spinner title,gender,mkofficer,branch,vbranch,channel,product,stage;
    String profilename,spinnertitleHolder,spinnergenderHolder,spinnermkofficerHolder,spinnerbranchHolder,spinnervbranchHolder,spinnerchannelHolder,spinnerproductHolder,spinnerstageHolder;
    EditText nic,fullname,initials,dob,email,mobile,paddress1,paddress2,pcity,followupdate,followupaction;
    ArrayAdapter<CharSequence> stageadapter,titleadapter,genderadapter,mkofficeradapter,branchadapter,vbranchadapter,channeladapter,productadapter;
    TextView profilenameTv,leedsId, topicTv, alertmsg, calculator, logout,stageTV;
    Dialog dialog;
    Button submit, close;
    JSONObject data;
    String SaveUrl = "http://cms.fintrex.lk/lead/save?";
    String ServerLogoutURL = "http://cms.fintrex.lk/logout?";
    URL url;
    String finalResult;
    DatePickerDialog.OnDateSetListener setListener;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_lead_layout);

        //get topic of the header field
        topic = getIntent().getStringExtra("topic");
        topicTv = findViewById(R.id.topic);
        topicTv.setText(topic);

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

        //define logout button
        logout = findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });


        //calculator open
        calculator = findViewById(R.id.calculator);
        calculator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Calculator.class);
                startActivity(intent);
            }
        });

        //spinner sets
        title = findViewById(R.id.title);
        gender = findViewById(R.id.gender);
        mkofficer = findViewById(R.id.mkofficer);
        branch = findViewById(R.id.branch);
        vbranch = findViewById(R.id.virtualbranch);
        channel = findViewById(R.id.channel);
        product = findViewById(R.id.product);
        stage = findViewById(R.id.stage);
        stageTV = findViewById(R.id.stageTV);

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
        dialog = new Dialog(NewLeadLayout.this);
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
                Intent intent = new Intent(getApplicationContext(), LeedsHeadPage.class);
                startActivity(intent);
                finish();
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
                         NewLeadLayout.this, new DatePickerDialog.OnDateSetListener() {
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
                         NewLeadLayout.this, new DatePickerDialog.OnDateSetListener() {
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
        submit = findViewById(R.id.save);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateName() &validateMobile() & validateStage()){
                    SaveData();

                }
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

    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        if (parent.getId() == R.id.title) {
            String dropdownInterest = parent.getItemAtPosition(position).toString();
            switch (dropdownInterest) {
                case "--Choose Title--":
                    spinnertitleHolder = "";
                    break;
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
                case "--Choose Gender--":
                    spinnergenderHolder = "";
                    break;
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
                case "--Choose Marketing Officer--":
                    spinnermkofficerHolder = "";
                    break;
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
                case "Gihan Dabare":
                    spinnermkofficerHolder = "425";
                    break;
                case "Buddi Gayashan":
                    spinnermkofficerHolder = "427";
                    break;
                case "Chamidu Malshan":
                    spinnermkofficerHolder = "436";
                    break;
                case "Sahan Chathuranga":
                    spinnermkofficerHolder = "437";
                    break;
                case "Suresh jayathilaka":
                    spinnermkofficerHolder = "438";
                    break;
                case "Madhura Naullage":
                    spinnermkofficerHolder = "439";
                    break;
                case "Ruwan Hemantha":
                    spinnermkofficerHolder = "440";
                    break;
                case "Anjana Ishara":
                    spinnermkofficerHolder = "441";
                    break;
                case "Isuru Sasanka Bandara":
                    spinnermkofficerHolder = "446";
                    break;
                case "Lakshan Priyadarshana":
                    spinnermkofficerHolder = "450";
                    break;
                case "Anne Jennifer":
                    spinnermkofficerHolder = "451";
                    break;
                case "Charika Perera":
                    spinnermkofficerHolder = "453";
                    break;

            }
        }

        else if (parent.getId() == R.id.branch) {
            String dropdownTerm = parent.getItemAtPosition(position).toString();
            switch (dropdownTerm) {
                case "--Choose Branch--":
                    spinnerbranchHolder = "";
                    break;
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
                case "Kandy":
                    spinnerbranchHolder = "6";
                    break;
                case "Nugegoda":
                    spinnerbranchHolder = "19";
                    break;
                case "Panadura":
                    spinnerbranchHolder = "20";
                    break;
                case "Wennappuwa":
                    spinnerbranchHolder = "21";
                    break;
                case "Pettah":
                    spinnerbranchHolder = "22";
                    break;
                case "Nittambuwa":
                    spinnerbranchHolder = "23";
                    break;
                case "Homagama":
                    spinnerbranchHolder = "24";
                    break;

            }
        }

        else if (parent.getId() == R.id.virtualbranch) {
            String dropdownTerm = parent.getItemAtPosition(position).toString();
            switch (dropdownTerm) {
                case "--Choose Virtual Branch--":
                    spinnervbranchHolder = "";
                    break;
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
                case "Kandy":
                    spinnervbranchHolder = "6";
                    break;
                case "Nugegoda":
                    spinnervbranchHolder = "19";
                    break;
                case "Panadura":
                    spinnervbranchHolder = "20";
                    break;
                case "Wennappuwa":
                    spinnervbranchHolder = "21";
                    break;
                case "Pettah":
                    spinnervbranchHolder = "22";
                    break;
                case "Nittambuwa":
                    spinnervbranchHolder = "23";
                    break;
                case "Homagama":
                    spinnervbranchHolder = "24";
                    break;

            }
        }

        else if (parent.getId() == R.id.channel) {
            String dropdownTerm = parent.getItemAtPosition(position).toString();
            switch (dropdownTerm) {
                case "--Choose Channel--":
                    spinnerchannelHolder = "";
                    break;
                case "Pharmacies/Jewellery Shops/Salon":
                    spinnerchannelHolder = "7";
                    break;
                case "Insurance Agent":
                    spinnerchannelHolder = "4";
                    break;
                case "Housing Scheme/Apartments":
                    spinnerchannelHolder = "6";
                    break;
                case "Professionals/Doctors/Lawyers":
                    spinnerchannelHolder = "11";
                    break;
                case "Existing Clients":
                    spinnerchannelHolder = "5";
                    break;
                case "Education Centers/Tuition Class":
                    spinnerchannelHolder = "10";
                    break;
                case "Retail/Whole sale/Grocery Shop":
                    spinnerchannelHolder = "9";
                    break;
                case "Service Station/Vehicle Dealers/Taxi":
                    spinnerchannelHolder = "8";
                    break;

            }
        }

        else if (parent.getId() == R.id.product) {
            String dropdownTerm = parent.getItemAtPosition(position).toString();
            switch (dropdownTerm) {
                case "--Choose Product--":
                    spinnerproductHolder = "";
                    break;
                case "Letter of Credit":
                    spinnerproductHolder = "44";
                    break;
                case "Revolving Loan":
                    spinnerproductHolder = "45";
                    break;
                case "Import Loan":
                    spinnerproductHolder = "46";
                    break;
                case "Pledge Loans":
                    spinnerproductHolder = "47";
                    break;
                case "Term Loan":
                    spinnerproductHolder = "59";
                    break;
                case "Trade Finance":
                    spinnerproductHolder = "60";
                    break;
                case "Advances Against Import Bill":
                    spinnerproductHolder = "63";
                    break;
                case "Bank Guarantee":
                    spinnerproductHolder = "64";
                    break;
                case "Credit Card":
                    spinnerproductHolder = "65";
                    break;
                case "Factoring":
                    spinnerproductHolder = "66";
                    break;
                case "Hire Purchase":
                    spinnerproductHolder = "67";
                    break;
                case "Overdraft":
                    spinnerproductHolder = "68";
                    break;
                case "Personal Loan":
                    spinnerproductHolder = "69";
                    break;
                case "Short Term Loan":
                    spinnerproductHolder = "70";
                    break;
                case "Vehicle Loan":
                    spinnerproductHolder = "71";
                    break;
                case "Vehicle Pledge Loan":
                    spinnerproductHolder = "72";
                    break;
                case "Letter of Credit (Sight/Usance)":
                    spinnerproductHolder = "73";
                    break;
                case "Letter of Credit (Sight)- Facility Client":
                    spinnerproductHolder = "74";
                    break;
                case "Letter of Credit (Usance)- Facility Client":
                    spinnerproductHolder = "75";
                    break;
                case "Finance Lease-4W":
                    spinnerproductHolder = "79";
                    break;
                case "Finance Lease-3W":
                    spinnerproductHolder = "80";
                    break;
                case "Letter of Credit (Sight)- One Off Client":
                    spinnerproductHolder = "81";
                    break;
                case "Letter of Credit (Usance)- One Off Client":
                    spinnerproductHolder = "82";
                    break;
                case "Finance Lease-2W":
                    spinnerproductHolder = "83";
                    break;
                case "Smart Draft":
                    spinnerproductHolder = "84";
                    break;
                case "Smart Cash":
                    spinnerproductHolder = "85";
                    break;
                case "Fixed Deposit":
                    spinnerproductHolder = "86";
                    break;
                case "Savings":
                    spinnerproductHolder = "87";
                    break;
                case "Short Term Revolving Loan":
                    spinnerproductHolder = "88";
                    break;
                case "Gold Loan":
                    spinnerproductHolder = "90";
                    break;
                case "Cash Back Loan":
                    spinnerproductHolder = "91";
                    break;


            }
        }

        else if (parent.getId() == R.id.stage) {
            String dropdownTerm = parent.getItemAtPosition(position).toString();
            switch (dropdownTerm) {
                case "--Choose Stage--":
                    spinnerstageHolder = "";
                    break;
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

    //check validation of the nic
    private boolean validateNic(){
        String val = nic.getText().toString().trim();
        val = val.replace(" ","");
        Boolean patternnok = false;

        if(val.isEmpty()){
            nic.setError("Field cannot be empty");
            return false;
        }else{
            nic.setError(null);
            String oldpattern = "[0-9]{9}[vV]";
            String newpattern = "[0-9]{12}";
            patternnok = Pattern.compile(oldpattern).matcher(val).matches() | Pattern.compile(newpattern).matcher(val).matches();
            if (!patternnok){
                nic.setError("Your NIC is not valid");
                return false;
            }
            else {
                return true;
            }
        }

    }

    //check validation of the mobile
    private boolean validateMobile(){
        String val = mobile.getText().toString().trim();
        Boolean patternok=false;

        if(val.isEmpty()){
            mobile.setError("Field cannot be empty");
            return false;
        }else{
            mobile.setError(null);
            String mobilepattern = "[0-9]{10}";
            patternok = Pattern.compile(mobilepattern).matcher(val).matches();
            if (!patternok) {
                mobile.setError("Your Mobile is not valid");
                return false;
            }else {
                return true;
            }
        }

    }

    //validate fullname
    private boolean validateName(){
        String val = fullname.getText().toString();

        if(val.isEmpty()){
            fullname.setError("Field cannot be empty");
            return false;
        }else{
            fullname.setError(null);
            return true;
        }

    }

    //validate stage spinner
    private boolean validateStage(){
        String val = spinnerstageHolder;

        if(val.equals("")){
            stageTV.setError("Please select Stage");
            stageTV.setText("Please select Stage");
            stageTV.setTextColor(Color.RED);
            return false;
        }else{
            return true;
        }

    }





    public void SaveData(){

        class SaveLeedsFunctionClass extends AsyncTask<String,Void,String> {

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
                        alertmsg  = dialog.findViewById(R.id.updatemsg);
                        alertmsg.setText(msg);

                    } else {

                        Toast.makeText(getApplicationContext(), "Lead not added", Toast.LENGTH_LONG).show();
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
                    //data.put("uid",id);
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

                    MultipartRequest multipart = new MultipartRequest(SaveUrl);
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

        SaveLeedsFunctionClass saveLeedsFunctionClass = new SaveLeedsFunctionClass();
        saveLeedsFunctionClass.execute();
    }

    public void logout(){
        class LogoutFunctionClass extends AsyncTask<String,Void,String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                progressDialog = new ProgressDialog(NewLeadLayout.this);
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