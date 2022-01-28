package com.example.cms.Leeds;

import static com.example.cms.LoginActivity.Username;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cms.HelperClass.MultipartRequest;
import com.example.cms.HelperClass.PostRequest;
import com.example.cms.LoginActivity;
import com.example.cms.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Pattern;

public class NewLeadLayout extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    String topic,msg;
    ImageView back;
    Spinner title,gender,mkofficer,branch,vbranch,channel,product,stage;
    String profilename,spinnertitleHolder="1",spinnergenderHolder="1",spinnermkofficerHolder="4",spinnerbranchHolder="8",spinnervbranchHolder="8",spinnerchannelHolder="1",spinnerproductHolder="1",spinnerstageHolder="3";
    EditText nic,fullname,initials,dob,email,mobile,paddress1,paddress2,pcity,followupdate,followupaction;
    ArrayAdapter<CharSequence> stageadapter,titleadapter,genderadapter,mkofficeradapter,branchadapter,vbranchadapter,channeladapter,productadapter;
    TextView profilenameTv,leedsId, topicTv, alertmsg, calculator, logout;
    Dialog dialog;
    Button submit, close;
    JSONObject data;
    String SaveUrl = "http://192.168.40.7:8080/cms/lead/save?";
    String ServerLogoutURL = "http://192.168.40.7:8080/cms/logout?";
    URL url;
    String finalResult;


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
                onBackPressed();
                dialog.dismiss();
            }
        });


        //update details button click
        submit = findViewById(R.id.save);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateNic() & validateName() &validateMobile()){
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
                case "Thilina Lakmal":
                    spinnermkofficerHolder = "4";
                    break;
                case "Ranthilaka Durayalage Sagara Saman Kumara":
                    spinnermkofficerHolder = "1";
                    break;
                case "Dinesh Wickremasinghe":
                    spinnermkofficerHolder = "2";
                    break;
                case "Sanjaya Liyanage":
                    spinnermkofficerHolder = "3";
                    break;
                case "Rajapaksha Arachchillage Akila Chanaka":
                    spinnermkofficerHolder = "5";
                    break;
                case "Henda Witharanage Kusal Dewapriya":
                    spinnermkofficerHolder = "6";
                    break;
                case "Bambarawana Liayanagamage Aruna Ravindra Rathnayaka":
                    spinnermkofficerHolder = "7";
                    break;
                case "Amila Rumes Weerasinghe":
                    spinnermkofficerHolder = "8";
                    break;
                case "Jayan Harshana Mallikaarchchi":
                    spinnermkofficerHolder = "9";
                    break;
                case "Kotte Muhandiramge Isuru Malinda  Rodrigo":
                    spinnermkofficerHolder = "10";
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