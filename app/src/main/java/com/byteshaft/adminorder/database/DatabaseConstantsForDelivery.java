package com.byteshaft.adminorder.database;


public class DatabaseConstantsForDelivery {

    public static final String DELIVERED_TABLE_NAME = "delivered";
    public static final String DELIVERED_NAME_COLUMN = "name";
    public static final String DELIVERED_ADDRESS_COLUMN = "address";
    public static final String DELIVERED_PRODUCT_COLUMN = "product_name";
    public static final String DELIVERED_PLACE_COLUMN = "place_location";
    public static final String DELIVERY_TIME_COLUMN = "delivery_time";
    public static final String CURRENT_TIME_DATE = "current_time_date";

    public static final String CREATE_DELIVERY_TABLE = "CREATE TABLE " +
            DELIVERED_TABLE_NAME + "(" +
            DELIVERED_NAME_COLUMN + " TEXT , " +
            DELIVERED_ADDRESS_COLUMN + " TEXT , " +
            DELIVERED_PRODUCT_COLUMN + " TEXT , " +
            DELIVERED_PLACE_COLUMN + " TEXT , " +
            DELIVERY_TIME_COLUMN + " TEXT , " +
            CURRENT_TIME_DATE + " TEXT UNIQUE " +
            " ) ";
}
