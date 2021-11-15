package ali.bozorgzad.project.app.reminder;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.view.Window;


public class ActivitySetting extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.reminder_settings);
    }
}
