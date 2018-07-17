package com.smonline.virtual.client.stub;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.IBinder;

import com.smonline.virtual.R;

/**
 * @author Lody
 */
public class DaemonService extends Service {

    private static final int NOTIFY_ID = 1001;
    private static PendingIntent mPendingIntent;

    public static void startup(Context context) {
        context.startService(new Intent(context, DaemonService.class));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        startup(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("com.smonline.appbox", "com.smonline.appbox.ui.home.HomeActivity"));
        mPendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        startService(new Intent(this, InnerService.class));
        Notification.Builder builder = new Notification.Builder(this);
        builder.setSmallIcon(R.mipmap.notification_icon);
        builder.setContentTitle(this.getString(R.string.fg_notification_title));
        builder.setContentText(this.getString(R.string.fg_notification_tip));
        builder.setContentIntent(mPendingIntent);
        startForeground(NOTIFY_ID, builder.build());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    public static final class InnerService extends Service {

        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            Notification.Builder builder = new Notification.Builder(this);
            builder.setSmallIcon(R.mipmap.notification_icon);
            builder.setContentTitle(this.getString(R.string.fg_notification_title));
            builder.setContentText(this.getString(R.string.fg_notification_tip));
            builder.setContentIntent(mPendingIntent);
            startForeground(NOTIFY_ID, builder.build());
            stopForeground(true);
            stopSelf();
            return super.onStartCommand(intent, flags, startId);
        }

        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }
    }
}
