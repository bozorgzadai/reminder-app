package ali.bozorgzad.project.app.reminder;

import java.util.ArrayList;
import java.util.Calendar;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


public class ActivityReminderDetails extends ActivityEnhanced {

    private StructReminder      currentReminder;
    private Button              btnDone;
    private boolean             mustInsert                     = false;
    private boolean             getCurrentTimeAlarmOnce        = false;
    private boolean             getCurrentTimeNotificationOnce = false;
    private boolean             getCurrentDateOnce             = false;
    private boolean             checkAlarmSet                  = false;
    private boolean             dateSet                        = false;
    private boolean             checkTimeSet                   = false;
    private int                 position;
    private int                 hourOfAlarm;
    private int                 minuteOfAlarm;
    private int                 hourOfNotification;
    private int                 minuteOfNotification;
    private int                 year;
    private int                 monthOfYear;
    private int                 dayOfMonth;

    private ImageView           imgBack;

    private EditText            edtTitle;
    private RatingBar           ratePriority;
    private CheckBox            chkActive;
    private TextView            txtDate;
    private TextView            txtTime;
    private TextView            txtAlarm;
    private TextView            txtRepetition;
    private TextView            txtNote;
    private TextView            txtNumbers;

    private ViewGroup           layoutDate;
    private ViewGroup           layoutTime;
    private ViewGroup           layoutAlarm;
    private ViewGroup           layoutRepetition;
    private ViewGroup           layoutNote;
    private ViewGroup           layoutNumbers;

    private EditText            currentEditText;
    private String              massageText;
    private String              noteText;
    private ArrayList<String>   tempPhoneNumbers               = new ArrayList<String>();

    private Calendar            calendar                       = Calendar.getInstance();

