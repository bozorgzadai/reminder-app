package ali.bozorgzad.project.app.reminder;

import java.io.File;
import java.util.ArrayList;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Application;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;


public class G extends Application {

    public static Context                   context;
    public static LayoutInflater            layoutInflater;
    public static NotificationManager       notificationManager;
    public static Activity                  currentActivity;
    public static AlarmManager              alarmManager;
    public static SharedPreferences         sharedPreferences;

    public static int                       currentReminderId;

    public static ArrayList<StructReminder> reminders    = new ArrayList<StructReminder>();

    public static SQLiteDatabase            database;
    public static final String              DIR_SDCARD   = Environment.getExternalStorageDirectory().getAbsolutePath();
    public static final String              DIR_DATABASE = DIR_SDCARD + "/database-reminder/";


    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        new File(DIR_DATABASE).mkdirs();

        boolean skipWelcome = G.sharedPreferences.getBoolean("SKIP_WELCOME_KEY", false);
        if ( !skipWelcome) {
            SharedPreferences.Editor editor = G.sharedPreferences.edit();
            editor.putBoolean("SKIP_WLCOME_KEY", true);
            editor.putBoolean("SHOW_TASK_TIME_KEY", true);
            editor.putBoolean("SHOW_TASK_DATE_KEY", true);
            editor.putBoolean("NOTIFICATION_VIBRATE_KEY", true);
            editor.putBoolean("NOTIFICATION_LIGHT_KEY", true);
            editor.putBoolean("SENDING_SMS_KEY", true);
            editor.commit();
        }

        manageDatabase();
    }


    private void manageDatabase() {

        database = SQLiteDatabase.openOrCreateDatabase(DIR_DATABASE + "/database_reminder.sqlite", null);
        database.execSQL("CREATE  TABLE  IF NOT EXISTS reminder (" +
                "reminder_id INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL  , " +
                "reminder_title TEXT, " +
                "reminder_ratePriority REAL, " +
                "reminder_active BOOL, " +
                "reminder_year INTEGER, " +
                "reminder_month INTEGER, " +
                "reminder_day INTEGER, " +
                "reminder_hourNotification INTEGER, " +
                "reminder_minuteNotification INTEGER, " +
                "reminder_repetition INTEGER, " +
                "reminder_hourAlarm INTEGER, " +
                "reminder_minuteAlarm INTEGER, " +
                "reminder_extraNote TEXT, " +
                "reminder_massageText TEXT, " +
                "reminder_phoneNumbers TEXT " +
                ")");

    }
}
