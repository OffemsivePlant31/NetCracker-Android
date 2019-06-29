package com.nc.nc_android.background;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.nc.nc_android.MainActivity;
import com.nc.nc_android.R;
import com.nc.nc_android.dto.GameNotificationDto;
import com.nc.nc_android.dto.NotificationDto;
import com.nc.nc_android.localdata.UserData;
import com.nc.nc_android.retrofit.NotificationApi;
import com.nc.nc_android.retrofit.RetrofitSingleton;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class NotificationService extends IntentService{

    public NotificationService() {
        super("NotificationService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        NotificationApi api = RetrofitSingleton.getInstance(getApplicationContext()).getApi(NotificationApi.class);

        long userid;

        while(true){

            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            userid = UserData.loadUserId();
            if (userid == -1) continue;

            api.getAll(userid).enqueue(new Callback<List<NotificationDto>>() {
                @Override
                public void onResponse(Call<List<NotificationDto>> call, Response<List<NotificationDto>> response) {
                    if(!(response.code() == 200)||(response.body() == null)) return;

                    int notificationId = 0;
                    String page = null;

                    for(NotificationDto dto : response.body()){

                        switch ((int)dto.getType() / 10){
                            case 1:
                            case 2:
                            case 3:
                            case 4: notificationId = 1; page = "game"; break;
                            case 5: notificationId = 2; page = "game"; break;
                            case 6: notificationId = 3; page = "welcome"; break;
                        }

                        createNotification(dto.getTitle(), dto.getText(), notificationId, page);

                    }
                }

                @Override
                public void onFailure(Call<List<NotificationDto>> call, Throwable t) {

                }
            });


        }


    }




    private void createNotification(String title, String text, int id, String page){
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        mBuilder
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(text)
                .setAutoCancel(true);

        NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
        bigText.bigText(text);
        bigText.setBigContentTitle(title);
        bigText.setSummaryText(title);

        mBuilder.setStyle(bigText);
        mBuilder.setPriority(NotificationCompat.PRIORITY_MAX);

        Intent resultIntent = new Intent(this, MainActivity.class);
        resultIntent.putExtra("page", page);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);

        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.notify(id, mBuilder.build());

    }
}
