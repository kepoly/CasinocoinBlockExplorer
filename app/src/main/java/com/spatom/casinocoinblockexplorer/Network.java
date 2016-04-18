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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Network extends AppCompatActivity {

    public final String TAG = MainActivity.class.getSimpleName();
    private ListView mDrawerList;
    private DrawerLayout mDrawerLayout;
    private ArrayAdapter<String> mAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private String mActivityTitle;
    private Button networkRefreshButton;
    private TableLayout latest;

    private TextView networkHashRate;
    private TextView networkDifficulty;
    private TextView networkCoinSupply;
    private TextView networkLastPrice;
    private TextView networkBlockCount;

    private JSONObject row_1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network);

        networkRefreshButton = (Button) findViewById(R.id.network_home_button);
        networkRefreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(getIntent());
            }
        });

        mDrawerList = (ListView) findViewById(R.id.navList);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        addDrawerItems();
        setupDrawer();

        networkHashRate = (TextView) findViewById(R.id.hash_rate_view);
        networkDifficulty = (TextView) findViewById(R.id.difficulty_view);
        networkCoinSupply = (TextView) findViewById(R.id.coin_supply_view);
        networkLastPrice = (TextView) findViewById(R.id.price_view);
        networkBlockCount = (TextView) findViewById(R.id.block_count_view);

        String urlHash = "http://csc.blockexp.info/ext/summary";
        OkHttpClient client = new OkHttpClient();
        Request requestHash = new Request.Builder().url(urlHash).build();
        Call callHash = client.newCall(requestHash);
        callHash.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {

                try {
                    String jsonData = response.body().string();
                    Log.v(TAG, jsonData);
                    if (response.isSuccessful()) {
                        try {

                            JSONObject obj = new JSONObject(jsonData);
                            Object row1 = obj.getJSONArray("data").get(0);
                            row_1 = (JSONObject) row1;
                            runGetData();
                        } catch (Throwable t) {
                            System.out.println("nojson");
                            Toast.makeText(Network.this, "Error with Service, Please Reload.", Toast.LENGTH_SHORT).show();

                        }
                    } else {
                        alertUserAboutError();
                    }
                } catch (IOException e) {
                    Log.e(TAG, "x", e);
                }
            }
        });
    }

    private void runGetData() {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                String hashText = "err";
                String diff = "err";
                String supply = "err";
                String price = "err";
                String block = "err";
                try {
                    hashText = row_1.get("hashrate").toString();
                    diff = row_1.get("difficulty").toString();
                    supply = row_1.get("supply").toString();
                    price = String.format("%.8f", row_1.get("lastPrice"));
                    block = row_1.get("blockcount").toString();
                    //get the text views final json value and set it to a string
                } catch (Throwable e) {
                    System.out.println("bad");
                }
                //set the text view here
                networkHashRate.setText(hashText);
                networkDifficulty.setText(diff);
                networkCoinSupply.setText(supply);
                networkLastPrice.setText(price.toString());
                networkBlockCount.setText(block);
            }
        });
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
