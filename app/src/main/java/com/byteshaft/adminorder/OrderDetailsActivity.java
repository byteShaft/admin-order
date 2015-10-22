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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.byteshaft.adminorder.database.DatabaseHelpers;

import java.util.ArrayList;

public class OrderDetailsActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{

    private ListView mListView;
    private String mName;
    private DatabaseHelpers mDatabaseHelpers;
    private ArrayList<String> mArrayList;
    private static String sCurrentClickValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_details);
        mDatabaseHelpers = new DatabaseHelpers(getApplicationContext());
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        mName = getIntent().getStringExtra("name");
        setTitle(mName);
        mListView = (ListView) findViewById(R.id.detail_listview);
        mListView.setOnItemClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mArrayList = null;
        mArrayList = mDatabaseHelpers.getAllProducts(("table"+ mName));
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        sCurrentClickValue = parent.getItemAtPosition(position).toString();

    }

    class DetailsArrayAdapter extends ArrayAdapter<String> implements View.OnClickListener {

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
                holder.deliveryTime = (TextView) convertView.findViewById(R.id.deliveryTime);
                holder.orderPlace = (TextView) convertView.findViewById(R.id.orderPlace);
                holder.receivingTime = (TextView) convertView.findViewById(R.id.receiveTime);
                convertView.setTag(holder);
                holder.status.setOnClickListener(this);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            String currentName = mArrayList.get(position);
            System.out.println(currentName);
            String[] details = mDatabaseHelpers.getDetails(mName, currentName);
            holder.product.setText(details[1]);
            holder.address.setText(details[0]);
            holder.orderPlace.setText(details[2]);
            holder.deliveryTime.setText(details[4]);
            holder.receivingTime.setText(details[5]);
            String status = details[3];
            if (status.equals("0")) {
                holder.status.setBackgroundDrawable(getResources().getDrawable(R.drawable.white_done));
            } else {
                holder.status.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_done_green));
            }
            return convertView;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.status:
                    System.out.println(sCurrentClickValue);
                    System.out.println("ok");

                    break;
            }

        }
    }

    static class ViewHolder {
        public TextView product;
        public TextView deliveryTime;
        public TextView receivingTime;
        public TextView orderPlace;
        public TextView address;
        public ImageView status;
    }

}
