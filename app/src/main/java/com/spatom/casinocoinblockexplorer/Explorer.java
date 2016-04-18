package com.spatom.casinocoinblockexplorer;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Explorer extends AppCompatActivity {

    public final String TAG = MainActivity.class.getSimpleName();
    private ListView mDrawerList;
    private DrawerLayout mDrawerLayout;
    private ArrayAdapter<String> mAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private String mActivityTitle;
    private Button creditsRefreshButton;
    private TableLayout latest;

    private JSONObject row_1;
    private JSONObject row_2;
    private JSONObject row_3;
    private JSONObject row_4;
    private JSONObject row_5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
//        setTheme(android.R.style.Theme_DeviceDefault_Light_DarkActionBar);
        setContentView(R.layout.activity_explorer);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        Toast.makeText(Explorer.this, "Swipe right to open Drawer", Toast.LENGTH_SHORT).show();

        //START THE HTTP CALL TO GET JSON
        String url = "http://csc.blockexp.info/ext/getlasttxs/5/30";
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        Call call = client.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            //IF GET JSON SUCCEEDS
            @Override
            public void onResponse(Call call, Response response) throws IOException {

                try {
                    String jsonData = response.body().string();
                    Log.v(TAG, jsonData);
                    if (response.isSuccessful()) {

                        try {
                            JSONObject obj = new JSONObject(jsonData);
                            Object row1 = obj.getJSONArray("data").get(0);

                            Object row2 = obj.getJSONArray("data").get(1);
                            Object row3 = obj.getJSONArray("data").get(2);
                            Object row4 = obj.getJSONArray("data").get(3);
                            Object row5 = obj.getJSONArray("data").get(4);

                            row_1 = (JSONObject) row1;
                            row_2 = (JSONObject) row2;
                            row_3 = (JSONObject) row3;
                            row_4 = (JSONObject) row4;
                            row_5 = (JSONObject) row5;

                            row_1.get("txid");

                        } catch (Throwable t) {
                            System.out.println("nojson");
                            Toast.makeText(Explorer.this, "Error with Service, Please Reload.", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        alertUserAboutError();
                    }
                } catch (IOException e) {
                    Log.e(TAG, "x", e);
                }

                latest = (TableLayout) findViewById(R.id.latest_tx);

            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    for(int i = 0; i < 5; i++) {
                        TableRow row = new TableRow(Explorer.this);
                        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
                        lp.setMargins(50, 10, 175 ,10);

                        TextView col1 = new TextView(Explorer.this);
                        TextView col2 = new TextView(Explorer.this);
                        TextView col3 = new TextView(Explorer.this);
                        TextView col4 = new TextView(Explorer.this);
                        TextView col5 = new TextView(Explorer.this);

                        Float total;
                        String finalTotal = "err";
                        try {
                            if(i == 0) {
                                col1.setText(row_1.get("txid").toString());
                                col2.setText(row_1.get("blockhash").toString());
                                col3.setText(row_1.get("blockindex").toString());
                                col4.setText(row_1.get("timestamp").toString());
                                col5.setText(row_1.get("total").toString());
                            }else if(i == 1) {
                                col1.setText(row_2.get("txid").toString());
                                col2.setText(row_2.get("blockhash").toString());
                                col3.setText(row_2.get("blockindex").toString());
                                col4.setText(row_2.get("timestamp").toString());
                                col5.setText(row_2.get("total").toString());
                            }else if(i == 2) {
                                col1.setText(row_3.get("txid").toString());
                                col2.setText(row_3.get("blockhash").toString());
                                col3.setText(row_3.get("blockindex").toString());
                                col4.setText(row_3.get("timestamp").toString());
                                col5.setText(row_3.get("total").toString());
                            }else if(i == 3) {
                                col1.setText(row_4.get("txid").toString());
                                col2.setText(row_4.get("blockhash").toString());
                                col3.setText(row_4.get("blockindex").toString());
                                col4.setText(row_4.get("timestamp").toString());
                                col5.setText(row_4.get("total").toString());
                            }else if(i == 4) {
                                col1.setText(row_5.get("txid").toString());
                                col2.setText(row_5.get("blockhash").toString());
                                col3.setText(row_5.get("blockindex").toString());
                                col4.setText(row_5.get("timestamp").toString());
                                col5.setText(row_5.get("total").toString());
                            }
                        } catch (Throwable e) {
                            System.out.println("bad");
                        }
                        row.addView(col1, lp);
                        row.addView(col2, lp);
                        row.addView(col3, lp);
                        row.addView(col4, lp);
                        row.addView(col5, lp);
                        latest.addView(row, i + 1);
                    }
                }
            });
            }
        });

        mDrawerList = (ListView) findViewById(R.id.navList);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mActivityTitle = getTitle().toString();

        creditsRefreshButton = (Button) findViewById(R.id.explorer_home_button);
        creditsRefreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(getIntent());
            }
        });

        addDrawerItems();
        setupDrawer();

        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

    }

    private void alertUserAboutError() {


    }

    private void addDrawerItems() {
        String[] osArray = {"App Home", "Explorer Home", "Network", "Blocks", "Transactions"};
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, osArray);
        mDrawerList.setAdapter(mAdapter);

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(id == 0) {
                    finish();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                } else if(id == 1) {
                    mDrawerLayout.closeDrawer(Gravity.LEFT);
                    Intent intent = new Intent(getApplicationContext(), Explorer.class);
                    startActivity(intent);
                } else if(id == 2) {
                    mDrawerLayout.closeDrawer(Gravity.LEFT);
                    Intent intent = new Intent(getApplicationContext(), Network.class);
                    startActivity(intent);
                } else if(id == 3) {
                    mDrawerLayout.closeDrawer(Gravity.LEFT);
                    Intent intent = new Intent(getApplicationContext(), Blocks.class);
                    startActivity(intent);
                } else if(id == 4) {
                    mDrawerLayout.closeDrawer(Gravity.LEFT);
                    Intent intent = new Intent(getApplicationContext(), Transactions.class);
                    startActivity(intent);
                }
            }
        });
    }

    private void setupDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if(getSupportActionBar() != null) {
                    getSupportActionBar().setTitle("Navigation!");
                }
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                if(getSupportActionBar() != null) {
                    getSupportActionBar().setTitle(mActivityTitle);
                }
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

        // Activate the navigation drawer toggle
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}