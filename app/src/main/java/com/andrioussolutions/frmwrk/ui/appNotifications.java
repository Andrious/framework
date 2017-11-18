package com.andrioussolutions.frmwrk.ui;

import com.google.firebase.messaging.RemoteMessage;

import com.andrioussolutions.R;
import com.andrioussolutions.frmwrk.App;
import com.gtfp.errorhandler.ErrorHandler;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
/**
 *  Copyright  2017  Andrious Solutions Ltd.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 *
 * Created  3/7/2017.
 */
public class appNotifications{

    private static NotificationCompat.Builder mNotificationBuilder;

    private static NotificationManager mNotificationManager;

    private static int mIcon;

    private static String mTitle;

    private static boolean mAutoCancel;

    private static Uri mSoundUri;

     private static appNotifications self;


    private appNotifications(){

    }



    public static appNotifications getInstance(){

        if(self == null){

           self =  new appNotifications();

            appNotifications.onCreate(App.getController());
        }

        return self;
    }




    public static void onCreate(Activity activity){

        if(self == null){

            self = new appNotifications();
        }

        // Don't call more than once.
        if(mNotificationBuilder != null) return;

        mNotificationBuilder = new NotificationCompat.Builder(activity);

        mNotificationManager =
                (NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent intent = new Intent(activity, activity.getClass());

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent
                .getActivity(activity, 0 /* Request code */, intent, PendingIntent.FLAG_ONE_SHOT);

        mNotificationBuilder.setContentIntent(pendingIntent);

        // Best to initialize everything here.
       mIcon = -1;

        mAutoCancel = true;
    }




    public static void onDestroy(){

        self = null;

        mNotificationBuilder = null;

        mNotificationManager = null;
    }




    public static appNotifications setSmallIcon(int icon){

        if (icon > 0){

            mIcon = icon;
        }

        return self;
    }




    public static appNotifications setContentTitle(@NonNull String title){

        if (title != null && !title.isEmpty()){

            mTitle = title;
        }

        return self;
    }




    public static appNotifications setAutoCancel(boolean autoCancel){

        mAutoCancel = autoCancel;

        return self;
    }




    public static appNotifications setSound(@NonNull Uri url){

        mSoundUri = url;

        return self;
    }



    public static appNotifications setSound(@NonNull String name){

        if(name == null) return self;

        if(name.equals("default")){

            mSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        }

        return self;
    }



    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    public static void send(@NonNull String messageBody){

        if(mNotificationBuilder == null) {

            ErrorHandler.logError("appNotification.onCreate() was not called!");

            return;
        }

        if(messageBody == null || messageBody.isEmpty()) return;

        mNotificationBuilder
                .setSmallIcon(mIcon == -1 ? R.drawable.ic_notification : mIcon)
                .setContentTitle(mTitle == null ? "Notification" : mTitle)
                .setAutoCancel(mAutoCancel)
                .setContentText(messageBody);

        if(mSoundUri != null){

            mNotificationBuilder.setSound(mSoundUri);
        }

        mNotificationManager.notify(0 /* ID of notification */, mNotificationBuilder.build());
    }




    public static void send(@NonNull RemoteMessage remoteMessage){

        RemoteMessage.Notification notify =  remoteMessage.getNotification();

        if(notify == null) return;

        setContentTitle(notify.getTitle());

        setSound(notify.getSound());

        send(notify.getBody());
    }
}
