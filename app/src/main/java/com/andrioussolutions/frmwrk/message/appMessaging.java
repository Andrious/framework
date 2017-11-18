package com.andrioussolutions.frmwrk.message;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import com.andrioussolutions.frmwrk.appFireBaseDB;

import android.app.Activity;
import android.support.annotation.Keep;
import android.support.annotation.NonNull;

import java.util.HashSet;
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
 *
 * Created  3/6/2017.
 */
public class appMessaging{

    private static MessagingService mMessenger;

    private static String mToken = "";

    private static HashSet<MessageReceivedListener> mMessageListeners;

    private static HashSet<DataMessageListener> mDataMessageListeners;

    private static HashSet<NotificationListener> mNotificationListeners;



    private appMessaging(){

    }



    public static void onCreate(Activity activity){

        mMessenger = new MessagingService();

        // Best to initialize everything here.
        mMessageListeners = new HashSet<>();

        mDataMessageListeners = new HashSet<>();

        mNotificationListeners = new HashSet<>();
    }



    public static String getToken(){

        String token = FirebaseInstanceId.getInstance().getToken();

        if (token != null){ mToken = token; }

        return mToken;
    }



    public static String setToken(String token){

        String oldToken = mToken;

        mToken = token;

        return oldToken;
    }



    public static void addMessageListener(@NonNull MessageReceivedListener listener){

        mMessageListeners.add(listener);
    }



    public static void removeMessageListener(@NonNull MessageReceivedListener listener){

        mMessageListeners.remove(listener);
    }




    public static void addDataMessageListener(@NonNull DataMessageListener listener){

        mDataMessageListeners.add(listener);
    }




    public static void removeDataMessageListener(@NonNull DataMessageListener listener){

        mDataMessageListeners.remove(listener);
    }




    public static void addNotificationListener(@NonNull NotificationListener listener){

        mNotificationListeners.add(listener);
    }




    public static void removeNotificationListener(@NonNull NotificationListener listener){

        mNotificationListeners.remove(listener);
    }





    public static void onDestroy(){

        mMessenger = null;

        mMessageListeners = null;

        mDataMessageListeners =null;

        mNotificationListeners = null;
    }



     @Keep
    public interface MessageReceivedListener{

        void onMessageReceived(RemoteMessage remoteMessage);
    }



   @Keep
    public interface DataMessageListener{

        void onMessageReceived(RemoteMessage remoteMessage);
    }



     @Keep
    public interface NotificationListener{

        void onMessageReceived(RemoteMessage remoteMessage);
    }



   @Keep
    public static class MessagingService extends FirebaseMessagingService{



        public MessagingService(){

            super();
        }



        /**
         * Called when message is received.
         *
         * @param message Object representing the message received from Firebase Cloud Messaging.
         */
        // [START receive_message]
        @Override
        public void onMessageReceived(RemoteMessage message){
            // [START_EXCLUDE]
            // There are two types of messages data messages and notification messages. Data messages are handled
            // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
            // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
            // is in the foreground. When the app is in the background an automatically generated notification is displayed.
            // When the user taps on the notification they are returned to the app. Messages containing both notification
            // and data payloads are treated as notification messages. The Firebase console always sends notification
            // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
            // [END_EXCLUDE]

            for (MessageReceivedListener listener : mMessageListeners){

                listener.onMessageReceived(message);
            }

            // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
//            Log.d(TAG, "From: " + message.getFrom());

            // Check if message contains a data payload.
            if (message.getData().size() > 0){

                for (DataMessageListener listener : mDataMessageListeners){

                    listener.onMessageReceived(message);
                }
                // Check if message contains a notification payload.
            } else if (message.getNotification() != null){

                for (NotificationListener listener : mNotificationListeners){

                    listener.onMessageReceived(message);
                }
//                sendNotification(message.getNotification().getBody());
//                Log.d(TAG, "Message Notification Body: " + message.getNotification().getBody());
            }

            // Also if you intend on generating your own notifications as a result of a received FCM
            // message, here is where that should be initiated. See sendNotification method below.
        }
        // [END receive_message]
    }



@Keep
    public static class TokenService extends FirebaseInstanceIdService{

        public TokenService(){

            super();
        }



        /**
         * Called if InstanceID token is updated. This may occur if the security of
         * the previous token had been compromised. Note that this is called when the InstanceID
         * token
         * is initially generated so this is where you would retrieve the token.
         */
        @Override
        public void onTokenRefresh(){

            // Get updated InstanceID token.
            // If you want to send messages to this application instance or
            // manage this apps subscriptions on the server side, send the
            // Instance ID token to your app server.
            sendRegistrationToServer(FirebaseInstanceId.getInstance().getToken());
        }



        /**
         * Persist token to third-party servers.
         *
         * Modify this method to associate the user's FCM InstanceID token with any server-side
         * account
         * maintained by your application.
         *
         * @param token The new token.
         */
        private void sendRegistrationToServer(String token){

            // TODO: Implement this method to send token to your app server.
            appFireBaseDB logFeedback = new appFireBaseDB("Token");

            logFeedback.insert("Token", token);
        }
    }
}
