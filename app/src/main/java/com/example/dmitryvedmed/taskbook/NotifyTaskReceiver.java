package com.example.dmitryvedmed.taskbook;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.example.dmitryvedmed.taskbook.logic.DBHelper;
import com.example.dmitryvedmed.taskbook.logic.ListNote;
import com.example.dmitryvedmed.taskbook.logic.SimpleNote;
import com.example.dmitryvedmed.taskbook.logic.SuperNote;
import com.example.dmitryvedmed.taskbook.ui.ListNoteDialogActivity;
import com.example.dmitryvedmed.taskbook.ui.SimpleNoteDialogActivity;
import com.example.dmitryvedmed.taskbook.untils.Constants;

public class NotifyTaskReceiver extends BroadcastReceiver {
    public NotifyTaskReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        int id = intent.getIntExtra(Constants.ID, -2);
        if(id==-2) {
            return;
        }
        DBHelper dbHelper = new DBHelper(context);
        SuperNote superNote = dbHelper.getNote(id);
        if(superNote == null)
            return;
        Intent notificationIntent;

        if(superNote instanceof SimpleNote){
            SimpleNote task = (SimpleNote) superNote;
            if(!task.isRepeating())
                task.setRemind(false);
            else {
                task.setReminderTime(task.getReminderTime() + task.getRepeatingPeriod());
            }

            notificationIntent = new Intent(context, SimpleNoteDialogActivity.class);
            notificationIntent.putExtra(Constants.ID, task.getId());
            notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            PendingIntent contentIntent = PendingIntent.getActivity(context,
                    0, notificationIntent,
                    PendingIntent.FLAG_CANCEL_CURRENT);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

            Resources res = context.getResources();

            builder.setContentIntent(contentIntent)
                    .setSmallIcon(R.drawable.ic_menu_white_24dp)
                    .setLargeIcon(BitmapFactory.decodeResource(res, R.mipmap.super_ic))
                    .setTicker(String.valueOf(task.getId()))
                    .setWhen(System.currentTimeMillis())
                    .setAutoCancel(false)
                    .addAction(R.drawable.ic_menu_white_24dp, context.getString(R.string.act_open), contentIntent)
                    .setContentTitle(task.getHeadLine())
                    .setContentText(task.getContent());

            Notification notification = builder.build();
            Uri ringURI =
                    RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            notification.sound = ringURI;
            notification.flags = notification.flags | Notification.FLAG_ONGOING_EVENT;
            NotificationManager notificationManager = (NotificationManager) context
                    .getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(id, notification);
            dbHelper.updateNote(task, null);
        }
        else{
            notificationIntent = new Intent(context, ListNoteDialogActivity.class);
            ListNote task = (ListNote) superNote;
            if(!task.isRepeating())
                task.setRemind(false);
            else {
                task.setReminderTime(task.getReminderTime() + task.getRepeatingPeriod());
            }
            notificationIntent.putExtra(Constants.ID, task.getId());
            notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            PendingIntent contentIntent = PendingIntent.getActivity(context,
                    0, notificationIntent,
                    PendingIntent.FLAG_CANCEL_CURRENT);


            NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
            Resources res = context.getResources();

            String text = "";
            if(task.getUncheckedItems().size() != 0){
                for (int i = task.getUncheckedItems().size()-1; i >= 0 ; i--) {
                    if(!task.getUncheckedItems(i).equals("")){
                        text = task.getUncheckedItems(i);
                        continue;
                    }
                }
            }
            if (text.equals("")){
                for (int i = task.getCheckedItems().size()-1; i >= 0 ; i--) {
                    if(!task.getCheckedItem(i).equals("")){
                        text = task.getCheckedItem(i);
                        continue;
                    }
                }
            }


            builder.setContentIntent(contentIntent)
                    .setSmallIcon(R.drawable.ic_format_list_checks_white_24dp)
                    .setLargeIcon(BitmapFactory.decodeResource(res, R.mipmap.super_ic))
                    .setTicker(String.valueOf(task.getId()))
                    .setWhen(System.currentTimeMillis())
                    .setAutoCancel(false)
                    .addAction(R.drawable.ic_format_list_checks_white_24dp, context.getString(R.string.act_open), contentIntent)
                    .setContentTitle(task.getHeadLine())
                    .setContentText(text);

            Notification notification = builder.build();

            Uri ringURI =
                    RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            notification.sound = ringURI;
            notification.flags = notification.flags | Notification.FLAG_ONGOING_EVENT;
            NotificationManager notificationManager = (NotificationManager) context
                    .getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(id, notification);
            dbHelper.updateNote(task, null);
        }
    }
}
