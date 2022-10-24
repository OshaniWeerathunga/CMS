package com.example.cms.Leeds;

import static com.example.cms.LoginActivity.Username;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cms.Adapter.CalDataAdapter;
import com.example.cms.Adapter.LeedsTableAdapter;
import com.example.cms.HelperClass.MultipartRequest;
import com.example.cms.HelperClass.PostRequest;
import com.example.cms.LoginActivity;
import com.example.cms.Models.CalDataModel;
import com.example.cms.Models.LeedsTableModel;
import com.example.cms.Models.StructuredRecordList;
import com.example.cms.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class Calculator extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    TextView topicTv,profilenameTv,logout,rate;
    TextView capitalTotTv,interestTotTv,installmentTotTv,rentalTV;
    String topic,profilename;
    ImageView back;
    Button cal;
    ProgressDialog progressDialog;
    Spinner product,repayment;
    EditText amountET,rateET,tenorET,amortizedET;
    ArrayAdapter<CharSequence> productAdapter, repaymentAdapter;
    ArrayAdapter adapter;
    ArrayList<String> productList = new ArrayList<String>();
    ArrayList<String> productValueList = new ArrayList<String>();
    String ServerLogoutURL = "http://cms.fintrex.lk/logout?";
    String ServerloadRateURL = "http://cms.fintrex.lk/leasing/charges?";
    String loadProductListURL = "http://cms.fintrex.lk/MobileApp/getMobileProductList";
    String loadCalculatorURL = "http://cms.fintrex.lk/MobileApp/getMobileTrialCal";
    String UploadUrl = "http://cms.fintrex.lk/lead/update?";

    URL url;
    String finalResult,productId,repaymentId;
    String capital,outstanding,intrest,month,installment;
    String capitalTot, interestTot, installmentTot,rental;
    HashMap<String,String> hashMap = new HashMap<>();
    HashMap<String,String> hashMap2 = new HashMap<>();
    HashMap hashMapStructure = new HashMap<>();

    RecyclerView recyclerView;
    List<CalDataModel> callist = new ArrayList<>();
    CalDataAdapter caladapter;

    LinearLayout structuredLayout,structuredList;
    Button addRecord;
    ImageView removeRecord;
    EditText from,to,structuredInstallment;

    JSONObject data;
    JSONArray structarray;
    ArrayList<StructuredRecordList> recordLists = new ArrayList<>();
    String f,t,in;

    List<StructuredRecordList> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        //load product list dropdown items
        getProductList();


        rate = findViewById(R.id.rateTopic);
        amountET = findViewById(R.id.financeAmt);
        rateET = findViewById(R.id.interestRate);
        tenorET = findViewById(R.id.tenor);
        amortizedET = findViewById(R.id.amortized);

        amountET.addTextChangedListener(onTextChangedListener());


        rentalTV = findViewById(R.id.rental);
        capitalTotTv = findViewById(R.id.txtCapitalTot);
        installmentTotTv = findViewById(R.id.txtInstallmentTot);
        interestTotTv = findViewById(R.id.txtInterestTot);

        structuredList = findViewById(R.id.structured_list);
        structuredLayout = findViewById(R.id.structuredRecordLayout);
        addRecord = findViewById(R.id.addRecord);

        //get topic of the header field
        topic = getIntent().getStringExtra("topic");
        topicTv = findViewById(R.id.topic);
        topicTv.setText(topic);

        //get profile name
        profilename = Username;
        profilenameTv = findViewById(R.id.profilename);
        profilenameTv.setText("  " + profilename);

        //define recyclerview
        recyclerView = findViewById(R.id.calrecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));


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


        //spinner setting for title
        product = findViewById(R.id.productCal);
        productAdapter = ArrayAdapter.createFromResource(
                getApplicationContext(),
                R.array.calProduct,
                R.layout.spinner_colorbg);
        productAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        product.setAdapter(productAdapter);
        product.setOnItemSelectedListener(this);




        //spinner setting for gender
        repayment = findViewById(R.id.repaymentCal);
        repaymentAdapter=ArrayAdapter.createFromResource(
                getApplicationContext(),
                R.array.calRepayment,
                R.layout.spinner_colorbg);
        repaymentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        repayment.setAdapter(repaymentAdapter);
        repayment.setOnItemSelectedListener(this);


        //calculation
        cal = findViewById(R.id.calculate);
        cal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String amount = amountET.getText().toString().replace(",","");
                String rate = rateET.getText().toString();
                String tenor = tenorET.getText().toString();
                String amortized = amortizedET.getText().toString();
                String repayment = repaymentId;

                if(validateAmount() & validateRate() & validateTenor()){

                    //check payment method
                    if (repaymentId.equals("Equated")){
                        calculateEquated(amount,rate,tenor,amortized,repayment);
                    }
                    else if (repaymentId.equals("Structured")){

                        // check validity of add records
                        if(checkValidity()){

                            structarray=new JSONArray();

                            for (int j=0;j<recordLists.size();j++){
                                StructuredRecordList structuredRecordList1 = recordLists.get(j);

                                data = new JSONObject();

                                try {

                                    data.put("from",structuredRecordList1.getFrom());
                                    data.put("to",structuredRecordList1.getTo());
                                    data.put("installment",structuredRecordList1.getInstallment());


                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                structarray.put(data);
                            }
                            //calculate
                            calculateStructured(amount,rate,tenor,amortized,repayment,structarray);


                        }
                    }


                }

            }
        });


        //click structured record button
        addRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addView();
            }
        });

    }


    private TextWatcher onTextChangedListener(){
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                amountET.removeTextChangedListener(this);

                try {
                    String originalString = s.toString();

                    Long longval;
                    if (originalString.contains(",")) {
                        originalString = originalString.replaceAll(",", "");
                    }
                    longval = Long.parseLong(originalString);

                    DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
                    formatter.applyPattern("#,###,###,###");
                    String formattedString = formatter.format(longval);

                    //setting text after format to EditText
                    amountET.setText(formattedString);
                    amountET.setSelection(amountET.getText().length());
                } catch (NumberFormatException nfe) {
                    nfe.printStackTrace();
                }

                amountET.addTextChangedListener(this);
            }
        };
    }

    public boolean checkValidity() {

        recordLists.clear();
        Boolean result = true;

        for(int i=1; i<structuredList.getChildCount();i++){
            View structuredListView = structuredList.getChildAt(i);

            EditText fromET = (EditText) structuredListView.findViewById(R.id.From);
            EditText toET = (EditText) structuredListView.findViewById(R.id.To);
            EditText structuredInstallmentET = (EditText) structuredListView.findViewById(R.id.structureInstall);

            StructuredRecordList structuredRecordList = new StructuredRecordList();

            if(!fromET.getText().toString().equals("")){
                //structuredRecordList.setFrom(from.getText().toString());
                f = fromET.getText().toString();
            }else {
                result = false;
                break;
            }

            if(!toET.getText().toString().equals("")){
                //structuredRecordList.setTo(to.getText().toString());
                t = toET.getText().toString();
            }else {
                result = false;
                break;
            }

            if(!structuredInstallmentET.getText().toString().equals("")){
                //structuredRecordList.setInstallment(structuredInstallment.getText().toString());
                in = structuredInstallmentET.getText().toString();
            }else {
                result = false;
                break;
            }


            StructuredRecordList structuredRecord = new StructuredRecordList(f,t,in);
            recordLists.add(structuredRecord);

            //recordLists.add(structuredRecordList);
            System.out.println("Array list values ---- "+recordLists);

        }

        if(recordLists.size()==0){
            result = false;
            Toast.makeText(this,"Add Record First",Toast.LENGTH_SHORT).show();
        }else if (!result){
            Toast.makeText(this,"Enter Details Correctly",Toast.LENGTH_SHORT).show();
        }




        System.out.println("result is -    "+result.toString());
        return result;

    }


    public void getProductList(){
        class ProductListFunctionClass extends AsyncTask<String,Void,String>  {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);

                try {
                    JSONObject jsonObject = new JSONObject(httpResponseMsg);
                    JSONArray array = jsonObject.getJSONArray("result");

                    for(int i = 0; i < array.length(); i++){
                        JSONObject jsonObject1 = array.getJSONObject(i);
                        productList.add(jsonObject1.getString("text"));
                        productValueList.add(jsonObject1.getString("value"));

                    }
                    System.out.println(productValueList);
                    //set product spinner
                    adapter = new ArrayAdapter(getApplicationContext(),R.layout.spinner_colorbg,productList);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    product.setAdapter(adapter);

                }catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            protected String doInBackground(String... params) {

                try {
                    url = new URL(loadProductListURL);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

                finalResult = PostRequest.getData(url);

                return finalResult;
            }
        }

        ProductListFunctionClass productListFunctionClass = new ProductListFunctionClass();
        productListFunctionClass.execute();

    }


    public void logout(){
        class LogoutFunctionClass extends AsyncTask<String,Void,String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                progressDialog = new ProgressDialog(Calculator.this);
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
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getId() == R.id.productCal) {
            String dropdownTerm = parent.getItemAtPosition(position).toString();
            switch (position) {
                case 0:
                    productId = "71";
                    getRate(productId);
                    break;
                case 1:
                    productId = "79";
                    getRate(productId);
                    break;
                case 2:
                    productId = "80";
                    getRate(productId);
                    break;
                case 3:
                    productId = "83";
                    getRate(productId);
                    break;
                case 4:
                    productId = "84";
                    getRate(productId);
                    break;
                case 5:
                    productId = "85";
                    getRate(productId);
                    break;
            }
        }

        else if (parent.getId() == R.id.repaymentCal) {
            String dropdownTerm = parent.getItemAtPosition(position).toString();
            switch (position) {
                case 0:
                    repaymentId = "Equated";
                    structuredLayout.setVisibility(View.INVISIBLE);
                    break;
                case 1:
                    repaymentId = "Structured";
                    structuredLayout.setVisibility(View.VISIBLE);
                    break;
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void getRate(String pid){
        class GetRateFunctionClass extends AsyncTask<String,Void,String>  {

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
                    String alco = jsonObject.getString("alco");
                    String min = jsonObject.getString("min");
                    String max = jsonObject.getString("max");

                    rate.setText("Alco - "+ alco + "," + " Max - "+ max +","+ " Min - " + min);

                    rateET.setHint("Alco "+alco);


                }catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            protected String doInBackground(String... params) {

                try {
                    url = new URL(ServerloadRateURL);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                hashMap.put("pid",params[0]);

                finalResult = PostRequest.postRequest(url,hashMap);

                return finalResult;
            }
        }

        GetRateFunctionClass getRateFunctionClass = new GetRateFunctionClass();
        getRateFunctionClass.execute(pid);

    }

    //check fiels empty or not
    private boolean validateAmount(){
        String val = amountET.getText().toString();

        if(val.isEmpty()){
            amountET.setError("Field cannot be empty");
            return false;
        }else{
            amountET.setError(null);
            return true;
        }

    }
    private boolean validateRate(){
        String val = rateET.getText().toString();

        if(val.isEmpty()){
            rateET.setError("Field cannot be empty");
            return false;
        }else{
            rateET.setError(null);
            return true;
        }

    }
    private boolean validateTenor(){
        String val = tenorET.getText().toString();

        if(val.isEmpty()){
            tenorET.setError("Field cannot be empty");
            return false;
        }else{
            tenorET.setError(null);
            return true;
        }

    }


    //calculate equate payment method
    public void calculateEquated(String amount, String interest, String tenor, String amortized, String repayment){
        class CalculateFunctionClass extends AsyncTask<String,Void,String>  {

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
                    JSONArray array = jsonObject.getJSONArray("result");

                    callist = new ArrayList<>();

                    capitalTot = jsonObject.getString("capitaltot");
                    interestTot = jsonObject.getString("interestTot");
                    installmentTot = jsonObject.getString("installmentTot");
                    rental = (jsonObject.getString("rental_amount"));


                    for(int i=0; i<array.length();i++){
                        //JSONArray jarray = array.getJSONArray(i);
                        JSONObject jsonObject1 = array.getJSONObject(i);

                        capital = jsonObject1.getString("capital");
                        outstanding = jsonObject1.getString("outstanding");
                        month = jsonObject1.getString("month");
                        intrest = jsonObject1.getString("interest");
                        installment = jsonObject1.getString("installment");


                        CalDataModel calDataModel = new CalDataModel(capital,outstanding,month,intrest,installment);
                        callist.add(calDataModel);

                    }
                    //format to number type
                    DecimalFormat formatter = new DecimalFormat("#,###.00");

                    rentalTV.setText(formatter.format(Float.parseFloat(rental)));
                    capitalTotTv.setText(formatter.format(Float.parseFloat(capitalTot)));
                    interestTotTv.setText(formatter.format(Float.parseFloat(interestTot)));
                    installmentTotTv.setText(formatter.format(Float.parseFloat(installmentTot)));

                    caladapter = new CalDataAdapter(Calculator.this,callist);
                    recyclerView.setAdapter(caladapter);


                }catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            protected String doInBackground(String... params) {

                try {
                    url = new URL(loadCalculatorURL);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                hashMap2.put("finance_amount",params[0]);
                hashMap2.put("interest_rate",params[1]);
                hashMap2.put("tenor",params[2]);
                hashMap2.put("amortized",params[3]);
                hashMap2.put("repayment_type",params[4]);

                finalResult = PostRequest.postRequest(url,hashMap2);

                return finalResult;
            }
        }

        CalculateFunctionClass calculateFunctionClass = new CalculateFunctionClass();
        calculateFunctionClass.execute(amount,interest,tenor,amortized,repayment);

    }

    //calculate structured payment method
    public void calculateStructured(String amount, String interest, String tenor, String amortized, String repayment, JSONArray structarray){
        class CalculateFunctionClass extends AsyncTask<String,Void,String>  {

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
                    JSONArray array = jsonObject.getJSONArray("result");

                    callist = new ArrayList<>();

                    capitalTot = jsonObject.getString("capitaltot");
                    interestTot = jsonObject.getString("interestTot");
                    installmentTot = jsonObject.getString("installmentTot");
                    rental = jsonObject.getString("rental_amount");

                    for(int i=0; i<array.length();i++){
                        //JSONArray jarray = array.getJSONArray(i);
                        JSONObject jsonObject1 = array.getJSONObject(i);

                        capital = jsonObject1.getString("capital");
                        outstanding = jsonObject1.getString("outstanding");
                        month = jsonObject1.getString("month");
                        intrest = jsonObject1.getString("interest");
                        installment = jsonObject1.getString("installment");


                        CalDataModel calDataModel = new CalDataModel(capital,outstanding,month,intrest,installment);
                        callist.add(calDataModel);

                    }
                    //format to number type
                    DecimalFormat formatter = new DecimalFormat("#,###.00");

                    rentalTV.setText(formatter.format(Float.parseFloat(rental)));
                    capitalTotTv.setText(formatter.format(Float.parseFloat(capitalTot)));
                    interestTotTv.setText(formatter.format(Float.parseFloat(interestTot)));
                    installmentTotTv.setText(formatter.format(Float.parseFloat(installmentTot)));

                    caladapter = new CalDataAdapter(Calculator.this,callist);
                    recyclerView.setAdapter(caladapter);


                }catch (JSONException e) {
                    e.printStackTrace();
                }



            }

            @Override
            protected String doInBackground(String... params) {

/*
                data = new JSONObject();

                try {
                    url = new URL(loadCalculatorURL);

                    data.put("from",from.getText().toString());
                    data.put("to",to.getText().toString());
                    data.put("installment",structuredInstallment.getText().toString());


                } catch (Exception e) {
                    e.printStackTrace();
                }

                structarray.put(data);

 */
                try {
                    url = new URL(loadCalculatorURL);

                } catch (Exception e) {
                    e.printStackTrace();
                }

                hashMapStructure.put("finance_amount",params[0]);
                hashMapStructure.put("interest_rate",params[1]);
                hashMapStructure.put("tenor",params[2]);
                hashMapStructure.put("amortized",params[3]);
                hashMapStructure.put("repayment_type",params[4]);
                hashMapStructure.put("paymentstructer", structarray.toString());

                finalResult = PostRequest.postRequest(url,hashMapStructure);

                System.out.println(hashMapStructure);

                return finalResult;
                /*
                try {
                    data.put("from",from.getText().toString());
                    data.put("to",to.getText().toString());
                    data.put("installment",structuredInstallment.getText().toString());

                    structarray.put(data);

                    MultipartRequest multipart = new MultipartRequest(loadCalculatorURL);
                    multipart.addFormField("paymentstructer", structarray.toString());
                    String response = multipart.finish();

                    System.out.println("response inside try block---"+response);
                    return response;


                } catch (Exception e) {
                    e.printStackTrace();
                }

                return  null;

                 */
            }
        }

        CalculateFunctionClass calculateFunctionClass = new CalculateFunctionClass();
        calculateFunctionClass.execute(amount,interest,tenor,amortized,repayment);

    }


    //add structured records
    public  void  addView(){
        final View structuredListView = getLayoutInflater().inflate(R.layout.structured_records,null,false);

        from = structuredListView.findViewById(R.id.From);
        to = structuredListView.findViewById(R.id.To);
        structuredInstallment = structuredListView.findViewById(R.id.structureInstall);

        ImageView remove = structuredListView.findViewById(R.id.record_remove);
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeView(structuredListView);
            }
        });
        structuredList.addView(structuredListView);

    }

    //remove structured record
    public void removeView(View view) {
        structuredList.removeView(view);

    }
}