    private boolean             readFromPhoneNumbersOnce       = false;
    private ArrayList<EditText> tempEditText                   = new ArrayList<EditText>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder_details);

        imgBack = (ImageView) findViewById(R.id.imgBack);

        edtTitle = (EditText) findViewById(R.id.edtTitle);
        ratePriority = (RatingBar) findViewById(R.id.ratePriority);
        chkActive = (CheckBox) findViewById(R.id.chkActive);
        txtDate = (TextView) findViewById(R.id.txtDate);
        txtTime = (TextView) findViewById(R.id.txtTime);
        txtAlarm = (TextView) findViewById(R.id.txtAlarm);
        txtRepetition = (TextView) findViewById(R.id.txtRepetition);
        txtNote = (TextView) findViewById(R.id.txtNote);
        txtNumbers = (TextView) findViewById(R.id.txtNumbers);

        layoutDate = (ViewGroup) findViewById(R.id.layoutDate);
        layoutTime = (ViewGroup) findViewById(R.id.layoutTime);
        layoutAlarm = (ViewGroup) findViewById(R.id.layoutAlarm);
        layoutRepetition = (ViewGroup) findViewById(R.id.layoutRepetition);
        layoutNote = (ViewGroup) findViewById(R.id.layoutNote);
        layoutNumbers = (ViewGroup) findViewById(R.id.layoutNumbers);

        btnDone = (Button) findViewById(R.id.btnDone);

        ///////////////////////////////////
        ///////////////////////////////////

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            position = extras.getInt("POSITION");
            currentReminder = G.reminders.get(position);

            dateSet = true;
            checkTimeSet = true;
            edtTitle.setText(currentReminder.title);
            ratePriority.setRating(currentReminder.ratePriority);
            chkActive.setChecked(currentReminder.active);

            int tempMonth = currentReminder.month;
            tempMonth++;
            txtDate.setText(currentReminder.year + " / " + tempMonth + " / " + currentReminder.day);
            txtTime.setText(currentReminder.hourNotification + " : " + currentReminder.minuteNotification);
            txtAlarm.setText(currentReminder.hourAlarm + " : " + currentReminder.minuteAlarm);
            txtRepetition.setText(currentReminder.repetition + "");

            if (currentReminder.extraNote.length() > 0) {
                if (currentReminder.extraNote.length() > 22) {
                    txtNote.setText(currentReminder.extraNote.subSequence(0, 23) + " ...");
                } else {
                    txtNote.setText(currentReminder.extraNote);
                }
            }

            if (currentReminder.phoneNumbers.size() > 0) {
                txtNumbers.setText(currentReminder.phoneNumbers.get(0) + " ...");
            }

            year = currentReminder.year;
            monthOfYear = currentReminder.month;
            dayOfMonth = currentReminder.day;
            hourOfNotification = currentReminder.hourNotification;
            minuteOfNotification = currentReminder.minuteNotification;
            hourOfAlarm = currentReminder.hourAlarm;
            noteText = currentReminder.extraNote;
            massageText = currentReminder.massageText;
            tempPhoneNumbers = currentReminder.phoneNumbers;

        } else {
            currentReminder = new StructReminder();
            mustInsert = true;
            getCurrentTimeAlarmOnce = true;
            getCurrentTimeNotificationOnce = true;
            getCurrentDateOnce = true;
            noteText = " ";
            massageText = " ";
        }

        imgBack.setOnClickListener(new OnClickListener() {

            public void onClick(View arg0) {
                finish();
            }
        });

        ratePriority.setOnTouchListener(new OnTouchListener() {

            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    float touchPositionX = event.getX();
                    float width = (ratePriority.getWidth());
                    float starsf = (touchPositionX / width) * 5.0f;
                    float stars = (float) (starsf + 0.3);
                    ratePriority.setRating(stars);
                }
                return true;
            }
        });

        layoutTime.setOnClickListener(new OnClickListener() {

            public void onClick(View arg0) {
                if (dateSet) {
                    OnTimeSetListener timeListener = new OnTimeSetListener() {

                        public void onTimeSet(TimePicker view, int hourOfNotificationPicker, int minuteOfNotificationPicker) {

                            boolean currectTime = true;
                            if (year == calendar.get(Calendar.YEAR)) {
                                if (monthOfYear == calendar.get(Calendar.MONTH)) {
                                    if (dayOfMonth == calendar.get(Calendar.DAY_OF_MONTH)) {
                                        Log.i("LOG", "SDFSDF");
                                        if (hourOfNotificationPicker < calendar.get(Calendar.HOUR_OF_DAY)) {
                                            currectTime = false;
                                        }
                                        else if (hourOfNotificationPicker == calendar.get(Calendar.HOUR_OF_DAY)) {
                                            if (minuteOfNotificationPicker <= calendar.get(Calendar.MINUTE)) {
                                                currectTime = false;
                                            }
                                        }
                                    }
                                }
                            }

                            if (currectTime) {
                                hourOfNotification = hourOfNotificationPicker;
                                minuteOfNotification = minuteOfNotificationPicker;
                                getCurrentTimeNotificationOnce = false;
                                checkTimeSet = true;

                                txtTime.setText(hourOfNotification + " : " + minuteOfNotification);
                                Toast.makeText(G.context, "Time Set = " + hourOfNotificationPicker + ":" + minuteOfNotificationPicker, Toast.LENGTH_SHORT).show();
                            } else {
                                final AlertDialog.Builder builderDateSet = new AlertDialog.Builder(G.currentActivity);
                                builderDateSet.setMessage("The time that you set is illegal because that's not appropriate with Date \nBecarful ..!  ");
                                builderDateSet.setTitle("Warning !!!");
                                builderDateSet.setIcon(android.R.drawable.ic_dialog_info);
                                builderDateSet.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                                    public void onClick(DialogInterface dialog, int arg1) {}
                                });
                                builderDateSet.create().show();
                            }
                        }
                    };

                    int minute;
                    int hourOfDay;
                    if (getCurrentTimeNotificationOnce == true) {
                        minute = calendar.get(Calendar.MINUTE);
                        hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
                    } else {
                        minute = minuteOfNotification;
                        hourOfDay = hourOfNotification;
                    }

                    TimePickerDialog timePicker = new TimePickerDialog(G.currentActivity, timeListener, hourOfDay, minute, true);
                    timePicker.show();
                } else {
                    final AlertDialog.Builder builderDateSet = new AlertDialog.Builder(G.currentActivity);
                    builderDateSet.setMessage("Please first set date ");
                    builderDateSet.setTitle("Warning !!!");
                    builderDateSet.setIcon(android.R.drawable.ic_dialog_info);
                    builderDateSet.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int arg1) {}
                    });
                    builderDateSet.create().show();
                }

            }
        });

        layoutAlarm.setOnClickListener(new OnClickListener() {

            public void onClick(View arg0) {
                if (dateSet) {
                    OnTimeSetListener timeAlarmListener = new OnTimeSetListener() {

                        public void onTimeSet(TimePicker view, int hourOfAlarmPicker, int minuteOfAlarmPicker) {
                            boolean currectTime = true;
                            if (year == calendar.get(Calendar.YEAR)) {
                                if (monthOfYear == calendar.get(Calendar.MONTH)) {
                                    if (dayOfMonth == calendar.get(Calendar.DAY_OF_MONTH)) {
                                        if (hourOfAlarmPicker < calendar.get(Calendar.HOUR_OF_DAY)) {
                                            currectTime = false;
                                        }
                                        else if (hourOfAlarmPicker == calendar.get(Calendar.HOUR_OF_DAY)) {
                                            if (minuteOfAlarmPicker <= calendar.get(Calendar.MINUTE)) {
                                                currectTime = false;
                                            }
                                        }
                                    }
                                }
                            }

                            if (currectTime) {
                                hourOfAlarm = hourOfAlarmPicker;
                                minuteOfAlarm = minuteOfAlarmPicker;
                                checkAlarmSet = true;
                                getCurrentTimeAlarmOnce = false;

                                txtAlarm.setText(hourOfAlarm + " : " + minuteOfAlarm);
                                Toast.makeText(G.context, "Alarm Set = " + hourOfAlarmPicker + ":" + minuteOfAlarmPicker, Toast.LENGTH_SHORT).show();
                            } else {
                                final AlertDialog.Builder builderDateSet = new AlertDialog.Builder(G.currentActivity);
                                builderDateSet.setMessage("The time that you set is illegal because that's not appropriate with Date \nBecarful ..!  ");
                                builderDateSet.setTitle("Warning !!!");
                                builderDateSet.setIcon(android.R.drawable.ic_dialog_info);
                                builderDateSet.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                                    public void onClick(DialogInterface dialog, int arg1) {}
                                });
                                builderDateSet.create().show();
                            }

                        }
                    };

                    int minute;
                    int hourOfDay;
                    if (getCurrentTimeAlarmOnce == true) {
                        minute = calendar.get(Calendar.MINUTE);
                        hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
                    } else {
                        minute = minuteOfAlarm;
                        hourOfDay = hourOfAlarm;
                    }

                    TimePickerDialog timeAlarmPicker = new TimePickerDialog(G.currentActivity, timeAlarmListener, hourOfDay, minute, true);
                    timeAlarmPicker.show();
                } else {
                    final AlertDialog.Builder builderDateSet = new AlertDialog.Builder(G.currentActivity);
                    builderDateSet.setMessage("Please first set date ");
                    builderDateSet.setTitle("Warning !!!");
                    builderDateSet.setIcon(android.R.drawable.ic_dialog_info);
                    builderDateSet.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int arg1) {}
                    });
                    builderDateSet.create().show();
                }

            }
        });

        layoutDate.setOnClickListener(new OnClickListener() {

            public void onClick(View arg0) {

                final int yearDialog;
                final int monthDialog;
                final int dayOfMonthDialog;
                if (getCurrentDateOnce == true) {
                    getCurrentDateOnce = false;
                    yearDialog = calendar.get(Calendar.YEAR);
                    monthDialog = calendar.get(Calendar.MONTH);
                    dayOfMonthDialog = calendar.get(Calendar.DAY_OF_MONTH);
                } else {
                    yearDialog = year;
                    monthDialog = monthOfYear;
                    dayOfMonthDialog = dayOfMonth;
                }

                OnDateSetListener dateListener = new OnDateSetListener() {

                    public void onDateSet(DatePicker view, int yearPicker, int monthOfYearPicker, int dayOfMonthPicker) {
                        boolean currectDate = true;
                        if (yearPicker < calendar.get(Calendar.YEAR)) {
                            currectDate = false;
                        }
                        else if (yearPicker == calendar.get(Calendar.YEAR)) {
                            if (monthOfYearPicker < calendar.get(Calendar.MONTH)) {
                                currectDate = false;
                            }
                            else if (monthOfYearPicker == calendar.get(Calendar.MONTH)) {
                                if (dayOfMonthPicker < calendar.get(Calendar.DAY_OF_MONTH)) {
                                    currectDate = false;
                                }
                            }
                        }

                        if (currectDate) {
                            year = yearPicker;
                            monthOfYear = monthOfYearPicker;
                            dayOfMonth = dayOfMonthPicker;
                            dateSet = true;
                            int tempMonth = monthOfYear;
                            tempMonth++;

                            Toast.makeText(G.context, "Date Set = " + yearPicker + "/" + tempMonth + "/" + dayOfMonthPicker, Toast.LENGTH_SHORT).show();
                            txtDate.setText(year + " / " + tempMonth + " / " + dayOfMonth);
                        } else {
                            getCurrentDateOnce = true;

                            AlertDialog.Builder builder = new AlertDialog.Builder(G.currentActivity);
                            builder.setMessage("The Date the you set was invalid ...\nPlease be careful ");
                            builder.setTitle("Warning !!!");
                            builder.setIcon(android.R.drawable.ic_dialog_info);
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int arg1) {}
                            });
                            builder.create().show();
                        }
                    }
                };

                DatePickerDialog DatePicker = new DatePickerDialog(G.currentActivity, dateListener, yearDialog, monthDialog, dayOfMonthDialog);
                DatePicker.show();

            }
        });

        layoutNumbers.setOnClickListener(new OnClickListener() {

            public void onClick(View arg0) {
                final Dialog dialogNumbers = new Dialog(G.currentActivity);
                dialogNumbers.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                dialogNumbers.setContentView(R.layout.dialog_numbers);

                ImageView imgPlus = (ImageView) dialogNumbers.findViewById(R.id.imgPlus);
                ImageView imgDone = (ImageView) dialogNumbers.findViewById(R.id.imgDone);
                ImageView imgMassageText = (ImageView) dialogNumbers.findViewById(R.id.imgMassageText);

                if (readFromPhoneNumbersOnce == false) {
                    readFromPhoneNumbersOnce = true;

                    if (tempPhoneNumbers.size() > 0) {
                        for (String numbers: tempPhoneNumbers) {
                            EditText edtNumber = new EditText(G.currentActivity);
                            edtNumber.setSingleLine(true);
                            edtNumber.setTextSize(13);
                            edtNumber.layout(0, 2, 0, 0);
                            edtNumber.setFilters(new InputFilter[]{ new InputFilter.LengthFilter(11) });
                            edtNumber.setInputType(InputType.TYPE_CLASS_NUMBER);
                            edtNumber.setText(numbers);

                            LinearLayout linearLayout = (LinearLayout) dialogNumbers.findViewById(R.id.edtLayout);
                            LayoutParams layoutParams = new LayoutParams(240, 50);

                            linearLayout.addView(edtNumber, layoutParams);
                            tempEditText.add(edtNumber);
                            currentEditText = edtNumber;
                        }
                    } else {
                        EditText edtNumber = new EditText(G.currentActivity);
                        edtNumber.setHint("0913***2921");
                        edtNumber.setHintTextColor(Color.parseColor("#ffffff"));
                        edtNumber.setSingleLine(true);
                        edtNumber.setTextSize(13);
                        edtNumber.setFocusable(true);
                        edtNumber.layout(0, 2, 0, 0);
                        edtNumber.setFilters(new InputFilter[]{ new InputFilter.LengthFilter(11) });
                        edtNumber.setInputType(InputType.TYPE_CLASS_NUMBER);

                        LinearLayout linearLayout = (LinearLayout) dialogNumbers.findViewById(R.id.edtLayout);
                        LayoutParams layoutParams = new LayoutParams(240, 50);

                        linearLayout.addView(edtNumber, layoutParams);
                        tempEditText.add(edtNumber);
                        currentEditText = edtNumber;
                    }
                } else {
                    for (String numbers: tempPhoneNumbers) {
                        EditText edtNumber = new EditText(G.currentActivity);
                        edtNumber.setSingleLine(true);
                        edtNumber.setTextSize(13);
                        edtNumber.layout(0, 2, 0, 0);
                        edtNumber.setFilters(new InputFilter[]{ new InputFilter.LengthFilter(11) });
                        edtNumber.setInputType(InputType.TYPE_CLASS_NUMBER);
                        edtNumber.setText(numbers);

                        LinearLayout linearLayout = (LinearLayout) dialogNumbers.findViewById(R.id.edtLayout);
                        LayoutParams layoutParams = new LayoutParams(240, 50);

                        linearLayout.addView(edtNumber, layoutParams);
                        currentEditText = edtNumber;
                    }
                }

                imgDone.setOnClickListener(new OnClickListener() {

                    public void onClick(View arg0) {
                        int lastEditTextSize = tempEditText.get(tempEditText.size() - 1).length();
                        if (lastEditTextSize == 11 || lastEditTextSize == 0) {
                            tempPhoneNumbers.clear();
                            for (EditText editText: tempEditText) {
                                if (editText.length() != 0) {
                                    tempPhoneNumbers.add(editText.getText().toString());
                                }
                            }
                            dialogNumbers.dismiss();

                            if (tempPhoneNumbers.size() > 0) {
                                txtNumbers.setText(tempPhoneNumbers.get(0) + " ...");
                            }
                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(G.currentActivity);
                            builder.setMessage("The last Number is invalid ...\nPlease be careful ");
                            builder.setTitle("Warning !!!");
                            builder.setIcon(android.R.drawable.ic_dialog_info);

                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface arg0, int arg1) {

                                }
                            });
                            builder.create().show();
                        }

                    }
                });

                imgPlus.setOnClickListener(new OnClickListener() {

                    public void onClick(View arg0) {
                        if (currentEditText.length() == 11) {
                            EditText edtNumber = new EditText(G.currentActivity);
                            edtNumber.setHint("0913***2921");
                            //edtNumber.setEllipsize());
                            edtNumber.setHintTextColor(Color.parseColor("#ffffff"));
                            edtNumber.setSingleLine(true);
                            edtNumber.setTextSize(13);
                            edtNumber.layout(0, 2, 0, 0);
                            edtNumber.setFilters(new InputFilter[]{ new InputFilter.LengthFilter(11) });
                            edtNumber.setInputType(InputType.TYPE_CLASS_NUMBER);

                            LinearLayout linearLayout = (LinearLayout) dialogNumbers.findViewById(R.id.edtLayout);
                            LayoutParams layoutParams = new LayoutParams(240, 50);

                            linearLayout.addView(edtNumber, layoutParams);
                            tempEditText.add(edtNumber);
                            currentEditText = edtNumber;
                        }

                    }
                });
                dialogNumbers.show();

                dialogNumbers.setOnKeyListener(new Dialog.OnKeyListener() {

                    public boolean onKey(DialogInterface arg0, int keyCode, KeyEvent event) {
                        if (keyCode == KeyEvent.KEYCODE_BACK) {

                            tempEditText.clear();
                            for (String phoneNubmer: tempPhoneNumbers) {
                                EditText edtNumber = new EditText(G.currentActivity);
                                edtNumber.setText(phoneNubmer);
                                tempEditText.add(edtNumber);
                            }
                        }
                        return false;

                    }
                });

                imgMassageText.setOnClickListener(new OnClickListener() {

                    public void onClick(View arg0) {
                        final Dialog dialogMassageText = new Dialog(G.currentActivity);
                        dialogMassageText.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                        dialogMassageText.setContentView(R.layout.dialog_massage_text);

                        ImageView imgMassageCheckDone = (ImageView) dialogMassageText.findViewById(R.id.imgMassageCheckDone);
                        final EditText edtMassageText = (EditText) dialogMassageText.findViewById(R.id.edtMassageText);

                        edtMassageText.setText(massageText);
                        imgMassageCheckDone.setOnClickListener(new OnClickListener() {

                            public void onClick(View arg0) {
                                massageText = edtMassageText.getText().toString();
                                dialogMassageText.dismiss();
                            }
                        });

                        dialogMassageText.show();
                    }
                });

            }
        });

        layoutNote.setOnClickListener(new OnClickListener() {

            public void onClick(View arg0) {
                final Dialog dialogNote = new Dialog(G.currentActivity);
                dialogNote.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                dialogNote.setContentView(R.layout.dialog_note);

                ImageView imgNoteCheckDone = (ImageView) dialogNote.findViewById(R.id.imgNoteCheckDone);
                final EditText edtNoteText = (EditText) dialogNote.findViewById(R.id.edtNoteText);

                edtNoteText.setText(noteText);
                imgNoteCheckDone.setOnClickListener(new OnClickListener() {

                    public void onClick(View arg0) {
                        noteText = edtNoteText.getText().toString();
                        dialogNote.dismiss();
                        if (noteText.length() > 0) {
                            if (noteText.length() > 22) {
                                txtNote.setText(noteText.subSequence(0, 23) + " ...");
                            } else {
                                txtNote.setText(noteText);
                            }
                        }
                    }
                });

                dialogNote.show();
            }
        });

        btnDone.setOnClickListener(new OnClickListener() {

            public void onClick(View arg0) {
                if (checkTimeSet) {
                    currentReminder.title = edtTitle.getText().toString();
                    currentReminder.ratePriority = ratePriority.getRating();
                    currentReminder.active = chkActive.isChecked();
                    currentReminder.year = year;
                    currentReminder.month = monthOfYear;
                    currentReminder.day = dayOfMonth;
                    currentReminder.hourNotification = hourOfNotification;
                    currentReminder.minuteNotification = minuteOfNotification;
                    currentReminder.hourAlarm = hourOfAlarm;
                    currentReminder.minuteAlarm = minuteOfAlarm;

                    if (txtRepetition.getText().toString().equals("Current Repeat")) {
                        currentReminder.repetition = 1;
                    }

                    currentReminder.massageText = massageText;
                    currentReminder.extraNote = noteText;
                    currentReminder.phoneNumbers = tempPhoneNumbers;

                    if (mustInsert == true) {

                        G.database.execSQL("INSERT INTO reminder (reminder_title,reminder_ratePriority,reminder_active,reminder_year,reminder_month,reminder_day,reminder_hourNotification,reminder_minuteNotification,reminder_repetition,reminder_hourAlarm,reminder_minuteAlarm,reminder_extraNote,reminder_massageText,reminder_phoneNumbers) VALUES ( " +
                                "  '" + currentReminder.title + "'  " +
                                ", '" + currentReminder.ratePriority + "'  " +
                                ", '" + currentReminder.active + "'  " +
                                ", '" + currentReminder.year + "'  " +
                                ", '" + currentReminder.month + "'  " +
                                ", '" + currentReminder.day + "'  " +
                                ", '" + currentReminder.hourNotification + "'  " +
                                ", '" + currentReminder.minuteNotification + "'  " +
                                ", '" + currentReminder.repetition + "'  " +
                                ", '" + currentReminder.hourAlarm + "'  " +
                                ", '" + currentReminder.minuteAlarm + "'  " +
                                ", '" + currentReminder.extraNote + "'  " +
                                ", '" + currentReminder.massageText + "'  " +
                                ", '" + currentReminder.phoneNumbers + "' " +
                                ")");
                        currentReminder.id = G.currentReminderId + 1;
                        G.currentReminderId++;

                        G.reminders.add(currentReminder);

                    } else {
                        G.database.execSQL("UPDATE reminder SET " +
                                "reminder_title = '" + currentReminder.title + "'" +
                                ", reminder_ratePriority = '" + currentReminder.ratePriority + "'" +
                                ", reminder_active = '" + currentReminder.active + "'" +
                                ", reminder_year = '" + currentReminder.active + "'" +
                                ", reminder_month = '" + currentReminder.month + "'" +
                                ", reminder_day = '" + currentReminder.day + "'" +
                                ", reminder_hourNotification = '" + currentReminder.hourNotification + "'" +
                                ", reminder_minuteNotification = '" + currentReminder.minuteNotification + "'" +
                                ", reminder_repetition = '" + currentReminder.repetition + "'" +
                                ", reminder_hourAlarm = '" + currentReminder.hourAlarm + "'" +
                                ", reminder_minuteAlarm = '" + currentReminder.minuteAlarm + "'" +
                                ", reminder_extraNote = '" + currentReminder.extraNote + "'" +
                                ", reminder_massageText = '" + currentReminder.massageText + "'" +
                                ", reminder_phoneNumbers = '" + currentReminder.phoneNumbers + "'" +
                                " WHERE  reminder_id = '" + currentReminder.id + "'");
                    }

                    if (currentReminder.active == true) {

                        // Alarm 

                        if (checkAlarmSet) {
                            Intent myIntent = new Intent(G.context, ActivityAlarm.class);
                            myIntent.putExtra("HOURALARM", currentReminder.hourAlarm);
                            myIntent.putExtra("MINUTEALARM", currentReminder.minuteAlarm);
                            myIntent.putExtra("YEAR", currentReminder.year);
                            myIntent.putExtra("MONTH", currentReminder.month);
                            myIntent.putExtra("DAY", currentReminder.day);
                            myIntent.putExtra("TITLE", currentReminder.title);

                            myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            PendingIntent pendingIntent = PendingIntent.getActivity(G.context, 0, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                            Calendar calendarAlarm = Calendar.getInstance();
                            calendarAlarm.set(Calendar.YEAR, currentReminder.year);
                            calendarAlarm.set(Calendar.MONTH, currentReminder.month);
                            calendarAlarm.set(Calendar.DAY_OF_MONTH, currentReminder.day);
                            calendarAlarm.set(Calendar.HOUR_OF_DAY, currentReminder.hourAlarm);
                            calendarAlarm.set(Calendar.MINUTE, currentReminder.minuteAlarm);
                            calendarAlarm.set(Calendar.SECOND, 00);
                            //calendarAlarm.set(Calendar.AM_PM, currentReminder.hourAlarm < 12 ? Calendar.AM : Calendar.PM);

                            G.alarmManager.set(AlarmManager.RTC_WAKEUP, calendarAlarm.getTimeInMillis(), pendingIntent);
                        }

                        // Notification 

                        Intent intent = new Intent(G.context, ShowNotification.class);
                        intent.putExtra("DATE", txtDate.getText().toString());
                        intent.putExtra("TITLE", currentReminder.title);
                        intent.putExtra("PHONE_NUMBERS", currentReminder.phoneNumbers);
                        intent.putExtra("MASSAGE_TEXT", currentReminder.massageText);
                        PendingIntent pendingIntentNtf = PendingIntent.getBroadcast(G.context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                        Calendar calendarNotification = Calendar.getInstance();
                        calendarNotification.set(Calendar.YEAR, currentReminder.year);
                        calendarNotification.set(Calendar.MONTH, currentReminder.month);
                        calendarNotification.set(Calendar.DAY_OF_MONTH, currentReminder.day);
                        calendarNotification.set(Calendar.HOUR_OF_DAY, currentReminder.hourNotification);
                        calendarNotification.set(Calendar.MINUTE, currentReminder.minuteNotification);
                        calendarNotification.set(Calendar.SECOND, 00);
                        // calendarNotification.set(Calendar.AM_PM, currentReminder.hourNotification < 12 ? Calendar.AM : Calendar.PM);

                        G.alarmManager.set(AlarmManager.RTC_WAKEUP, calendarNotification.getTimeInMillis(), pendingIntentNtf);

                    }

                    finish();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(G.currentActivity);
                    builder.setMessage("You have to set the time ...\nPlease be careful ");
                    builder.setTitle("Warning !!!");
                    builder.setIcon(android.R.drawable.ic_dialog_info);
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int arg1) {}
                    });
                    builder.create().show();
                }

            }
        });

    }
}
