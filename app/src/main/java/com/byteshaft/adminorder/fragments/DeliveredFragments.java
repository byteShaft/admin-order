package com.byteshaft.adminorder.fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.byteshaft.adminorder.R;
import com.byteshaft.adminorder.database.DatabaseHelpers;

import java.util.ArrayList;

public class DeliveredFragments extends ListFragment {

    private View mBaseView;
    private ListView mListView;
    private ArrayAdapter<String> mModeAdapter;
    private DatabaseHelpers mDatabaseHelpers;
    private ArrayList<Integer> mItemsToBeDeleted;
    private SparseBooleanArray mSelectedItems;
    private SparseArray<String> mFileNamesReal;
    private ArrayList<String> mArrayList;
    private Parcelable state;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBaseView = inflater.inflate(R.layout.deliver, container, false);
        mDatabaseHelpers = new DatabaseHelpers(getContext());
        mListView = (ListView) mBaseView.findViewById(android.R.id.list);
        mListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        return mBaseView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mItemsToBeDeleted = new ArrayList<>();
        getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        getListView().setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {

            @Override
            public void onItemCheckedStateChanged(ActionMode actionMode, int i, long l, boolean b) {
                if (b) {
                    mSelectedItems.put(i, true);
                } else {
                    mSelectedItems.put(i, false);
                }
                mModeAdapter.notifyDataSetChanged();
            }

            @Override
            public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
                MenuInflater inflater = actionMode.getMenuInflater();
                inflater.inflate(R.menu.main, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
                System.out.println("onPrepareActionMode");
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.delete:
                        for (int i = 0; i < mArrayList.size(); i++) {
                            if (mSelectedItems.get(i)) {
                                Log.i("log",String.valueOf(mSelectedItems.get(i) + " " + mArrayList.get(i)));
                                mDatabaseHelpers.deleteItem(mArrayList.get(i));
                                Toast.makeText(getContext(), "file deleted", Toast.LENGTH_SHORT).show();

                            }
                            Log.i("debug", "delete stuff");
                        }
                        reloadAdapterAndRestoreView(actionMode);
                        return true;
                }
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode actionMode) {
            }
        });
        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
//        registerForContextMenu(getListView());
    }

    private void reloadAdapterAndRestoreView(ActionMode actionMode) {
        mSelectedItems.clear();
        actionMode.finish();
        state = getListView().onSaveInstanceState();
        populateListView();
        getListView().onRestoreInstanceState(state);
    }

    private void populateListView() {
        mArrayList = mDatabaseHelpers.getAllDeliveredProductsDates();
        mModeAdapter = new DeliveredAdapter(getContext(), R.layout.single_deliver_layout, mArrayList);
        mListView.setAdapter(mModeAdapter);
        System.out.println(mArrayList == null);
        mSelectedItems = new SparseBooleanArray(mArrayList.size());
        mFileNamesReal = new SparseArray<>(mArrayList.size());
        for (int i = 0; i < mArrayList.size(); i++) {
            mSelectedItems.put(i, false);
            mFileNamesReal.put(i, mArrayList.get(i));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        populateListView();
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
            }
            String date = mArrayList.get(position);
            String[] details = mDatabaseHelpers.getDeliveredDetails(date);
            holder.product.setText(details[1]);
            holder.address.setText(details[0]);
            holder.orderPlace.setText(details[2]);
            holder.deliveryTime.setText(details[3]);
            holder.receivingTime.setText(date);

            if (mSelectedItems.get(position)) {
                convertView.setBackgroundColor(Color.DKGRAY);
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
