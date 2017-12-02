package com.bing.lan.view.label;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.bing.lan.view.R;

public class LabelButtonActivity extends AppCompatActivity implements View.OnClickListener {

    LabelButton lab;
    private Button btn_start_pay, btn_end_pay;
    private static final String TAG = "-->520";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_label_button);

        lab = (LabelButton) findViewById(R.id.lab);
        lab.setOnLabelClickListener(new LabelButton.OnLabelClickListener() {
            @Override
            public void onLabelClick(View v, boolean isSelected) {
                Log.e(TAG, "点击了LabelButton, isSelected: " + isSelected);

                Toast.makeText(LabelButtonActivity.this, "点击了LabelButton, isSelected: " + isSelected, Toast.LENGTH_SHORT).show();
            }
        });

        btn_start_pay = (Button) findViewById(R.id.btn_start_pay);
        btn_end_pay = (Button) findViewById(R.id.btn_end_pay);

        btn_start_pay.setOnClickListener(this);
        btn_end_pay.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_start_pay:
                lab.setButtonEnabled(true);
                break;
            case R.id.btn_end_pay:
                lab.setButtonEnabled(false);
                break;
        }
    }
}
