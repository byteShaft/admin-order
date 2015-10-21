package com.byteshaft.adminorder.database;


public class DatabaseConstants {

    public static final String DATABASE_NAME = "order_booker_database";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "order_booker";
    public static final String NAME_COLUMN = "name";
    public static final String ADDRESS_COLUMN = "address";
    public static final String MOBILE_NUMBER_COLUMN = "mobile_number";
    public static final String PRODUCT_COLUMN = "product_name";
    public static final String ORDER_PLACE_COLUMN = "place_location";
    public static final String DELIVERY_TIME_COLUMN = "delivery_time";
    public static final String ORDER_STATUS_COLUMN = "order_status";
    public static final String CURRENT_TIME_DATE = "current_time_date";

    public static final String TABLE_CREATE =
            "CREATE TABLE " +
                    TABLE_NAME + "(" +
                    NAME_COLUMN + " TEXT , " +
                    ADDRESS_COLUMN + " TEXT , " +
                    MOBILE_NUMBER_COLUMN + " TEXT , " +
                    PRODUCT_COLUMN + " TEXT , " +
                    ORDER_PLACE_COLUMN + " TEXT , " +
                    DELIVERY_TIME_COLUMN + " TEXT , " +
                    ORDER_STATUS_COLUMN + " TEXT , " +
                    CURRENT_TIME_DATE + " TEXT " +
                      " ) ";
}
