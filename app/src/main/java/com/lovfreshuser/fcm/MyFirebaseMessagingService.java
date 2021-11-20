package com.lovfreshuser.fcm;

import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.lovfreshuser.ui.HomeActivity;
import com.lovfreshuser.utils.HelperClass;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "Notification";
    private NotificationUtils notificationUtils;
    private int notificationIndex = 0;

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.e("NEW_TOKEN", s);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.e("", "From: " + remoteMessage.getFrom());
        if (remoteMessage == null)
            return;
        try{
            if (true) {
                NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
                notificationUtils.playNotificationSound();
            }

            if (HelperClass.Companion.isUserLoggedIn()) {
                // Check if message contains a notification payload.
                //   new SessionManager(getApplicationContext()).isNotification(true);
                NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
                notificationUtils.playNotificationSound();
                if (remoteMessage.getNotification() != null) {
                    Log.e(TAG, "Notification Body: " + remoteMessage.getNotification().getBody());
                    Map<String, String> params = remoteMessage.getData();
                    JSONObject object = new JSONObject(params);
                    handleNotification(object);
                }
                if (remoteMessage.getData().size() > 0) {
                    Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());
                    try {
                        Map<String, String> params = remoteMessage.getData();
                        JSONObject object = new JSONObject(params);
                        handleDataMessage(object);
                    } catch (Exception e) {
                        Log.e(TAG, "Exception: " + e.getMessage());
                    }
                }else {
                    String order_id = "1";
                    Intent resultIntent = null;
                    resultIntent = new Intent(getApplicationContext(), HomeActivity.class);
                    if (order_id.length() > 0) {
                        resultIntent.putExtra("NOTIFICATION_ORDER_ID", order_id);
                    }
                    resultIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(getApplicationContext());
                    taskStackBuilder.addNextIntentWithParentStack(resultIntent);
                    PendingIntent pendingNotificationIntent = PendingIntent.getActivity(getApplicationContext(), notificationIndex, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_ONE_SHOT);
                    showNotificationMessage(getApplicationContext(), remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody(), "", resultIntent, pendingNotificationIntent);
                }
            }

        }catch ( Exception  ex){

        }

    }


    private void handleNotification(JSONObject json) {
        if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
            // app is in foreground, broadcast the push message
            Intent pushNotification = new Intent("pushNotification");
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);
            // play notification sound
            if (true) {
                NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
                notificationUtils.playNotificationSound();
            }
//        } else {
//            //  new SessionManager(getApplicationContext()).isNotification(true);
//            // If the app is in background, firebase itself handles the notification
        }
    }

    private void handleDataMessage(JSONObject json) {
        Log.e(TAG, "push json: " + json.toString());
        try {
            String title = "", message = "", order_id = "";
            if (json.has("data")) {
                String data = json.getString("data");
                JSONObject data1 = new JSONObject(data);
                title = data1.getString("title");
                message = data1.getString("body");
                order_id = data1.getString("order_id");
                Log.e(TAG, "title: " + title);
                Log.e(TAG, "body: " + message);
            } else {
                if (json.has("title")) {
                    title = json.getString("title");
                }
                message = json.getString("body");
                order_id = json.getString("order_id");
                Log.e(TAG, "title: " + title);
                Log.e(TAG, "body: " + message);
            }
            Intent resultIntent = null;

            resultIntent = new Intent(getApplicationContext(), HomeActivity.class);
            if (order_id.length() > 0) {
                resultIntent.putExtra("NOTIFICATION_ORDER_ID", order_id);
            }
            resultIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(getApplicationContext());
            taskStackBuilder.addNextIntentWithParentStack(resultIntent);
            PendingIntent pendingNotificationIntent = PendingIntent.getActivity(getApplicationContext(), notificationIndex, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_ONE_SHOT);
            showNotificationMessage(getApplicationContext(), title, message, "", resultIntent, pendingNotificationIntent);

        } catch (JSONException e) {
            Log.e(TAG, "Json Exception: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }

    private void showNotificationMessage(Context context, String title, String message, String timeStamp, Intent intent, PendingIntent pendingNotificationIntent) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent, pendingNotificationIntent);
    }
}