package com.byteshaft.adminorder.database;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.byteshaft.adminorder.AppGlobals;

import java.util.ArrayList;

public class DatabaseHelpers extends SQLiteOpenHelper {

    public DatabaseHelpers(Context context) {
        super(context, DatabaseConstants.DATABASE_NAME, null, DatabaseConstants.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DatabaseConstants.CREATE_PARENT_TABLE);
        Log.i(AppGlobals.getLogTag(getClass()), "Database created !!!");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" + DatabaseConstants.TABLE_NAME);
        onCreate(db);
    }

    public boolean insertValuesCheckIfNotExist(String number, String name, String address, String product,
                                   String orderPlace, String orderTime, String status,
                                   String currentTimeDate) {
        SQLiteDatabase myDb = this.getWritableDatabase();
        boolean exist;
        try {
            Cursor c = myDb.rawQuery("select * from " + ("table"+number), null);
            Log.i(AppGlobals.getLogTag(getClass()), "table exist");
            createNewEntry(name, address, number, product, orderPlace, orderTime, status,
                    currentTimeDate);
            return true;
        } catch (SQLiteException e) {
            myDb.execSQL(DatabaseConstants.createTable(number));
            Log.i(AppGlobals.getLogTag(getClass()), "table created");
            createNewEntry(name, address, number, product, orderPlace, orderTime, status,
                    currentTimeDate);
            return false;
        }
    }

    public void createNewEntry(String name, String address, String mobileNumber,
                               String productName, String orderPlace, String orderTime,
                               String status, String currentTimeDate) {

        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseConstants.NAME_COLUMN, name);
        values.put(DatabaseConstants.ADDRESS_COLUMN, address);
        values.put(DatabaseConstants.PRODUCT_COLUMN, productName);
        values.put(DatabaseConstants.ORDER_PLACE_COLUMN, orderPlace);
        values.put(DatabaseConstants.DELIVERY_TIME_COLUMN, orderTime);
        values.put(DatabaseConstants.ORDER_STATUS_COLUMN, status);
        values.put(DatabaseConstants.CURRENT_TIME_DATE, currentTimeDate);
        sqLiteDatabase.insert(("table"+mobileNumber), null, values);
        Log.i(AppGlobals.getLogTag(getClass()), "created New Entry");
        sqLiteDatabase.close();
    }

    public void insertIntParentColumn(String phone, String timeDate) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseConstants.MOBILE_NUMBER_COLUMN, phone);
        values.put(DatabaseConstants.CURRENT_TIME_DATE, timeDate);
        sqLiteDatabase.insert(DatabaseConstants.TABLE_NAME, null, values);
        Log.i(AppGlobals.getLogTag(getClass()), "created New Entry");
        sqLiteDatabase.close();

    }

    public ArrayList<String> getAllPhoneNumbers() {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        String query = "SELECT * FROM " + DatabaseConstants.TABLE_NAME + " ORDER BY " +
                DatabaseConstants.CURRENT_TIME_DATE + " DESC";
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);
        ArrayList<String> arrayList = new ArrayList<>();
        while (cursor.moveToNext()) {
            String itemname = cursor.getString(cursor.getColumnIndex(
                    DatabaseConstants.MOBILE_NUMBER_COLUMN));
            if (itemname != null && !arrayList.contains(itemname)) {
                arrayList.add(itemname);
            }
        }
        return arrayList;
    }

    public boolean getShippingStatus(String value) {
        String delivery = null;
        boolean status = false;
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        String query = String.format(
                "SELECT %s FROM %s WHERE %s= ?",
                DatabaseConstants.ORDER_STATUS_COLUMN,
                ("table" + value),
                DatabaseConstants.ORDER_STATUS_COLUMN);
        Cursor cursor = sqLiteDatabase.rawQuery(query, new String[]{"0"});
        while (cursor.moveToNext()) {
            delivery = (cursor.getString(cursor.getColumnIndex(DatabaseConstants.ORDER_STATUS_COLUMN)));
            if (delivery.equals("0")) {
                status = true;
            }
        }
        return status;
    }

//    public void updateCategory(String name) {
//        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
//        ContentValues values = new ContentValues();
//        values.put(DatabaseConstants.NAME_COLUMN,name );
//        values.put(DatabaseConstants.CONTACTS, contacts);
//        values.put(DatabaseConstants.DATE_COLUMN, Helpers.getTimeStampForDatabase());
//        sqLiteDatabase.update(DatabaseConstants.TABLE_NAME, values, DatabaseConstants.ID_COLUMN + "=" + id, null);
//        Log.i(AppGlobals.getLogTag(getClass()), "Updated.......");
//        sqLiteDatabase.close();
//    }
//
//    public void deleteCategory(String value) {
//        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
//        sqLiteDatabase.delete(DatabaseConstants.TABLE_NAME, DatabaseConstants.TITLE +
//                "=?", new String[]{value});
//        sqLiteDatabase.close();
//    }
//
//    public boolean checkIfItemAlreadyExist(String item) {
//        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
//        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + DatabaseConstants.TABLE_NAME
//                + " WHERE "+DatabaseConstants.TITLE+"  = '" + item + "'", null);
//        return cursor.getCount() > 0;
//    }
}
