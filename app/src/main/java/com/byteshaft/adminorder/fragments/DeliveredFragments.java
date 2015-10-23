package com.byteshaft.adminorder.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.byteshaft.adminorder.R;
import com.byteshaft.adminorder.database.DatabaseHelpers;

import java.util.ArrayList;

public class DeliveredFragments extends Fragment {

    private View mBaseView;
    private ListView mListView;
    private ArrayAdapter<String> mModeAdapter;
    private ArrayList<String> mArrayList;
    private DatabaseHelpers mDatabaseHelpers;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBaseView = inflater.inflate(R.layout.deliver, container, false);
        mDatabaseHelpers = new DatabaseHelpers(getContext());
        mListView = (ListView) mBaseView.findViewById(R.id.list_view_deliver);
        mListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        return mBaseView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mArrayList = mDatabaseHelpers.getAllDeliveredProductsDates();
        mModeAdapter = new DeliveredAdapter(getContext(), R.layout.single_deliver_layout, mArrayList);
        mListView.setAdapter(mModeAdapter);
    }

    class DeliveredAdapter extends ArrayAdapter<String> {


        public DeliveredAdapter(Context context, int resource, ArrayList<String> videos) {
            super(context, resource, videos);
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            if (convertView == null) {
                LayoutInflater inflater = getActivity().getLayoutInflater();
                convertView = inflater.inflate(R.layout.single_deliver_layout, parent, false);
                holder = new ViewHolder();
                holder.product = (TextView) convertView.findViewById(R.id.product);
                holder.address = (TextView) convertView.findViewById(R.id.address);
                holder.deliveryTime = (TextView) convertView.findViewById(R.id.deliveryTime);
                holder.orderPlace = (TextView) convertView.findViewById(R.id.orderPlace);
                holder.receivingTime = (TextView) convertView.findViewById(R.id.receiveTime);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
//            }
            String date = mArrayList.get(position);
            String[] details = mDatabaseHelpers.getDeliveredDetails(date);
                holder.product.setText(details[1]);
                holder.address.setText(details[0]);
                holder.orderPlace.setText(details[2]);
                holder.deliveryTime.setText(details[3]);
                holder.receivingTime.setText(date);
//

            }
            return convertView;

        }
    }


        static class ViewHolder {
            public TextView product;
            public TextView deliveryTime;
            public TextView receivingTime;
            public TextView orderPlace;
            public TextView address;
        }
    }
