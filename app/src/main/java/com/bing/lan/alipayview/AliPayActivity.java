package com.bing.lan.alipayview;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class AliPayActivity extends AppCompatActivity implements View.OnClickListener {

    private AlipayView alipay_view;
    private Button btn_start_pay, btn_end_pay, btn_new_activity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alipay);
        initViews();
    }

    public void initViews() {
        alipay_view = (AlipayView) findViewById(R.id.alipay_view);
        btn_start_pay = (Button) findViewById(R.id.btn_start_pay);
        btn_end_pay = (Button) findViewById(R.id.btn_end_pay);
        btn_new_activity = (Button) findViewById(R.id.btn_new_activity);

        btn_start_pay.setOnClickListener(this);
        btn_end_pay.setOnClickListener(this);
        btn_new_activity.setOnClickListener(this);

        alipay_view.setState(AlipayView.State.IDLE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_start_pay:
                alipay_view.setOverPay(false);
                alipay_view.setState(AlipayView.State.PROGRESS);
                break;
            case R.id.btn_end_pay:
                alipay_view.setOverPay(true);
                break;
            case R.id.btn_new_activity:
                //Intent intent = new Intent(this, LabelButtonActivity.class);
                Intent intent = new Intent(this, YouZanActivity.class);
                startActivity(intent);
                break;
        }
    }
}
