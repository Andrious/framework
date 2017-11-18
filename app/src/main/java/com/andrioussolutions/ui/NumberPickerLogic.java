package com.andrioussolutions.ui;

import android.widget.EditText;
/**
 * Copyright (C) 2017  Andrious Solutions Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Created  29 Jun 2017
 */
public class NumberPickerLogic{

    int minimum = 0;

    int maximum = Integer.MAX_VALUE;

    EditText number;




    public NumberPickerLogic(EditText number){

        this(number, 0, Integer.MAX_VALUE);
    }




    public NumberPickerLogic(EditText number, int minimum, int maximum){

        super();

        this.number = number;

        this.minimum = minimum;

        this.maximum = maximum;
    }




    public void increment(){

        final int newValue = clamp(getValue() + 1);

        setValue(newValue);
    }




    public void decrement(){

        final int newValue = clamp(getValue() - 1);

        setValue(newValue);
    }




    /** Ensure that the value to be set falls within the allowable range */
    int clamp(int newValue){

        if (newValue < minimum){

            newValue = minimum;
        }

        if (newValue > maximum){

            newValue = maximum;
        }

        return newValue;
    }




    /** Return the integer value of the clicker. */
    public int getValue(){

        return Integer.parseInt(number.getText().toString());
    }




    /** Force the value */
    public void setValue(int value){

        number.setText(Integer.toString(value));
    }

}
