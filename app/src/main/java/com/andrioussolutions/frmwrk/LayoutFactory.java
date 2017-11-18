package com.andrioussolutions.frmwrk;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
/**
 *  Copyright  2015  Andrious Solutions Ltd.
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
 * Created  1/25/2017.
 */

public class LayoutFactory extends LayoutInflater implements LayoutInflater.Factory2{

//    LayoutInflater mPhoneLayoutInflater;

    protected LayoutFactory(Context context){

        super(context);

//        mPhoneLayoutInflater = ((Activity)context).getWindow().getLayoutInflater();
    }

    protected LayoutFactory(LayoutInflater original, Context newContext){

        super(original, newContext);
    }

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs){

        if(!getContext().equals(context)) return null;

        View layout;

        try{

//            layout = this.onCreateView(parent, name, attrs);

            layout = this.onCreateView(name, attrs);

//            mPhoneLayoutInflater.onCreateView(name, attrs);

        }catch (ClassNotFoundException ex){

            layout = null;

        }catch (Exception ex){

            layout = null;
        }

        return layout;
    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs){

        if(!getContext().equals(context)) return null;

        View layout;

        try{

//            layout = this.createView(name, null, attrs);

            layout = this.onCreateView(name, attrs);

        }catch (ClassNotFoundException ex){

            layout = null;

        }catch (Exception ex){

            layout = null;
        }

        return layout;
    }

    @Override
    public LayoutInflater cloneInContext(Context newContext){

        return LayoutInflater.from(newContext);
    }
}
