package com.example.cms.HelperClass;

import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class PostRequest {

    //list of cookies
    public static List<String> cookies;


    public static String postRequest(URL url, HashMap<String, String> Data) {

        try {
            URL posturl= url;

            HttpURLConnection httpURLConnection = (HttpURLConnection) posturl.openConnection();
            httpURLConnection.setInstanceFollowRedirects(false);

            if(cookies!=null && cookies.size()>0){
                httpURLConnection.setRequestProperty("Cookie", TextUtils.join(";",cookies));

                System.out.printf("cookie sent");
                System.out.println(cookies);
            }

            httpURLConnection.setReadTimeout(60000);

            httpURLConnection.setConnectTimeout(60000);

            httpURLConnection.setRequestMethod("POST");

            httpURLConnection.setDoInput(true);

            httpURLConnection.setDoOutput(true);

            OutputStream outputStream = httpURLConnection.getOutputStream();

            BufferedWriter bufferedWriter = new BufferedWriter(

                    new OutputStreamWriter(outputStream, "UTF-8"));

            bufferedWriter.write(FinalDataParse(Data));

            bufferedWriter.flush();

            bufferedWriter.close();

            outputStream.close();

            if (httpURLConnection.getResponseCode() == HttpsURLConnection.HTTP_OK) {

                if(httpURLConnection.getHeaderFields().get("Set-Cookie")!=null){
                    cookies=httpURLConnection.getHeaderFields().get("Set-Cookie");
                    System.out.println(httpURLConnection.getHeaderFields());
                    System.out.printf("cookie received");
                    System.out.println(cookies);
                }

                BufferedReader bufferedReader = new BufferedReader(
                        new InputStreamReader(
                                httpURLConnection.getInputStream()
                        )
                );
                return bufferedReader.readLine();
            }
            else if (httpURLConnection.getResponseCode()>=300 & (httpURLConnection.getResponseCode()<=399)){

                return String.valueOf(httpURLConnection.getResponseCode());
            }
            else {
                return null;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return  null;
        }

    }

    public static String FinalDataParse(HashMap<String, String> hashMap2) throws UnsupportedEncodingException {

        StringBuilder stringBuilder = new StringBuilder();

        for(Map.Entry<String, String> map_entry : hashMap2.entrySet()){

            stringBuilder.append("&");

            stringBuilder.append(URLEncoder.encode(map_entry.getKey(), "UTF-8"));

            stringBuilder.append("=");

            stringBuilder.append(URLEncoder.encode(map_entry.getValue(), "UTF-8"));

            System.out.println(stringBuilder);

        }


        return stringBuilder.toString();


    }

    public static String getData(URL url) {

        try {
            URL posturl= url;

            HttpURLConnection httpURLConnection = (HttpURLConnection) posturl.openConnection();

            if(cookies!=null && cookies.size()>0){
                httpURLConnection.setRequestProperty("Cookie", TextUtils.join(";",cookies));

                System.out.printf("cookie sent");
                System.out.println(cookies);
            }

            httpURLConnection.setReadTimeout(60000);

            httpURLConnection.setConnectTimeout(60000);

            httpURLConnection.setRequestMethod("POST");

            httpURLConnection.setDoInput(true);

            httpURLConnection.setDoOutput(true);

            OutputStream  outputStream = httpURLConnection.getOutputStream();

            outputStream.close();

            if (httpURLConnection.getResponseCode() == HttpsURLConnection.HTTP_OK) {

                if(httpURLConnection.getHeaderFields().get("Set-Cookie")!=null){
                    cookies=httpURLConnection.getHeaderFields().get("Set-Cookie");
                    System.out.printf("cookie received");
                    System.out.println(cookies);
                }


                BufferedReader bufferedReader = new BufferedReader(
                        new InputStreamReader(
                                httpURLConnection.getInputStream()
                        )
                );
                return bufferedReader.readLine();

            }
            else if (httpURLConnection.getResponseCode()>=300 & (httpURLConnection.getResponseCode()<=399)){

                String redir = httpURLConnection.getHeaderField("Location");
                if (redir!=null && redir.equals("login")){
                    return "auth_fail";
                }
                return null;
            }
            else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return  null;
        }

    }

    public static String logout(URL url) {

        try {
            URL posturl= url;

            HttpURLConnection httpURLConnection = (HttpURLConnection) posturl.openConnection();
            httpURLConnection.setInstanceFollowRedirects(false);
            if(cookies!=null && cookies.size()>0){
                httpURLConnection.setRequestProperty("Cookie", TextUtils.join(";",cookies));

                System.out.printf("cookie sent");
                System.out.println(cookies);
            }

            httpURLConnection.setReadTimeout(60000);

            httpURLConnection.setConnectTimeout(60000);

            httpURLConnection.setRequestMethod("POST");

            httpURLConnection.setDoInput(true);

            httpURLConnection.setDoOutput(true);

            OutputStream  outputStream = httpURLConnection.getOutputStream();

            BufferedWriter bufferedWriter = new BufferedWriter(

                    new OutputStreamWriter(outputStream, "UTF-8"));

            //bufferedWriter.write(FinalDataParse(Data));

            bufferedWriter.flush();

            bufferedWriter.close();

            outputStream.close();

            if (httpURLConnection.getResponseCode() == HttpsURLConnection.HTTP_OK) {

                if(httpURLConnection.getHeaderFields().get("Set-Cookie")!=null){
                    cookies=httpURLConnection.getHeaderFields().get("Set-Cookie");

                }

                BufferedReader bufferedReader = new BufferedReader(
                        new InputStreamReader(
                                httpURLConnection.getInputStream()
                        )
                );
                return bufferedReader.readLine();
            }
            else if (httpURLConnection.getResponseCode()>=300 & (httpURLConnection.getResponseCode()<=399)){
                String redir = httpURLConnection.getHeaderField("Location");
                if (redir!=null && redir.equals("login")){
                    return "auth_fail";
                }
                return null;
            }
            else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return  null;
        }

    }




    public static String jsonpostRequest(URL url, HashMap<String, JSONObject> Data) {

        try {
            URL posturl= url;

            HttpURLConnection httpURLConnection = (HttpURLConnection) posturl.openConnection();
            httpURLConnection.setInstanceFollowRedirects(false);

            if(cookies!=null && cookies.size()>0){
                httpURLConnection.setRequestProperty("Cookie", TextUtils.join(";",cookies));

                System.out.printf("cookie sent");
                System.out.println(cookies);
            }

            httpURLConnection.setReadTimeout(60000);

            httpURLConnection.setConnectTimeout(60000);

            httpURLConnection.setRequestMethod("POST");

            httpURLConnection.setDoInput(true);

            httpURLConnection.setDoOutput(true);

            OutputStream outputStream = httpURLConnection.getOutputStream();

            BufferedWriter bufferedWriter = new BufferedWriter(

                    new OutputStreamWriter(outputStream, "UTF-8"));

            bufferedWriter.write(jsonFinalDataParse(Data));

            bufferedWriter.flush();

            bufferedWriter.close();

            outputStream.close();

            if (httpURLConnection.getResponseCode() == HttpsURLConnection.HTTP_OK) {

                if(httpURLConnection.getHeaderFields().get("Set-Cookie")!=null){
                    cookies=httpURLConnection.getHeaderFields().get("Set-Cookie");
                    System.out.println(httpURLConnection.getHeaderFields());
                    System.out.printf("cookie received");
                    System.out.println(cookies);
                }

                BufferedReader bufferedReader = new BufferedReader(
                        new InputStreamReader(
                                httpURLConnection.getInputStream()
                        )
                );
                return bufferedReader.readLine();
            }
            else if (httpURLConnection.getResponseCode()>=300 & (httpURLConnection.getResponseCode()<=399)){

                return String.valueOf(httpURLConnection.getResponseCode());
            }
            else {
                return null;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return  null;
        }

    }

    public static String jsonFinalDataParse(HashMap<String, JSONObject> hashMap2) throws UnsupportedEncodingException {

        StringBuilder stringBuilder = new StringBuilder();

        for(Map.Entry<String, JSONObject> map_entry : hashMap2.entrySet()){

            stringBuilder.append("&");

            stringBuilder.append(URLEncoder.encode(map_entry.getKey(), "UTF-8"));

            stringBuilder.append("=");

            stringBuilder.append(URLEncoder.encode(String.valueOf(map_entry.getValue()), "UTF-8"));

            System.out.println(stringBuilder);

        }


        return stringBuilder.toString();


    }



}
