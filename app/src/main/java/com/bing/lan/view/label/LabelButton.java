package com.bing.lan.view.label;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.bing.lan.view.R;

/**
 * Created by 520 on 2017/7/7.
 */

public class LabelButton extends FrameLayout {

    View shade_view;
    View button;

    private static final String TAG = "-->LabelButton";

    public LabelButton(@NonNull Context context) {
        super(context);
        initView(context, null);
    }

    public LabelButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        inflate(context, R.layout.view_label_button, this);

        shade_view = findViewById(R.id.shade_view);
        button = findViewById(R.id.button);
    }

    //boolean enabled;
    //
    //@Override
    //public void setEnabled(boolean enabled) {
    //    super.setEnabled(enabled);
    //    //this.enabled = enabled;

    //}
    //
    //@Override
    //public boolean dispatchTouchEvent(MotionEvent ev) {
    //    return super.dispatchTouchEvent(ev);
    //}
    //
    //@Override
    //public boolean onInterceptTouchEvent(MotionEvent ev) {
    //
    //    //if (!enabled) {
    //    //    return true;
    //    //}
    //    return super.onInterceptTouchEvent(ev);
    //}

    public void setButtonEnabled(boolean enabled) {
        if (button == null) {
            return;
        }
        button.setEnabled(enabled);
        //shade_view.setVisibility(enabled ? View.GONE : View.VISIBLE);
    }

    public void setOnLabelClickListener(@Nullable final OnLabelClickListener l) {
        if (button == null) {
            return;
        }
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                button.setSelected(!button.isSelected());

                if (l != null) {
                    l.onLabelClick(v, button.isSelected());
                }
            }
        });
    }

    public interface OnLabelClickListener {

        void onLabelClick(View v, boolean isSelected);
    }
}
