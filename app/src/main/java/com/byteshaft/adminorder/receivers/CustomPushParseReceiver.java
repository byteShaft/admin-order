package com.byteshaft.adminorder.receivers;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.byteshaft.adminorder.AppGlobals;
import com.byteshaft.adminorder.MainActivity;
import com.byteshaft.adminorder.utils.NotificationUtils;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.ParsePushBroadcastReceiver;
import com.parse.ParseQuery;

import org.json.JSONException;
import org.json.JSONObject;

public class CustomPushParseReceiver extends ParsePushBroadcastReceiver {

    private Intent parseIntent;
    private String personName;
    private String phoneNumber;
    private String address;
    private String product;
    private String from;
    private String deliveryTime;

    public CustomPushParseReceiver() {
        super();
    }

    @Override
    protected void onPushReceive(Context context, Intent intent) {
        super.onPushReceive(context, intent);
        if (intent == null)
            return;
        try {
            JSONObject json = new JSONObject(intent.getExtras().getString("com.parse.Data"));
//            Log.e(AppGlobals.getLogTag(getClass()), "Push received: " + json);
            parseIntent = intent;
            parsePushJson(context, json);
            String name = json.getString("name");
            String phone = json.getString("phone");
            String address = json.getString("address");
            String product = json.getString("product");
            // etc that departmental store
            String from = json.getString("from");
            String delivery = json.getString("delivery_time");
            String senderId = json.getString("sender_id");
            System.out.println(name + phone + product + address + delivery + from + senderId);
            ParseQuery<ParseInstallation> parseQuery = ParseQuery.getQuery(ParseInstallation.class);
            parseQuery.whereEqualTo("user",senderId);
            ParsePush.sendMessageInBackground(AppGlobals.PUSH_RESPONCE, parseQuery);
            System.out.println("send responce");

        } catch (JSONException e) {
            Log.e(AppGlobals.getLogTag(getClass()), "Push message json exception: " + e.getMessage());
        }
    }

    @Override
    protected void onPushDismiss(Context context, Intent intent) {
        super.onPushDismiss(context, intent);
    }

    @Override
    protected void onPushOpen(Context context, Intent intent) {
        super.onPushOpen(context, intent);
    }

    private void parsePushJson(Context context, JSONObject json) {
        try {
            boolean isBackground = json.getBoolean("is_background");
            System.out.println(isBackground);
            JSONObject data = json.getJSONObject("data");
            String title = "New Order";
            String message = data.getString("message");
            if (!isBackground) {
                Intent resultIntent = new Intent(context, MainActivity.class);
                showNotificationMessage(title, message, resultIntent);
            }
        } catch (JSONException e) {
            Log.e(AppGlobals.getLogTag(getClass()), "Push message json exception: " + e.getMessage());
        }
    }

    private void showNotificationMessage(String title, String message, Intent intent) {
        NotificationUtils notificationUtils = new NotificationUtils();
        intent.putExtras(parseIntent.getExtras());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, intent);
    }
}
