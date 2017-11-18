package com.andrioussolutions.ui;

/**
 * Copyright (C) 2017 Andrious Solutions Ltd.
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

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
/**
 * NumberClicker Makes a layout with three components, like this:
 * <pre>
 * +-------------------+ +-------------------+
 * |                   | |          +        |
 * |         3        | |-------------------|
 * |                   | |-------------------|
 * |                   | |          -        |
 * |-------------------| |-------------------|
 * </pre>
 * The + and - buttons are set to increment and decrement the number in the textfield!
 *
 * @author Ian Darwin
 */
public class NumberPickerJava extends ViewGroup{

    EditText number;

    Button plus, minus;

    private NumberPickerLogic logic;




    public NumberPickerJava(Context context){

        this(context, null);
    }




    public NumberPickerJava(Context context, AttributeSet attrs){

        super(context, attrs);

        addView(this.number = new EditText(context));

        number.setTextSize(3, 24f);

        number.setGravity(Gravity.CENTER);

        logic = new NumberPickerLogic(number);

        addView(this.plus = new Button(context));

        plus.setText("+");

        addView(this.minus = new Button(context));

        minus.setText("-");

        logic.setValue(1);

        this.plus.setOnClickListener(new OnClickListener(){

            @Override
            public void onClick(View v){

                logic.increment();
            }
        });
        this.minus.setOnClickListener(new OnClickListener(){

            @Override
            public void onClick(View v){

                logic.decrement();
            }
        });
    }




    /**
     * Compute sizes and positions of subfields.
     */
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom){

        int layoutWidth = right - left;

        int layoutHeight = bottom - top;

        int halfX = layoutWidth / 2;

        int halfY = layoutHeight / 2;

        number.layout(0 + number.getPaddingLeft(), 0 + number.getPaddingTop(),
                halfX - number.getPaddingRight(), layoutHeight - number.getPaddingBottom());

        plus.layout(halfX + plus.getPaddingLeft(), 0 + plus.getPaddingTop(),
                right - plus.getPaddingRight(), halfY - plus.getPaddingTop());

        minus.layout(halfX + minus.getPaddingLeft(), halfY + minus.getPaddingTop(),
                right - minus.getPaddingRight(), layoutHeight - minus.getPaddingBottom());
    }
}