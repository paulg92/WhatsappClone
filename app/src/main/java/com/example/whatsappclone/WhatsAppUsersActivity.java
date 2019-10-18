package com.example.whatsappclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.List;

public class WhatsAppUsersActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private ListView mListView;
    private ArrayList<String> whatsAppUsers;
    private ArrayAdapter<String> mAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whats_app_users);

        FancyToast.makeText(this, "welcome " + ParseUser.getCurrentUser().getUsername(),
                Toast.LENGTH_LONG, FancyToast.INFO, true).show();

        mListView = findViewById(R.id.my_listView);
        whatsAppUsers = new ArrayList<>();
        mAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, whatsAppUsers);
        mSwipeRefreshLayout = findViewById(R.id.swipeRefresh);

        try {
            ParseQuery<ParseUser> query = ParseUser.getQuery();
            query.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername());
            query.findInBackground(new FindCallback<ParseUser>() {
                @Override
                public void done(List<ParseUser> objects, ParseException e) {
                    if (objects.size() > 0 && e == null) {
                        for (ParseUser user : objects) {
                            whatsAppUsers.add(user.getUsername());
                        }
                        mListView.setAdapter(mAdapter);
                        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                String clickedUser = (String) mListView.getItemAtPosition(position);
                                FancyToast.makeText(WhatsAppUsersActivity.this,
                                        clickedUser, FancyToast.LENGTH_SHORT
                                        , FancyToast.SUCCESS, true).show();
                            }
                        });
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                try {
                    ParseQuery<ParseUser> parseQuery = ParseUser.getQuery();
                    parseQuery.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername());
                    parseQuery.whereNotContainedIn("username", whatsAppUsers);
                    parseQuery.findInBackground(new FindCallback<ParseUser>() {
                        @Override
                        public void done(List<ParseUser> objects, ParseException e) {
                            if (objects.size() > 0 && e == null) {
                                for (ParseUser user : objects) {
                                    whatsAppUsers.add(user.getUsername());
                                }
                                mAdapter.notifyDataSetChanged();
                                if (mSwipeRefreshLayout.isRefreshing()) {
                                    mSwipeRefreshLayout.setRefreshing(false);
                                }
                            } else {
                                if (mSwipeRefreshLayout.isRefreshing()) {
                                    mSwipeRefreshLayout.setRefreshing(false);
                                }
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case R.id.logoutUserItem:
                ParseUser.getCurrentUser().logOutInBackground(new LogOutCallback() {
                    @Override
                    public void done(ParseException e) {
                        Intent intent = new Intent(WhatsAppUsersActivity.this,
                                SignUpActivity.class);
                        startActivity(intent);
                    }
                });
                break;
            case R.id.menu_refresh:
                mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        try {
                            ParseQuery<ParseUser> parseQuery = ParseUser.getQuery();
                            parseQuery.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername());
                            parseQuery.whereNotContainedIn("username", whatsAppUsers);
                            parseQuery.findInBackground(new FindCallback<ParseUser>() {
                                @Override
                                public void done(List<ParseUser> objects, ParseException e) {
                                    if (objects.size() > 0 && e == null) {
                                        for (ParseUser user : objects) {
                                            whatsAppUsers.add(user.getUsername());
                                        }
                                        mAdapter.notifyDataSetChanged();
                                        if (mSwipeRefreshLayout.isRefreshing()) {
                                            mSwipeRefreshLayout.setRefreshing(false);
                                        }
                                    } else {
                                        if (mSwipeRefreshLayout.isRefreshing()) {
                                            mSwipeRefreshLayout.setRefreshing(false);
                                        }
                                    }
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onRefresh() {
    }
}
