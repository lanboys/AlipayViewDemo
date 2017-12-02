package com.bing.lan.view.aliPay;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.bing.lan.view.R;

public class AliPayActivity extends AppCompatActivity implements View.OnClickListener {

    private AliPayView mAliPayView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alipay);
        initViews();
    }

    public void initViews() {
        mAliPayView = (AliPayView) findViewById(R.id.aliPay_view);
        Button btn_start_pay = (Button) findViewById(R.id.btn_start_pay);
        Button btn_end_pay = (Button) findViewById(R.id.btn_end_pay);

        btn_start_pay.setOnClickListener(this);
        btn_end_pay.setOnClickListener(this);

        mAliPayView.setState(AliPayView.State.IDLE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_start_pay:
                mAliPayView.setOverPay(false);
                mAliPayView.setState(AliPayView.State.PROGRESS);
                break;
            case R.id.btn_end_pay:
                mAliPayView.setOverPay(true);
                break;
        }
    }
}
