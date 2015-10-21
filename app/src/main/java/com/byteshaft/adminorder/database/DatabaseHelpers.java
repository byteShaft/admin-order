package com.byteshaft.adminorder.database;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
        db.execSQL(DatabaseConstants.TABLE_CREATE);
        Log.i(AppGlobals.getLogTag(getClass()), "Database created !!!");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" + DatabaseConstants.TABLE_NAME);
        onCreate(db);
    }

    public void createNewEntry(String name, String address, String mobileNumber,
                               String productName, String orderPlace, String orderTime,
                               String status, String currentTimeDate) {

        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseConstants.NAME_COLUMN, name);
        values.put(DatabaseConstants.ADDRESS_COLUMN, address);
        values.put(DatabaseConstants.MOBILE_NUMBER_COLUMN, mobileNumber);
        values.put(DatabaseConstants.PRODUCT_COLUMN, productName);
        values.put(DatabaseConstants.ORDER_PLACE_COLUMN, orderPlace);
        values.put(DatabaseConstants.DELIVERY_TIME_COLUMN, orderTime);
        values.put(DatabaseConstants.ORDER_STATUS_COLUMN, status);
        values.put(DatabaseConstants.CURRENT_TIME_DATE, currentTimeDate);
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
            if (itemname != null) {
                arrayList.add(itemname);
            }
        }
        return arrayList;
    }

    public String getDeliveryTime(String value) {
        String delivery = null;
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        String query = String.format(
                "SELECT %s,%s FROM %s WHERE %s= ?",
                DatabaseConstants.MOBILE_NUMBER_COLUMN,
                DatabaseConstants.DELIVERY_TIME_COLUMN,
                DatabaseConstants.TABLE_NAME,
                DatabaseConstants.DELIVERY_TIME_COLUMN);
        Cursor cursor = sqLiteDatabase.rawQuery(query, new String[]{value});
        while (cursor.moveToNext()) {
            delivery = (cursor.getString(cursor.getColumnIndex(DatabaseConstants.CURRENT_TIME_DATE)));
        }
        return delivery;
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
