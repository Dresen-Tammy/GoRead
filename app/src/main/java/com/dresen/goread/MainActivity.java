package com.dresen.goread;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dresen.goread.adapters.BookListAdapter;
import com.dresen.goread.model.Book;
import com.dresen.goread.model.User;
import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Cookie;
import okhttp3.HttpUrl;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.widget.Toast.makeText;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    RecyclerView mRecyclerView;
    private BookListAdapter mAdapter;
    public ArrayList<Book> booksList = null;
    public static User currentUser;
    ClearableCookieJar cookieJar = null;

    /* *******************************************************************
     * onCreate is called when app starts or when it is awakened from sleep.
     * ******************************************************************* */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        currentUser = new User();
        cookieJar = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(getApplicationContext()));

        try {
            getBooks();
        } catch (IOException e) {
            e.printStackTrace();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        Menu menu = navigationView.getMenu();
        MenuItem logout = menu.findItem(R.id.nav_logout);
        MenuItem register = menu.findItem(R.id.nav_register);
        MenuItem login = menu.findItem(R.id.nav_login);
        //TODO, if logged in hide login/register button. else hide logout
        // TODO logout.setVisible(false);
    }


    /* *******************************************************************
     * Menu items for all of the different app menus.
     * ******************************************************************* */
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.action_title_sort) {
            Collections.sort(booksList, Book.bookTitleComparator);
            mRecyclerView.setAdapter(mAdapter);
            return true;
        }
        if (id == R.id.action_author_sort) {
            Collections.sort(booksList, Book.bookAuthorComparator);
            mRecyclerView.setAdapter(mAdapter);
            return true;
        }


        return super.onOptionsItemSelected(item);
    }


    /* *******************************************************************
     * onNavigationItemSelected is side drawer. It holds Register/login/logout
     * ******************************************************************* */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        // Login
        if (id == R.id.nav_login) {
            loginPopup();


            // Register
        } else if (id == R.id.nav_register) {
            registerPopup();



            // logout
        } else if (id == R.id.nav_logout) {
            logoutPopup();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }




    /* *******************************************************************
     * getBooks gets arrayList of books for main view library recyclerView.
     * ******************************************************************* */
    public void getBooks() throws IOException {
        //TODO get books from database
        final DbService db = new DbService();
        db.getDatabase("books", "", new Callback() {


            @Override
            public void onFailure(Call call, IOException e) {
                MainActivity.this.makeToast("Error loading library");
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                booksList = db.processResults(response);
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter = new BookListAdapter(getApplicationContext(), booksList);
                        mRecyclerView = findViewById(R.id.recyclerView);
                        mRecyclerView.setAdapter(mAdapter);
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
                        mRecyclerView.setLayoutManager(layoutManager);
                        mRecyclerView.setHasFixedSize(true);
                    }
                });
            }
        });
    }



    /* *******************************************************************
     * Register button pressed
     * ******************************************************************* */
    public void registerPopup() {
        // create LayoutInflator to create popup, pass in context
        LayoutInflater factory = LayoutInflater.from(this);
        // get the view for the popup
        final View textEntryView = factory.inflate(R.layout.login_entry, null);
        // get handle on edit texts for popup
        final EditText username = (EditText) textEntryView.findViewById(R.id.editText_username);
        final EditText password = (EditText) textEntryView.findViewById(R.id.editText_password);

        // get user entered text from popup
        username.setText("", TextView.BufferType.EDITABLE);
        password.setText("", TextView.BufferType.EDITABLE);
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        // Set title of popup
        alert.setTitle("Register to add books and reviews").setView(textEntryView)


                // set buttons for popup with onclick listeners
                .setPositiveButton("Register", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //Register
                        try {
                            String user = username.getText().toString();
                            String pass = password.getText().toString();
                            if (user != "" && pass != null) {
                                registerUser(user, pass);
                            } else {
                                registerPopup();
                                makeToast("Fill in Username and Password");
                            }

                        } catch (IOException e) {
                            makeToast("Error registering user");
                            e.printStackTrace();
                        }

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        alert.show();
    }




    /* *******************************************************************
     * Register user by submitting command, username and password to db
     * ******************************************************************* */

    private void registerUser(String username, String password) throws IOException {
        final DbService db = new DbService();
        RequestBody body = db.registerBody("register", username, password);

        db.postDatabase(cookieJar,"register", username, password, new

                Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        makeToast("Error registering user.");
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        // TODO check if user was registered and do if/else to send appropriate message.
                        String message = db.processRegistration(response);
                        makeToast(message);
                    }
                });

    }

    /* *******************************************************************
     * login button pressed, login popup
     * ******************************************************************* */

    private void loginPopup() {
        LayoutInflater factory = LayoutInflater.from(this);
        // get the view for the popup
        final View textEntryView = factory.inflate(R.layout.login_entry, null);
        // get handle on edit texts for popup
        final EditText username = (EditText) textEntryView.findViewById(R.id.editText_username);
        final EditText password = (EditText) textEntryView.findViewById(R.id.editText_password);

        // get user entered text from popup
        username.setText("", TextView.BufferType.EDITABLE);
        password.setText("", TextView.BufferType.EDITABLE);

        // Turn create popup
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        // Set title of popup
        alert.setTitle("Login").setView(textEntryView)
                // set buttons for popup with onclick listeners
                .setPositiveButton("Login", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Login
                        try {
                            String user = username.getText().toString();
                            String pass = password.getText().toString();
                            if (user != "" && pass != null) {
                                loginUser(user, pass);
                            } else {
                                loginPopup();
                                makeToast("Fill in Username and Password");
                            }

                        } catch (IOException e) {
                            makeToast("Error registering user");
                            e.printStackTrace();
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        alert.show();
    }

    private void loginUser(String user, String pass) throws IOException {

        final DbService db = new DbService();
        // create request body
        RequestBody body = db.registerBody("login", user, pass);

        db.postDatabase(cookieJar, "login", user, pass, new

                Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        makeToast("Error logging in.");
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        // TODO check if user was registered and do if/else to send appropriate message.
                        Context context = getApplicationContext();
                        List<Cookie> cookies = cookieJar.loadForRequest(HttpUrl.parse(Constants.LIBRARY_BASE_URL));
                        Boolean loggedin = false;
                        for (Cookie cookie :cookies) {
                            if (cookie != null) {
                                loggedin = true;
                                makeToast("Login Successful");
                            }
                        }
                        if (loggedin = false) {
                            makeToast("Error logging in");
                        }
                    }
                });

    }


    /* *******************************************************************
     * logout button pressed
     * ******************************************************************* */
    public void logoutPopup() {
        // create LayoutInflator to create popup, pass in context
        LayoutInflater factory = LayoutInflater.from(this);
        // get the view for the popup
        final View textEntryView = factory.inflate(R.layout.logout, null);
        // get handle on edit texts for popup

        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        // Set title of popup
        alert.setTitle("Logout").setView(textEntryView)
                // set buttons for popup with onclick listeners
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final DbService db = new DbService();
                        try {
                            db.postDatabase(cookieJar, "logout", currentUser.getUname(), "", new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {
                                    makeToast("Error Logging Out");
                                    e.printStackTrace();
                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    cookieJar.clear();
                                    makeToast("Logged out");

                                }
                            });
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        alert.show();
    }

    /* *******************************************************************
     * toast message
     * ******************************************************************* */
    public void makeToast(final String message) {
        MainActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                CharSequence text = message;
                int duration = Toast.LENGTH_LONG;

                Toast toast = Toast.makeText(getApplicationContext(), text, duration);
                toast.show();
            }
        });
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User user) {
        currentUser = user;
    }
}