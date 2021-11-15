package ali.bozorgzad.project.app.reminder;

import java.util.ArrayList;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;


public class AdapterReminder extends ArrayAdapter<StructReminder> {

    public AdapterReminder(ArrayList<StructReminder> array) {
        super(G.context, R.layout.adapter_reminder, array);
    }


    private static class ViewHolder {

        //private int      positionItemDel;
        public ViewGroup layoutRoot;
        public TextView  txtTitle;
        public TextView  txtTime;
        public TextView  txtDate;
        public CheckBox  chkActive;
        public ImageView imgDelete;
        public RatingBar chkRatePriority;


        public ViewHolder(View view) {
            layoutRoot = (ViewGroup) view.findViewById(R.id.layoutRoot);
            txtTitle = (TextView) view.findViewById(R.id.txtTitle);
            txtTime = (TextView) view.findViewById(R.id.txtTime);
            txtDate = (TextView) view.findViewById(R.id.txtDate);
            chkActive = (CheckBox) view.findViewById(R.id.chkActive);
            chkRatePriority = (RatingBar) view.findViewById(R.id.chkRatePriority);
            imgDelete = (ImageView) view.findViewById(R.id.imgDelete);
        }


        public void fill(final ArrayAdapter<StructReminder> adapter, final StructReminder item, final int position) {
            txtTitle.setText(item.title);

            boolean showTaskTimeKey = G.sharedPreferences.getBoolean("SHOW_TASK_TIME_KEY", true);
            boolean showTaskDateKey = G.sharedPreferences.getBoolean("SHOW_TASK_DATE_KEY", true);

            if (showTaskTimeKey) {
                txtTime.setText(item.hourNotification + " : " + item.minuteNotification);
            } else {
                txtTime.setText("");
            }
            if (showTaskDateKey) {
                int tempMonth = item.month;
                tempMonth++;
                txtDate.setText(item.year + " /" + tempMonth + " /" + item.day);
            } else {
                txtDate.setText("");
            }

            chkActive.setChecked(item.active);
            chkRatePriority.setRating(item.ratePriority);

            layoutRoot.setOnClickListener(new OnClickListener() {

                public void onClick(View arg0) {
                    Intent intent = new Intent(G.currentActivity, ActivityReminderDetails.class);
                    intent.putExtra("POSITION", position);
                    G.currentActivity.startActivity(intent);
                }
            });

            imgDelete.setOnClickListener(new OnClickListener() {

                public void onClick(View arg0) {
                    AlertDialogDelete(adapter, item);
                }
            });

            chkActive.setOnCheckedChangeListener(new OnCheckedChangeListener() {

                public void onCheckedChanged(CompoundButton arg0, boolean currentState) {
                    item.active = currentState;
                }
            });
        }


        private void AlertDialogDelete(final ArrayAdapter<StructReminder> adapter, final StructReminder item) {

            AlertDialog.Builder builder = new AlertDialog.Builder(G.currentActivity);
            builder.setMessage("Are you sure that you wanna delete a task ?");
            builder.setTitle("Delete !!!");
            builder.setIcon(android.R.drawable.ic_delete);

            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface arg0, int arg1) {
                    adapter.remove(item);

                    G.database.execSQL("DELETE FROM reminder WHERE reminder_id = '" + item.id + "' ");
                    //G.reminders.remove(positionItemDel);
                    //adapter.notifyDataSetChanged();
                }
            });
            builder.setNeutralButton("CANCEL", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface arg0, int arg1) {

                }
            });
            builder.create().show();
        }

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        StructReminder item = getItem(position);
        if (convertView == null) {
            convertView = G.layoutInflater.inflate(R.layout.adapter_reminder, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.fill(this, item, position);
        return convertView;
    }
}
