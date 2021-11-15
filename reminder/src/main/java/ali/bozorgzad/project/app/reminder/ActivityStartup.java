package ali.bozorgzad.project.app.reminder;

import java.util.ArrayList;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;


public class ActivityStartup extends ActivityEnhanced {

    private ListView        list;
    private AdapterReminder adapter;
    private Button          btnNewTask;
    private ImageView       imgSlidingMenu;
    private EditText        edtSearch;
    private CheckBox        chkOnlyActive;
    private CheckBox        chkSortByPriority;

    private String          clauseSortByPriority = "''";
    private CharSequence    clauseSearch         = "";
    private String          clauseOnlyActive     = "";


    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_startup);

        //*  Sliding Menu ... START *//
        Display display = getWindowManager().getDefaultDisplay();
        int width = display.getWidth();
        //Point size = new Point();
        //display.getsize(size);
        // int width = size.x;
        //int height = size.y;

        setTitle("Hello");
        final SlidingMenu menu = new SlidingMenu(this);
        menu.setMode(SlidingMenu.RIGHT);
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        menu.setBehindOffset((int) (width - (width / 1.4)));
        menu.setFadeDegree(0.35f);
        menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);

        View view = G.layoutInflater.inflate(R.layout.sliding_menu, null);
        menu.setMenu(view);

        chkOnlyActive = (CheckBox) menu.findViewById(R.id.chkOnlyActive);
        chkSortByPriority = (CheckBox) menu.findViewById(R.id.chkSortByPriority);
        imgSlidingMenu = (ImageView) findViewById(R.id.imgSlidingMenu);

        ///

        chkOnlyActive.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton arg0, boolean currentChecked) {
                if (currentChecked) {
                    clauseOnlyActive = "true";
                } else {
                    clauseOnlyActive = "";
                }
                readFromDatabase();
                adapter.notifyDataSetChanged();

            }
        });

        chkSortByPriority.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton arg0, boolean currentChecked) {
                if (currentChecked) {
                    clauseSortByPriority = "reminder_ratePriority DESC";
                } else {
                    clauseSortByPriority = "''";
                }
                readFromDatabase();
                adapter.notifyDataSetChanged();
            }
        });

        menu.findViewById(R.id.layoutOnlyActive).setOnClickListener(new OnClickListener() {

            public void onClick(View arg0) {
                boolean curretnChacked = chkOnlyActive.isChecked();
                chkOnlyActive.setChecked( !curretnChacked);
            }
        });

        menu.findViewById(R.id.layoutSortByPriority).setOnClickListener(new OnClickListener() {

            public void onClick(View arg0) {
                boolean curretnChacked = chkSortByPriority.isChecked();
                chkSortByPriority.setChecked( !curretnChacked);
            }
        });

        menu.findViewById(R.id.layoutSetting).setOnClickListener(new OnClickListener() {

            public void onClick(View arg0) {
                Intent intent = new Intent(G.currentActivity, ActivitySetting.class);
                G.currentActivity.startActivity(intent);
            }
        });

        imgSlidingMenu.setOnClickListener(new OnClickListener() {

            public void onClick(View arg0) {
                menu.showMenu();
            }
        });

        //*  Sliding Menu ... END *//

        list = (ListView) findViewById(R.id.list);
        edtSearch = (EditText) findViewById(R.id.edtSearch);
        btnNewTask = (Button) findViewById(R.id.btnNewTask);

        adapter = new AdapterReminder(G.reminders);
        list.setAdapter(adapter);

        readFromDatabase();

        edtSearch.setHintTextColor(Color.parseColor("#ffffff"));
        edtSearch.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence currentText, int arg1, int arg2, int arg3) {
                clauseSearch = currentText;
                readFromDatabase();
                adapter.notifyDataSetChanged();
            }


            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                // TODO Auto-generated method stub

            }


            public void afterTextChanged(Editable arg0) {

            }
        });

        btnNewTask.setOnClickListener(new OnClickListener() {

            public void onClick(View arg0) {
                Intent intent = new Intent(G.currentActivity, ActivityReminderDetails.class);
                G.currentActivity.startActivity(intent);
            }
        });

    }


    private void readFromDatabase() {
        G.reminders.clear();
        Cursor cursor = G.database.rawQuery("SELECT * FROM reminder WHERE reminder_title LIKE '%" + clauseSearch + "%'" + " AND reminder_active LIKE '%" + clauseOnlyActive + "%' ORDER BY " + clauseSortByPriority + " ", null);
        while (cursor.moveToNext()) {
            StructReminder reminder = new StructReminder();
            reminder.id = cursor.getInt(cursor.getColumnIndex("reminder_id"));
            reminder.title = cursor.getString(cursor.getColumnIndex("reminder_title"));
            reminder.ratePriority = cursor.getFloat(cursor.getColumnIndex("reminder_ratePriority"));
            reminder.active = Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex("reminder_active")));
            reminder.year = cursor.getInt(cursor.getColumnIndex("reminder_year"));
            reminder.month = cursor.getInt(cursor.getColumnIndex("reminder_month"));
            reminder.day = cursor.getInt(cursor.getColumnIndex("reminder_day"));
            reminder.hourNotification = cursor.getInt(cursor.getColumnIndex("reminder_hourNotification"));
            reminder.minuteNotification = cursor.getInt(cursor.getColumnIndex("reminder_minuteNotification"));
            reminder.hourAlarm = cursor.getInt(cursor.getColumnIndex("reminder_hourAlarm"));
            reminder.minuteAlarm = cursor.getInt(cursor.getColumnIndex("reminder_minuteAlarm"));
            reminder.repetition = cursor.getInt(cursor.getColumnIndex("reminder_repetition"));
            reminder.extraNote = cursor.getString(cursor.getColumnIndex("reminder_extraNote"));
            reminder.massageText = cursor.getString(cursor.getColumnIndex("reminder_massageText"));

            String phoneNumbers = cursor.getString(cursor.getColumnIndex("reminder_phoneNumbers"));
            int phoneNumbersLen = phoneNumbers.length();
            ArrayList<String> phoneNumbersArrayList = new ArrayList<String>();
            int start = 1;
            int end = 12;

            while (end < phoneNumbersLen) {
                phoneNumbersArrayList.add(phoneNumbers.substring(start, end));
                start += 13;
                end += 13;
            }
            reminder.phoneNumbers = phoneNumbersArrayList;

            if (G.currentReminderId < reminder.id) {
                G.currentReminderId = reminder.id;
            }
            G.reminders.add(reminder);
        }

        cursor.close();
    }

}