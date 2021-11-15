package ali.bozorgzad.project.app.reminder;

import java.util.ArrayList;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


public class ShowNotification extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        String date = intent.getStringExtra("DATE");
        String title = intent.getStringExtra("TITLE");
        String massageText = intent.getStringExtra("MASSAGE_TEXT");
        ArrayList<String> phoneNumbers = intent.getStringArrayListExtra("PHONE_NUMBERS");

        int icon = R.drawable.alarm_icon;
        CharSequence tickerText = title;
        CharSequence contentTitle = title;
        CharSequence contentText = date;

        Intent notificationIntent = new Intent(G.context, ActivityStartup.class);
        PendingIntent pendingIntentNotification = PendingIntent.getActivity(G.context, 0, notificationIntent, 0);
        Notification notification = new Notification(icon, tickerText, System.currentTimeMillis());

        notification.setLatestEventInfo(G.context, contentTitle, contentText, pendingIntentNotification);

        notification.flags |= Notification.FLAG_AUTO_CANCEL;

        boolean notificationVibrate = G.sharedPreferences.getBoolean("NOTIFICATION_VIBRATE_KEY", true);
        boolean notificationLight = G.sharedPreferences.getBoolean("NOTIFICATION_LIGHT_KEY", true);
        boolean sendingSms = G.sharedPreferences.getBoolean("SENDING_SMS_KEY", false);

        if (notificationVibrate) {
            notification.defaults = Notification.DEFAULT_VIBRATE;
        }
        if (notificationLight) {
            notification.defaults = Notification.DEFAULT_LIGHTS;
        }

        notification.defaults = Notification.DEFAULT_SOUND;
        G.notificationManager.notify(3000, notification);

        if (sendingSms) {
            for (String number: phoneNumbers) {
                HelperSms.sendSms(number, massageText);
            }
        }
    }
}
