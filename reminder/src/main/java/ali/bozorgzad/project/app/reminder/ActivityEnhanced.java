package ali.bozorgzad.project.app.reminder;

import android.app.Activity;


public class ActivityEnhanced extends Activity {

    @Override
    protected void onResume() {
        G.currentActivity = this;
        super.onResume();

    }

}
