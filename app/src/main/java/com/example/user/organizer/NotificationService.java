package com.example.user.organizer;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

//-------------Сервис для проверки и вывода уведомелений о новых событиях----------------------
public class NotificationService extends Service {
    private String idAuthUser;
    private NotificationManager nm;
    private Context context;
    DBUtilities dbUtilities;
    Thread thread;

    public NotificationService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        dbUtilities = new DBUtilities(context);
        nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }//onCreate

    @Override // полезная работа сервиса
    public int onStartCommand(Intent intent, int flags, int startId) {
        idAuthUser = intent.getStringExtra("idAuthUser");
        someWork();
        return super.onStartCommand(intent, flags, startId);
    }//onStartCommand

    private void someWork() {
        //формируем поток
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while(true) {
                        //проверяем наличие сообщение принадлежащие данному учаснику
                        List<com.example.user.organizer.Notification> listNotification = new ArrayList<>();
                        listNotification.addAll(dbUtilities.getSomeNotifications(idAuthUser));

                        if (listNotification.size() > 0)
                            for (com.example.user.organizer.Notification notification : listNotification) {
                                sendNotification(notification);

                                dbUtilities.deleteRowByValue(
                                        "notifications",
                                        "id",
                                        notification.getId()
                                );//deleteRowByValue

                            }//foreach
                        Log.d("qwe", String.format("работает поток %s       %s", this.hashCode(), idAuthUser));
                        //задержка перед формирование следующего файла
                        Thread.sleep(2000);
                    }//while
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        //запускаем поток
        thread.start();
    }//someWork

    private void sendNotification(com.example.user.organizer.Notification notification) {
        String messageText = String.format("%s%s %s в %s,\n в городе %s на поле %s",
                !notification.getNotice().equals(" ") ?//здесь указывается имя учасника покидающего собвтие
                        dbUtilities.searchValueInColumn(
                                "users",
                                "id",
                                "name",
                                notification.getNotice()
                        )+" ":"",
                notification.getMessage(),
                notification.getDate(),
                notification.getTime(),
                notification.getCity_id(),
                notification.getField_id());

        Notification.Builder nBuilder = new Notification.Builder(this)
                .setContentText(messageText)
                .setContentTitle(notification.getMessage())
                .setSmallIcon(R.drawable.file)
                .setStyle(new Notification.BigTextStyle()
                        .bigText(messageText))
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.file));

        // 3-я часть  Click on notification
        Intent intent = new Intent(this, NavigationDrawerLogInActivity.class);
        intent.putExtra("idAuthUser", idAuthUser);
        intent.putExtra("notice", notification.getNotice());
        intent.putExtra("title", notification.getMessage());
        intent.putExtra("eventId", notification.getEvent_id());
        intent.putExtra("notificationServiceFlag", true);
        intent.putExtra("messageText", messageText);
        intent.putExtra("messageId",
                dbUtilities.getIdByValue(
                        "notificationmessages",
                        "message",
                        notification.getMessage()
                )//getIdByValue
        );// intent.putExtra
        PendingIntent pIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        nBuilder.setContentIntent(pIntent);
        Notification notif = nBuilder.build();

        // ставим флаг, чтобы уведомление пропало после нажатия
        notif.flags |= Notification.FLAG_AUTO_CANCEL;
        notif.defaults = Notification.DEFAULT_ALL;

        // отправляем
        //Utils.getRandom(1, 100) - чтоб сообщений приходило много, а не одно
        nm.notify(Utils.getRandom(1, 100), notif);
    }//sendNotification

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {
        thread.interrupt();
        Log.d("qwe", String.format("Прервать поток %s       %s", this.hashCode(), idAuthUser));
        super.onDestroy();
    }
}
