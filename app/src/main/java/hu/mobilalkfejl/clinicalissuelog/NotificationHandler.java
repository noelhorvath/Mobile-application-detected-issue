package hu.mobilalkfejl.clinicalissuelog;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;

import androidx.core.app.NotificationCompat;

public class NotificationHandler {
    private static final String CHANNEL_ID = "clinical_issue_log_notification_channel";
    private final int NOTIFICATION_ID_CREATE = 0;
    private final int NOTIFICATION_ID_DELETE = 1;
    private final int NOTIFICATION_ID_UPDATE = 2;

    private NotificationManager notificationManager;
    private Context currentContext;

    public NotificationHandler(Context context){
        this.currentContext = context;
        this.notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        createChannel();
    }

    public void createChannel(){
        NotificationChannel channel = new NotificationChannel
                (CHANNEL_ID, "Clinical Issue Notification", NotificationManager.IMPORTANCE_HIGH);

        channel.enableLights(true);
        channel.setLightColor(Color.RED);
        channel.enableVibration(true);
        channel.setImportance(NotificationManager.IMPORTANCE_DEFAULT);
        channel.setDescription("Notifications from Clinical Issue Log application");

        notificationManager.createNotificationChannel(channel);
    }

    public void sendAfterCreate(String message) {
        Intent intent = new Intent(currentContext, DetectedIssueListActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(currentContext, NOTIFICATION_ID_CREATE, intent, PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(currentContext, CHANNEL_ID)
                .setContentTitle("Clinical Issue Log Application")
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setSmallIcon(R.drawable.ic_healing_notification_icon)
                .setContentIntent(pendingIntent);

        notificationManager.notify(NOTIFICATION_ID_CREATE, builder.build());
    }

    public void sendAfterDelete(String message) {
        Intent intent = new Intent(currentContext, ViewDetectedIssueActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(currentContext, NOTIFICATION_ID_DELETE, intent, PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(currentContext, CHANNEL_ID)
                .setContentTitle("Clinical Issue Log Application")
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setSmallIcon(R.drawable.ic_healing_notification_icon)
                .setContentIntent(pendingIntent);

        notificationManager.notify(NOTIFICATION_ID_DELETE, builder.build());
    }

    public void sendAfterUpdate(String message) {
        Intent intent = new Intent(currentContext, EditDetectedIssueActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(currentContext, NOTIFICATION_ID_UPDATE, intent, PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(currentContext, CHANNEL_ID)
                .setContentTitle("Clinical Issue Log Application")
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setSmallIcon(R.drawable.ic_healing_notification_icon)
                .setContentIntent(pendingIntent);

        notificationManager.notify(NOTIFICATION_ID_UPDATE, builder.build());
    }

    public void cancelDeleteNotification() {
        notificationManager.cancel(NOTIFICATION_ID_DELETE);
    }
    public void cancelCreateNotification() { notificationManager.cancel(NOTIFICATION_ID_CREATE); }
    public void cancelUpdateNotification() { notificationManager.cancel(NOTIFICATION_ID_UPDATE); }

}
