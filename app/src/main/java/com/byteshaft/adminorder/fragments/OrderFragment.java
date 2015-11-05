package com.byteshaft.adminorder.fragments;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.byteshaft.adminorder.AppGlobals;
import com.byteshaft.adminorder.OrderDetailsActivity;
import com.byteshaft.adminorder.R;
import com.byteshaft.adminorder.database.DatabaseHelpers;

import java.util.ArrayList;

public class OrderFragment  extends Fragment  implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private View mBaseView;
    private ArrayList<String> ordersPhoneNumber;
    private ListView mListView;
    private DatabaseHelpers mDatabaseHelpers;
    private ArrayAdapter<String> mArrayAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBaseView = inflater.inflate(R.layout.content_main, container, false);
        mListView = (ListView) mBaseView.findViewById(R.id.listView);
        mDatabaseHelpers = new DatabaseHelpers(AppGlobals.getContext());
        return mBaseView;
    }

    @Override
    public void onResume() {
        super.onResume();
        ordersPhoneNumber = null;
        ordersPhoneNumber = mDatabaseHelpers.getAllCustomerName();
        mArrayAdapter = new PhoneArrayAdapter(getContext(), R.layout.row,
                ordersPhoneNumber);
        mListView.setAdapter(mArrayAdapter);
        mListView.setOnItemClickListener(this);
        mListView.setOnItemLongClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getContext(), OrderDetailsActivity.class);
        intent.putExtra("name", parent.getItemAtPosition(position).toString());
        startActivity(intent);

    }

    @Override
    public boolean onItemLongClick(final AdapterView<?> parent, View view, final int position, long id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Delete");
        builder.setMessage("Do you want to delete this Rule?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mDatabaseHelpers.dropTable(parent.getItemAtPosition(position).toString());
                mDatabaseHelpers.deleteOrder(parent.getItemAtPosition(position).toString());
                String item = mArrayAdapter.getItem(position);
                mArrayAdapter.remove(item);
                mArrayAdapter.notifyDataSetChanged();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();

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
                LayoutInflater inflater = getActivity().getLayoutInflater();
                convertView = inflater.inflate(R.layout.row, parent, false);
                holder = new ViewHolder();
                holder.name = (TextView) convertView.findViewById(R.id.number);
                holder.latestProduct = (TextView) convertView.findViewById(R.id.latestProducts);
                   holder.status = (ImageView) convertView.findViewById(R.id.status_for_user);
                holder.phoneNumber = (TextView) convertView.findViewById(R.id.phoneNumber);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            String name = ordersPhoneNumber.get(position);
            String latestItem = mDatabaseHelpers.getLatestOrder(name);
            String[] number = mDatabaseHelpers.getPhoneNumber(name);
            holder.phoneNumber.setText(number[0]);
            if (latestItem != null) {
                holder.latestProduct.setText(latestItem);
            }
            holder.name.setText(name);
            if (mDatabaseHelpers.getShippingStatus(name)) {
                holder.status.setBackgroundDrawable(getResources().getDrawable(R.drawable.white_done));
            } else {
                holder.status.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_done_green));
            }
            return convertView;
        }
    }

    static class ViewHolder {
        public TextView name;
        public TextView phoneNumber;
        public TextView latestProduct;
        public ImageView status;
    }
}
