package com.andrioussolutions.frmwrk.db;

import com.andrioussolutions.frmwrk.appModel;

import android.content.ContentValues;
/**
 *  Copyright  2016  Andrious Solutions Ltd.
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
 * Created  8/15/2016.
 */
public interface dbRecValues {

     ContentValues bindRecValues(appModel.dataItem itemToDo);// {

//        // Assign values for each row.
//        mRecValues.put("ToDoItem", itemToDo.getItemName());
//
//        long epochTime = itemToDo.getDueDateInEpoch();
//
//        mRecValues.put("ToDoDateTime", ToDoItem.EpochToDateTime(epochTime));
//
//        mRecValues.put("ToDoDateTimeEpoch", epochTime);
//
//        mRecValues.put("ToDoReminderEpoch", itemToDo.getReminderEpoch());
//
//        mRecValues.put("ToDoReminderChk", itemToDo.getReminderChk());
//
//        mRecValues.put("ToDoLEDColor", itemToDo.getLEDColor());
//
//        mRecValues.put("ToDoFired", itemToDo.hasFired());
//
//        mRecValues.put("ToDoSetAlarm", itemToDo.setAlarm());
//
//        mRecValues.put("deleted", itemToDo.isDeleted());
//
//        return mRecValues;
//    }


      void bindRecValues(String columnName, String value);//{
//
//        if (columnName.equals("ToDoItem")){
//
//            mRecValues.put(columnName, value);
//
//        } else if (columnName.equals("ToDoDateTime")){
//
//            mRecValues.put(columnName, value);
//
//        } else if (columnName.equals("ToDoDateTimeEpoch")){
//
//            mRecValues.put(columnName, setBindRecLong(value));
//
//        } else if (columnName.equals("ToDoTimeZone")){
//
//            mRecValues.put(columnName, setBindRecInt(value));
//
//        } else if (columnName.equals("ToDoReminderEpoch")){
//
//            mRecValues.put(columnName, setBindRecLong(value));
//
//        } else if (columnName.equals("ToDoReminderChk")){
//
//            mRecValues.put(columnName, setBindRecInt(value));
//
//        } else if (columnName.equals("ToDoLEDColor")){
//
//            mRecValues.put(columnName, setBindRecInt(value));
//
//        } else if (columnName.equals("ToDoFired")){
//
//            mRecValues.put(columnName, setBindRecInt(value) != 0);
//
//        } else if (columnName.equals("ToDoSetAlarm")){
//
//            mRecValues.put(columnName, setBindRecInt(value) != 0);
//
//        } else if (columnName.equals("deleted")){
//
//            mRecValues.put(columnName, setBindRecInt(value) != 0);
//        }
//    }


     boolean ifNewRec();//{
//
//        String item = mRecValues.getAsString("ToDoItem");
//
//        Long time = mRecValues.getAsLong("ToDoDateTimeEpoch");
//
//        Long reminder = mRecValues.getAsLong("ToDoReminderEpoch");
//
//        Cursor records = getRecs("TRIM(ToDoItem) = '" + item.trim() + "' AND ToDoDateTimeEpoch = " + time);
//
//        boolean newRec = records.getCount() == 0;
//
//        records.close();
//
//        return newRec;
//    }

}
