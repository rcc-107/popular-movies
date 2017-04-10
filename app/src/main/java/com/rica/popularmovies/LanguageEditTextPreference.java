package com.rica.popularmovies;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by Rica on 4/6/2017.
 */

public class LanguageEditTextPreference extends EditTextPreference{

    private int mMinLength;
    private final int MIN_LANGUAGE_LENGTH = 2;

    public LanguageEditTextPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta = context.getTheme().obtainStyledAttributes(attrs,R.styleable.LanguageEditTextPreference,0,0);
        try{
            mMinLength = ta.getInteger(R.styleable.LanguageEditTextPreference_minLength,MIN_LANGUAGE_LENGTH);
            Log.d("MinLength: ",Integer.toString(mMinLength));
        }finally {
            ta.recycle();
        }
    }

    @Override
    protected void showDialog(Bundle state) {
        super.showDialog(state);
        EditText editText = getEditText();
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Dialog d = getDialog();
                if(d instanceof AlertDialog) {
                    Button positive = ((AlertDialog) d).getButton(AlertDialog.BUTTON_POSITIVE);
                    if(s.length() < mMinLength) {
                        positive.setEnabled(false);
                    }else{
                        positive.setEnabled(true);
                    }
                }
            }
        });
    }
}
