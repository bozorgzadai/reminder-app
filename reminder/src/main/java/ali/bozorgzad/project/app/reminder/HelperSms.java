package ali.bozorgzad.project.app.reminder;

import android.telephony.SmsManager;


public class HelperSms {

    public static void sendSms(String destination, String massageText) {
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(destination, null, massageText, null, null);
    }
}
