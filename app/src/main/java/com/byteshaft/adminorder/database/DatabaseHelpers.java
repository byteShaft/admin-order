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
        db.execSQL(DatabaseConstantsForDelivery.CREATE_DELIVERY_TABLE);
        Log.i(AppGlobals.getLogTag(getClass()), "Database created !!!");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" + DatabaseConstants.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS" + DatabaseConstantsForDelivery.DELIVERED_TABLE_NAME);
        onCreate(db);
    }

    public boolean insertValuesCheckIfNotExist(String name, String address, String product,
                                   String orderPlace, String orderTime, String status,
                                   String currentTimeDate) {
        String trimmedName = name.replaceAll(" ", "");
        SQLiteDatabase myDb = this.getWritableDatabase();
        boolean exist;
        try {
            Cursor c = myDb.rawQuery("select * from " + ("table"+trimmedName), null);
            Log.i(AppGlobals.getLogTag(getClass()), "table exist");
            createNewEntry(trimmedName, address, product, orderPlace, orderTime, status,
                    currentTimeDate);
            myDb.close();
            return true;
        } catch (SQLiteException e) {
            myDb.execSQL(DatabaseConstants.createTable(trimmedName));
            Log.i(AppGlobals.getLogTag(getClass()), "table created");
            createNewEntry(trimmedName, address, product, orderPlace, orderTime, status,
                    currentTimeDate);
            myDb.close();
            return false;
        }
    }



    public void addEntryToDeliveredTable(String name, String address, String productName,
                                         String orderPlace, String orderTime, String currentTimeDate) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseConstantsForDelivery.DELIVERED_NAME_COLUMN, name);
        values.put(DatabaseConstantsForDelivery.DELIVERED_ADDRESS_COLUMN, address);
        values.put(DatabaseConstantsForDelivery.DELIVERED_PRODUCT_COLUMN, productName);
        values.put(DatabaseConstantsForDelivery.DELIVERED_PLACE_COLUMN, orderPlace);
        values.put(DatabaseConstantsForDelivery.DELIVERY_TIME_COLUMN, orderTime);
        values.put(DatabaseConstantsForDelivery.CURRENT_TIME_DATE, currentTimeDate);
        sqLiteDatabase.insert(DatabaseConstantsForDelivery.DELIVERED_TABLE_NAME, null, values);
        Log.i(AppGlobals.getLogTag(getClass()), "created New Entry");
        sqLiteDatabase.close();

    }

    public void createNewEntry(String name, String address,String productName, String orderPlace,
                               String orderTime, String status, String currentTimeDate) {

        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseConstants.ADDRESS_COLUMN, address);
        values.put(DatabaseConstants.PRODUCT_COLUMN, productName);
        values.put(DatabaseConstants.ORDER_PLACE_COLUMN, orderPlace);
        values.put(DatabaseConstants.DELIVERY_TIME_COLUMN, orderTime);
        values.put(DatabaseConstants.ORDER_STATUS_COLUMN, status);
        values.put(DatabaseConstants.CURRENT_TIME_DATE, currentTimeDate);
        sqLiteDatabase.insert(("table" + name), null, values);
        sqLiteDatabase.close();
    }

    public void insertIntParentColumn(String name, String phone, String timeDate) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseConstants.NAME_COLUMN, name);
        values.put(DatabaseConstants.MOBILE_NUMBER_COLUMN, phone);
        values.put(DatabaseConstants.CURRENT_TIME_DATE, timeDate);
        sqLiteDatabase.insert(DatabaseConstants.TABLE_NAME, null, values);
        Log.i(AppGlobals.getLogTag(getClass()), "created New Entry");
        sqLiteDatabase.close();

    }

    public ArrayList<String> getAllCustomerName() {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        String query = "SELECT * FROM " + DatabaseConstants.TABLE_NAME + " ORDER BY " +
                DatabaseConstants.CURRENT_TIME_DATE + " DESC";
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);
        ArrayList<String> arrayList = new ArrayList<>();
        while (cursor.moveToNext()) {
            String itemname = cursor.getString(cursor.getColumnIndex(
                    DatabaseConstants.NAME_COLUMN));
            if (itemname != null && !arrayList.contains(itemname)) {
                arrayList.add(itemname);
            }
        }
        sqLiteDatabase.close();
        return arrayList;
    }

    public ArrayList<String> getAllProducts(String tablename) {
        String trimmedName = tablename.replaceAll(" ", "");
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        String query = "SELECT * FROM " + trimmedName + " ORDER BY " +
                DatabaseConstants.CURRENT_TIME_DATE + " DESC";
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);
        ArrayList<String> arrayList = new ArrayList<>();
        while (cursor.moveToNext()) {
            String itemname = cursor.getString(cursor.getColumnIndex(
                    DatabaseConstants.PRODUCT_COLUMN));
            if (itemname != null) {
                arrayList.add(itemname);
            }
        }
        sqLiteDatabase.close();
        return arrayList;
    }

    public boolean getShippingStatus(String value) {
        String delivery;
        String trimmedName = value.replaceAll(" ", "");
        boolean status = false;
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        String query = String.format(
                "SELECT * FROM %s ",
                ("table" + trimmedName));
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);
        while (cursor.moveToNext()) {
            delivery = (cursor.getString(cursor.getColumnIndex(DatabaseConstants.ORDER_STATUS_COLUMN)));
            System.out.println(delivery);
            if (delivery.equals("0")) {
                status = true;
            }
        }
        sqLiteDatabase.close();
        return status;
    }

    public String[] getDetails(String name, String product) {
        String trimmedName = name.replaceAll(" ", "");
        String[] list = new String[6];
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        String query = String.format(
                "SELECT %s, %s, %s , %s, %s, %s FROM %s WHERE %s= ?",
                DatabaseConstants.ORDER_STATUS_COLUMN,
                DatabaseConstants.ADDRESS_COLUMN,
                DatabaseConstants.PRODUCT_COLUMN,
                DatabaseConstants.ORDER_PLACE_COLUMN,
                DatabaseConstants.DELIVERY_TIME_COLUMN,
                DatabaseConstants.CURRENT_TIME_DATE,
                                ("table" + trimmedName),
                DatabaseConstants.PRODUCT_COLUMN);
        Cursor cursor = sqLiteDatabase.rawQuery(query, new String[]{product});
        while (cursor.moveToNext()) {
            list[0] = (cursor.getString(cursor.getColumnIndex(DatabaseConstants.ADDRESS_COLUMN)));
            list[1] = (cursor.getString(cursor.getColumnIndex(DatabaseConstants.PRODUCT_COLUMN)));
            list[2] = (cursor.getString(cursor.getColumnIndex(DatabaseConstants.ORDER_PLACE_COLUMN)));
            list[3] = (cursor.getString(cursor.getColumnIndex(DatabaseConstants.ORDER_STATUS_COLUMN)));
            list[4] = (cursor.getString(cursor.getColumnIndex(DatabaseConstants.DELIVERY_TIME_COLUMN)));
            list[5] = (cursor.getString(cursor.getColumnIndex(DatabaseConstants.CURRENT_TIME_DATE)));
        }
        sqLiteDatabase.close();
        return list;
    }

    public String getLatestOrder(String name) {
        String latestOrder = null;
        String trimmedName = name.replaceAll(" ", "");
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        String query = "SELECT * FROM " + ("table"+trimmedName) +" ORDER BY "
                +DatabaseConstants.CURRENT_TIME_DATE +" DESC LIMIT 1";
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);
        while(cursor.moveToNext()) {
            latestOrder = cursor.getString(cursor.getColumnIndex(DatabaseConstants.PRODUCT_COLUMN));
        }
        return latestOrder;
    }

    public void deleteOrder(String tableName, String productName) {
        String trimmedName = tableName.replaceAll(" ", "");
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.delete(("table"+trimmedName), DatabaseConstants.CURRENT_TIME_DATE +
                "=?", new String[]{productName});
        sqLiteDatabase.close();
    }

    public void updateStatus(String tableName, String status, String currentDate) {
        String trimmedName = tableName.replaceAll(" ", "");
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseConstants.ORDER_STATUS_COLUMN, status);
        sqLiteDatabase.update(("table" + trimmedName), values, DatabaseConstants.CURRENT_TIME_DATE+" = ?",
                new String[]{currentDate});
        sqLiteDatabase.close();
        Log.i(AppGlobals.getLogTag(getClass()), "updated....");
    }

    public ArrayList<String> getAllDeliveredProductsDates() {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        String query = "SELECT * FROM " + DatabaseConstantsForDelivery.DELIVERED_TABLE_NAME + " ORDER BY " +
                DatabaseConstants.CURRENT_TIME_DATE + " DESC";
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);
        ArrayList<String> arrayList = new ArrayList<>();
        while (cursor.moveToNext()) {
            String itemName = cursor.getString(cursor.getColumnIndex(
                    DatabaseConstantsForDelivery.CURRENT_TIME_DATE));
            if (itemName != null) {
                arrayList.add(itemName);
            }
        }
        sqLiteDatabase.close();
        return arrayList;
    }

    public String[] getDeliveredDetails(String date) {
        String[] list = new String[6];
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        String query = String.format(
                "SELECT  %s, %s , %s, %s FROM %s WHERE %s= ?",
                DatabaseConstantsForDelivery.DELIVERED_ADDRESS_COLUMN,
                DatabaseConstantsForDelivery.DELIVERED_PRODUCT_COLUMN,
                DatabaseConstantsForDelivery.DELIVERED_PLACE_COLUMN,
                DatabaseConstantsForDelivery.DELIVERY_TIME_COLUMN,
                DatabaseConstantsForDelivery.DELIVERED_TABLE_NAME,
                DatabaseConstantsForDelivery.CURRENT_TIME_DATE);
        Cursor cursor = sqLiteDatabase.rawQuery(query, new String[]{date});
        while (cursor.moveToNext()) {
            list[0] = (cursor.getString(cursor.getColumnIndex(
                    DatabaseConstantsForDelivery.DELIVERED_ADDRESS_COLUMN)));
            list[1] = (cursor.getString(cursor.getColumnIndex(
                    DatabaseConstantsForDelivery.DELIVERED_PRODUCT_COLUMN)));
            list[2] = (cursor.getString(cursor.getColumnIndex(
                    DatabaseConstantsForDelivery.DELIVERED_PLACE_COLUMN)));
            list[3] = (cursor.getString(cursor.getColumnIndex(
                    DatabaseConstantsForDelivery.DELIVERY_TIME_COLUMN)));
        }
        sqLiteDatabase.close();
        return list;
    }

        public void deleteItem(String value) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.delete(DatabaseConstantsForDelivery.DELIVERED_TABLE_NAME,
                DatabaseConstants.CURRENT_TIME_DATE +
                "=?", new String[]{value});
        sqLiteDatabase.close();
    }

    public String[] getPhoneNumber(String name) {
        String[] list = new String[1];
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        String query = String.format(
                "SELECT %s FROM %s WHERE %s= ?",
                DatabaseConstants.MOBILE_NUMBER_COLUMN,
                DatabaseConstants.TABLE_NAME,
                DatabaseConstants.NAME_COLUMN);
        Cursor cursor = sqLiteDatabase.rawQuery(query, new String[]{name});
        while (cursor.moveToNext()) {
            list[0] = (cursor.getString(cursor.getColumnIndex(DatabaseConstants.MOBILE_NUMBER_COLUMN)));
        }
        sqLiteDatabase.close();
        return list;
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
