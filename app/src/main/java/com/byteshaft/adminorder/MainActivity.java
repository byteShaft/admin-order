package com.byteshaft.adminorder;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.byteshaft.adminorder.database.DatabaseHelpers;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, AdapterView.OnItemClickListener {

    private ArrayList<String> ordersPhoneNumber;
    private ListView mListView;
    private DatabaseHelpers mDatabaseHelpers;
    private ArrayAdapter<String> mArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mListView = (ListView) findViewById(R.id.listView);
        mDatabaseHelpers = new DatabaseHelpers(AppGlobals.getContext());
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ordersPhoneNumber = null;
        ordersPhoneNumber = mDatabaseHelpers.getAllPhoneNumbers();
        mArrayAdapter = new PhoneArrayAdapter(getApplicationContext(), R.layout.row,
                ordersPhoneNumber);
        mListView.setAdapter(mArrayAdapter);
        mListView.setOnItemClickListener(this);
        mListView.setDivider(null);
    }

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
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.orders) {
//            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        } else if (id == R.id.deliver) {
//            getSupportFragmentManager().beginTransaction()
//                    .add(R.id.container, new DeliveredFragments())
//                    .commit();
        }  else if (id == R.id.nav_share) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getApplicationContext(), OrderDetailsActivity.class);
        intent.putExtra("name", parent.getItemAtPosition(position).toString());
        startActivity(intent);

    }

    class PhoneArrayAdapter extends ArrayAdapter<String> {

        public PhoneArrayAdapter(Context context, int resource, ArrayList<String> videos) {
            super(context, resource, videos);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                LayoutInflater inflater = getLayoutInflater();
                convertView = inflater.inflate(R.layout.row, parent, false);
                holder = new ViewHolder();
                holder.number = (TextView) convertView.findViewById(R.id.number);
                holder.latestProduct = (TextView) convertView.findViewById(R.id.latestProducts);
                holder.status = (ImageView) convertView.findViewById(R.id.delivery_time);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            String name = ordersPhoneNumber.get(position);
            String latestItem = mDatabaseHelpers.getLatestOrder(name);
            Log.i("that", latestItem);
            holder.latestProduct.setText(latestItem);
            holder.number.setText(name);
            if (mDatabaseHelpers.getShippingStatus(name)) {
                holder.status.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_done_gray));
            } else {
                holder.status.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_done_green));

            }
            return convertView;
        }
    }

    static class ViewHolder {
        public TextView number;
        public TextView latestProduct;
        public ImageView status;
    }



}
