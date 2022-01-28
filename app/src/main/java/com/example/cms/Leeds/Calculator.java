package com.example.cms.Leeds;

import static com.example.cms.LoginActivity.Username;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Calculator extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    TextView topicTv,profilenameTv,logout,rate,rentalTV;
    String topic,profilename;
    ImageView back;
    Button cal;
    Spinner product,repayment;
    EditText amountET,rateET,tenorET,amortizedET;
    ArrayAdapter<CharSequence> productAdapter, repaymentAdapter;
    ArrayAdapter adapter;
    ArrayList<String> productList = new ArrayList<String>();
    ArrayList<String> productValueList = new ArrayList<String>();
    String ServerLogoutURL = "http://192.168.40.7:8080/cms/logout?";
    String ServerloadRateURL = "http://192.168.40.7:8080/cms/leasing/charges?";
    String loadProductListURL = "http://192.168.40.7:8080/cms/MobileApp/getMobileProductList";
    String loadCalculatorURL = "http://192.168.40.7:8080/cms/MobileApp/getMobileTrialCal";
    URL url;
    String finalResult,productId,repaymentId;
    HashMap<String,String> hashMap = new HashMap<>();
    HashMap<String,String> hashMap2 = new HashMap<>();

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
        rentalTV = findViewById(R.id.rental);
        amortizedET = findViewById(R.id.amortized);

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
                String amount = amountET.getText().toString();
                String rate = rateET.getText().toString();
                String tenor = tenorET.getText().toString();
                String amortized = amortizedET.getText().toString();
                String repayment = repaymentId;

                System.out.println(amount+rate+tenor+amortized+repayment);

                if(validateAmount() & validateRate() & validateTenor()){
                    //calculate(amount,rate,tenor,amortized,repayment);
                    calculate("1000000.00","15.00","12","0.00",repayment);
                }

            }
        });

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
                    break;
                case 1:
                    repaymentId = "Structured";
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


    public void calculate(String amount,String rate,String tenor,String amortized,String repayment){
        class CalculateFunctionClass extends AsyncTask<String,Void,String>  {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);
                System.out.println(httpResponseMsg);

                /*
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

                 */

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

                System.out.println(hashMap2);

                finalResult = PostRequest.postRequest(url,hashMap);

                return finalResult;
            }
        }

        CalculateFunctionClass calculateFunctionClass = new CalculateFunctionClass();
        calculateFunctionClass.execute(amount,rate,tenor,amortized,repayment);

    }



}