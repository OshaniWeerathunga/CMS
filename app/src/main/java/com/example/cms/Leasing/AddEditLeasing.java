package com.example.cms.Leasing;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;

import com.example.cms.HelperClass.PostRequest;
import com.example.cms.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

public class AddEditLeasing extends AppCompatActivity {

    String id;
    String EachUserLeedsloadUrl = "http://cms.fintrex.lk/leasing/customerDetails?";
    URL url;
    String finalResult;
    HashMap<String,String> hashMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_leasing);

        id = getIntent().getStringExtra("id");

        GetEachUserLeaseDataFunction(id);
    }

    //get user lease details
    public void GetEachUserLeaseDataFunction(String uid){

        class EachUserLeedsFunctionClass extends AsyncTask<String,Void,String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);
                System.out.println("leasing list edit form     "+httpResponseMsg);

                /*
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

                 */

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
}