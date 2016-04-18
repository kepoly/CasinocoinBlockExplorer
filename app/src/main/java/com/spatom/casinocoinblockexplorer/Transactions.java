package com.spatom.casinocoinblockexplorer;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Transactions extends AppCompatActivity {

    public final String TAG = MainActivity.class.getSimpleName();
    private ListView mDrawerList;
    private DrawerLayout mDrawerLayout;
    private ArrayAdapter<String> mAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private String mActivityTitle;
    private Button transactionsRefreshButton;
    private TableLayout latest;
    private EditText enterInfo;

    private JSONObject row_1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        String transId = "2c017c5706236155bf0748e4c2b7aa92859cb14b7ab746c6cd75848a48443757";
        String url = "http://csc.blockexp.info/api/getrawtransaction?txid=";
        url += transId;
        String params = "&decrypt=1";
        url += params;

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        Call call = client.newCall(request);

        call.enqueue(new Callback() {
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
                            Object row1 = obj;
                            row_1 = (JSONObject) row1;
                        } catch (Throwable t) {
                            System.out.println("nojson");
                            Toast.makeText(Transactions.this, "Error with Service, Please Reload.", Toast.LENGTH_SHORT).show();

                        }

                    } else {
                        alertUserAboutError();
                    }
                } catch (IOException e) {
                    Log.e(TAG, "x", e);
                }

                latest = (TableLayout) findViewById(R.id.latest_trans);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        TextView col1 = new TextView(Transactions.this);
                        TextView col2 = new TextView(Transactions.this);
                        TextView col3 = new TextView(Transactions.this);
                        TextView col4 = new TextView(Transactions.this);
                        TextView col5 = new TextView(Transactions.this);
                        try {

                            col1.setText(row_1.getString("blockhash").toString());
                            col1.setTextColor(Color.BLUE);
                            col2.setText(row_1.getString("txid"));
                            col2.setTextColor(Color.BLUE);
                            col3.setText(row_1.getString("confirmations").toString());
                            col3.setTextColor(Color.BLUE);
                            col4.setText(row_1.getString("blocktime").toString());
                            col4.setTextColor(Color.BLUE);
                            col5.setText(row_1.getString("time").toString());
                            col5.setTextColor(Color.BLUE);

                        } catch (Throwable e) {
                            System.out.println("bad");
                        }

                        TableRow row = new TableRow(Transactions.this);
                        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT);
                        lp.setMargins(50, 10, 175, 10);

                        row.addView(col1, lp);
                        row.addView(col2, lp);
                        row.addView(col3, lp);
                        row.addView(col4, lp);
                        row.addView(col5, lp);
                        latest.addView(row, 1);
                    }
                });
            }
        });

        /*
        *TABLE
        */
        mDrawerList = (ListView) findViewById(R.id.navList);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mActivityTitle = getTitle().toString();

        enterInfo = (EditText) findViewById(R.id.editTextTransactions);

        enterInfo.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    refreshTableWithNewData(enterInfo.getText().toString());
                    return true;
                }
                return false;

            }
        });

        transactionsRefreshButton = (Button) findViewById(R.id.transactions_home_button);
        transactionsRefreshButton.setOnClickListener(new View.OnClickListener() {
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


    private void refreshTableWithNewData(String tx) {

        String transId = tx;
        String url = "http://csc.blockexp.info/api/getrawtransaction?txid=";
        url += transId;
        String params = "&decrypt=1";
        url += params;

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        Call call = client.newCall(request);

        call.enqueue(new Callback() {
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

                            Object row1 = obj;
                            row_1 = (JSONObject) row1;
                        } catch (Throwable t) {
                            System.out.println("nojson");
                            Toast.makeText(Transactions.this, "Error with Service, Please Reload.", Toast.LENGTH_SHORT).show();

                        }

                    } else {
                        alertUserAboutError();
                    }
                } catch (IOException e) {
                    Log.e(TAG, "x", e);
                }

                latest = (TableLayout) findViewById(R.id.latest_trans);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        TextView col1 = new TextView(Transactions.this);
                        TextView col2 = new TextView(Transactions.this);
                        TextView col3 = new TextView(Transactions.this);
                        TextView col4 = new TextView(Transactions.this);
                        TextView col5 = new TextView(Transactions.this);
                        try {

                            col1.setText(row_1.getString("blockhash").toString());
                            col1.setTextColor(Color.BLUE);
                            col2.setText(row_1.getString("txid"));
                            col2.setTextColor(Color.BLUE);
                            col3.setText(row_1.getString("confirmations").toString());
                            col3.setTextColor(Color.BLUE);
                            col4.setText(row_1.getString("blocktime").toString());
                            col4.setTextColor(Color.BLUE);
                            col5.setText(row_1.getString("time").toString());
                            col5.setTextColor(Color.BLUE);

                        } catch (Throwable e) {
                            System.out.println("bad");
                        }

                        TableRow row = new TableRow(Transactions.this);
                        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT);
                        lp.setMargins(50, 10, 175, 10);

                        row.addView(col1, lp);
                        row.addView(col2, lp);
                        row.addView(col3, lp);
                        row.addView(col4, lp);
                        row.addView(col5, lp);
                        latest.addView(row, 1);
                    }
                });
            }
        });
    }
    private void alertUserAboutError() {

    }

     /* INSERT INTO THE DRAWER */

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
