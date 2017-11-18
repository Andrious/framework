package com.andrioussolutions.frmwrk.message;

import com.google.firebase.messaging.RemoteMessage;

import com.andrioussolutions.R;
import com.andrioussolutions.frmwrk.appAnalytics;
import com.andrioussolutions.frmwrk.appFireBaseDB;
import com.andrioussolutions.frmwrk.files.appFile;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
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
 * Created  3/8/2017.
 */


public class appChatLine{

    private static final String CHAT_FILE = "chat.txt";

    private Context mContext;

    private Resources mResources;

    private  LinearLayout mView;




    public appChatLine(Context context){

        mContext = context;

        mResources = context.getResources();
    }



    public static String getFileName(){

        return CHAT_FILE;
    }



    public static boolean log(String message){

        if (message == null || message.isEmpty()){ return false; }

        return appFile.write(CHAT_FILE, message.trim() + "\n\n" + getContent());
    }



    public static String getContent(){

        return appFile.read(CHAT_FILE);
    }



    public static boolean log(RemoteMessage message){

        RemoteMessage.Notification notify = message.getNotification();

        if (notify == null){ return false; }

        String title = notify.getTitle();

        String body = notify.getBody();

        return log(title == null ? "" : title + "\n" + (body == null ? "" : body));
    }



    public View getView(){

        LinearLayout list = new LinearLayout(mContext);

        list.setOrientation(LinearLayout.VERTICAL);

        TextView tv = new TextView(mContext);

        tv.setText(getContent());

        list.addView(tv);

        ScrollView scroll = new ScrollView(mContext);

        scroll.addView(list);

        EditText msg = new EditText(mContext);

        mView  = new LinearLayout(mContext);

        mView.setOrientation(LinearLayout.VERTICAL);

        mView.addView(scroll);

        mView.addView(msg);

        LinearLayout buttons = new LinearLayout(mContext);

        Button cancel = new Button(mContext, null, android.R.attr.buttonBarButtonStyle);

        cancel.setText(mResources.getString(R.string.cancel));

        cancel.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1));

        cancel.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v){

                Object tag = mView.getTag();

                if(tag != null && tag instanceof Dialog){

                    ((Dialog)tag).cancel();

                    mView.setTag(null);
                }
            }
        });

        Button send = new Button(mContext, null, android.R.attr.buttonBarButtonStyle);

        send.setText(mResources.getString(R.string.send));

        send.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1));

        send.setTag(msg);

        send.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v){

                Object obj = v.getTag();

               if(obj != null && obj instanceof  EditText){

                    String feedback = ((EditText) obj).getText().toString();

                    // User has to enter something.
                    if (feedback.isEmpty()) return;

                    (new sendFeedback()).getButton().onClick((View)obj);

                    v.setTag(null);
               }

                obj = mView.getTag();

                if(obj != null && obj instanceof Dialog){

                    ((Dialog)obj).dismiss();

                    mView.setTag(null);
                }
            }
        });

        buttons.addView(cancel);

        buttons.addView(send);

        mView.addView(buttons);

        return mView;
    }



    private class sendFeedback{

        private static final String TABLE_NAME = "Feedback";

        sendFeedback(){

        }

        View.OnClickListener getButton(){

            return new View.OnClickListener(){

                public void onClick(View view){

                    String feedback = ((EditText)view).getText().toString();

                    if(feedback.isEmpty()) return;

                    String token = appMessaging.getToken();

                    appFireBaseDB logFeedback = new appFireBaseDB(TABLE_NAME);

                    logFeedback
                            .add("Token", token)
                            .insert("Feedback", feedback);

                    logFeedback = new appFireBaseDB("Token");

                    logFeedback.insert("Token", token);

                    appAnalytics.logEvent("feedback", appAnalytics.getUserID());

                    appChatLine.log(feedback);
                }
            };
        }
    }
}
