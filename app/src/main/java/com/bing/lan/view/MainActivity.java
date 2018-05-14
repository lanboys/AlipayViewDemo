package com.bing.lan.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.bing.lan.view.alipay.AliPayActivity;
import com.bing.lan.view.label.LabelButtonActivity;
import com.bing.lan.view.loading.LoadingActivity;
import com.bing.lan.view.screenLock.ScreenLockActivity;
import com.bing.lan.view.scrollHeader.ScrollHeaderActivity;
import com.bing.lan.view.youzan.YouZanActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    public void onLabel(View view) {
        Intent intent = new Intent(this, LabelButtonActivity.class);
        startActivity(intent);
    }

    public void onAliPay(View view) {
        Intent intent = new Intent(this, AliPayActivity.class);
        startActivity(intent);
    }

    public void onYouZan(View view) {
        Intent intent = new Intent(this, YouZanActivity.class);
        startActivity(intent);
    }

    public void onLoading1(View view) {
        Intent intent = new Intent(this, LoadingActivity.class);
        startActivity(intent);
    }

    public void onScreenLock(View view) {
        Intent intent = new Intent(this, ScreenLockActivity.class);
        startActivity(intent);
    }

    public void onScrollHead(View view) {
        Intent intent = new Intent(this, ScrollHeaderActivity.class);
        startActivity(intent);
    }
}
