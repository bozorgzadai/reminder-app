package ali.bozorgzad.project.app.reminder;

import java.util.ArrayList;


public class StructReminder {

    public int               id;
    public String            title;
    public float             ratePriority;
    public boolean           active;
    public int               year;
    public int               month;
    public int               day;
    public int               hourNotification;
    public int               minuteNotification;

    public int               hourAlarm;
    public int               minuteAlarm;
    public int               repetition;
    public String            extraNote;
    public String            massageText;
    public ArrayList<String> phoneNumbers = new ArrayList<String>();
}
