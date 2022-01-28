package com.example.cms.HelperClass;

import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import android.text.TextUtils;

public class MultipartRequest {

    private final String boundary;
    private static final String LINE_FEED = "\r\n";
    private HttpURLConnection httpConn;
    private String charset;
    private OutputStream outputStream;
    private PrintWriter writer;

    public MultipartRequest(String requestURL)
            throws IOException {
        this.charset = "UTF-8";

        // creates a unique boundary based on time stamp
        boundary = "===" + System.currentTimeMillis() + "===";

        URL url = new URL(requestURL);
        httpConn = (HttpURLConnection) url.openConnection();
        if (PostRequest.cookies != null && PostRequest.cookies.size() > 0) {
            httpConn.setRequestProperty("Cookie", TextUtils.join(";", PostRequest.cookies));

            System.out.printf("cookie sent");
            System.out.println(PostRequest.cookies);
        }

        httpConn.setReadTimeout(60000);

        httpConn.setConnectTimeout(60000);

        httpConn.setRequestMethod("POST");

        httpConn.setDoInput(true);

        httpConn.setDoOutput(true);
        httpConn.setRequestProperty("Content-Type",
                "multipart/form-data; boundary=" + boundary);
       // httpConn.setRequestProperty("Cookie", "JSESSIONID=37BEDA4A8FC9B7ED43C6171CC3E46348");
        outputStream = httpConn.getOutputStream();
        writer = new PrintWriter(new OutputStreamWriter(outputStream, charset),
                true);
    }

    public void addFormField(String name, String value) {
        writer.append("--" + boundary).append(LINE_FEED);
        writer.append("Content-Disposition: form-data; name=\"" + name + "\"")
                .append(LINE_FEED);
        writer.append("Content-Type: text/plain; charset=" + charset).append(
                LINE_FEED);
        writer.append(LINE_FEED);
        writer.append(value).append(LINE_FEED);
        writer.flush();
    }

    public void addFilePart(String fieldName, File uploadFile)
            throws IOException {
        String fileName = uploadFile.getName();
        writer.append("--" + boundary).append(LINE_FEED);
        writer.append(
                "Content-Disposition: form-data; name=\"" + fieldName
                        + "\"; filename=\"" + fileName + "\"")
                .append(LINE_FEED);
        writer.append(
                "Content-Type: "
                        + URLConnection.guessContentTypeFromName(fileName))
                .append(LINE_FEED);
        writer.append("Content-Transfer-Encoding: binary").append(LINE_FEED);
        writer.append(LINE_FEED);
        writer.flush();

        FileInputStream inputStream = new FileInputStream(uploadFile);
        byte[] buffer = new byte[4096];
        int bytesRead = -1;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
        outputStream.flush();
        inputStream.close();

        writer.append(LINE_FEED);
        writer.flush();
    }

    public void addHeaderField(String name, String value) {
        writer.append(name + ": " + value).append(LINE_FEED);
        writer.flush();
    }

    public String finish() throws IOException {

        writer.append(LINE_FEED).flush();
        writer.append("--" + boundary + "--").append(LINE_FEED);
        writer.close();

        // checks server's status code first
        int status = httpConn.getResponseCode();
        System.out.println("status is ---- "+status);
        if (status == HttpURLConnection.HTTP_OK) {
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(
                            httpConn.getInputStream()
                    )
            );
            return bufferedReader.readLine();
        } else if (httpConn.getResponseCode() >= 300 & (httpConn.getResponseCode() <= 399)) {

            return String.valueOf(httpConn.getResponseCode());
        } else {
            return null;
        }

    }


}
