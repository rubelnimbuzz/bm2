//#if SYSTEM_NOTIFY && android
//# package Messages.notification;
//# 
//# import android.app.Activity;
//# import android.app.NotificationManager;
//# import android.app.PendingIntent;
//# import android.content.Context;
//# import android.content.Intent;
//# import org.bombusmod.BombusModActivity;
//# import org.bombusmod.R;
//# import Client.Msg;
//# import Client.StaticData;
//# 
//# public class AndroidNotification implements Notificator {
//# 
//#     private static final int NOTIFY_ID = 1;
//# 
//#     public void sendNotify(final String title, final String text) {
//#         NotificationManager mNotificationManager = (NotificationManager) BombusModActivity.getInstance().getSystemService(Context.NOTIFICATION_SERVICE);
//#         long when = System.currentTimeMillis();
//#         int icon = R.drawable.app_icon;
//#         if (StaticData.getInstance().roster.highliteMessageCount < 1) {
//#             return;
//#         } else {
//#             android.app.Notification notification = new android.app.Notification(icon, "New messages: " + StaticData.getInstance().roster.highliteMessageCount, when);
//#             notification.flags |= android.app.Notification.FLAG_AUTO_CANCEL;
//#             Intent notificationIntent = new Intent(BombusModActivity.getInstance(), BombusModActivity.class);
//#             PendingIntent contentIntent = PendingIntent.getActivity(BombusModActivity.getInstance(), 0, notificationIntent, 0);
//#             notification.setLatestEventInfo(BombusModActivity.getInstance().getApplicationContext(), "You have new messages...", "Unread messages: " + StaticData.getInstance().roster.highliteMessageCount, contentIntent);
//#             notification.ledARGB = 0xff00ff00;
//#             notification.ledOnMS = 300;
//#             notification.ledOffMS = 1000;
//#             notification.flags |= android.app.Notification.FLAG_SHOW_LIGHTS;
//#             notification.number = StaticData.getInstance().roster.highliteMessageCount;
//#             mNotificationManager.notify(NOTIFY_ID, notification);
//#         }
//#     }
//# 
//#     public void clear() {
//#         NotificationManager mNotificationManager = (NotificationManager) BombusModActivity.getInstance().getSystemService(Context.NOTIFICATION_SERVICE);
//#         mNotificationManager.cancel(NOTIFY_ID);
//#     }
//# }
//#endif

