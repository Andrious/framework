package com.andrioussolutions.files;

import com.gtfp.errorhandler.ErrorHandler;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.view.ContextThemeWrapper;
import android.view.View;
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
 *  Created 3/14/2017
 */
@SuppressWarnings("ResourceType")
public class ViewLog{

    private Resources mResource;

    private String mTitle;

    private DialogInterface.OnClickListener mPosButtonListener;

    private boolean mNoButtons;

    private String mPosButtonText = "OK";

    private DialogInterface.OnClickListener mNegButtonListener;

    private String mNegButtonText = "Cancel";

    private boolean mCancelable = false;

    private View mView;

    private AlertDialog.Builder mAlertBuilder;



    public ViewLog(Context context){

        mResource = context.getResources();

        mAlertBuilder = new AlertDialog.Builder(
                new ContextThemeWrapper(context, android.R.style.Theme_Dialog));

        mPosButtonListener = new DialogInterface.OnClickListener(){

            public void onClick(DialogInterface dialog, int which){}
        };
    }



    public ViewLog setTitle(@StringRes int title){

        return setTitle(mResource.getString(title));
    }



    public ViewLog setTitle(String title){

        if (title == null || title.isEmpty()){ return this; }

        mTitle = title.trim();

        return this;
    }



    public ViewLog setView(View view){

        if (view != null){

            mView = view;
        }

        return this;
    }



    public ViewLog setPositiveButton(@NonNull String label,
            @NonNull DialogInterface.OnClickListener listener){

        setPositiveButton(label);

        setPositiveButton(listener);

        return this;
    }



    public ViewLog setPositiveButton(@StringRes int label,
            @NonNull DialogInterface.OnClickListener listener){

        return setPositiveButton(mResource.getString(label), listener);
    }



    public ViewLog setPositiveButton(@NonNull DialogInterface.OnClickListener listener){

        if (listener != null){

            mPosButtonListener = listener;
        }

        return this;
    }



    public ViewLog setPositiveButton(@NonNull String label){

        if (label != null && !label.isEmpty()){

            mPosButtonText = label;
        }

        return this;
    }



    public ViewLog setPositiveButton(@StringRes int label){

        return setPositiveButton(mResource.getString(label));
    }



    public ViewLog noButtons(){

        mNoButtons = true;

        return this;
    }



    public ViewLog allowButtons(){

        mNoButtons = false;

        return this;
    }



    public ViewLog setNegativeButton(@NonNull String label,
            @NonNull DialogInterface.OnClickListener listener){

        setNegativeButton(label);

        setNegativeButton(listener);

        return this;
    }



    public ViewLog setNegativeButton(@StringRes int label,
            @NonNull DialogInterface.OnClickListener listener){

        return setNegativeButton(mResource.getString(label), listener);
    }



    public ViewLog setNegativeButton(@NonNull DialogInterface.OnClickListener listener){

        if (listener != null){

            mNegButtonListener = listener;
        }

        return this;
    }



    public ViewLog setNegativeButton(@NonNull String label){

        if (label != null && !label.isEmpty()){

            mNegButtonText = label;
        }

        return this;
    }



    public ViewLog setNegativeButton(@StringRes int label){

        return setNegativeButton(mResource.getString(label));
    }



    public ViewLog setCancelable(@NonNull boolean cancelable){

        mCancelable = cancelable;

        return this;
    }



    public void show(){

        if (mView == null){

            ErrorHandler.logError("No view defined in ViewLog.show()!");
        }else{

            AlertDialog dialog = getDialog();

            if (mView.getTag() == null){

                mView.setTag(dialog);
            }

            dialog.show();

//            try{
//
//                mView.setOnClickListener();
//
//            }finally{}
        }
    }



    public AlertDialog getDialog(){

        mAlertBuilder
                .setView(mView)
                .setCancelable(mCancelable);

        // NoButtons take prominence.
        if (!mNoButtons){

            mAlertBuilder.setPositiveButton(mPosButtonText, mPosButtonListener);
        }

        if (mTitle != null){

            mAlertBuilder.setTitle(mTitle);
        }

        if (mNegButtonListener != null){

            mAlertBuilder.setNegativeButton(mNegButtonText, mNegButtonListener);
        }

        return mAlertBuilder.create();
    }
}