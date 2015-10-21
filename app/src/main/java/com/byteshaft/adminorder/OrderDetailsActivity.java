package com.byteshaft.adminorder;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.byteshaft.adminorder.database.DatabaseHelpers;

import java.util.ArrayList;

public class OrderDetailsActivity extends AppCompatActivity {

    private ListView mListView;
    private String mNumber;
    private DatabaseHelpers mDatabaseHelpers;
    private ArrayList<String> mArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_details);
        mDatabaseHelpers = new DatabaseHelpers(getApplicationContext());
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        mNumber = getIntent().getStringExtra("number");
        setTitle(mNumber);
        mListView = (ListView) findViewById(R.id.detail_listview);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mArrayList = null;
        mArrayList = mDatabaseHelpers.getAllProducts(("table"+ mNumber));
        ArrayAdapter arrayAdapter = new DetailsArrayAdapter(getApplicationContext(),
                R.layout.single_detail_layout, mArrayList);
        mListView.setAdapter(arrayAdapter);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent upIntent = new Intent(this, MainActivity.class);
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpTo(this, upIntent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    class DetailsArrayAdapter extends ArrayAdapter<String> {

        public DetailsArrayAdapter(Context context, int resource, ArrayList<String> videos) {
            super(context, resource, videos);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                LayoutInflater inflater = getLayoutInflater();
                convertView = inflater.inflate(R.layout.single_detail_layout, parent, false);
                holder = new ViewHolder();
                holder.product = (TextView) convertView.findViewById(R.id.product);
                holder.address = (TextView) convertView.findViewById(R.id.address);
                holder.status = (ImageView) convertView.findViewById(R.id.status);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            String product = mArrayList.get(position);
            System.out.println(product);
            String[] details = mDatabaseHelpers.getDetails(mNumber, product);
            holder.product.setText(product);
            holder.address.setText(details[1]);
            String status = details[4];
            if (status.equals("0")) {
                holder.status.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_done_gray));
            } else {
                holder.status.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_done_green));

            }
            return convertView;
        }
    }

    static class ViewHolder {
        public TextView product;
        public TextView address;
        public ImageView status;
    }

}
