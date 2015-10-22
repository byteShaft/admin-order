package com.byteshaft.adminorder.database;


public class DatabaseConstanctsForDelivery {

    public static final String DATABASE_NAME = "delivered";
    public static final int DATABASE_VERSION = 2;
    public static final String DELIVERED_TABLE_NAME = "delivered";
    public static final String DELIVERED_NAME_COLUMN = "name";
    public static final String DELIVERED_ADDRESS_COLUMN = "address";
    public static final String DELIVERED_MOBILE_NUMBER_COLUMN = "mobile_number";
    public static final String DELIVERED_PRODUCT_COLUMN = "product_name";
    public static final String DELIVERED_PLACE_COLUMN = "place_location";
    public static final String DELIVERY_TIME_COLUMN = "delivery_time";
    public static final String DELIVERED_STATUS_COLUMN = "delivered_status";
    public static final String CURRENT_TIME_DATE = "current_time_date";

    public static final String CREATE_PARENT_TABLE = "CREATE TABLE " +
            DELIVERED_TABLE_NAME + "(" +
            DELIVERED_NAME_COLUMN + " TEXT  PRIMARY KEY, " +
            CURRENT_TIME_DATE + " TEXT " +
            " ) ";

    public static String createTable(String tablename) {
        return "CREATE TABLE " +
                ("table" + tablename) + "(" +
                DELIVERED_ADDRESS_COLUMN + " TEXT , " +
                DELIVERED_PRODUCT_COLUMN + " TEXT , " +
                DELIVERED_PLACE_COLUMN + " TEXT , " +
                DELIVERY_TIME_COLUMN + " TEXT , " +
                DELIVERED_STATUS_COLUMN + " TEXT , " +
                CURRENT_TIME_DATE + " TEXT " +
                " ) ";

    }
}
