package com.byteshaft.adminorder;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.byteshaft.adminorder.database.DatabaseHelpers;
import com.byteshaft.adminorder.fragments.DeliveredFragments;
import com.byteshaft.adminorder.fragments.OrderFragment;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<String> ordersPhoneNumber;
    private ListView mListView;
    private DatabaseHelpers mDatabaseHelpers;
    private ArrayAdapter<String> mArrayAdapter;
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mListView = (ListView) findViewById(R.id.listView);
        mDatabaseHelpers = new DatabaseHelpers(AppGlobals.getContext());
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        setupDrawerContent(navigationView);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }

    public void selectDrawerItem(MenuItem menuItem) {
        Fragment fragment = null;

        Class fragmentClass;
        switch (menuItem.getItemId()) {
            case R.id.orders_fragment:
                fragmentClass = DeliveredFragments.class;
                break;
            case R.id.deliver_fragment:
                fragmentClass = OrderFragment.class;
                break;
            default:
                fragmentClass = DeliveredFragments.class;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();

        // Highlight the selected item, update the title, and close the drawer
        menuItem.setChecked(true);
        setTitle(menuItem.getTitle());
        drawer.closeDrawers();

    }


    @Override
    protected void onResume() {
        super.onResume();
        ordersPhoneNumber = null;
        ordersPhoneNumber = mDatabaseHelpers.getAllPhoneNumbers();
        mArrayAdapter = new PhoneArrayAdapter(getApplicationContext(), R.layout.row,
                ordersPhoneNumber);
        mListView.setAdapter(mArrayAdapter);
        mListView.setDivider(null);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
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
                holder.deliveryTime = (TextView) convertView.findViewById(R.id.delivery_time);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            String number = ordersPhoneNumber.get(position);
            holder.number.setText(number);
            holder.deliveryTime.setText(mDatabaseHelpers.getDeliveryTime(number));
            return convertView;
        }
    }

    static class ViewHolder {
        public TextView number;
        public TextView deliveryTime;
    }
}
