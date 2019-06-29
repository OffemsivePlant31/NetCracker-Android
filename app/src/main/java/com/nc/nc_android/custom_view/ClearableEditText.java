package com.nc.nc_android.custom_view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.nc.nc_android.R;



public class ClearableEditText extends FrameLayout{

    EditText etInput;
    Button btnClear;

    OnClearButtonClickListener onClearButtonClickListener;
    OnTextChangeListener onTextChangeListener;

    public ClearableEditText(@NonNull Context context) {
        super(context);
        init(null);
    }

    public ClearableEditText(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public ClearableEditText(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs){

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_clearable_edittext, this, true);

        etInput = findViewById(R.id.etInput);
        btnClear = findViewById(R.id.btnClear);

        btnClear.setOnClickListener(v -> {
            if(onClearButtonClickListener != null)  {
                onClearButtonClickListener.handle();
            }
            etInput.setText("");
        });
        etInput.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override public void afterTextChanged(Editable s) {
                if(onTextChangeListener != null)  {
                    onTextChangeListener.handle(s.toString());
                }
            }
        });

        if(attrs != null){
            TypedArray a = getContext().getTheme().obtainStyledAttributes(
                    attrs,
                    R.styleable.ClearableEditText,
                    0, 0);

            try {
                etInput.setTextColor(a.getColor(R.styleable.ClearableEditText_textColor, Color.BLACK));
                etInput.setHint(a.getString(R.styleable.ClearableEditText_hint));
                etInput.setTextSize(a.getDimension(R.styleable.ClearableEditText_textSize, 21));
            } finally {
                a.recycle();
            }
        }

    }


    public String getText(){
        return etInput.getText().toString();
    }

    public boolean isEmpty(){
        return etInput.getText().toString().length() == 0;
    }




    public interface OnClearButtonClickListener{
        void handle();
    }

    public interface OnTextChangeListener{
        void handle(String text);
    }

}
