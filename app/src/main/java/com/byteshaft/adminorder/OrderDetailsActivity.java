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
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import com.byteshaft.adminorder.database.DatabaseHelpers;

import java.util.ArrayList;

public class OrderDetailsActivity extends AppCompatActivity {

    private ListView mListView;
    private String mName;
    private DatabaseHelpers mDatabaseHelpers;
    private ArrayList<String> mArrayList;


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
    }


    @Override
    protected void onResume() {
        super.onResume();
        mArrayList = null;
        mArrayList = mDatabaseHelpers.getAllProducts(("table" + mName));
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
            final ViewHolder holder;
            if (convertView == null) {
                LayoutInflater inflater = getLayoutInflater();
                convertView = inflater.inflate(R.layout.single_detail_layout, parent, false);
                holder = new ViewHolder();
                holder.product = (TextView) convertView.findViewById(R.id.product);
                holder.address = (TextView) convertView.findViewById(R.id.address);
                holder.switchStatus = (Switch) convertView.findViewById(R.id.switchStatus);
                holder.deliveryTime = (TextView) convertView.findViewById(R.id.deliveryTime);
                holder.orderPlace = (TextView) convertView.findViewById(R.id.orderPlace);
                holder.receivingTime = (TextView) convertView.findViewById(R.id.receiveTime);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            String currentName = mArrayList.get(position);
            String[] details = mDatabaseHelpers.getDetails(mName, currentName);
            holder.product.setText(details[1]);
            holder.address.setText(details[0]);
            holder.orderPlace.setText(details[2]);
            holder.deliveryTime.setText(details[4]);
            holder.receivingTime.setText(details[5]);
            String status = details[3];
            if (status != null) {
                if (status.equals("0")) {
                    holder.switchStatus.setChecked(false);
                    holder.switchStatus.setText("pending");
                } else {
                    holder.switchStatus.setChecked(true);
                    holder.switchStatus.setText("Delivered");
                    holder.switchStatus.setClickable(false);
                }
            }
            holder.switchStatus.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    switch (buttonView.getId()) {
                        case R.id.switchStatus:
                            if (!holder.switchStatus.isChecked()) {
                                break;
                            } else {
                                holder.switchStatus.setText("Delivered");
                                mDatabaseHelpers.updateStatus(mName, "1",
                                        holder.receivingTime.getText().toString());
                                mDatabaseHelpers.addEntryToDeliveredTable(mName,
                                        holder.address.getText().toString(),
                                        holder.product.getText().toString(),
                                        holder.orderPlace.getText().toString(),
                                        holder.deliveryTime.getText().toString(),
                                        holder.receivingTime.getText().toString());
                                holder.switchStatus.setChecked(true);
                                holder.switchStatus.setClickable(false);
                            }
                            break;
                    }
                }
            });
            return convertView;
        }
    }


    static class ViewHolder {
        public TextView product;
        public TextView deliveryTime;
        public TextView receivingTime;
        public TextView orderPlace;
        public TextView address;
        public Switch switchStatus;
    }
}
