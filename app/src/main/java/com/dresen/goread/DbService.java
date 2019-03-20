package com.dresen.goread;

import android.util.Log;
import android.widget.ArrayAdapter;

import com.dresen.goread.model.Book;
import com.dresen.goread.model.User;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;


import java.io.IOException;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;


import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Cookie;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/*
*  DbService has post and get requests to database.
 */
public class DbService {

    public static void postDatabase(ClearableCookieJar cookieJar, String command, String user, String pass, Callback callback) throws IOException {

        // build client to use to send request
        OkHttpClient client = new OkHttpClient.Builder()
                .cookieJar(cookieJar)
                .build();
        // add the url and the body
        RequestBody body = new FormBody.Builder()
                .add("command", command)
                .add("username", user)
                .add("password", pass)
                .build();

        Request request = new Request.Builder()
                .url(Constants.LIBRARY_BASE_URL)
                .post(body)
                .build();
        // create call to database
        Call call = client.newCall(request);
        // execute call (Enqueue is an okhttp method that runs asynchronously.
        // When the response comes, it calls callback method
        call.enqueue(callback);

    }

    public static void getDatabase(String cvalue, String ivalue, Callback callback) throws IOException {
        // build client to use the send request
        OkHttpClient client = new OkHttpClient.Builder()
                .build();

        // build url string with parameter
        HttpUrl.Builder urlBuilder = HttpUrl.parse(Constants.LIBRARY_BASE_URL).newBuilder();
        urlBuilder.addQueryParameter(Constants.COMMAND, cvalue);
        urlBuilder.addQueryParameter(Constants.ID, ivalue);
        String url = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(url)
                .build();

        Call call = client.newCall(request);
        call.enqueue(callback);
    }
    public RequestBody registerBody(String command,String user, String pass) {
        RequestBody body = new FormBody.Builder()
                .add("command", command)
                .add("username", user)
                .add("password", pass)
                .build();
        return body;
    }

    public ArrayList<Book> processResults(Response response) throws IOException {
        System.out.println(response);
        String jsonData = response.body().string();
        System.out.println(jsonData);
        ObjectMapper mapper = new ObjectMapper();

        ArrayList<Book> books = (ArrayList<Book>) mapper.readValue(jsonData, ArrayList.class);
        ArrayList<Book> newBooks = mapper.convertValue(books, new TypeReference<ArrayList<Book>>(){});


        return newBooks;
    }

    public String processRegistration(Response response) throws IOException {
        String jsonData = response.body().string();
        System.out.println(jsonData);
        ObjectMapper mapper = new ObjectMapper();

        HashMap<String, Object> result = mapper.readValue(jsonData, HashMap.class);
        String message = (String) result.get("message");
        if (message.contains("success")) {
            String theUser = (String) result.get("user");
            User user = mapper.readValue(theUser, User.class);
            MainActivity.setCurrentUser(user);
            return "Registration successful. Login";
        } else if (message.contains("retry")) {
            return "Username not available. Choose a different one.";
        } else {
            return "Error with registration. Try again";
        }

    }

    public String processLogin(Response response) {
        System.out.println("process Login reached");
        return null;
    }
}
