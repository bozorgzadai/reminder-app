package ali.bozorgzad.project.app.reminder;

import java.io.IOException;
import java.util.Calendar;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;


public class ActivityAlarm extends Activity {

    private TextView    txtTime;
    private TextView    txtDate;
    private TextView    txtTitle;

    private MediaPlayer mediaPlayer;

    private String      title = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        txtTime = (TextView) findViewById(R.id.txtTime);
        txtDate = (TextView) findViewById(R.id.txtDate);
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        Button btnDismiss = (Button) findViewById(R.id.btnDismiss);
        Button btnSnooze = (Button) findViewById(R.id.btnSnooze);

        playSound();
        getExtras();

        btnDismiss.setOnClickListener(new OnClickListener() {

            public void onClick(View arg0) {
                Intent intent = new Intent(G.context, ActivityStartup.class);
                G.currentActivity.startActivity(intent);
                mediaPlayer.stop();
                finish();
            }
        });

        btnSnooze.setOnClickListener(new OnClickListener() {

            public void onClick(View arg0) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int minute = calendar.get(Calendar.MINUTE);
                int hour = calendar.get(Calendar.HOUR);
                if (minute >= 55) {
                    if (minute == 55) {
                        minute = 00;
                    }
                    if (minute == 56) {
                        minute = 1;
                    }
                    if (minute == 57) {
                        minute = 2;
                    }
                    if (minute == 58) {
                        minute = 3;
                    }
                    if (minute == 59) {
                        minute = 4;
                    }

                    hour++;

                } else {
                    minute += 5;
                }

                Intent intentAlarm = new Intent(G.context, ActivityAlarm.class);
                intentAlarm.putExtra("HOURALARM", hour);
                intentAlarm.putExtra("MINUTEALARM", minute);
                intentAlarm.putExtra("YEAR", year);
                intentAlarm.putExtra("MONTH", month);
                intentAlarm.putExtra("DAY", day);
                intentAlarm.putExtra("TITLE", title);
                PendingIntent pendingIntent = PendingIntent.getActivity(G.context, 0, intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT);
                G.alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 1000 * 60 * 5, pendingIntent);

                Intent intent = new Intent(G.context, ActivityStartup.class);
                G.currentActivity.startActivity(intent);
                mediaPlayer.stop();
                finish();
            }
        });
    }


    private void playSound() {
        mediaPlayer = new MediaPlayer();
        try {
            AssetFileDescriptor afd = getAssets().openFd("alarm_sound.mp3");
            mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            mediaPlayer.prepare();

        }
        catch (IllegalStateException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.start();
        mediaPlayer.setLooping(true);
    }


    private void getExtras() {
        int hourAlarm = 0;
        int minuteAlarm = 0;
        int year = 0;
        int month = 0;
        int day = 0;

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            hourAlarm = extras.getInt("HOURALARM");
            minuteAlarm = extras.getInt("MINUTEALARM");
            year = extras.getInt("YEAR");
            month = extras.getInt("MONTH");
            day = extras.getInt("DAY");
            title = extras.getString("TITLE");
        }

        int tempMonth = month;
        tempMonth++;

        txtTime.setText(hourAlarm + " : " + minuteAlarm);
        txtDate.setText(year + " / " + tempMonth + " / " + day);
        txtTitle.setText(title);
    }

}
